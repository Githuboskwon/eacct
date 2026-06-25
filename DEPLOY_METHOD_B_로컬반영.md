# 배포 가이드 — 방법 B: 로컬 소스를 서버에 직접 반영

> 로컬 PC의 신버전 소스를 서버(`innoacc2dev`)의 `/data/apps/ije-eacct/`에 직접 전송해 반영하는 방법.
> 서버는 **dev 프로파일**, 기존 systemd 서비스(`ije-eacct-back.service`) 재사용, Pinpoint 3.0.1 유지.

---

## 0. 사전 정보 (서버 환경 — 이미 파악됨)

| 항목 | 값 |
|------|-----|
| 서버 접속 | `shlee@innoacc2dev` (sudo 가능) |
| 배포 경로 | `/data/apps/ije-eacct/{ije-eacct-back, ije-eacct-front}` |
| 소유권 | `ije-eacct-front`, `ije-eacct-back/build` = **root** / 나머지 = iljinadm → **작업 시 sudo 필요** |
| 백엔드 jar | `eacct-back.jar` 는 **심볼릭 링크** → `ije-eacct-back/build/libs/eacct-back.jar` (빌드하면 자동 반영, 따로 복사 불필요) |
| 프로파일 | `dev` (DB: `197.200.11.40:1541/TEST`) |
| 현재 서버 런타임 | Java 11, Node 16 → **업그레이드 필요** |

---

## 두 가지 방식 — 먼저 선택

### 【방식 B-1】 소스를 전송 → **서버에서 빌드** (권장)
- 서버에 **Java 21 + Node 18.20.8** 설치 필요
- 전송량 적음(소스만), 운영 환경에서 빌드 → 가장 안전
- node_modules/build/dist는 전송 제외(서버에서 생성)

### 【방식 B-2】 로컬에서 빌드 → **산출물만 전송**
- **로컬 PC에 Java 21 + Node 18.20.8** 필요 (빌드용)
- 서버엔 Java 21 **JRE만** 있으면 됨(백엔드 실행용), Node 불필요
- `dist`(프론트), `eacct-back.jar`(백엔드)만 전송

> 둘 중 하나만 따라가면 됩니다. 아래는 각 방식별 전체 절차입니다.

---

# 방식 B-1 — 서버에서 빌드 (권장)

## 1. 서버 런타임 업그레이드 (서버에서)
```bash
# Java 21
sudo add-apt-repository -y ppa:openjdk-r/ppa
sudo apt update && sudo apt install -y openjdk-21-jdk
sudo update-alternatives --config java        # java-21 선택
java -version

# Node 18.20.8 (nvm)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
source ~/.bashrc
nvm install 18.20.8 && nvm alias default 18.20.8
node -v
```

## 2. 로컬에서 소스 압축 → 서버로 전송 (로컬 PC에서)
```bash
# 로컬 workspace 경로에서 실행. node_modules/dist/build/.git 제외하고 압축
# (Windows10+ 기본 tar, 또는 git-bash에서 실행)
tar --exclude=node_modules --exclude=dist --exclude=build --exclude=.git \
    -czvf eacct-src.tar.gz ije-eacct-back ije-eacct-front

# 서버 홈으로 전송
scp eacct-src.tar.gz shlee@innoacc2dev:~/
```

## 3. 서버에서 압축 해제 → 배포 위치에 소스 반영 (서버에서)
```bash
cd ~
mkdir -p ~/eacct-new && tar -xzvf eacct-src.tar.gz -C ~/eacct-new

# 기존 소스 백업 후 신버전 src/설정 반영 (배포 폴더가 root 소유 → sudo)
# 프론트
sudo cp -r ~/eacct-new/ije-eacct-front/src        /data/apps/ije-eacct/ije-eacct-front/
sudo cp    ~/eacct-new/ije-eacct-front/package.json /data/apps/ije-eacct/ije-eacct-front/
sudo cp    ~/eacct-new/ije-eacct-front/vue.config.js /data/apps/ije-eacct/ije-eacct-front/ 2>/dev/null
sudo cp -r ~/eacct-new/ije-eacct-front/public      /data/apps/ije-eacct/ije-eacct-front/
sudo cp -r ~/eacct-new/ije-eacct-front/.env*       /data/apps/ije-eacct/ije-eacct-front/ 2>/dev/null

# 백엔드 (src, build.gradle 등)
sudo cp -r ~/eacct-new/ije-eacct-back/src         /data/apps/ije-eacct/ije-eacct-back/
sudo cp    ~/eacct-new/ije-eacct-back/build.gradle /data/apps/ije-eacct/ije-eacct-back/
```
> 통째로 폴더를 갈아끼우면 더 간단하지만, uploads·설정 등 운영 데이터 보존을 위해 src/설정만 덮는 걸 권장합니다.

## 4. 프론트 빌드 (서버에서)
```bash
cd /data/apps/ije-eacct/ije-eacct-front

# 빌드 전: .env.development 의 VUE_APP_API_URL 확인/수정
#   기존 운영 도메인이 eleceasdev.iljin.co.kr 이므로 그에 맞게:
#   VUE_APP_API_URL=http://eleceasdev.iljin.co.kr:19500
sudo nano .env.development        # 필요시 수정

# 기존 node_modules 잔재 제거 후 클린 설치 (ERESOLVE 방지)
sudo rm -rf node_modules package-lock.json
sudo npm install

# dist 백업 후 빌드 (빌드가 dist 자동 갱신)
sudo tar -cvf "backup/dist-$(date +%Y-%m-%d.%H%M%S).tar" dist
sudo npm run build:development     # 기존 빌드 모드에 맞게 (development 또는 staging)
```

## 5. 백엔드 빌드 + 재기동 (서버에서)
```bash
cd /data/apps/ije-eacct/ije-eacct-back
sudo chmod +x gradlew
sudo ./gradlew clean build -x test       # build/libs/eacct-back.jar 생성 (심볼릭 링크 자동 반영)

# 서비스 재시작 (기존 unit 그대로 — 프로파일 dev, Pinpoint 포함)
sudo systemctl restart ije-eacct-back
sudo systemctl status ije-eacct-back
journalctl -u ije-eacct-back -f          # DB연결/포트 리슨 확인
```

---

# 방식 B-2 — 로컬에서 빌드 후 산출물만 전송

## 1. 서버에 Java 21 설치 (실행용 JRE)
```bash
sudo add-apt-repository -y ppa:openjdk-r/ppa
sudo apt update && sudo apt install -y openjdk-21-jdk
sudo update-alternatives --config java
java -version
```
> 이 방식은 서버에서 빌드 안 하므로 Node는 설치 불필요.

## 2. 로컬에서 빌드 (로컬 PC — Java 21 + Node 18.20.8 필요)
```bash
# 프론트
cd ije-eacct-front
# .env.development 의 VUE_APP_API_URL 을 서버 접속 주소로 설정
#   예: VUE_APP_API_URL=http://eleceasdev.iljin.co.kr:19500
rm -rf node_modules package-lock.json
npm install
npm run build:development          # dist 생성

# 백엔드
cd ../ije-eacct-back
./gradlew clean build -x test      # build/libs/eacct-back.jar 생성
```

## 3. 산출물 전송 (로컬 → 서버)
```bash
# 프론트 dist 압축 전송
cd ije-eacct-front
tar -czvf dist.tar.gz dist
scp dist.tar.gz shlee@innoacc2dev:~/

# 백엔드 jar 전송
scp ../ije-eacct-back/build/libs/eacct-back.jar shlee@innoacc2dev:~/
```

## 4. 서버에서 배포 위치에 반영 (서버에서)
```bash
cd ~
# 프론트 dist 교체
tar -xzvf dist.tar.gz                  # ~/dist 생성
cd /data/apps/ije-eacct/ije-eacct-front
sudo tar -cvf "backup/dist-$(date +%Y-%m-%d.%H%M%S).tar" dist    # 기존 백업
sudo rm -rf dist
sudo cp -r ~/dist /data/apps/ije-eacct/ije-eacct-front/dist

# 백엔드 jar 교체 (심볼릭 링크 대상 경로에 직접)
sudo systemctl stop ije-eacct-back
sudo cp /data/apps/ije-eacct/ije-eacct-back/build/libs/eacct-back.jar \
        /data/apps/ije-eacct/ije-eacct-back/build/libs/eacct-back.jar.bak_$(date +%Y%m%d)
sudo cp ~/eacct-back.jar /data/apps/ije-eacct/ije-eacct-back/build/libs/eacct-back.jar
sudo systemctl start ije-eacct-back
journalctl -u ije-eacct-back -f
```

---

## 공통: 최종 검증 (두 방식 동일)
```bash
curl http://localhost:19500/actuator/health      # {"status":"UP"}
```
- [ ] 브라우저 접속 → 로그인(ID/PW, SSO 없음) 성공
- [ ] ag-Grid 목록/전표 화면 정상
- [ ] F5 새로고침 시 404 안 남 (nginx history 모드)
- [ ] 파일 업로드/다운로드 (`/data/apps/ije-eacct/uploads`)
- [ ] Pinpoint 대시보드에 `ije-eacct-dev2` 표시

## Java 21 전환 주의 — 모듈 접근 에러
백엔드 기동 로그에 `InaccessibleObjectException` 이 보이면, 기존 `ije-eacct-back.service`의 `java` 뒤에 추가:
```
--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED
```
(먼저 그냥 돌려보고, 에러 날 때만 추가)

## 롤백
문제 시 백업으로 복구:
```bash
# 프론트
cd /data/apps/ije-eacct/ije-eacct-front
sudo rm -rf dist && sudo tar -xvf backup/dist-<백업일시>.tar
# 백엔드
sudo cp .../build/libs/eacct-back.jar.bak_<날짜> .../build/libs/eacct-back.jar
sudo systemctl restart ije-eacct-back
```

---

## 전송 도구 참고
- **scp**: Windows 10+ 기본 포함(OpenSSH), git-bash에도 있음. 위 명령 그대로 사용.
- **GUI 선호 시**: WinSCP / FileZilla(SFTP)로 `~/`에 올린 뒤, 서버에서 sudo로 배포 위치에 복사.
- **rsync**(있으면 더 깔끔): `rsync -avz --exclude node_modules --exclude dist ije-eacct-front/ shlee@innoacc2dev:~/eacct-new/ije-eacct-front/`
