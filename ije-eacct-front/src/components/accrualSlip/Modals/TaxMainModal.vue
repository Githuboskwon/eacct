<template>
    <layout style="width: 1200px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="btn-type1">
                <el-button @click="goSearch" type="primary" icon="el-icon-search">
                    조회
                </el-button>
            </div>
            <el-form class="pop-content-search" label-position="left" label-width="120">
                <el-row class="field has-addons">
                    <el-col :span="8">
                        <el-form-item label="작성일자">
                            <el-date-picker
                                v-model="search.postDt"
                                type="daterange"
                                align="right"
                                unlink-panels
                                format="YYYYMMDD"
                                range-separator="~"
                                start-placeholder="시작일"
                                end-placeholder="종료일"
                                style="width: 70%;">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="사업자번호/명">
                            <el-input style="width: 70%;" v-model="search.searchSuId" ref="search" @keypress.native.enter="goSearch"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="진행상태">
                            <el-select v-model="search.slipStatus" style="width: 50%; margin-left: 55px;">
                                <el-option 
                                    v-for="(item, idx) in slipStatus"
                                    :key="idx"
                                    :value="item.key"
                                    :label="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row class="field has-addons">
                    <el-col :span="8">
                        <el-form-item label="승인번호">
                            <el-input style="width: 70%;" v-model="search.searchIssueId" @keypress.native.enter="goSearch"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="담당자 이메일">
                            <el-input style="width: 70%;" v-model="search.searchEmail" @keypress.native.enter="goSearch"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="공급받는자 담당자">
                            <el-input style="width: 50%;" v-model="search.searchIpPersname" @keypress.native.enter="goSearch"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div class="grid-wrap">
                <ag-grid-vue 
                    style="width: 100%; height: 400px;"
                    class="ag-theme-alpine"
                    rowSelection="multiple"
                    :columnDefs="columnDefs"
                    :gridOptions="gridOptions"
                    :rowData="rowData"
                    :suppressMenuHide="true"
                    :stopEditingWhenCellsLoseFocus="true"
                    :defaultColDef="defaultColDef"
                    @grid-ready="onGridReady"
                    @rowDoubleClicked="rowDoubleClick"
                    />
            </div>
        </div>
    </layout>
</template>

<script>
import Layout from '@/components/ModalSlot4.vue'

import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
  compatConfig: { MODE: 2 },
    props: {
        postingStEndDate: {
            Type: Array,
            required: true
        },
        lineAttribute6: {
            Type: String,
            required: false
        },
        values: {
            Type: Object,
            required: true
        }
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '세금계산서 조회',
            rowData: [],
            gridOptions: {
                suppressRowClickSelection: true,
                suppressScrollOnNewData: true,
                statusBar: {
                    statusPanels: [
                        {
                        statusPanel: 'agTotalAndFilteredRowCountComponent',
                        align: 'left',
                        },
                    ]
                },
            },
            columnDefs:[],
            defaultColDef: { 
                resizable: true, 
                filter:true, 
                sortable: true,
                // flex: 2
            },
            gridApi : null,
            columnApi : null,
            slipStatus: [{key: '01', value: '미처리'}, {key: '07', value: '처리제외'},{key: '', value: '전체'}],
            search: {
                postDt: [], //기간
                slipStatus: '01', //처리상태
                searchSuId: '', //사업자번호
                searchIssueId: '', //승인번호
                searchEmail: '', //담당자 이메일
                searchIpPersname: '', //공급받는자
            }
        }
    },
    created() {
        this.search.postDt = this.postingStEndDate;

        this.makeColDef();
    },
    mounted() {
        this.$refs.search.$el.getElementsByTagName('input')[0].focus();
    },
    methods: {
        goSearch() {
            this.$store.commit('loading');
            // /${this.search.code}/${this.search.name}
            
            const {postDt, slipStatus, searchSuId, searchIssueId, searchEmail, searchIpPersname } = this.search;
            //yyyy-MM-dd HH:mm:ss
            const searchFrom = this.$moment(postDt[0]).format("YYYY-MM-DD HH:mm:ss")
            const searchTo = this.$moment(postDt[1]).format("YYYY-MM-DD HH:mm:ss")
            const slipHeaderId = this.slipHeaderId || '';
            const searchTaxEvidenceType = this.values.taxEvidenceType || '';
            const searchLineAttribute6 = this.values.lineAttribute6 || '';
            const params = {
                searchFrom, 
                searchTo,
                slipStatus,
                searchSuId,
                searchIssueId,
                slipHeaderId,
                searchEmail,
                searchIpPersname,
                searchTaxEvidenceType,
                searchLineAttribute6
            };
            this.$http.post(`/api/tax/dtiNtsMain/list`, params)
            .then(res => res.data)
            .then(data => {
                /** 구매전표(PO, IM)에서 넘어온 데이터는 사업자번호, 공급가액, 세액, 합계가 모두 일치한 것에 대한 것만 필터링하여 보여줌. */
                if(['PO', 'IM'].includes(this.values.slipTypeCd)) {
                    this.rowData = data.filter(f => 
                        f.suId.replace(/-/gi, '') === this.values.evidenceVendorVat.replace(/-/gi, '') &&
                        this.$numeral(f.chargetotal || 0).value() === this.$numeral(this.values.supplyAmount).value() &&
                        this.$numeral(f.taxtotal || 0).value() === this.$numeral(this.values.taxAmount).value() &&
                        this.$numeral(f.grandtotal || 0).value() && this.$numeral(this.values.totalAmount).value() )
                    .map(x =>x);
                } else {
                    this.rowData = data;
                }
            })
            .finally(() => {
                this.$store.commit('finish');
            });
        },
        onGridReady(params){
            this.gridApi = params.api;
            this.columnApi = params.columnApi;
            // this.autoSizeAll(this.columnApi);
        },
        /**
         * * 그리드 컬럼 width 자동 맞춤
         * @param {*} columnApi 
        */
        autoSizeAll(columnApi) {
            const allColumnIds = [];
            columnApi.columnController.gridColumns.forEach((column) => {
                if(column.getId() !== 'chk') {
                    allColumnIds.push(column.getId());
                }
            });
            columnApi.columnController.autoSizeColumns(allColumnIds, false);
        },
        makeColDef(){
            const self = this;
            // {"field": "chk", "headerName": "", "type": "", "suppressMenu": true, "editable": false, "layout" : "checkbox", "hide": false, "cellStyle": {"textAlign": "center"}},
            this.columnDefs = [
                // {
                //     headerName:'',
                //     field:'chk',
                //     suppressMenu: true,
                //     sortable: false,
                //     editable: false,
                //     headerCheckboxSelection: true,
                //     checkboxSelection: true,
                //     showDisabledCheckboxes: true,
                //     width: 50,
                //     cellStyle: (params) => {
                //         return { 'item-align': 'center', 'color' : 'transparent'};
                //     }
                // },
                {
                    headerName: 'No.', 
                    width: 80, 
                    editable: false, 
                    valueGetter: (params) => {
                        return params.node.rowIndex + 1 
                    },
                },
                {
                    headerName:'유형',
                    headerClass: 'ag-center-header',
                    field:'slipTypeText',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'발행일자',
                    headerClass: 'ag-center-header',
                    field:'issueDt',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'전자세금계산서 \n 작성일자',
                    headerClass: 'ag-center-header',
                    field:'issueDate',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급자 사업체명',
                    headerClass: 'ag-center-header',
                    field:'suName',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급자 \n 사업자등록번호',
                    headerClass: 'ag-center-header',
                    field:'suId',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급가액 합계',
                    headerClass: 'ag-center-header',
                    field:'chargetotal',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'세액 합계',
                    headerClass: 'ag-center-header',
                    field:'taxtotal',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'총액',
                    headerClass: 'ag-center-header',
                    field:'grandtotal',
                    cellStyle:{textAlign:'right'},
                    valueFormatter: (params) => {
                        return self.$numeral(params.value).format('0,000.000')
                    }
                },
                {
                    headerName:'공급자 담당자명',
                    headerClass: 'ag-center-header',
                    field:'suPersname',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급자 담당자 이메일',
                    headerClass: 'ag-center-header',
                    field:'suEmail',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'전자세금계산서종류',
                    headerClass: 'ag-center-header',
                    field:'description',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'수탁 사업자등록번호',
                    headerClass: 'ag-center-header',
                    field:'bpId',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'수탁사업자 사업체명',
                    headerClass: 'ag-center-header',
                    field:'bpName',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 사업자등록번호',
                    headerClass: 'ag-center-header',
                    field:'ipId',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'세금계산서 승인번호',
                    headerClass: 'ag-center-header',
                    field:'issueId',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 주소',
                    headerClass: 'ag-center-header',
                    field:'ipAddr',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급받는자 업태',
                    headerClass: 'ag-center-header',
                    field:'ipBustype',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급받는자 담당 부서 1',
                    headerClass: 'ag-center-header',
                    field:'ipDeptname1',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 담당 부서 2',
                    headerClass: 'ag-center-header',
                    field:'ipDeptname2',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 담당자 이메일 1',
                    headerClass: 'ag-center-header',
                    field:'ipEmail1',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급받는자 담당자 이메일 2',
                    headerClass: 'ag-center-header',
                    field:'ipEmail2',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급받는자 업종',
                    headerClass: 'ag-center-header',
                    field:'ipIndtype',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급받는자 종사 \n 업장 식별코드',
                    headerClass: 'ag-center-header',
                    field:'',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 \n 사업체명',
                    headerClass: 'ag-center-header',
                    field:'ipName',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 담당자명 1',
                    headerClass: 'ag-center-header',
                    field:'ipPersname1',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 담당자명 2',
                    headerClass: 'ag-center-header',
                    field:'ipPersname2',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 대표자명',
                    headerClass: 'ag-center-header',
                    field:'ipRepres',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 담당자 전화번호 1',
                    headerClass: 'ag-center-header',
                    field:'ipTelno1',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 담당자 전화번호 2',
                    headerClass: 'ag-center-header',
                    field:'ipTelno2',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급받는자 사업자등록번호 \n 구분 코드',
                    headerClass: 'ag-center-header',
                    field:'ipId',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급자 주소',
                    headerClass: 'ag-center-header',
                    field:'suAddr',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급자 업태',
                    headerClass: 'ag-center-header',
                    field:'suBustype',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급자 담당부서',
                    headerClass: 'ag-center-header',
                    field:'suDeptname',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급자 업종',
                    headerClass: 'ag-center-header',
                    field:'suIndtype',
                    cellStyle:{textAlign:'left'},
                },
                {
                    headerName:'공급자 대표자명',
                    headerClass: 'ag-center-header',
                    field:'suRepres',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'공급자 담당자 전화번호',
                    headerClass: 'ag-center-header',
                    field:'suTelno',
                    cellStyle:{textAlign:'center'},
                },
                {
                    headerName:'비고',
                    headerClass: 'ag-center-header',
                    field:'descText1',
                    cellStyle:{textAlign:'center'},
                }
            ];
            // this.goSearch();
        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
    }
}
</script>