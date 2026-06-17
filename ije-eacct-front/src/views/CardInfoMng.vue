<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch()">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="item search_wrap">
       <div class="search_box">
         <div class="search_title">
           <span class="search_tit">- 발급일자</span>
         </div>
         <div class="search_con">
           <!-- <div class="datepicker w-type-ymd02">
             <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />
           </div>
           <span class="wave"> ~ </span>
           <div class="datepicker w-type-ymd02">
             <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />
           </div> -->
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
                <span class="search_tit">- 발급일자</span>
              </div>
              <div class="search_con search-area">
<!--                 <div class="datepicker w-type-ymd02 w45p">-->
<!--                   <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />-->
<!--                 </div>-->
<!--                 <span class="datepicker w10p dp-ib tac"> ~ </span>-->
<!--                 <div class="datepicker w-type-ymd02 w45p">-->
<!--                   <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />-->
<!--                 </div>-->
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

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 회사</span>
                </div>
                <div class="search_con search-area">
                  <div v-if="authority==='ADMIN'" class="desc">
                    <select class="select is-fullwidth w100p" v-model="form.compCd">
                      <option value=''>==전체==</option>
                      <option
                          v-for="item in compCds"
                          :key="item.key"
                          :value="item.key"
                          v-text="item.value"/>
                    </select>
                  </div>
                  <div v-else class="desc">
                    <select class="select is-fullwidth w100p" v-model="form.compCd" disabled>
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

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 카드소유자</span>
                </div>
                <div class="search_con search-area">
                  <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.crdPssrDut" disabled>
                  <input class="input dp-ib input-bg w30p_5r" type="text" v-model="form.crdPssrDept" disabled>
                  <input class="input dp-ib input-bg w20p_5r" type="text" v-model="form.crdPssrId" disabled>
                  <input class="input dp-ib w30p" type="text" v-model="form.crdPssrNm" @input="initEmp" @keypress.enter="popEmp">
                  <button class="icon is-right" @click="popEmp(true)"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 카드상태</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth w100p" v-model="form.crdStatCd">
                    <option value=''>==전체==</option>
                    <option
                        v-for="item in crdStatCds"
                        :key="item.key"
                        :value="item.key"
                        v-text="item.value"/>
                  </select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 카드사</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth w100p" v-model="form.crdCompCd">
                    <option value=''>==전체==</option>
                    <option
                        v-for="item in crdCompCds"
                        :key="item.key"
                        :value="item.key"
                        v-text="item.value"/>
                  </select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 카드번호</span>
                </div>
                <div class="search_con search-area">
                  <input class="input Rt-M tal w100p" type="text" v-model="form.crdNo">
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 카드구분</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth w100p" v-model="form.crdFgCd">
                    <option value=''>==전체==</option>
                    <option
                        v-for="item in crdFgCds"
                        :key="item.key"
                        :value="item.key"
                        v-text="item.value"/>
                  </select>
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
            <h3 class="ico_table_name">법인카드 목록</h3>
          </div>
          <div class="btn-type1 clearfix">
            <el-button type="success" size="small" icon="el-icon-document-checked" @click="saveExcel()">엑셀 저장</el-button>
          </div>
        </div>

        <div class="grid-wrap">
          <!--                    <dhx-grid ref="grid" v-model="cardList" :config="config" @constructGridSuccessful="constructGridSuccessful"/>-->
          <ag-grid-vue ref="grid" style="width: 100%; height: 250px;" class="ag-theme-alpine grid_search_height"
                       :columnDefs="columnDefs"
                       :rowData="cardList"
                       :gridOptions="gridOptions"
                       :defaultColDef="defaultColDef"
                       :frameworkComponents="frameworkComponents"
                       @grid-ready="onGridReady"
                       @cell-clicked="onCellClicked"/>


          <ag-grid-vue
              ref="excel"
              class="ag-theme-alpine"

              :columnDefs="columnDefsExcel"
              :rowData="cardListExcel"
              :gridOptions="gridOptionsExcel"
              v-show="false"
          />
        </div>

        <div class="pagingbox">
          <button class="btn-page page-first" @click="goFirst" :disabled="page.page==0 || page.count==0"></button>
          <button class="btn-page page-prev" @click="goPre" :disabled="page.page==0 || page.count==0"></button>

          <button :class="page.page==item.value-1 ? 'page-num active' : 'page-num'"
                  v-for="item in page.pageList"
                  :value="item.value"
                  @click="goPage(item.value)">{{item.value}}</button>

          <button class="btn-page page-next" @click="goNext" :disabled="page.page==page.lastPage || page.count==0"></button>
          <button class="btn-page page-last" @click="goEnd" :disabled="page.page==page.lastPage || page.count==0"></button>
        </div>

      </div>
      <card-info ref="cardInfo" :cardDetail="cardDetail"/>
    </div>
  </div>
  <!-- //테이블 -->

  <!--팝업-->
  <!-- <b-modal :active.sync="showEmpModal" has-modal-card @receive="receiveEmp">
      <emp :param="form.crdPssrNm"></emp>
  </b-modal> -->
  <!--//팝업 -->
</template>

<script>
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import _ from 'lodash'

import DhxCalendar from '@/components/DhxCalendar.vue'
// import Emp from '@/components/Emp.vue';
import Emp from '@/components/Emp_Ag.vue';
import CardInfo from '@/components/CardInfo.vue';
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";

export default {
  name: 'CardInfoMng',
  mixins: [mixin, mixinSlip],
  components: {Emp, CardInfo, DhxCalendar},
  data() {
    return {
      // config : {
      //   columns: [
      //     {id: 'rn', type: 'cntr', align: 'center', sort: 'na', value: 'No.', width: 35},
      //     {id: 'crdNo', type: 'ro', align: 'center', sort: 'na', value: '카드번호', width: 250},
      //     {id: 'compNm', type: 'ro', align: 'center', sort: 'na', value: '회사', width: 120},
      //     {id: 'crdCompNm', type: 'ro', align: 'center', sort: 'na', value: '카드사', width: 200},
      //     {id: 'crdStatNm', type: 'ro', align: 'center', sort: 'na', value: '카드상태', width: 100},
      //     {id: 'crdPubcDt', type: 'ro', align: 'center', sort: 'na', value: '발급일자', width: 120},
      //     {id: 'crdVldYm', type: 'ron', align: 'center', sort: 'na', value: '유효기간', width: 100},
      //     {id: 'crdPlmtAmt', type: 'ron', align: 'right', sort: 'na', value: '한도금액', width: 150},
      //     {id: 'crdFgNm', type: 'ro', align: 'center', sort: 'na', value: '카드구분', width: 100},
      //     {id: 'crdPssrNm', type: 'ro', align: 'center', sort: 'na', value: '카드소유자', width: 100},
      //     {id: 'crdOln', type: 'ro', align: 'left', sort: 'na', value: '적요', width: 300},
      //   ],
      //   height: 223,
      //   enablePaging: true,
      //   pagingSize: 5,
      //   queryPage: (page) => {
      //     page = page || 0
      //     return new Promise((resolve, reject) => {
      //       let data = {
      //         contents: this.cardList,
      //         page: page,
      //         totalPages: parseInt(this.cardList.length / this.config.pagingSize) + (parseInt(this.cardList.length % this.config.pagingSize) > 0 ? 1 : 0),
      //         totalElements: 0
      //       }
      //       resolve({
      //         data: data
      //       })
      //     })
      //   },
      //   afterRefreshData(grid, data) {
      //     this.$nextTick(() => {
      //       _.forEach(data, (item, index) => {
      //         var rId = index + 1
      //
      //         grid.cells(rId, 1).setValue(this.$parent.getFormattedPureCardNumber(grid.cells(rId, 1).getValue()));
      //         if(!_.isEmpty(grid.cells(rId, 5).getValue()))grid.cells(rId, 5).setValue(this.$moment(grid.cells(rId, 5).getValue()).format('YYYY-MM-DD'));
      //         if(!_.isEmpty(grid.cells(rId, 6).getValue()))grid.cells(rId, 6).setValue(this.$moment(grid.cells(rId, 6).getValue(), 'YYYYMM').format('YYYY-MM'));
      //       })
      //     })
      //   }
      // },
      columnDefs : [],
      gridOptions : {
        enableColResize: true,
        enableFilter: true,
        animateRows: false,
        enableSorting: true
      },
      gridOptionsExcel:{},
      defaultColDef:{
        resizable: true,
        filter: true,
        sortable: true,
        editable: true,
      },
      columnDefsExcel:{},
      frameworkComponents: {
        checkboxRenderer: CheckboxCellRenderer
      },
      title: '법인카드 정보관리',
      compCds: [],
      crdCompCds: [],
      crdStatCds: [],
      crdFgCds: [],
      cardList: [],
      cardDetail: [],
      cardListExcel: [],
      showEmpModal: false,
      authority: '',
      initPage: false,
      searchDt: [this.$moment().startOf('month').format('2010-01-01 HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')],
      page:{
        page: 0,
        limit: 100,
        count: 0,
        lastPage: 0,
        pageList: []
      },
      form: {
        compCd: '',
        crdCompCd: '',
        crdStatCd: '',
        crdFgCd: '',
        crdNo: '',
        crdPssrId: '',
        crdPssrNm: '',
        crdPssrTitle: '',
        crdPssrDut: '',
        crdPssrDept: '',
        searchDtmFr: this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD'),
        searchDtmTo: this.$moment().format('YYYY-MM-DD'),
      },
    };
  },
  methods: {
    constructGridSuccessful(grid) {
      this.config.queryPage(0)
      grid.setNumberFormat('0,000', 7, '.', ',')
      grid.attachEvent('onRowSelect', (rId) => {
        let crdNo = grid.cells(rId,1).getValue();

      });
    },
    onCellClicked(params){
      const crdNo = params.data.crdNo;
      this.getcardInfo(crdNo);
    },
    onGridReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    getCompCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})
          .then(response => {
            this.compCds = response.data;
          });
    },
    getCardCompCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "CRD_COMP_CD"}})
          .then(response => {
            this.crdCompCds = response.data;
          });
    },
    getCardStatCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "CRD_STAT_CD"}})
          .then(response => {
            this.crdStatCds = response.data;
          });
    },
    getCardFgCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "CRD_FG_CD"}})
          .then(response => {
            this.crdFgCds = response.data;
          });
    },
    saveExcel() {

      const param = JSON.parse(JSON.stringify(this.form));

      this.$store.commit('loading');
      this.$http.post(`/api/card/list`, {
        compCd: param.compCd,
        searchDtmFr: param.searchDtmFr,
        searchDtmTo: param.searchDtmTo,
        crdStatCd: param.crdStatCd,
        crdNo: param.crdNo,
        crdPssrId: param.crdPssrId,
        crdCompCd: param.crdCompCd,
        crdFgCd: param.crdFgCd,
        page: 0,
        limit: this.page.count
      }).then(response => {
        if (response.data) {
          this.cardListExcel = response.data;
        }
      }).finally(() => {
        this.$store.commit('finish');
        this.gridOptionsExcel.api.exportDataAsExcel({fileName: '법인카드 정보'});
      });
    },
    newPage() {
      this.$refs.cardInfo.newPage();
    },
    goFirst(){
      this.page.page = 0
      this.goSearch()
    },
    goPre(){
      if(this.page.page > 0) {
        this.page.page = this.page.page - 1
      }
      this.goSearch()
    },
    goNext(){
      //마지막 구하기
      if(this.page.page < this.page.lastPage) {
        this.page.page = this.page.page + 1
      }
      this.goSearch()
    },
    goEnd(){
      this.page.page = this.page.lastPage
      this.goSearch()
    },
    goPage(param){
      this.page.page = param - 1
      this.goSearch()
    },
    getCount(param){
      this.$http.post(`/api/card/count`, {
        compCd: param.compCd,
        // searchDtmFr: param.searchDtmFr,
        // searchDtmTo: param.searchDtmTo,
        crdStatCd: param.crdStatCd,
        crdNo: param.crdNo,
        crdPssrId: param.crdPssrId,
        crdCompCd: param.crdCompCd,
        crdFgCd: param.crdFgCd
          }
      ).then(response => {
        this.page.count = response.data;
        this.page.lastPage = Math.ceil(response.data / 100) - 1

        this.page.pageList = []

        if(this.page.lastPage > -1){
          var count = 0;
          for(var i=Math.floor(this.page.page/10)*10; i<=this.page.lastPage; i++){
            count++;
            if(count > 10) break

            this.page.pageList.push({value: i+1})
          }
        }
      })
    },
    compareDate(value){
      if(!value){
        this.$swal({ type: 'info', text: '일자를 입력해주세요.' });
        return false;
      }
    },
    goSearch() {
      if(this.initPage){
        this.initPage = false
        this.page.page = 0
      }
      this.compareDate(this.searchDt);
      this.form.searchDtmFr = this.$moment(this.searchDt[0]).format("YYYYMMDD");
      this.form.searchDtmTo = this.$moment(this.searchDt[1]).format("YYYYMMDD");
      const param = JSON.parse(JSON.stringify(this.form));
      const stripFields = ['searchDtmFr', 'searchDtmTo','crdNo'];
      _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

      this.getCount(param);
      this.$store.commit('loading');
      this.$http.post(`/api/card/list`, {
            compCd: param.compCd,
            searchDtmFr: param.searchDtmFr,
            searchDtmTo: param.searchDtmTo,
            crdStatCd: param.crdStatCd,
            crdNo: param.crdNo,
            crdPssrId: param.crdPssrId,
            crdCompCd: param.crdCompCd,
            crdFgCd: param.crdFgCd,
            page: this.page.page,
            limit: this.page.limit
          }).then(response => {
            if (response.data) {
              this.cardList = response.data;
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
    getcardInfo(crdNo) {
      let cardNo = this.toPure(crdNo);
      if(crdNo != undefined && crdNo != '0') {
        this.$http.get(`/api/card/detail/${cardNo}`).then(response => {
          if(response.data) {
            this.cardDetail = response.data[0];
          }
        })
            .catch(({message}) => {
              console.error(message);
            });
      }
    },
    save() {
      // form validation
      if(!this.$refs.cardInfo.validation()) return;

      const cardInfo = JSON.parse(JSON.stringify(this.$refs.cardInfo.form));
      const stripFields = ['crdNo','crdPubcDt','crdVldYm','crdAbltDt','crdPlmtAmt','stlBacctNo','preCrdNo','crdUseStrDt','crdUseEndDt'];
      _.forEach(stripFields, (name) => cardInfo[name] = this.toPure(cardInfo[name]));

      this.$http.post(`/api/card/save`, cardInfo)
          .then((response) => {
            this.$swal({ type: 'info', text: '저장되었습니다.' });
            this.goSearch();
          })
          .catch((e) => {
            console.error(e);
          });
    },
    remove() {
      const crdNo = this.toPure(this.$refs.cardInfo.form.crdNo);
      const crdNo2 = this.$refs.cardInfo.form.crdNo;

      if (crdNo === '') {
        this.$swal({type: 'warning', text: '카드를 선택해주세요.'});
        return false;
      } else {
        this.$swal({
          type: 'warning',
          text: `[${crdNo2}] 법인카드를 삭제합니다. 계속 하시겠습니까?`,
          showCancelButton: true,
          confirmButtonText: '예',
          cancelButtonText: '아니오',
        }).then((result) => {
          if (result.value) {
            this.$http.delete(`/api/card/delete/${crdNo}`)
                .then(response => {
                  if (response.status === 200) {
                    this.$swal({type: 'warning', text: response.data});
                    this.$swal({ type: 'info', text: '삭제되었습니다.' });
                    this.goSearch();

                    this.$refs.cardInfo.newPageSkip();
                  }
                });
          }
        });
      }
    },
    popEmp(clear) {
      let vm = this
      this.$modal.open({
        component: Emp,
        props: {
          param: this.form.crdPssrNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveEmp(obj)
          }
        }
      })
    },
    receiveEmp(obj) {
      this.form.crdPssrId = obj.empNo;
      this.form.crdPssrNm = obj.empNm;
      this.form.crdPssrDut = obj.jobDutNm;
      this.form.crdPssrDept = obj.deptNm;
    },
    initEmp(force) {
      if (force === true) this.form.crdPssrNm = '';
      if (this.form.crdPssrNm === '') {
        this.form.crdPssrId = '';
        this.form.crdPssrDut = '';
        this.form.crdPssrDept = '';
      }
    },
    getDateFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD')
      }
    },
    makeColDef(){
      const that = this;
      this.columnDefs = [
        {
          headerName: 'No.',
          width: 60,
          editable: false,
          valueFormatter: function(params) {
            return that.page.page*100 + params.node.rowIndex + 1;
          },
          cellStyle : {'textAlign': 'center'}
        },
        {
          headerName: '카드번호',
          field: 'crdNo',
          width: 180,
          cellStyle:{textAlign: 'center'},
          editable: false,
          valueFormatter: (params) => {
            return that.getFormattedPureCardNumber(params.value);
          }
        },
        {
          headerName: '회사',
          field: 'compNm',
          width: 120,
          editable: false,
          cellStyle:{textAlign: 'left'},
        },
        {
          headerName: '카드사',
          field: 'crdCompNm',
          width: 110,
          editable: false,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '카드상태',
          field: 'crdStatNm',
          width: 110,
          editable: false,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '발급일자',
          field: 'crdPubcDt',
          width: 130,
          editable: false,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(`${params.value}`)
          }
        },
        {
          headerName: '유효기간',
          field: 'crdVldYm',
          width: 130,
          editable: false,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(`${params.value}`)
          }
        },
        {
          headerName: '한도금액',
          field: 'crdPlmtAmt',
          width: 180,
          editable: false,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {
          headerName: '카드구분',
          field: 'crdFgNm',
          width: 120,
          editable: false,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '카드소유자',
          field: 'crdPssrNm',
          width: 120,
          editable: false,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '적요',
          field: 'crdOln',
          width: 350,
          editable: false,
          cellStyle:{textAlign: 'left'}
        },
      ]


      this.columnDefsExcel = [
        {
          headerName: 'No.',
          width: 80,
          editable: false,
          valueGetter: (params) => {
            return params.node.rowIndex + 1
          },
          cellStyle : {'textAlign': 'center'}
        },
        {
          headerName: '카드번호',
          field: 'crdNo',
          width: 200,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getFormattedPureCardNumber(params.value);
          }
        },
        {
          headerName: '회사',
          field: 'compNm',
          width: 120,
          cellStyle:{textAlign: 'left'},
        },
        {
          headerName: '카드사',
          field: 'crdCompNm',
          width: 120,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '카드상태',
          field: 'crdStatNm',
          width: 120,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '발급일자',
          field: 'crdPubcDt',
          width: 150,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(`${params.value}`)
          }
        },
        {
          headerName: '유효기간',
          field: 'crdVldYm',
          width: 130,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(`${params.value}`)
          }
        },
        {
          headerName: '한도금액',
          field: 'crdPlmtAmt',
          width: 180,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          }
        },
        {
          headerName: '카드구분',
          field: 'crdFgNm',
          width: 150,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '카드소유자',
          field: 'crdPssrNm',
          width: 150,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '카드소유자 사번',
          field: 'crdPssrId',
          width: 150,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '적요',
          field: 'crdOln',
          width: 180,
          cellStyle:{textAlign: 'left'}
        },
      ]

    },
    dateSetting(str){
      switch (str) {
        case 'toDay':
          this.form.searchDtmFr = this.$moment().format('YYYYMMDD')
          this.form.searchTo = this.$moment().format('YYYYMMDD')
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
  },
  mounted() {
    document.title = this.title + ' - IJEAS';
    this.form.compCd = this.$store.state.loginInfo.compCd;
    this.authority = this.$store.state.loginInfo.authorities[0].roleCd;
    this.getCompCdCombo();
    this.getCardCompCdCombo();
    this.getCardStatCdCombo();
    this.getCardFgCdCombo();
    this.goSearch();
  },
  beforeMount(){
    this.makeColDef();
  },
  watch: {
    'form': {
      immediate: false,
      deep: true,
      handler() {
        this.initPage = true
      }
    },
  },
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
  height: 50px;
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
  padding-left: 80px;
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

@media (max-width: 1580px) {
  .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
    width: 50%;
  }
}
</style>

