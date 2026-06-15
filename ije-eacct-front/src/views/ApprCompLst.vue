<template>
    <div class="inner-box">
        <div class="content-name">
            <h2 class="dp-ib">{{title}}</h2>
            <div class="btn-type1 clearfix">
              <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch()">조회</el-button>
            </div>
        </div>

        <!-- 검색조건 영역 -->
        <div class="desc-content search-border">
            <div class="item search_wrap">
                <div class="search_box">
                    <div class="search_title">
                        <span class="search_tit" style="color: #CC3D3D;">- 상신일자</span>
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
                                    <span class="search_tit">- 상신일자</span>
                                </div>
                                <div class="search_con search-area">
<!--                                    <div class="datepicker w-type-ymd02 w45p">-->
<!--                                        <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />-->
<!--                                    </div>-->
<!--                                    <span class="datepicker w10p dp-ib tac"> ~ </span>-->
<!--                                    <div class="datepicker w-type-ymd02 w45p">-->
<!--                                        <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />-->
<!--                                    </div>-->
                                    <el-date-picker
                                        v-model="searchDt"
                                        type="daterange"
                                        align="right"
                                        unlink-panels
                                        style="width: 70%;"
                                        range-separator="~"
                                        start-placeholder="시작일"
                                        end-placeholder="종료일">
                                    </el-date-picker>
                                    <button @click="dateSetting('toDay')" class="search_bt_white_s">당일</button>
                                    <button @click="dateSetting('crrntMnth')" class="search_bt_white_s">당월</button>
                                    <button @click="dateSetting('PrvsMnth')" class="search_bt_white_s">전월</button>
                                </div>
                            </div>

<!--                            <div class="search_box_pop">-->
<!--                                <div class="search_title">-->
<!--                                    <span class="search_tit">- 문서유형</span>-->
<!--                                </div>-->
<!--                                <div class="search_con search-area">-->
<!--                                    <b-select class="select is-fullwidth w100p" v-model="form.docTypeCd">-->
<!--                                        <option value=''>==전체==</option>-->
<!--                                        <option-->
<!--                                                v-for="item in docTypes"-->
<!--                                                :key="item.key"-->
<!--                                                :value="item.key"-->
<!--                                                v-text="item.value"/>-->
<!--                                    </b-select>-->
<!--                                </div>-->
<!--                            </div>-->

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 문서상태</span>
                                </div>
                                <div class="search_con search-area">
                                    <b-select class="select is-fullwidth w100p" v-model="form.slipStatus">
                                        <option value=''>==전체==</option>
                                        <option
                                                v-for="item in slipStatusList"
                                                :key="item.key"
                                                :value="item.key"
                                                v-text="item.value"/>
                                    </b-select>
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 기안자</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.draftUserDut" disabled>
                                    <input class="input dp-ib input-bg w30p_5r" type="text" v-model="form.draftUserDept" disabled>
                                    <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.draftId" disabled>
                                    <input class="input dp-ib ps_rl w30p" type="text" v-model="form.draftNm" @input="initEmp('draft')" @keypress.enter="popEmp('draft')">
                                    <button class="icon is-right" @click="popEmp('draft')"><i class="fas fa-search"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 결재자</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.aAprverDut" disabled>
                                    <input class="input dp-ib input-bg w30p_5r" type="text" v-model="form.aAprverDept" disabled>
                                    <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.aAprverId" disabled>
                                    <input class="input dp-ib ps_rl w30p" type="text" v-model="form.aAprverNm" @input="initEmp('aAprver')" @keypress.enter="popEmp('aAprver')" disabled>
                                    <button class="icon is-right"><i class="fas fa-search"></i>
                                    </button>
                                </div>
                            </div>

                            <div class="search_box_pop">
                              <div class="search_title">
                                <span class="search_tit">- 전표번호</span>
                              </div>
                              <div class="search_con search-area">
                                <input class="input Rt-M tal w100p" type="text" v-model="form.slipNo">
                              </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 제목</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input Rt-M tal" type="text" v-model="form.docTitleNm">
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
                                <li>
                                  <button @click="resetSearch()" class="bt_gray_s"><i class="fas fa-undo"></i></button><!-- 검색 초기화 버튼 -->
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
                        <h3 class="ico_table_name">결재한 목록</h3>
                    </div>
                    <div class="btn-type1 clearfix">
                      <el-button size="small" type="success" icon="el-icon-document-checked" @click="saveExcel()">엑셀 저장</el-button>
                    </div>
                </div>

                <div class="grid-wrap">
                    <!-- <dhx-grid ref="grid" v-model="compList" :config="config" @constructGridSuccessful="constructGridSuccessful"/> -->
                    <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height" 
                            :columnDefs="columnDefs" 
                            :rowData="compList" 
                            :gridOptions="gridOptions"
                            :defaultColDef="defaultColDef"
                            @rowDoubleClicked="rowDoubleClick"
                            @cell-value-changed="cellValueChange"
                            @row-selected="onRowSelected"
                            />
                </div>
            </div>

        </div>
        <!-- //테이블 -->
    </div>
</template>

<script>
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import _ from 'lodash'

import DhxCalendar from '@/components/DhxCalendar.vue'
import DhxGrid from '@/components/DhxGrid.vue'
import Emp from '@/components/Emp_Ag.vue'
// import BulkSlipModal from '@/components/BulkSlipModal.vue'

import { AgGridVue } from 'ag-grid-vue'
import ApprovalModal from '@/components/accrualSlip/Approval/Main.vue';
import SlipDetailModal from "@/components/SlipDetailModal.vue";
import SlipBulkDetailModal from "@/components/SlipBulkDetailModal.vue";
import SlipGlDetailModal from "@/components/SlipGlDetailModal.vue";
import SlipFundDetailModal from "@/components/SlipFundDetailModal.vue";
import SlipCollectionDetailModal from "@/components/SlipCollectionDetailModal.vue";
import DraftPop from "@/components/costBudget/SlipBudgetDetailModal.vue";
import SlipBondDetailModal from "@/components/SlipBondDetailModal.vue";
// import ApprSubm from "@/views/ApprSubm";

export default {
    name: 'ApprCompLst',
    mixins: [mixin, mixinSlip],
    components: {Emp, DhxGrid, DhxCalendar, AgGridVue},
    data() {
        return {
            columnDefs : [],
            gridOptions : {
                enableRangeSelection: true,
                statusBar: {
                    statusPanels: [
                        {
                        statusPanel: 'agTotalAndFilteredRowCountComponent',
                        align: 'left',
                        },
                    ]
                },
            },
            defaultColDef: {},
            title: '결재한 문서',
            docTypes: [],
            slipStatusList: [],
            compList: [],
            showEmpModal: false,
            searchDt: [this.$moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')],
            form: {
                docTypeCd: '',
                slipStatus: '',
                searchDtmFr: this.$moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD'),
                searchDtmTo: this.$moment().format('YYYY-MM-DD'),
                draftId: '',
                draftNm: '',
                draftUserDut: '',
                draftUserDept: '',
                aAprverId : this.$store.state.loginInfo.loginId,
                aAprverNm : this.$store.state.loginInfo.userName,
                aAprverDut : this.$store.state.loginInfo.loginJobGradeNm,
                aAprverDept : this.$store.state.loginInfo.loginDeptNm,
                docTitleNm: '',
                slipNo: '',
            },
        };
    },
    methods: {
        rowDoubleClick(params){
            this.$cookie.set('searchForm', JSON.stringify(this.form));
            this.$store.commit('searchForm', JSON.parse(this.$cookie.get('searchForm')));

            let slipType = params.data.slipType;
            let docMngNo = params.data.apprNo;
            let slipNo = params.data.slipNo;

            switch(slipType)
            {
                case 'E1' :
                case 'E2' :
                case 'E3' :
                case 'E4' :
                    this.$router.push({path: `/apprDtlQry/${docMngNo}`});
                    break;
                case 'E5' :
                    this.$modal.open({
                        component: BulkSlipModal,
                        props: {
                            grSlipNo: grSlipNo,
                            docMngNo: docMngNo,
                            loaded: false
                        },
                        parent: this,
                        width: 1200
                    })
                    break;
                default:
                if(params.data.docTypeCd === 'BDGT'){
                    var vm = this
                    this.$modal.open({
                    component: ApprSubm,
                    props: {
                        docType: params.data.docTypeCd,
                        budReqNo: params.data.docMngNo,
                        docMngNo : params.data.apprNo,
                        // value: this.data
                    },
                    parent: this,
                    width: 1200,
                    events: {
                        close(data) {
                        // if (callback !== undefined && typeof callback === 'function') {
                        //   callback.apply(this, [data])
                        // }
                        vm.goSearch();
                        }
                    }
                    })
                }
                else{
                    this.showDetailPop(slipNo, slipType, docMngNo, params);
                }
                break;
            }
        },
        showDetailPop(slipNo, slipType, docMngNo, params){
            let vm = this;
            let title = ''
            let setModal = undefined
            switch(slipType)
            {
              case '21' :
                title = '건별지급'
                setModal = SlipDetailModal
                break;
              case '22' :
                title = '대량지급'
                setModal = SlipBulkDetailModal
                break;
              case '23' :
                title = '전자채권'
                setModal = SlipBondDetailModal
                break;
              case '24' :
                title = '자금전표'
                setModal = SlipFundDetailModal
                break;
              case '25' :
                title = '집금전표'
                setModal = SlipCollectionDetailModal
                break;
              case '27' :
                title = 'GL전표'
                setModal = SlipGlDetailModal
                break;
              case '90' :
                title = '비용예산'
                setModal = DraftPop
                break;
              default:
                title = '전표'
                setModal = ApprovalModal;
                break;
            }

            this.$modal.open({
                component: setModal,
                parent: this,
                props: {
                    docMngNo: docMngNo,
                    slipNo: slipNo,
                    slipType: slipType,
                    title: title,
                    docType: 'comp',
                    slipHeaderId : params.data.apprGroupId,
                    status : params.data.slipStatus,
                },
              width: slipType === '90' ? 1800 : 1200,
              events: {
                close(data) {
                  vm.goSearch();

                }
              },
            })
        },
        getStatusTypeCombo() {
            this.$http.get(`/api/code/combo`, {params: {groupCd: "SLIP_STAT_CD"}})
                .then(response => {
                    this.slipStatusList = response.data;
                });
        },
        saveExcel() {
            var params = {
            fileName : "결재한 문서"
            };
            this.gridOptions.api.exportDataAsCsv(params)
        },
        compareDate(value){
            if(!value){
            this.$swal({ type: 'info', text: '기안일자를 입력해주세요.' });
            return false;
            }
        },
        goSearch() {
            this.compareDate(this.searchDt);
            this.form.searchDtmFr = this.$moment(this.searchDt[0]).format("YYYYMMDD");
            this.form.searchDtmTo = this.$moment(this.searchDt[1]).format("YYYYMMDD");

            const param = JSON.parse(JSON.stringify(this.form));

            // const stripFields = ['searchDtmFr', 'searchDtmTo'];
            // _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

            // if (param.searchDtmFr > param.searchDtmTo) {
            //     this.$swal({type: 'warning', text: '조회 시작일자와 종료일자 지정이 잘못되었습니다.'});
            //     return false
            // }

            this.$store.commit('loading');
            this.$http.post(`/api/appr/done/list`, {
                        docTypeCd: param.docTypeCd,
                        searchDtmFr: this.toPure(param.searchDtmFr),
                        searchDtmTo: this.toPure(param.searchDtmTo),
                        slipStatus: param.slipStatus,
                        draftId: param.draftId,
                        docTitleNm: param.docTitleNm,
                        slipNo: param.slipNo
                }
            )
                .then(response => {
                    if (response.data) {
                        this.compList = response.data;
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
        resetSearch(){
            this.searchDt = [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')];
            this.form.slipStatus = "";
            this.form.searchDtmFr = '';
            this.form.searchDtmTo = '';
            this.form.draftId = "";
            this.form.draftNm = "";
            this.form.draftUserDut = "";
            this.form.draftUserDept = "";
            this.form.delegateChk = "";
            this.form.docTitleNm = "";
            this.form.page = 0;
            this.form.cctrCd = "";
            this.form.cctrNm = "";
        },
        popEmp(type) {
            const that = this;

            this.$modal.open({
                component: Emp,
                parent: this,
                props: {
                    param: this.form.draftNm
                },
                width: 1800,
                events: {
                    close(obj) {
                        if(type === 'draft') {
                            that.form.draftId = obj.empNo;
                            that.form.draftNm = obj.empNm;
                            that.form.draftUserDut = obj.jobGradeNm;
                            that.form.draftUserDept = obj.deptNm;
                        } else if (type === 'aAprver') {
                            that.form.aAprverId = obj.empNo;
                            that.form.aAprverNm = obj.empNm;
                            that.form.aAprverDut = obj.jobGradeNm;
                            that.form.aAprverDept = obj.deptNm;

                        }
                    }
                }
            });
        },
        initEmp(type, force) {
            if(type === 'draft') {
                if (force === true) this.form.draftNm = '';
                if (this.form.draftNm === '') {
                    this.form.draftId = '';
                    this.form.draftUserDut = '';
                    this.form.draftUserDept = '';
                }

            } else if(type === 'aAprver') {
                if (force === true) this.form.aAprverNm = '';
                if (this.form.aAprverNm === '') {
                    this.form.aAprverId = '';
                    this.form.aAprverDut = '';
                    this.form.aAprverDept = '';
                }

            }
        },
        makeColDef(){

            this.columnDefs = [
                {
                    headerName: ''
                    , headerCheckboxSelection: true
                    , checkboxSelection: true
                    , field:'regYn'
                    , width : 60
                    , cellStyle:{textAlign: 'center'}
                    , editable: false
                },
                {
                    headerName: 'No.', 
                    width: 65,
                    cellStyle:{textAlign: 'center'},
                    valueFormatter: (params) => {               
                        return params.node.rowIndex+1;
                    }
                },
                {
                    headerName: '제목', 
                    field: 'docTitleNm', 
                    width: 350,
                    // valueFormatter: (params) => {
                    //     if(params.value === '제목'){
                    //     params.value = "제목 - 머릿말("+params.data.headerRemark+")";
                    //     }
                    //     return params.value;
                    // },
                    cellStyle:{textAlign: 'left'}
                },
                {
                    headerName: '거래유형', 
                    field: 'slipTypeNm', 
                    width: 120,
                    cellStyle:{textAlign: 'left'},
                    hide:true
                },
                {
                    headerName: '결재상태',
                    field: 'slipStatusNm',
                    width: 120,
                    cellStyle:{textAlign: 'center'}
                },
                {
                  headerName: '문서번호',
                  field: 'slipNo',
                  width: 180,
                  cellStyle:{textAlign: 'left'}
                },
                {
                    headerName: '결재번호', 
                    field: 'apprNo', 
                    width: 180,
                    cellStyle:{textAlign: 'left'}
                },
                {
                    headerName: '기안자부서',
                    field: 'draftDeptNm',
                    width: 160,
                    cellStyle:{textAlign: 'left'}
                },
                {
                    headerName: '기안자사번', 
                    field: 'draftId', 
                    width: 120,
                    cellStyle:{textAlign: 'center'}
                },
                {
                    headerName: '기안자', 
                    field: 'draftNm', 
                    width: 100,
                    cellStyle:{textAlign: 'center'}
                },
                {
                    headerName: '직급', 
                    field: 'draftUserJob', 
                    width: 100,
                    cellStyle:{textAlign: 'center'}
                },
                {
                    headerName: '기안일자', 
                    field: 'draftDtm', 
                    width: 120,
                    valueFormatter: (params) => {
                        return this.getDateFormat(params.value)
                    },
                    cellStyle:{textAlign: 'left'}
                },
            ]
        },
        onGridReady(){
            this.gridOptions.api.sizeColumnsToFit();
        },
        dateSetting(str){
            switch (str) {
            case 'toDay':
                this.form.searchDtmFr = this.$moment().format('YYYYMMDD')
                this.form.searchDtmTo = this.$moment().format('YYYYMMDD')
                this.searchDt =  [this.$moment().format('YYYY-MM-DD HH:mm:ss'),this.$moment().format('YYYY-MM-DD HH:mm:ss')];
                break;
            case 'crrntMnth':
                this.form.searchDtmFr = this.$moment().startOf('month').format('YYYYMMDD')
                this.form.searchDtmTo = this.$moment().endOf('month').format('YYYYMMDD')
                this.searchDt =  [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'),this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')]
                break;
            case 'PrvsMnth':
                this.form.searchDtmFr = this.$moment().add(-1, 'month').startOf('month').format('YYYYMMDD')
                this.form.searchDtmTo = this.$moment().add(-1, 'month').endOf('month').format('YYYYMMDD')
                this.searchDt =  [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss'),this.$moment().add(-1, 'month').endOf('month').format('YYYY-MM-DD HH:mm:ss')]
                break;
            }
            // this.goSearch()
        },
        onRowSelected(params) {
            const rowIdx = params.rowIndex;
            this.data[rowIdx].regYn = params.node.isSelected();
        }
    },
    beforeMount(){
        this.makeColDef();
        this.defaultColDef = { resizable: true, filter:true, sortable: true };
    },
    created(){
        document.title = this.title + ' - IJEAS';
        if(!_.isEmpty(this.$route.params)){
        this.form = this.$route.params;
        }
    },
    mounted() {
        this.getStatusTypeCombo()
        this.goSearch()
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
    padding-left: 75px;
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

.desc-content .item.desc-left .desc-item .td-s-thumb.search-area input,
.desc-content .item.desc-left .desc-item .td-s-thumb.search-area .ip-box {
    float: left;
}

.desc-content .item.desc-left .desc-item .desc.select select {
    width: 70%;
}

.desc-content .item.desc-left {
    width: 38%;
    padding-right: 20px;
}

.desc-content .item.desc-center {
    width: 35%;
    padding-right: 40px;
}

.desc-content .item.desc-right {
    width: 27%;
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

.search-border .td-s-thumb.search-area .ip-box {
    vertical-align: top;
}

@media (max-width: 1580px) {
    .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
        width: 50%;
    }
}
</style>

