# eAcct 배포 가이드 — Ubuntu 20.04.2 LTS (버전업 신버전)

> 대상: Java 11→21 / Spring Boot 2→3.3.5, Vue CLI 3→5 업그레이드가 완료된 신버전 소스.
> 전제: 기존 개발서버를 **디스크 클론**하여 새 Ubuntu 서버로 이전한 상태. OS·디렉토리·기존 배포 구조는 그대로 있으나, **런타임(Java/Node)은 구버전**일 가능성이 높음.
> DB: 기존 Oracle `197.200.11.145:1543/MATRIX` 그대로 사용. 실행 방식: **systemd 서비스**.

---

## ⚠️ 가장 먼저 — 클론 서버의 구버전 런타임 확인

신버전 코드는 **Java 21**, **Node 18.20.8**을 요구합니다. 클론 서버에는 구버전이 깔려 있을 수 있으니 먼저 확인하세요.

```bash
java -version        # 11.x 이면 21로 업그레이드 필요
node -v              # 18 미만이면 업그레이드 필요
nginx -v             # nginx 설치 여부
systemctl --version  # systemd 사용 가능 확인
```

---

## 1단계. Java 21 설치

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk      # Ubuntu 20.04 기본 저장소에 없으면 아래 PPA 사용

# (기본 저장소에 21이 없을 경우)
# sudo add-apt-repository -y ppa:openjdk-r/ppa && sudo apt update && sudo apt install -y openjdk-21-jdk

# 기본 java를 21로 전환 (구버전과 공존 시)
sudo update-alternatives --config java     # 목록에서 21 선택
java -version                               # "21" 확인
```

> 빌드를 새 서버에서 직접 하지 않고, **Windows/CI에서 빌드한 jar만 올릴 경우**에도 실행에는 Java 21 런타임(JRE)이 반드시 필요합니다.

---

## 2단계. Node 18.20.8 설치 (nvm 권장)

기존 Node와 충돌을 피하려면 nvm으로 격리 설치하는 것이 안전합니다.

```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
source ~/.bashrc
nvm install 18.20.8
nvm use 18.20.8
nvm alias default 18.20.8
node -v        # v18.20.8 확인
```

> `package.json`의 `volta.node`가 18.20.8로 고정되어 있습니다. Vue CLI 5(webpack 5)는 이 버전에서 정상 빌드되며, 구버전 우회 옵션(`--openssl-legacy-provider`)은 **불필요**합니다.

---

## 3단계. 백엔드 빌드 & 실행 (systemd)

### 3-1. 빌드

```bash
cd /data/apps/ije-eacct/ije-eacct-back     # 실제 소스 경로에 맞게 조정
chmod +x gradlew
./gradlew clean build -x test              # 테스트 제외 빌드
# 산출물: build/libs/eacct-back.jar
```

> QueryDSL Q타입은 `compileJava` 시 `build/generated/sources/`에 자동 생성됩니다.
> 테스트 포함 빌드는 `./gradlew build` (레거시 제외 테스트는 build.gradle에서 이미 제외됨).

### 3-2. 배포 디렉토리에 jar 배치

```bash
# 예시: 실행 기준 디렉토리를 /data/apps/ije-eacct 로 사용
sudo mkdir -p /data/apps/ije-eacct/{logs,app/uploads,app/decoded}
sudo cp build/libs/eacct-back.jar /data/apps/ije-eacct/eacct-back.jar
```

> **중요**: `application-linux.properties`의 경로가 상대경로(`./app/uploads`, `./app/decoded`, `./logs`)입니다.
> 따라서 systemd `WorkingDirectory`를 jar가 있는 디렉토리로 지정해야 업로드/로그가 올바른 위치에 생성됩니다.

### 3-3. systemd 서비스 등록

`/etc/systemd/system/eacct-back.service` 파일 생성 (아래 `eacct-back.service` 참고):

```bash
sudo cp eacct-back.service /etc/systemd/system/eacct-back.service
sudo systemctl daemon-reload
sudo systemctl enable eacct-back
sudo systemctl start eacct-back
sudo systemctl status eacct-back        # active (running) 확인
journalctl -u eacct-back -f             # 실시간 로그
```

### 3-4. DB 연결 확인 (기존 DB 그대로)

새 서버에서 기존 Oracle로 **네트워크 도달 가능 여부**만 확인하면 됩니다.

```bash
# 포트 도달 확인 (nc 없으면: sudo apt install -y netcat)
nc -zv 197.200.11.145 1543
# 또는
timeout 3 bash -c 'cat < /dev/null > /dev/tcp/197.200.11.145/1543' && echo OPEN || echo BLOCKED
```

> 막혀 있으면 새 서버 IP에 대한 **방화벽/Oracle Listener ACL** 허용이 필요합니다 (DB 관리자에게 요청).
> 접속 정보(`IJELOGIS` 계정)는 코드에 이미 설정되어 있어 수정 불필요합니다.

---

## 4단계. 프론트엔드 빌드 & Nginx 서빙

### 4-1. API URL 확인 (빌드 전 필수)

`npm run build`는 **production 모드**로 `.env.production`을 사용합니다. 현재 값:

```
VUE_APP_API_URL=http://eleceas.iljin.co.kr:19500
VUE_APP_MO_URL=http://eleceas.iljin.co.kr:19051
```

> 새 서버의 접속 도메인/IP가 다르면 `.env.production`의 `VUE_APP_API_URL`을 **백엔드가 실제로 노출되는 주소**로 수정 후 빌드하세요.
> (브라우저가 직접 호출하는 주소이므로 `localhost`가 아닌 외부에서 접근 가능한 주소여야 합니다.)

### 4-2. 빌드

```bash
cd /data/apps/ije-eacct/ije-eacct-front
npm install
npm run build          # 산출물: dist/
```

### 4-3. Nginx 설정

`/etc/nginx/sites-available/eacct` 생성 (아래 `nginx-eacct.conf` 참고) 후:

```bash
sudo cp nginx-eacct.conf /etc/nginx/sites-available/eacct
sudo ln -sf /etc/nginx/sites-available/eacct /etc/nginx/sites-enabled/eacct
sudo nginx -t          # 설정 검증
sudo systemctl reload nginx
```

> **history 모드 필수**: `try_files $uri $uri/ /index.html;` 가 없으면 새로고침 시 404가 발생합니다.

---

## 5단계. 최종 검증 체크리스트

- [ ] `systemctl status eacct-back` → active (running)
- [ ] `journalctl -u eacct-back` → DB 연결 에러 없음, 포트 19500 리슨
- [ ] `curl http://localhost:19500/actuator/health` → `{"status":"UP"}`
- [ ] 브라우저로 프론트 접속 (포트 19050) → 로그인 화면 표시
- [ ] ID/PW 로그인 성공 (SSO 제거됨 — ID/PW만 동작)
- [ ] 전표/목록 등 ag-Grid 화면 정상 렌더링
- [ ] 새로고침(F5) 시 404 안 나는지 (history 모드)
- [ ] 파일 업로드/다운로드 동작 (`app/uploads` 권한 확인)

---

## 참고: 기존(구버전) 서비스와의 충돌 방지

클론 서버에 **구버전 백엔드가 systemd/스크립트로 이미 떠 있을 수 있습니다.** 포트 19500 중복을 피하려면 기존 프로세스를 먼저 중지하세요.

```bash
sudo ss -ltnp | grep 19500          # 19500 점유 프로세스 확인
# 기존 서비스가 있으면 중지/비활성화
# sudo systemctl stop <기존서비스명> && sudo systemctl disable <기존서비스명>
```

기존 nginx 설정(`/etc/nginx/sites-enabled/`)에 구버전 root 경로가 잡혀 있을 수 있으니 함께 점검하세요.
