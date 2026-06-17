/**
 * [DRAFT] vue-cli 5 (webpack 5) 전환용 vue.config.js 초안 (2026-06-17)
 *
 * 적용 방법: vue-cli 5 설치 + npm run build 통과 확인 후
 *   이 파일 내용으로 기존 vue.config.js를 교체.
 *
 * 기존 대비 변경점:
 *  - 중복 `configureWebpack` 키 제거 (객체가 함수를 덮어써 해시 파일명 설정이 사문화돼 있었음)
 *  - 죽은 `ts-loader` 규칙 제거 (프로젝트에 .ts/.tsx 파일 0)
 *  - 수동 `HtmlWebpackPlugin` 제거 (vue-cli 5가 public/index.html 자동 주입)
 *  - 수동 postcss-loader+sass-loader 규칙 제거 (vue-cli 5 내장 CSS 파이프라인 사용;
 *    대신 package.json의 sass-loader를 webpack5 호환 버전(^12)으로 상향)
 *  - `transpileDependencies: [ansiRegex]` 제거 (ansi-regex 직접 의존 불필요; 필요 시 재추가)
 *  - alias `@`→src 는 vue-cli 5 기본 제공이나 명시 유지(안전)
 *  - runtimeCompiler 유지 (ag-grid 셀렌더러 Vue.extend({template}) 의존)
 */
const path = require('path');

module.exports = {
  productionSourceMap: process.env.NODE_ENV !== 'production', // 운영 배포 시 디버깅 close
  runtimeCompiler: true,
  // 빌드 시 eslint-loader 비활성화. 기존 코드는 airbnb v3 기준으로 작성됐는데
  // cli5가 요구하는 eslint 7/8과 호환되는 airbnb v6은 스타일 규칙이 엄격해
  // (max-len/indent/quotes 등) 빌드를 막는다. 빌드 툴체인 교체 단계에서는
  // 전체 재포맷을 보류하고 lint는 `npm run lint` 수동 실행으로 분리. (MIGRATION_PLAN §11.5)
  lintOnSave: false,
  filenameHashing: true,
  publicPath: '/',
  outputDir: path.resolve(__dirname, 'dist'),
  css: {
    loaderOptions: {
      // webpack5/css-loader는 SCSS의 루트절대경로 url(/img/...) (public 폴더 참조)을
      // 모듈로 해석하려다 실패한다. webpack4에선 그대로 두던 동작 → `/` 시작 URL은 해석 제외.
      css: {
        url: {
          filter: (url) => !url.startsWith('/'),
        },
      },
    },
  },
  chainWebpack: (config) => {
    config.resolve.alias.set('@', path.resolve(__dirname, 'src/'));
    // Vue 3 마이그레이션 빌드: vue → @vue/compat 별칭 + 전체 호환모드(MODE 2).
    // 기존 Vue 2 코드(Vue.use/Vue.prototype/$on 등)를 경고와 함께 동작시키며 점진 전환.
    config.resolve.alias.set('vue', '@vue/compat');
    config.resolve.alias.set('vue$', '@vue/compat'); // runtimeCompiler의 vue$ alias 덮어쓰기 방지
    config.module
      .rule('vue')
      .use('vue-loader')
      .tap((options) => {
        options.compilerOptions = {
          ...(options.compilerOptions || {}),
          compatConfig: { MODE: 2 },
        };
        return options;
      });
  },
  devServer: {
    port: process.env.VUE_APP_PORT,
    client: {
      overlay: {
        // 컴파일/빌드 에러는 계속 오버레이로 표시(유용)
        errors: true,
        warnings: false,
        // 런타임 에러 오버레이는 끔. vue-cli5(webpack-dev-server4)가 새로 추가한 기능으로,
        // 앱 전반의 기존 미처리 rejection/latent 에러(마이그레이션 무관)를 무더기로 노출해
        // 검증을 방해함. vue-cli3와 동일하게 런타임 에러는 콘솔로만 확인. (MIGRATION_PLAN 참고)
        runtimeErrors: false,
      },
    },
  },
};
