<template>
    <layout style="width: 1500px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="btn-type1">
                <el-button @click="goSearch" icon="el-icon-search">
                    조회
                </el-button>
            </div>
            <div class="pop-content-search">
                <div class="field has-addons">
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 기간</span>
                            <el-date-picker
                                v-model="search.periodName"
                                type="month"
                                format="YYYY-MM"
                                value-format="YYYYMM"
                                style="width: 60%;">
                            </el-date-picker>
                        </div>
                    </div>
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 상신부서</span>
                            <el-input class="mr3" type="text" v-model="search.budgetDeptCode" style="width: 30%" disabled ></el-input>
                            <div class="ip-box ip-box-w02" style="width:48%;">
                                <el-input type="text" v-model="search.budgetDeptName" @keypress.native.enter="cctr2OpenModal" clearable>
                                    <i class="el-icon-search el-input__icon" slot="suffix" @click="cctr2OpenModal"></i>
                                </el-input>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid-wrap">
                <ag-grid-vue 
                    style="width: 100%; height: 400px;"
                    class="ag-theme-alpine"    
                    rowSelection="single"
                    :columnDefs="columnDefs"     
                    :gridOptions="gridOptions"
                    :rowData="rowData"
                    :suppressMenuHide="true"
                    :defaultColDef="defaultColDef"
                    @rowDoubleClicked="rowDoubleClick"
                    @grid-ready="onGridReady"/>
            </div>
        </div>
    </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'

import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

import Cctr2Modal from '@/components/accrualSlip/GridModal/Cctr2Modal'; /** 예산부서 조회 */

export default {
    props: {
        dept: {
            Type: Object,
            required: true
        },
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '예산 조회',
            rowData: [],
            gridOptions: {},
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
                personId: '',
                periodName: '',
                budgetDeptCode: '',
                budgetDeptName: ''
            },
        }
    },
    created() {
        
        this.search.periodName = this.$moment(this.dept.postingDt).format('YYYYMM');
        this.search.budgetDeptCode = this.dept.deptCd || '';
        this.search.budgetDeptName = this.dept.deptNm || '';
        this.search.personId = this.dept.personId;

        if(this.search.periodName && this.search.budgetDeptCode) {
            this.goSearch();
        }
        this.makeColDef();
    },
    methods: {
        goSearch() {
            this.$store.commit('loading');
            
            const {budgetDeptCode, periodName} = this.search
            if(!periodName) {
                this.$message({ type: 'danger', message: '기간을 선택해주세요.' });
                return false;
            }
            if(!budgetDeptCode) {
                this.$message({ type: 'danger', message: '상신부서를 선택해주세요.' });
                return false;
            }

            this.$http.post(`/api/budget/amount/list/${periodName}/${budgetDeptCode}`)
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
            // this.autoSizeAll(this.columnApi);
        },
        makeColDef(){
            const self = this;
            
            this.columnDefs = [
                {
                    headerName:'회계기간',
                    field:'periodName',
                    cellStyle:{textAlign:'center'},
                    width : 100,
                },
                {
                    headerName:'예산부서코드',
                    field:'budgetDeptCode',
                    cellStyle:{textAlign:'center'},
                    width : 120,
                },
                {
                    headerName:'예산부서',
                    field:'budgetDeptDesc',
                    cellStyle:{textAlign:'left'},
                    width : 160,
                },
                {
                    headerName:'통제집계코드',
                    field:'budgetAcctCode',
                    cellStyle:{textAlign:'center'},
                    width : 130,
                },
                {
                    headerName:'통제집계계정',
                    field:'budgetAcctDesc',
                    cellStyle:{textAlign:'left'},
                    width : 140,
                },
                {
                    headerName:'예산금액',
                    field:'budgetAmount',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format(`#,###`);
                    },
                    width : 150,
                },
                {
                    headerName:'전자전표 상신금액',
                    field:'extEncumbranceAmount',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format(`#,###`);
                    },
                    width : 150,
                },
                {
                    headerName:'가집행금액',
                    field:'encumbranceAmount',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format(`#,###`);
                    },
                    width : 150,
                },
                {
                    headerName:'집행금액',
                    field:'actualAmount',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format(`#,###`);
                    },
                    width : 150,
                },
                {
                    headerName:'가용금액',
                    field:'availableAmount',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format(`#,###`);
                    },
                    width : 150,
                }
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
        /**
         * TODO : 예산부서 조회 ? 귀속부서 조회 ? 무엇으로 해야하나.?
         * * 우선 예산부서로 조회해놓음. (예시로 그리드 컴포넌트에 정의되어져 있는 예산부서 가져다씀.)
         * * '@/components/accrualSlip/GridModal/Cctr2Modal';
         */
        cctr2OpenModal() {
            const self = this;
            const {personId, periodName, budgetDeptName } = this.search;
            this.$modal.open({
                component: Cctr2Modal,
                parent: this,
                props: {
                    personId : personId,
                    postingDate: periodName,
                    schTxt: budgetDeptName
                },
                width: 700,
                events: {
                    close(object) {
                        self.search.budgetDeptCode = object.deptCd;
                        self.search.budgetDeptName = object.deptNm;
                    }
                },
            });
        },
        /**
         * * 그리드 컬럼 width 자동 맞춤
         * @param {*} columnApi 
        */
        autoSizeAll(columnApi) {
            const allColumnIds = [];
            columnApi.columnController.gridColumns.forEach((column) => {
                allColumnIds.push(column.getId());
            });
            columnApi.columnController.autoSizeColumns(allColumnIds, false);
        },

        initBudgetDept() {
            return new Promise((resolve) => {
                this.search.budgetDeptCode = '';
                this.search.budgetDeptName = '';
                resolve();
            })
        }
    },
    watch: {
        /** 예산부서 변경 시 초기화 진행 */
        'search.budgetDeptName': {
            immediate: true,
            deep: true,
            handler(nVal, oVal) {
                if(this.search.budgetDeptCode && oVal.length > nVal.length) {
                    this.$confirm('부서초기화를 하시겠습니까?', 'warning',
                    {
                        distinguishCancelAndClose: true,
                        confirmButtonText: '예',
                        cancelButtonText: '아니오',
                        type: 'warning'
                    })
                    .then((val) => {
                        if(val) {
                            this.initBudgetDept()
                            .then(_ => {
                                this.$message({ type: '초기화', message: '초기화 되었습니다.' });
                            });
                        }
                    })
                    .catch(_ => {
                        this.value.budgetDeptName = oVal;
                        this.$message({ type: '취소', message: '취소되었습니다.' });
                    });
                }
            }
        }
    }
}
</script>