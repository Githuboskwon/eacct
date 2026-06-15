// import 'buefy/dist/buefy.css';
// import 'bulma-o-steps/bulma-steps.sass';
import Buefy from 'buefy';
import Vue from 'vue';
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

import {
  Dialog,
  Autocomplete,
  Input,
  InputNumber,
  Radio,
  RadioGroup,
  RadioButton,
  Checkbox,
  CheckboxButton,
  CheckboxGroup,
  Select,
  Option,
  Button,
  ButtonGroup,
  DatePicker,
  Popover,
  Tooltip,
  Breadcrumb,
  BreadcrumbItem,
  Form,
  FormItem,
  Alert,
  Slider,
  Icon,
  Row,
  Col,
  Upload,
  Transfer,
  Link,
  Divider,
  Loading,
  MessageBox,
  Message,
  Notification,
  Drawer,
} from 'element-ui';
import el_locale from 'element-ui/lib/locale';
import ko_locale from 'element-ui/lib/locale/lang/ko';

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

Vue.use(Dialog);
Vue.use(Autocomplete);
Vue.use(Input);
Vue.use(InputNumber);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(RadioButton);
Vue.use(Checkbox);
Vue.use(CheckboxButton);
Vue.use(CheckboxGroup);
Vue.use(Select);
Vue.use(Option);
Vue.use(Button);
Vue.use(ButtonGroup);
Vue.use(DatePicker);
Vue.use(Popover);
Vue.use(Tooltip);
Vue.use(Breadcrumb);
Vue.use(BreadcrumbItem);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Alert);
Vue.use(Slider);
Vue.use(Icon);
Vue.use(Row);
Vue.use(Col);
Vue.use(Upload);
Vue.use(Transfer);
Vue.use(Link);
Vue.use(Divider);
Vue.use(Drawer);

Vue.use(Loading.directive);

Vue.prototype.$ELEMENT = { size: 'small', zIndex: 3000 };

el_locale.use(ko_locale);
Vue.prototype.$loading = Loading.service;
Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$notify = Notification;
Vue.prototype.$message = Message;

Vue.prototype.$bus = new Vue();

/**
 * Regist global components
 */
import SearchCctr from './components/SearchCctr.vue'
import SearchAccount from './components/SearchAccount.vue'
import SearchEmp from './components/SearchEmp.vue'
import SearchComp from './components/SearchComp.vue'
import {AgGridVue} from 'ag-grid-vue'
import {TheMask} from 'vue-the-mask'

Vue.component('SearchCctr', SearchCctr)
Vue.component('SearchAccount', SearchAccount)
Vue.component('SearchEmp', SearchEmp)
Vue.component('SearchComp', SearchComp)
Vue.component('AgGridVue', AgGridVue)
Vue.component('TheMask', TheMask)

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

new Vue({
  router,
  store,
  i18n,
  $,
  render: h => h(App),
  created() {
    //console.log(this.$cookie.get('sessionAlive'));
    /**
     * HTTP Request Interceptor
     */
    this.$http.interceptors.request.use((config) => {

      /**
       * check current session alive.
       * using by browser cookie(life-time: session)
       */
      //console.log(this.$cookie.get('sessionAlive'));
      if(this.$cookie.get('sessionAlive') === null) {
        //console.log(this.$cookie.get('sessionAlive'));
        console.log('this is null');
        this.$store.commit('logout');
        this.$cookie.delete('loginInfo');
      }
      
      /**
       * Session auto-injection
       */
      if (this.$store && this.$store.state && this.$store.state.loginInfo) {
        var session = this.$store.state.loginInfo
        /**
         * compCd가 없는 경우, 자동으로 Injection
         */
        switch (String(config.method || '').toLowerCase()) {
          case 'get':
          case 'delete':
            if (config.params !== undefined && typeof config.params === 'object' && !config.params.compCd) {
              config.params.compCd = session.compCd
            }
            break
          case 'post':
          case 'put':
            if (config.data !== undefined && typeof config.data === 'object' && !config.data.compCd) {
              config.data.compCd = session.compCd
            }
            break
        }
      } else {
        config.data.compCd = process.env.VUE_APP_COMP_CODE
      }
      return config
    })

    this.$http.interceptors.response.use(response => {
      
      return response
    }, error => {
      
      if (error.response && error.response.data && error.response.data.message) {
        
        if(error.response.data.status===403){
          this.$router.push({path: `/login`});
        }else{
          this.$swal({
            type: 'error',
            text: error.response.data.message
          })
        }
      }
      return Promise.reject(error)
    })
  }
}).$mount('#app');
