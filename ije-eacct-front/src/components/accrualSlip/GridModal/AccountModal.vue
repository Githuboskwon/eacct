<template>
    <layout style="width: 1000px;">
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
                      <span class="NotoM" style="margin-right: 10px;">- 계정코드</span>
                      <el-input type="text" ref="search" v-model="search.code" @keypress.native.enter="goSearch" clearable style="width: 60%;">
                        <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                      </el-input>
                    </div>
                  </div>
                  <div class="control is-expanded search-area">
                    <div class="rs-mt2">
                      <span class="NotoM" style="margin-right: 10px;">- 계정명</span>
                      <el-input type="text" v-model="search.name" @keypress.native.enter="goSearch" clearable style="width: 70%;">
                        <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                      </el-input>
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
                    :defaultColDef="defaultColDef"
                    @rowDoubleClicked="rowDoubleClick"
                    @grid-ready="onGridReady"/>
            </div>
        </div>
    </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'
import {AgGridVue} from 'ag-grid-vue';

export default {
  compatConfig: { MODE: 2 },
    props: {
        trxTypeCd: {    //거래유형
            Type: String,
            required: true,
        },
        acctModule: {   //거래유형에서의 매출, 매입 유형
            Type: String,
            required: true,
        },
        deptType: {     //귀속부서유형 (제조: M, 판관: S, 공통: 그 외(C?) (선택)
            Type: String,
            required: false
        },
        schTxt: {
            Type: String,
            required: false
        },
        attribute1: {
            Type: String,
            required: false,
        },
        attribute3: {
            Type: String,
            required: true,
            default: 'form'
        }
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '계정과목 조회',
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
                code: '',
                name: ''
            },
        }
    },
    created() {
        this.search.name = this.schTxt || '';
        // if(this.search.name) {
        // }
        this.goSearch();
        this.makeColDef();
    },
    mounted() {
        this.$refs.search.$el.getElementsByTagName('input')[0].focus();
    },
    methods: {
        goSearch() {
            this.$store.commit('loading');
            
            const trxTypeCd = this.trxTypeCd;
            const acctModule = this.acctModule;
            const deptType = this.deptType || '';
            const acctCd = this.search.code;
            const acctNm = this.search.name;
            const attribute1 = this.attribute1;
            const attribute3 = this.attribute3;
            const params = {
                trxTypeCd,
                acctModule,
                deptType,
                acctCd,
                acctNm,
                attribute1,
                attribute3
            }
            this.$http.post(`/api/account/slip/list`, params)
            .then(res => res.data)
            .then(data => {
                this.rowData = data;
            })
            .finally(() => {
                this.$store.commit('finish');
            });
        },
        /**
         * 그리드 컬럼 width 자동 맞춤
         * @param {*} columnApi 
         */
         autoSizeAll(columnApi) {
            const allColumnIds = [];
            columnApi.columnController.gridColumns.forEach((column) => {
                allColumnIds.push(column.getId());
            });
            columnApi.columnController.autoSizeColumns(allColumnIds, false);
        },
        onGridReady(params){
            this.gridApi = params.api;
            this.columnApi = params.columnApi;
            // this.gridApi.sizeColumnsToFit();
            // this.autoSizeAll(this.gridApi);
        },
        makeColDef(){
            const self = this;
            this.columnDefs = [
                {
                    headerName:'계정과목',
                    field:'acctNm',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'계정코드',
                    field:'acctCd',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'표준적요',
                    field:'acctAttribute1',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'계정유형',
                    field:'interfaceSlipType',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'DR_CR',
                    field:'drCr',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'차대변 구분',
                    field:'drcrType',
                    cellStyle:{textAlign:'left'},
                }
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>