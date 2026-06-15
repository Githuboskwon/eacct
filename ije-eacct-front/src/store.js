import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import moment from 'moment';
import VueMomentJS from 'vue-momentjs';

Vue.use(VueMomentJS, moment);
Vue.use(Vuex);

const defaultOpenDate = {
    localMonthDate: moment().subtract(3, 'month').format('YYYY-MM-DD'),
    nextMonthDate: moment(),
};

export default new Vuex.Store({
    plugins: [createPersistedState()],
    state: {
        loginInfo: {
            userName: '',
            loginId: '',
            loginPw: '',
            loginCompCd: '',
            token: '',
            authorities: [],
            menus: [],
        },
        openDate: {
            localMonthDate: '',
            nextMonthDate: '',
        },
        searchForm: {},
        locale: "ko",
        isLoading: false,
        oauthNew: '',
        evidFileSize: 0,
        jiniSize : 0,
        jiniFileSize: 0
    },
    mutations: {
        setOpenDate(state, openDate) {
            state.openDate = Object.assign({}, defaultOpenDate, openDate || {});
        },
        searchForm(state, search) {
            state.searchForm = search
        },
        login(state, loginInfo) {
            state.loginInfo = loginInfo;
        },
        logout(state) {
            state.loginInfo = null;
        },
        loading(state) {
            state.isLoading = true;
        },
        finish(state) {
            state.isLoading = false;
        },
        register(state, oauthNew) {
            state.oauthNew = oauthNew;
        },
        setLocale(state, locale) {
            state.locale = locale;
        },
        setEvidFileSize(state, fileSize) {
            state.evidFileSize = fileSize || 0;
        },
        setJiniSize(state, jiniSize) {
            state.jiniSize = jiniSize || 0;
        },
        setJiniFileSize(state, fileSize) {
            state.jiniFileSize = fileSize || 0;
        }
    },
});
