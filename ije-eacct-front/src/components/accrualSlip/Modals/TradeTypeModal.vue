<template>
    <layout style="width: 1400px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="btn-type1">
                <el-button @click="goSearch" icon="el-icon-search">
                    조회
                </el-button>
            </div>
            <div class="pop-content-search">
                <div class="field has-addons ">
                  <div class="control is-expanded search-area">
                    <div class="rs-mt2">
                      <span class="NotoM" style="margin-right: 10px;">- 거래유형코드</span>
                      <el-input type="text" ref="search" v-model="search.code" @keypress.native.enter="goSearch" clearable style="width: 60%;">
                        <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                      </el-input>
                    </div>
                  </div>
                  <div class="control is-expanded search-area">
                    <div class="rs-mt2">
                      <span class="NotoM" style="margin-right: 10px;">- 거래유형명</span>
                      <el-input type="text" v-model="search.name" @keypress.native.enter="goSearch" clearable style="width: 70%;">
                        <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                      </el-input>
                    </div>
                  </div>
                    <!-- <div class="mr20 ">
                        <span class="pop-c-name">- 거래유형 코드/명</span>
                    </div>
                    <div class="control is-expanded">
                        <el-input type="text" ref="search" v-model="search" @keypress.enter="goSearch" autofocus @keypress.native.enter="goSearch"></el-input>
                    </div> -->
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
        personId: {
            Type: Number,
            required: true,
        },
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
                resizable: true, 
                filter:true, 
                sortable: true,
                // flex: 2
            },
            gridApi : null ,
            columnApi : null ,
            search: {
                code: '',
                name: '',
            },
        }
    },
    created() {
        this.search.name = this.schTxt || '';

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
            const params = {
                compCd: this.$store.state.loginInfo.compCd,
                trxSpTypeCd: 10, //전표유형 ( 발생전표 : 10, 정산전표: 20, 원장전표 : 30 )
                personId: this.personId,
                trxTypeCd: this.search.code,
                trxTypeNm: this.search.name
            }
            this.$store.commit('loading');
            this.$http.post(`/api/trx/slip/list`, params)
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
                    headerName:'거래유형명',
                    field:'trxTypeNm',
                    cellStyle:{textAlign:'left'},
                    width: 250,
                },
                {
                    headerName:'거래유형코드',
                    field:'trxTypeCd',
                    cellStyle:{textAlign:'left'},
                    width: 120,
                },
                {
                    headerName:'계정코드',
                    field:'acctCd',
                    hide: true
                },
                {
                    headerName:'계정명',
                    field:'acctNm',
                    cellStyle:{textAlign:'left'},
                    width: 120,
                },
                {
                    headerName:'적요',
                    field:'trxTypeDescription',
                    cellStyle:{textAlign:'left'},
                    width: 230,
                },
                {
                    headerName:'세무증빙유형',
                    field:'evidenceName',
                    cellStyle:{textAlign:'center'},
                    width: 160,
                },
                {
                    headerName:'세무증빙유형 코드',
                    field:'evidenceCode',
                    cellStyle:{textAlign:'center'},
                    width: 120,
                },
                {
                    headerName:'헤더계정수',
                    field:'acctCnt',
                    cellStyle:{textAlign:'center'},
                    width: 110,
                },
                {
                    headerName:'차대변 구분',
                    field:'drCr',
                    cellStyle:{textAlign:'center'},
                    width: 110,
                },
                {
                    headerName:'부가세여부 코드',
                    field:'lineAttribute1',
                    cellStyle:{textAlign:'center'},
                    width: 110,
                },
                {
                    headerName:'부가세여부',
                    field:'lineAttribute1Name',
                    cellStyle:{textAlign:'center'},
                    width: 120,
                },
                {
                    headerName:'원천세 그룹ID',
                    field:'awtGroupId',
                    cellStyle:{textAlign:'center'},
                    width: 120,
                },
                {
                    headerName:'원천세 그룹명',
                    field:'awtGroupNm',
                    cellStyle:{textAlign:'center'},
                    width: 120,
                },
                {
                    headerName:'사업장코드',
                    field:'locationCode',
                    cellStyle:{textAlign:'center'},
                    width: 120,
                },
                {
                    headerName:'사업장명',
                    field:'locationName',
                    cellStyle:{textAlign:'center'},
                    width: 120,
                },
                {
                    headerName:'라인ATTRIBUTE2',
                    field:'lineAttribute2',
                    cellStyle:{textAlign:'center'},
                    width: 120,
                },
                {
                    headerName:'라인ATTRIBUTE3',
                    field:'lineAttribute3',
                    cellStyle:{textAlign:'center'},
                    width: 120,
                },
            ];
        },
        rowDoubleClick(params){
            if(['PO', 'IM'].includes(params.data.slipTypeCd)) {
                this.$alert(`작성 불가능한 거래유형입니다.`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'error',
                    center: true
                });
                return false;
            }
            
            this.$emit('close', params.data);
        },
    }
}
</script>