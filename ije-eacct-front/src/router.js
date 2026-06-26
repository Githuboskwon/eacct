import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

/**
 * 같은 경로로 push/replace 시 vue-router가 "redundant navigation"으로 promise를 reject한다.
 * 호출부마다 .catch()가 없어 unhandled rejection이 되고, vue-cli5(webpack-dev-server) 런타임
 * 오버레이에 "Avoided redundant navigation..."로 노출된다(기능 무관, dev 전용).
 * ⚠️ vue-router 3.2+는 이 에러에 name이 없고(`err.name` 미작동) isNavigationFailure도
 * 이 빌드에선 named export가 아니라 미사용. 메시지로 판별(버전 무관, 그 외 에러는 전파).
 */
const isDuplicated = (err) => {
  if (!err) return false;
  if (err.name === 'NavigationDuplicated') return true; // vue-router < 3.2
  return /redundant navigation/i.test(err.message || ''); // vue-router 3.2+ (name 없음)
};

// push/replace 프로토타입 패치는 제거한다. 과거 패치는 중복 네비게이션 reject를
// resolve로 바꿔 삼켜서, 호출부의 .catch(중복 → window.location.reload()/폴백 push)가
// 동작하지 않게 만들었다(ERROR_FIXES.md #1). 네이티브 reject 동작을 복원해 .catch가
// 의도대로 발화하도록 한다.
//
// 패치 도입 사유였던 "dev 런타임 오버레이에 노출"은 .catch 없는 호출부의 unhandled
// rejection만 전역에서 골라 억제해 해결한다. .catch가 있는 호출부의 의미는 보존되고,
// 중복 네비게이션 외의 진짜 네비게이션 에러는 그대로 전파된다.
if (typeof window !== 'undefined') {
  window.addEventListener('unhandledrejection', (event) => {
    if (isDuplicated(event.reason)) {
      event.preventDefault(); // 중복 네비게이션 미처리 reject만 조용히 무시
    }
  });
}

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/quickSetting',
            name: 'quickSetting',
            component: () =>
                import ('./views/QuickSetting.vue'),
        },
        {
            path: '/',
            name: 'main',
            component: () =>
                import ('./views/MyMain.vue'),
        },
       
        {
          path: '/authMng',
          name: 'authMng',
          component: () => import('./views/AuthMng.vue')
        },
        {
          path: '/pgmMng',
          name: 'pgmMng',
          component: () => import('./views/PgmMng.vue')
        },
        {
            path: '/codeMng',
            name: 'codeMng',
            component: () =>
                import ('./views/CodeMng.vue'),
        },
        {
            path: '/login',
            name: 'Login',
            props: true,
            component: () =>
                import ('./components/Login.vue'),
        },
        {
            path: '/oauth/new',
            name: 'OAuthNewUser',
            props: true,
            component: () =>
                import ('./components/OAuthNewUser.vue'),
        },
        {
            path: '/GridComponent',
            name: 'GridComponent',
            component: () =>
                import ('./views/GridComponent.vue'),
        },
        {
            // PoC: GridED → ag-grid 전환 실증 (운영 영향 없음, 검증용)
            path: '/gridEdPoc',
            name: 'gridEdPoc',
            component: () =>
                import ('./components/poc/GridEDPoc.vue'),
        },
        {
            path: '/deptAuthMng',
            name: 'deptAuthMng',
            component: () =>
                import ('./views/DeptAuthMng.vue'),
        },
        {
            path: '/certificateMng',
            name: 'certificateMng',
            props: true,
            component: () => import('./views/CertificateMng.vue')
        },
        {
            path: '/taxMonitoring',
            name: 'taxMonitoring',
            props: true,
            component: () => import('./views/TaxMonitoring.vue')
        },
        {
            path: '/pacctMng',
            name: 'pacctMng',
            component: () => import('./views/PacctMng.vue')
        },
        {
            path: '/acctMng',
            name: 'acctMng',
            component: () => import ('./views/AcctMng.vue')
        },
        {
            path: '/acctSubMng',
            name: 'acctSubMng',
            component: () => import ('./views/AcctSubMng.vue')
        },
        {
            path: '/closeMng',
            name: 'closeMng',
            component: () =>
                import ('./views/CloseMng.vue'),
        },
        {
            path: '/confirmMng',
            name: 'confirmMng',
            component: () =>
                import ('./views/ConfirmMng.vue'),
        },
        {
            path: '/delegateMng',
            name: 'delegateMng',
            component: () =>
                import ('./views/DelegateMng.vue'),
        },
        {
            path: '/expendMoneyMng',
            name: 'expendMoneyMng',
            component: () =>
                import ('./views/ExpendMoneyMng.vue'),
        },
        {
            path: '/oilPcePop',
            name: 'oilPcePop',
            component: () =>
                import ('./views/oilPcePop.vue'),
        },
        {
            path: '/taxMng',
            name: 'taxMng',
            component: () =>
                import ('./views/TaxMng.vue'),
        },
        {
            path: '/cardInfoMng',
            name: 'cardInfoMng',
            component: () =>
                import ('./views/CardInfoMng.vue'),
        },
        {
            path: '/cardUseLst',
            name: 'cardUseLst',
            component: () => import ('./views/CardUseLst.vue'),
        },
        {
            path: '/expPriceReg',
            name: 'expPriceReg',
            component: () =>
                import ('./components/ExpPriceReg.vue'),
        },
        {
            path: '/estimateReg',
            name: 'estimateReg',
            component: () =>
                import ('./components/EstimateReg.vue'),
        },
        {
            path: '/exchangeRateMng',
            name: 'exchangeRateMng',
            component: () =>
                import ('./components/ExchangeRateMng.vue'),
        },
        {
            path: '/estimateList',
            name: 'estimateList',
            component: () =>
                import ('./components/EstimateList.vue'),
        },
        {
            path: '/expPriceList',
            name: 'expPriceList',
            component: () =>
                import ('./components/ExpPriceList.vue'),
        },
        {
            path: '/businessTripDisMng',
            name: 'businessTripDisMng',
            props: true,
            component: () => import('./views/BusinessTripDisMng.vue')
        },
        {
            path: '/schedulerMng',
            name: 'schedulerMng',
            props: true,
            component: () => import('./views/SchedulerMng.vue')
        },
        {
            path: '/mailSendMng',
            name: 'mailSendMng',
            props: true,
            component: () => import('./views/MailSendMng.vue')
        },
        {
            path: '/apprLineMng',
            name: 'apprLineMng',
            component: () =>
                import('./views/ApprLineMng.vue'),
        },
        {
            path: '/vendorLst',
            name: 'vendorLst',
            component: () =>
                import('./views/VendorLst.vue'),
        },
        {
            path: '/bondMst',
            name: 'bondMst',
            props: true,
            component: () => import('./views/BondMst.vue')
        },
        {
            path: '/bondExpendLst',
            name: 'boneExpendList',
            props: true,
            component: () => import('./views/BondExpendList.vue')
        },
        {
            path: '/hrSlipReg',
            name: 'hrSlipReg',
            props: true,
            component: () => import('./views/HrSlipReg.vue')
        },
        {
            path: '/hrExpendStatus',
            name: 'hrExpendStatus',
            props: true,
            component: () => import('./views/HrExpendStatus.vue')
        },
        {    path: '/apprPendLst',
            name: 'apprPendLst',
            props: (route) => {
                return {
                    params: route.params,
                    dateSet: route.params.dateSet
                }
            },
            component: () =>
                import ('./views/ApprPendLst.vue'),
        },
        {
            path: '/apprCompLst',
            name: 'apprCompLst',
            component: () => import ('./views/ApprCompLst.vue'),
        },
        {
            path: '/apprReqLst',
            name: 'apprReqLst',
            component: () => import ('./views/ApprReqLst.vue'),
        },
        {
            path: '/apprMndSet',
            name: 'apprMndSet',
            component: () =>
                import ('./views/ApprMndSet.vue'),
        },
        {
            path: '/apprRuleSet',
            name: 'apprRuleSet',
            component: () =>
                import ('./views/ApprRuleSet.vue'),
        },
        {
            path: '/exctpExpense',
            name: 'exctpExpense',
            props: true,
            component: () => import('./views/ExctpExpense.vue')
        },
        {
            path: '/paySlipLst',
            name: 'paySlipList',
            props: true,
            component: () => import('./views/PaySlipList.vue')
        },
        {
            path: '/purSlipLst',
            name: 'purSlipLst',
            props: true,
            component: () => import('./views/PurSlipList.vue')
        },
        {
            path: '/etcSalesSlipLst',
            name: 'etcSalesSlipLst',
            props: true,
            component: () => import('./views/EtcSalesSlipLst.vue')
        },
        {
            path: '/fundSlipLst',
            name: 'fundSlipLst',
            props: true,
            component: () => import('./views/FundSlipLst.vue')
        },
        {
            path: '/collectionSlipLst',
            name: 'collectionSlipLst',
            props: true,
            component: () => import('./views/CollectionSlipLst.vue')
        },
        {
            path: '/salesSlipLst',
            name: 'salesSlipLst',
            props: true,
            component: () => import('./views/SalesSlipLst.vue')
        },
        {
            path: '/foreignSlipLst',
            name: 'foreignSlipLst',
            props: true,
            component: () => import('./views/ForeignSlipLst.vue')
        },
        {
            path: '/exportSlipLst',
            name: 'exportSlipLst',
            props: true,
            component: () => import('./views/ExportSlipLst.vue')
        },
        {
            path: '/eSlipSubmit',
            name: 'eSlipSubmit',
            props: true,
            component: () => import('./views/ESlipSubmit.vue')
        },
        {   path: '/projectLst',
            name: 'projectLst',
            component: () => import ('./views/ProjectLst.vue'),
        },
        {
            path: '/projectPlanBudLst',
            name: 'projectPlanBudLst',
            component: () => import ('./views/ProjectPlanBudLst.vue'),
        },
        {
            path: '/projectSuperLst',
            name: 'projectSuperLst',
            component: () => import ('./views/ProjectSuperLst.vue'),
        },
        {
            path: '/projectConsLst',
            name: 'projectConsLst',
            component: () => import ('./views/ProjectConsLst.vue'),
        },
        {
            path: '/projectExcBudList',
            name: 'projectExcBudList',
            component: () => import ('./views/ProjectExcBudList.vue'),
        },
        {
            path: '/projectPosExpLst',
            name: 'projectPosExpLst',
            component: () => import ('./views/ProjectPosExpLst.vue'),
        },
        {
            path: '/projectPlanBudRepLst',
            name: 'projectPlanBudRepLst',
            component: () => import ('./views/ProjectPlanBudRepLst.vue'),
        },
        {
            path: '/projectExcActRepLst',
            name: 'projectExcActRepLst',
            component: () => import ('./views/ProjectExcActRepLst.vue'),
        },
        {
            path: '/projectStaTabRep',
            name: 'projectStaTabRep',
            component: () => import ('./views/ProjectStaTabRep.vue'),
        },
        {
            path: '/projectBudActRep',
            name: 'projectBudActRep',
            component: () => import ('./views/ProjectBudActRep.vue'),
        },
        {
            path: '/projectWrtChaLst',
            name: 'projectWrtChaLst',
            component: () => import ('./views/ProjectWrtChaLst.vue'),
        },
        {
            path: '/evidAtchPopGroupware',
            name: 'evidAtchPopGroupware',
            props: (route) => {
                return {
                    params: route.params
                }
            },
            component: () => import ('./components/EvidAtchPopGroupware.vue'),
        },
        {
            path: '/evidAtchPopModeless',
            name: 'evidAtchPopModeless',
            props: (route) => {
                return {
                    params: route.params
                }
            },
            component: () => import ('./components/EvidAtchPopModeless.vue'),
        },
        {
            path: '/slipLst',
            name: 'slipLst',
            props: (route) => {
                return {
                    params: route.params,
                    dateSet: route.params.dateSet,
                }
            },
            component: () => import ('./views/SlipLst.vue'),
        },
        {
            path: '/slipMng',
            name: 'slipMng',
            component: () => import ('./views/SlipMng.vue'),
        },
        {
            path: '/GLSlipLst',
            name: 'GLSlipList',
            component: () => import ('./views/GLSlipList.vue'),
        },
        /**
         * * 전표발생 화면
         */
        {
            path: '/accrualSlip/:slipHeaderId?',
            name: 'accrualSlip',
            props: (route) => {
                return {
                    slipHeaderId: route.params.slipHeaderId,
                    memberInfo: route.params.memberInfo,
                    taxInfo: route.params.taxInfo,
                }
            },
            component: () => import ('./views/accrualSlip/index.vue'),
        },
        {
            path: '/salesTaxInvoiceLst',
            name: 'salesTaxInvoiceLst',
            component: () => import ('./views/SalesTaxInvoiceLst.vue'),
        },
        {
            path: '/salesTaxInvoiceIssue',
            name: 'salesTaxInvoiceIssue',
            component: () => import ('./views/SalesTaxInvoiceIssue.vue'),
        },
        {
            path: '/budgetMng',
            name: 'budgetMng',
            component: () => import ('./views/BudgetMng.vue'),
        },
        {
            path: '/ebillSlipRcvLst',
            name: 'ebillSlipRcvLst',
            props: (route) => {
                return {
                    params: route.params,
                    dateSet: route.params.dateSet,
                }
            },
            component: () => import ('./views/EbillSlipRcvLst.vue'),
        },
        {
            path: '/termsPaymentMng',
            name: 'termsPaymentMng',
            component: () => import ('./views/TermsPaymentMng.vue'),
        },
        {
            path: '/sample',
            name: 'sample',
            component: () =>
                import ('./views/sample/index.vue'),
        },
        {
            path: '/refLst',
            name: 'refLst',
            component: () => import ('./views/RefLst.vue'),
        },
    ],
});
