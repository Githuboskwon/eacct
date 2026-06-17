<template>
    <div class="inner-box">
        <div class="content-name">
            <h2 class="dp-ib">{{title}}</h2>
            <div class="btn-wrap btn-type1 clearfix">
                <button class="btn-size btn-blue fl_left" @click="goSearch"><span class="btn-icon"><i
                        class="fas fa-pen-alt"></i></span> 조회
                </button>
                <button class="btn-size btn-gray fl_left" @click="popVendor(true)"><span class="btn-icon"><i
                        class="far fa-file"></i></span> 신규
                </button>
            </div>
        </div>

        <!-- 검색조건 영역 -->
        <div class="desc-content search-border">
            <div class="item desc-left">
                <div class="desc-item">
                    <div class="tit">
                        <span class="label-tit">- 회사</span>
                    </div>
                    <div class="desc">
                        <select class="select is-fullwidth" v-model="form.compCd">
                            <option value=''>==전체==</option>
                            <option
                                    v-for="item in compCds"
                                    :key="item.key"
                                    :value="item.key"
                                    v-text="item.value"/>
                        </select>
                    </div>
                </div>
            </div>
            <div class="item desc-center">
                <div class="desc-item">
                    <div class="tit">
                        <span class="label-tit">- 문서유형</span>
                    </div>
                    <div class="desc">
                        <select class="select is-fullwidth" v-model="form.docTypeCd">
                            <option value=''>==전체==</option>
                            <option
                                    v-for="item in docTypes"
                                    :key="item.key"
                                    :value="item.key"
                                    v-text="item.value"/>
                        </select>
                    </div>
                </div>
            </div>
            <div class="item desc-right">
                <div class="desc-item">
                    <div class="tit">
                        <span class="label-tit">- 사용여부</span>
                    </div>
                    <div class="desc">
                        <select class="select is-fullwidth" v-model="form.useYn">
                            <option value=''>==전체==</option>
                            <option value='Y'>사용</option>
                            <option value='N'>미사용</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <!-- //검색조건 영역 -->

        <!-- 테이블 -->
        <div class="table-area">
            <div class="table-b">
                <div class="table-header">
                    <div class="table-name">
                        <h3 class="ico_table_name">전결 규정 내역</h3>
                    </div>
                    <div class="btn-wrap btn-type1 clearfix">
                        <button class="btn-size btn-excel" @click="saveExcel">엑셀 저장</button>
                    </div>
                </div>
                <div class="grid-wrap">
                    <ag-grid-vue ref="grid"
                                 style="width: 100%; height: 570px;"
                                 class="ag-theme-alpine"
                                 :columnDefs="columnDefs"
                                 :rowData="ruleDetails"
                                 :gridOptions="gridOptions"
                                 :defaultColDef="defaultColDef"
                                 @rowDoubleClicked="rowDoubleClick"/>
                </div>
            </div>

        </div>
        <!-- //테이블 -->

        <b-modal :active.sync="showVendorModal1" has-modal-card @receive="receiveRulePop">
            <appr-rule-pop :docTypeNm="hiddenDocTypeNm"
                           :docTypeCd="hiddenDocTypeCd"
                           :dtlTypeNm="hiddenDtlTypeNm"
                           :dtlTypeCd="hiddenDtlTypeCd"
                           :useYn="hiddenUseYn"
                           :ruleSeq="hiddenRuleSeq"
                           :curNm="hiddenCurNm"
                           :curCd="hiddenCurCd"
                           :maxAmt="hiddenMaxAmt"
                           :apprTypeNm="hiddenApprTypeNm"
                           :apprTypeCd="hiddenApprTypeCd"
                           :fixYn="hiddenFixYn"
                           :aprverClassNm="hiddenAprverClassNm"
                           :aprverClassVal="hiddenAprverClassVal"
                           :aprverClassCd="hiddenAprverClassCd"
                           :remark="hiddenRemark"
                           :hiddenCheck="hiddenCheck">
            </appr-rule-pop>
        </b-modal>

    </div>
</template>

<script>
    import mixin from '@/mixin';
    import mixinSlip from '@/mixin/slip';
    import { AgGridVue } from 'ag-grid-vue'
    // import ApprRulePop from '@/components/ApprRulePop.vue'

    export default {
        name: 'ApprRuleSet',
        mixins: [mixin, mixinSlip],
        components: {AgGridVue},
        data() {
            return {
                columnDefs: [
                    {headerName: 'No.', width: 50, cellStyle: {textAlign: 'center'}, valueGetter: params => params.node.rowIndex + 1},
                    {headerName: '문서유형', field: 'docTypeNm', width: 100, cellStyle: {textAlign: 'center'}},
                    {headerName: '상세유형', field: 'dtlTypeNm', width: 200, cellStyle: {textAlign: 'center'}},
                    {headerName: '사용여부', field: 'useYn', width: 100, cellStyle: {textAlign: 'center'}},
                    {headerName: '규정순번', field: 'ruleSeq', width: 100, cellStyle: {textAlign: 'center'}},
                    {headerName: '통화코드', field: 'curNm', width: 100, cellStyle: {textAlign: 'center'}},
                    {
                        headerName: '상한금액', field: 'maxAmt', width: 150, cellStyle: {textAlign: 'right'},
                        valueFormatter: params => (params.value === null || params.value === undefined || params.value === '')
                            ? '' : Number(params.value).toLocaleString(undefined, {maximumFractionDigits: 0})
                    },
                    {headerName: '결재유형', field: 'apprTypeNm', width: 120, cellStyle: {textAlign: 'center'}},
                    {headerName: '고정여부', field: 'fixYn', width: 100, cellStyle: {textAlign: 'center'}},
                    {headerName: '결재자구분', field: 'aprverClassNm', width: 180, cellStyle: {textAlign: 'center'}},
                    {headerName: '결재자구분값', field: 'aprverClassVal', width: 200, cellStyle: {textAlign: 'center'}},
                    {headerName: '비고', field: 'remark', width: 200, editable: true, cellStyle: {textAlign: 'center'}},
                    {headerName: '문서코드', field: 'docTypeCd', hide: true},
                    {headerName: '상세코드', field: 'dtlTypeCd', hide: true},
                    {headerName: '통화Cd', field: 'curCd', hide: true},
                    {headerName: '결재코드', field: 'apprTypeCd', hide: true},
                    {headerName: '결재자구분코드', field: 'aprverClassCd', hide: true},
                ],
                gridOptions: {},
                defaultColDef: {resizable: true, sortable: true, filter: true},
                hiddenDocTypeNm: '',
                hiddenDocTypeCd: '',
                hiddenDtlTypeNm: '',
                hiddenDtlTypeCd: '',
                hiddenUseYn: '',
                hiddenRuleSeq: '',
                hiddenCurNm: '',
                hiddenCurCd: '',
                hiddenMaxAmt: '',
                hiddenApprTypeNm: '',
                hiddenApprTypeCd: '',
                hiddenFixYn: '',
                hiddenAprverClassNm: '',
                hiddenAprverClassVal: '',
                hiddenAprverClassCd: '',
                hiddenRemark: '',
                hiddenCheck:'N',
                title: '전결 규정 관리',
                compCds: [],
                docTypes: [],
                ruleDetails: [],
                showVendorModal1: false,
                form: {
                    compCd: '',
                    docTypeCd: '',
                    useYn: '',
                },
            };
        },
        methods : {
            rowDoubleClick(params) {
                const d = params.data;
                this.hiddenDocTypeNm = d.docTypeNm;
                this.hiddenDocTypeCd = d.docTypeCd;
                this.hiddenDtlTypeNm = d.dtlTypeNm;
                this.hiddenDtlTypeCd = d.dtlTypeCd;
                this.hiddenUseYn = d.useYn;
                this.hiddenRuleSeq = d.ruleSeq;
                this.hiddenCurNm = d.curNm;
                this.hiddenCurCd = d.curCd;
                this.hiddenMaxAmt = d.maxAmt;
                this.hiddenApprTypeNm = d.apprTypeNm;
                this.hiddenApprTypeCd = d.apprTypeCd;
                this.hiddenFixYn = d.fixYn;
                this.hiddenAprverClassNm = d.aprverClassNm;
                this.hiddenAprverClassVal = d.aprverClassVal;
                this.hiddenAprverClassCd = d.aprverClassCd;
                this.hiddenRemark = d.remark;
                this.hiddenCheck = 'Y';
                this.popVendor();
            },
            getCompCdCombo() {
                this.$http.get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})
                    .then(response => {
                        this.compCds = response.data;
                    });
            },
            getDocTypeCombo() {
                this.$http.get(`/api/code/combo`, {params: {groupCd: "DOC_TYPE_CD"}})
                    .then(response => {
                        this.docTypes = response.data;
                    });
            },
            saveExcel() {
                this.gridOptions.api.exportDataAsExcel({fileName: '전결규정내역'});
            },
            goSearch() {
                this.$store.commit('loading');
                this.$http.post(`/api/appr/rule/list`, {
                        compCd: this.form.compCd,
                        docTypeCd: this.form.docTypeCd,
                        useYn: this.form.useYn,
                    }
                )
                    .then(response => {
                        if (response.data) {
                            this.ruleDetails = response.data;
                        }
                    }).finally(() => {
                        this.$store.commit('finish')
                    });
            },
            popVendor(clear) {
                if (clear === true) {
                    this.hiddenDocTypeNm = ''
                    this.hiddenDocTypeCd = ''
                    this.hiddenDtlTypeNm = ''
                    this.hiddenDtlTypeCd = ''
                    this.hiddenUseYn = ''
                    this.hiddenRuleSeq = ''
                    this.hiddenCurNm = ''
                    this.hiddenCurCd = ''
                    this.hiddenMaxAmt = ''
                    this.hiddenApprTypeNm = ''
                    this.hiddenApprTypeCd = ''
                    this.hiddenFixYn = ''
                    this.hiddenAprverClassNm = ''
                    this.hiddenAprverClassVal = ''
                    this.hiddenAprverClassCd = ''
                    this.hiddenRemark = ''
                    this.hiddenCheck = 'N'
                }
                this.showVendorModal1 = true;
            },
            receiveRulePop(obj) {
                this.goSearch();
            },
        },
        mounted() {
          document.title = this.title + ' - IJEAS';
            this.getCompCdCombo();
            this.getDocTypeCombo();
            this.goSearch();
        }
    };
</script>

<style scoped>
    .desc-content:after {
        clear: both;
        content: '';
        display: block;
    }

    .btn-wrap {
        margin-bottom: 10px;
    }

    .desc-content {
        border: 2px solid #9db6c9;
        background: #f9fafc;
        margin: 0 0 20px 0;
        border-radius: 4px;
        padding: 15px 2%;
        clear: both;
    }

    .desc-content .item {
        float: left;
    }

    .desc-content .item .desc-item {
        position: relative;
        padding-left: 82px;
        margin-bottom: 8px;
    }

    .desc-content .item .desc-item:last-child {
        margin-bottom: 0;
        height: 25px;
    }

    .desc-content .item .desc-item .tit {
        position: absolute;
        left: 0;
    }

    .desc-content .item .desc-item .label-tit {
        font-family: 'NotoM';
        color: #222;
        font-size: 15px;
    }

    .desc-content .item.desc-left .desc-item {
        padding-left: 90px;
    }

    .desc-content .item.desc-left .desc-item .desc:after {
        clear: both;
        content: '';
        display: block;
    }

    .desc-content .item.desc-left .desc-item .td-s-thumb.search-area input{
        float: left;
    }

    .desc-content .item.desc-left .desc-item .desc.select select {
        width: 70%;
    }

    .desc-content .item.desc-left {
        width: 35%;
        padding-right: 40px;
    }

    .desc-content .item.desc-center {
        width: 35%;
        padding-right: 40px;
    }

    .desc-content .item.desc-right {
        width: 30%;
    }

    .search-area input {
        position: relative;
    }

    .search-border .td-s-thumb.search-area > input,
    .search-border .td-s-thumb.search-area > .ip-box
    .search-border .td-s-thumb.search-area > button {
        float: left;
    }

</style>

