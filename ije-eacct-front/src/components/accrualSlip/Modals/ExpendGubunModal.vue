<template>
    <layout style="width: 1000px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="btn-type1">
                <el-button @click="goSearch" icon="el-icon-search">
                    조회
                </el-button>
            </div>
            
            <div class="grid-wrap">
                <ag-grid-vue 
                    style="width: 100%; height: 600px;"
                    class="ag-theme-alpine"    
                    rowSelection="single"
                    :columnDefs="columnDefs"     
                    :gridOptions="gridOptions"
                    :rowData="rowData"
                    :suppressMenuHide="true"
                    :defaultColDef="defaultColDef"
                    @rowDoubleClicked="rowDoubleClick"
                    :suppressRowTransform="true"
                    @grid-ready="onGridReady"/>
            </div>
        </div>
    </layout>
</template>

<script>

import _ from 'lodash';
import Layout from '@/components/ModalSlot.vue'

import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
    props: {
        fameveDate: {
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
            title: '경조구분 조회',
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
            search: '',
            options: [
                {
                    remark2: '', //경조구분
                    remark3: '', //경조대상
                    remark5: '', //key
                    wreathYn: 'Y', //경조화환
                    mutualYn: 'Y', //상조용품
                    expendAmt: 0, //회사지급액
                }
            ]
        }
    },
    created() {
        this.goSearch();
        this.makeColDef();
    },
    methods: {
        goSearch() {
            this.$store.commit('loading');
            const searchDate = this.fameveDate;
            const params = {
                searchDate
            };
            this.$http.post(`/api/expend/popup/list`, params)
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
                    headerName:'경조구분',
                    field:'remark2',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'경조대상',
                    field:'remark3',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'회사지급액',
                    field:'expendAmt',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format('0,0');
                    }
                },
                {
                    headerName:'경조화환',
                    field:'wreathYn',
                    cellStyle:{textAlign:'center'},
                    valueFormatter: (params) => {
                        return params.value === 'Y' ? '유': '무'
                    }
                },
                {
                    headerName:'상조용품',
                    field:'mutualYn',
                    cellStyle:{textAlign:'center'},
                    valueFormatter: (params) => {
                        return params.value === 'Y' ? '유': '무'
                    }
                }
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>