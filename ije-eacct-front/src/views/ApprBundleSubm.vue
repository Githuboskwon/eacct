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

                <appr-erp-dtl ref="apprDtl" v-if="isLoaded" v-model="data" :slipTypeCd="slipTypeCd" :docType="docType" :approval-line.sync="apprLine" :docMngNo="docMngNo" :budReqNo="budReqNo" @input="$forceUpdate()" @approvalLine="changeApprovalLineEvent" @apprInfo="apprInfoCheck"/>
                <!-- Appr Header area -->
                <!--                <div class="table-area" style="margin-top: 0px;">
                                    <appr-header :doc-type="docType" :apprHeader="apprHeader" :appr-line="apprLine" v-model="data"/>
                                </div>-->
                <!-- //Appr Header area -->
                <!--            <component :is="docComponent" v-model="data" :readonly="true" @input="queryApprLineRule"/>-->

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
    
    const options = {
        'USE_SELECT_CD' : [{"detailCd" : "0", "detailNm" : "후첨"}, {"detailCd" : "1", "detailNm" : "첨부"}]
    }

    export default {
        props: {
            docMngNo: {
                type: String,
                required: false
            },
            budReqNo: {
                type: String,
                required: false
            },
            docType: {
                type: String,
                required: false,
                default: 'SLIP'
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
                default: 'ERP전표'
            },
            slipTypeCd: {
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
                apprNo: '',
                apprSeq: '',
                docTitleNm: '',
                //docTypeCd: '',
                docStatus: '',
                draftUserId: '',
                thisApprUser: '',
                deleYn: 'N',
                apprInfo: [],
                loaded: false,
                isNew: false,
                isMxMn: true,
                isLoading: false
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
                
                if(this.slipTypeCd === '21'){
                    this.columnDefs = [
                            {
                                headerName: '건별지급', 
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
                                      headerName: '지급문서번호',
                                      field: 'checkNo',
                                      width: 120,
                                      cellStyle:{textAlign: 'center'}
                                    },
                                    {
                                        headerName: '지급일자', 
                                        field: 'checkDt', 
                                        width: 120,
                                        valueFormatter: (params) => {
                                            return that.getDateFormat(params.value)
                                        },
                                        editable: false,
                                    },
                                    {
                                        headerName: '출금계좌번호', 
                                        field: 'bankAcctNum', 
                                        width: 150,
                                        editable: false,
                                    },                                                             
                                ]
                            },
                            {
                                headerName: '지급정보', 
                                children:[
                                    {
                                        headerName: '거래처코드', 
                                        field: 'vendorCd', 
                                        width: 120,
                                        cellStyle:{textAlign: 'left'}
                                    },
                                    {
                                        headerName: '거래처명', 
                                        field: 'vendorNm', 
                                        width: 200,
                                        cellStyle:{textAlign: 'left'}
                                    },
                                    {
                                        headerName: '통화', 
                                        field: 'currencyCd', 
                                        width: 90,
                                        cellStyle:{textAlign: 'center'}
                                    },
                                    {
                                        headerName: '입력금액', 
                                        field: 'paymentAmt', 
                                        width: 120,
                                        cellStyle:{textAlign: 'right'},
                                        valueFormatter: (params) => { 
                                            return that.getNumberFormat(params.value);
                                        }
                                    },
                                    {
                                        headerName: '원화금액', 
                                        field: 'baseAmt', 
                                        width: 120,
                                        cellStyle:{textAlign: 'right'},
                                        valueFormatter: (params) => { 
                                            return that.getNumberFormat(params.value);
                                        }
                                    },
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
                                headerName: '전자전표번호', 
                                field: 'eaSlipNo', 
                                width: 140,
                                cellStyle:{textAlign: 'left'},
                                hide:true
                            },
                        ]                
                
                }else if(this.slipTypeCd === 'E12'){//구매전표

                    this.columnDefs = [
                        {
                            headerName: 'ERP 구매전표', 
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
                                    headerName: '회계일자', 
                                    field: 'postDt', 
                                    width: 110,
                                    cellStyle:{textAlign: 'left'},
                                    valueFormatter: (params) => {               
                                        return that.$moment(params.value).format('YYYY-MM-DD');
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
                
                }else if(this.slipTypeCd === 'E13'){//공사비전표
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
                
                }else if(this.slipTypeCd === 'E14'){//기타전표
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
            addApprDesc(){
              return new Promise((resolve,reject) => {
                const vm = this
                this.$modal.open({
                  component:ApprBundlePopDrafter,
                  props:{
                    docTitleNm:this.apprTitle
                  },
                  events: {
                    close(drafterData) {
                      // Close event handler
                      if(!_.isEmpty(drafterData)){
                        //기안자 결재의견 set
                        vm.apprLine[0].apprDesc = drafterData
                        return resolve()
                      }else{
                        return reject()
                      }
                    }
                  }
                })
              })
            },
            approval() {//2020.08.26 전결수정
                if (this.apprLine.length < 1) {
                    this.$swal({type: 'warning', text: '결재선을 지정해주세요.'});
                    return false;
                }
              this.addApprDesc().then(() => {
                this.$swal({
                    type: 'info',
                    text: `결재내역을 상신합니다. 계속 하시겠습니까?`,
                    showCancelButton: true,
                    confirmButtonText: '예',
                    cancelButtonText: '아니오',
                }).then((result) => {
                    if (result.value) {
                       this.isLoading = true;
                       let results = [];
                       if(this.slipTypeCd === '21'){
                            _.forEach(this.data, (v, i) => {
                                results[i] = {
                                    docTypeCd : 'SLIP',
                                    docMngNo : v.slipNo,
                                    docTitleNm : + v.remark + ' / ' + this.$filters.amt(v.baseAmt),
                                }
                            })
                        }else if(this.slipTypeCd === 'E12'){ //구매전표
                            _.forEach(this.data, (v, i) => {
                                results[i] = {
                                    docTypeCd : 'SLIP',
                                    docMngNo : v.grSlipNo,
                                    docTitleNm : this.apprTitle + ' / (' + v.curCd +') '  + this.$filters.amt(v.totAmt) + ' / ' + v.hdSgtxt,
                                    taxAttachCd : v.taxAttachCd
                                }
                            })
                        }else if(this.slipTypeCd === 'E13'){//공사비전표
                            _.forEach(this.data, (v, i) => {
                                results[i] = {
                                    docTypeCd : 'SLIP',
                                    docMngNo : v.grSlipNo,
                                    docTitleNm : this.apprTitle + ' / (' + v.curCd +') '  + this.$filters.amt(v.totAmt) + ' / ' + v.hdSgtxt,
                                    taxAttachCd : v.taxAttachCd
                                }
                            })
                        }else if(this.slipTypeCd === 'E14'){//기타전표
                            _.forEach(this.data, (v, i) => {
                                results[i] = {
                                    docTypeCd : 'SLIP',
                                    docMngNo : v.grSlipNo,
                                    docTitleNm : this.apprTitle + ' / ' + this.$filters.amt(v.totAmt) + ' / ' + v.payCustNm
                                }
                            })
                        }

                        _.forEach(this.apprLine, (v, index) => {
                            v.apprSeq = index + 1
                        })
                        
                        this.$http.post('/api/appr/bundle/req', {
                                approvalHeaderDtos: results,
                                approvalDetails: this.apprLine
                        }).then(response => {
                            this.$swal({type: 'info', text: '상신이 완료되었습니다.'})
                            .then((result) => {
                                if(result.value){
                                    this.isLoading = false;
                                    this.$parent.$parent.status.readonly = true
                                    this.$parent.close();
                                    this.$parent.$parent.goSearch()
                                }
                            });
                        }).catch((e) => {
                            this.$parent.close();
                            this.$parent.$parent.status.readonly = true
                            this.$parent.$parent.goSearch()
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
                        lineList: this.apprLine
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
                            // Close event handler
                            console.log('Appr Popup Close Event!!')
                            vm.$refs.apprDtl.getAppr(data)
                        }
                    }
                })
            },
            changeApprovalLineEvent(data) {
                 this.apprLine = data
            },
            apprInfoCheck(data) {
                console.log('Appr Info Check!!')
                this.apprInfo = data
                console.log(JSON.stringify(this.apprInfo))
                console.log(this.apprInfo.thisApprUser +', '+this.apprInfo.deleYn)
            },
        },
        computed: {
            isLoaded() {
                return this.loaded
            }
        },
        created() {
            if(this.docMngNo === undefined) {
                this.isNew = true
            }
            else{
                this.isNew = false
                this.title = '결재보기'
            }

            this.makeColDef()            
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
