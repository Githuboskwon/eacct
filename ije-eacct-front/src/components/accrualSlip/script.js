/**
 * 발생전표 헤더의 모든 이벤트
 */

import assert from '@/libs/assert';

/** 
 * * 서브헤더의 화면 정의 시 거래유형에서의 전표유형코드로 작명해서 사용.
 * * * * * * * * * * * HD.vue 컴포넌트의 computed subHeaderComponent 리턴에서 사용.
 * * EXPEND : 경조금
 * * TRAFFIC: 여비교통비
 * * TRIP : 출장비
 * * BOND : BOND
 * * 원천세 : AWT
 * * 잡급비 : ETCAWT
 * * 법인카드(임직원-삼성카드) : CARD
 * * 구매물대(미지급금) : PO
 * * 수입물대 : IM
 * * 인사전표 : HR
 */
import EXPEND from '@/components/accrualSlip/SubHD/Expend.vue'; /** 경조금 */
import TRAFFIC from '@/components/accrualSlip/SubHD/Traffic.vue'; /** 여비교통비 */
import TRIP from '@/components/accrualSlip/SubHD/Trip.vue'; /** 출장비 */
import BOND from '@/components/accrualSlip/SubHD/Bond.vue'; /** BOND */
import AWT from '@/components/accrualSlip/SubHD/Awt.vue'; /** 원천세 */
import ETCAWT from '@/components/accrualSlip/SubHD/EtcAwt.vue'; /** 잡급비 */
import CARD from '@/components/accrualSlip/SubHD/Card.vue'; /** 법인카드(임직원-삼성카드) */
import PO from '@/components/accrualSlip/SubHD/Po.vue'; /** 구매물대(미지급금) */
import IM from '@/components/accrualSlip/SubHD/Im.vue'; /** 수입물대 */
import HR from '@/components/accrualSlip/SubHD/Hr.vue'; /** 인사전표 */

/**
 * * 보조 컴포넌트
 */
import AssistantVAT from '@/components/accrualSlip/Assistant/AssistantVAT.vue'; /** 부가세 */

/**
 * * Modal
 */
import TradeTypeModal from '@/components/accrualSlip/Modals/TradeTypeModal.vue'; /** 거래유형 */
import VendorModal from '@/components/Vendor_Ag'; /** 거래처 */
import GLTermsModal from '@/components/accrualSlip/Modals/GLTermsModal.vue'; /** 결제조건 */
import TaxEvidenceModal from '@/components/accrualSlip/Modals/TaxEvidenceModal.vue'; /** 세무증빙유형 */
import BizModal from '@/components/accrualSlip/Modals/BizModal.vue'; /** 사업장조회 */
import AdvancedModal from '@/components/accrualSlip/Modals/AdvancedModal.vue'; /** 선급금정산 */
import VendorBanksModal from '@/components/accrualSlip/Modals/VendorBanksModal.vue'; /** 계좌정보 */
import TaxMainModal from '@/components/accrualSlip/Modals/TaxMainModal.vue'; /** 세금계산서 */
import EmpModal from '@/components/Emp_Ag.vue';
import DelegatorModal from '@/components/accrualSlip/Modals/DelegatorModal.vue';

/** 전자세금계산서 보기 */
import ElecTaxPopup from '@/components/ElecTaxPopup.vue';

export default {
    components: {
        EXPEND, TRAFFIC, TRIP, BOND, AWT, ETCAWT, CARD, PO, IM, AssistantVAT, HR
    },
    data() {
        return {
            employes: [],
            openDay: '',
            closeDay: '',
            slipStatusList: [
                {key: 'SV', value: '저장됨'},
                {key: 'VE', value: '검증오류'},
                {key: 'VC', value: '검증됨'},
                {key: 'AP', value: '결재중'},
                {key: 'AR', value: '결재반려'},
                {key: 'CP', value: '검인중'},
                {key: 'CR', value: '검인반려'},
                {key: 'CC', value: '검인됨'},
                {key: 'IE', value: '이관오류'},
                {key: 'IC', value: '이관완료'},
                {key: 'SD', value: '전표삭제'},
                {key: 'FP', value: '검인중(ERP)'},
                {key: 'FH', value: '검인보류(ERP)'},
                {key: 'FC', value: '검인됨(ERP)'},
                {key: 'FR', value: '검인반려(ERP)'},
                {key: 'SC', value: '전표취소'},
            ], //진행단계 프론트 코드화 하드코딩
            value: {
                docTitle: '',
                docUrl: '',
                approvalGroupId: '', //상신그룹ID
                slipGroupYn: '', //
                slipGroupNumber: '',//헤더 전표그룹번호
                slipTypeCd: '', //전표유형타입(프론트 사용 예시 : COMMON, EXPEND, TRFFIC 과 같은 코드)
                slipNo: '',//헤더 전표번호
                status: '', //진행단계 (전자전표)
                slipStatus: '',//진행단계 (인터페이스)
                slipHeaderId: '', //헤더전표ID
                slipDate: '', //전표일자 -> 회계일자 따라감.
                postingDt: '', //회계일자
                deptCd: '', //(귀속)부서코드
                deptNm: '', //(귀속)부서명
                deptType: '', //(귀속) 판관/제조/공통 판단
                empNo: '', //직원코드
                empNm: '', //직원명
                orgId: '', //직원에서 찍혀내려진 회사코드
                trxTypeCode: '',// 거래유형코드
                trxTypeName: '',// 거래유형명
                lineAttribute1: '', //거래유형 라인(부가세: V, 원천세: W 등)
                lineAttribute1Name: '',//거래유형 라인명
                lineAttribute2: '',
                lineAttribute3: '', //거래유형 라인(스마트빌: S, )
                ttypeInterfaceModule: '', //거래유형 매출,매입 인터페이스 모듈 
                prepayCnt: '', //선급금 정산잔액이 있는지 체크 하는 변수
                ttypeInterfaceSlipType: '', //거래유형 인터페이스 전표유형
                ttypeInputModule: '', //거래유형 출처
                ttypePrepaymentFlag: '', //거래유형 선급여부
                ttypeAddInfoType: '', //거래유형 세부유형
                slipDisplayFlag: 'Y', //전표표시 대상여부
                slipCreationTargetFlag: 'Y', //ERP전표생성 대상 여부 flag
                awtGroupId: '', //거래유형(원천세인 경우 원천세 그룹ID)
                awtGroupNm: '', //거래유형(원천세인 경우 원천세 그룹Name)
                awtLocationCode: '', //사업장코드(원천세)
                awtLocationName: '', //사업장명(원천세)
                taxLocationCode: '', //사업장코드
                taxLocationName: '', //사업장
                //repayment_due_date
                repaymentDueDate: this.$moment().format("YYYYMMDD"), //반제예정일
                drCr: '', //차대 구분
                projectId: '1', //프로젝트ID
                taskId: '', // 타스크 ID
                taskCode: '', //타스크 코드
                taskName: '',//타스크 명
                taxEvidenceType: '', //세무증빙유형코드
                taxEvidenceTypeName: '', //세무증빙유형명
                curCd: 'KRW', //통화
                exchangeDate: this.$moment().format("YYYYMMDD"), //환율일자
                excRt: 1,  //환율
                exchangeRateType: 'Y',// 환율 유형 ??
                //헤더 저장 시 ccid 값 s
                ledgerId: '',// 헤더 원장 ID
                budgetDeptCode: '', //헤더 예산부서코드
                actualDeptCode: '', //헤더 귀속부서코드
                acctCode: '', //헤더계정 코드 (중요)
                acctName: '', //헤더계정명 (중요)
                itemGroupCode: '', //헤더 제품군코드
                projectCode: '', //헤더 프로젝트 코드
                trBankAcctCode: '', //헤더 금융계좌코드
                slipTypeCode: '', //헤더 전표원장
                compCode: '', //헤더 법인코드
                segment1Code: '', //헤더 법인코드
                segment9Code: '', //헤더 여분1코드
                segment10Code: '', //헤더 여분1코드
                //헤더 저장 값 e
                description: '', //적요
                totAmt: 0,  //전표금액
                totAmtKrw: 0, //전표금액(KRW)
                stlFgCd: 'N', //정산구분
                prepaymentApplyFlag: 'N', //선급금정산
                amountToApply: '', //선급금정산금액
                amount_to_apply: 0, //반제신청금액
                prepaymentYn: 'N', //선급금확인여부
                realAmt: 0, //실지급금액
                
                vendorId: '', //거래처 ID
                vendorNum: '', //거래처 코드 인 듯
                vendorSiteId: 0, //거래처 사이트 ID
                vendorPartyId: '',
                vendorPartySiteId: '',
                customerId: '',
                customerSiteId: '',
                customerPartyId: '',
                customerPartySiteId: '',

                venPaymentCurrencyCd: 'KRW', //거래처 통화코드
                payGroupLookupCode: '', //
                invoiceTypeLookupCode: 'STANDARD',
                integrationVendorNum: '', //거래처ID
                integrationVendorName: '', // 거래처명
                vatRegistrationNum: '', //부가세 등록 계좌번호
                bankAccountId: '', //은행 계정 ID
                bankAccountName: '', //은행 계정명
                bankAccountNumber: '',//은행 계좌번호
                bankName: '',   //은행명
                bankAcct: '', //은행명 + 은행계좌번호
                termId: '',   //결제조건 id
                termName: '',   //결제조건
                termDescription: '', //결제방법
                termDueDate: this.$moment().format('YYYYMMDD'), //결제예정일
                termDueDateChangeFlag: 'N', //hidden 결제예정일 변경 가능 플래그 (디폴트 : 불가능 = 'N' ) 
                noteFlag: 'N',// 결제어음여부
                paymentReceiptMethodCode: '', //지급방법
                vendorAcctCheck: '', //계좌정보 필수 플래그
                maturityDate: '', //어음만기예정일
                remark: '',
                expend: { //경조사 관련
                    expendDt: this.$moment().format('YYYYMMDD'), //경조일시
                    expendCd: '', //경조구분
                    expendNm: '', //경조구분명 (넘기는 값은 아님.)
                    expendRelation: '', //신청인과의 관계
                    expendReceiveNm: '', //당사자 성명
                    expendAmt: '', //회사 지급액
                    wreathYn: 'Y', //경조 화환
                    holiday: '', //휴가일
                    mutualYn: 'N', //상조용품
                },
                traffic: { //여비교통비
                    temp2: 'Y', //해당여부
                    tripCd: 'C', //교통비
                    tripObj: '', //내용
                },
                trip: [], //출장비
                bond: {
                    splitEtcYn: 'N', //분할 및 기타비용
                    bondGubun: '', //BOND 종류
                    type: 'LOCAL', //수수료 유형 (국내 or 해외)
                    benCountryCd: '', //수익자 국가 코드
                    benCountry: '', //수익자 국가
                    refNo: '', //REF NO. (신규/AMEND)
                    amendText: '신규',//REF NO. (신규/AMEND)
                    projectNm: '',//PJT 명
                    customerNm: '', //고객명
                    openingDt: '', //개설일
                    currencyCd: '',//통화
                    currencyNm: '',//통화
                    guaranteeAmt: 0, //보증금액
                    maturityDt: '', //만기일
                    rate: 0, //요율
                    localBankNm: '', //은행
                    intBankNm: '', //은행
                    budget: 0, //예산
                    amendSeq: 0,
                    amendCount: 0
                },
                awt: [], //원천세
                etcawt: [], //잡급비
                /** 부가세 관련 데이터 s. */
                evidenceVendorNum: '', //증빙거래처
                evidenceVendorVat: '', //사업자번호
                evidenceVendorName: '', //증빙거래처명
                evidenceVendorCustSiteId: '', //증빙거래처ID
                taxIssueTypeCode: '10', //발행구분
                taxIssueTypeName: '정발행', //발행구분명
                evidenceDate: '', //부가세 작성년월일
                taxStatusCode: '',  //부가세명
                percentageRate: 0, //세율
                taxRateId: '',      //부가세계정ID (이게 key 인듯)
                taxRateCode: '',    //부가세계정
                taxAcctCode: '',     //부가세계정코드
                taxAcctName: '', //헤더부가세코드명?
                taxbillSuId: '', //세금계산서id
                taxSmartbillNo: '', //세금계산서 KEY 번호(승인번호)
                dtiType: '', //세금계산서 세금코드 타입
                taxbillSupplyAmt: 0, //세금계산서 공급가액
                taxbillTaxAmt: 0, //세금계산서 세액
                taxbillTotalAmt: 0, //세금계산서 합계
                supplyAmount: 0, //공급가액
                taxAmount: 0, //세액
                totalAmount: 0, //합계
                vSupplyAmount: 0, //공급가액 (View 용)
                vTaxAmount: 0, //세액 (View 용)
                vTotalAmount: 0, //합계 (View 용)
                /** 부가세 관련 데이터 e. */
                globalAttribute1: '',
                globalAttribute2: '',
                globalAttribute3: '',
                globalAttribute4: '',
                globalAttribute5: '',
                globalAttribute6: '',
                globalAttribute7: '',
                globalAttribute8: 'N', //상이계좌여부
                globalAttribute9: '',
                globalAttribute10: '',
                globalAttribute11: '',
                globalAttribute12: '',
                globalAttribute13: '',
                globalAttribute14: '', //세금계산서 번호
                globalAttribute15: '',
                globalAttribute16: '',
                globalAttribute17: '',
                globalAttribute18: '',
                globalAttribute19: '',
                globalAttribute20: '',
            },
            options: { //화면 options 리스트 객체 담는 object
                FRGN_CUR_CD: [], //통화목록
            },
            status: { //컴포넌트 활성화 상태 object
                loaded: true,
                assistantloaded: false,
            },
            isTaxReadOnly: true
        }
    },
    methods: {
        /**
         * 초기 공통함수 호출
         * * 통화목록 세팅 : gubun 값으로 상단에 위치할 데이터를 정렬
         * * 회계년월 세팅 : 회계일자를 open ~ close 일자 범위내에 설정하도록 초기값 호출
         * * 초기 직원 세팅 : route parameters 가 있는 경우와 없는 경우에 따라 초기 셋업
         */
        init() {
            //통화 목록 세팅
            this.$http.get(`/api/exchange/currency/list`)
            .then(res => {
                this.options['FRGN_CUR_CD'] = res.data.map(x => {
                    x.gubun = x.gubun || 99; //우선순위 Null 값에 대한 후순위로 변경
                    return x;
                }).map(x => x).sort((a, b) => {
                    return a.gubun - b.gubun; 
                });
            });
            //회계년월 세팅
            this.$http.get(`/api/acctPeriod/open/close/date`)
            .then(res => res.data)
            .then(data => {
                this.openDay = data.openDay;
                this.closeDay = data.closeDay;
            });
            
            /**
             * 초기 직원 세팅
             * * 대쉬보드에서 법인카드 사용내역으로 들어온 경우 해당 직원 셋업 (default : 로그인 사용자)
            */
            const name = this.$route.params?.memberInfo?.name || this.$store.state.loginInfo.loginId; //라우팅으로 넘어온경우 (법인카드 내역 등.)
            const trxCd = this.$route.params?.memberInfo?.trxCd;
            const cardType = this.$route.params?.memberInfo?.type || 'A';
            const taxInfo = this.$route.params?.taxInfo || {};
            if(name && trxCd) {
                const options = this.$route.params?.memberInfo?.options;
                //1. 법인카드 내역 팝업 호출
                this.autoCardSetup(name, trxCd, options, cardType);
            } else {
                if(taxInfo && taxInfo.trxTypeCd && taxInfo.evidenceName) {
                    if(!taxInfo.empNo) {
                        if(this.$route.params.slipHeaderId === undefined){
                           //임직원 자동 세팅
                           this.empOpenEnterModal(name);
                        }
                    }
                    this.setDefaultTaxSlip(taxInfo);
                } else {
                    if(this.$route.params.slipHeaderId === undefined){
                        //임직원 자동 세팅
                        this.empOpenEnterModal(name);
                    }
                }
            }
        },
        /**
         * * 그리드 비활성 모드 이벤트 버스 호출
         */
        gridUpdate() {
            const { slipTypeCd, prepaymentApplyFlag } = this.value; 
            //
            const params = {
                slipTypeCd,
                editable: prepaymentApplyFlag !== 'K'
            }
            //그리드 
            this.$bus.emit('setGridEditingStatus', params)
        },
        /**
         * 전표 유형 변화
         * @param val (slipTypeCd)
         */
        slipGridUpdate(val) {
            const { slipHeaderId } = this.$route.params; 
            //1. 그리드 재 초기화 bus push 
            if(!slipHeaderId) {
                this.$bus.emit('setGridColDef', val)
            }
            //2. 서브 헤더 로드 결정
            /**
             * * 서브컴포넌트 활성할 지 결정과 함께 이벤트 발생 
             * TODO: 경조금 일 때 하나 행 추가 (** 행추가 버튼 비활성화 상태이기 때문에 **) 그 밖에 값들에 대해서도 체크할 것.
             * @param {*} typeCode 
             */
            const load = (typeCode) => {
                if (typeCode === 'EXPEND') {
                    
                    if(!slipHeaderId) { //전표번호가 있는 경우는 행 추가 안함.
                        this.$bus.emit('addRow');
                    }
                }
                return ['EXPEND', 'TRAFFIC', 'TRIP', 'BOND', 'AWT', 'ETCAWT', 'MCARD', 'ACARD', 'CARD', 'PO', 'IM'].includes(typeCode);
            }
            this.status.loaded = load(val);
        },
        /**
         * * 변수 초기화 함수
         * @param {*} target 
         * @returns 
         */
        initialize(target) {
            const self = this;
            const func = {
                /**
                 * 직원초기화
                 */
                initEmp() {
                    self.value.deptCd = '';
                    self.value.deptNm = '';
                    self.value.empNo = '';
                    self.value.empNm = '';
                    self.value.orgId = '';
                    self.value.ledgerId = '';
                    self.value.personId = '';
                    self.value.budgetDeptCode = ''; //헤더 예산부서코드
                    self.value.actualDeptCode = ''; //헤더 귀속부서코드
                    self.value.itemGroupCode = ''; //헤더 제품군코드
                    self.value.projectCode = ''; //헤더 프로젝트 코드
                    self.value.trBankAcctCode = ''; //헤더 금융계좌코드
                    self.value.compCode = ''; //헤더 법인코드
                    self.value.segment9Code = ''; //헤더 여분1코드
                    self.value.segment10Code = ''; //헤더 여분1코드
                    self.value.taskId = '';
                    self.value.taskCode = '';
                    self.value.taskName = '';
                    self.value.postingDt = ''; //회계일자 별도
                    //
                    self.value.description = ''; //헤더 적요
                    self.value.remark = ''; //공급자 비고
                    self.value.termDueDate = ''; //결제예정일
                    self.value.repaymentDueDate = ''; //반제예정일
                    self.value.curCd = 'KRW';//통화
                    self.value.excRt = 1; //환율
                    self.value.taxLocationCode = ''; //사업장코드
                    self.value.taxLocationName = ''; //사업장
                    self.value.description = '';
                },
                /**
                 * 거래유형 초기화
                 */
                initTrxType() {
                    self.value.acctCode = ''; //헤더 계정코드 (중요)
                    self.value.acctName = '';
                    self.value.trxTypeCode = '';
                    self.value.trxTypeName = '';
                    self.value.slipTypeCd = '';
                    self.value.ttypeInterfaceModule = '';
                    self.value.ttypeInterfaceSlipType = ''
                    self.value.lineAttribute2 = '';
                    self.value.ttypeInputModule = ''; //거래유형 출처
                    self.value.ttypePrepaymentFlag = ''; //거래유형 선급여부
                    self.value.ttypeAddInfoType = ''; //거래유형 세부유형
                    self.value.slipDisplayFlag = 'Y'; //전표표시 대상여부
                    self.value.slipCreationTargetFlag = 'Y'; //ERP전표생성 대상 여부 flag
                    self.value.drCr = '';
                    self.value.postingDt = ''; //회계일자 별도
                    self.value.awtGroupId = ''; //원천세 관련 키
                    self.value.awtGroupNm = '';
                    self.value.awtLocationCode = '';
                    self.value.awtLocationName = '';
                    self.value.taxLocationCode = ''; //사업장코드
                    self.value.taxLocationName = ''; //사업장
                    //
                    self.value.description = ''; //헤더 적요
                    self.value.remark = ''; //공급자 비고
                    self.value.termDueDate = ''; //결제예정일
                    self.value.repaymentDueDate = ''; //반제예정일
                    self.value.curCd = 'KRW';//통화
                    self.value.excRt = 1; //환율
                    this.initVendor();
                    this.initEvidence();
                    this.initAmounts();
                    this.initTaxBill();
                    this.initTerm();
                    this.initBank();
                    self.$bus.emit('setGridColDef', self.value.slipTypeCd)
                },
                /**
                 * 세무증빙 유형 초기화
                 */
                initEvidence() {
                    self.value.taxEvidenceTypeName = '';
                    self.value.taxEvidenceType = '';
                    self.value.lineAttribute1 = '';
                    self.value.lineAttribute1Name = '';
                    self.value.lineAttribute3 = '';
                    self.value.lineAttribute6 = '';
                    self.isTaxReadOnly = true;
                },
                /**
                 * 헤더 전표 금액 초기화
                 */
                initAmounts() {
                    self.value.totAmt = 0;
                    self.value.totAmtKrw = 0;
                    self.value.realAmt = 0;
                },
                /**
                 * 세금계산서 초기화
                 */
                initTaxBill() {
                    self.value.evidenceDate = '';
                    self.value.taxSmartbillNo = ''; 
                    self.value.taxbillSuId = ''; 
                    self.value.dtiType = '';
                    self.value.taxbillSupplyAmt = 0; //세금계산서 공급가액
                    self.value.taxbillTaxAmt = 0; //세금계산서 세액
                    self.value.taxbillTotalAmt = 0; //세금계산서 합계
                    self.value.taxStatusCode = '';
                    self.value.percentageRate = 0;
                    self.value.taxRateId = '';
                    self.value.taxRateCode = '';
                    self.value.taxAcctCode = '';
                    self.value.taxAcctName = ''; //저장 시 필요한 듯
                    self.value.supplyAmount = 0;
                    self.value.taxAmount = 0;
                    self.value.totalAmount = 0;
                    self.value.vSupplyAmount = 0;
                    self.value.vTaxAmount = 0;
                    self.value.vTotalAmount = 0;
                },
                /**
                 * * 계좌정보 초기화
                 */
                initBank() {
                    self.value.bankAccountId = '';
                    self.value.bankAccountName = '';
                    self.value.bankAccountNumber = '';
                    self.value.bankName = '';
                    self.value.bankAcct = '';
                },
                /**
                 * * 결제조건 초기화
                 */
                initTerm() {
                    self.value.termId = '';
                    self.value.termName = '';
                    self.value.termDescription = '';
                    self.value.noteFlag = 'N';
                    self.value.paymentReceiptMethodCode = '';
                    self.value.vendorAcctCheck = '';
                },
                /** 거래처 초기화 */
                initVendor(){
                    self.value.integrationVendorNum = ''; //거래처ID
                    self.value.integrationVendorName = ''; // 거래처명
                    self.value.vatRegistrationNum = ''; //부가세 등록 계좌번호
                    self.value.prepayCnt = '';
                    self.value.termId = ''; //거래처 결재조건 ID
                    self.value.termName = ''; //거래처 결재조건명
                    self.value.termDescription = ''; //거래처 결재 방법
                    self.value.noteFlag = 'N'; //어음여부
                    self.value.vendorId = ''; //거래처 ID
                    self.value.vendorNum = ''; //거래처 코드
                    self.value.vendorSiteId = ''; //거래처 사이트 ID
                    self.value.venPaymentCurrencyCd = 'KRW'; //거래처 통화코드
                    self.value.payGroupLookupCode = ''; //
                    self.value.vendorPartyId = '';
                    self.value.vendorPartySiteId = '';
                    self.value.customerId = '';
                    self.value.customerSiteId = '';
                    self.value.customerPartyId = '';
                    self.value.customerPartySiteId = ''
                    self.value.termDueDate = ''; //결제예정일
                    self.value.repaymentDueDate = ''; //반제예정일
                },
                /** 증빙거래처 초기화(부가세) */
                initEvidenceVendor() {
                    self.value.evidenceVendorNum = ''; //증빙거래처
                    self.value.evidenceVendorName = ''; //증빙거래처명
                    self.value.evidenceVendorVat = ''; //사업자번호
                    self.value.evidenceVendorCustSiteId =  ''; //증빙거래처ID
                },
                /** 경조금 초기화 */
                initExpend() {
                    self.value.expend.expendDt = ''
                    self.value.expend.expendCd = '';//경조구분
                    self.value.expend.expendNm = ''; //경조구분명 (넘기는 값은 아님.)
                    self.value.expend.expendRelation = ''; //신청인과의 관계
                    self.value.expend.expendReceiveNm = ''; //당사자 성명
                    self.value.expend.expendAmt = 0; //회사 지급액
                    self.value.expend.wreathYn = 'Y'; //경조 화환
                    self.value.expend.holiday = ''; //휴가일
                    self.value.expend.mutualYn = 'N'; //상조용품
                },
                /** 여비교통비 초기화 */
                initTrffic() {
                    self.value.traffic.temp2 = 'Y'; //해당여부
                    self.value.traffic.tripCd = 'C'; //교통비
                    self.value.traffic.tripObj = ''; //내용
                },
                /** 출장비 초기화 */
                initTrip() {
                    self.value.trip = [];
                },
                /** BOND 초기화 */
                initBond() {
                    self.value.bond.splitEtcYn = 'N'; //분할 및 기타비용
                    self.value.bond.bondGubun = ''; //BOND 종류
                    self.value.bond.type = 'LOCAL'; //수수료 유형 (국내 or 해외)
                    self.value.bond.benCountryCd = ''; //수익자 국가 코드
                    self.value.bond.benCountry = ''; //수익자 국가
                    self.value.bond.refNo = '', //REF NO. (신규/AMEND)
                    self.value.bond.amendText = '신규';//REF NO. (신규/AMEND)
                    self.value.bond.projectNm = '';//PJT 명
                    self.value.bond.customerNm = ''; //고객명
                    self.value.bond.openingDt = ''; //개설일
                    self.value.bond.currencyCd = '';//통화
                    self.value.bond.currencyNm = '';//통화
                    self.value.bond.guaranteeAmt = 0; //보증금액
                    self.value.bond.maturityDt = ''; //만기일
                    self.value.bond.rate = 0.0; //요율
                    self.value.bond.localBankNm = ''; //은행
                    self.value.bond.intBankNm = ''; //은행
                    self.value.bond.budget = 0; //예산
                    self.value.bond.amendSeq = 0;
                    self.value.bond.amendCount = 0;
                },
                /** 원천세 초기화 */
                initAwt() {
                    self.value.awt = [];
                },
                /** 원천세(잡급비) 초기화 */
                initEtcAwt() {
                    self.value.etcawt = [];
                },
                /** 서브헤더 초기화 */
                initSubDatas() {
                    this.initExpend();
                    this.initTrffic();
                    this.initTrip();
                    this.initBond();
                    this.initAwt();
                    this.initEtcAwt();
                },
                all() {
                    this.initEmp();
                    this.initTrxType();
                    this.initEvidence();
                    this.initAmounts();
                    this.initTaxBill();
                    this.initTerm();
                    this.initBank();
                    this.initVendor();
                    this.initEvidenceVendor();
                    this.initSubDatas();
                    // this.initExpend();
                    // self.$bus.emit('setGridColDef', self.value.slipTypeCd)
                }
            };

            return new Promise((resolve) => {
                resolve(func[target]());
            });
        },
        // 직원 관련 autocomplate 이벤트 관련 함수 e.
        /**
         * * 통화목록 호출
         * @returns 
         */
        getFrgnCurCd() {
            return this.$http.get('/api/code/combo', { params: { groupCd: 'FRGN_CUR_CD' } });
        },
        /**
         * * 환율 업데이트 함수
         * @param {*} value 
         * @param {*} refresh //그리드 공급가액 초기화 여부
         */
        exchangeUpdate(value, refresh) {
            const {curCd, exchangeDate} = value;

            this.$http.post(`/api/exchange/rate/${exchangeDate}/${curCd}`)
            .then(res => res.data)
            .then(data => {
                if(data.length > 0) {
                    this.value.curCd = data[0]?.fromCurrency || 'KRW';
                    this.value.excRt = this.$numeral(data[0]?.conversionRate).value() || 1;
                    if(data.fromCurrency === 'JPY'){
                        this.value.totAmtKrw =  this.$numeral(Math.round(this.$numeral(this.value.totAmt * this.value.excRt * 0.01).value())).format(`0,0`);
                    }else {
                        this.value.totAmtKrw = this.$numeral(Math.round((this.value.totAmt * this.value.excRt))).format(`0,0`);
                    }
                } else {
                    if(curCd == 'KRW') {
                        this.value.curCd = 'KRW';
                        this.value.excRt = 1;
                        this.value.totAmtKrw = this.$numeral(Math.round((this.$numeral(this.value.totAmt).value() * this.$numeral(this.value.excRt).value()))).format(`0,0`);
                        this.gridUpdate();
                        if(refresh) {
                            this.$bus.emit('supplyAmountInit');
                        }
                    } else {
                        this.$alert(`환율정보가 없어 원화로 재설정합니다.`, '확인', {
                            dangerouslyUseHTMLString: true,
                            confirmButtonText: '확인',
                            type: 'error',
                            center: true,
                            callback: () => {
                                this.value.curCd = 'KRW';
                                this.value.excRt = 1;
                                this.value.totAmtKrw = this.$numeral(Math.round((this.$numeral(this.value.totAmt).value() * this.$numeral(this.value.excRt).value()))).format(`0,0`);
                                this.gridUpdate();
                                if(refresh) {
                                    this.$bus.emit('supplyAmountInit');
                                }
                            }
                        });
                    }
                }
            })
            .catch(err => {
                console.log(err);
            })
            .finally(_ => {
                if(refresh) {
                    this.$bus.emit('supplyAmountInit');
                }
                // this.initialize(`initTerm`);
                // this.initialize(`initBank`);
                // this.initialize(`initVendor`);
                this.calcRealAmt();
                this.gridUpdate();
                //공급자/결제조건에 따른 결제예정일 계산
                this.termDueDateCalculator();
            });
        },
        /**
         * 법인카드로부터 라우팅 되어졌을 때 자동 셋업 함수
         * @param {*} empNo 
         * @param {*} cardCd 
         * @param {*} options **optional** { 사용방법 강구 }
         * @param {*} cardType (A : 항공권 외, F : 항공권)
         */
        autoCardSetup(empNo, cardCd, options = {}, cardType = 'A') {
            if(!empNo) {
                this.initialize(`initEmp`);
                this.$message({ type: 'warning', message: '사원번호가 누락되었습니다.' });
                return false;
            }
            this.$http.get(`/api/emp/pop/delegate/list/${empNo}`).then( ( res ) => res.data )
            .then( data => {
                if(data.length === 1) {
                    const { empNo, empNm, deptCd, cctrCd, deptNm, ledgerId, personId, 
                        taskNo, taskNm, taskId, productCd, pjtCd, trAcctCd, slipTypeCd, 
                        segment1Cd, segment9Cd, segment10Cd, compCd, attribute2,
                        taxLocationCode, taxLocationName
                    } = data[0];
                    this.value.deptCd = deptCd;
                    this.value.deptNm = deptNm;
                    this.value.empNo = empNo;
                    this.value.empNm = empNm;
                    this.value.orgId = compCd;
                    this.value.ledgerId = ledgerId;
                    this.value.personId = personId;

                    this.value.budgetDeptCode = cctrCd || ''; //헤더 예산부서코드
                    this.value.actualDeptCode = deptCd || ''; //헤더 귀속부서코드
                    this.value.itemGroupCode = productCd || ''; //헤더 제품군코드
                    this.value.projectCode = pjtCd || ''; //헤더 프로젝트 코드
                    this.value.trBankAcctCode = trAcctCd || ''; //헤더 금융계좌코드
                    this.value.slipTypeCode = slipTypeCd || '11'; // 디폴트 보조 원장 코드
                    this.value.compCode = segment1Cd || ''; //헤더 법인코드
                    this.value.segment9Code = segment9Cd || ''; //헤더 여분1코드
                    this.value.segment10Code = segment10Cd || ''; //헤더 여분1코드
                    this.value.taskId = taskId || '';
                    this.value.taskCode = taskNo || '';
                    this.value.taskName = taskNm || '';
                    this.value.taxLocationCode = taxLocationCode;
                    this.value.taxLocationName = taxLocationName;
                    this.value.deptType = attribute2; //(귀속) 판관/제조/공통 판단

                    //오늘 날짜로 회계일자 세팅
                    this.value.postingDt = this.$moment().format('YYYYMMDD');

                    //거래유형 세팅
                    const params = {
                        trxSpTypeCd: 10, //전표유형 ( 발생전표 : 10, 정산전표: 20, 원장전표 : 30 )
                        personId: this.value.personId,
                        trxTypeCd: cardCd // 거래유형코드로 검색
                    }
                    this.$http.post(`/api/trx/slip/list`, params)
                    .then( ( res ) => res.data )
                    .then( data => {
                        if(data.length === 1) {
                            if(['PO', 'IM'].includes(data[0].slipTypeCd)) {
                                this.$alert(`작성 불가능한 거래유형입니다.`, '확인', {
                                    dangerouslyUseHTMLString: true,
                                    confirmButtonText: '확인',
                                    type: 'error',
                                    center: true
                                });
                                return false;
                            }
                            const { acctCd, acctNm, trxTypeCd, trxTypeNm, 
                                slipTypeCd, interfaceModule, interfaceSlipType, 
                                lineAttribute2, lineAttribute3, inputModule, prepaymentFlag, 
                                addInfoType, drCr, slipDisplayFlag, slipCreationTargetFlag,
                                integrationVendorNum, userVendorFlag, termNm, paymentReceiptTermId, awtGroupId, awtGroupNm, locationCode, locationName, trxTypeDescription} = data[0];
        
                            this.value.slipTypeCd = slipTypeCd; //전표유형 코드(전자전표)
                            this.value.acctCode = acctCd; //헤더 계정코드 (중요)
                            this.value.acctName = acctNm;
                            this.value.trxTypeCode = trxTypeCd; //거래유형코드
                            this.value.trxTypeName = trxTypeNm;
                            this.value.ttypeInterfaceModule = interfaceModule;
                            this.value.ttypeInterfaceSlipType = interfaceSlipType;
                            this.value.lineAttribute2 = lineAttribute2;
                            this.value.ttypeInputModule = inputModule; //거래유형 출처
                            this.value.ttypePrepaymentFlag = prepaymentFlag; //거래유형 선급여부
                            this.value.ttypeAddInfoType = addInfoType; //거래유형 세부유형
                            this.value.slipDisplayFlag = slipDisplayFlag; //전표표시 대상여부
                            this.value.slipCreationTargetFlag = slipCreationTargetFlag; //ERP전표생성 대상 여부 flag
                            this.value.description = trxTypeDescription;
                            this.value.drCr = drCr; //차대구분
        
                            //거래처 / 은행 등 세팅
                            this.setDefaultVendorTermsBank(integrationVendorNum, userVendorFlag, interfaceModule, paymentReceiptTermId, termNm)
                            .finally(_ => {
                                // 세무증빙 유형 등 세팅 
                                this.setDefaultTaxEvidenceCode()
                                .then(_ => {
                                    //3. 법인카드 내역 팝업 호출
                                    this.$parent.btnAction({
                                        action: cardType === 'A' ? 'cardUsedHst' : 'acardUsedHst',
                                        act_type: 'popup',
                                        options
                                    });
                                })
                            });
                        }
                    });
                } else {
                    this.$message({ type: 'warning', message: '사원이 존재하지 않습니다.' });
                }
            });
        },
        /**
         * * 직원검색 엔터 이벤트
         * @param {*} search 
         */
        empOpenEnterModal(search) {
            if(!search) {
                this.initialize(`initEmp`);
                this.$message({ type: 'warning', message: '직원명을 입력하세요.' });
                return false;
            }
            
            this.$http.get(`/api/emp/pop/delegate/list/${search}`)
            .then( ( res ) => res.data )
            .then( data => {
                if(data.length === 1) {
                    const { empNo, empNm, deptCd, cctrCd, deptNm, ledgerId, personId, 
                        taskNo, taskNm, taskId, productCd, pjtCd, trAcctCd, slipTypeCd, 
                        segment1Cd, segment9Cd, segment10Cd, compCd, attribute2,
                        taxLocationCode, taxLocationName
                    } = data[0];
                    this.value.deptCd = deptCd;
                    this.value.deptNm = deptNm;
                    this.value.empNo = empNo;
                    this.value.empNm = empNm;
                    this.value.orgId = compCd;
                    this.value.ledgerId = ledgerId;
                    this.value.personId = personId;

                    this.value.budgetDeptCode = cctrCd || ''; //헤더 예산부서코드
                    this.value.actualDeptCode = deptCd || ''; //헤더 귀속부서코드
                    this.value.itemGroupCode = productCd || ''; //헤더 제품군코드
                    this.value.projectCode = pjtCd || ''; //헤더 프로젝트 코드
                    this.value.trBankAcctCode = trAcctCd || ''; //헤더 금융계좌코드
                    this.value.slipTypeCode = slipTypeCd || '11'; // 디폴트 보조 원장 코드
                    this.value.compCode = segment1Cd || ''; //헤더 법인코드
                    this.value.segment9Code = segment9Cd || ''; //헤더 여분1코드
                    this.value.segment10Code = segment10Cd || ''; //헤더 여분1코드
                    this.value.taskId = taskId || '';
                    this.value.taskCode = taskNo || '';
                    this.value.taskName = taskNm || '';
                    this.value.taxLocationCode = taxLocationCode;
                    this.value.taxLocationName = taxLocationName;

                    this.value.deptType = attribute2; //(귀속) 판관/제조/공통 판단

                } else {
                    this.empOpenModal(search);
                }
            })
        },
        /**
         * * 직원검색
         * @param {*} search 
         */
        empOpenModal(search) {
            const self = this;
            //DelegatorModal
            this.$modal.open({
                component: EmpModal,
                fullScreen: true,
                props: {
                    param: search,
                    type: 'accural',
                },
                parent: this,
                width: 1200,
                events: {
                    close(obj) {

                        const { empNo, empNm, deptCd, cctrCd, deptNm, ledgerId, personId, 
                            taskNo, taskNm, taskId, productCd, pjtCd, trAcctCd, slipTypeCd, 
                            segment1Cd, segment9Cd, segment10Cd , compCd,
                            taxLocationCode, taxLocationName
                        } = obj;
                        self.value.deptCd = deptCd;
                        self.value.deptNm = deptNm;
                        self.value.empNo = empNo;
                        self.value.empNm = empNm;
                        self.value.orgId = compCd;
                        self.value.ledgerId = ledgerId;
                        self.value.personId = personId;

                        self.value.budgetDeptCode = cctrCd || ''; //헤더 예산부서코드
                        self.value.actualDeptCode = deptCd || ''; //헤더 귀속부서코드
                        self.value.itemGroupCode = productCd || ''; //헤더 제품군코드
                        self.value.projectCode = pjtCd || ''; //헤더 프로젝트 코드
                        self.value.trBankAcctCode = trAcctCd || ''; //헤더 금융계좌코드
                        self.value.slipTypeCode = slipTypeCd || '11'; // 디폴트 보조 원장 코드
                        self.value.compCode = segment1Cd || ''; //헤더 법인코드
                        self.value.segment9Code = segment9Cd || ''; //헤더 여분1코드
                        self.value.segment10Code = segment10Cd || ''; //헤더 여분1코드

                        self.value.taskId = taskId || '';
                        self.value.taskCode = taskNo || '';
                        self.value.taskName = taskNm || '';
                        self.value.taxLocationCode = taxLocationCode;
                        self.value.taxLocationName = taxLocationName;
                    },
                    
                }
            });
        },
        /**
         * * 거래유형 엔터 이벤트
         * @param {*} search 
         */
        trxOpenEnterModal(search) {
            if(!this.value.personId) {
                this.$alert(`직원선택을 먼저 해주세요.`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'error',
                    center: true
                });
                return false;
            }

            if(this.$route.params?.memberInfo?.trxNm) { 
                //TODO : 회계일자를 오늘로 자동 세팅 해줘야하는지 확인 .
                // this.value.postingDt = this.$moment().format('YYYYMMDD');
            } else {
                if(!this.value.postingDt) {
                    this.$alert(`회계일자를 선택해주세요.`, '확인', {
                        dangerouslyUseHTMLString: true,
                        confirmButtonText: '확인',
                        type: 'error',
                        center: true
                    });
                    return false;
                }
            }

            if(!search) {
                this.initialize(`initTrxType`);
                this.$message({ type: 'warning', message: '거래유형명을 입력하세요.' });
                return false;
            }
            const params = {
                trxSpTypeCd: 10, //전표유형 ( 발생전표 : 10, 정산전표: 20, 원장전표 : 30 )
                personId: this.value.personId,
                trxTypeNm: search
            }
            this.$http.post(`/api/trx/slip/list`, params)
            .then( ( res ) => res.data )
            .then( data => {
                if(data.length === 1) {
                    if(['PO', 'IM'].includes(data[0].slipTypeCd)) {
                        this.$alert(`작성 불가능한 거래유형입니다.`, '확인', {
                            dangerouslyUseHTMLString: true,
                            confirmButtonText: '확인',
                            type: 'error',
                            center: true
                        });
                        return false;
                    }
                    const { acctCd, acctNm, trxTypeCd, trxTypeNm, 
                        slipTypeCd, interfaceModule, interfaceSlipType, 
                        lineAttribute2, lineAttribute3, inputModule, prepaymentFlag, 
                        addInfoType, drCr, slipDisplayFlag, slipCreationTargetFlag,
                        integrationVendorNum, userVendorFlag, termNm, paymentReceiptTermId, awtGroupId, awtGroupNm, locationCode, locationName, trxTypeDescription} = data[0];

                    this.value.slipTypeCd = slipTypeCd; //전표유형 코드(전자전표)
                    this.value.acctCode = acctCd; //헤더 계정코드 (중요)
                    this.value.acctName = acctNm;
                    this.value.trxTypeCode = trxTypeCd; //거래유형코드
                    this.value.trxTypeName = trxTypeNm;
                    this.value.ttypeInterfaceModule = interfaceModule;
                    this.value.ttypeInterfaceSlipType = interfaceSlipType;
                    this.value.lineAttribute2 = lineAttribute2;

                    this.value.ttypeInputModule = inputModule; //거래유형 출처
                    this.value.ttypePrepaymentFlag = prepaymentFlag; //거래유형 선급여부

                    this.value.ttypeAddInfoType = addInfoType; //거래유형 세부유형
                    this.value.slipDisplayFlag = slipDisplayFlag; //전표표시 대상여부
                    this.value.slipCreationTargetFlag = slipCreationTargetFlag; //ERP전표생성 대상 여부 flag

                    if(['AWT', 'ETCAWT'].includes(slipTypeCd)) {
                        this.value.awtGroupId = awtGroupId; //원천세 관련 키
                        this.value.awtGroupNm = awtGroupNm;
                        this.value.awtLocationCode = locationCode;
                        this.value.awtLocationName = locationName;
                        this.value.taxLocationCode = locationCode;
                        this.value.taxLocationName = locationName;
                    }
                    this.value.description = trxTypeDescription;
                    this.value.drCr = drCr; //차대구분

                    // console.log(" 거래유형 엔터 ", paymentReceiptTermId)
                    this.setDefaultVendorTermsBank(integrationVendorNum, userVendorFlag, interfaceModule, paymentReceiptTermId, termNm);
                    
                    this.setDefaultTaxEvidenceCode()
                } else {
                    this.trxOpenModal(search);
                }
            });
        },
        /**
         * * 거래유형 팝업
         */
        trxOpenModal(search) {
            if(!this.value.personId) {
                this.$alert(`직원선택을 먼저 해주세요.`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'error',
                    center: true
                });
                return false;
            }
            if(!this.value.postingDt) {
                this.$alert(`회계일자를 선택해주세요.`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'error',
                    center: true
                });
                return false;
            }
            const self = this;
            this.$modal.open({
                component: TradeTypeModal,
                props: {
                    schTxt: search,
                    personId: parseInt(this.value.personId)
                },
                parent: this,
                width: 1400,
                events: {
                    close(val) {
                        const {
                            acctCd, acctNm, trxTypeCd, trxTypeNm, slipTypeCd, interfaceModule, interfaceSlipType, lineAttribute2, lineAttribute3,
                            inputModule, prepaymentFlag, addInfoType, drCr, slipDisplayFlag, slipCreationTargetFlag,
                            integrationVendorNum, userVendorFlag, termNm, paymentReceiptTermId, awtGroupId, awtGroupNm, locationCode, locationName, trxTypeDescription
                        } = val;
                        self.initialize('initSubDatas');

                        self.value.slipTypeCd = slipTypeCd; //전표유형코드(전자전표)
                        self.value.acctCode = acctCd; //헤더 계정코드 (중요)
                        self.value.acctName = acctNm;
                        self.value.trxTypeCode = trxTypeCd;
                        self.value.trxTypeName = trxTypeNm;
                        self.value.ttypeInterfaceModule = interfaceModule;
                        self.value.ttypeInterfaceSlipType = interfaceSlipType;
                        self.value.lineAttribute2 = lineAttribute2;

                        if(['AWT', 'ETCAWT'].includes(slipTypeCd)) {
                            self.value.awtGroupId = awtGroupId; //원천세 관련 키
                            self.value.awtGroupNm = awtGroupNm;
                            self.value.awtLocationCode = locationCode;
                            self.value.awtLocationName = locationName;
                            self.value.taxLocationCode = locationCode;
                            self.value.taxLocationName = locationName;
                        }

                        self.value.description = trxTypeDescription;
                        self.value.ttypeInputModule = inputModule; //거래유형 출처
                        self.value.ttypePrepaymentFlag = prepaymentFlag; //거래유형 선급여부
                        self.value.ttypeAddInfoType = addInfoType; //거래유형 세부유형
                        self.value.slipDisplayFlag = slipDisplayFlag; //전표표시 대상여부
                        self.value.slipCreationTargetFlag = slipCreationTargetFlag; //ERP전표생성 대상 여부 flag
                        self.value.drCr = drCr;

                        self.setDefaultVendorTermsBank(integrationVendorNum, userVendorFlag, interfaceModule, paymentReceiptTermId, termNm);
                        
                        self.setDefaultTaxEvidenceCode();
                    }
                }
            });
        },
        /**
         * * 거래처 팝업
         */
        vendorOpenModal(search) {

            if(!this.value.postingDt) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '회계일자를 선택해주세요.' });
                return false;
            }

            if(!this.value.trxTypeCode) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '거래유형을 선택해주세요.' });
                return false;
            }

            if(!this.value.taxEvidenceType) {
                this.$message.error({ type: 'danger', message: '세무증빙유형을 선택해주세요.' });
                return false;
            }
            
            const apFlag = this.value.ttypeInterfaceModule === 'AP' ? 'INV' : null;
            const arFlag = this.value.ttypeInterfaceModule === 'AR' ? 'TRX' : null;
            
            const self = this;
            this.$modal.open({
                component: VendorModal,
                props: {
                    slipTypeCd: this.value.slipTypeCd,
                    arFlag,
                    apFlag,
                    currencyCode: this.value.curCd,
                    param: search,
                },
                parent: this,
                width: 1300,
                events: {
                    close(obj) {
                        const { integrationVendorNum, integrationVendorName, vatRegistrationNum, 
                            vendorId, vendorNum, venTermsId, venTermsName, noteAccountFlag, prepayCnt, vendorSiteId, 
                            venPaymentCurrencyCd, venPaymentDescription, venPayGroupLookupCd,
                            venPartyId, venPartySiteId, customerId, customerSiteId, customerPartyId, custPartySiteId, custPaymentTermId } = obj;
                        
                        self.value.integrationVendorNum = integrationVendorNum; //거래처ID
                        self.value.integrationVendorName = integrationVendorName; // 거래처명
                        self.value.vatRegistrationNum = vatRegistrationNum; //공급자 사업자번호
                        self.value.prepayCnt = prepayCnt; //선급금잔액 체크 변수
                        // self.value.termId = venTermsId; //거래처 결재조건 ID
                        // self.value.termName = venTermsName; //거래처 결재조건명
                        // self.value.termDescription = venPaymentDescription; //거래처 결재방법

                        if(self.value.taxbillSuId) {
                            if(self.value.taxbillSuId !== self.value.vatRegistrationNum) {
                                //알림형
                                self.$alert(`세금계산서의 공급자사업자 번호와 증빙거래처의 사업자 번호가 다릅니다. \n잘 확인하시고 상신바랍니다.`, '확인', {
                                    dangerouslyUseHTMLString: true,
                                    confirmButtonText: '확인',
                                    type: 'error',
                                    center: true
                                });
                            }
                        }

                        const trxTypeCd = self.value.ttypeInterfaceModule; //interfaceModule
                        
                        
                        // 선급금정산 팝업 호출 부분에 필요한 파라미터 세팅
                        self.value.vendorId = vendorId; //거래처 ID
                        self.value.vendorNum = vendorNum; //거래처코드
                        self.value.vendorSiteId = vendorSiteId; //거래처 사이트 ID
                        self.value.venPaymentCurrencyCd = venPaymentCurrencyCd; //거래처 통화코드
                        self.value.payGroupLookupCode = venPayGroupLookupCd; //
                        self.value.vendorPartyId = venPartyId;
                        self.value.vendorPartySiteId = venPartySiteId;
                        self.value.customerId = customerId;
                        self.value.customerSiteId = customerSiteId;
                        self.value.customerPartyId = customerPartyId;
                        self.value.customerPartySiteId = custPartySiteId;
                        self.value.custPaymentTermId = custPaymentTermId;
                        
                        //증빙거래처 기본 세팅
                        self.value.evidenceVendorNum = integrationVendorNum; // 증빙 거래처Number
                        self.value.evidenceVendorVat = vatRegistrationNum; //증빙거래처 사업자번호
                        self.value.evidenceVendorName = integrationVendorName; //증빙거래처명
                        if(self.value.ttypeInterfaceModule === 'AP') {
                            self.value.evidenceVendorCustSiteId = vendorSiteId; // 거래처 ID
                        }
                        if(self.value.ttypeInterfaceModule === 'AR') { // AR 일 때
                            self.value.evidenceVendorCustSiteId = customerSiteId; // 거래처 ID
                        }

                        //거래처에 선급금 잔액이 존재합니다. 확인 후 반재 처리 하시기 바랍니다. 알럿창 띄움.
                        if((parseInt(prepayCnt || 0) * 1) > 0 && self.value.ttypeInterfaceModule === 'AP') {
                            self.$alert(`${integrationVendorName} 거래처에 선급금 잔액이 존재합니다. <br/>확인 후 반재 처리 하시기 바랍니다.`, '확인', {
                                dangerouslyUseHTMLString: true,
                                confirmButtonText: '확인',
                                type: 'info',
                                center: true,
                                callback: () => {
                                    // 확인 여부 Y로 체크해줌.
                                    self.value.prepaymentYn = 'Y';
                                }
                            });
                        }
                        
                        const search = {
                            vendorCd: self.value.integrationVendorNum || '',
                            deptCd: self.value.actualDeptCode || '',
                            trxTypeCd,
                            currencyCd: self.value.curCd,
                            termId: trxTypeCd === 'AP' ? venTermsId : custPaymentTermId,
                        }
                        self.setDefaultTermsBank(search);
                    },
                    
                }
            });
        },
        
        /**
         * * 결제조건 엔터 팝업
         */
        termOpenEnterModal(search) {
            const self = this;
            const trxTypeCd = this.value.ttypeInterfaceModule;
            if(!this.value.postingDt) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '회계일자를 선택해주세요.' });
                return false;
            }
            if(!trxTypeCd) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '거래유형을 선택해주세요.' });
                return false;
            }

            if(!this.value.integrationVendorNum) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '공급자(거래처)를 선택해 주세요.' });
                return false;
            }

            /**
             * 결제조건 데이터 셋팅
             * @param {*} data 
             */
            const setData = (data) => {
                const { termId, name, description, notesFlag, paymentMethod, vendorAcctCheck } = data;
                this.value.termId = termId;
                this.value.termName = name;
                this.value.termDescription = description;
                this.value.noteFlag = notesFlag || 'N'; //어음여부
                this.value.paymentReceiptMethodCode = paymentMethod;
                this.value.vendorAcctCheck = vendorAcctCheck
            };

            
            const searchNm = search;
            const vendorCd = this.value.integrationVendorNum || '';
            const deptCd = this.value.actualDeptCode || '';
            const currencyCd = this.value.curCd || 'KRW';
            const params = {
                searchNm,
                trxTypeCd,
                vendorCd,
                deptCd,
                currencyCd
            }
            this.$http.post(`/api/gl/term/list`, params).then(res => res.data)
            .then(data => {
                if(data && data.length === 1) {
                    setData(data[0])
                    // 결제예정일 함수 호출
                    this.termDueDateCalculator();
        
                    const integrationVendorNum = self.value.integrationVendorNum;
                    
                    const params = {
                        integrationVendorNum,
                        noteAccountFlag: data[0].notesFlag === 'Y' ? 'NOTE' : 'CHECK',
                        currencyCode: self.value.curCd
                    };
                    //* 계좌정보 조회 */
                    this.$http.post(`/api/vendor/bankList`, params)
                    .then(res => res.data)
                    .then(data => {
                        if(this.value.noteFlag == 'Y') {
                            const { bankAccountName, bankAccountNumber, bankName, bankAccountId } = data.filter(item => item.primaryFlag == 'Y' && item.noteAccountFlag == 'NOTE')[0];
                            this.value.bankAccountId  = bankAccountId;
                            this.value.bankAccountName = bankAccountName;
                            this.value.bankAccountNumber = bankAccountNumber;
                            this.value.bankName = bankName;
                            this.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                        } else {
                            const { bankAccountName, bankAccountNumber, bankName, bankAccountId } = data.filter(item => item.primaryFlag == 'Y' && item.noteAccountFlag == 'CHECK')[0];
                            this.value.bankAccountId  = bankAccountId;
                            this.value.bankAccountName = bankAccountName;
                            this.value.bankAccountNumber = bankAccountNumber;
                            this.value.bankName = bankName;
                            this.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                        }
                    });
                } else {
                    const params = {
                        vendorCd: this.value.integrationVendorNum || '',
                        deptCd: this.value.actualDeptCode || '',
                        curCd: this.value.curCd || 'KRW',
                        schTxt: search,
                        trxTypeCd
                    }
                    this.$modal.open({
                        component: GLTermsModal,
                        props: params,
                        parent: this,
                        width: 1200,
                        events: {
                            close(val) {
                                const { termId, name, description, notesFlag, paymentMethod, vendorAcctCheck } = val;
                                setData(val);
                                // 결제예정일 함수 호출
                                self.termDueDateCalculator();
        
                                const integrationVendorNum = self.value.integrationVendorNum;
                                
                                const params = {
                                    integrationVendorNum,
                                    noteAccountFlag: notesFlag === 'Y' ? 'NOTE' : 'CHECK',
                                    currencyCode: self.value.curCd
                                };
                                //* 계좌정보 조회 */
                                self.$http.post(`/api/vendor/bankList`, params)
                                .then(res => res.data)
                                .then(data => {
                                    if(self.value.noteFlag == 'Y') {
                                        const { bankAccountName, bankAccountNumber, bankName, bankAccountId } = data.filter(item => item.primaryFlag == 'Y' && item.noteAccountFlag == 'NOTE')[0];
                                        self.value.bankAccountId  = bankAccountId;
                                        self.value.bankAccountName = bankAccountName;
                                        self.value.bankAccountNumber = bankAccountNumber;
                                        self.value.bankName = bankName;
                                        self.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                                    } else {
                                        const { bankAccountName, bankAccountNumber, bankName, bankAccountId } = data.filter(item => item.primaryFlag == 'Y' && item.noteAccountFlag == 'CHECK')[0];
                                        self.value.bankAccountId  = bankAccountId;
                                        self.value.bankAccountName = bankAccountName;
                                        self.value.bankAccountNumber = bankAccountNumber;
                                        self.value.bankName = bankName;
                                        self.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                                    }
                                });
                            }
                        }
                    });
                }

            });
        },
        /**
         * * 결제조건 팝업
         */
        termOpenModal(search) {
            const self = this;
            const trxTypeCd = this.value.ttypeInterfaceModule;
            if(!this.value.postingDt) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '회계일자를 선택해주세요.' });
                return false;
            }
            if(!trxTypeCd) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '거래유형을 선택해주세요.' });
                return false;
            }

            if(!this.value.integrationVendorNum) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '공급자(거래처)를 선택해 주세요.' });
                return false;
            }

            /**
             * 결제조건 데이터 셋팅
             * @param {*} data 
             */
            const setData = (data) => {
                const { termId, name, description, notesFlag, paymentMethod, vendorAcctCheck } = data;
                this.value.termId = termId;
                this.value.termName = name;
                this.value.termDescription = description;
                this.value.noteFlag = notesFlag || 'N'; //어음여부
                this.value.paymentReceiptMethodCode = paymentMethod;
                this.value.vendorAcctCheck = vendorAcctCheck
            };

            const params = {
                vendorCd: this.value.integrationVendorNum || '',
                deptCd: this.value.actualDeptCode || '',
                curCd: this.value.curCd || 'KRW',
                schTxt: search,
                trxTypeCd
            }
            this.$modal.open({
                component: GLTermsModal,
                props: params,
                parent: this,
                width: 1200,
                events: {
                    close(val) {
                        const { termId, name, description, notesFlag, paymentMethod, vendorAcctCheck } = val;
                        setData(val);
                        // 결제예정일 함수 호출
                        self.termDueDateCalculator();

                        const integrationVendorNum = self.value.integrationVendorNum;
                        
                        const params = {
                            integrationVendorNum,
                            noteAccountFlag: notesFlag === 'Y' ? 'NOTE' : 'CHECK',
                            currencyCode: self.value.curCd
                        };
                        //* 계좌정보 조회 */
                        self.$http.post(`/api/vendor/bankList`, params)
                        .then(res => res.data)
                        .then(data => {
                            if(self.value.noteFlag == 'Y') {
                                const { bankAccountName, bankAccountNumber, bankName, bankAccountId } = data.filter(item => item.primaryFlag == 'Y' && item.noteAccountFlag == 'NOTE')[0];
                                self.value.bankAccountId  = bankAccountId;
                                self.value.bankAccountName = bankAccountName;
                                self.value.bankAccountNumber = bankAccountNumber;
                                self.value.bankName = bankName;
                                self.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                            } else {
                                const { bankAccountName, bankAccountNumber, bankName, bankAccountId } = data.filter(item => item.primaryFlag == 'Y' && item.noteAccountFlag == 'CHECK')[0];
                                self.value.bankAccountId  = bankAccountId;
                                self.value.bankAccountName = bankAccountName;
                                self.value.bankAccountNumber = bankAccountNumber;
                                self.value.bankName = bankName;
                                self.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                            }
                        });
                    }
                }
            });
        },
        
        /**
         * * 계좌정보 팝업
         */
        vendorBanksOpenModal(search) {
            if(!this.value.integrationVendorNum) {
                this.$message.error({ type: '알림', message: '거래처를 입력해주세요.' });
                this.value.bankAccountName = '';
                this.value.bankAccountId = '';
                return false;
            }
            const self = this;
            const integrationVendorNum = this.value.integrationVendorNum || '';
            this.$modal.open({
                component: VendorBanksModal,
                props: {
                    schTxt: search,
                    integrationVendorNum,
                    noteAccountFlag: this.value.noteFlag === 'Y' ? 'NOTE' : 'CHECK',
                    curCd: this.value.curCd
                },
                parent: this,
                width: 1200,
                events: {
                    close(data) {
                        const { bankAccountId, bankAccountName, bankAccountNumber, bankName } = data;
                        self.value.bankAccountId  = bankAccountId;
                        self.value.bankAccountName = bankAccountName;
                        self.value.bankAccountNumber = bankAccountNumber;
                        self.value.bankName = bankName;
                        self.value.bankAcct = `[${bankName}] ${bankAccountNumber}`;
                    }
                }
            });
        },
        /**
         * * 세금계산서 초기화
         */
        clearTaxSmartbillNo() {
            this.$confirm('세금계산서를 초기화 하시겠습니까?', 'Warning',
                {
                    distinguishCancelAndClose: true,
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                    type: 'warning'
                })
                .then((val) => {
                    if(val) {
                        //taxbillSuId
                        this.initialize(`initTaxBill`)
                            .then(_ => {
                                this.$message({ type: 'success', message: '초기화 되었습니다.' });
                            });
                    }
                })
                .catch(_ => {
                    this.value.taxSmartbillNo = oVal;
                    this.$message({ type: 'info', message: '취소되었습니다.' });
                });
        },
        /**
         * 세금계산서 팝업
         * * 부가세 인 경우만 활성
         */
        taxMainOpenModal() {
            //isTaxReadOnly
            if(!this.value.postingDt) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '회계일자를 선택해주세요.' });
                return false;
            }
            
            
            const postingStEndDate = [
                this.$moment(this.$store.state.openDate.localMonthDate).format('YYYY-MM-DD'),
                this.$moment(this.$store.state.openDate.nextMonthDate).format('YYYY-MM-DD')
            ];
            const self = this;
            this.$modal.open({
                component: TaxMainModal,
                props: {
                    postingStEndDate,
                    values: this.value,
                    lineAttribute6: this.value.lineAttribute6 || '', //lineAttribute6 : I 이면 세액이 0 인 데이터 만 출력
                },
                parent: this,
                width: 1200,
                events: {
                    close(val) {
                        const { issueId, /** 세금계산서 승인번호*/ suId, /** 공급자 사업자번호 */ chargetotal, /** 공급가액 합계 */
                            taxtotal, /** 세액 합계 */ grandtotal, /** 총액 (공급가액 합계 + 세액 합계) */ issueDate, /** 전자세금계산서 작성일 */
                            dtiType, /** 세금유형(일반/면세/영세 등) */
                        } = val;
                        self.value.evidenceDate = self.$moment(issueDate).format("YYYYMMDD");
                        self.value.taxSmartbillNo = issueId; 
                        self.value.taxbillSuId = suId;
                        self.value.dtiType = dtiType;
                        //1. 공급자 자동 기입
                        
                        self.$http.post(`/api/vendor/list`, {
                            vatRegistrationNum: suId,
                            apFlag: null,
                            arFlag: null,
                            currencyCode: self.value.curCd,
                            page: 0,
                            limit: 1
                        })
                        .then(res => res.data)
                        .then(data => {
                            const { integrationVendorNum, integrationVendorName, vatRegistrationNum,
                                 vendorId, vendorNum, venTermsId, venTermsName, noteAccountFlag, prepayCnt, vendorSiteId,
                                 venPaymentCurrencyCd, venPaymentDescription, venPayGroupLookupCd,
                                 venPartyId, venPartySiteId, customerId, customerSiteId, customerPartyId, custPartySiteId, custPaymentTermId } = data[0];
                        
                            const trxTypeCd = self.value.ttypeInterfaceModule;
                            const search = {
                                vendorCd: integrationVendorNum || '',
                                deptCd: self.value.actualDeptCode || '',
                                currencyCd: self.value.curCd,
                                trxTypeCd,
                                termId: trxTypeCd === 'AP' ? venTermsId : custPaymentTermId,
                            }

                            self.value.integrationVendorNum = integrationVendorNum; //거래처ID
                            self.value.integrationVendorName = integrationVendorName; // 거래처명
                            self.value.vatRegistrationNum = vatRegistrationNum; //공급자 사업자 번호
                            
                            self.value.custPaymentTermId = custPaymentTermId;
                            self.value.prepayCnt = prepayCnt; //선급금잔액 체크 변수

                            self.value.evidenceVendorNum = integrationVendorNum; // 증빙 거래처Number
                            self.value.evidenceVendorVat = vatRegistrationNum; //증빙거래처 사업자번호
                            self.value.evidenceVendorName = integrationVendorName; //증빙거래처명
                            if(self.value.ttypeInterfaceModule === 'AP') {
                                self.value.evidenceVendorCustSiteId = vendorSiteId; // 거래처 ID
                            }
                            if(self.value.ttypeInterfaceModule === 'AR') { // AR 일 때
                                self.value.evidenceVendorCustSiteId = customerSiteId; // 거래처 ID
                            }

                            if(!['PO', 'IM'].includes(self.value.slipTypeCd)) {
                                self.setDefaultTermsBank(search);
                            }
                            //2. 부가세 정보 자동 기입
                            self.value.taxbillSupplyAmt = chargetotal; //세금계산서 공급가액
                            self.value.taxbillTaxAmt = taxtotal || 0; //세금계산서 세액
                            self.value.taxbillTotalAmt = grandtotal; //세금계산서 합계

                            // ------------------------ 선급금정산 팝업 호출 부분에 필요한 파라미터 세팅
                            self.value.vendorId = vendorId; //거래처 ID
                            self.value.vendorNum = vendorNum; //거래처 코드
                            self.value.vendorSiteId = vendorSiteId; //거래처 사이트 ID
                            self.value.venPaymentCurrencyCd = venPaymentCurrencyCd; //거래처 통화코드
                            self.value.payGroupLookupCode = venPayGroupLookupCd;
                            self.value.vendorPartyId = venPartyId;
                            self.value.vendorPartySiteId = venPartySiteId;
                            self.value.customerId = customerId;
                            self.value.customerSiteId = customerSiteId;
                            self.value.customerPartyId = customerPartyId;
                            self.value.customerPartySiteId = custPartySiteId;
                            

                            //거래처에 선급금 잔액이 존재합니다. 확인 후 반재 처리 하시기 바랍니다. 알럿창 띄움.
                            if((parseInt(prepayCnt || 0) * 1) > 0 && self.value.ttypeInterfaceModule === 'AP') {
                                self.$alert(`${integrationVendorName} 거래처에 선급금 잔액이 존재합니다. <br/>확인 후 반재 처리 하시기 바랍니다.`, '확인', {
                                    dangerouslyUseHTMLString: true,
                                    confirmButtonText: '확인',
                                    type: 'info',
                                    center: true,
                                    callback: () => {
                                        // 확인 여부 Y로 체크해줌.
                                        self.value.prepaymentYn = 'Y';
                                    }
                                });
                            }
                            
                        });

                        if(!['PO', 'IM'].includes(self.value.slipTypeCd)) {
                            //3. 상세내역 자동 기입( 공급가액, 세액 합계 등 )
                            self.$bus.emit('setDefaultTaxLine', {
                                chargetotal,
                                taxtotal: taxtotal || 0,
                                dtiType: dtiType || ''
                            });
                        }
                    }
                }
            });
            
        },
        /**
         * * 선급금 정산 금액 초기화
         */
        clearPrepayment() {
            this.value.amountToApply = 0;
            this.calcRealAmt();
        },
        /**
         * * 실지급 금액 계산
         */
        calcRealAmt() {
            const { curCd } = this.value;
            //통화 자리수 확인
            const AMT_LENGTH = this.options['FRGN_CUR_CD'].filter(f => f.currencyCode === curCd )[0]?.precision || 0;
            const totValue = this.$numeral(this.value.totAmtKrw).value();
            const paymntValue =  this.$numeral(this.value.amountToApply).value();
            
            const numFormats = (curCd, decimal) => {
                let format = `0,000`;
                if(decimal > 0 && curCd != 'KRW') {
                    format = `${format}.` + '#'.repeat(decimal);
                }
                return format;
            }
            
            const realAmt = parseFloat(parseFloat(totValue).toFixed(AMT_LENGTH)) - parseFloat(parseFloat(paymntValue || 0).toFixed(AMT_LENGTH));
            
            this.value.realAmt = this.$numeral(realAmt).format(numFormats(curCd,AMT_LENGTH));
        },
        /**
         * * 환율 flag 변경에 대한 회계일자 및 환율업데이트
         */
        exchangeRateEvent() {
            if(this.value.exchangeRateType == 'Y') {
                this.value.exchangeDate = this.value.postingDt;
            } else {
                //this.value.exchangeDate = this.$moment().format('YYYYMMDD');
            }
            this.value.slipDate = this.$moment(this.value.postingDt).format('YYYY-MM-DD HH:mm:ss');
            this.exchangeUpdate(this.value);
        },
        /**
         * * 금액 숫자만 입력
         * @param {*} val 
         * @returns 
         */
        amountNumberic(input, out){
            const reg = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z]/;
            // 한글, 영문 체크
            if(reg.exec(input)!==null) {
                this.value[out] = input.replace(/[^0-9]/g, 0);
            }
            // .... 만 입력하게 될 경우 체크
            if(isNaN(parseFloat(input))) {
                this.value[out] = 0;
            }
        },
        setDefaultTaxEvidenceCode() {
            return new Promise((resolve, reject) => {

                if(!this.value.personId) {
                    this.$alert(`직원선택을 먼저 해주세요.`, '확인', {
                        dangerouslyUseHTMLString: true,
                        confirmButtonText: '확인',
                        type: 'error',
                        center: true
                    });
                    return false;
                }
    
                if(!this.value.trxTypeCode) {
                    this.$message({ type: 'warning', message: '거래유형을 입력하세요.' });
                    return false;
                }
    
                this.$store.commit('loading');
                const params = { searchCd: '', searchNm: '' };
                this.$http.post(`/api/tax/evidence/code/${this.value.trxTypeCode}`, params)
                .then(res => res.data)
                .then(data => {
                    if(!!data && data.length === 1) {
                        const { evidenceCode, evidenceName, lineAttribute1, lineAttribute1Name, lineAttribute2, lineAttribute3, lineAttribute6} = data[0];
                        this.value.taxEvidenceType = evidenceCode;
                        this.value.taxEvidenceTypeName = evidenceName;
                        this.value.lineAttribute1 = lineAttribute1;
                        this.value.lineAttribute1Name = lineAttribute1Name;
                        this.value.lineAttribute3 = lineAttribute3;
                        this.value.lineAttribute6 = lineAttribute6;
                        this.taxReadOnly(lineAttribute1, lineAttribute3);
                    }
                    resolve()
                })
                .finally(() => {
                    this.$store.commit('finish');
                    this?.$refs?.subHD?.setHeaderVat();
                });
            })
        },
        /**
         * 세부증빙유형 엔터키 팝업
         * @param {*} value 
         * @returns 
         */
        taxEvidenceCodeEnterPop(value) {

            if(!this.value.personId) {
                this.$alert(`직원선택을 먼저 해주세요.`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'error',
                    center: true
                });
                return false;
            }

            if(!this.value.trxTypeCode) {
                this.$message({ type: 'warning', message: '거래유형을 입력하세요.' });
                return false;
            }

            this.$store.commit('loading');
            const params = { searchCd: '', searchNm: value };
            this.$http.post(`/api/tax/evidence/code/${this.value.trxTypeCode}`, params)
            .then(res => res.data)
            .then(data => {
                if(!!data && data.length === 1) {
                    const { evidenceCode, evidenceName, lineAttribute1, lineAttribute1Name, lineAttribute2, lineAttribute3, lineAttribute6} = data[0];
                    this.value.taxEvidenceType = evidenceCode;
                    this.value.taxEvidenceTypeName = evidenceName;
                    this.value.lineAttribute1 = lineAttribute1;
                    this.value.lineAttribute1Name = lineAttribute1Name;
                    this.value.lineAttribute3 = lineAttribute3;
                    this.value.lineAttribute6 = lineAttribute6;
                    this.taxReadOnly(lineAttribute1, lineAttribute3);
                } else {
                    this.taxEvidenceCodePop(this.value)
                }
            })
            .finally(() => {
                this.$store.commit('finish');
                this?.$refs?.subHD?.setHeaderVat();
            });

        },
        /**
         * * 세무증빙 유형 팝업
         * @param {*} values 
         * @returns 
         */
        taxEvidenceCodePop(values) {
            const { trxTypeCode, taxEvidenceTypeName } = values;
            //2. 거래유형 체크
            if(!trxTypeCode) {
                this.$message.error({ type: '알림', message: '거래유형을 선택해주세요.' });
                return false;
            }

            const self = this;
            this.$modal.open({
                component: TaxEvidenceModal,
                props: {
                    values,
                    schTxt: taxEvidenceTypeName
                },
                parent: this,
                width: 1200,
                events: {
                    close(obj) {

                        const { evidenceCode, evidenceName, lineAttribute1, lineAttribute1Name, lineAttribute2, lineAttribute3, lineAttribute6} = obj;
                        self.value.taxEvidenceType = evidenceCode;
                        self.value.taxEvidenceTypeName = evidenceName;
                        self.value.lineAttribute1 = lineAttribute1;
                        self.value.lineAttribute1Name = lineAttribute1Name;
                        self.value.lineAttribute3 = lineAttribute3;
                        self.value.lineAttribute6 = lineAttribute6;
                        self.taxReadOnly(lineAttribute1, lineAttribute3);

                        self.$refs?.subHD?.setHeaderVat();
                    }
                }
            });
        },
        /**
         * 사업장 조회 팝업
         */
        openBizPop() {
            if(!this.value.personId) {
                this.$alert(`직원선택을 먼저 해주세요.`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'error',
                    center: true
                });
                return false;
            }
            //BizModal
            const self = this;
            this.$modal.open({
                component: BizModal,
                props: {
                    deptCd: this.value.deptCd
                },
                parent: this,
                width: 800,
                events: {
                    close(obj) {
                        const { taxLocationCode, taxLocationName } = obj;
                        self.value.taxLocationCode = taxLocationCode;
                        self.value.taxLocationName = taxLocationName;
                    }
                }
            });

        },
        /**
         * 부가세 유무 세금계산서 활성/비활성 판단
         * @param {*} lineAttribute1 
         * @param {*} lineAttribute3 
         */
        taxReadOnly(lineAttribute1, lineAttribute3) {
            //--------- 부가세 유무 세금계산서 활성/비활성 판단. (isTaxReadOnly) ---------//
            // console.group(lineAttribute1, lineAttribute3, this.value.ttypeInterfaceModule)
            if(lineAttribute1 === 'V') { //부가세인 경우
                if(!this.value.evidenceDate) {
                    this.value.evidenceDate = this.value.postingDt;
                }
                
                if(lineAttribute3 === 'S') { //스마트빌 인 경우
                    if(this.value.ttypeInterfaceModule === 'AP') { //거래처
                        if(this.value.taxIssueTypeCode === '10' || !this.value.slipHeaderId) {
                            this.isTaxReadOnly = false;
                        } else {
                            this.initialize(`initTaxBill`);
                            this.initialize(`initEvidenceVendor`);
                            if(!this.value.evidenceDate) {
                                this.value.evidenceDate = this.value.postingDt;
                            }
                        }
                    } else if(this.value.ttypeInterfaceModule === 'AR') { //고객
                        if(this.value.taxIssueTypeCode === '20') {
                            this.isTaxReadOnly = false;
                        } else {
                            this.isTaxReadOnly = true;
                            this.initialize(`initTaxBill`);
                            this.initialize(`initEvidenceVendor`);
                            if(!this.value.evidenceDate) {
                                this.value.evidenceDate = this.value.postingDt;
                            }
                        }
                    } else {
                        this.isTaxReadOnly = false;
                        this.value.evidenceDate = '';
                        this.initialize(`initTaxBill`);
                    }
                } else { //스마트빌이 아닌 경우 (카드)
                    this.isTaxReadOnly = true;
                    this.value.evidenceDate = '';
                    this.initialize(`initTaxBill`);
                }
            } else { //부가세가 아닌 경우
                this.isTaxReadOnly = true;
                this.value.evidenceDate = '';
                this.initialize(`initTaxBill`);
                this.initialize(`initEvidenceVendor`);
                
                if(lineAttribute1 != "W"){
                    this.value.taxAcctCode = '';
                    this.value.percentageRate = 0;
                }
            }
        },
        /**
         * * 선급금 정산 구분 변경 이벤트
         * TODO :선급금 정산(K) 구분일 때 공급자 선택이 되어 있는지 체크 후 그리드 비활성화 및 선급금정산 팝업 노출, 반대 일 경우 선급금정산금액이 있다면 반제신청한 금액 초기화하고 그리드 활성
         * @param {*} value 
         */
        prepaymentApplyChanges(value) {

            //1. 거래유형 체크
            if(!this.value.trxTypeCode) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: '알림', message: '거래유형을 선택해주세요.' });
                return false;
            }
            //2. 공급자 체크
            if(!this.value.integrationVendorNum) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: '알림', message: '공급 거래처를 선택해주세요.' });
                return false;
            }

            //4. 선급금 확인 여부
            if(!!this.value.prepaymentYn && this.value.prepaymentYn !== 'Y') {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: 'danger', message: '선급금확인여부를 동의해주세요.' });
                return false;
            }

            //4. 상세내역 (그리드) 내용이 하나 이상 포함되어져 있는지 체크
            const { getRowData } = this.$parent.$refs.grid;
            const rowDataSize = getRowData().length || 0;
            if(!rowDataSize) {
                this.value.prepaymentApplyFlag = 'N';
                this.$message.error({ type: '알림', message: '상세내역을 입력해주세요.' });
                return false;
            }

            //5. 선급금 일 때 
            if(value == 'K') {
                this.$confirm('선급금정산 체크시 거래상세내역 수정이 불가능 하며 선급금정산 입력만 가능해 집니다. 선급금정산 입력을 하시겠습니까?', 'Warning',
                {
                    distinguishCancelAndClose: true,
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                    type: 'warning'
                })
                .then((val) => {
                    if(val) {
                        //그리드 비활성 시점
                        this.advancedPop(this.value)
                    }
                })
                .catch(() => {
                    this.value.prepaymentApplyFlag = 'N';
                })
                .finally(_ =>{
                    this.gridUpdate();
                });
            } else {
                if(this.value.amountToApply) {

                    this.$confirm(`선급금정산 내역이 존재합니다. <br/>미정산 처리 후 거래상세내역 수정이 가능해 집니다. <br/>미정산처리를 하시겠습니까?`, 'Warning',
                    {
                        dangerouslyUseHTMLString: true,
                        distinguishCancelAndClose: true,
                        confirmButtonText: '예',
                        cancelButtonText: '아니오',
                        type: 'warning'
                    })
                    .then((val) => {
                        if(val) {
                            this.advancedPop(this.value)
                        }
                    })
                    .catch(() => {
                        this.value.prepaymentApplyFlag = 'K';
                    })
                    .finally(_ => {
                        this.gridUpdate();
                    });
                } else {
                    this.value.prepaymentApplyFlag = 'N';
                    this.gridUpdate();
                }                
            }
        },
        /**
         * 세금계산서 팝업 
         * @param {*} issueId 
         */
        taxbillViewer(issueId) {
            
            const url = `http://197.200.1.19:10002/addservice/view/XXSB_DTI_PRINT_VIEW_NTS.asp?issue_id=${issueId}&SBTYPE=AP`;
            //800 x 500
            window.open(url, '_blank'); 
        },
        /**
         * * 선급금 정산 팝업
         * @param {*} values 
         */
        advancedPop(values){
            // this.value.vendorId = ''; //거래처 ID
            // this.value.vendorSiteId = ''; //거래처 사이트 ID
            // this.value.venPaymentCurrencyCd = 'KRW'; //거래처 통화코드
            const { slipHeaderId, vendorId, vendorNum, vendorSiteId, 
                venPaymentCurrencyCd, curCd,totAmt, ledgerId, postingDt, personId, prepaymentYn,
                amountToApply } = values;
            if(!slipHeaderId) {
                this.$message.error({ type: 'danger', message: '저장 이후 선급금 정산처리가 가능합니다.' });
                return false;
            }

            if(!vendorId) {
                this.$message.error({ type: 'danger', message: '거래처 코드가 없습니다. 다시 확인해주세요.' });
                return false;
            }

            const self = this;
            this.$modal.open({
                component: AdvancedModal,
                props: {
                    vendorId,
                    vendorCd: vendorNum,
                    vendorSiteId,
                    venPaymentCurrencyCd: curCd,
                    totAmt,
                    slipHeaderId,
                    ledgerId,
                    postingDt,
                    personId,
                    originPrepaymentApplyFlag: amountToApply > 0
                },
                parent: this,
                width: 1200,
                events: {
                    close(obj) {
                    },
                    apply(params) {

                        self.value.amountToApply = self.$numeral(params.amount || 0).format('0,0');
                        self.value.prepaymentApplyFlag = params.prepaymentApplyFlag;
                        setTimeout(() => {
                            self.$parent.save(self.$parent.$refs);
                        }, 100)
                    },
                    parentClose(originPrepaymentApplyFlag) {
                        self.value.prepaymentApplyFlag = originPrepaymentApplyFlag;
                    }
                }
            });
        },
        /**
         * * 최종 헤더 함수 벨리데이션 체크
         */
        validate(data) {
            
            return new Promise((resolve, reject) => {
                try {
                    const { slipTypeCd } = this.value;

                    assert.apply(this, [data.deptCd, '부서가 존재하지 않습니다. 직원을 선택해 주세요.']);
                    assert.apply(this, [data.empNo, '직원이 선택해 주세요.']);
                    assert.apply(this, [data.postingDt, '회계일자를 선택해 주세요.']);
                    assert.apply(this, [data.trxTypeCode, '거래유형을 선택해 주세요.']);
                    assert.apply(this, [data.taxLocationCode, '사업장을 선택해 주세요.']);
                    assert.apply(this, [data.excRt, '환율 값을 다시 확인해 주세요.']);
                    assert.apply(this, [data.taxEvidenceType, '세무증빙유형을 선택해 주세요.']);
                    assert.apply(this, [data.description, '적요를 작성해 주세요.']);
                    if(data.prepaymentApplyFlag === 'K') {
                        assert.apply(this, [data.amountToApply, '선급금 정산금액을 확인해주세요.']);
                    }
                    
                    assert.apply(this, [data.description, '적요를 선택해 주세요.']);

                    assert.apply(this, [data.termId, '결제조건을 선택해 주세요.']);
                    assert.apply(this, [data.termDueDate, '결제예정일을 선택해 주세요.']);
                    if(data.vendorAcctCheck == 'Y') {
                        assert.apply(this, [data.integrationVendorNum, '거래처를 선택해 주세요.']);
                        assert.apply(this, [data.bankAcct, '계좌정보를 선택해 주세요.']);
                    }
                    if(slipTypeCd === 'COMMON') {   //공통

                    }
                    if(slipTypeCd === 'TRAFFIC') {   //교통비
                        const {tripCd, temp2, tripObj} = data.slipTrafficHdDto;
                        assert.apply(this, [temp2, '자가운전 보조금 대상자 해당여부를 체크하여 주시기 바랍니다.']);
                        assert.apply(this, [tripCd, '출장유형을 선택하세요.']);
                        assert.apply(this, [tripObj, '내용이 없습니다.']);
                    }

                    if(slipTypeCd === 'EXPEND') {   //경조금
                        
                        const {
                            expendDt, //경조일시
                            expendCd, //경조구분
                            expendRelation, //신청인과의 관계
                            expendReceiveNm, //당사자 성명
                        } = data.slipExpendDto;
                        assert.apply(this, [expendDt, `내부경조내역의 경조일시를 입력해 주세요.`]);
                        assert.apply(this, [expendCd, `내부경조내역의 경조구분을 선택해 주세요.`]);
                        assert.apply(this, [expendRelation, `내부경조내역의 신청인관계를 입력해 주세요.`]);
                        assert.apply(this, [expendReceiveNm, `내부경조내역의 당사자성명을 입력해 주세요.`]);
                    }

                    if(slipTypeCd === 'BOND') {
                        const {refNo, rate, type} = data.bondHeaderDto;
                        assert.apply(this, [refNo, `REF NO가 없습니다.`]);
                        assert.apply(this, [rate, `${type === 'LOCAL' ? '국내': '해외'}요율을 입력해주세요.`]);
                        // assert.apply(this, [typeof rate === 'number', `${type === 'LOCAL' ? '국내': '해외'}요율은 숫자만 입력 가능합니다.`]);
                    }

                    //세금계산서 부가세 인 경우
                    if(data.lineAttribute1 === 'V' && !['MCARD', 'ACARD', 'CARD', 'TRIP', 'PO', 'IM'].includes(slipTypeCd)) {
                        if(!this.isTaxReadOnly) {
                            assert.apply(this, [data.taxbillSuId, '세금계산서를 선택해 주세요.']);
                            assert.apply(this, [String(data.taxbillSuId).replaceAll('-', '') === String(data.vatRegistrationNum).replaceAll('-', '') , '세금계산서의 공급자사업자 번호와 증빙거래처의 사업자 번호가 다릅니다.']);
                        }
                        assert.apply(this, [data.evidenceDate, '부가세 작성년월일을 선택해 주세요.']);
                        assert.apply(this, [data.evidenceVendorNum, '증빙거래처를 선택해 주세요.']);
                        assert.apply(this, [data.taxRateCode, '헤더부가세코드 선택해 주세요.']);
                        assert.apply(this, [data.taxIssueTypeCode, '발행구분을 선택해 주세요.']);
                    }

                    const postDt = this.$moment(data.postingDt).format('YYYYMMDD'); //회계일자
                    const termDueDt  = this.$moment(data.termDueDate).format('YYYYMMDD'); //결제예정일
                    assert.apply(this, [(postDt <= termDueDt), '회계일자 보다 결제예정일이 과거일 수 없습니다.']);
                    if(data.noteFlag === 'Y') {
                        const maturityDt = this.$moment(data.maturityDate).format('YYYYMMDD'); //어음일자
                        assert.apply(this, [(postDt <= maturityDt), '회계일자 보다 어음만기일이 과거일 수 없습니다.']);
                        assert.apply(this, [(termDueDt <= maturityDt), '결제예정일 보다 어음만기일이 과거일 수 없습니다.']);
                    }
                    const {acctCode} = this.value;

                    if(acctCode == "1115550" || acctCode == "1115530" || acctCode == "1115510" || acctCode == "1115560" || acctCode == "1115520"){
                        assert.apply(this, [data.repaymentDueDate, '반제예정일을 입력하세요.']);
                        const repaymentDueDt  = this.$moment(data.repaymentDueDate).format('YYYYMMDD'); //반제예정일
                        assert.apply(this, [(postDt < repaymentDueDt), '반제예정일이 회계일자보다 과거일 수 없습니다.']);
                    }

                    if(data.amountToApply > 0) {
                        assert.apply(this, [data.prepaymentYn == 'Y', '선급금 정산 금액이 존재합니다. 선급금확인여부를 확인하여 주세요.']);
                        
                        if((parseInt(data.prepayCnt || 0) * 1) > 0 && data.ttypeInterfaceModule === 'AP') {
                            assert.apply(this, [data.prepaymentYn == 'Y', '선급금 정산 금액이 존재합니다. 선급금확인여부를 확인하여 주세요.']);
                        }
                    }


                    resolve();
                } catch (e) {
                    if (e instanceof EvalError) {
                        console.error(e.name + ": " + e.message);
                    } else if (e instanceof RangeError) {
                        console.error(e.name + ": " + e.message);
                    } else if (e instanceof TypeError) {
                        console.error(e.name + ": " + e.message);
                        this.$swal({
                            type: 'warning',
                            text: "저장할 데이터가 존재하지 않습니다."
                        }).then(reject)
                    } else {
                        this.$swal({
                            type: 'error',
                            text: e
                        }).then(reject)
                    }
                }
            });
        },
        /**
         * * 세금계산서 보기
         * @param {*} eTaxNo 
         */
        goEtaxPage(eTaxNo) {
            this.$modal.open({
                component: ElecTaxPopup,
                parent: this,
                props: {
                    eTaxNo: eTaxNo
                }
            });
        },
        /**
         * 세금합계 계산
         * @param {*} rate 
         */
        taxCalulator(rate) {
            this.value.supplyAmount = this.value.taxbillSupplyAmt;
            this.value.taxAmount = (parseInt(this.value.supplyAmount) * (rate / 100)); //실 세금
            this.value.totalAmount = parseInt(this.value.supplyAmount) + parseInt(this.value.supplyAmount * (rate / 100)); // 실 금액 합계
            this.value.vSupplyAmount = this.value.taxbillSupplyAmt;
            this.value.vTaxAmount = (parseInt(this.value.vSupplyAmount) * (rate / 100)); //실 세금(VIEW 용)
            this.value.vTotalAmount = parseInt(this.value.vSupplyAmount) + parseInt(this.value.vSupplyAmount * (rate / 100)); // 실 금액 합계
        },
        /**
         * 결제 예정일 계산
         */
        termDueDateCalculator() {
            const {ttypeInterfaceModule, termId, postingDt, deptCd, noteFlag} = this.value;
            
            if(ttypeInterfaceModule && termId && postingDt && deptCd) {
                const postDt = this.$moment(postingDt).format('YYYY-MM-DD HH:mm:ss');
                
                const reqParams = {
                    typeInterfaceModule: ttypeInterfaceModule,
                    termId,
                    postDt,
                    noteFlag,
                    deptCd
                }
                this.$http.post(`/api/gl/term/due/date`, reqParams)
                .then(res => res.data)
                .then(data => {
                    const { termsFlag, termsStatus, termsValue, typeInterfaceModule, xdueDate, xmaturityDate } = data;
                    if(!xdueDate) {
                        this.value.termDueDate = '';
                        this.value.termDueDateChangeFlag = 'Y';
                        this.$message.error({ type: 'danger', message: '예정일 계산 대상이 아닙니다. 직접 결제예정일을 선택하시기 바랍니다.' });
                    } else {
                        if(typeInterfaceModule === 'AR') {
                            this.value.termDueDate = xdueDate;
                            this.value.termDueDateChangeFlag = 'Y';
                        } else {
                            this.$message({ type: 'warning', message: '입력한 데이터에 의해 결제예정일이 변경 되었습니다. \n결제예정일이 맞는지 확인 바랍니다' });
                            this.value.termDueDate = xdueDate;
                            this.value.termDueDateChangeFlag = termsValue || 'N';
                        }
                    }
                    if(noteFlag === 'Y') {
                        const maturityDate = xmaturityDate === 'X' ? xmaturityDate.replace('X', '') : xmaturityDate;
                        this.value.maturityDate = maturityDate ? this.$moment(maturityDate).format('YYYYMMDD') : '';
                    } else {
                        this.value.maturityDate = '';
                    }
                    //alert
                }).catch((err) => {
                    this.value.maturityDate = '';
                    this.$message.error({ type: 'danger', message: '예정일 계산 대상이 아닙니다. 직접 결제예정일을 선택하시기 바랍니다.' });
                    this.value.termDueDateChangeFlag = 'Y';
                });
            }
        },
        
        /**
         * * 회계년월에 포함되어있는 날만 활성화 되도록하는 함수
         * @param {*} time 
         */
        disabledDate (date) {
                  
            // this.value.postingDt
            const curDate = this.$moment(date).format('YYYYMMDD');
            const startDate = this.$moment(this.openDay).startOf('month').format('YYYYMMDD');
            const endOfDate = this.$moment(this.closeDay).endOf('month').format('YYYYMMDD');
            
            if(curDate >= startDate && curDate <= endOfDate) {
                return false;
            }

            return true;
        },
        
        /**
         * 정발행 코드명 가져오기
         * @param {*} code 
         * @returns 
         */
        getTaxIssueTypeName(code) {
            return new Promise((resolve, reject) => {
                const params = {
                    searchCd: '',
                    searchNm: '',
                }
                this.$http.post(`/api/gl/tax/issue/list`, params)
                .then(res => res.data)
                .then(data => {
                    resolve(data);
                })
                .catch(err => reject(err));
            });
        },
        /**
         * 기본 결재조건 & 계좌정보 세팅
         * @param {*} reqParams 
         *  vendorCd: 거래처코드
         *  deptCd: 부서
         *  searchCd: 결제고건 검색코드
         *  searchNm: 결제조건 검색명
         *  trxTypeCd : 거래유형
         *  currencyCd: 통화
         *  termId : 결제조건 id
         * * ***************************************************************************************
         * * 파라미터 termId 는 거래처에서 넘어오는 값이며, searchCd와 searchNm 은 거래유형으로부터 넘어오는 값임.
         * * 우선순위는 거래유형에서 넘어오는 searchCd (결제조건ID) 이고, 후순위 거래처에서 넘어오는 termId이므로,
         * * 해당 함수 요청 파라미터에 거래유형에서는 termId 를 제외한 값을 넘기고,
         * * 거래처에서는 searchCd, searchNm 을 제외하고 넘겨줘서 조회해야 한다.
         * * ***************************************************************************************
         */
        setDefaultTermsBank(reqParams) {
            this.$store.commit('loading');
            
            let params = Object.assign({}, reqParams, {
                searchCd : reqParams.searchCd ? reqParams.searchCd : reqParams.termId
            })

            // 2024-05-21 이동화 대리 요청 : 매입세금계산서취소 일때는 기본 결재조건이 지급보류로 들어가도록
            if(this.value.trxTypeCode === 'SPAP078'){
                params.searchNm = '보류';
                params.searchCd = 10005;
            }

            this.$http.post(`/api/gl/term/list`, params)
            .then(res => res.data)
            .then(data => {
                if(!!data && data.length > 0) {
                    const { termId, name, description, notesFlag, paymentMethod, vendorAcctCheck } = data?.filter(f => f.termId == reqParams.searchCd ? reqParams.searchCd : reqParams.termId)?.[0];
                    

                    this.value.termId = termId;
                    this.value.termName = name;
                    this.value.termDescription = description;
                    this.value.noteFlag = notesFlag || 'N'; //어음여부
                    this.value.paymentReceiptMethodCode = paymentMethod;
                    this.value.vendorAcctCheck = vendorAcctCheck;
                    // 결제예정일 함수 호출
                    this.termDueDateCalculator();

                    const params = {
                        integrationVendorNum: reqParams.vendorCd,
                        noteAccountFlag: this.value.noteFlag === 'Y' ? 'NOTE' : 'CHECK',
                        currencyCode: this.value.curCd
                    };
                    //* 계좌정보 조회 */
                    this.$http.post(`/api/vendor/bankList`, params)
                    .then(res => res.data)
                    .then(data => {
                        if(!!data && data.length > 0) {
                            if(this.value.noteFlag == 'Y') {
                                const { bankAccountName, bankAccountNumber, bankName, bankAccountId } = data.filter(item => item.primaryFlag == 'Y' && item.noteAccountFlag == 'NOTE')?.[0];
                                this.value.bankAccountId  = bankAccountId;
                                this.value.bankAccountName = bankAccountName || '';
                                this.value.bankAccountNumber = bankAccountNumber || '';
                                this.value.bankName = bankName || '';
                                this.value.bankAcct = `[${bankName || ''}] ${bankAccountNumber || ''}`;
                            } else {
                                const { bankAccountName, bankAccountNumber, bankName, bankAccountId } = data.filter(item => item.primaryFlag == 'Y' && item.noteAccountFlag == 'CHECK')?.[0];
                                this.value.bankAccountId  = bankAccountId;
                                this.value.bankAccountName = bankAccountName || '';
                                this.value.bankAccountNumber = bankAccountNumber || '';
                                this.value.bankName = bankName || '';
                                this.value.bankAcct = `[${bankName || ''}] ${bankAccountNumber || ''}`;
                            }
                        }
                    });
                } else {
                    this.$message.error({ type: 'danger', message: '예정일 계산 대상이 아닙니다. 직접 결제예정일을 선택하시기 바랍니다.' });
                    this.value.termDueDateChangeFlag = 'Y'; //결제예정일 수동 기입
                }
            })
            .finally(_ => {
                this.$store.commit('finish');
            });
        },
        /**
         * * 기본 거래처 & 결재조건 & 계좌정보 세팅
         * 
         */
        setDefaultVendorTermsBank(integrationVendorNum, userVendorFlag = 'N', trxTypeCd = 'AP', paymentReceiptTermId, termNm = '') {
            
            return new Promise((resolve, reject) => {
                const apFlag = trxTypeCd === 'AP' ? 'INV' : null;
                const arFlag = trxTypeCd === 'AR' ? 'TRX' : null;
                
                const vendorCd = userVendorFlag == 'N' ? integrationVendorNum : ''
                const vendorNm = userVendorFlag == 'Y' ? this.value.empNo : '';
    
                if(vendorCd || vendorNm) {
                    const vendorParams = {
                        integrationVendorNum: vendorCd,
                        // integrationVendorName: userVendorFlag == 'Y' ? vendorNm : vendorCd ? '': this.value.empNm,
                        integrationVendorName: vendorNm,
                        currencyCode: this.value.curCd,
                        apFlag,
                        arFlag, 
                        page: 0, limit: 100
                    }
                    this.$store.commit('loading');
                    this.$http.post(`/api/vendor/list`, vendorParams)
                    .then(res => res.data)
                    .then(data => {
                        if(!!data && data.length > 0) {
                            const { 
                                integrationVendorNum, integrationVendorName, vatRegistrationNum, 
                                venTermsId, venTermsName, vendorId, vendorNum,vendorSiteId,
                                venPaymentCurrencyCd, venPaymentDescription, venPayGroupLookupCd,
                                venPartyId, venPartySiteId, customerId, customerSiteId, customerPartyId, custPartySiteId, 
                                custPaymentTermId, custPaymentTermName, prepayCnt
                            } = data[0];
                            this.value.integrationVendorNum = integrationVendorNum; //거래처ID
                            this.value.integrationVendorName = integrationVendorName; // 거래처명
                            this.value.vatRegistrationNum = vatRegistrationNum; //공급자 사업자번호
                            this.value.termId = trxTypeCd === 'AP' ? venTermsId : custPaymentTermId; //거래처 결재조건 ID
                            this.value.termName = trxTypeCd === 'AP' ? venTermsName : custPaymentTermName; //거래처 결재조건명
                            this.value.termDescription = venPaymentDescription; //거래처 결재방법
                            this.value.prepayCnt = prepayCnt;
                            this.value.vendorId = vendorId; //거래처 ID
                            this.value.vendorNum = vendorNum; //거래처코드
                            this.value.vendorSiteId = vendorSiteId; //거래처 사이트 ID
                            this.value.venPaymentCurrencyCd = venPaymentCurrencyCd; //거래처 통화코드
                            this.value.payGroupLookupCode = venPayGroupLookupCd; //
                            this.value.vendorPartyId = venPartyId;
                            this.value.vendorPartySiteId = venPartySiteId;
                            this.value.customerId = customerId;
                            this.value.customerSiteId = customerSiteId;
                            this.value.customerPartyId = customerPartyId;
                            this.value.customerPartySiteId = custPartySiteId;
                            this.value.custPaymentTermId = custPaymentTermId;
        
                            //증빙거래처 기본 세팅
                            this.value.evidenceVendorNum = integrationVendorNum; // 증빙 거래처Number
                            this.value.evidenceVendorVat = vatRegistrationNum; //증빙거래처 사업자번호
                            this.value.evidenceVendorName = integrationVendorName; //증빙거래처명
                            if(this.value.ttypeInterfaceModule === 'AP') {
                                this.value.evidenceVendorCustSiteId = vendorSiteId; // 거래처 ID
                            }
                            if(this.value.ttypeInterfaceModule === 'AR') { // AR 일 때
                                this.value.evidenceVendorCustSiteId = customerSiteId; // 거래처 ID
                            }
        
                            // default payment Term id, name 
                            if(paymentReceiptTermId || termNm) {
                                /**
                                 * paymentReceiptTermId || termNm 기본 지정된 결제조건이 셋업되어있다면
                                 * 있는 것으로 재조회 이후 계좌정보 세팅
                                 */
                                
                                const search = {
                                    vendorCd: integrationVendorNum || '',
                                    deptCd: this.value.actualDeptCode || '',
                                    searchCd: paymentReceiptTermId || null,
                                    searchNm: termNm || null,
                                    trxTypeCd,
                                    currencyCd: this.value.curCd,
                                    // termId: trxTypeCd === 'AP' ? venTermsId : custPaymentTermId,
                                }
                                this.setDefaultTermsBank(search);
                            } else {
                                // /**
                                //  * paymentReceiptTermId || termNm 기본 지정된 결제조건이 셋업되어있지 않다면
                                //  * 거래유형의 결재방법을 가져와 조회 후 셋업
                                //  */
                                this.value.integrationVendorNum = integrationVendorNum; //거래처ID
                                this.value.integrationVendorName = integrationVendorName; // 거래처명
                                this.value.vatRegistrationNum = vatRegistrationNum; //공급자 사업자번호
                                this.value.termId = venTermsId; //거래처 결재조건 ID
                                this.value.termName = venTermsName; //거래처 결재조건명
                                this.value.termDescription = venPaymentDescription; //거래처 결재방법
        
                                
                                const search = {
                                    vendorCd: integrationVendorNum || '',
                                    deptCd: this.value.actualDeptCode || '',
                                    trxTypeCd,
                                    currencyCd: this.value.curCd,
                                    termId: trxTypeCd === 'AP' ? venTermsId : custPaymentTermId,
                                }
                                this.setDefaultTermsBank(search);
                            }
                        }
                        resolve();
                    })
                    .finally(_ => {
                        this.$store.commit('finish');
                    })
                } else {
                    reject();
                }
                
            });
        },
        /**
         * 활성 / 비활성 전표 상태별 셋업
         * * save : 저장
         * * delete : 삭제
         * * approve : 상신
         * * readApprove : 결재보기
         * * 구입/수입 물대의 경우 버튼 비활성
         * @param {*} data 
         */
        setDisabledState (data) {
            const params = {
                save: ['SV', 'VC', 'VE', ''].includes(data.slipStatus),
                delete: ['SV', 'VC', 'VE', 'AR', 'CR'].includes(data.slipStatus), // 반려된 경우도 삭제 할 수 있도록 변경
                approve: ['SV', 'VC', 'VE'].includes(data.slipStatus),
                recycle: !['SV', ''].includes(data.slipStatus),
                readApprove: !['SV', 'VC', ''].includes(data.slipStatus),
                cardException: ['SPAP004'].includes(data.trxTypeCode)
            }
            this.$bus.emit('setShowingButton', params);
        },
        
        /**
         * 세금계산서에서 넘어왔을 때의 이벤트
         * * 세금계산서으 정보는 미리 알고 있음. 
         * * dtiType이 '01', 'B01' 이면 매입전자세금계산서
         * * dtiType이 '02', 'B02' 이면 매입전자계산서
         * * 위 dtiType 으로 세무증빙유형을 세팅 할 수 있다.
         * * 거래유형의 경우 알 수 없으므로 선택하는 팝업을 띄어줘야한다.
         * * 이 후 임직원세팅 > 거래유형 > 세무증빙유형 (거래유형의 dtiType 으로 Search) > 세금계산서 순으로 재설정.
         * 
         * TODO: 임직원세팅 > 거래유형 > 세무증빙유형 (거래유형의 dtiType 으로 Search) > 세금계산서 > 거래처 > 결제조건 > 부가세 정보 > 라인 정보
         * * Promise setEmp()
         * * Promise setTrx()
         * * Promise setEvidency()
         * * Promise setTax()
         * * Promise setVendor()
         * * Promise setTerms()
         * * Promise setHeaderTax()
         * * setLine()
         * * 
         */
        setDefaultTaxSlip(taxInfo) {
            const self = this;
            
            const setTaxSlipData = (data) => {
                /** 임직원 세팅 */
                const setEmp = (empNo) => {
                    if(!empNo) {
                        this.$message({ type: 'warning', message: '사원번호를 찾을 수 없습니다.' });
                        return false;
                    }
                    return new Promise((resolve, reject) => {
                        this.$http.get(`/api/emp/pop/delegate/list/${empNo}`)
                        .then( ( res ) => res.data )
                        .then( data => {
                            if(data.length === 1) resolve(data[0]);
                            else reject({message: '사용자 정보를 찾을 수 없습니다.'});
                        })
                        .catch( (err) => reject(err) );
                    });
                };
                /** 거래유형 세팅 */
                const setTrx = (trx) => {
                    return new Promise((resolve, reject) => {
                        const params = {
                            trxSpTypeCd: 10, //전표유형 ( 발생전표 : 10, 정산전표: 20, 원장전표 : 30 )
                            personId: self.value.personId,
                            trxTypeCd: trx.trxTypeCd
                        }
                        this.$http.post(`/api/trx/slip/list`, params)
                        .then( ( res ) => res.data )
                        .then( data => {
                            if(data.length === 1) resolve(data[0]);
                            else reject({message: '정확히 일치하는 거래유형을 알 수 없습니다.'});
                        })
                        .catch( (err) => reject(err) );
                    })
                };
                /** 증빙유형 세팅 */
                const setEvidency = (evid) => {
                    return new Promise((resolve, reject) => {
                        const params = { searchCd: evid.evidenceCode, searchNm: evid.evidenceName };
                        this.$http.post(`/api/tax/evidence/code/${self.value.trxTypeCode}`, params)
                        .then(res => res.data)
                        .then(data => {
                            if(data.length === 1) resolve(data[0]);
                            else reject({message: '정확히 일치하는 증빙유형을 알 수 없습니다.'});
                        })
                        .catch( (err) => reject(err) );
                    })
                }
                /** 세금계산서 세팅 */
                const setTax = (tax) => {
                    return new Promise((resolve, reject) => {
                        resolve(tax);
                    })
                };
                /** 거래처 세팅 */
                const setVendor = (vendor) => {
                    return new Promise((resolve, reject) => {
                        this.$http.post(`/api/vendor/list`, {
                            vatRegistrationNum: vendor.taxbillSuId,
                            apFlag: null,
                            arFlag: null,
                            currencyCode: vendor.curCd,
                            page: 0,
                            limit: 1
                        })
                        .then(res => res.data)
                        .then(data => {
                            if(data.length === 1) resolve(data[0]);
                            else reject({message: '정확히 일치하는 거래처를 알 수 없습니다.'});
                        })
                        .catch( (err) => reject(err) );
                    })
                };
                /** 결제조건 세팅 */
                const setTerms = (emp) => {
                    return new Promise((resolve, reject) => {
                        /** 
                         * TODO: 결제예정일 등 메서드 호출
                         */
                    })
                };
                /** 헤더부가세정보 세팅 */
                const setHeaderTax = (emp) => {
                    return new Promise((resolve, reject) => {
                        /** 
                         * TODO: 헤더부가세코드 세팅 이후 라인 정보 세팅
                         */
                    })
                };
                //TODO : 벨리데이션 (직원 / 거래유형 / 증빙유형) > 헤더/라인 정보 세팅
                //TODO : 순서의 맞게 로직 체결
                
                setEmp(data.empNo).then(emp => {
                    const keys = Object.keys(emp);
                    for (const key of keys) {
                        if(emp[key]) {

                            if(key.indexOf('deptCd') > -1) {
                                self.value[key] = emp[key];
                                self.value['actualDeptCode'] = emp[key];
                            } else if(key.indexOf('cctrCd') > -1) {
                                self.value['budgetDeptCode'] = emp[key];
                            } else if(key.indexOf('compCd') > -1) {
                                self.value['orgId'] = emp[key];
                            } else if(key.indexOf('productCd') > -1) {
                                self.value['itemGroupCode'] = emp[key];
                            } else if(key.indexOf('pjtCd') > -1) {
                                self.value['projectCode'] = emp[key];
                            } else if(key.indexOf('trAcctCd') > -1) {
                                self.value['trBankAcctCode'] = emp[key];
                            } else if(key.indexOf('slipTypeCd') > -1) {
                                self.value['slipTypeCode'] = emp[key];
                            } else if(key.indexOf('segment1Cd') > -1) {
                                self.value['compCode'] = emp[key];
                            } else if(key.indexOf('segment9Cd') > -1) {
                                self.value['segment9Code'] = emp[key];
                            } else if(key.indexOf('segment10Cd') > -1) {
                                self.value['segment10Code'] = emp[key];
                            } else if(key.indexOf('taskNo') > -1) {
                                self.value['taskCode'] = emp[key];
                            } else if(key.indexOf('taskNm') > -1) {
                                self.value['taskName'] = emp[key];
                            }else if(key.indexOf('taxLocationCode') > -1) {
                                self.value['taxLocationCode'] = emp[key];
                            } else if(key.indexOf('taxLocationName') > -1) {
                                self.value['taxLocationName'] = emp[key];
                            } else if(key.indexOf('attribute2') > -1) {
                                self.value['deptType'] = emp[key];
                            } else {
                                self.value[key] = emp[key];
                            }
                        }
                    }
                })
                .finally(_ => {
                    setTrx(data).then((trx) => {
                        
                        const keys = Object.keys(trx);
                        for (const key of keys) {
                            if(trx[key]) {

                                if(key.indexOf('acctCd') > -1) {
                                    self.value['acctCode'] = trx[key];
                                } else if(key.indexOf('acctNm') > -1) {
                                    self.value['acctName'] = trx[key];
                                } else if(key.indexOf('trxTypeCd') > -1) {
                                    self.value['trxTypeCode'] = trx[key];
                                } else if(key.indexOf('trxTypeNm') > -1) {
                                    self.value['trxTypeName'] = trx[key];
                                } else if(key.indexOf('interfaceModule') > -1) {
                                    self.value['ttypeInterfaceModule'] = trx[key];
                                } else if(key.indexOf('interfaceSlipType') > -1) {
                                    self.value['ttypeInterfaceSlipType'] = trx[key];
                                } else if(key.indexOf('inputModule') > -1) {
                                    self.value['ttypeInputModule'] = trx[key];
                                } else if(key.indexOf('prepaymentFlag') > -1) {
                                    self.value['ttypePrepaymentFlag'] = trx[key];
                                } else if(key.indexOf('addInfoType') > -1) {
                                    self.value['ttypeAddInfoType'] = trx[key];
                                } else if(key.indexOf('trxTypeDescription') > -1) {
                                    self.value['description'] = trx[key];
                                } else if(key.indexOf('slipTypeCd') > -1) {
                                    self.value['slipTypeCd'] = trx[key];
                                } else if(key.indexOf('slipDisplayFlag') > -1) {
                                    self.value['slipDisplayFlag'] = trx[key];
                                } else if(key.indexOf('slipCreationTargetFlag') > -1) {
                                    self.value['slipCreationTargetFlag'] = trx[key];
                                } else if(key.indexOf('drCr') > -1) {
                                    self.value['drCr'] = trx[key];
                                } else if(['AWT', 'ETCAWT'].includes(trx.slipTypeCd)) {
                                    if(key.indexOf('awtGroupId') > -1) {
                                        self.value['awtGroupId'] = trx[key];
                                    } else if(key.indexOf('awtGroupNm') > -1) {
                                        self.value['awtGroupNm'] = trx[key];
                                    } else if(key.indexOf('awtLocationCode') > -1) {
                                        self.value['awtLocationCode'] = trx[key];
                                    } else if(key.indexOf('awtLocationName') > -1) {
                                        self.value['awtLocationName'] = trx[key];
                                    } else if(key.indexOf('taxLocationCode') > -1) {
                                        self.value['taxLocationCode'] = trx[key];
                                    } else if(key.indexOf('taxLocationName') > -1) {
                                        self.value['taxLocationName'] = trx[key];
                                    }
                                }
                            }
                        }
                    })
                    .finally(_ => {
                        setEvidency(data).then((evid) => {
                            const keys = Object.keys(evid);
                            for (const key of keys) {
                                if(evid[key]) {
                                    if(key === 'evidenceCode') {
                                        self.value['taxEvidenceType'] = evid[key];
                                    } else if(key === 'evidenceName') {
                                        self.value['taxEvidenceTypeName'] = evid[key];
                                    } else if(key === 'lineAttribute1') {
                                        self.value['lineAttribute1'] = evid[key];
                                    } else if(key === 'lineAttribute1Name') {
                                        self.value['lineAttribute1Name'] = evid[key];
                                    } else if(key === 'lineAttribute3') {
                                        self.value['lineAttribute3'] = evid[key];
                                    } else if(key === 'lineAttribute6') {
                                        self.value['lineAttribute6'] = evid[key];
                                    }
                                    
                                }
                            }
                            
                        }).finally(_ => {
                            self.$store.commit('finish');
                            self.taxReadOnly(self.value.lineAttribute1, self.value.lineAttribute3);
                            
                            setTax(data).then((tax) => {
                                const keys = Object.keys(tax);
                                for (const key of keys) {
                                    if(tax[key]) {
                                        if(key.indexOf('issueDate') > -1) {
                                            self.value['evidenceDate'] = self.$moment(tax[key]).format("YYYYMMDD");
                                        } else if(key.indexOf('issueId') > -1) {
                                            self.value['taxSmartbillNo'] = tax[key];
                                        } else if(key.indexOf('suId') > -1) {
                                            self.value['taxbillSuId'] = tax[key];
                                        } else if(key.indexOf('dtiType') > -1) {
                                            self.value['dtiType'] = tax[key];
                                        } else if(key.indexOf('chargetotal') > -1) {
                                            self.value['taxbillSupplyAmt'] = tax[key];
                                        } else if(key.indexOf('taxtotal') > -1) {
                                            self.value['taxbillTaxAmt'] = tax[key] || 0;
                                        } else if(key.indexOf('grandtotal') > -1) {
                                            self.value['taxbillTotalAmt'] = tax[key];
                                        }
                                    }
                                }
                                
                                // 상세내역 자동 기입( 공급가액, 세액 합계 등 )
                                self.$bus.emit('setDefaultTaxLine', {
                                    chargetotal: data?.chargetotal || 0,
                                    taxtotal: data?.taxtotal || 0,
                                    dtiType: data?.dtiType
                                });
                            })
                            .finally(_ => {
                                const params = {
                                    taxbillSuId: self.value.taxbillSuId,
                                    curCd: self.value.curCd
                                }
                                setVendor(params).then(vendor => {
                                    const keys = Object.keys(vendor);
                                    for (const key of keys) {
                                        if(vendor[key]) {
                                            if(key === 'integrationVendorNum') {
                                                self.value['integrationVendorNum'] = vendor[key];
                                                self.value['evidenceVendorNum'] = vendor[key];
                                            } else if(key === 'integrationVendorName') {
                                                self.value['integrationVendorName'] = vendor[key];
                                                self.value['evidenceVendorName'] = vendor[key];
                                            } else if(key === 'vatRegistrationNum') {
                                                self.value['vatRegistrationNum'] = vendor[key];
                                                self.value['evidenceVendorVat'] = vendor[key];
                                            } else if(key === 'venTermsId') {
                                                self.value['termId'] = vendor[key];
                                            } else if(key === 'venTermsName') {
                                                self.value['termName'] = vendor[key];
                                            } else if(key === 'venPaymentDescription') {
                                                self.value['termDescription'] = vendor[key];
                                            } else if(key === 'custPaymentTermId') {
                                                self.value['custPaymentTermId'] = vendor[key];
                                            } else if(key === 'prepayCnt') {
                                                self.value['prepayCnt'] = vendor[key];
                                            } else if(key === 'vendorSiteId') {
                                                if(self.value.ttypeInterfaceModule === 'AP') {
                                                    self.value['evidenceVendorCustSiteId'] = vendor[key];
                                                }
                                                self.value['vendorSiteId'] = vendor[key];
                                            } else if(key === 'customerSiteId') {
                                                if(self.value.ttypeInterfaceModule === 'AR') {
                                                    self.value['evidenceVendorCustSiteId'] = vendor[key];
                                                }
                                                self.value['customerSiteId'] = vendor[key];
                                            } else if(key === 'vendorId') {
                                                self.value['vendorId'] = vendor[key];
                                            } else if(key === 'vendorNum') {
                                                self.value['vendorNum'] = vendor[key];
                                            } else if(key === 'venPaymentCurrencyCd') {
                                                self.value['venPaymentCurrencyCd'] = vendor[key];
                                            } else if(key === 'venPayGroupLookupCd') {
                                                self.value['payGroupLookupCode'] = vendor[key];
                                            } else if(key === 'venPartyId') {
                                                self.value['vendorPartyId'] = vendor[key];
                                            } else if(key === 'venPartySiteId') {
                                                self.value['vendorPartySiteId'] = vendor[key];
                                            } else if(key === 'customerId') {
                                                self.value['customerId'] = vendor[key];
                                            } else if(key === 'customerPartyId') {
                                                self.value['customerPartyId'] = vendor[key];
                                            } else if(key === 'custPartySiteId') {
                                                self.value['customerPartySiteId'] = vendor[key];
                                            }
                                        }
                                    }
                                    if((parseInt(self.value['prepayCnt'] || 0) * 1) > 0 && self.value.ttypeInterfaceModule === 'AP') {
                                        self.$alert(`${self.value['evidenceVendorName']} 거래처에 선급금 잔액이 존재합니다. <br/>확인 후 반재 처리 하시기 바랍니다.`, '확인', {
                                            dangerouslyUseHTMLString: true,
                                            confirmButtonText: '확인',
                                            type: 'info',
                                            center: true,
                                            callback: () => {
                                                // 확인 여부 Y로 체크해줌.
                                                self.value.prepaymentYn = 'Y';
                                            }
                                        });
                                    }
                                })
                                .finally(_ => {
                                    
                                    const trxTypeCd = self.value.ttypeInterfaceModule;
                                    const search = {
                                        vendorCd: self.value[`evidenceVendorNum`] || '',
                                        deptCd: self.value.actualDeptCode || '',
                                        currencyCd: self.value.curCd,
                                        searchNm: self.value['termDescription'],
                                        trxTypeCd,
                                        termId: trxTypeCd === 'AP' ? self.value['termId'] : self.value['custPaymentTermId'],
                                    }
                                    self.setDefaultTermsBank(search);
                                });
                            });
                        });
                    })
                });
            };

            setTaxSlipData(taxInfo);
            
        },

    },//methods end here
}