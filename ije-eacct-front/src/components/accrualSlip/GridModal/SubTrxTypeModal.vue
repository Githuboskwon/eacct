<template>
    <layout style="width: 800px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="btn-type1">
                <el-button @click="goSearch" icon="el-icon-search">
                    조회
                </el-button>
            </div>
          <!-- 명칭 오류, 검색 필요없어보여서 제거, Back단에 검색 조건 없음 -->
<!--            <div class="pop-content-search">
                <div class="field has-addons">
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 세금코드</span>
                            <el-input type="text" ref="search" v-model="search.code" @keypress.native.enter="goSearch" clearable style="width: 60%;">
                            </el-input>
                        </div>
                    </div>
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 세금코드명</span>
                            <el-input type="text" v-model="search.name" @keypress.native.enter="goSearch" clearable style="width: 70%;">
                            </el-input>
                        </div>
                    </div>
                </div>
            </div>-->
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
    props: {
        trxTypeCd: {
            Type: String,
            required: true
        },
        attribute1: {
            Type: String,
            required: true
        },
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '보조거래유형 조회',
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
            const attribute1 = this.attribute1;
            
            const params = {
                trxTypeCd,
                attribute1
            }
            this.$http.post(`/api/account/slip/subList`, params)
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
                    headerName:'법인코드',
                    field:'compCd',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'거래유형코드',
                    field:'trxTypeCd',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'(부)거래유형코드',
                    field:'childTrxTypeCd',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'(부)거래유형명',
                    field:'childTrxTypeNm',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'구분',
                    field:'attribute1',
                    cellStyle:{textAlign:'center'},
                }
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>