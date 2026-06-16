<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch()">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-document-add" @click="openDelegatePop('I')">신규</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
        <div class="form-group">
          <label class="control-label-req" id="standardMonth">위임일자</label>
          <div class="form-input">
<!--            <div class="datepicker w-type-ymd">-->
<!--               <dhx-calendar class="input" v-model="form.startDate"/>-->
<!--            </div>-->
<!--            <span> ~ </span>-->
<!--            <div class="datepicker w-type-ymd">-->
<!--              <dhx-calendar class="input" v-model="form.endDate"/>-->
<!--            </div>-->
            <el-date-picker
                v-model="searchDt"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="~"
                start-placeholder="시작일"
                end-placeholder="종료일">
            </el-date-picker>
          </div>
        </div>

        <div class="form-group">
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
                  <span class="search_tit">위임일자</span>
                </div>
                <div class="search_con search-area">
                  <div class="form-input">
<!--                    <div class="datepicker w-type-ymd">-->
<!--                      <dhx-calendar class="input" v-model="form.startDate"/>-->
<!--                    </div>-->
<!--                    <div class="datepicker w-type-ymd">-->
<!--                      <dhx-calendar class="input" v-model="form.endDate"/>-->
<!--                    </div>-->
                    <el-date-picker
                        v-model="searchDt"
                        type="daterange"
                        align="right"
                        unlink-panels
                        range-separator="~"
                        start-placeholder="시작일"
                        end-placeholder="종료일">
                    </el-date-picker>
                  </div>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">위임상태</span>
                </div>
                <div class="search_con search-area">
                  <b-select class="select is-fullwidth w100p" v-model="form.delegateStatCd">
                    <option value=''>==전체==</option>
                    <option
                        v-for="item in delegateStatCdList"
                        :key="item.key"
                        :value="item.key"
                        v-text="item.value"/>
                  </b-select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">위임자</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg w30p_5r" type="text" v-model="form.giveUserId" disabled>
                  <div v-if="authority==='ADMIN'" class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.giveUserNm" @input="initGiver" @keypress.enter="popEmp('g')">
                    <button class="icon is-right" @click="popEmp('g')"><i class="fas fa-search"></i>
                    </button>
                  </div>
                  <div v-else class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.giveUserNm" @input="initGiver" @keypress.enter="popEmp('g')">
                    <button class="icon is-right" @click="popEmp('g')"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">수임자</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg w30p_5r" type="text" v-model="form.receiveUserId" disabled>
<!--                  <input class="input input-bg w30p_5r" type="text" v-show="false" v-model="form.receiveUserId" >-->
                  <div v-if="authority==='ADMIN'" class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.receiveUserNm" @input="initReceiver" @keypress.enter="popEmp('r')">
                    <button class="icon is-right" @click="popEmp('r')"><i class="fas fa-search"></i>
                    </button>
                  </div>
                  <div v-else class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.receiveUserNm" @input="initReceiver" @keypress.enter="popEmp('r')">
                    <button class="icon is-right" @click="popEmp('r')"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>

            </div>

            <div class="modal-window-bottom">
              <ul>
                <li>
                  <button class="bt_blue_s" @click="goSearch()">검색</button>
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
    <!-- //검색조건 영역 -->

    <!-- 테이블 -->
    <div class="table-area">

      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">위임관리내역</h3>
          </div>
          <div class="table-name">
            <h3 style='color : #CC3D3D'>{{selTotAmt}}</h3>
          </div>
        </div>

        <div class="grid-wrap mt10">
          <ag-grid-vue ref="grid"
                       style="width: 100%;"
                       class="ag-theme-alpine grid_search_height"

                       :columnDefs="columnDefs"
                       :rowData="data"
                       :gridOptions="gridOptions"
                       :defaultColDef="defaultColDef"
                       :frameworkComponents="frameworkComponents"
                       :suppressRowClickSelection="true"

                       rowSelection="multiple"

                       @grid-ready="onGridReady"
                       @rowDoubleClicked="rowDoubleClick"
                       @cell-clicked="onCellClicked"
          />

        </div>
      </div>

    </div>
    <!-- //테이블 -->

  </div>
</template>

<script>
// import Vue from 'vue'
import mixin from '@/mixin'
import mixinSlip from '@/mixin/slip'
import _ from 'lodash'

import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import DhxCalendar from '@/components/DhxCalendar.vue'
//팝업
import Emp from '@/components/Emp_Ag.vue'
import DelegatePop from '@/components/DelegatePop.vue'


import { AgGridVue } from 'ag-grid-vue'
// import AgDhxCalendar from "@/components/agGrid/AgDhxCalendar.vue";

export default {
  name: 'payrollSlipLst',
  mixins: [mixin, mixinSlip],
  components: { Emp, DhxCalendar, AgGridVue},
  data() {
    return {
      columnDefs : [],
      gridOptions: {
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
      defaultColDef: {
        resizable: true,
        filter: true,
        sortable: true,
        editable: true
      },
      frameworkComponents: null,
      context: {
        headerAllCheckEvent: null
      },
      gridApi: null,
      columnApi: null,
      title: '위임관리',
      slipTypes: [],
      slipStats: [],
      data: [],
      evdYnList: [{'key': 'N', 'value' : '미첨부'}, {'key': 'Y', 'value' : '첨부'}],
      delegateStatCdList: [{'key': '1', 'value' : '위임'}, {'key': '2', 'value' : '해제'}],
      tempData: [],
      authority: '',
      chkYn:false,
      searchDt: [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')],
      form: {
        compCd : "",
        giveUserId : "",
        receiveUserId : "",
        delegateSeq : "",
        delegateStatCd : "",
        startDate : this.$moment().startOf('month').format('YYYYMMDD'),
        endDate : this.$moment().endOf('month').format('YYYYMMDD'),
        remark : "",
        regId : "",
        regDtm : "",
        chgId : "",
        chgDtm : "",
        modGiveUserId : "",
        modReceiveUserId : "",
      },
      showEmpModal: false,
      showCustomerModal: false,
      result : [],
      delChkList: [],
      rowIdx: '',
      selTotAmt: '',
      objectPopup: []
    };
  },
  methods: {
    onGridReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;

      this.makeColDef()
      this.goSearch();
    },
    makeColDef(){
      const that = this;
      this.columnDefs = [
        {
          headerName: '위임자',
          field: 'giveUserNm',
          width: 100,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '위임자사번',
          field: 'giveUserId',
          hide:true
        },
        {
          headerName: '위임시작일자',
          field: 'startDate',
          width: 150,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(`${params.value}`)
          }
        },
        {
          headerName: '위임종료일자',
          field: 'endDate',
          width: 150,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(`${params.value}`)
          }
        },
        {
          headerName: '수입자명',
          field: 'receiveUserNm',
          width: 150,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '수입자사번',
          field: 'receiveUserId',
          hide: true
        },
        {
          headerName: '비고',
          field: 'remark',
          width: 600,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '위임상태',
          field: 'delegateStatCd',
          width: 180,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDelegateStat(`${params.value}`)
          }
        },
        {
          headerName: '수정일자',
          field: 'chgDtm',
          width: 150,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(`${params.value}`)
          }
        },
        {
          headerName: '수정자',
          field: 'chgNm',
          width: 100,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName: '수정자',
          field: 'chgId',
          hide: true
        },
      ]
    },
    onCellClicked(params) {

    },
    rowDoubleClick(params){

        if(params.data.delegateStatCd !== '1'){
          this.$swal({ type: 'warning', text: '위임이 해제된 건입니다.' });
          return false;
        }

        let giveUserId = params.data.giveUserId;
        let giveUserNm = params.data.giveUserNm;
        let receiveUserId = params.data.receiveUserId;
        let receiveUserNm = params.data.receiveUserNm;
        let delegateSeq = params.data.delegateSeq;
        let startDate = params.data.startDate;
        let endDate = params.data.endDate;
        let remark = params.data.remark;
        let delegateStatCd = params.data.delegateStatCd;

        let list = {
          giveUserId : giveUserId,
          giveUserNm : giveUserNm,
          receiveUserId : receiveUserId,
          receiveUserNm : receiveUserNm,
          delegateSeq : delegateSeq,
          remark : remark,
          startDate : startDate,
          endDate : endDate,
          delegateStatCd : delegateStatCd,
        };


        this.openDelegatePop('U',list);

    },
    openDelegatePop(flag,list){
      const vm = this;
      if(this.isEmpty(list) == ""){
        list = {
          compCd : "",
          giveUserId : "",
          receiveUserId : "",
          delegateSeq : "",
          delegateStatCd : "",
          startDate : this.$moment().startOf('month').format('YYYYMMDD'),
          endDate : this.$moment().endOf('month').format('YYYYMMDD'),
          remark : "",
        };
      }

      this.$modal.open({
        component: DelegatePop,
        parent: this,
        props: {
          title: "위임처리",
          data : list,
          flag : flag,
          giveUserId : list.giveUserId,
          receiveUserId : list.receiveUserId,
          seq : list.delegateSeq,
          delegateStatCd : list.delegateStatCd
        },
        width: 1200,
        events: {
          close(){
            vm.goSearch();
          }
        }
      });

    },
    cellValueChange(params){
      if(params.data.chk){
        this.delChkList.push(params.data)
      }
    },
    mainGridRefresh(idx) {
      var rows = [];
      var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
      rows.push(rowNode);
      this.gridApi.redrawRows({ rowNodes: rows });
    },
    getDateFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD')
      }
    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD hh:mm:ss')
      }
    },
    getDelegateStat(val) {
      if (val) {
        return val === '1' ? '위임' : '해제';
      }
    },
    getSlipTypeCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "SLIP_TYPE_CD"}})
          .then(response => {
            this.slipTypes = response.data;
          });
    },
    isEmpty(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return ""
      }else{
        return value
      }
    },
    compareDate(value){
      if(!value){
        this.$swal({ type: 'info', text: '위임일자를 입력해주세요.' });
        return false;
      }
    },
    goSearch() {

      this.compareDate(this.searchDt);
      this.form.startDate = this.$moment(this.searchDt[0]).format("YYYYMMDD");
      this.form.endDate = this.$moment(this.searchDt[1]).format("YYYYMMDD");

      this.$store.commit('loading');

      this.$http.post(`/api/delegate/list`,this.form)
          .then(response => {
            if (response.data) {
              this.data = response.data;
              this.closeModal();
            }
          }).catch(response => {
        // TODO::Error Handling
        return response
      }).finally(() => {
        this.$store.commit('finish');
      });

    },
    deleteRow() {
      let vm = this;
      // let list = this.data.filter(v => v.regYn)
      const list = this.gridApi.getSelectedRows();
      vm.result = [];

      if(list.length > 0){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;
            this.$http.post(`/api/confirm/delete`, vm.result)
                .then((response) => {
                  this.$swal({ type: 'success', text: '검인 순서를 삭제 하였습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.goSearch();
                        }
                      });
                })
                .catch((e) => {
                  this.$swal({ type: 'success', text: '검인 순서를 삭제 실패 하였습니다.' })
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
          }
        })
      } else {
        this.$swal({ type: 'info', text: '삭제할 항목을 선택하세요.' });
      }
    },
    openModal() {
      document.getElementById("open-moda").style.opacity = "1";
      document.getElementById("open-moda").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-moda").style.opacity = "0";
      document.getElementById("open-moda").style.pointerEvents = "none";
    },
    resetSearch() {
      this.form.giveUserId = "";
      this.form.giveUserNm = "";
      this.form.receiveUserId = "";
      this.form.receiveUserNm = "";
      this.form.delegateStatCd = "";
      this.form.startDate = this.$moment().startOf('month').format('YYYYMMDD');
      this.form.endDate = this.$moment().endOf('month').format('YYYYMMDD');
    },
    validation(param) {
      if (!param.searchFr || !param.searchTo) {
        this.$swal({type: 'warning', text: 'GL일자를 입력해 주세요.'});
        return false;
      }
      return true;
    },
    popEmp(flag) {
      let vm = this

      let userNm = "";

      if(flag === 'g'){
        userNm = this.form.giveUserNm;
      }else{
        userNm = this.form.receiveUserNm;
      }

      this.$modal.open({
        component: Emp,
        props: {
          param: userNm
        },
        parent: this,
        events: {
          close(obj) {
            if(flag === 'g'){
              vm.receiveGiver(obj)
            }else{
              vm.receiveReceiver(obj)
            }
            vm.$forceUpdate();
          }
        }
      })
    },
    receiveGiver(obj) {
      this.form.giveUserId = obj.empNo;
      this.form.giveUserNm = obj.empNm;
    },
    receiveReceiver(obj) {
      this.form.receiveUserId = obj.empNo;
      this.form.receiveUserNm = obj.empNm;
    },
    initGiver(force) {
      if (force === true) this.form.giveUserNm = '';
      if (this.form.giveUserNm === '')this.form.giveUserId = '';
    },
    initReceiver(force) {
      if (force === true) this.form.receiveUserNm = '';
      if (this.form.receiveUserNm === '')this.form.receiveUserId = '';
    },
    dateSetting(str){
      switch (str) {
        case 'toDay':
          this.form.searchFr = this.$moment().format('YYYYMMDD')
          this.form.searchTo = this.$moment().format('YYYYMMDD')
          break;
        case 'crrntMnth':
          this.form.searchFr = this.$moment().startOf('month').format('YYYYMMDD')
          this.form.searchTo = this.$moment().endOf('month').format('YYYYMMDD')
          break;
        case 'PrvsMnth':
          this.form.searchFr = this.$moment().add(-1, 'month').startOf('month').format('YYYYMMDD')
          this.form.searchTo = this.$moment().add(-1, 'month').endOf('month').format('YYYYMMDD')
          break;
      }
      // this.goSearch()
    }
  },
  beforeMount(){
    const that = this;
    this.defaultColDef = { resizable: true, filter:true, sortable: true };
    this.frameworkComponents = {//그리드에서 사용할 외부 comp 등록
      checkboxRenderer: CheckboxCellRenderer,
    };
  },
  mounted() {

  },
  destroyed() {
    if(this.objectPopup.length > 0){
      for (const item of this.objectPopup) {
        item.close()
      }
    }
  },
};
</script>

<style scoped>
.control.select.is-fullwidth {
  width: 55%;
}
</style>
