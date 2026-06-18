<template>
    <layout style="width: 700px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="btn-type1">
                <el-button class="btn-size btn-gray" @click="goSearch" icon="el-icon-search">
                    조회
                </el-button>
            </div>
            <div class="pop-content-search">
                <div class="field has-addons ">
                    <div class="mr20 ">
                        <span class="pop-c-name">- 거래유형 코드/명</span>
                    </div>
                    <div class="control is-expanded">
                        <input class="input" type="text" v-model="search" @keypress.enter="goSearch">
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
  compatConfig: { MODE: 2 },
    props: {
        schTxt: {
            Type: String,
            required: false
        }
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '거래유형 조회',
            rowData: [],
            gridOptions: {},
            columnDefs:[],
            defaultColDef: { 
                // resizable: true, 
                filter:true, 
                sortable: true,
                // flex: 2
            },
            gridApi : null ,
            columnApi : null ,
            search: '',
        }
    },
    created() {
        this.search = this.schTxt || '';
        if(this.search) {
            this.goSearch();
        }
        this.makeColDef();
    },
    methods: {
        goSearch() {
            const params = {
                compCd: this.$store.state.loginInfo.compCd,
                trxTypeNm: this.search,
            }
            this.$store.commit('loading');
            this.$http.post(`/api/trx/list`, params)
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

            //this.gridApi.sizeColumnsToFit();
        },
        makeColDef(){
            const self = this;
            this.columnDefs = [
                {
                    headerName:'거래유형명',
                    field:'trxTypeNm',
                    cellStyle:{textAlign:'left'},
                    width: 375,
                },
                {
                    headerName:'거래유형코드',
                    field:'trxTypeCd',
                    cellStyle:{textAlign:'left'},
                    width: 250,
                },
                {
                    headerName:'전표양식코드',
                    field:'slipTypeCd',
                    hide: true
                },
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>