<template>
    <div>
        <div class="table-area">
            <div class="table-b">
                <div class="table-header">
                    <div class="table-name">
                        <h3 class="ico_table_name">출장(항공권)</h3>
                    </div>
                    <div class="btn-wrap btn-type2 clearfix mr3" v-if="!readOnly">
                        <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
                        <el-button type="info" icon="el-icon-document-add" @click="initRow">초기화</el-button>
                    </div>
                </div>
                <div class="grid-tbl-box">
                    <ag-grid-vue
                        id="trip-agGrid"
                        ref="trip-grid"
                        class="ag-theme-alpine"
                        rowSelection="multiple"
                        style="width: 100%; height: 220px;"
                        :columnDefs="columnDefs"     
                        :gridOptions="gridOptions"
                        :rowData="value.trip"
                        :defaultColDef="defaultColDef"
                        :stopEditingWhenCellsLoseFocus="true"
                        :enableRangeSelection="true"
                        @bodyScroll="handleScroll($event)"
                        @rowDataChanged="handleRowDataChanged($event)"
                        @grid-ready="onGridReady"
                        @cell-key-press="onCellKeyPress"
                        @cell-value-changed="onCellValueChanged"
                    />
                </div>
            </div>
        </div>
    </div>
</template>

<script>

import CardAirSlipModal from '@/components/accrualSlip/Modals/CardAirSlipModal'; /** 작성전표 조회 */

export default {
  compatConfig: { MODE: 2 },
    props: ['value', 'readOnly'],
    data() {
        return {
            rowCount: 0,
            gridOptions: {
                suppressScrollOnNewData: true,
                suppressRowClickSelection: true
            },
            columnDefs:[],
            defaultColDef: { 
                resizable: true,
                editable: false
            },
            gridApi : null,
            columnApi : null,
            stayScrolledToEnd: true,
            copyRowData: [], //row copy value
        }
    },
    methods: {
        onGridReady(params) {
            this.gridApi = params.api;
            this.columnApi = params.columnApi;
            this.createColumnDef()
            .then(colums => {
                this.columnDefs = colums;
            })
            .catch(err => {
                this.$message({ type: `danger`, message: `${err}` });
            })
            .finally(_ => {
                this.makeBottomTotal();
            });
        },

        createColumnDef() {
            const self = this;
            return new Promise((resolve, reject) => {
                try {
                    const colDef = [
                        {
                            field: "no", 
                            headerName: "순번",
                            width: 80,
                            colSpan:(params) => {
                                if(params.data.rn === '합계'){
                                    return 6;
                                }else{
                                    return 1;
                                }
                            },
                            cellStyle:(params) => {
                                if(params.data.rn === '합계'){
                                    return {textAlign: 'center'}
                                }else{
                                    return {'justify-content': 'center','align-items': 'center', 'display': 'flex','height': '100%'}
                                }
                            },
                            valueFormatter: (params) => {
                                if(params.data.rn === '합계'){
                                    return `합계`
                                }else{
                                    return params.node.rowIndex+1;
                                }
                            }
                        },
                        {
                            field: "strPostingDate", 
                            headerName: "회계일자",
                            cellStyle:{textAlign: 'center'},
                            valueFormatter: (params) => {
                                return self.$moment(params.value).format('YYYY-MM-DD');
                            }
                        },
                        {
                            field: "slipNo", 
                            headerName: "전표번호",
                            cellStyle:{textAlign: 'center'}
                        },
                        {
                            field: "slipTypeText", 
                            headerName: "거래유형",
                            cellStyle:{textAlign: 'center'}
                        },
                        {
                            field: "erpVendorName", 
                            headerName: "거래처",
                            cellStyle:{textAlign: 'center'}
                        },
                        {
                            field: "usedCur", 
                            headerName: "통화",
                            cellStyle:{textAlign: 'center'}
                        },
                        {
                            field: "usedAmt", 
                            headerName: "금액",
                            cellStyle:{textAlign:'right'},
                            valueFormatter: (params) => {
                                return self.$numeral(params.value || 0).format('#,#');
                            }
                        }
                    ];
                    resolve(colDef);
                } catch(error) {
                    reject(error);
                }
            });
        },
        /**
         * * 그리드 Cell Enter Key 전용으로 사용 .
         * @param {*} e 
         */
         onCellKeyPress(e) {
            if (!e.event) {
                return;
            }
            const keyboardEvent = e.event;
            const key = keyboardEvent.key;
            const field = e.colDef.field;
            
            if (key.length) {
                if (key === 'Enter') {

                    //e.rowIndex ,e.value
                   
                }
            }
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
            const newData = this.value.trip[rowIndex];
         },
        /**
         * * grid row 데이터 변화
         * @param {*} event 
         */
        handleRowDataChanged(event) {
            const index = this.value.trip.length - 1;
            if (this.stayScrolledToEnd) {
                this.gridOptions.api.ensureIndexVisible(index, 'bottom');
            }
        },
        makeBottomTotal() {
            this.gridOptions.getRowStyle = (params) =>{
                if(params.data.rn === '합계'){
                    return {background: '#f9f9f3'};
                }
            }
            const initValue = 0;
            let sum = [{
                rn: '합계'
                , usedAmt: this.value.trip.map(x => this.$numeral(x.detailUsedAmt).value()).reduce((prev, next) => {return prev + next}, initValue)
            }];
            this.gridOptions.api.setPinnedBottomRowData(sum);
        },
        /**
         * * 스크롤 감지하여 최 하단으로 이동 시킴.
         * @param {*} event 
         */
        handleScroll(event) {
            const grid = document.getElementById('trip-agGrid');
            if (grid) {
                const gridBody = grid.querySelector('.ag-body-viewport');
                const scrollPos = gridBody.offsetHeight + event.top;
                const scrollDiff = gridBody.scrollHeight - scrollPos;
                
                this.stayScrolledToEnd = (scrollDiff <= 3);
            }
        },
        /**
         * * 부가세 헤더 세팅
         */ 
         setHeaderVat() {
            let success = false;
            const {taxEvidenceType} = this.value;
            const params = {
                searchCd: '매입불공제'
            }
            this.$http.post(`/api/surTaxCode/list/${taxEvidenceType}/form`, params)
            .then(res => res.data)
            .then(data => {
                if(!!data && data.length === 1) {
                    const { 
                        taxStatusCode,  //부가세명
                        percentageRate, //세율
                        taxRateId,      //부가세계정ID (이게 key 인듯)
                        taxRateCode,    //부가세계정
                        taxAcctCode     //부가세계정코드
                    } = data[0];
                    this.value.taxStatusCode = taxStatusCode;
                    this.value.percentageRate = percentageRate;
                    this.value.taxRateId = taxRateId;
                    this.value.taxRateCode = taxRateCode;
                    this.value.taxAcctCode = taxAcctCode;
                    this.value.taxAcctName = taxRateCode; //저장 시 필요한 듯
                    success = true;
                }
            })
            .finally(() => {
                console.log(`TRIP 부가세 세팅 ${success ? '성공' : '실패'}`)
            });
        },
        /**
         * 작성전표 조회 (CardAirSlipModal) 팝업 작성
         * * 행 추가
         */
        addRow() {
            const self = this;
            const {empNo} = this.value;
            const copyRowData = this.copyRowData;
            this.$modal.open({
                component: CardAirSlipModal,
                props: {
                    empNo,
                    copyRowData
                },
                parent: this,
                width: 1200,
                events: {
                    close(val) {
                        val.map(x => {
                            x.detailSlipHeaderId = x.slipHeaderId;
                            x.detailSlipNo = x.slipNo;
                            x.detailGlDt = x.strPostingDate;
                            x.detailTrxType = x.slipType;
                            x.detailVendorId = x.integrationVendorNum;
                            x.detailVendorNm = x.erpVendorName
                            x.detailUsedCur = x.usedCur;
                            x.detailUsedAmt = x.usedAmt;
                            x.detailRegId = empNo;
                            x.detailRegDtm = self.$moment(x.addDate).format("YYYY-MM-DD HH:mm:ss")
                            self.value.trip.push(x);
                            self.copyRowData.push(x);
                        });
                        self.makeBottomTotal();
                    }
                }
            });
        },
        /**
         * * 초기화
         */
        initRow() {
            this.value.trip = [];
            this.copyRowData = [];
        }
    }
}
</script>