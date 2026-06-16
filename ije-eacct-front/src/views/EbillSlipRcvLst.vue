<template>
    <div class="inner-box">
        <div class="content-name">
            <h2 class="dp-ib">{{title}}</h2>
            <div class="btn-type1 clearfix">
                <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch">조회</el-button>
            </div>
        </div>

        <!-- 검색조건 영역 -->
        <div class="desc-content search-border">
            <div class="item search_wrap">
                <div class="search_box">
                    <div class="search_title">
                        <span class="search_tit">- 공급일자</span>
                    </div>
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
                                    <span class="search_tit">- 공급일자</span>
                                </div>
                                <div class="search_con search-area">
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
                                  <button @click="dateSetting('toDay')" class="search_bt_white_s">당일</button>
                                  <button @click="dateSetting('crrntMnth')" class="search_bt_white_s">당월</button>
                                  <button @click="dateSetting('PrvsMnth')" class="search_bt_white_s">전월</button>
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 승인번호</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input Rt-M tal" type="text" v-model="form.searchIssueId">
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 공급자</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input Rt-M tal" type="text" v-model="form.searchVendor">
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 담당자/E-MAIL</span>
                                </div>
                                <div class="search_con search-area">
                                    <input class="input Rt-M tal" type="text" v-model="form.searchEmail">
                                </div>
                            </div>

                            <div class="search_box_pop">
                                <div class="search_title">
                                    <span class="search_tit">- 처리상태</span>
                                </div>
                                <div class="search_con search-area">
                                    <b-select class="select is-fullwidth" v-model="form.searchStatus">
                                        <option value=''>==전체==</option>
                                        <option
                                                v-for="item in dealStats"
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
                        <h3 class="ico_table_name">수신내역</h3>
                    </div>
                    <div class="btn-type1 clearfix">
                        <el-button size="small" type="success" icon="el-icon-document-checked" @click="saveExcel">엑셀 저장</el-button>
                    </div>
                </div>
                <div class="grid-wrap">
                    <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height" 
                            :columnDefs="columnDefs" 
                            :rowData="rcvList" 
                            :gridOptions="gridOptions"
                            :defaultColDef="defaultColDef"
                            @rowDoubleClicked="rowDoubleClick"/>
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


    /*   import SlipDetailModal from '@/components/SlipDetailModal.vue'
       import SlipPayDetailModal from '@/components/SlipPayDetailModal.vue'
       import SlipEtcDetailModal from '@/components/SlipEtcDetailModal.vue'
       import ErpItfErrModal from '@/components/ErpItfErrModal.vue'
       import GlDetailModal from '@/components/GlDetailModal.vue'
       import PayrollSlipModal from '@/components/PayrollSlipModal.vue'
       import BulkSlipModal from '@/components/BulkSlipModal.vue'

       import XmlUploadPop from '@/components/XmlUploadPop.vue'*/

    import { AgGridVue } from 'ag-grid-vue'
    import EtaxTrx from "@/components/EtaxTrxSearchPop";

    export default {
        name: 'SlipList',
        mixins: [mixin, mixinSlip],
        components: {DhxCalendar, DhxGrid, AgGridVue},
        props: {
          dateSet: {
            type: Object,
            required: false
          }
        },        
        data() {
            return {
                empDetail: [],
                columnDefs : [],
                gridOptions : {
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
                title: '전자세금계산서',
                dealStats: [],
                rcvList: [],
                searchDt: [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')],
                form: {
                    searchFrom: this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'),
                    searchTo: this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss'),
                    searchIssueId: '',
                    searchVendor: '',
                    searchEmail:'',
                    searchStatus:'00'
                },
                setDate: [],
            };
        },
        methods: {
            makeColDef(){
                const that = this;
                this.columnDefs = [
                    {
                        headerName: '', 
                        children:[
                             {
                                headerName: 'No.', 
                                width: 80,
                                cellStyle:{textAlign: 'center'},
                                valueFormatter: (params) => {               
                                    return params.node.rowIndex+1;
                                }
                            },
                        ]
                    },
                    {
                        headerName: '전자세금계산서', 
                        children:[
                            {
                                headerName: '전자세금계산서 승인번호', 
                                field: 'issueId', 
                                width: 230,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: '처리상태', 
                                field: 'dealStatNm',
                                width: 110,
                                cellStyle:{textAlign: 'left'},
                                valueFormatter: (params) => {
                                    if(!params.data.slipStatus){
                                      return '미처리'
                                    }else if(params.data.slipStatus == 'SV' || params.data.slipStatus == 'AP' || params.data.slipStatus == 'CP'){
                                      return '처리중'
                                    }else if(params.data.slipStatus == 'CC'){
                                      return '처리완료'
                                    }else if(params.data.slipStatus == 'AR' || params.data.slipStatus == 'CR' || params.data.slipStatus == 'SC' || params.data.slipStatus == 'FR'){
                                      return '미처리(반려)'
                                    }
                                }
                            },
                            {
                                headerName: '공급일자', 
                                field: 'dtiWdate',
                                width: 110,
                                cellStyle:{textAlign: 'center'},
                                valueFormatter: (params) => {               
                                    return that.$moment(params.value).format('YYYY-MM-DD');
                                }
                            },
                            {
                                headerName: '전표번호', 
                                field: 'slipNo',
                                width: 180,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                              headerName: '전표상태',
                              field: 'slipStatus',
                              width: 140,
                              cellStyle:{textAlign: 'left'},
                              hide: true
                            },
                            {
                                headerName: '전표상태', 
                                field: 'slipStatusNm',
                                width: 140,
                                cellStyle:{textAlign: 'left'},
                                valueFormatter: (params) => {
                                  if(params.data.slipStatus == 'SV'){
                                    return '저장됨'
                                  }else if(params.data.slipStatus == 'AP'){
                                    return '결재중'
                                  }else if(params.data.slipStatus == 'CP'){
                                    return '검인중'
                                  }else if(params.data.slipStatus == 'CC'){
                                    return '검인됨'
                                  }
                                }
                            },
                        ]
                    },
                    {
                        headerName: '공급자', 
                        children:[
                            {
                                headerName: '회사', 
                                field: 'supComName',
                                width: 200,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: '거래처명', 
                                field: 'erpName',
                                width: 200,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: '사업자등록번호', 
                                field: 'supComRegno',
                                width: 200,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: '업종', 
                                field: 'supComType',
                                width: 260,
                                cellStyle:{textAlign: 'left'}
                            },
                        ]
                    },
                    {
                        headerName: '금액', 
                        children:[
                            {
                                headerName: '공급가액', 
                                field: 'supAmount',
                                width: 180,
                                cellStyle:{textAlign: 'right'},
                                valueFormatter: (params) => { 
                                    return that.getNumberFormat(params.value);
                                }
                            },
                            {
                                headerName: '부가세액', 
                                field: 'taxAmount',
                                width: 180,
                                cellStyle:{textAlign: 'right'},
                                valueFormatter: (params) => { 
                                    return that.getNumberFormat(params.value);
                                }
                            },
                            {
                                headerName: '합계금액', 
                                field: 'totalAmount',
                                width: 180,
                                cellStyle:{textAlign: 'right'},
                                valueFormatter: (params) => { 
                                    return that.getNumberFormat(params.value);
                                }
                            },
                        ]
                    },
                    {
                        headerName: '세금계산서', 
                        children:[
                            {
                                headerName: '세금계산서 종류', 
                                field: 'typeCodeTxt',
                                width: 220,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                              headerName: '수정사유',
                              field: 'amendCode',
                              width: 200,
                              cellStyle:{textAlign: 'left'},
                              hide: true
                            },
                            {
                                headerName: '수정사유', 
                                field: 'amendCodeNm',
                                width: 200,
                                cellStyle:{textAlign: 'left'}
                            },
                        ]
                    },
                    {
                        headerName: '수탁사업자', 
                        children:[
                            {
                                headerName: '회사', 
                                field: 'brokerComName',
                                width: 200,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: '사업자등록번호', 
                                field: 'brokerComRegno',
                                width: 220,
                                cellStyle:{textAlign: 'left'}
                            },
                        ]
                    },
                    {
                        headerName: '공급받는 자', 
                        children:[
                            {
                                headerName: '회사', 
                                field: 'byrComName',
                                width: 240,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: '종사업장', 
                                field: 'byrBizplaceCode',
                                width: 200,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: 'E-Mail', 
                                field: 'byrEmail',
                                width: 240,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: '담당자', 
                                field: 'byrPersonName',
                                width: 180,
                                cellStyle:{textAlign: 'left'}
                            },
                            {
                                headerName: '부서', 
                                field: 'byrDeptName',
                                width: 180,
                                cellStyle:{textAlign: 'left'}
                            }
                        ]
                    },
                ]
            },
            rowDoubleClick(params){

                if(params.data.erpName === null){
                    this.$swal({
                      type: 'warning',
                      text: '공급자 등록되지 않는 거래처입니다. 거래처를 등록해주시기 바랍니다.'
                    });
                    return;
                }

                if(params.data.slipStatus == 'AR' || params.data.slipStatus == 'CR' || params.data.slipStatus == 'SC' || params.data.slipStatus == 'FR'){
                  this.$swal({ type: 'warning', html: '미처리(반려)인 경우 반려처리된' + '<br/>' + '전표내역에서 재사용하시기 바랍니다.' });
                  return false;
                }

                if(!params.data.slipStatus){
                  const that = this;

                  this.$modal.open({
                    component: EtaxTrx,
                    parent: this,
                    props: {
                      param: this.form.trxTypeNm,
                      personId: this.empDetail.personId,
                      etaxInfo: params.data
                    },
                    width: 700,
                    events: {
                      close(obj) {
                        if(obj){
                          //발생전표로 전달
                        }
                      }
                    }
                  });
                  //백버튼 생성하기 위해서 쿠키에 임의로 지정 후 스토어에 저장
                  const searchForm = {
                    searchDt: this.searchDt,
                    form: this.form
                  };
                  this.$cookie.set('searchForm', JSON.stringify(searchForm));
                  this.$store.commit('searchForm', JSON.parse(this.$cookie.get('searchForm')));
                }

                // this.goPage({grSlipNo, status, slipTypeCd, xml: {compCd, bpCd, issueDate, invSeq, dtiType}})
            },
            getDealStatCombo() {
                this.$http.get(`/api/code/combo`, {params: {groupCd: "DEAL_STAT_CD"}})
                    .then(response => {
                        this.dealStats = response.data;
                    });
            },
            getEmpDetail(){
              this.$http.get(`/api/emp/${this.$store.state.loginInfo.loginId}`)
                .then((response) => {
                  this.empDetail = response.data
                })
            },
            saveExcel() {
                var params = {
                    fileName : "전자세금계산서 수신내역"
                };
                this.gridOptions.api.exportDataAsCsv(params)
            },
            goPage(params) {
                //백버튼 생성하기 위해서 쿠키에 임의로 지정 후 스토어에 저장
                this.$cookie.set('searchForm', JSON.stringify(this.form));
                this.$store.commit('searchForm', JSON.parse(this.$cookie.get('searchForm')));

            },
            goSearch() {
                this.form.searchFrom = this.$moment(this.searchDt[0]).format('YYYY-MM-DD HH:mm:ss');
                this.form.searchTo = this.$moment(this.searchDt[1]).format('YYYY-MM-DD HH:mm:ss');

                const param = JSON.parse(JSON.stringify(this.form));

                if (!this.validation(param)) return;

                this.$store.commit('loading');
                this.$http.post(`/api/tax/taxRcvList`, param)
                    .then(response => {
                        if (response.data) {
                            this.rcvList = response.data;
                        }
                    }).catch(response => {
                        this.$swal({type: 'warning', text: response});
                    })
                    .finally(() => {
                        this.$store.commit('finish');
                    });
                    document.getElementById("open-moda").style.opacity = "0";
                    document.getElementById("open-moda").style.pointerEvents = "none";
            },
            resetSearch(){
                this.form.searchIssueId = ''
                this.form.searchStatus = '00'
                this.form.searchVendor = ''
                this.form.searchEmail = ''

                if(this.setDate != undefined && this.setDate.startDate != undefined && this.setDate.endDate != undefined){
                    // 포털에서 넘어왔을 경우
                    this.searchDt = [this.$moment(this.setDate.startDate).format('YYYY-MM-DD HH:mm:ss') , this.$moment(this.setDate.endDate).format('YYYY-MM-DD HH:mm:ss')];
                }else{
                    if(this.$route.params) {
                        // 뒤로가기로 넘어왔을 경우
                        this.setCallPageByParam();
                    } else {
                        // 그 외
                        this.searchDt = [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().format('YYYY-MM-DD HH:mm:ss')];
                    }
                }
            },
            getStartDate(){
              this.$store.commit('loading');
              this.$http.get(`/api/acctPeriod/openDate`)
              .then(response => {
                this.form.searchFrom = this.$moment(response.data,'YYYYMMDD').format('YYYY-MM-DD HH:mm:ss');
                this.form.searchTo = this.$moment(this.form.searchFrom).endOf('month').format('YYYY-MM-DD HH:mm:ss')

              }).catch(response => {
                console.log(response)
              })
              .finally(() => {
                this.$store.commit('finish');
              });
            },
            openModal() {
                document.getElementById("open-moda").style.opacity = "1";
                document.getElementById("open-moda").style.pointerEvents = "auto";
            }, 
            closeModal() {
                document.getElementById("open-moda").style.opacity = "0";
                document.getElementById("open-moda").style.pointerEvents = "none";
            },
            validation(param) {
                if (!param.searchFrom || !param.searchTo) {
                    this.$swal({type: 'warning', text: '공급일자를 입력해 주세요.'});
                    return false;
                }
                return true;
            },
            dateSetting(str) {
              switch (str) {
                case 'toDay':
                  this.searchDt = [this.$moment().format('YYYY-MM-DD HH:mm:ss'), this.$moment().format('YYYY-MM-DD HH:mm:ss')]
                  break;
                case 'crrntMnth':
                  this.searchDt = [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'), this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')]
                  break;
                case 'PrvsMnth':
                  this.searchDt = [this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss'), this.$moment().add(-1, 'month').endOf('month').format('YYYY-MM-DD HH:mm:ss')]
                  break;
              }
              // this.goSearch()
            },
            setCallPageByParam(){
              if (this.$route.params) {
                    for (const member in this.$route.params) {
                        if (this.$route.params[member]) {
                          if(member === 'searchDt') {
                              this.searchDt = this.$route.params.searchDt;
                          }
                          else {
                            this.form = this.$route.params[member]
                          }
                        }
                    }
                }
            },
        },
        beforeMount(){
            if(_.isEmpty(this.params))this.getStartDate()
            this.makeColDef()
            this.defaultColDef = { resizable: true, filter:true, sortable: true };
        },
        mounted() {
          document.title = this.title + ' - IJEAS';
            if(this.$route.query.dealStatCd) {this.form.dealStatCd = this.$route.query.dealStatCd}
            //this.form.ipDeptcode1 = this.$store.state.loginInfo.loginDeptCd;
            //this.form.ipDeptname1 = this.$store.state.loginInfo.loginDeptNm;
            this.getDealStatCombo();
            this.getEmpDetail();
            if(this.dateSet !== undefined) this.setDate = this.dateSet

            setTimeout(() => {
              this.resetSearch();
              this.goSearch();
            },500);

        },
        watch: {

        }        
    };
</script>

<style scoped>
    .gridbox {
        height: 470px !important;
    }

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
        width: 41%;
        padding-right: 40px;
    }

    .desc-content .item.desc-center {
        width: 35%;
        padding-right: 40px;
    }

    .desc-content .item.desc-right {
        width: 24%;
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

    .remove_text {
        margin-left: 0;
    }

    .contents div.gridbox_material.gridbox .xhdr {
        border-bottom: 1px solid #dfdfdf;
    }

    @media (max-width: 1580px) {
        .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
            width: 50%;
        }
    }
</style>
