<template>
    <div>
        <div class="table-area">
            <div class="table-b">
                <div class="table-header">
                    <div class="table-name">
                        <h3 class="ico_table_name">원천세정보</h3>
                    </div>
                </div>
                <div class="grid-tbl-box">
                    <ag-grid-vue
                        id="awt-agGrid"
                        ref="awt-grid"
                        class="ag-theme-alpine"
                        rowSelection="multiple"
                        style="width: 100%; height: 220px;"
                        :columnDefs="columnDefs"
                        :gridOptions="gridOptions"
                        :rowData="value.awt"
                        :defaultColDef="defaultColDef"
                        :stopEditingWhenCellsLoseFocus="true"
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

export default {
    props: ['value'],
    components: {
        
    },
    data() {
        return {
            rowCount: 0,
            gridOptions: {
                suppressScrollOnNewData: true,
            },
            columnDefs:[],
            defaultColDef: { 
                resizable: true,
                editable: false
            },
            gridApi : null,
            columnApi : null,
            stayScrolledToEnd: true,
        }
    },
    created() {
        this.getAwt()
        .then(awt => {
            this.value.awt = awt;
        })
        .finally(() => {
            this.createColumnDef()
            .then(colums => {
                this.columnDefs = colums;
            })
            .catch(err => {
                this.$message({ type: `danger`, message: `${err}` });
            })
            .finally(_ => {
                if(this.value.slipTypeCd && this.value.slipNo) {
                    const {slipNo, slipTypeCd} = this.value;
                    let methodPath = `/api/slip/line`;

                    this.$http.get(`${methodPath}/${slipNo}`).then(res => res.data)
                    .then(data => {
                        
                        const rowData = data.map((x, i) => {
                            //디테일 저장시 키값 재정의
                            x.slipTypeCd = x.slipTypeCode;
                            x.cctrCd = x.budgetDeptCode;
                            x.cctrNm = x.budgetDeptName;
                            x.deptCd = x.actualDeptCode;
                            x.deptNm = x.actualDeptName;
                            x.deptType = x.actualDeptType;
                            x.acctCd = x.acctCode;
                            x.acctNm = x.acctName;
                            x.acctDffCnt = x.dffCnt;
                            x.acctRequiredFlagCnt = x.requiredFlagCnt;
                            x.projectId = x.projectId;
                            x.pjtCd = x.projectCode;
                            x.pjtNm = x.projectName;
                            x.taskNo = x.taskCode;
                            x.taskNm = x.taskName;
                            x.trAcctCd = x.trAcctCode; //금융계좌 코드
                            x.productCd = x.itemGroupCode;
                            x.productNm = x.itemGroupName;
                            x.taxAcctCode = x.taxAcctCode;
                            x.taxId = x.taxId;
                            x.taxCd = x.taxCode;
                            x.taxNm = x.taxCode;
                            x.segment9Cd = x.segment9Code;
                            x.segment10Cd = x.segment10Code;
                            if(['AWT', 'ETCAWT'].includes(slipTypeCd)) {
                                if(x.withholdingTaxCode === 'null') {
                                    x.withholdingTaxCode = '';
                                }
                            }

                            return x;
                        });
                        
                        rowData.forEach((x, i) => {
                            
                            const subRowNode = this.gridApi.getRowNode(i);
                            subRowNode.setDataValue(`preTaxAmt`, rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
                        });
                        
                    });
                }
                else {
                    if(this.value.rowData && this.value.rowData.length > 0) {
                        this.value.awt.forEach((x, i) => {
                            const subRowNode = this.gridApi.getRowNode(i);
                            subRowNode.setDataValue(`preTaxAmt`, this.value.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
                        });
        
                    } else {
                        this.makeBottomTotal();
                    }

                }
            });
        });
    },
    methods: {
        onGridReady(params) {
            this.gridApi = params.api;
            this.columnApi = params.columnApi;
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
                                    return 2;
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
                            field: "awtAccountName", 
                            headerName: "원천세 계정명",
                            width: "300px",
                            cellStyle:{textAlign: 'left'},
                        },
                        {
                            field: "taxRate", 
                            headerName: "세율(%)",
                            cellStyle:{textAlign: 'right'}
                        },
                        {
                            field: "preTaxAmt", 
                            headerName: "공급가액",
                            cellStyle:{textAlign:'right'},
                            valueFormatter: (params) => {
                                return self.$numeral(params.value || 0).format('#,#');
                            }
                        },
                        {
                            field: "afterTaxAmt", 
                            headerName: "세액",
                            cellStyle:{textAlign:'right'},
                            valueFormatter: (params) => {
                                return self.$numeral(params.value || 0).format('#,#');
                            }
                        },
                        {
                            field: "vendorName", 
                            headerName: "공급자명",
                            cellStyle:{textAlign: 'left'}
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
            const newData = this.value.awt[rowIndex];

            if(field === 'preTaxAmt') {
                const preTaxAmt = this.$numeral(newData.preTaxAmt).value();
                const taxRate = this.$numeral(newData.taxRate).value();
                
                const afterTaxAmt = Math.floor((preTaxAmt * (taxRate / 100)) / 10) * 10;
                
                this.setGridDataValue(rowIndex, 'afterTaxAmt', afterTaxAmt);

                const afterTotTaxAmt = this.value.awt.map(x => x.afterTaxAmt).reduce((prev, next) => {return prev + next}, 0)
                
                //세액이 1000원 미만일 경우 전부 0원 처리
                //240202 세액 절삭 취소 요청 반영
                /*if(afterTotTaxAmt < 1000) {
                    this.value.awt.forEach((x, i) => {
                        this.setGridDataValue(i, 'afterTaxAmt', 0);
                    });
                }*/
            }
            
            this.makeBottomTotal();
         },
         /**
         * * 그리드 셀 데이터 변경
         * @param {*} i (index)
         * @param {*} k (key)
         * @param {*} v (value)
         */
        setGridDataValue(i, k, v) {
            const rowNode = this.gridApi.getRowNode(i);
            // rowNode.setDataValue(`${k}`, v);
            this.value.awt[i][k] = v;
            this.gridOptions.api.refreshCells();
        },
        /**
         * * grid row 데이터 변화
         * @param {*} event 
         */
        handleRowDataChanged(event) {
            
            const index = this.value.awt.length - 1;
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
                , taxRate: this.value.awt.map(x => x.taxRate).reduce((prev, next) => {return prev + next}, initValue)
                , preTaxAmt: this.value.awt[0]?.preTaxAmt || 0
                , afterTaxAmt: this.value.awt.map(x => x.afterTaxAmt).reduce((prev, next) => {return prev + next}, initValue)
            }];
            
            this.gridOptions.api.setPinnedBottomRowData(sum);
        },
        /**
         * * 스크롤 감지하여 최 하단으로 이동 시킴.
         * @param {*} event 
         */
        handleScroll(event) {
            const grid = document.getElementById('awt-agGrid');
            if (grid) {
                const gridBody = grid.querySelector('.ag-body-viewport');
                const scrollPos = gridBody.offsetHeight + event.top;
                const scrollDiff = gridBody.scrollHeight - scrollPos;
                
                this.stayScrolledToEnd = (scrollDiff <= 3);
            }
        },
        /**
         * 원천세 정보 가져오기
         */
        getAwt() {
            return new Promise((resolve, reject) => {
                const params = {
                    awtGroupId: this.value.awtGroupId,
                    trxTypeCd : this.value.slipTypeCd
                }
                this.$http.post(`/api/trx/getAwt`, params)
                .then(res => res.data)
                .then(data => resolve(data))
                .catch(err => reject(err));
            })
        }
    }
}
</script>