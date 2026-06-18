<template>
    <layout style="width: 700px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="btn-type1 ">
                <el-button class="btn-size btn-gray" @click="goSearch" icon="el-icon-search">
                    조회
                </el-button>
            </div>
            <div class="pop-content-search">
                <div class="field has-addons">
                    <div class="mr20">
                      <span class="NotoM" style="margin-right: 10px;">- 결제조건</span>
                    </div>
                    <div class="control is-expanded search-area">
                      <el-input type="text" v-model="search.name" @keypress.native.enter="goSearch" clearable>
                        <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                      </el-input>
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

          this.$http.post(`/api/vendor/getTerms`, {
            compCd: this.$store.state.loginInfo.compCd,
            searchNm: this.search.name,
            searchCd: this.search.code
          })
            .then(response => {
              this.rowData = response.data
            })
            .catch(response => {
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
                },
                {
                    headerName:'결제방법',
                    field:'description',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'지급방법',
                    field:'paymentMethod',
                    cellStyle:{textAlign:'center'},
                    width: 100,
                },
                {
                    headerName:'어음여부',
                    field:'notesFlag',
                    cellStyle:{textAlign:'center'},
                    width: 100,
                },
                {
                    headerName:'ID',
                    field:'termId',
                    cellStyle:{textAlign:'center'},
                    width: 100,
                },
                {
                    headerName:'계좌정보필수',
                    field:'vendorAcctCheck',
                    cellStyle:{textAlign:'left'},
                    hide: true
                }
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>