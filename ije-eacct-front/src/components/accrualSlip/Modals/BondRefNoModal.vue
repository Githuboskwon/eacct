<template>
    <layout style="width: 800px;">
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
                            <span class="NotoM" style="margin-right: 10px;">- REF NO.</span>
                            <el-input type="text" id="input" v-model="refNo" @keypress.native.enter="goSearch" clearable style="width: 60%;">
                                <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                            </el-input>
                        </div>
                    </div>
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 고객명</span>
                            <el-input type="text" v-model="search" @keypress.native.enter="goSearch" clearable style="width: 70%;">
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
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: 'REF NO 조회',
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
            refNo: '',
            search: '',
        }
    },
    created() {
        this.makeColDef();
    },
    mounted() {
        this.$refs.search.$el.getElementsByTagName('input')[0].focus();
        this.goSearch();
    },
    methods: {
        goSearch() {
            const params = {
                refNo: this.refNo,
                customerNm: this.search
            }
            this.$store.commit('loading');
            this.$http.post(`/api/bondMst/refNo/list`, params)
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
                    headerName:'REF_NO',
                    field:'refNo',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'고객명',
                    field:'customerNm',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'개설일',
                    field:'openingDt',
                    cellStyle:{textAlign:'center'},
                    valueFormatter: (params) => {
                        return self.$moment(params.value).format('YYYYMMDD');
                    }
                },
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>