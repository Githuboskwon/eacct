<template>
    <div class="modal-card" id="draggable" style="width:1600px;">
        <header class="modal-card-head pop-header">
            <button class="icon is-right" @click="move" style="position:absolute;top:18px;right:70px;z-index:30;"><i class="fas fa-window-maximize" style="color:#fff;font-size:22px;padding-top:1px"></i></button>
            <p class="modal-card-title tit">
                {{ title }}
                <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
            </p>
        </header>
        <section class="modal-card-body pop-content">
            <div class="inner-box">
                <div v-if="isNew" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
                    <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px;">
                        <button class="btn-size btn-blue" @click="approval()">
                            <span class="btn-icon"><i class="fas fa-pen-alt"></i></span> 상신
                        </button>
                        <button class="btn-size btn-blue" @click="openVendorPopup()">
                            <span class="btn-icon"><i class="fas fa-user-check"></i></span> 결재자 지정
                        </button>
                    </div>
                </div>
                <div v-if="!isNew" class="content-name" style="margin-top: 0px; margin-bottom: 0px;">
                    <div class="btn-wrap btn-type1 clearfix" style="float:right; margin-bottom: 0px;">
                        <button v-if="this.$store.state.loginInfo.loginId === this.apprInfo.thisApprUser || this.apprInfo.deleYn ==='Y'" class="btn-size btn-blue" @click="openApprPopup()"><span class="btn-icon"><i
                                class="fas fa-pen-alt"></i></span> 결재
                        </button>
<!--                        <button v-if="this.$store.state.loginInfo.loginId == this.apprInfo.draftUserId && this.apprInfo.docStatus === 'REQ'"-->
<!--                                class="btn-size btn-yellow" @click="cancel(docMngNo)"><span class="btn-icon"><i class="far fa-file"></i></span> 상신취소-->
<!--                        </button>-->
                    </div>
                </div>
                <div class="table-area">
                    <div class="table-b">
                        <div class="table-name">
                            <h3 class="ico_table_name">결재선</h3>
                        </div>
                        <table class="table">
                            <colgroup>
                                <col width="3%">
                                <col width="7%">
                                <col width="20%">
                                <col width="20%">
                                <col width="10%">
                                <col width="10%">
                                <col width="30%">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th class="no-radius">NO</th>
                                    <th>결재유형</th>
                                    <th>결재자</th>
                                    <th>실제결재자</th>
                                    <th>결재상태</th>
                                    <th>결재일시</th>
                                    <th style="border-right: 1px solid rgb(173, 173, 173);">의견</th>
                                </tr>
                            </thead>
                            <tbody id="tbody">
                                <tr v-for="(item, index) in apprLine" :key="index">
                                    <td style="text-align: center;">{{index+1}}</td>
                                    <td style="text-align: center;">{{item.apprType}}</td>
                                    <td>{{item.aprverUser}}</td>
                                    <td>{{item.aaprverUser}}</td>
                                    <td style="text-align: center;">{{item.apprStatus}}</td>
                                    <td style="text-align: center;">{{item.apprDtm === null ? null : $moment(item.apprDtm).format('YYYY-MM-DD HH:mm:ss')}}</td>
                                    <td style="border-right: 1px solid #adadad">{{item.apprDesc}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="table-area">
                  <div class="table-header">
                    <div class="table-name">
                      <h3 class="ico_table_name">전표 내역</h3>
                    </div>
                    <div class="btn-wrap btn-type1 clearfix" v-if="slipType=='SLIP'">
                      <div style="float:right;"> 총 금액 : {{$numeral(totalAmt).format('0,0')}} </div>
                    </div>
                  </div>
                  <div class="grid-wrap">
                      <ag-grid-vue ref="grid" style="width: 100%; height: 500px;" class="ag-theme-alpine"
                              :columnDefs="columnDefs"
                              :rowData="data"
                              :gridOptions="gridOptions"
                              :defaultColDef="defaultColDef"
                              :frameworkComponents="frameworkComponent"

                              @grid-ready="onReady"
                              />
                  </div>
                </div>
            </div>
            <loading :active.sync="this.isLoading" :can-cancel="false" :is-full-page="true" color="#6799FF" :opacity="0.2" />
        </section>
    </div>
</template>


<script>
    import DhxGrid from '@/components/DhxGrid.vue'

    import ApprLineSet from '@/components/ApprLineSet.vue'
    import ApprActPop from '@/components/ApprActPop.vue'
    import ApprErpDtl from '@/components/ApprErpDtl.vue'
    import ApprBundlePopDrafter from "@/components/ApprBundlePopDrafter.vue";

    import Loading from 'vue-loading-overlay';
    import 'vue-loading-overlay/dist/vue-loading.css';

    import AgGridCheckbox from "@/components/agGrid/AgGridCheckbox.vue"
    import AgGridScanAttach from "@/components/agGrid/AgGridScanAttach.vue"

    import mixinSlip from '@/mixin/slip'
    import _ from "lodash";
    
    const options = {
        'USE_SELECT_CD' : [{"detailCd" : "0", "detailNm" : "후첨"}, {"detailCd" : "1", "detailNm" : "첨부"}]
    }

    export default {
        props: {
            docMngNo: {
                type: String,
                required: false
            },
            docType: {
                type: String,
                required: false,
                default: 'submit'
            },
            value: {
                type: Array,
                required: false,
                default() {
                  return {}
                }
            },
            config:{
                type: Object,
                required: false,
            },
            apprTitle: {
                type: String,
                required: false,
                default: '전표'
            },
            slipType: {
                type: String,
                required: false,
            },
        },
        mixins: [mixinSlip],
        components: {
            DhxGrid,
            ApprLineSet,
            ApprErpDtl,
            Loading,
          ApprBundlePopDrafter
        },
        data() {
            return {
                columnDefs : [],
                gridOptions : {},
                frameworkComponent : {
                     'checkBox' : AgGridCheckbox,                     
                     'scanAttach' : AgGridScanAttach
                },
                defaultColDef: {resizable: true, filter:true, sortable: true},                
                title: '결재상신',
                data: [],
                dataProp: {},
                apprHeader: {},
                apprLine: [],
                apprDesc: '',
                apprNo: '',
                apprSeq: '',
                docTitleNm: '',
                docStatus: '',
                draftUserId: '',
                thisApprUser: '',
                deleYn: 'N',
                apprInfo: [],
                loaded: false,
                isNew: true,
                isMxMn: true,
                isLoading: false,
                totalAmt: 0
            }
        },
        methods: {
            //그리드 Ready
            onReady(params) {      
                
                //그리드용 api 정의        
                this.gridApi = params.api;
                this.columnApi = params.columnApi;

                if(this.slipTypeCd === 'E11') {
                    this.gridApi.sizeColumnsToFit();
                }
            },            
            makeColDef(){
                const that = this;

                if(this.slipType === 'SLIP'){

                  this.columnDefs = [
                    {
                      headerName: 'No.',
                      width: 80,
                      valueFormatter: (params) => {
                        return params.node.rowIndex + 1;
                      },
                      cellStyle:{textAlign: 'center'}
                    },
                    {
                      headerName: '회계일자',
                      field: 'postingDt',
                      width: 120,
                      cellStyle:{textAlign: 'center'},
                      valueFormatter: (params) => {
                        return that.getDateFormat(params.value)
                      },
                      editable: false,
                    },
                    {
                      headerName: '전표번호',
                      field: 'slipNo',
                      width: 185,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                    },
                    {
                      headerName: '결제예정일',
                      field: 'termDueDate',
                      width: 120,
                      cellStyle:{textAlign: 'center'},
                      valueFormatter: (params) => {
                        return that.getDateFormat(params.value)
                      },
                      editable: false,
                    },
                    {
                      headerName: '거래유형',
                      field: 'slipType',
                      width: 120,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                    },
                    {
                      headerName: '작성부서명',
                      field: 'deptNm',
                      width: 150,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                    },
                    {
                      headerName: '작성부서코드',
                      field: 'deptNo',
                      width: 150,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                      hide: true
                    },
                    {
                      headerName: '작성자',
                      field: 'empNm',
                      width: 120,
                      cellStyle:{textAlign: 'center'},
                      editable: false,
                    },
                    {
                      headerName: '작성자사번',
                      field: 'empNo',
                      width: 120,
                      cellStyle:{textAlign: 'center'},
                      editable: false,
                      hide: true
                    },
                    {
                      headerName: '적요',
                      field: 'headerRemark',
                      width: 300,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                    },
                    {
                      headerName: 'PO번호',
                      field: 'poNumber',
                      width: 150,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                    },
                    {
                      headerName: '공급가',
                      field: 'supplyAmount',
                      width: 150,
                      cellStyle:{textAlign: 'right'},
                      valueFormatter: (params) => {
                        return that.getNumberFormat(params.value);
                      },
                      editable: false,
                    },
                    {
                      headerName: '부가세액',
                      field: 'taxAmount',
                      width: 150,
                      cellStyle:{textAlign: 'right'},
                      valueFormatter: (params) => {
                        return that.getNumberFormat(params.value);
                      },
                      editable: false,
                    },
                    {
                      headerName: '총금액(KRW)',
                      field: 'usedAmt',
                      width: 150,
                      cellStyle:{textAlign: 'right'},
                      valueFormatter: (params) => {
                        return that.getNumberFormat(params.value);
                      },
                      editable: false,
                    },
                    {
                      headerName: '거래처코드',
                      field: 'integrationVendorNum',
                      width: 150,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                    },
                    {
                      headerName: '거래처명',
                      field: 'integrationVendorName',
                      width: 150,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                    },
                    {
                      headerName: '결재조건',
                      field: 'termName',
                      width: 150,
                      cellStyle:{textAlign: 'left'},
                      editable: false,
                    },
                    {
                      headerName: '통화',
                      field: 'usedCur',
                      width: 150,
                      cellStyle:{textAlign: 'center'},
                      editable: false,
                    },
                    {
                      headerName: '총금액',
                      field: 'usedAmt',
                      width: 150,
                      cellStyle:{textAlign: 'right'},
                      valueFormatter: (params) => {
                        return that.getNumberFormat(params.value);
                      },
                      editable: false,
                    },
                    {
                      headerName: '작성일자',
                      field: 'regDtm',
                      width: 120,
                      cellStyle:{textAlign: 'center'},
                      valueFormatter: (params) => {
                        return that.getDateFormat(params.value)
                      },
                      editable: false,
                    },
                  ]

                }
                
                if(this.slipType === '21'){
                    this.columnDefs = [
                            {
                                headerName: '건별지급', 
                                children:[
                                    {
                                        headerName: 'No.', 
                                        width: 120,
                                        valueFormatter: (params) => {               
                                            return params.node.rowIndex + 1;
                                        },
                                        cellStyle:{textAlign: 'center'}
                                    },
                                    {
                                      headerName: '지급문서번호',
                                      field: 'checkNo',
                                      width: 150,
                                      cellStyle:{textAlign: 'center'}
                                    },
                                    {
                                        headerName: '지급일자', 
                                        field: 'checkDt', 
                                        width: 150,
                                        valueFormatter: (params) => {
                                            return that.getDateFormat(params.value)
                                        },
                                        editable: false,
                                    },
                                    {
                                        headerName: '출금계좌번호', 
                                        field: 'bankAcctNum', 
                                        width: 200,
                                        editable: false,
                                    },                                                             
                                ]
                            },
                            {
                                headerName: '지급정보', 
                                children:[
                                    {
                                        headerName: '통화', 
                                        field: 'currencyCd', 
                                        width: 100,
                                        cellStyle:{textAlign: 'center'}
                                    },
                                    {
                                        headerName: '외화지급금액', 
                                        field: 'paymentAmt', 
                                        width: 200,
                                        cellStyle:{textAlign: 'right'},
                                        valueFormatter: (params) => { 
                                            return that.getNumberFormat(params.value);
                                        }
                                    },
                                    {
                                        headerName: '원화지급금액', 
                                        field: 'baseAmt', 
                                        width: 200,
                                        cellStyle:{textAlign: 'right'},
                                        valueFormatter: (params) => { 
                                            return that.getNumberFormat(params.value);
                                        }
                                    },
                                    {
                                        headerName: '공급자명', 
                                        field: 'vendorNm', 
                                        width: 200,
                                        cellStyle:{textAlign: 'left'}
                                    },
                                ]
                            },
                            {
                                headerName: '증빙첨부', 
                                children:[
                                    {
                                        headerName: '첨부', 
                                        field: 'ufileCnt', 
                                        width: 100,
                                        cellStyle:{textAlign: 'center'},
                                        cellRenderer: 'scanAttach',
                                        // cellRendererParams:{
                                        //     funcNm : 'openEvidencePopup'
                                        // }
                                    },
                                    {
                                        headerName: '기안', 
                                        field: 'jiniCnt', 
                                        width: 100,
                                        cellStyle:{textAlign: 'center'},
                                        cellRenderer: 'scanAttach',
                                        // cellRendererParams:{
                                        //     funcNm : 'openEvidencePopup'
                                        // }
                                    }
                                ]
                            },
                            {
                                headerName: '전자전표번호', 
                                field: 'eaSlipNo', 
                                width: 140,
                                cellStyle:{textAlign: 'left'},
                                hide:true
                            },
                        ]
                }else if(this.slipType === '22'){
                  this.columnDefs = [
                    {
                      headerName: 'No.',
                      width: 120,
                      valueFormatter: (params) => {
                        return params.node.rowIndex + 1;
                      },
                      cellStyle:{textAlign: 'center'}
                    },
                    {
                      headerName: '은행명',
                      field: 'bankNm',
                      width: 150,
                      editable: false,
                    },
                    {
                      headerName: '계좌번호',
                      field: 'bankAcctNum',
                      width: 150,
                      editable: false,
                    },
                    {
                      headerName: '지급일자',
                      field: 'checkDt',
                      valueFormatter: (params) => {
                        return that.getDateFormat(params.value)
                      },
                      width: 120,
                      editable: false,
                    },
                    {
                      headerName: '통화',
                      field: 'usedCur',
                      width: 80,
                      editable: false,
                    },
                    {
                      headerName: '외화지급금액',
                      field: 'usedForAmt',
                      width: 150,
                      valueFormatter: (params) => {
                        return that.getDoubleNumberFormat(params.value);
                      },
                      cellStyle:{textAlign: 'right'},
                      editable: false,
                    },
                    {
                      headerName: '원화지급금액',
                      field: 'usedAmt',
                      // cellRenderer: 'numberInputRenderer',
                      width: 150,
                      valueFormatter: (params) => {
                        return that.getNumberFormat(params.value);
                      },
                      editable: false,
                    },
                    {
                      headerName: '지급방법',
                      field: 'paymentMethodLookupCode',
                      width: 120,
                      editable: false,
                    },
                    {
                      headerName: '증빙첨부',
                      field: 'ufileCnt',
                      width: 100,
                      cellStyle:{textAlign: 'center'},
                      cellRenderer: 'scanAttach',
                      // cellRendererParams:{
                      //     funcNm : 'openEvidencePopup'
                      // }
                    },
                    {
                      headerName: '기안서연동',
                      field: 'jiniCnt',
                      width: 100,
                      cellStyle:{textAlign: 'center'},
                      cellRenderer: 'scanAttach',
                      // cellRendererParams:{
                      //     funcNm : 'openDrfPlanPopup'
                      // }
                    },
                    {
                      headerName: '적요',
                      field: 'remark',
                      hide: true
                    },
                    {
                      headerName: '전표번호',
                      field: 'slipNo',
                      hide: true
                    },
                    {
                      headerName: '전표타입',
                      field: 'slipType',
                      hide: true
                    },
                  ]
                }else if(this.slipType === '23'){

                  this.columnDefs = [
                    {
                      headerName: 'No.',
                      width: 120,
                      valueFormatter: (params) => {
                        return params.node.rowIndex + 1;
                      },
                      cellStyle:{textAlign: 'center'}
                    },
                    {
                      headerName: '은행명',
                      field: 'bankNm',
                      width: 250,
                      editable: false,
                    },
                    {
                      headerName: '출금계좌번호',
                      field: 'bankAcctNum',
                      width: 250,
                      editable: false,
                    },
                    {
                      headerName: '만기일',
                      field: 'futurePayDueDt',
                      valueFormatter: (params) => {
                        return that.getDateFormat(params.value)
                      },
                      width: 250,
                      cellStyle:{textAlign: 'center'},
                      editable: false,
                    },
                    {
                      headerName: '만기금액',
                      field: 'sumAmt',
                      width: 250,
                      valueFormatter: (params) => {
                        return that.getNumberFormat(params.value);
                      },
                      cellStyle:{textAlign: 'right'},
                      editable: false,
                    },
                    {
                      headerName: '증빙첨부',
                      field: 'ufileCnt',
                      width: 150,
                      cellStyle:{textAlign: 'center'},
                      cellRenderer: 'scanAttach',
                      // cellRendererParams:{
                      //     funcNm : 'openEvidencePopup'
                      // }
                    },
                    {
                      headerName: '기안서연동',
                      field: 'jiniCnt',
                      width: 150,
                      cellStyle:{textAlign: 'center'},
                      cellRenderer: 'scanAttach',
                      // cellRendererParams:{
                      //     funcNm : 'openDrfPlanPopup'
                      // }
                    },
                    {
                      headerName: '적요',
                      field: 'remark',
                      hide: true
                    },
                    {
                      headerName: '전표번호',
                      field: 'slipNo',
                      hide: true
                    },
                    {
                      headerName: '전표번호',
                      field: 'slipNo',
                      hide: true
                    },
                    {
                      headerName: '전표타입',
                      field: 'slipType',
                      hide: true
                    },
                  ]

                }else if(this.slipType === '24') {

                  this.columnDefs = [
                    {
                      headerName: 'No.',
                      width: 120,
                      valueFormatter: (params) => {
                        return params.node.rowIndex + 1;
                      },
                      cellStyle:{textAlign: 'center'}
                    },
                    {
                      headerName: '전표일자',
                      field: 'journalDt',
                      valueFormatter: (params) => {
                        return that.getDateFormat(params.value)
                      },
                      width: 150,
                      cellStyle:{textAlign: 'center'},
                      editable: false,
                    },
                    {
                      headerName: '거래유형',
                      field: 'dealType',
                      width: 100,
                      editable: false,
                    },
                    {
                      headerName: '상품유형',
                      field: 'productType',
                      width: 100,
                      editable: false,
                    },
                    {
                      headerName: '거래번호(DEAL)',
                      field: 'dealNum',
                      width: 100,
                      editable: false,
                    },
                    {
                      headerName: '통화',
                      field: 'currencyCodeHeader',
                      width: 100,
                      editable: false,
                    },
                    {
                      headerName: '매입금액(FX)',
                      field: 'buyAmt',
                      width: 100,
                      valueFormatter: (params) => {
                        return that.getDoubleNumberFormat(params.value);
                      },
                      cellStyle:{textAlign: 'right'}
                    },
                    {
                      headerName: '매도금액(FX)',
                      field: 'sellAmt',
                      width: 100,
                      valueFormatter: (params) => {
                        return that.getDoubleNumberFormat(params.value);
                      },
                      cellStyle:{textAlign: 'right'}
                    },
                    {
                      headerName: '거래손익(FX)',
                      field: 'profitAmt',
                      width: 100,
                      valueFormatter: (params) => {
                        return that.getNumberFormat(params.value);
                      },
                      cellStyle:{textAlign: 'right'}
                    },
                    {
                      headerName: '처리금액',
                      field: 'slipAmt',
                      width: 100,
                      valueFormatter: (params) => {
                        return that.getNumberFormat(params.value);
                      },
                      cellStyle:{textAlign: 'right'}
                    },
                    {
                      headerName: '적요',
                      field: 'remark',
                      width: 150,
                      editable: false,
                    },
                    {
                      headerName: '작성자',
                      field: 'chgNm',
                      width: 150,
                      editable: false,
                    },
                    {
                      headerName: '증빙첨부',
                      field: 'ufileCnt',
                      width: 100,
                      cellStyle:{textAlign: 'center'},
                      cellRenderer: 'scanAttach',
                      // cellRendererParams:{
                      //     funcNm : 'openEvidencePopup'
                      // }
                    },
                    {
                      headerName: '기안서연동',
                      field: 'jiniCnt',
                      width: 100,
                      cellStyle:{textAlign: 'center'},
                      cellRenderer: 'scanAttach',
                      // cellRendererParams:{
                      //     funcNm : 'openDrfPlanPopup'
                      // }
                    },
                    {
                      headerName: '전표번호',
                      field: 'slipNo',
                      hide: true
                    },
                    {
                      headerName: '전표헤더번호',
                      field: 'slipHeaderId',
                      hide: true
                    },
                    {
                      headerName: '전표타입',
                      field: 'slipType',
                      hide: true
                    },
                  ];
                }else if(this.slipType === '25') {
                  this.columnDefs = [
                    {
                      headerName: ''
                      , headerCheckboxSelection: true
                      , checkboxSelection: true
                      , field:'regYn'
                      , width : 80
                      , cellStyle:{textAlign: 'center'}
                      , editable: false
                    },
                    {
                      headerName: '전표일자',
                      field: 'glDt',
                      valueFormatter: (params) => {
                        return that.getDateFormat(params.value)
                      },
                      width: 150,
                      cellStyle:{textAlign: 'center'},
                      editable: false,
                    },
                    {
                      headerName: '적요',
                      field: 'remark',
                      width: 400,
                      editable: false,
                    },
                    {
                      headerName: '통화',
                      field: 'currencyCd',
                      width: 200,
                      editable: false,
                    },
                    {
                      headerName: '차변금액(원화)',
                      field: 'functionalAmtDr',
                      width: 200,
                      valueFormatter: (params) => {
                        return that.getDoubleNumberFormat(params.value)
                      },
                      editable: false,
                    },
                    {
                      headerName: '대변금액(원화)',
                      field: 'functionalAmtCr',
                      width: 200,
                      valueFormatter: (params) => {
                        return that.getDoubleNumberFormat(params.value)
                      },
                      editable: false,
                    },
                    {
                      headerName: '증빙첨부',
                      field: 'ufileCnt',
                      width: 100,
                      cellStyle:{textAlign: 'center'},
                      cellRenderer: 'scanAttach',
                      // cellRendererParams:{
                      //     funcNm : 'openEvidencePopup'
                      // }
                    },
                    {
                      headerName: '기안서연동',
                      field: 'jiniCnt',
                      width: 100,
                      cellStyle:{textAlign: 'center'},
                      cellRenderer: 'scanAttach',
                      // cellRendererParams:{
                      //     funcNm : 'openDrfPlanPopup'
                      // }
                    },
                    {
                      headerName: '전표번호',
                      field: 'slipNo',
                      hide: true
                    },
                    {
                      headerName: '전표헤더번호',
                      field: 'slipHeaderId',
                      hide: true
                    },
                    {
                      headerName: '전표타입',
                      field: 'slipType',
                      hide: true
                    },
                  ]
                } else if (this.slipType === '27'){
                  this.columnDefs = [
                    {
                      headerName: 'No.',
                      width: 120,
                      valueFormatter: (params) => {
                        return params.node.rowIndex + 1;
                      },
                      cellStyle:{textAlign: 'center'}
                    },
                    {
                      headerName: '원천',
                      field: 'sourceNm',
                      width: 150,
                      editable: false,
                    },
                    {
                      headerName: '출처',
                      field: 'categoryNm',
                      width: 150,
                      editable: false,
                    },
                    {
                      headerName: '전표유형',
                      field: 'glSlipType',
                      width: 100,
                      editable: false,
                    },
                    {
                      headerName: '거래유형',
                      field: 'trxTypeNm',
                      width: 100,
                      editable: false,
                    },
                    {
                      headerName: 'GL일자',
                      field: 'glDt',
                      width: 130,
                      valueFormatter: (params) => {
                        return that.getDateFormat(params.value);
                      },
                      cellStyle:{textAlign: 'center'},
                      editable: false,
                    },
                    {
                      headerName: '분개명',
                      field: 'headerNm',
                      width: 150,
                      editable: false,
                    },
                    {
                      headerName: '통화코드',
                      field: 'currencyCd',
                      width: 100,
                      cellStyle:{textAlign: 'center'},
                      editable: false,
                    },
                    {
                      headerName: '입력금액(외화)',
                      field: '',
                      width: 200,
                      children:[
                        {
                          headerName: '차변',
                          field: 'forTotalDr',
                          width: 100,
                          valueFormatter: (params) => {
                            return that.getDoubleNumberFormat(params.value);
                          },
                          cellStyle:{textAlign: 'right'}
                        },
                        {
                          headerName: '대변',
                          field: 'forTotalCr',
                          width: 100,
                          valueFormatter: (params) => {
                            return that.getDoubleNumberFormat(params.value);
                          },
                          cellStyle:{textAlign: 'right'}
                        },
                      ],
                      editable: false,
                    },
                    {
                      headerName: '환산금액(원화)',
                      field: '',
                      width: 200,
                      children:[
                        {
                          headerName: '차변',
                          field: 'krwTotalDr',
                          width: 100,
                          valueFormatter: (params) => {
                            return that.getNumberFormat(params.value);
                          },
                          cellStyle:{textAlign: 'right'}
                        },
                        {
                          headerName: '대변',
                          field: 'krwTotalCr',
                          width: 100,
                          valueFormatter: (params) => {
                            return that.getNumberFormat(params.value);
                          },
                          cellStyle:{textAlign: 'right'}
                        },
                      ],
                      editable: false,
                    },
                    {
                      headerName: '배치명',
                      field: 'batchNm',
                      width: 120,
                      editable: false,
                    },
                    {
                      headerName: '적요',
                      field: 'headerRemark',
                      width: 200,
                      editable: false,
                    },
                  ]
                }else if(this.slipType === 'E13'){//공사비전표
                        this.columnDefs = [
                                {
                                    headerName: 'ERP 공사비전표', 
                                    children:[
                                        {
                                            headerName: 'No.', 
                                            width: 70,
                                            valueFormatter: (params) => {               
                                                return params.node.rowIndex+1;
                                            }
                                            ,cellStyle:{textAlign: 'center'}
                                        },
                                        {
                                            headerName: 'ERP번호(일자-Serial No)', 
                                            field: 'erpSlipNo', 
                                            width: 180,
                                            cellStyle:{textAlign: 'center'}
                                        },
                                        {
                                            headerName: '거래처코드', 
                                            field: 'payCustCd', 
                                            width: 120,
                                            cellStyle:{textAlign: 'left'}
                                        },
                                        {
                                            headerName: '거래처명', 
                                            field: 'payCustNm', 
                                            width: 200,
                                            cellStyle:{textAlign: 'left'}
                                        },
                                        {
                                            headerName: '적요', 
                                            field: 'hdSgtxt', 
                                            width: 200,
                                            cellStyle:{textAlign: 'left'}
                                        },                            
                                        {
                                            headerName: '통화', 
                                            field: 'curCd', 
                                            width: 90,
                                            cellStyle:{textAlign: 'center'}
                                        },
                                    ]
                                },
                                {
                                    headerName: '입력금액', 
                                    children:[
                                        {
                                            headerName: '공급가액', 
                                            field: 'supAmt', 
                                            width: 120,
                                            cellStyle:{textAlign: 'right'},
                                            valueFormatter: (params) => { 
                                                return that.getNumberFormat(params.value);
                                            }
                                        },
                                        {
                                            headerName: '세액', 
                                            field: 'vatAmt', 
                                            width: 120,
                                            cellStyle:{textAlign: 'right'},
                                            valueFormatter: (params) => { 
                                                return that.getNumberFormat(params.value);
                                            }
                                        },
                                        {
                                            headerName: '합계', 
                                            field: 'totAmt', 
                                            width: 120,
                                            cellStyle:{textAlign: 'right'},
                                            valueFormatter: (params) => { 
                                                return that.getNumberFormat(params.value);
                                            }
                                        },
                                    ]
                                },
                                // {
                                //     headerName: '환산금액', 
                                //     children:[
                                //         {
                                //             headerName: '공급가액', 
                                //             field: 'krwSupAmt', 
                                //             width: 120,
                                //             cellStyle:{textAlign: 'right'},
                                //             valueFormatter: (params) => { 
                                //                 return that.getNumberFormat(params.value);
                                //             }
                                //         },
                                //         {
                                //             headerName: '세액', 
                                //             field: 'krwVatAmt', 
                                //             width: 120,
                                //             cellStyle:{textAlign: 'right'},
                                //             valueFormatter: (params) => { 
                                //                 return that.getNumberFormat(params.value);
                                //             }
                                //         },
                                //         {
                                //             headerName: '합계', 
                                //             field: 'krwTotAmt', 
                                //             width: 120,
                                //             cellStyle:{textAlign: 'right'},
                                //             valueFormatter: (params) => { 
                                //                 return that.getNumberFormat(params.value);
                                //             }
                                //         },
                                //     ]
                                // },
                                {
                                    headerName: '세금계산서', 
                                    children:[
                                        {
                                            headerName: '첨부', 
                                            field: 'taxBillCnt', 
                                            width: 80,
                                            cellStyle:{textAlign: 'center'},
                                            cellRenderer: 'scanAttach',
                                            cellRendererParams:{
                                                funcNm : 'openTaxBillPopup'
                                            }
                                        },
                                        {
                                            headerName: '후첨부', 
                                            field: 'taxAttachCd', 
                                            width: 80,
                                            cellStyle:{textAlign: 'center'},
                                            editable: true,
                                            cellEditor: 'agSelectCellEditor',
                                            cellEditorParams: () => {
                                                return {values : options['USE_SELECT_CD'].map(v=>v.detailCd)}
                                            },
                                            valueFormatter: (params) => {               
                                                return options['USE_SELECT_CD'].filter(v=>params.value === v.detailCd).map(v=>v.detailNm)
                                            }
                                        }
                                    ]
                                },
                                {
                                    headerName: '증빙첨부', 
                                    children:[
                                        {
                                            headerName: '첨부', 
                                            field: 'ufileCnt', 
                                            width: 80,
                                            cellStyle:{textAlign: 'center'},
                                            cellRenderer: 'scanAttach',
                                            cellRendererParams:{
                                                funcNm : 'openEvidencePopup'
                                            }
                                        }
                                    ]
                                },
                                {
                                    headerName: '기안서', 
                                    children:[
                                        {
                                            headerName: '연동', 
                                            field: 'uwingsFileCnt', 
                                            width: 80,
                                            cellStyle:{textAlign: 'center'},
                                            cellRenderer: 'scanAttach',
                                            cellRendererParams:{
                                                funcNm : 'openDrfPlanPopup'
                                            }
                                        }
                                    ]
                                },
                                {
                                    headerName: '환율', 
                                    children:[
                                        {
                                            headerName: '환율', 
                                            field: 'excRt', 
                                            width: 80,
                                            cellStyle:{textAlign: 'right'}
                                        },
                                        {
                                            headerName: '환율일자', 
                                            field: 'excDate', 
                                            width: 110,
                                            cellStyle:{textAlign: 'left'}
                                        }
                                    ]
                                },
                                {
                                    headerName: 'grSlipNo', 
                                    field: 'grSlipNo', 
                                    width: 180,
                                    cellStyle:{textAlign: 'left'},
                                    hide:true
                                },
                                {
                                    headerName: 'issueId', 
                                    field: 'issueId', 
                                    width: 180,
                                    cellStyle:{textAlign: 'left'},
                                    hide:true
                                }
                            ]                
                
                }else if(this.slipType === 'E14'){//기타전표
                            this.columnDefs = [
                                {
                                    headerName: '기타전표', 
                                    children:[
                                        {
                                            headerName: 'No.', 
                                            width: 70,
                                            valueFormatter: (params) => {               
                                                return params.node.rowIndex + 1;
                                            },
                                            cellStyle:{textAlign: 'center'}
                                        },
                                        
                                        {
                                            headerName: 'ERP번호(일자-Serial No)', 
                                            field: 'erpSlipNo', 
                                            width: 180,
                                            cellStyle:{textAlign: 'center'}
                                        },
                                        {
                                            headerName: '회계일자', 
                                            field: 'postDt', 
                                            width: 110,
                                            cellStyle:{textAlign: 'center'}
                                        },
                                        {
                                          headerName: '지급예정일',
                                          field: 'payDueDt',
                                          width: 110,
                                          cellStyle:{textAlign: 'center'},
                                          valueFormatter: (params) =>{
                                            if(!_.isEmpty(params.data)){
                                              return that.$moment(params.data).format('YYYY-MM-DD')
                                            }else{
                                              return ''
                                            }
                                          }
                                        },
                                        {
                                            headerName: '거래처코드', 
                                            field: 'payCustCd', 
                                            width: 120,
                                            cellStyle:{textAlign: 'left'}
                                        },
                                        {
                                            headerName: '거래처명', 
                                            field: 'payCustNm', 
                                            width: 200,
                                            cellStyle:{textAlign: 'left'}
                                        },
                                        {
                                            headerName: '전표유형', 
                                            field: 'hdRef10', 
                                            width: 90,
                                            cellStyle:{textAlign: 'left'}
                                        },                            
                                        {
                                            headerName: '통화', 
                                            field: 'curCd', 
                                            width: 90,
                                            cellStyle:{textAlign: 'center'}
                                        },
                                        {
                                            headerName: '공급가액', 
                                            field: 'supAmt', 
                                            width: 120,
                                            cellStyle:{textAlign: 'right'},
                                            valueFormatter: (params) => { 
                                                return that.getNumberFormat(params.value);
                                            }
                                        },
                                        {
                                            headerName: '세액', 
                                            field: 'vatAmt', 
                                            width: 120,
                                            cellStyle:{textAlign: 'right'},
                                            valueFormatter: (params) => { 
                                                return that.getNumberFormat(params.value);
                                            }
                                        },
                                        {
                                            headerName: '합계', 
                                            field: 'totAmt', 
                                            width: 120,
                                            cellStyle:{textAlign: 'right'},
                                            valueFormatter: (params) => { 
                                                return that.getNumberFormat(params.value);
                                            }
                                        },
                                        {
                                            headerName: '사업자번호', 
                                            field: 'payCustBizNo', 
                                            width: 160,
                                            cellStyle:{textAlign: 'left'}
                                        },
                                    ]
                                },
                                {
                                    headerName: '세금계산서', 
                                    children:[
                                        {
                                            headerName: '첨부', 
                                            field: 'taxBillCnt', 
                                            width: 80,
                                            cellStyle:{textAlign: 'center'},
                                            cellRenderer: 'scanAttach',
                                            cellRendererParams:{
                                                funcNm : 'openTaxBillPopup'
                                            }
                                        },
                                        {
                                            headerName: '후첨부', 
                                            field: 'taxAttachCd', 
                                            width: 80,
                                            cellStyle:{textAlign: 'center'},
                                            editable: true,
                                            cellEditor: 'agSelectCellEditor',
                                            cellEditorParams: () => {
                                                return {values : options['USE_SELECT_CD'].map(v=>v.detailCd)}
                                            },
                                            valueFormatter: (params) => {               
                                                return options['USE_SELECT_CD'].filter(v=>params.value === v.detailCd).map(v=>v.detailNm)
                                            }
                                        }
                                    ]
                                },                                
                                {
                                    headerName: '증빙', 
                                    children:[
                                        {
                                            headerName: '첨부', 
                                            field: 'ufileCnt', 
                                            width: 80,
                                            cellStyle:{textAlign: 'center'},
                                            cellRenderer: 'scanAttach',
                                            cellRendererParams:{
                                                funcNm : 'openEvidencePopup'
                                            }
                                        }
                                    ]
                                },
                                {
                                    headerName: '기안서', 
                                    children:[
                                        {
                                            headerName: '연동', 
                                            field: 'uwingsFileCnt', 
                                            width: 80,
                                            cellStyle:{textAlign: 'center'},
                                            cellRenderer: 'scanAttach',
                                            cellRendererParams:{
                                                funcNm : 'openDrfPlanPopup'
                                            }
                                        }
                                    ]
                                },
                                {
                                    headerName: '환율', 
                                    children:[
                                        {
                                            headerName: '정보', 
                                            field: 'excRt', 
                                            width: 80,
                                            cellStyle:{textAlign: 'right'},
                                            valueFormatter: (params) => { 
                                                return that.getNumberFormat(params.value);
                                            }
                                        },
                                        {
                                            headerName: '일자', 
                                            field: 'excDt', 
                                            width: 110,
                                            cellStyle:{textAlign: 'left'}
                                        },
                                    ]
                                },
                                {
                                    headerName: '그룹전표번호', 
                                    field: 'grSlipNo', 
                                    width: 140,
                                    cellStyle:{textAlign: 'left'},
                                    hide:true
                                },
                                {
                                    headerName: '전자전표번호', 
                                    field: 'eaSlipNo', 
                                    width: 140,
                                    cellStyle:{textAlign: 'left'},
                                    hide:true
                                },
                            ]
                }

 
            },
            move(){
              if(this.isMxMn) {
                $(".modal-card").removeClass('pop_min')
                $(".modal-card").addClass('pop_max');

                $(".animation-content").removeClass('pop_min')
                $(".animation-content").addClass('pop_max');
                this.isMxMn = false
              } else {
                $(".modal-card").removeClass('pop_max')
                $(".modal-card").addClass('pop_min');

                $(".animation-content").removeClass('pop_max')
                $(".animation-content").addClass('pop_min');
                this.isMxMn = true
              }
            },
            addApprDesc(title, docType){
              return new Promise((resolve,reject) => {
                const vm = this
                this.$modal.open({
                  component:ApprBundlePopDrafter,
                  props:{
                    docTitleNm: title,
                    docType: docType,
                  },
                  events: {
                    close(data) {
                      // Close event handler
                      if(!_.isEmpty(data.apprDesc) || !_.isEmpty(data.slipReusePossibleYn)){
                        //기안자 결재의견 set
                        vm.apprRemark = data.apprDesc;
                        vm.apprDesc = data.apprDesc;
                        vm.slipReusePossibleYn = data.slipReusePossibleYn;

                        return resolve()
                      }else{
                        return reject()
                      }
                    }
                  }
                })
              })
            },
            approval() {
              const vm = this;
              if (this.apprLine.length < 1) {
                  this.$swal({type: 'warning', text: '결재선을 지정해주세요.'});
                  return false;
              }
              this.addApprDesc(this.apprTitle, 'submit').then(() => {
                this.$swal({
                    type: 'info',
                    text: `결재내역을 상신합니다. 계속 하시겠습니까?`,
                    showCancelButton: true,
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                }).then((result) => {
                    if (result.value) {
                       this.$store.commit('loading');
                       let results = [];
                       if(this.slipType === 'SLIP'){
                         _.forEach(this.data, (v, i) => {
                           results[i] = {
                             compCd : this.$store.state.loginInfo.compCd,
                             docTypeCd: 'SLIP',
                             slipNo: v.slipNo, //전표번호
                             apprGroupId: v.approvalGroupId, //전표 헤더 그룹 아이디
                             deptCd: v.deptNo, //귀속부서
                             totalAmt: this.$numeral(v.enteredAmount).value(), //전표금액(KRW)
                             docTitleNm : v.slipTypeNm + ' / ' + this.$filters.number(v.enteredAmount) + ' / ' + v.headerRemark,
                             glDate: v.postingDt, //회계일자
                             slipType: v.slipType, //거래유형 코드
                             slipHeaderId: v.slipHeaderId, ////헤더전표ID
                             draftId : v.empNo, //위임자 No
                           }
                         })
                       }

                       if(this.slipType=== '21'){
                            _.forEach(this.data, (v, i) => {
                                results[i] = {
                                    compCd : this.$store.state.loginInfo.compCd,
                                    apprGroupId : v.slipHeaderId,
                                    slipHeaderId: v.slipHeaderId,
                                    slipNo : v.slipNo,
                                    slipType: v.slipType,
                                    docTitleNm : v.headerRemark ? v.headerRemark + ' / ' + this.$numeral(v.baseAmt).format('0,0') : this.$numeral(v.baseAmt).format('0,0'),
                                    docContents : v.apprRemark,
                                    draftId : this.$store.state.loginInfo.loginId,
                                    loginId : this.$store.state.loginInfo.loginId,
                                }
                            })
                        }else if(this.slipType === '22'){
                        _.forEach(this.data, (v, i) => {
                          let totalAmt = v.usedCur === "KRW" ? this.getNumberFormat(v.usedAmt) : this.getDoubleNumberFormat(v.usedForAmt);
                          results[i] = {
                            compCd : this.$store.state.loginInfo.compCd,
                            apprGroupId : v.slipHeaderId,
                            slipHeaderId: v.slipHeaderId,
                            slipNo : v.slipNo,
                            slipType: v.slipType,
                            docTitleNm : this.isEmpty(v.remark) === "" ? totalAmt : this.isEmpty(v.remark) + ' / ' + totalAmt,
                            docContents : v.apprRemark,
                            draftId : this.$store.state.loginInfo.loginId,
                            loginId : this.$store.state.loginInfo.loginId,
                          }
                        })
                      }else if(this.slipType === '23'){
                         _.forEach(this.data, (v, i) => {
                           results[i] = {
                             compCd : this.$store.state.loginInfo.compCd,
                             apprGroupId : v.slipHeaderId,
                             slipHeaderId: v.slipHeaderId,
                             slipNo : v.slipNo,
                             slipType: v.slipType,
                             docTitleNm : this.isEmpty(v.remark) === "" ? v.sumAmt : this.isEmpty(v.remark) + ' / ' + v.sumAmt,
                             docContents : v.apprRemark,
                             draftId : this.$store.state.loginInfo.loginId,
                             loginId : this.$store.state.loginInfo.loginId,
                           }
                         })
                       }else if(this.slipType === '24'){
                         _.forEach(this.data, (v, i) => {
                           results[i] = {
                             compCd : this.$store.state.loginInfo.compCd,
                             apprGroupId : v.slipHeaderId,
                             slipHeaderId: v.slipHeaderId,
                             slipNo : v.slipNo,
                             slipType: v.slipType,
                             docTitleNm : this.isEmpty(v.remark) === "" ? this.$numeral(v.slipAmt).format('0,0') : this.isEmpty(v.remark) + ' / ' + this.$numeral(v.slipAmt).format('0,0'),
                             docContents : v.apprRemark,
                             draftId : this.$store.state.loginInfo.loginId,
                             loginId : this.$store.state.loginInfo.loginId,
                           }
                         })
                       }else if(this.slipType === '25'){
                         _.forEach(this.data, (v, i) => {
                           results[i] = {
                             compCd : this.$store.state.loginInfo.compCd,
                             apprGroupId : v.slipHeaderId,
                             slipHeaderId: v.slipHeaderId,
                             slipNo : v.slipNo,
                             slipType: v.slipType,
                             docTitleNm : v.remark + ' / ' + this.$numeral(v.functionalAmtCr).format('0,0'),
                             docContents : v.apprRemark,
                             draftId : this.$store.state.loginInfo.loginId,
                             loginId : this.$store.state.loginInfo.loginId,
                           }
                         })
                       }else if(this.slipType === '27'){
                            _.forEach(this.data, (v, i) => {
                                results[i] = {
                                    compCd : this.$store.state.loginInfo.compCd,
                                    apprGroupId : v.slipHeaderId,
                                    slipHeaderId: v.slipHeaderId,
                                    slipNo : v.slipNo,
                                    slipType: v.slipType,
                                    docTitleNm : v.headerRemark ? v.headerRemark + ' / ' + this.$numeral(v.accountedCr).format('0,0') : this.$numeral(v.accountedCr).format('0,0'),
                                    docContents : v.apprRemark,
                                    draftId : this.$store.state.loginInfo.loginId,
                                    loginId : this.$store.state.loginInfo.loginId,
                                }
                            })
                        }
                        _.forEach(this.apprLine, (v, index) => {
                            v.apprSeq = index + 1
                        })
                        
                        this.$http.post('/api/appr/bundle/req', {
                                approvalHeaderDtos: results,
                                approvalDetails: this.apprLine
                        }).then(res => {
                            vm.$store.commit('finish');
                            this.$swal({type: 'success', html: res.data})
                            .then((result) => {
                                if(result.value){
                                    vm.$store.commit('finish');
                                    //this.$parent.$parent.status.readonly = true
                                    this.$parent.close();
                                    this.$parent.$parent.goSearch();
                                }
                            });
                        }).catch((e) => {
                            this.$store.commit('finish')
                            // this.$parent.$parent.status.readonly = true
                        })
                    }
                })
              })
            },
            cancel(docMngNo) {
                this.$swal({
                    type: 'info',
                    text: `상신을 취소합니다. 계속 하시겠습니까?`,
                    showCancelButton: true,
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                }).then((result) => {
                    if (result.value) {
                        if (docMngNo != undefined) {
                            this.$http.get(`/api/appr/detail/cancel/${docMngNo}`)
                            .then(response => {
                                this.$parent.close();
                                this.$swal({type: 'info', text: '상신 취소되었습니다.'});
                                this.$parent.$parent.status.readonly = false
                            })
                            .catch(({message}) => {
                                console.error(message);
                            });
                        }
                    }
                })
            },
            openVendorPopup() {
                let vm = this
                this.$modal.open({
                    component: ApprLineSet,
                    props: {
                        lineList: this.apprLine,
                        setUserId : this.$store.state.loginInfo.loginId,
                    },
                    parent: this,
                    events: {
                        close(data) {
                            // Close event handler
                          if(data.lineList) {
                            vm.apprLine = data.lineList || []
                          }
                        }
                    }
                })
            },
            openApprPopup() {
                let vm = this
                this.$modal.open({
                    component: ApprActPop,
                    props: {
                        docTitleNm: this.apprInfo.docTitleNm,
                        apprNo: this.apprInfo.apprNo,
                        apprSeq: this.apprInfo.apprSeq
                    },
                    parent: this,
                    events: {
                        close(data) {
                            console.log('Appr Popup Close Event!!')
                            vm.$refs.apprDtl.getAppr(data)
                            vm.closeModal();
                        }
                    }
                })
            },
            changeApprovalLineEvent(data) {
                 this.apprLine = data
            },
            getMainApprLine() {
              this.$http.post(`/api/apprLine/main`, {
                compCd: this.$store.state.loginInfo.compCd,
                userId : this.$store.state.loginInfo.loginId,
                mainApprYn: 'Y',
                useYn: 'Y',
              })
              .then(res => {
                let lineList = []
                res.data.forEach(e => {
                  let apprTypeNm = '';
                  if(e.apprTypeCd==='20') apprTypeNm = '결재'
                  else if(e.apprTypeCd==='30') apprTypeNm = '합의'
                  lineList.push(
                      {
                        aprverId: e.apprUserId,
                        aprverUser : e.apprUserNm,
                        jobCd: e.jobCd,
                        jobNm : e.jobNm,
                        cctrCd: e.cctrCd,
                        cctrNm : e.cctrNm,
                        apprTypeCd: e.apprTypeCd,
                        apprType: apprTypeNm,
                        serveCd: e.serveCd,
                      }
                  )
                })
                this.apprLine = lineList || [];
              })
            },
            apprInfoCheck(data) {
                console.log('Appr Info Check!!')
                this.apprInfo = data
            },
            closeModal(){
                this.$parent.close()
            },
            getDtmFormat(val) {
              if (val) {
                return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
              }
            },
            getDoubleNumberFormat(val) {
              if(val) {
                return this.$numeral(val).format('0,0.00');
              }
            },
            isEmpty(value){
              if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
                return ""
              }else{
                return value
              }
            },
            sumTotalAmt(){
                this.totalAmt = 0
                for(var i=0; i<this.data.length; i++){
                  this.totalAmt += Number((this.data[i].usedAmt).replaceAll(",", ""))
                }
            },
        },
        computed: {
            isLoaded() {
                return this.loaded
            }
        },
        created() {
            this.makeColDef();
            this.getMainApprLine();
            this.sumTotalAmt();
        },
        watch: {
            value: {
                immediate: true,
                deep: true,
                handler(value) {
                    if(value !== undefined) {
                        this.data = value
                        this.loaded = true
                    }
                }
            }
        },
        mounted() {
    //pop slots modal move
    $(".animation-content").addClass('pop_min');
    $(".animation-content").draggable();
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '4');
  },
  destroyed() {
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '7');
  }
    }
</script>

<style scoped>
    .modal-card {
        width: auto;
    }
</style>
