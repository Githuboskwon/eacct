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
          <div class="search_title" style="width: 15%">
            <span class="search_tit">지급조건</span>
          </div>
          <div class="search_con">
            <div class="td-s-thumb search-area" style="width: 300px">
              <input class="input input-bg" type="text" style="width:100px;" v-model="form.termId" disabled>
              <div class="ip-box ip-box-w02" style="width:180px;">
                <input class="input" type="text" v-model="form.termNm" @input="initTerm" @keypress.enter="popTerm">
                <button class="icon is-right" @click="popTerm()"><i class="fas fa-search"></i>
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="search_box">
          <div class="search_title" style="width: 10%">
            <span class="search_tit">부서</span>
          </div>
          <div class="search_con">
            <div class="td-s-thumb search-area" style="width: 300px">
              <input class="input input-bg" type="text" style="width:100px;" v-model="form.deptCd" disabled>
              <div class="ip-box ip-box-w02" style="width:180px;">
                <input class="input" type="text" v-model="form.deptNm" @input="initCctr" @keypress.enter="popCctr">
                <button class="icon is-right" @click="popCctr()"><i class="fas fa-search"></i>
                </button>
              </div>
            </div>
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
            <h3 class="ico_table_name">결제조건 등록내역</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteRow()">행삭제</el-button>
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

                       @cell-value-changed="cellValueChange"
                       @row-selected="onRowSelected"
                       @grid-ready="onGridReady"
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
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
//팝업
import Cctr from '@/components/Cctr_Ag.vue'
import GLTermsModal from '@/components/accrualSlip/Modals/GLTermsModal.vue'; /** 결제조건 */


import { AgGridVue } from 'ag-grid-vue'
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn";
// import AgDhxCalendar from "@/components/agGrid/AgDhxCalendar.vue";

export default {
  name: 'payrollSlipLst',
  mixins: [mixin, mixinSlip],
  components: {AgGridVue},
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
      title: '결제조건 등록관리',
      slipTypes: [],
      slipStats: [],
      data: [],
      tempData: [],
      authority: '',
      chkYn:false,
      form: {
        deptCd: "",
        deptNm: "",
        termId: "",
        termNm: "",
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
          , width : 60
          , cellStyle:{textAlign: 'center', color: 'transparent'}
          , editable: false
        },
        {
          headerName: '구분',
          field: 'trxType',
          width: 70,
          cellStyle:{textAlign: 'center'},
          editable: false
        },
        {
          headerName: '지급코드',
          field: 'termId',
          width: 100,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'schBtn',
          cellRendererParams(params) {
            return {
              funcNm: 'updateTermItem',
              // valFuncNm: 'updateTermItemVal',
              disable : params.node.data.insertYn !== 'Y' ? true : false,
            }
          },
          editable: false
        },
        {
          headerName: '지급조건',
          field: 'name',
          width: 250,
          cellStyle:{textAlign: 'left'},
          editable: false
        },
        {
          headerName: '내용',
          field: 'description',
          width: 250,
          cellStyle:{textAlign: 'left'},
          editable: false
        },
        {
          headerName: '부서코드',
          field: 'deptCd',
          width: 120,
          cellStyle:{textAlign: 'center' , 'background-color': '#fdfdd4'},
          cellRenderer: 'schBtn',
          cellRendererParams(params) {
            return {
              funcNm: 'updateCctrItem',
              // valFuncNm: 'updateTermItemVal',
              disable : params.node.data.insertYn !== 'Y' && params.node.data.dtChangeFlag === 'ALL' ? true : false,
            }
          },
          editable: false
        },
        {
          headerName: '부서',
          field: 'deptNm',
          width: 180,
          cellStyle:{textAlign: 'left' , 'background-color': '#fdfdd4'},
          editable: false
        },
        {
          headerName: '결제예정일 변경여부',
          field: 'dtChangeFlag',
          width: 110,
          cellRenderer: 'select',
          cellRendererParams: {
            options : [{'key': 'ALL', 'value' : 'ALL'}, {'key': 'Y', 'value' : 'Y'}, {'key': 'N', 'value' : 'N'}],
            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
          },
          cellStyle:{textAlign: 'center' , 'background-color': '#fdfdd4'},
          // editable: false
        },
        {
          headerName: '구매포탈 사용여부',
          field: 'ptEnabledFlag',
          width: 100,
          cellRenderer: 'select',
          cellRendererParams: {
            options : [{'key': 'Y', 'value' : 'Y'}, {'key': 'N', 'value' : 'N'}],
            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
          },
          cellStyle:{textAlign: 'center' , 'background-color': '#fdfdd4'},
        },
        {
          headerName: '전자전표 사용여부',
          field: 'eaEnabledFlag',
          width: 100,
          cellRenderer: 'select',
          cellRendererParams: {
            options : [{'key': 'Y', 'value' : 'Y'}, {'key': 'N', 'value' : 'N'}],
            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
          },
          cellStyle:{textAlign: 'center' , 'background-color': '#fdfdd4'},
        },
        {
          headerName: '수정일자',
          field: 'chgDtm',
          width: 150,
          cellStyle:{textAlign: 'center' , 'background-color': '#fdfdd4'},
          valueFormatter: (params) => {
            return params.node.data.insertYn !== 'Y' ? that.getDateFormat(`${params.value}`) : '' ;
          },
          editable: false
        },
        {
          headerName: '수정자',
          field: 'chgNm',
          width: 100,
          cellStyle:{textAlign: 'center' , 'background-color': '#fdfdd4'},
          editable: false
        },
        {
          headerName: '국가코드',
          field: 'currencyType',
          hide: true,
        },
        {
          headerName: '거래유형',
          field: 'paymentMethod',
          hide: true,
        },
        {
          headerName: 'vendorAcctCheck',
          field: 'vendorAcctCheck',
          hide: true,
        },
        {
          headerName: 'trxTypeCd',
          field: 'trxTypeCd',
          hide: true,
        },
        {
          headerName: 'trxTypeNm',
          field: 'trxTypeNm',
          hide: true,
        },
        {
          headerName: '기존부서코드',
          field: 'oriDeptCd',
          hide: true,
        },
        {
          headerName: '기존부서',
          field: 'oriDeptNm',
          hide: true,
        },
      ]
    },
    onCellClicked(params) {

    },
    addRow() {
      // let index = this.data.length;
      this.data.unshift({
        insertYn : 'Y',
        termId : '',
        deptCd : '',
        dtChangeFlag : '',
        ptEnabledFlag : '',
        eaEnabledFlag : '',
      });
    },
    deleteRow() {
      let vm = this;

      const list = this.gridApi.getSelectedRows().filter(f => f.insertYn !== 'Y');
      const nList = this.gridApi.getSelectedRows().filter(f => f.insertYn === 'Y');

      nList.forEach((x)=>{
        let index = x.rowIdx;
        this.data.splice(index,1);
      })

      if(list.length === 0 && nList.length > 0){
        // 저장 하지 않은 값은 바로 삭제
      }else
      if(list.length > 0){
        this.$swal({
          type: 'question',
          html: '선택한 항목을 삭제 하시겠습니까?' + '<br/>' + '해당 내역을 삭제하면 복구 할 수 없습니다.',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            this.$http.post(`/api/termsPayment/delete`, list)
                .then((response) => {
                  this.$swal({ type: 'success', text: '결제조건을 삭제 하였습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.goSearch();
                        }
                      });
                })
                .catch((e) => {
                  this.$swal({type: 'error', text: e.response.data.message})
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
    save(){ // 저장 / 수정

      const vm = this;
      const list = this.gridApi.getSelectedRows();

      let flag = true;
      let msg = "";

      list.forEach(x=>{
        console.log(x);
        if(vm.isEmpty(x.termId) === ""){
          flag = false;
          msg = '지급코드를 선택하여 주세요.';
          return
        }else
        if(vm.isEmpty(x.deptCd) === ""){
          flag = false;
          msg = '부서코드를 선택하여 주세요.';
          return
        }else
        if(vm.isEmpty(x.dtChangeFlag) === ""){
          flag = false;
          msg = '결제예정일 변경여부를 선택하여 주세요.';
          return
        }else
        if(vm.isEmpty(x.ptEnabledFlag) === ""){
          flag = false;
          msg = '구매포탈 사용여부를 선택하여 주세요.';
          return
        }else
        if(vm.isEmpty(x.eaEnabledFlag) === ""){
          flag = false;
          msg = '전자전표 사용여부를 선택하여 주세요.';
          return
        }
      })

      if(!flag){
        this.$swal({ type: 'error', text: msg })
        return false;
      }


      this.$store.commit('loading');
      this.$http.post(`/api/termsPayment/save`,list)
          .then(response => {
            this.$swal({ type: 'success', text: '저장 하였습니다.' })
                .then((result) => {
                  this.goSearch();
                });
          }).catch((e) => {
        this.$swal({ type: 'error', text: e.response.data.message })
      })
      .finally(() => {
        this.$store.commit('finish');
        this.closeModal();
      });

    },
    cellValueChange(params){
      const rowNode = params.api.getRowNode(params.node.rowIndex);

      if(params.colDef.field === 'dtChangeFlag' && params.value === 'ALL'){
        rowNode.setDataValue('deptCd', "00000");
        rowNode.setDataValue('deptNm', "전체");
      }

      // 값 변경시 체크 되도록 변경
      rowNode.setSelected(true);

      this.mainGridRefresh(params.node.rowIndex);
      this.$forceUpdate();
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

      this.$http.post(`/api/termsPayment/list`,this.form)
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
    popTerm() {
      let vm = this
      const params = {
        vendorCd: '',
        deptCd: '',
        curCd: 'KRW',
        trxTypeCd : '',
        schTxt: this.form.termNm,
        searchYn: 'Y',
      }
      this.$modal.open({
        component: GLTermsModal,
        props: params,
        parent: this,
        width: 1200,
        events: {
          close(obj) {
            vm.receiveTerm(obj)
            vm.$forceUpdate();
          }
        }
      })
    },
    updateTermItem(clear) {

      const vm = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);

      const params = {
        vendorCd: '',
        deptCd: '',
        curCd: 'KRW',
        trxTypeCd : '',
        schTxt: '',
        searchYn: 'Y',
      }

      this.$modal.open({
        component: GLTermsModal,
        props: params,
        parent: this,
        width: 1200,
        events: {
          close(object) {
            rowNode.setDataValue('termId', object.termId);
            rowNode.setDataValue('name', object.name);
            rowNode.setDataValue('description', object.description);

            rowNode.setDataValue('currencyType', object.currencyType);
            rowNode.setDataValue('paymentMethod', object.paymentMethod);
            rowNode.setDataValue('vendorAcctCheck', object.vendorAcctCheck);
            vm.gridApi.getRowNode(vm.rowId).setSelected(true);
            vm.$forceUpdate();
          }
        }
      })
    },
    updateCctrItem(clear) {

      const vm = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);

      this.$modal.open({
        component: Cctr,
        props: {
          param: ''
        },
        parent: this,
        events: {
          close(object) {
            // 결제예정일 변경여부가 ALL 이 아닌경우만 부서 변경 가능하도록.
            if(rowNode.data.dtChangeFlag !== "ALL"){
              rowNode.setDataValue('deptCd', object.deptCd);
              rowNode.setDataValue('deptNm', object.deptNm);
            }
            vm.gridApi.getRowNode(vm.rowId).setSelected(true);
            vm.$forceUpdate();
          }
        }
      })

    },
    receiveCctr(obj) {
      this.form.deptCd = obj.deptCd;
      this.form.deptNm = obj.deptNm;
    },
    receiveTerm(obj) {
      this.form.termId = obj.termId;
      this.form.termNm = obj.name;
      this.$forceUpdate();
    },
    initTerm(force) {
      if (force === true) this.form.termNm = '';
      if (this.form.termNm === '') this.form.termId = '';
    },
    initCctr(force) {
      if (force === true) this.form.deptNm = '';
      if (this.form.deptNm === '') this.form.deptCd = '';
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
      select: SelectCellRenderer,
      schBtn : AgGridSearchBtn,
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
