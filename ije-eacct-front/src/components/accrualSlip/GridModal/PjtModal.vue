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
                            <span class="NotoM" style="margin-right: 10px;">- 프로젝트코드</span>
                            <el-input type="text" ref="search" v-model="search.code" @keypress.native.enter="goSearch" clearable style="width: 60%;">
                                <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                            </el-input>
                        </div>
                    </div>
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 프로젝트명</span>
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
    props: {
        personId: {
            Type: Number,
            required: true,
        },
        postingDate: {
            Type: String,
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
            title: '프로젝트 조회',
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
            
            const projectCd = this.search.code;
            const projectNm = this.search.name;
            const personId = this.personId;
            const postingDate = this.postingDate;
            const params = {
                personId,
                postingDate,
                projectCd,
                projectNm
            }
            this.$http.post(`/api/project/slip/list`, params)
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
                    headerName:'부서유형',
                    field:'projectCd',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'부서명',
                    field:'projectNm',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'프로젝트 ID',
                    field:'projectId',
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