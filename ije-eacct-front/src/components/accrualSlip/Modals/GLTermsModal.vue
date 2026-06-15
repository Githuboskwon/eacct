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
                            <span class="NotoM" style="margin-right: 10px;">- 결제조건코드</span>
                            <el-input type="text" ref="search" v-model="search.code" @keypress.native.enter="goSearch" clearable style="width: 60%;">
                                <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                            </el-input>
                        </div>
                    </div>
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 결제조건</span>
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

import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
    props: {
        schTxt: {
            Type: String,
            required: false
        },
        trxTypeCd: {
            Type: String,
            required: true
        },
        vendorCd: {
            Type: String,
            required: false
        },
        deptCd: {
            Type: String,
            required: false
        },
        curCd: {
            Type: String,
            required: true
        },
        searchYn: {
          Type: String,
          required: false
        },
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '결제조건 조회',
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
        if(this.search.name) {
            this.goSearch();
        }
        this.makeColDef();
    },
    mounted() {
        this.$refs.search.$el.getElementsByTagName('input')[0].focus();
    },
    methods: {
        goSearch() {
            this.$store.commit('loading');
            // /${this.search.code}/${this.search.name}
            const searchCd = this.search.code;
            const searchNm = this.search.name;
            const trxTypeCd = this.trxTypeCd;
            const vendorCd = this.vendorCd || '';
            const deptCd = this.deptCd || '';
            const currencyCd = this.curCd || 'KRW';
            const searchYn = this.searchYn || '';
            const params = {
                searchCd,
                searchNm,
                trxTypeCd,
                vendorCd,
                deptCd,
                currencyCd,
                searchYn
            }
            this.$http.post(`/api/gl/term/list`, params)
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
        },
        makeColDef(){
            const self = this;
            this.columnDefs = [
                {
                    headerName:'결제조건',
                    field:'name',
                    cellStyle:{textAlign:'left'},
                    width : 250,
                },
                {
                    headerName:'결제방법',
                    field:'description',
                    cellStyle:{textAlign:'left'},
                    width : 250,
                },
                {
                    headerName:'지급방법',
                    field:'paymentMethod',
                    cellStyle:{textAlign:'center'},
                    width : 120,
                },
                {
                    headerName:'어음여부',
                    field:'notesFlag',
                    cellStyle:{textAlign:'center'},
                    width : 100,
                },
                {
                    headerName:'ID',
                    field:'termId',
                    cellStyle:{textAlign:'center'},
                    width : 100,
                },
                {
                    headerName:'계좌정보필수',
                    field:'vendorAcctCheck',
                    cellStyle:{textAlign:'center'},
                    width : 100,
                }
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>