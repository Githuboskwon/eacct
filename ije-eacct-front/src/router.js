import { createRouter, createWebHistory } from 'vue-router';

// vue-router 4: Vue.use(Router) 불필요. 중복내비 reject 가드도 불필요(v4는 reject 대신
// NavigationFailure로 resolve). mode:'history' → createWebHistory(base).

export default createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: [
        {
            // Vue3 element-plus v-model 격리 진단용 (임시)
            path: '/eptest',
            name: 'eptest',
            component: () => import('./views/EpTest.vue'),
        },
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
            path: '/certificateMng',
            name: 'certificateMng',
            props: true,
            component: () => import('./views/CertificateMng.vue')
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
