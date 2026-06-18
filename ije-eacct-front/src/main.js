// import 'buefy/dist/buefy.css';
// import 'bulma-o-steps/bulma-steps.sass';
import Buefy from 'buefy';
import Vue, { createApp, configureCompat } from 'vue';
import swal from 'sweetalert2';

// 전역 MODE 2(Vue2 호환) 유지 — main.js의 전역 Vue.prototype/use/filter/component가
// MODE 2 전역 API에 의존하므로 MODE 3 전역은 부팅 불가. element-plus 정상화는
// 전역 MODE3 + main.js app-level 전환 + 필터 처리까지 묶은 별도 작업으로(§18).
configureCompat({ ATTR_FALSE_VALUE: false });
import createBus from '@/libs/eventBus';
import VueCookie from 'vue-cookie';
import VueSweetalert2 from 'vue-sweetalert2';
import moment from 'moment';
import VueMomentJS from 'vue-momentjs';
import axios from 'axios';
import App from './App.vue';
import router from './router';
import store from './store';
import i18n from './i18n';
import $ from 'jquery';

// Polyfills
import 'core-js/stable';
import 'regenerator-runtime/runtime';

import './assets/element-variables.scss';
import './assets/common.scss';

import {LicenseManager} from "ag-grid-enterprise";

LicenseManager.setLicenseKey("CompanyName=iljin cns,LicensedApplication=ijcns,LicenseType=SingleApplication,LicensedConcurrentDeveloperCount=1,LicensedProductionInstancesCount=0,AssetReference=AG-013981,ExpiryDate=3_March_2022_[v2]_MTY0NjI2NTYwMDAwMA==79ca3f5c5621088302aec8ce6faf7207");

// element-ui → element-plus (Vue 3). 컴포넌트 전역 등록은 createApp 이후 app.use(파일 하단).
import ElementPlus, { ElMessage, ElMessageBox, ElNotification, ElLoading } from 'element-plus';
import 'element-plus/dist/index.css';
import elLocaleKo from 'element-plus/es/locale/lang/ko';
import * as ElementPlusIcons from '@element-plus/icons-vue';

/**
 * ag-grid License Manage
 * init
 */
moment.locale('ko')

Vue.prototype.$http = axios;
Vue.prototype.$http.defaults.withCredentials = true;
Vue.prototype.$http.defaults.baseURL = process.env.VUE_APP_API_URL;
Vue.prototype.$http.defaults.timeout = 1800000;
Vue.prototype.$http.defaults.headers = {
  'Access-Control-Allow-Origin': '*',
  'Content-type': 'application/json',
};

/**
 * * Buefy 프로토타입 생성자 세팅 옵션 공식문서
 * @link https://buefy.org/documentation/constructor-options
 * */
Vue.use(Buefy, {
  //Default modal canCancel attribute	Boolean, Array : false, escape, x, outside, button
  //제어할 수 있는 부분을 선언 버튼 과 X 아이콘, esc
  defaultModalCanCancel: ['button', 'x', 'escape'],
});

Vue.use(VueSweetalert2);
Vue.use(VueCookie);
Vue.use(VueMomentJS, moment);

// element-plus 프로그래매틱 API → 전역 $ 매핑 (compat: Vue.prototype 전역 동작)
Vue.prototype.$loading = ElLoading.service;
Vue.prototype.$msgbox = ElMessageBox;
Vue.prototype.$alert = ElMessageBox.alert;
Vue.prototype.$confirm = ElMessageBox.confirm;
Vue.prototype.$prompt = ElMessageBox.prompt;
Vue.prototype.$notify = ElNotification;
Vue.prototype.$message = ElMessage;

// Vue 3 호환: new Vue() 이벤트버스 → mitt(리스너 예외 격리 래퍼). API: $on/$emit → on/emit
Vue.prototype.$bus = createBus();

/**
 * Regist global components
 */
import SearchCctr from './components/SearchCctr.vue'
import SearchAccount from './components/SearchAccount.vue'
import SearchEmp from './components/SearchEmp.vue'
import SearchComp from './components/SearchComp.vue'
import {AgGridVue} from 'ag-grid-vue'

Vue.component('SearchCctr', SearchCctr)
Vue.component('SearchAccount', SearchAccount)
Vue.component('SearchEmp', SearchEmp)
Vue.component('SearchComp', SearchComp)
Vue.component('AgGridVue', AgGridVue)

/**
 * Integrate NumeralJS
 */
const numeral = require('numeral')

Vue.prototype.$numeral = numeral
/// Integrate NumeralJS

// filters
Vue.filter('number', (value) => value ? new Intl.NumberFormat().format(value.toString().replace(/\D/g, "") || 0) : value);
Vue.filter('amt', (value) => value ? new Intl.NumberFormat().format(value.toString().replace(/(\D)(?=(\D{3})+(?:\.\D+)?$)/g, "") || 0) : value);
Vue.filter('dateformat', (value, format) => moment(value).format(format || 'yyyy-MM-DD'));
Vue.prototype.$filters = Vue.options.filters;

// Debug?
Vue.prototype.debug = (process.env.NODE_ENV === 'development')

// 로그와 경고를 출력(production 출력안함.)
Vue.config.silent = (process.env.NODE_ENV === 'production')
// 전역 사용자 정의 디렉티브 v-focus 등록
Vue.directive('focus', {
  // 바인딩 된 엘리먼트가 DOM에 삽입되었을 때...
  inserted: function (el) {
    // 엘리먼트에 포커스를 줍니다
    el.focus()
  }
})

import { _url as url } from './libs/join'

// Vue 3: new Vue({...}).$mount() → createApp + app.use(plugins) + mount
const app = createApp(App);
app.use(store);
app.use(router);
app.use(i18n);
app.use(ElementPlus, { locale: elLocaleKo, size: 'small', zIndex: 3000 });
// element-plus 아이콘 컴포넌트 전역 등록 (구 icon="el-icon-*" 문자열 → :icon 컴포넌트는 후속 처리)
Object.entries(ElementPlusIcons).forEach(([name, comp]) => app.component(name, comp));

/**
 * HTTP 인터셉터 (구 root created에서 이관). 컴포넌트 밖이라 this 대신
 * store/router/axios/VueCookie/swal 인스턴스를 직접 참조.
 */
axios.interceptors.request.use((config) => {
  // 세션 쿠키 확인
  if (VueCookie.get('sessionAlive') === null) {
    console.log('this is null');
    store.commit('logout');
    VueCookie.delete('loginInfo');
  }
  // 세션 자동 주입 (compCd)
  if (store && store.state && store.state.loginInfo) {
    var session = store.state.loginInfo;
    switch (String(config.method || '').toLowerCase()) {
      case 'get':
      case 'delete':
        if (config.params !== undefined && typeof config.params === 'object' && !config.params.compCd) {
          config.params.compCd = session.compCd;
        }
        break;
      case 'post':
      case 'put':
        if (config.data !== undefined && typeof config.data === 'object' && !config.data.compCd) {
          config.data.compCd = session.compCd;
        }
        break;
    }
  } else {
    config.data.compCd = process.env.VUE_APP_COMP_CODE;
  }
  return config;
});

axios.interceptors.response.use((response) => response, (error) => {
  if (error.response && error.response.data && error.response.data.message) {
    if (error.response.data.status === 403) {
      router.push({ path: `/login` });
    } else {
      swal({ type: 'error', text: error.response.data.message });
    }
  }
  return Promise.reject(error);
});

app.mount('#app');
