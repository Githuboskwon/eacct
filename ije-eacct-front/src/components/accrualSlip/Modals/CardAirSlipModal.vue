<template>
    <layout style="width: 1000px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="btn-type1">
                <el-button type="primary" id="search" @click="goSearch" icon="el-icon-search">
                    조회
                </el-button>
                <el-button type="primary" id="search" @click="closeAddRow" icon="el-icon-plus">
                    추가
                </el-button>
            </div>
            <div class="pop-content-search">
                <div class="field has-addons">
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 회계일자</span>
                            <el-date-picker
                                v-model="search.postDt"
                                type="daterange"
                                align="right"
                                unlink-panels
                                range-separator="~"
                                start-placeholder="시작일"
                                end-placeholder="종료일">
                            </el-date-picker>
                        </div>
                    </div>
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 위임자선택</span>
                            <el-select v-model="search.empNo">
                                <el-option
                                    v-for="(emp, idx) in employes" :key="idx"
                                    :label="emp.empNm"
                                    :value="emp.empNo">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid-wrap">
                <ag-grid-vue 
                    style="width: 100%; height: 400px;"
                    class="ag-theme-alpine"    
                    rowSelection="multiple"
                    :columnDefs="columnDefs"     
                    :gridOptions="gridOptions"
                    :rowData="rowData"
                    :suppressMenuHide="true"
                    :enableRangeSelection="true"
                    :defaultColDef="defaultColDef"
                    @row-selected="onRowSelected"
                    @grid-ready="onGridReady"/>
            </div>
        </div>
    </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'

import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
    props: {
        empNo: {
            Type: Number,
            required: true
        },
        copyRowData: {
            Type: Array,
            required: false
        }
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '작성전표 조회',
            employes: [],
            rowData: [],
            gridOptions: {
                suppressRowClickSelection: true,
                getRowStyle: (params) => {
                    if(params.data.cStateYn){
                        return {background: 'orange'};
                    }
                }
            },
            columnDefs:[],
            defaultColDef: { 
                resizable: true, 
                filter:true, 
                sortable: true,
                // flex: 2
            },
            gridApi : null ,
            columnApi : null ,
            search: {
                postDt: [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss'), this.$moment().format('YYYY-MM-DD HH:mm:ss')],
                empNo: '',
            },
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
        }
    },
    created() {
        
        this.search.empNo = this.empNo;
        this.getDelegateEmployes().then(data => {
            this.employes = data
        })
        .finally(() =>{
            this.makeColDef();
            this.goSearch();
        });
    },
    mounted() {
        try {
            document.getElementById("search").focus();
        } catch (e) {
        }
    },
    methods: {
        goSearch() {
            
            this.$store.commit('loading');
            
            const searchNm = this.search;
            const params = {
                fromDate : this.search.postDt[0],
                toDate : this.search.postDt[1],
                empNo: this.search.empNo,
            }
            this.$http.post(`/api/slip/getAirline`, params)
            .then(res => res.data)
            .then(data => {
                this.rowData = data.map(x => {
                    this.copyRowData.map(c => {
                        if(x.slipNo === c.slipNo) {
                            x.cStateYn = true;   
                        }
                    });
                    return x;
                });
            })
            .finally(() => {
                this.$store.commit('finish');
            });
        },
        onGridReady(params){
            this.gridApi = params.api;
            this.columnApi = params.columnApi;
        },
        /**
         * * 그리드 체크 선택 이벤트
         * @param {*} params 
         */
        onRowSelected(params) {
            const rowIndex = params.rowIndex;
            
            this.rowData[rowIndex].chk = params.node.isSelected();
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
                    },
                    checkboxSelection: (params) => {
                        return !params.data.cStateYn
                    },
                },
                {
                    headerName:'회계일자',
                    field:'strPostingDate',
                    cellStyle:{textAlign:'center'},
                    valueFormatter: (params) => {
                        return self.$moment(params.value).format('YYYY-MM-DD');
                    }
                },
                {
                    headerName:'전표번호',
                    field:'slipNo',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'전표상태',
                    field:'slipStatus',
                    cellStyle:{textAlign:'center'},
                    valueFormatter: (params) => {
                        return this.slipStatusList.filter(f => f.key === params.value)?.[0]?.value || '';
                    }
                },
                {
                    headerName:'거래유형',
                    field:'slipTypeText',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급자',
                    field:'erpVendorName',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급자ID',
                    field:'integrationVendorNum',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'통화',
                    field:'usedCur',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'금액',
                    field:'usedAmt',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value || 0).format('#,#');
                    }
                },
                {
                    headerName:'적요',
                    field:'headerText',
                    cellStyle:{textAlign:'left'},
                }
            ];
        },
        /** 
         * * 추가하기
         */
        closeAddRow(){
            const data = this.gridApi.getSelectedNodes().map(x => x.data);
            if(!!data && data.length > 0) {
                this.$emit('close', data);
            } else {
                this.$message({ type: 'warning', message: '추가하실 전표를 선택해주세요.' });
            }
        },
        getDelegateEmployes() {
            return new Promise((resolve, reject) => {
                this.$http.get(`/api/emp/pop/delegate/list/`).then(res => res.data)
                .then(data => {
                    resolve(data)
                })
                .catch(err => reject(err));
            });
        }
    }
}
</script>