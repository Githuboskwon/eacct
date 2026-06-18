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
                            <span class="NotoM" style="margin-right: 10px;">- 은행명</span>
                            <el-input type="text" ref="search" v-model="search" @keypress.native.enter="goSearch" clearable style="width: 70%;">
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

export default {
  compatConfig: { MODE: 2 },
    props: {
        integrationVendorNum: {
            Type: String,
            required: false
        },
        schTxt: {
            Type: String,
            required: false
        },
        noteAccountFlag: {
            Type: String,
            required: false,
            default: 'N'
        },
        curCd: {
            Type: String,
            required: true,
            default: 'KRW'
        }
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '계좌정보 조회',
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
            search: ''
        }
    },
    created() {
        this.search = this.schTxt || '';
        if(this.search) {
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
            const searchNm = this.search;
            const integrationVendorNum = this.integrationVendorNum || '';
            const noteAccountFlag = this.noteAccountFlag
            const currencyCode = this.curCd;
            const params = {
                integrationVendorNum,
                searchNm,
                noteAccountFlag,
                currencyCode
            }
            this.$http.post(`/api/vendor/bankList`, params)
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
            // {"field": "chk", "headerName": "", "type": "", "suppressMenu": true, "editable": false, "layout" : "checkbox", "hide": false, "cellStyle": {"textAlign": "center"}},
            this.columnDefs = [
                {
                    headerName:'은행명',
                    field:'bankName',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'계좌번호',
                    field:'bankAccountNumber',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'예금주',
                    field:'bankAccountName',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'주계좌여부',
                    field:'primaryFlag',
                    cellStyle:{textAlign:'right'},
                },
                {
                    headerName:'약정내역',
                    field:'venTermsName',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'계좌정보필수',
                    field:'vendorAcctCheck',
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