<template>
    <div class="table-area">
        <div class="table-b">
            <div class="table-header">
                <div class="table-name" style="width: 180px">
                    <h3 class="ico_table_name">거래상세내역</h3>
                </div>
            </div>


          <div class="grid-tbl-box">
              <ag-grid-vue
                  id="agGrid"
                  ref="grid1"
                  class="ag-theme-alpine"
                  rowSelection="multiple"
                  :style="{ width: '100%', height: gridHeight }"
                  :columnDefs="columnDefs1"
                  :rowData="rowData"
                  :framework-components="frameworkComponent"
                  :defaultColDef="defaultColDef"
                  :suppressMenuHide="true"
                  @grid-ready="onGridReady"/>
          </div>

          <div class="grid-tbl-box">
            <ag-grid-vue
                id="agGrid"
                ref="grid2"
                class="ag-theme-alpine"
                rowSelection="multiple"
                :style="{ width: '100%', height: gridHeight }"
                :columnDefs="columnDefs2"
                :rowData="rowData"
                :framework-components="frameworkComponent"
                :defaultColDef="defaultColDef"
                :suppressMenuHide="true"
                @grid-ready="onGridReady"/>
          </div>

          <div class="grid-tbl-box">
            <ag-grid-vue
                id="agGrid"
                ref="grid3"
                class="ag-theme-alpine"
                rowSelection="multiple"
                :style="{ width: '100%', height: gridHeight }"
                :columnDefs="columnDefs3"
                :rowData="rowData"
                :framework-components="frameworkComponent"
                :defaultColDef="defaultColDef"
                :suppressMenuHide="true"
                @grid-ready="onGridReady"/>
          </div>

          <div class="grid-tbl-box">
            <ag-grid-vue
                id="agGrid"
                ref="grid4"
                class="ag-theme-alpine"
                rowSelection="multiple"
                :style="{ width: '100%', height: gridHeight }"
                :columnDefs="columnDefs4"
                :rowData="rowData"
                :framework-components="frameworkComponent"
                :defaultColDef="defaultColDef"
                :suppressMenuHide="true"
                @grid-ready="onGridReady"/>
          </div>

          <div class="grid-tbl-box">
            <ag-grid-vue
                id="agGrid"
                ref="grid5"
                class="ag-theme-alpine"
                rowSelection="multiple"
                :style="{ width: '100%', height: gridHeight }"
                :columnDefs="columnDefs5"
                :rowData="rowData"
                :framework-components="frameworkComponent"
                :defaultColDef="defaultColDef"
                :suppressMenuHide="true"
                @grid-ready="onGridReady"/>
          </div>

        </div>
    </div>
</template>

<script>

/**
 * * (임시) 자리수 표기
 * @param {*} curCd 
 * @param {*} decimal 
 */
 const numFormats = (curCd, decimal) => {
    let format = `0,000`;
    if(decimal > 0 && curCd != 'KRW') {
        format = `${format}.` + '#'.repeat(decimal);
    }
    return format;
}

/***
 * * GRID
 */
import SlipMngItemPop from '@/components/SlipMngItemPop'; /** 관리항목 팝업 */
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn.vue"; /** 그리드 검색 버튼 컴포넌트 */

export default {
  compatConfig: { MODE: 2 },
    props: {
        slipTypeCd: {
            Type: String,
            required: true,
            default: 'COMMON'
        },
        gridData: {
            Type: Array,
            required: true,
        },
        slipNo: {
            type: String,
            required: false,
        }
    },
    data() {
        return {
            options: {  //공통코드 리스트
                'TAX_FLAG': [], //공제여부 유형
                'DISTANCE_GB': [], //시내/시외
                'AREA_LIST': [], //출장거리
                'OIL_KIND': [], //유종
                'TPS_TYPE': [], //교통수단
                'TRAFFIC_TYPE': [], //기타교통비 1,2,3
                'TRIP_AMT_TYPE': [], //국내/해외 출장 용도
                'WITHHOLDING_TAX_CODE': [], //원천세코드(잡급비)
                'FRGN_CUR_CD': [],
            },
            frameworkComponent: {
                schBtn: AgGridSearchBtn
            },
            gridOptions: {},
            columnDefs1:[],
            columnDefs2:[],
            columnDefs3:[],
            columnDefs4:[],
            columnDefs5:[],
            gridHeight: '110px',
            defaultColDef: {
                editable: false,
                resizable: true,
                // flex: 1
            },
            gridApi : null,
            columnApi : null,
            rowData: [],
            trxTypeCd: '', //거래유형
        }
    },
    methods: {
        onGridReady(params) {
            this.gridApi = params.api;
            this.columnApi = params.columnApi;
        },
        onCellClicked(params) {
            console.log("onCellClicked", params);
        },
        createColumnDef() {
            const self = this;
            const colDef = new Promise((resolve) => {
                fetch(`/json/accrual-slip-print.json`, { method: 'GET' })
                .then(res => res.json())
                .then(res => { resolve(res.data); })
                .catch(_ => { resolve([]); })
            });


            /**
             * * 그리드 컬럼 확장 하기.
             * @param {*} cols 
             */
             const extensionColDef = (cols) => {
                if(cols.layout === 'checkbox') {
                    cols.hide = true;
                } else if(cols.layout === 'number') {
                    cols.valueFormatter = (params) => {
                        return self.$numeral(params.value).format('0,0');
                    };
                } else if(cols.layout === 'popup') { /** modal 컬럼 */
                    if(cols.field === 'attributeCategoryName') { //관리항목
                        cols.cellRenderer = `schBtn`;
                        cols.cellRendererParams = {
                            funcNm : `attributeCategoryPop`
                        };
                    }
                }

                /** 금액 관련 콤마 숫자 포맷 형변환 */
                if(['supplyAmount', 'vatAmount', 'usedAmt', 'afterTaxAmount'].includes(cols.field)) {
                    cols.valueFormatter = (params) => {
                        return new Intl.NumberFormat().format(params.value.toString().replace(/(\D)(?=(\D{3})+(?:\.\D+)?$)/g, "") || 0);
                    }
                }
                //타이틀 센터 정렬
                cols.headerClass = 'ag-center-header';
                cols.editable = false;
                return cols;
            };
            /**
             * * 그리드 랜더링 시작 ... 그리고 헤더 버튼 셋업
             */
             return new Promise((resolve, reject) => {
                 colDef.then((data) => {
                    console.log(data);

                     const { columns1, columns2, columns3, columns4, columns5, gridOptions, defaultColDef } = data.filter(f => f.type == this.trxTypeCd)[0];
     
                     //2. 그리드 옵션과 기본 컬럼 정의 값 병합
                     Object.assign(this.gridOptions, gridOptions);
                     Object.assign(this.defaultColDef, defaultColDef);
                     //3. 컬럼에 대한 옵션과 이벤트 등의 확장 재정의

                    if(columns1){
                      this.columnDefs1 = columns1.map(x => {
                        delete extensionColDef(x).layout;
                        return extensionColDef(x);
                      });
                    }else{
                      this.$refs.grid1.$el.style.display = 'none';
                    }

                     if(columns2){
                       this.columnDefs2 = columns2.map(x => {
                         delete extensionColDef(x).layout;
                         return extensionColDef(x);
                       });
                     }else{
                       this.$refs.grid2.$el.style.display = 'none';
                     }

                     if(columns3){
                       this.columnDefs3 = columns3.map(x => {
                         delete extensionColDef(x).layout;
                         return extensionColDef(x);
                       });
                     }else{
                       this.$refs.grid3.$el.style.display = 'none';
                     }

                     if(columns4){
                       this.columnDefs4 = columns4.map(x => {
                         delete extensionColDef(x).layout;
                         return extensionColDef(x);
                       });
                     }else{
                       this.$refs.grid4.$el.style.display = 'none';
                     }

                     if(columns5){
                       this.columnDefs5 = columns5.map(x => {
                         delete extensionColDef(x).layout;
                         return extensionColDef(x);
                       });
                     }else{
                       this.$refs.grid5.$el.style.display = 'none';
                     }

                   resolve();
                 })
                .catch(err => {
                    reject(err);
                });
             });
        },
        /**
         * * 관리항목 팝업
         */
         attributeCategoryPop() {
            
            const rowNode = this.gridApi.getRowNode(this.rowId);

            if(!rowNode.data.acctCd) {
                this.$message({
                    type: `warning`,
                    message: `계정과목을 선택하여 주세요.`
                });
                return false;
            }

            this.$modal.open({
                component: SlipMngItemPop,
                parent: this,
                props: {
                    applicationShortCd : this.$parent.$parent.$refs.mid.value.ttypeInterfaceModule,
                    acctCd: rowNode.data.acctCd,
                    data: {
                        attribute1: rowNode.data?.attribute1,
                        attribute2: rowNode.data?.attribute2,
                        attribute3: rowNode.data?.attribute3,
                        attribute4: rowNode.data?.attribute4,
                        attribute5: rowNode.data?.attribute5,
                        attribute6: rowNode.data?.attribute6,
                        attribute1Code: rowNode.data?.attribute1Code,
                        attribute2Code: rowNode.data?.attribute2Code,
                        attribute3Code: rowNode.data?.attribute3Code,
                        attribute4Code: rowNode.data?.attribute4Code,
                        attribute5Code: rowNode.data?.attribute5Code,
                        attribute6Code: rowNode.data?.attribute6Code,
                    },
                    readonly: true
                },
                width: 1200,
                events: {
                    
                },
            });
        },
        setGridHeight(data) {
          const rowHeight = 42; // 각 행의 높이 (픽셀 단위)
          const headerHeight = 50; // 헤더의 높이 (픽셀 단위)

          const totalHeight = data.length * rowHeight + headerHeight + 5;
          this.gridHeight = `${totalHeight}px`;

          if (this.gridApi) {
            this.gridApi.resetRowHeights();
          }
        },
    },
    async created() {
        const [taxes, distance ,area, oil, tps, traffic, trip, withholding, frgnCurCd] = await Promise.all([
            this.$http.get('/api/code/combo', { params: { groupCd: 'TAX_FLAG' } }),
            this.$http.get('/api/code/combo', { params: { groupCd: 'DISTANCE_GB_CD' } }),
            this.$http.get('/api/code/combo', { params: { groupCd: 'BUSINESS_TRIP_AREA_CD' } }),
            this.$http.get('/api/code/combo', { params: { groupCd: 'OIL_KIND_CD' } }),
            this.$http.get('/api/code/combo', { params: { groupCd: 'TPS_TYPE_CD' } }),
            this.$http.get('/api/code/combo', { params: { groupCd: 'TRAFFIC_TYPE_CD' } }),
            this.$http.get('/api/code/combo', { params: { groupCd: 'TRIP_AMT_TYPE_CD' } }),
            this.$http.get('/api/code/combo', { params: { groupCd: 'WITHHOLDING_TAX_CODE' } }),
            this.$http.get(`/api/exchange/currency/list`)
        ]);
        //공제여부
        this.options['TAX_FLAG'] = taxes.data;
        //출장거리 리스트
        this.options['AREA_LIST'] = area.data;
        this.options['AREA_LIST'].unshift({key: '00', value: '직접입력'});
        //시내/시외
        this.options['DISTANCE_GB'] = distance.data;
        this.options['DISTANCE_GB'].unshift({key: '', value: '선택'});
        //유종
        this.options['OIL_KIND'] = oil.data;
        this.options['OIL_KIND'].unshift({key: '', value: '선택'});
        //교통수단
        this.options['TPS_TYPE'] = tps.data;
        this.options['TPS_TYPE'].unshift({key: '', value: '선택'});
        //기타교통비1,2,3
        this.options['TRAFFIC_TYPE'] = traffic.data;
        this.options['TRAFFIC_TYPE'].unshift({key: '', value: '선택'});
        //국내/해외 출장 용도
        this.options['TRIP_AMT_TYPE'] = trip.data;
        //원천세코드
        this.options['WITHHOLDING_TAX_CODE'] = withholding.data;
        this.options['WITHHOLDING_TAX_CODE'].unshift({key: '', value: '선택'});
        
        this.options['FRGN_CUR_CD'] = frgnCurCd.data.map(x => {
                x.gubun = x.gubun || 99; //우선순위 Null 값에 대한 후순위로 변경
                return x;
            }).map(x => x).sort((a, b) => {
                return a.gubun - b.gubun; 
            });
        
        if(this.slipNo) {
            this.$http.get(`/api/slip/header/${this.slipNo}`)
            .then(header => header.data)
            .then(data => {
                const {slipTypeCd, lineAttribute1, curCd} = data;
                
                const AMT_LENGTH = this.options['FRGN_CUR_CD'].filter(f => f.currencyCode === curCd )[0]?.precision || 0;
                this.trxTypeCd = slipTypeCd;

                let methodPath = `/api/slip/line`;
                if(slipTypeCd === 'TRAFFIC') {
                    methodPath = `/api/slip/trafficLine`;
                }
                this.$http.get(`${methodPath}/${this.slipNo}`)
                .then(res => res.data)
                .then(data => this.setGridHeight(data))
                .then(data => {
                    this.createColumnDef()
                    .then(() => {
                        this.rowData = data.map(x => {
                            x.taxFlag = this.options['TAX_FLAG']?.filter(f => f.key === x.taxFlag)?.[0]?.value || '';
                            x.fromArea = this.options['AREA_LIST']?.filter(f => f.key === x.fromArea)?.[0]?.value || '';
                            x.oilBungae = this.options['OIL_KIND']?.filter(f => f.key === x.oilBungae)?.[0]?.value || '';
                            x.trafficType = this.options['TPS_TYPE']?.filter(f => f.key === x.trafficType)?.[0]?.value || '';
                            x.etcType1 = this.options['TRAFFIC_TYPE']?.filter(f => f.key === x.etcType1)?.[0]?.value || '';
                            x.etcType2 = this.options['TRAFFIC_TYPE']?.filter(f => f.key === x.etcType2)?.[0]?.value || '';
                            x.etcType3 = this.options['TRAFFIC_TYPE']?.filter(f => f.key === x.etcType3)?.[0]?.value || '';
                            x.distanceGubun = this.options['DISTANCE_GB']?.filter(f => f.key === x.distanceGubun)?.[0]?.value || '';
                            x.amtType = this.options['TRIP_AMT_TYPE']?.filter(f => f.key === x.attribute9)?.[0]?.value || '';
                            x.withholdingTaxCode = this.options['WITHHOLDING_TAX_CODE']?.filter(f => f.key === x.withholdingTaxCode)?.[0]?.value || '';
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
                          
                            return x;
                        });

                        if(lineAttribute1 === 'V') {
                            let lineSupAmt = 0;
                            let lineVatAmt = 0;
                            let lineViewSupAmt = 0;
                            let lineViewVatAmt = 0;
                            if(slipTypeCd === 'ACARD') {
                                lineSupAmt = this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {
                                    return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
                                }, 0) ;
                                lineVatAmt = this.rowData.map(x => this.$numeral(x.vatAmount).value()).reduce((prev, next) => {
                                    return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
                                }, 0) ;
                                lineViewSupAmt = lineSupAmt;
                                lineViewVatAmt = lineVatAmt;
                            } else {
                                
                                lineViewSupAmt = this.rowData.filter(f => f.taxId).map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {
                                    return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
                                }, 0) ;
                                lineViewVatAmt = this.rowData.filter(f => f.taxId).map(x => this.$numeral(x.vatAmount).value()).reduce((prev, next) => {
                                    return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
                                }, 0) ;
                            }
                            //부가세 서포트 컴포넌트에 공급가액/세액/합계 등을 전달하기 위한 이벤트 강제 호출
                            this.$nextTick(() => {
                                this.$emit('setViewTaxAmount', {vSupplyAmount: this.$numeral(lineViewSupAmt || 0).value(), vTaxAmount: this.$numeral(lineViewVatAmt || 0).value(), vTotalAmount: this.$numeral(lineViewSupAmt || 0).value() + this.$numeral(lineViewVatAmt || 0).value()})
                                this.$forceUpdate();
                            });
                            
                        } else {
                            const lineSupAmt = this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {
                                return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
                            }, 0) ;
                            const lineVatAmt = this.rowData.map(x => this.$numeral(x.vatAmount).value()).reduce((prev, next) => {
                                return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
                            }, 0) ;
                            //부가세 서포트 컴포넌트에 공급가액/세액/합계 등을 전달하기 위한 이벤트 강제 호출
                            this.$nextTick(() => {
                                this.$emit('setViewTaxAmount', {vSupplyAmount: this.$numeral(lineSupAmt || 0).value(), vTaxAmount: this.$numeral(lineVatAmt || 0).value(), vTotalAmount: this.$numeral(lineSupAmt || 0).value() + this.$numeral(lineVatAmt || 0).value()})
                                this.$forceUpdate();
                            })
                        }
                        
                    });
                });
            });
            
        } else {
            this.trxTypeCd = this.slipTypeCd;

            this.createColumnDef()
            .then(() => {
                this.rowData = this.gridData.map(x => {
                    x.taxFlag = this.options['TAX_FLAG']?.filter(f => f.key === x.taxFlag)?.[0]?.value || '';
                    x.fromArea = this.options['AREA_LIST']?.filter(f => f.key === x.fromArea)?.[0]?.value || '';
                    x.oilBungae = this.options['OIL_KIND']?.filter(f => f.key === x.oilBungae)?.[0]?.value || '';
                    x.trafficType = this.options['TPS_TYPE']?.filter(f => f.key === x.trafficType)?.[0]?.value || '';
                    x.etcType1 = this.options['TRAFFIC_TYPE']?.filter(f => f.key === x.etcType1)?.[0]?.value || '';
                    x.etcType2 = this.options['TRAFFIC_TYPE']?.filter(f => f.key === x.etcType2)?.[0]?.value || '';
                    x.etcType3 = this.options['TRAFFIC_TYPE']?.filter(f => f.key === x.etcType3)?.[0]?.value || '';
                    x.distanceGubun = this.options['DISTANCE_GB']?.filter(f => f.key === x.distanceGubun)?.[0]?.value || '';
                    x.amtType = this.options['TRIP_AMT_TYPE']?.filter(f => f.key === x.amtType)?.[0]?.value || '';
                    x.withholdingTaxCode = this.options['WITHHOLDING_TAX_CODE']?.filter(f => f.key === x.withholdingTaxCode)?.[0]?.value || '';
                    return x;
                });

                this.setGridHeight(this.rowData);
            });
        }
    },
    mounted() {

    }
}
</script>