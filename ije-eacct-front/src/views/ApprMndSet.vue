<template>
    <div class="inner-box">
        <div class="content-name">
            <h2 class="dp-ib">{{title}}</h2>
            <div class="btn-wrap btn-type1 clearfix">
                <button class="btn-size btn-blue fl_left" @click="goSearch"><span class="btn-icon"><i
                        class="fas fa-pen-alt"></i></span> 조회
                </button>
                <button class="btn-size btn-gray fl_left" v-if="['002','004','009','020'].indexOf(this.$store.state.loginInfo.loginDutCd) >= 0" @click="popVendor(true)"><span class="btn-icon"><i
                        class="far fa-file"></i></span> 신규
                </button>
            </div>
        </div>

        <!-- 검색조건 영역 -->
        <div class="desc-content search-border">
            <div class="item search_wrap">
                <div class="search_box">
                    <div class="search_title">
                        <span class="search_tit" style="color: #CC3D3D;">- 위임일자</span>
                    </div>
                    <div class="search_con">
<!--                        <div class="datepicker w-type-ymd02">-->
<!--                            <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />-->
<!--                        </div>-->
<!--                        <span class="wave"> ~ </span>-->
<!--                        <div class="datepicker w-type-ymd02">-->
<!--                            <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />-->
<!--                        </div>-->
                        <el-date-picker
                            v-model="searchDt"
                            type="daterange"
                            align="right"
                            unlink-panels
                            style="width: 260px;"
                            range-separator="~"
                            start-placeholder="시작일"
                            end-placeholder="종료일">
                        </el-date-picker>
                    </div>
                </div>
                <div class="search_box">
                    <button class="item-list icon is-right btn_s_w" @click="openModal()" type="button">상세검색<i class="fas fa-plus"></i></button>
                </div>


                <!-- 상세검색 내용 -->
                <div id="open-moda" class="modal-window">
                    <div class="modal-window-wrap">
                        <div class="modal-window-top">
                            <h4>상세검색</h4>
                            <button title="Close" @click="closeModal()" type="button" class="bt-modal-close mt5"><img src="../../public/img/bt_close_w.png" /></button>
                        </div>

                        <div class="search_box_wrap">
                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 위임일자</span>
                                </div>
<!--                                <div class="search_con search-area">-->
<!--                                    <div class="datepicker w-type-ymd02 w45p">-->
<!--                                        <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />-->
<!--                                    </div>-->
<!--                                    <span class="datepicker w10p dp-ib tac"> ~ </span>-->
<!--                                    <div class="datepicker w-type-ymd02 w45p">-->
<!--                                        <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />-->
<!--                                    </div>-->
<!--                                </div>-->
                                  <el-date-picker
                                      v-model="searchDt"
                                      type="daterange"
                                      align="right"
                                      style="width: 75%"
                                      unlink-panels
                                      range-separator="~"
                                      start-placeholder="시작일"
                                      end-placeholder="종료일">
                                  </el-date-picker>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 회사</span>
                                </div>
                                <div class="search_con search-area">
                                    <b-select class="select is-fullwidth w100p" v-model="form.compCd">
                                        <option value=''>==전체==</option>
                                        <option
                                                v-for="item in compCds"
                                                :key="item.key"
                                                :value="item.key"
                                                v-text="item.value"/>
                                    </b-select>
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 위임자</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input input-bg dp-ib w30p_5r" type="text" v-model="form.searchAdlgId" disabled>
                                    <input class="input dp-ib w70p" type="text" v-model="form.searchAdlgNm" @input="initSearchAdlgId"
                                        @keypress.enter="popSearchAdlgEmp">
                                    <button class="icon is-right" @click="popSearchAdlgEmp(true)"><i
                                            class="fas fa-search"></i>
                                    </button>
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 수임자</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input input-bg dp-ib w30p_5r" type="text" v-model="form.searchActId" disabled>
                                    <input class="input dp-ib w70p" type="text" v-model="form.searchActNm" @input="initSearchActId"
                                        @keypress.enter="popSearchActEmp">
                                    <button class="icon is-right" @click="popSearchActEmp(true)"><i
                                            class="fas fa-search"></i>
                                    </button>
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 위임상태</span>
                                </div>
                                <div class="search_con search-area">
                                    <b-select class="select is-fullwidth w100p" v-model="form.adlgStatCd">
                                        <option value=''>==전체==</option>
                                        <option
                                                v-for="item in adlgCds"
                                                :key="item.key"
                                                :value="item.key"
                                                v-text="item.value"/>
                                    </b-select>
                                </div>
                            </div>
                        </div>

                        <div class="modal-window-bottom">
                            <ul>
                                <li>
                                    <button class="bt_blue_s" @click="goSearch">검색</button>
                                </li>
                                <li>
                                    <button @click="closeModal()" title="Close" class="bt_white_s">닫기</button>
                                </li>
                            </ul>


                        </div>

                    </div>
                </div>
                <!-- //상세검색 내용 -->
            </div>
        </div>
        <!-- //검색조건 영역 -->

        <!-- 테이블 -->
        <div class="table-area">

            <div class="table-b">
                <div class="table-header">
                    <div class="table-name">
                        <h3 class="ico_table_name">결재 위임현황</h3>
                    </div>
                    <div class="btn-wrap btn-type1 clearfix">
                        <button class="btn-size btn-excel" @click="saveExcel">엑셀 저장</button>
                    </div>
                </div>

                <div class="grid-wrap">
                    <ag-grid-vue ref="grid"
                                 style="width: 100%; height: 550px;"
                                 class="ag-theme-alpine"
                                 :columnDefs="columnDefs"
                                 :rowData="mndList"
                                 :gridOptions="gridOptions"
                                 :defaultColDef="defaultColDef"
                                 @grid-ready="onGridReady"
                                 @rowDoubleClicked="rowDoubleClick"/>
                </div>
            </div>

        </div>
        <!-- //테이블 -->

        <!--팝업-->
        <!-- <b-modal :active.sync="showEmpModal" has-modal-card @receive="receiveSearchAdlgEmp">
            <emp :param="form.searchAdlgNm"></emp>
        </b-modal>
        <b-modal :active.sync="showEmpModal2" has-modal-card @receive="receiveSearchActEmp">
            <emp :param="form.searchActNm"></emp>
        </b-modal> -->

        <b-modal :active.sync="showVendorModal1" has-modal-card @receive="receiveMndPop">
            <appr-mnd-pop :adlgId="hiddenAdlgId"
                          :adlgNm="hiddenAdlgNm"
                          :actId="hiddenActId"
                          :actNm="hiddenActNm"
                          :adlgSeq="hiddenAdlgSeq"
                          :hiddenDtmFr="hiddenDtmFr"
                          :hiddenDtmTo="hiddenDtmTo"
                          :hiddenCheck="hiddenCheck">
            </appr-mnd-pop>
        </b-modal>

        <!--//팝업 -->
    </div>
</template>

<script>
    import mixin from '@/mixin';
    import mixinSlip from '@/mixin/slip';
    import _ from 'lodash'

    import DhxCalendar from '@/components/DhxCalendar.vue'
    import { AgGridVue } from 'ag-grid-vue'
    // import Emp from '@/components/Emp.vue';
    import Emp from '@/components/Emp_Ag.vue';
    // import ApprMndPop from '@/components/ApprMndPop.vue';

    export default {
        name: 'ApprMndSet',
        mixins: [mixin, mixinSlip],
        components: {Emp, AgGridVue, DhxCalendar},
        data() {
            return {
                columnDefs: [],
                gridOptions: {},
                defaultColDef: {resizable: true, sortable: true, filter: true},
                title: '결재위임',
                docTypes: [],
                docStatuses: [],
                compCds: [],
                adlgCds: [],
                mndList: [],
                hiddenAdlgId: '',
                hiddenAdlgNm: '',
                hiddenActId: '',
                hiddenActNm: '',
                hiddenAdlgSeq: '',
                hiddenDtmFr: '',
                hiddenDtmTo: '',
                hiddenCheck: 'N',
                showEmpModal: false,
                showEmpModal2: false,
                showVendorModal1: false,
                searchDt: [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')],
                form: {
                    compCd: '',
                    // searchDtmFr: this.$moment().format('YYYY-MM-DD'),
                    // searchDtmTo: this.$moment().add(+1, 'month').startOf('month').format('YYYY-MM-DD'),
                    searchDtmFr: '',
                    searchDtmTo: '',
                    searchAdlgId: '',
                    searchAdlgNm: '',
                    searchActId: '',
                    searchActNm: '',
                    adlgStatCd: '',
                },
            };
        },
        methods: {
            onGridReady() {
                this.makeColDef();
            },
            makeColDef() {
                const that = this;
                const dateFmt = (params) => params.value ? that.$moment(params.value).format('YYYY-MM-DD') : '';
                this.columnDefs = [
                    {headerName: 'No.', width: 50, cellStyle: {textAlign: 'center'}, valueGetter: params => params.node.rowIndex + 1},
                    {headerName: '위임자', field: 'adlgNm', width: 300, cellStyle: {textAlign: 'center'}},
                    {headerName: '수임자', field: 'actNm', width: 300, cellStyle: {textAlign: 'center'}},
                    {headerName: '위임시작일', field: 'adlgStrDt', width: 200, cellStyle: {textAlign: 'center'}, valueFormatter: dateFmt},
                    {headerName: '위임종료일', field: 'adlgEndDt', width: 200, cellStyle: {textAlign: 'center'}, valueFormatter: dateFmt},
                    {headerName: '위임실행일시', field: 'adlgExeDtm', hide: true},
                    {headerName: '위임해제일시', field: 'adlgRvcDtm', hide: true},
                    {headerName: '위임상태', field: 'adlgStatNm', width: 200, cellStyle: {textAlign: 'center'}},
                    {headerName: '위임자Id', field: 'adlgId', hide: true},
                    {headerName: '수임자Id', field: 'actId', hide: true},
                    {headerName: 'Seq', field: 'adlgSeq', hide: true},
                    {headerName: '위임상태Cd', field: 'adlgStatCd', hide: true},
                ];
            },
            rowDoubleClick(params) {
                const d = params.data;
                this.hiddenAdlgId = d.adlgId;
                this.hiddenAdlgNm = d.adlgNm;
                this.hiddenActId = d.actId;
                this.hiddenActNm = d.actNm;
                this.hiddenAdlgSeq = d.adlgSeq;
                this.hiddenDtmFr = d.adlgStrDt ? this.$moment(d.adlgStrDt).format('YYYY-MM-DD') : d.adlgStrDt;
                this.hiddenDtmTo = d.adlgEndDt ? this.$moment(d.adlgEndDt).format('YYYY-MM-DD') : d.adlgEndDt;
                this.hiddenCheck = 'Y';

                if (d.adlgStatCd !== '10') {
                    this.$swal({type: 'warning', text: '위임이 해제된 건입니다.'});
                    return false;
                }
                this.popVendor();
            },
            getCompCdCombo() {
                this.$http.get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})
                    .then(response => {
                        this.compCds = response.data;
                    });
            },
            getAdlgCdCombo() {
                this.$http.get(`/api/code/combo`, {params: {groupCd: "ADLG_STAT_CD"}})
                    .then(response => {
                        this.adlgCds = response.data;
                    });
            },
            saveExcel() {
                this.gridOptions.api.exportDataAsExcel({fileName: '결재위임내역'});
            },
          compareDate(value){
            if(!value){
              this.$swal({ type: 'info', text: '위임일자를 입력해주세요.' });
              return false;
            }
          },
          goSearch() {
                this.compareDate(this.searchDt);
                this.form.searchDtmFr = this.$moment(this.searchDt[0]).format("YYYYMMDD");
                this.form.searchDtmTo = this.$moment(this.searchDt[1]).format("YYYYMMDD");

                // console.log("조회...")
                const param = JSON.parse(JSON.stringify(this.form));
                // const stripFields = ['searchDtmFr', 'searchDtmTo'];
                // _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

                this.$store.commit('loading');
                this.$http.post(`/api/appr/delegating/list`, {
                        compCd: param.compCd,
                        adlgStrDt: param.searchDtmFr === '0' ? null : param.searchDtmFr,
                        adlgEndDt: param.searchDtmTo === '0' ? null : param.searchDtmTo,
                        adlgId: param.searchAdlgId,
                        actId: param.searchActId,
                        adlgStatCd: param.adlgStatCd
                    }
                )
                    .then(response => {
                        if (response.data) {
                            this.mndList = response.data;
                            // this.config.queryPage(0)
                        }
                    }).finally(() => {
                        this.$store.commit('finish')
                    });
                    document.getElementById("open-moda").style.opacity = "0";
                    document.getElementById("open-moda").style.pointerEvents = "none";
            },
            openModal() {
                document.getElementById("open-moda").style.opacity = "1";
                document.getElementById("open-moda").style.pointerEvents = "auto";
            },
            closeModal() {
                document.getElementById("open-moda").style.opacity = "0";
                document.getElementById("open-moda").style.pointerEvents = "none";
            },
            popVendor(clear) {
                if (clear === true) {
                    this.hiddenAdlgId = '';
                    this.hiddenAdlgNm = '';
                    this.hiddenActId = '';
                    this.hiddenActNm = '';
                    this.hiddenAdlgSeq = '';
                    this.hiddenDtmFr = '';
                    this.hiddenDtmTo = '';
                    this.hiddenCheck = 'N'
                }
                this.showVendorModal1 = true;
            },
            popEmp(clear) {
                if (clear === true) {
                    this.form.draftUserId = '';
                    this.form.draftNm = '';
                }
                this.showEmpModal = true;
            },
            popSearchAdlgEmp(clear) {
                let vm = this
                this.$modal.open({
                    component: Emp,
                    props: {
                        param: this.form.searchAdlgNm
                    },
                    parent: this,
                    events: {
                        close(obj) {
                            vm.receiveSearchAdlgEmp(obj)
                        }
                    }
                })

            },
            popSearchActEmp(clear) {
                let vm = this
                this.$modal.open({
                    component: Emp,
                    props: {
                        param: this.form.searchActNm
                    },
                    parent: this,
                    events: {
                        close(obj) {
                            vm.receiveSearchActEmp(obj)
                        }
                    }
                })
            },
            receiveSearchAdlgEmp(obj) {
                this.form.searchAdlgId = obj.empNo;
                this.form.searchAdlgNm = obj.empNm;
            },
            receiveSearchActEmp(obj) {
                this.form.searchActId = obj.empNo;
                this.form.searchActNm = obj.empNm;
            },
            receiveMndPop(obj) {
                this.goSearch();
            },
            initEmp(force) {
                if (force === true) this.form.wrtNm = '';
                if (this.form.wrtNm === '') this.form.draftUserId = '';
            },
            initSearchAdlgId(force) {
                if (force === true) this.form.searchAdlgNm = '';
                if (this.form.searchAdlgNm === '') this.form.searchAdlgId = '';
            },
            initSearchActId(force) {
                if (force === true) this.form.searchActNm = '';
                if (this.form.searchActNm === '') this.form.searchActId = '';
            },
        },
        mounted() {
          document.title = this.title + ' - IJEAS';
            if (this.$route.query) {
                for (let member in this.form) {
                    if (this.$route.query[member] !== undefined) {
                        this.form[member] = this.$route.query[member]
                    }
                }
            }
            this.getCompCdCombo();
            this.getAdlgCdCombo();
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
        padding-left: 70px;
    }

    .desc-content .item.desc-left .desc-item .desc:after {
        clear: both;
        content: '';
        display: block;
    }

    .desc-content .item.desc-left .desc-item .desc .datepicker {
        float: left;
    }

    .desc-content .item.desc-left .desc-item .desc span.wave {
        float: left;
        padding: 0 6px;
    }

    .desc-content .item.desc-left .desc-item .td-s-thumb.search-area:after {
        clear: both;
        content: '';
        display: block;
    }

    .desc-content .item.desc-left .desc-item .td-s-thumb.search-area input,
    .desc-content .item.desc-left .desc-item .td-s-thumb.search-area .ip-box {
        float: left;
    }

    .desc-content .item.desc-left .desc-item .desc.select select {
        width: 70%;
    }

    .desc-content .item.desc-left {
        width: 37%;
        padding-right: 20px;
    }

    .desc-content .item.desc-right {
        width: 26%;
    }

    .search-area input {
        position: relative;
    }

    .search-area .icon {
        position: absolute;
        right: 8px;
        top: 1px;
        z-index: 100;
        cursor: pointer;
        font-size: 16px;
        color: #555;
    }

    .search-border .td-s-thumb {
        position: relative;
        display: inline-block;
    }

    .search-border .td-s-thumb.search-area > input,
    .search-border .td-s-thumb.search-area > .ip-box
    .search-border .td-s-thumb.search-area > button {
        float: left;
    }

    .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
        width: 60%;
        margin-left: 6px;
    }

    @media (max-width: 1580px) {
        .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
            width: 50%;
        }
    }
</style>

