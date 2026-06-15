<template>
    <layout style="width: 1000px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="parentClose"></button></span>
        <div slot="content">
            <div class="table-name">
                <div style="display: flex;">
                    <h3 class="ico_table_name">선급금 조회</h3>
                    <div class="btn-type1" style="margin-left: auto;">
                        <el-button @click="payback" type="success" icon="el-icon-refresh-right" v-if="!readOnly">
                            반제
                        </el-button>
                        <el-button @click="goSearch" type="primary" icon="el-icon-search">
                            조회
                        </el-button>
                    </div>
                </div>
            </div>
            
            <div class="pop-content-search">
                <div class="field has-addons">
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 전표금액</span>
                            <el-input type="text" v-model="openerEnteredAmount" readonly style="width: 60%;">
                            </el-input>
                        </div>
                    </div>
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 반제 후 전표 잔액</span>
                            <el-input type="text" v-model="afterEnteredAmount" readonly style="width: 70%;">
                            </el-input>
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid-wrap">
                <ag-grid-vue 
                    style="width: 100%; height: 200px;"
                    class="ag-theme-alpine"
                    rowSelection="multiple"
                    :columnDefs="columnDefs"
                    :gridOptions="gridOptions"
                    :rowData="rowData"
                    :suppressMenuHide="true"
                    :stopEditingWhenCellsLoseFocus="true"
                    :defaultColDef="defaultColDef"
                    @row-selected="onRowSelected"
                    @grid-ready="onGridReady"
                    @cell-value-changed="onCellValueChanged"/>
                    <!-- @rowDoubleClicked="rowDoubleClick" -->
            </div>

            <div class="table-name" style="margin-top: 2%;">
                <div style="display: flex;">
                    <h3 class="ico_table_name">반제신청 조회</h3>
                    <div class="btn-type1" style="margin-left: auto;">
                        <el-button @click="cancelPayBack" type="danger" icon="el-icon-refresh-left" v-if="!readOnly">
                            미반제
                        </el-button>
                        <el-button @click="paymentSeach" type="primary" icon="el-icon-search">
                            조회
                        </el-button>
                    </div>
                </div>
                <div class="pop-content-search">
                    <div class="field has-addons">
                        <div class="control is-expanded search-area">
                            <div class="rs-mt2">
                                <span class="NotoM" style="margin-right: 10px;">- 반제신청 총금액</span>
                                <el-input type="text" v-model="paymentAllAmount" readonly style="width: 30%;">
                                </el-input>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="grid-wrap">
                    <ag-grid-vue 
                        style="width: 100%; height: 200px;"
                        class="ag-theme-alpine"
                        rowSelection="multiple"
                        :columnDefs="cancleColumnDefs"
                        :gridOptions="canceleGridOptions"
                        :rowData="cancelRowData"
                        :suppressMenuHide="true"
                        :stopEditingWhenCellsLoseFocus="true"
                        :defaultColDef="cancleDefaultColDef"
                        @grid-ready="onCancleGridReady"
                        @row-selected="onCancleRowSelected"
                        @cell-value-changed="onCancleCellValueChanged"/>

                </div>
            </div>
        </div>
    </layout>
</template>

<script>
import Layout from '@/components/ModalSlot4.vue'

import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
    props: {
        vendorId: { Type: String, required: true },
        vendorCd: { Type: String, required: true },
        vendorSiteId: { Type: String, required: true },
        venPaymentCurrencyCd: { Type: String, required: true },
        slipHeaderId: { Type: String, required: true },
        ledgerId: { Type: String, required: true },
        postingDt: { Type: String, required: true },
        personId: { Type: String, required: true },
        totAmt: { Type: Number, required: true },
        originPrepaymentApplyFlag: { Type: String, required: true },
        readOnly: { Type: Boolean, required: false, default: false}
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '선급금 조회',
            rowData: [],
            gridOptions: {
                suppressRowClickSelection: true,
                suppressScrollOnNewData: true,
            },
            columnDefs:[],
            defaultColDef: { 
                resizable: true, 
                filter:true, 
                sortable: true,
                // flex: 2
            },
            gridApi : null,
            columnApi : null,
            openerEnteredAmount: 0, //전표 총 금액
            afterEnteredAmount: 0, //반제 후 전표잔액

            //미반제 
            paymentAllAmount: 0, //전체 반제 금액(그리드 세팅)
            canceleGridOptions: {
                suppressRowClickSelection: true,
                suppressScrollOnNewData: true,
            },
            cancleDefaultColDef: {
                resizable: true, 
                filter:true, 
                sortable: true,
            },
            cancleGridApi : null,
            cancleColumnApi : null,
            cancleColumnDefs: [],
            cancelRowData: []
        }
    },
    created() {
        this.openerEnteredAmount = this.totAmt;

        this.makeColDef();
        this.makeCancleColDef();
    },
    methods: {
        goSearch() {
            this.$store.commit('loading');
            // /${this.search.code}/${this.search.name}
            const { vendorId, vendorSiteId, venPaymentCurrencyCd} = this;
            const params = {
                vendorCd : vendorId,
                vendorSiteId,
                currencyCd: venPaymentCurrencyCd
            }
            this.$http.post(`/api/prepayment/advanced/list`, params)
            .then(res => res.data)
            .then(data => {
                this.rowData = data;
            })
            .finally(() => {
                this.$store.commit('finish');
            });
        },
        onGridReady(params){
            this.gridApi = params.api;
            this.columnApi = params.columnApi;
            this.autoSizeAll(this.columnApi);
        },
        onCancleGridReady(params) {
            this.cancleGridApi = params.api;
            this.cancleColumnApi = params.columnApi;
            this.autoSizeAll(this.cancleColumnApi);
        },
        /**
         * * 그리드 컬럼 width 자동 맞춤
         * @param {*} columnApi 
        */
        autoSizeAll(columnApi) {
            const allColumnIds = [];
            columnApi.columnController.gridColumns.forEach((column) => {
                if(column.getId() !== 'chk') {
                    allColumnIds.push(column.getId());
                }
            });
            columnApi.columnController.autoSizeColumns(allColumnIds, false);
        },
        makeColDef(){
            const self = this;
            // {"field": "chk", "headerName": "", "type": "", "suppressMenu": true, "editable": false, "layout" : "checkbox", "hide": false, "cellStyle": {"textAlign": "center"}},
            this.columnDefs = [
                {
                    headerName:'',
                    field:'chk',
                    suppressMenu: true,
                    sortable: false,
                    editable: false,
                    headerCheckboxSelection: true,
                    checkboxSelection: true,
                    showDisabledCheckboxes: true,
                    width: 50,
                    cellStyle: (params) => {
                        return { 'item-align': 'center', 'color' : 'transparent'};
                    }
                },
                {
                    headerName:'선급전표번호',
                    field:'prepayInvoiceNumber',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'선급라인번호',
                    field:'prepayLineNumber',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'통화',
                    field:'paymentCurrencyCode',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'선급라인금액',
                    field:'lineAmount',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return params.data.paymentCurrencyCode === 'KRW' ? self.$numeral(params.value).format('0,0') : self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'선급잔액',
                    field:'prepayAmountRemaining',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return params.data.paymentCurrencyCode === 'KRW' ? self.$numeral(params.value).format('0,0') : self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'반제신청 금액',
                    field:'amountToApply',
                    editable: !this.readOnly,
                    cellStyle:{textAlign:'right', 'background' : 'pink'},
                    valueFormatter: (params) => {
                        return params.data.paymentCurrencyCode === 'KRW' ? self.$numeral(params.value).format('0,0') : self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'DESCRIPTION',
                    field:'description',
                    cellStyle:{textAlign:'left'},
                }
            ];
            this.goSearch();
        },
        /**
         * 미반제 처리 할 그리드 컬럼 정의
         */
        makeCancleColDef() {
            const self = this;
            this.cancleColumnDefs = [
                {
                    headerName:'',
                    field:'chk',
                    suppressMenu: true,
                    sortable: false,
                    editable: false,
                    headerCheckboxSelection: true,
                    checkboxSelection: true,
                    showDisabledCheckboxes: true,
                    width: 50,
                    cellStyle: (params) => {
                        return { 'item-align': 'center', 'color' : 'transparent'};
                    }
                },
                {
                    headerName:'선급전표번호',
                    field:'prepayInvoiceNumber',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'선급라인번호',
                    field:'prepayLineNumber',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'DESCRIPTION',
                    field:'description',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'반제라인ID',
                    field:'',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'통화',
                    field:'invoiceCurrencyCode',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'선급라인금액',
                    field:'lineAmount',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return params.data.invoiceCurrencyCode === 'KRW' ? self.$numeral(params.value).format('0,0') : self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'선급잔액',
                    field:'prepayAmountRemaining',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return params.data.invoiceCurrencyCode === 'KRW' ? self.$numeral(params.value).format('0,0') : self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'반제신청 금액',
                    field:'amountToApply',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return params.data.invoiceCurrencyCode === 'KRW' ? self.$numeral(params.value).format('0,0') : self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'반제 후 금액',
                    field:'afterAmt',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return params.data.invoiceCurrencyCode === 'KRW' ? self.$numeral(params.value).format('0,0') : self.$numeral(params.value).format('0,000.000')
                    }
                },
            ];
            this.paymentSeach();
        },
        /**
         * * 그리드 체크 선택 이벤트
         * @param {*} params 
         */
        onRowSelected(params) {
            const rowIndex = params.rowIndex;
            let rowNode = params.node;
            rowNode.setSelected(rowNode.isSelected());
            this.rowData[rowIndex].chk = params.node.isSelected();
        },
        /**
         * * 미반제 처리 할 그리드 체크 선택 이벤트
         * @param {*} params 
         */
        onCancleRowSelected(params) {
            const rowIndex = params.rowIndex;
            let rowNode = params.node;
            rowNode.setSelected(rowNode.isSelected());
            this.cancelRowData[rowIndex].chk = params.node.isSelected();
        },
        /**
         * * 그리드 변수 변경 이벤트 시
         * @param {*} params 
        */
        onCellValueChanged(params) {
            const field = params.colDef.field;
            const newValue = params.newValue;
            const oldValue = params.oldValue;
            const rowIndex = params.rowIndex;
            const newData = this.rowData[rowIndex];

            if(field === 'amountToApply') {
                newData.amountToApply = params.value;
                // 반제 후 전표잔액 계산
                // 반제 후 전표잔액 = 전표금액 - (반제신청금액 합) - (미반제신청금액 합) ???
                const oeAmt = this.$numeral(this.openerEnteredAmount).value() ; //전표금액
                const totToAmt = this.rowData.map(x => this.$numeral(x.amountToApply || 0).value()).reduce((prev, next) => {
                    return  prev + next ;
                }, 0) ;
                const totCancleToAmt = this.cancelRowData.map(x => this.$numeral(x.amountToApply || 0).value()).reduce((prev, next) => {
                    return  prev + next;
                }, 0) ;
                // 반올림 진행
                let afterEnteredAmountValue = oeAmt - totToAmt - totCancleToAmt;

                // 반올림 진행 (KRW와 다른 통화 코드에 따라 반올림 자리 다르게 설정)
                if (this.venPaymentCurrencyCd === 'KRW') {
                  this.afterEnteredAmount = this.$numeral(afterEnteredAmountValue).format('0,000'); // 포맷 적용
                } else {
                  afterEnteredAmountValue = afterEnteredAmountValue.toFixed(3); // 소수점 3자리까지 반올림
                  this.afterEnteredAmount = this.$numeral(afterEnteredAmountValue).format('0,000.000'); // 포맷 적용
                }

            }
        },
        /**
         * * 그리드 변수 변경 이벤트 시
         * @param {*} params 
        */
        onCancleCellValueChanged(params) {

        },
        /**
         * 반제 처리
         */
        payback() {
            if(this.readOnly) return;

            let chkFleg = true;

            const params = this.gridApi.getSelectedNodes().map(x => x.data).map(x => {
                x.slipHeaderId = this.slipHeaderId;
                x.ledgerId = this.ledgerId;
                x.prepayInvoiceId = x.invoiceId;
                x.prepayAmount = x.lineAmount;
                x.amountToApply = this.$numeral(x.amountToApply).value();
                x.applyGlDate = this.$moment(this.postingDt).format('YYYY-MM-DDTHH:mm:ss');
                x.createdPersonId = this.personId;
                x.lastUpdatedPersonId = this.personId;
                if(x.prepayAmountRemaining < x.amountToApply) chkFleg = false;
                return x;
            });

            if(!chkFleg){
              this.$message.error({ type: `warning`, message: `반제금액보다 선급잔액이 작습니다.` });
              return false;
            }

            if(!params[0]) {
                this.$message({ type: `warning`, message: `반제처리 할 행을 선택해주세요.` });
                return false;
            }
            let checkAmountToApply = this.gridApi.getSelectedNodes().map(x => x.data).reduce((prev, next) => {
                return prev + next.amountToApply
            }, 0);

            // 반올림 처리
            if (this.venPaymentCurrencyCd !== 'KRW') {
              checkAmountToApply = checkAmountToApply.toFixed(3); // 포맷 적용
            }

            console.log("checkAmountToApply :::: ",checkAmountToApply);
            console.log("openerEnteredAmount :::: ",this.$numeral(this.openerEnteredAmount).value());

            
            if(!checkAmountToApply) {
                this.$message.error({ type: `warning`, message: `반제처리 할 금액이 없습니다.` });
                return false;
            }
            if(checkAmountToApply < 0) {
                this.$message.error({ type: `warning`, message: `마이너스 금액은 반제처리가 불가능합니다.` });
                return false;
            }
            if(checkAmountToApply > this.$numeral(this.openerEnteredAmount).value()) {
                this.$message.error({ type: `warning`, message: `반제금액보다 전표금액이 작습니다.` });
                return false;
            }

            this.$store.commit('loading');
            this.$http.post(`/api/prepayment/advanced/save`,params)
            .then(res => {
                this.$alert(`${res.data}`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'success',
                    center: true,
                    callback: () => {
                        const params = {
                            amount : this.$numeral(this.openerEnteredAmount).value(),
                            prepaymentApplyFlag: 'K'
                        }
                        this.$emit('apply', params);

                        this.paymentSeach();
                    }
                });
            }).finally(_ => {
                this.$store.commit('finish');
            });
        },
        /**
         * 미반제 처리
         */
        cancelPayBack() {
            if(this.readOnly) return;

            const params = this.cancleGridApi.getSelectedNodes().map(x => x.data).map(x => {
                x.slipHeaderId = this.slipHeaderId;
                x.ledgerId = this.ledgerId;
                return x;
            });
            if(!params[0]) {
                this.$message({ type: `warning`, message: `미반제처리 할 행을 선택해주세요.` });
                return false;
            }
            this.$store.commit('loading');
            this.$http.post(`/api/prepayment/advanced/delete`,params)
            .then(res => res.data)
            .then(data => {
                this.$alert(`${data}`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'success',
                    center: true,
                    callback: () => {
                        const selectAmt = this.cancleGridApi.getSelectedNodes().map(x => x.data).map(x => this.$numeral(x.amountToApply).value()).reduce((prev, next) => {
                            return prev + next;
                        }, 0);
                        
                        const params = {
                            amount : this.$numeral(this.afterEnteredAmount).value() - selectAmt,
                            prepaymentApplyFlag: 'N'
                        }
                        this.$emit('apply', params);

                        this.paymentSeach();
                    }
                });
            })
            .finally(_ =>{
                this.$store.commit('finish');
            });
            
        },
        /**
         * 미반제 할 리스트
         */
        paymentSeach() {
            this.$store.commit('loading');
            const { vendorId, vendorSiteId, venPaymentCurrencyCd, slipHeaderId} = this;
            const params = {
                vendorCd : vendorId,
                vendorSiteId,
                currencyCd: venPaymentCurrencyCd,
                slipHeaderId
            }
            this.$http.post(`/api/prepayment/reimbursement/list`, params)
            .then(res => res.data)
            .then(data => {
                this.cancelRowData = data;
                this.paymentAllAmount = data.reduce((prev, next) => {
                    return prev + next?.amountToApply || 0
                }, 0);

                if (this.venPaymentCurrencyCd !== 'KRW') {
                  this.paymentAllAmount = this.paymentAllAmount.toFixed(3); // 소수점 3자리 반올림
                }
            })
            .finally(() => {
                const oeAmt = this.$numeral(this.openerEnteredAmount).value() ; //전표금액
                const payAmt = this.$numeral(this.paymentAllAmount).value() ; //반제 신청 총 금액금액

                // 반올림 진행
                let afterEnteredAmountValue = oeAmt - payAmt;

                // 반올림 처리
                if (this.venPaymentCurrencyCd === 'KRW') {
                  this.afterEnteredAmount = this.$numeral(afterEnteredAmountValue).format('0,000'); // 포맷 적용
                } else {
                  afterEnteredAmountValue = afterEnteredAmountValue.toFixed(3); // 소수점 3자리 반올림
                  this.afterEnteredAmount = this.$numeral(afterEnteredAmountValue).format('0,000.000'); // 포맷 적용
                }

                this.$store.commit('finish');
            });
        },
        /**
         * 엑박으로 팝업 클로징 할 때 이벤트 처리
         * * 선급금정산 기존 플래그로 재설정
         */
        parentClose() {
            this.$emit('parentClose', this.originPrepaymentApplyFlag ? 'K' : 'N')
            this.$parent.close();
        }
    }
}
</script>