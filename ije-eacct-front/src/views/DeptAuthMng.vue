<template>
<div class="inner-box">
    <!-- 타이틀 영역 START-->
    <div class="content-name">
      <h2 class="dp-ib">부서권한등록</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="buttonClickEventSearch()">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
      </div>
    </div>
    <!-- 타이틀 영역 END-->
    <!-- 검색조건 영역 START-->
    <div class="desc-content search-border">
      <div class="item search_wrap">
        <div class="search_box">
          <div class="search_title">
            <span class="search_tit">사원번호/명</span>
          </div>
          <div class="search_con">
            <div class="td-s-thumb search-area" style="width: 300px">
              <input class="input input-bg" type="text" style="width:100px;" v-model="form.empNo" disabled>
              <div class="ip-box ip-box-w02" style="width:120px;">
                <input class="input" type="text" v-model="form.empNm" @input="initEmp" @keypress.enter="popEmp">
                <button class="icon is-right" @click="popEmp()"><i class="fas fa-search"></i>
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
                  <span class="search_tit">사원번호/명</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg w30p_5r" type="text" v-model="form.empNo" disabled>
                  <div class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.empNm" @input="initEmp" @keypress.enter="popEmp">
                    <button class="icon is-right" @click="popEmp(true)"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">부서코드/명</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg w30p_5r" type="text" v-model="form.deptCd" disabled>
                  <div class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.deptNm" @input="initCctr" @keypress.enter="popCctr">
                    <button class="icon is-right" @click="popCctr(true)"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">재직여부</span>
                </div>
                <div class="search_con search-area">
                    <b-select class="select is-fullwidth" v-model="form.serveCd" lbl="serveCd">
                      <option value=''>==전체==</option>
                      <option value='10'>재직</option>
                      <option value='20'>퇴직</option>
                    </b-select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">부서권한</span>
                </div>
                <div class="search_con search-area">
                  <b-select class="select is-fullwidth" v-model="form.deptRoleYn" lbl="deptRoleYn">
                    <option value=''>==전체==</option>
                    <option value='Y'>예</option>
                    <option value='N'>아니오</option>
                  </b-select>
                </div>
              </div>

            </div>


            <div class="modal-window-bottom">
              <ul>
                <li>
                  <button class="bt_blue_s" @click="searchList()">검색</button>
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
    <!-- 검색조건 영역 END-->
    <!-- 데이터 영역 START-->
    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">{{this.$i18n.messages[this.$store.state.locale].userList}}</h3>
          </div>
        </div>
        <!-- Grid -->
        <ag-grid-vue         
          ref="grid"
          style="width: 100%; height: 40vh; min-height: 180px;"
          class="ag-theme-alpine"

          :columnDefs="columnDefs"
          :defaultColDef="defaultColDef"
          :rowData="data"          
          :frameworkComponents="frameworkComponents"
          :gridOptions="gridOptions"
          :suppressRowClickSelection="true"

          rowSelection="multiple"

          @grid-ready="onReady"
          @cell-clicked="onCellClicked"
        />
        <!-- //Grid -->
      </div>
    </div>
    <!-- //데이터 영역 END-->
    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">부서권한 내역</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteRow()">행삭제</el-button>
          </div>
        </div>
        <ag-grid-vue
            ref="gridSub"
            style="width: 100%; height: 25vh; min-height: 25px;"
            class="ag-theme-alpine"

            :columnDefs="columnDefsSub"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponents"
            :rowData="subData"
            :gridOptions="gridOptionsSub"
            :suppressRowClickSelection="true"

            rowSelection="multiple"

            @grid-ready="onReadySub"
            @cell-clicked="onCellClickedSub"
        />
      </div>
    </div>

</div>
</template>
<script>
import _ from "lodash";

import common from '@/mixin/common';
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import Cctr from '@/components/Cctr_Ag.vue'
import Emp from '@/components/Emp_Ag.vue'
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn";

export default {
  mixins: [common],
  name: "deptAuthMng",
  data() {
    return{
      form: {
        deptCd: "",
        deptNm: "",
        empNo : "",
        empNm : "",
        serveCd : "",
        deptRoleYn : "",
      }, 
      p_id: "",
      p_userInfo: {},     
      data: [],
      subData: [],
      rowNode: "",
      subRowNode: "",
      defaultColDef: {         
          resizable: true,
          filter: true,
          sortable: true,
          editable: false
      },              
      gridApi : null ,    //gridAip
      columnApi : null ,  //columApi
      columnDefs: [] ,    //comulum Defs
      gridApiSub: null,
      columnApiSub: null,
      columnDefsSub: [],
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
      gridOptionsSub: {
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
      frameworkComponents: {
        checkBox: CheckboxCellRenderer,
        schBtn : AgGridSearchBtn,
      },        
    }
  },
  methods: {
    //그리드 Ready
    onReady(params) {      
      //그리드용 api 정의
      this.gridApi = params.api;
      this.columnApi = params.columnApi;
    },
    onReadySub(params) {
      //그리드용 api 정의
      this.gridApiSub = params.api;
      this.columnApiSub = params.columnApi;
    },
    isEmpty(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return ""
      }else{
        return value
      }
    },
    //컬럼정의 함수
    createColumnDefs() { 
        const vm = this

        this.columnDefs = [
            {
              headerName: 'No.', width: 100, editable: false, valueGetter: (params) => {return params.node.rowIndex + 1 }, cellStyle : {'textAlign': 'center'}
            },
            {
              //headerName: this.$i18n.messages[this.$store.state.locale].authCd
              headerName: '사용자ID'
            , width : 150
            , field: 'empNo'
              , cellStyle : {'textAlign': 'center'}
            },
            {
              headerName: '이름'
            , width : 150
            , field: 'empNm'
            , cellStyle : {'textAlign': 'center'}
            },            
            {
              headerName: '부서명'
              ,width : 500
              ,field: 'deptNm'
              , cellStyle : {'textAlign': 'left'}
            },
            {
              headerName: '직급'
            , width : 200
            , field: 'jobGradeNm'
            , cellStyle : {'textAlign': 'center'}
            },
            {
              headerName: '직책'
            , width : 200
            , field: 'jobDutNm'
            , cellStyle : {'textAlign': 'center'}
            },
            {
              headerName: '재직여부'
            , width : 150
            , field: 'serveNm'
              , cellStyle : {'textAlign': 'center'}
            },
            {  headerName: '부서권한'
             , field:'deptRoleYn'
             , editable : false
             , width: 150
             , cellRenderer: 'checkBox'
             , cellRendererParams: {
                  trueValue: 'Y'    //true값  지정 defalut-"Y" ex) "Y", true, "1"
                , falseValue: 'N'  //false값 지정 defalut-"N" ex) "N", false, "0"
                , readonly: true // 수정 불가능하도록
             } 
             , cellStyle : {'textAlign': 'center'}  
            },
        ];
    },
    createColumnDefSub() {
      const vm = this

      this.columnDefsSub = [
        {
          headerName: 'No.', width: 100, editable: false, valueGetter: (params) => {return params.node.rowIndex + 1 }, cellStyle : {'textAlign': 'center'}
        },
        {
          headerName: '부서코드'
          , width : 300
          , field: 'deptCd'
          , cellStyle : {'textAlign': 'center'}
        },
        {
          headerName: '부서명'
          , width : 300
          , field: 'deptNm'
          , cellStyle : {'textAlign': 'center'}
          , cellRenderer: 'schBtn',
          cellRendererParams(params) {
            return {
              funcNm: 'updateDeptAuth',
              value: params.value
            }
          },
        },
        {
          headerName: '비고'
          ,width : 900
          ,field: 'remark'
          , cellStyle : {'textAlign': 'center'}
          , editable : true
        },
      ];
    },
    save() {

      const rowData = this.rowNode.data;

      let submitList = [];

      this.subData.forEach(x=>{
        submitList.push({
          compCd : this.$store.state.loginInfo.compCd,
          deptCd: x.deptCd,
          empNo : rowData.empNo,
          remark: x.remark
        })
      });


      if(submitList.length === 0){
        this.$swal({
          type: "error",
          html: "부서를 선택 하여 주세요."
        });
        return false;
      }


      //중복 확인
      for (var i = 0; i < this.subData.length; i++) {
        if(this.isEmpty(this.subData[i].deptCd) === "") {
          this.$swal({
            type: "error",
            html: "부서를 선택 하여 주세요."
          });
          return false;
        }
        for (var j = i + 1; j < this.subData.length; j++) {
          if (this.subData[i].deptCd === this.subData[j].deptCd) {
            this.$swal({
              type: "error",
              html: this.subData[j].deptNm +"의 부서 권한이 이미 존재합니다. "
            });
            return false;
          }
        }
      }

      this.$http.post("/api/dept/deptAuth/save", submitList).then(response => {
        this.$swal({ type: "success", text: response.data });
        // this.searchList();
      });
    },
    buttonClickEventSearch() {
      this.searchList()
    },
    searchList() {
      this.$store.commit('loading')
      this.$http.post(`/api/emp/list`,this.form).then(response => {
        this.data = response.data
        
        if(response.data.length == 0){
            this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
        }
        this.subData.splice(0);
        this.closeModal();
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    onCellClicked(params) {
      this.rowNode = params.node;

      this.$http
          .post("/api/dept/deptAuth/list", {
            compCd: this.form.compCd,
            empNo: this.rowNode.data.empNo
          })
          .then(response => {
            this.subData = response.data;
          });
    },
    onCellClickedSub(params){
      this.subRowNode = params.node;
    },
    onCellDoubleClicked(event) {
      
      const row = this.gridApi.getSelectedNodes();
      this.p_userInfo = row[0].data;
    },
    receiveCctr(obj) {
      this.form.deptCd = obj.deptCd;
      this.form.deptNm = obj.deptNm;
    },
    receiveEmp(obj) {
      this.form.empNo = obj.empNo;
      this.form.empNm = obj.empNm;
      this.$forceUpdate();
    },
    initCctr(force) {
      if (force === true) this.form.deptNm = '';
      if (this.form.deptNm === '') this.form.deptCd = '';
    },
    initEmp(force) {
      if (force === true) this.form.empNm = '';
      if (this.form.empNm === '') this.form.empNo = '';
    },
    popCctr(){
      const that  = this;

      this.$modal.open({
        width: 800,
        component: Cctr,
        parent: this,
        props: {
          param: this.form.deptNm
        },
        events: {
          close(data) {
            that.form.deptCd = data.deptCd
            that.form.deptNm = data.deptNm
          },
        }
      })
    },
    popEmp(clear) {
      let vm = this
      this.$modal.open({
        component: Emp,
        props: {
          param: this.form.empNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveEmp(obj)
          }
        }
      })
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
      this.form.deptCd= "";
      this.form.deptNm= "";
      this.form.empNo = "";
      this.form.empNm = "";
      this.form.serveCd = "";
      this.form.deptRoleYn = "";
    },
    addRow() {
      this.subData.push({
          deptCd : "",
          deptNm : "",
          remark : "",
      })
    },
    deleteRow() {

      console.log(this.subRowNode);

      if (this.subRowNode.length < 1) {
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
      } else {

        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$http
                .post("/api/dept/deptAuth/delete", {
                  compCd: this.form.compCd,
                  empNo: this.rowNode.data.empNo,
                  deptCd: this.subRowNode.data.deptCd
                })
                .then(response => {
                  this.$swal({type: 'success', text: '삭제 되었습니다.'});
                  this.searchList();
                });
          }
        })
      }
    },
    updateDeptAuth(pVal){

      const vm = this;
      const rowNode = this.gridApiSub.getRowNode(this.rowId);

      this.$modal.open({
        component: Cctr,
        parent: this,
        width: 700,
        props: {
          data: {
          }
        },
        events: {
          close(object) {
            rowNode.setDataValue('deptCd', object.deptCd);
            rowNode.setDataValue('deptNm', object.deptNm);
          }
        }
      });
    },
  },
  beforeMount() {
    this.createColumnDefs(); //그리드 컬럼정의 함수 호출
    this.createColumnDefSub();
  },
  mounted() {
    this.searchList()
  },
};
</script>

