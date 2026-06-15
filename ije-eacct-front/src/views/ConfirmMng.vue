<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch()">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-document-add" @click="openConfirmPop('I')">신규</el-button>
        <el-button size="large" type="danger" icon="el-icon-delete" @click="deleteRow('I')">삭제</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="item search_wrap">
        <div class="search_box">
          <div class="search_title">
            <span class="search_tit">부서</span>
          </div>
          <div class="search_con">
            <div class="td-s-thumb search-area" style="width: 300px">
              <input class="input input-bg" type="text" style="width:100px;" v-model="form.deptCd" disabled>
              <!--              <input class="input input-bg" type="text" v-show="false" v-model="form.deptNm">-->
              <div class="ip-box ip-box-w02" style="width:120px;">
                <input class="input" type="text" v-model="form.deptNm" @input="initCctr" @keypress.enter="popCctr">
                <button class="icon is-right" @click="popCctr()"><i class="fas fa-search"></i>
                </button>
              </div>
            </div>
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
                  <span class="search_tit">검인 순서</span>
                </div>
                <div class="search_con search-area">
                  <b-select class="select is-fullwidth w100p" v-model="form.confirmSeq">
                    <option value=''>==전체==</option>
                    <option
                        v-for="item in confirmSeqList"
                        :key="item.key"
                        :value="item.key"
                        v-text="item.value"/>
                  </b-select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">검인자</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg w30p_5r" type="text" v-model="form.confirmUserId" disabled>
                  <!--                  <input class="input input-bg w30p_5r" type="text" v-show="false" v-model="form.confirmUserId" >-->
                  <div v-if="authority==='ADMIN'" class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.confirmUserNm" @input="initEmp" @keypress.enter="popEmp">
                    <button class="icon is-right" @click="popEmp(true)"><i class="fas fa-search"></i>
                    </button>
                  </div>
                  <div v-else class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.confirmUserNm" @input="initEmp" @keypress.enter="popEmp">
                    <button class="icon is-right" @click="popEmp(true)"><i class="fas fa-search"></i>
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
    </div>
    <!-- //검색조건 영역 -->

    <!-- 테이블 -->
    <div class="table-area">

      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">검인관리내역</h3>
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

                       @row-selected="onRowSelected"
                       @grid-ready="onGridReady"
                       @rowDoubleClicked="rowDoubleClick"
                       @cell-clicked="onCellClicked"
          />
          <!--                                          ag grid option explanation  -->


          <!--          <ag-grid-vue ref="grid"                                        Grid 객체 정의 -->
          <!--                       style="width: 100%;"                              Grid CSS style 설정 -->
          <!--                       class="ag-theme-alpine grid_search_height"        테마 적용을 위한 class 정의 -->
          <!--                       :columnDefs="columnDefs"                          Grid 컬럼 정의 -->
          <!--                       :rowData="data"                                   Grid Data -->
          <!--                       :gridOptions="gridOptions"                        Grid Option (ex. resize,animate,sorting) -->
          <!--                       :defaultColDef="defaultColDef"                    Grid 컬럼 기본 정의 -->
          <!--                       :frameworkComponents="frameworkComponents"        Grid 프레임워크 컴포넌트 (ex. checkBox , radio button) -->
          <!--                       :enableRangeSelection="true"                      정렬 기능 강제 여부 -->
          <!--                       :suppressMaxRenderedRowRestriction="true"         최대 렌더링 행 제한 억제 -->
          <!--                       :suppressColumnVirtualisation="true"              열 가상화 여부 -->
          <!--                       :suppressRowVirtualisation="true"                 행 가상화 여부 -->
          <!--                       :suppress-row-click-selection="false"             행 선택 여부 -->
          <!--                       :context="context"                                Grid CallBack 기능 -->
          <!--                       @grid-ready="onGridReady"                         Grid Open 이후 동작 -->
          <!--                       @rowDoubleClicked="rowDoubleClick"                행 더블 클릭 -->
          <!--                       @cell-clicked="onCellClicked"                     셀 클릭 -->
          <!--          />-->

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
import Cctr from '@/components/Cctr_Ag.vue'
import Emp from '@/components/Emp_Ag.vue'
import ConfirmPop from '@/components/ConfirmPop.vue'


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
        // resizable: true,
        // filter: true,
        // sortable: true,
        // editable: true,
        // flex: 1,
      },
      frameworkComponents: null,
      context: {
      },
      gridApi: null,
      columnApi: null,
      title: '검인관리',
      slipTypes: [],
      slipStats: [],
      data: [],
      evdYnList: [{'key': 'N', 'value' : '미첨부'}, {'key': 'Y', 'value' : '첨부'}],
      confirmSeqList: [{'key': '1', 'value' : '1'}, {'key': '2', 'value' : '2'}, {'key': '3', 'value' : '3'}, {'key': '4', 'value' : '4'}, {'key': '5', 'value' : '5'}, {'key': '6', 'value' : '6'}, {'key': '7', 'value' : '7'}, {'key': '8', 'value' : '8'}, {'key': '9', 'value' : '9'}, {'key': '10', 'value' : '10'}],
      tempData: [],
      authority: '',
      chkYn:false,
      form: {
        deptCd: "",
        confirmUserId: "",
        confirmSeq: "",
        confirmStartAmt : "",
        confirmEndAmt : "",
        remark : ""
        // searchFr:this.$moment().startOf('month').format('YYYY-MM-DD'),
        // searchTo: this.$moment().endOf('month').format('YYYY-MM-DD'),
      },
      showCctrModal: false,
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
          headerName: ''
          , headerCheckboxSelection: true
          , checkboxSelection: true
          , field:'regYn'
          , width : 80
          , cellStyle:{textAlign: 'center'}
          , editable: false
        },
        {
          headerName: '부서코드',
          field: 'deptCd',
          width: 120,
          cellStyle:{textAlign: 'center'},
          editable: false
        },
        {
          headerName: '부서명',
          field: 'deptNm',
          width: 150,
          cellStyle:{textAlign: 'left'},
          editable: false
        },
        {
          headerName: '검인순서',
          field: 'confirmSeq',
          width: 100,
          cellStyle:{textAlign: 'center'},
          editable: false
        },
        {
          headerName: '검인자사번',
          field: 'confirmUserId',
          width: 150,
          cellStyle:{textAlign: 'center'},
          editable: false
        },
        {
          headerName: '검인자',
          field: 'confirmUserNm',
          width: 100,
          cellStyle:{textAlign: 'center'},
          editable: false
        },
        {
          headerName: '검인기준시작금액',
          field: 'confirmStartAmt',
          width: 180,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          },
          editable: false
        },
        {
          headerName: '검인기준종료금액',
          field: 'confirmEndAmt',
          width: 180,
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return that.getNumberFormat(params.value);
          },
          editable: false
        },
        {
          headerName: '비고',
          field: 'remark',
          width: 300,
          cellStyle:{textAlign: 'left'},
          editable: false
        },
        {
          headerName: '수정일자',
          field: 'chgDtm',
          width: 150,
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {
            return that.getDateFormat(`${params.value}`)
          },
          editable: false
        },
        {
          headerName: '수정자',
          field: 'chgNm',
          width: 100,
          cellStyle:{textAlign: 'center'},
          editable: false
        },
      ]
    },
    onCellClicked(params) {

    },
    rowDoubleClick(params){

      let deptCd = params.data.deptCd;
      let deptNm = params.data.deptNm;
      let confirmUserId = params.data.confirmUserId;
      let confirmUserNm = params.data.confirmUserNm;
      let confirmSeq = params.data.confirmSeq;
      let confirmStartAmt = params.data.confirmStartAmt;
      let confirmEndAmt = params.data.confirmEndAmt;
      let remark = params.data.remark;

      let list = {
        deptCd: deptCd,
        deptNm: deptNm,
        confirmUserId: confirmUserId,
        confirmUserNm: confirmUserNm,
        confirmSeq: confirmSeq,
        confirmStartAmt : confirmStartAmt,
        confirmEndAmt : confirmEndAmt,
        remark : remark
      };

      this.openConfirmPop('U',list);
    },
    openConfirmPop(flag,list){

      if(this.isEmpty(list) == ""){
        list = {
          deptCd:"",
          deptNm:"",
          confirmUserId:"",
          confirmUserNm:"",
          confirmSeq:"",
          confirmStartAmt : "",
          confirmEndAmt : "",
          remark : ""
        };
      }

      this.$modal.open({
        component: ConfirmPop,
        parent: this,
        props: {
          title: "검인처리",
          data : list,
          flag : flag,
          deptCd : list.deptCd,
          userID : list.confirmUserId,
          seq : list.confirmSeq
        },
        width: 1600,
        events: {
          close(obj){
            console.log(obj)
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
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
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
    goSearch() {

      this.$store.commit('loading');

      this.$http.post(`/api/confirm/list`,this.form)
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
    // goTest() {
    //
    //   this.$store.commit('loading');
    //
    //
    //   this.$http.get(`/api/card/use/info/57030707`).then(response => {
    //     if(response.data) {
    //       console.log(response.data);
    //     }
    //   })
    //   .catch(({message}) => {
    //     console.error(message);
    //   }).finally(() => {
    //     this.$store.commit('finish');
    //   });
    //
    //   //  let form = {
    //   //     currencyCd : "KRW",
    //   //     vendorCd: "1096833",
    //   //     trxTypeCd: "AR",
    //   //     searchCd: "",
    //   //     searchNm: "",
    //   //     deptCd: "11041",
    //   //     compCd: "81"
    //   // };
    //   //
    //   // this.$http.post(`/api/gl/term/list`,form)
    //   //     .then(response => {
    //   //       if (response.data) {
    //   //         console.log(response.data)
    //   //       }
    //   //     }).catch(response => {
    //   //   // TODO::Error Handling
    //   //   return response
    //   // }).finally(() => {
    //   //   this.$store.commit('finish');
    //   // });
    //
    //   //  let form = [
    //   //    {
    //   //      "rowId": "AABD5kAAXAAJ57fAAD",
    //   //      "prepayInvoiceId": 4781291,
    //   //      "prepayLineNumber": 1,
    //   //      "acctCode": "1115560",
    //   //      "acctDesc": "선급금[일반]",
    //   //      "prepayAmountRemainingErp": 980000,
    //   //      "prepayAmountRemaining": 979000,
    //   //      "lineAmount": 1000000,
    //   //      "accountingDate": "2023-07-18T00:00:00",
    //   //      "periodName": "202307",
    //   //      "setOfBooksId": 2021,
    //   //      "description": "선급금 테스트입니다.:47812911",
    //   //      "poLineLocationId": null,
    //   //      "poDistributionId": null,
    //   //      "rcvTransactionId": null,
    //   //      "orgId": 81,
    //   //      "ledgerId" : 2021,
    //   //      "slipHeaderId" : 256487,
    //   //      "prepayInvoiceNumber": "SPIE2023103100003",
    //   //      "invoiceDate": "2023-07-18T00:00:00",
    //   //      "invoiceAmount": 1000000,
    //   //      "integrationVendorNum": "1096833",
    //   //      "vendorId": 863854,
    //   //      "vendorSiteId": 863684,
    //   //      "invoiceCurrencyCode": "KRW",
    //   //      "paymentCurrencyCode": "KRW",
    //   //      "paymentCrossRate": 1,
    //   //      "poHeaderId": null,
    //   //      "poNumber": null,
    //   //      "vendorName": "일진씨앤에스 주식회사_06",
    //   //      "vendorNumber": "1189203",
    //   //      "vendorSiteCode": "구매업체",
    //   //      "receiptNumber": null,
    //   //      "earliestSettlementDate": "2023-10-31T13:59:18",
    //   //      "source": "SP",
    //   //      "taxRateCode": null,
    //   //      "chk": true
    //   //    }
    //   //  ]
    //   //
    //   // this.$http.post(`/api/prepayment/advanced/save`,form)
    //   //     .then(response => {
    //   //       if (response.data) {
    //   //         console.log(response.data)
    //   //       }
    //   //     }).catch(response => {
    //   //   // TODO::Error Handling
    //   //   return response
    //   // }).finally(() => {
    //   //   this.$store.commit('finish');
    //   // });
    //
    //
    //   // this.$http.get(`/api/acctPeriod/open/close/date`)
    //   //     .then(response => {
    //   //       if (response.data) {
    //   //         console.log(response.data)
    //   //       }
    //   //     }).catch(response => {
    //   //   // TODO::Error Handling
    //   //   return response
    //   // }).finally(() => {
    //   //   this.$store.commit('finish');
    //   // });
    //
    // },
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
    resetSearch(){
      this.form.deptCd = "";
      this.form.confirmUserId = "";
      this.form.confirmUserNm = "";
      this.form.confirmSeq = "";
      this.form.remark = "";
    },
    validation(param) {
      if (!param.searchFr || !param.searchTo) {
        this.$swal({type: 'warning', text: 'GL일자를 입력해 주세요.'});
        return false;
      }
      return true;
    },
    popCctr(flag) {
      let vm = this
      this.$modal.open({
        component: Cctr,
        props: {
          param: this.form.deptNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveCctr(obj)
          }
        }
      })
    },
    popEmp(clear) {
      let vm = this
      this.$modal.open({
        component: Emp,
        props: {
          param: this.form.confirmUserNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveEmp(obj)
            vm.$forceUpdate();
          }
        }
      })
    },
    receiveCctr(obj) {
      this.form.deptCd = obj.deptCd;
      this.form.deptNm = obj.deptNm;
    },
    receiveEmp(obj) {
      this.form.confirmUserId = obj.empNo;
      this.form.confirmUserNm = obj.empNm;
      this.$forceUpdate();
    },
    initCctr(force) {
      if (force === true) this.form.deptNm = '';
      if (this.form.deptNm === '') this.form.deptCd = '';
    },
    initEmp(force) {
      if (force === true) this.form.confirmUserNm = '';
      if (this.form.confirmUserNm === '') this.form.confirmUserId = '';
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
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.data[rowIdx].regYn = params.node.isSelected();
    }
  },
  beforeMount(){
    const that = this;
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

  popCctr(){
    const that  = this;

    this.$modal.open({
      width: 800,
      component: Cctr,
      parent: this,
      props: {
        param: this.form.cctrNm
      },
      events: {
        close(data) {
          that.form.cctrCd = data.cctrCd
          that.form.cctrNm = data.cctrNm
        },
      }
    })
  },
};
</script>

<style scoped>
.control.select.is-fullwidth {
  width: 55%;
}
</style>
