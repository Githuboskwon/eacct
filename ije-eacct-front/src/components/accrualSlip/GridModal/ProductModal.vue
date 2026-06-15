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
                            <span class="NotoM" style="margin-right: 10px;">- 제품군 코드</span>
                            <el-input type="text" ref="search" v-model="search.code" @keypress.native.enter="goSearch" clearable style="width: 60%;">
                                <i class="el-icon-search el-input__icon" slot="suffix" @click="goSearch"></i>
                            </el-input>
                        </div>
                    </div>
                    <div class="control is-expanded search-area">
                        <div class="rs-mt2">
                            <span class="NotoM" style="margin-right: 10px;">- 제품군</span>
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
        taskItemGroup: {
            Type: String,
            required: false,
            default: ''
        },
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
            title: '제품군 조회',
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
        this.search.code = this.taskItemGroup || '';
        this.search.name = this.schTxt || '';
        
        this.goSearch();
        this.makeColDef();
    },
    mounted() {
        this.$refs.search.$el.getElementsByTagName('input')[0].focus();
    },
    methods: {
        goSearch() {
            this.$store.commit('loading');
            
            const itemGroupCd = this.search.code;
            const itemGroupNm = this.search.name;
            const personId = this.personId;
            const postingDate = this.postingDate;
            const params = {
                personId,
                postingDate,
                itemGroupCd,
                itemGroupNm
            }
            this.$http.post(`/api/item/slip/list`, params)
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
                    headerName:'제품군',
                    field:'itemGroupNm',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'제품군 코드',
                    field:'itemGroupCd',
                    cellStyle:{textAlign:'left'},
                },     
            ];
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>