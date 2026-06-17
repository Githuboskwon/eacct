<template>
  <div class="inner-box">
    <form @submit.prevent="save">
      <div class="content-name">
        <h2 class="dp-ib">{{title}}</h2>
        <div class="btn-type1 clearfix">
          <el-button type="primary" icon="el-icon-search" size="large" @click="goOpen">조회</el-button>
          <el-button type="success" icon="el-icon-folder-checked" size="large" @click="save">저장</el-button>
          <!-- <button type="submit" class="btn-size btn-blue fl_left">
          <span class="btn-icon">
            <i class="fas fa-save"></i>
          </span>
            저장
          </button> -->
        </div>
      </div>
    </form>
    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="item desc-left">
        <div class="desc-item">
          <div class="tit">
            <span class="label-tit">- 작성자</span>
          </div>

          <div class="desc">
            <div class="td-s-thumb search-area" style="width: 300px;">
              <input class="input input-bg" type="text" style="width:65px;" v-model="form.wrtId" disabled>
              <div v-if="this.$store.state.loginInfo.authorities[0].roleCd ==='ADMIN'" class="ip-box ip-box-w02" style="width:120px;">
                <input class="input" type="text" v-model="form.wrtNm" @input="initEmp" @keypress.enter="popEmp">
                <button class="icon is-right" @click="popEmp(true)"><i class="fas fa-search"></i>
                </button>
              </div>
              <div v-else class="ip-box ip-box-w02" style="width:120px;">
                <input class="input" type="text" v-model="form.wrtNm" @input="initEmp" disabled>
              </div>
            </div>
          </div>
        </div>
      </div>


      <div class="item desc-center">
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
                  <span class="search_tit">- 결재선제목</span>
                </div>
                <div class="search_con search-area">
                    <div class="td-s-thumb search-area" style="width: 100%">
                      <input class="input" type="text" v-model="form.searchTitle">
                    </div>
                </div>
              </div>


              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 메인결재선여부</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth" v-model="form.mainType">
                    <option value="">전체</option>
                    <option value="Y">예</option>
                    <option value="N">아니오</option>
                  </select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 사용여부</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth" v-model="form.useType">
                    <option value="">전체</option>
                    <option value="Y">예</option>
                    <option value="N">아니오</option>
                  </select>
                </div>
              </div>

            </div>

            <div class="modal-window-bottom">
              <ul>
                <li>
                  <button class="bt_blue_s" @click="goOpen">검색</button>
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
    <!-- 테이블 -->
    <div class="table-b">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">결재라인 그룹코드</h3>
        </div>
        <div class="btn-type2 clearfix">
          <el-button type="info" icon="el-icon-plus" @click="addRow()">행추가</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="deleteRow">행삭제</el-button>
          <!-- <button class="btn-size btn-w-gray" @click="addRow()">
            <span class="btn-icon">
              <i class="fas fa-plus"></i>
            </span>행추가
          </button>
          <button class="btn-size btn-w-gray" @click="deleteRow">
            <span class="btn-icon">
              <i class="fas fa-trash-alt"></i>
            </span>행삭제
          </button> -->
        </div>
      </div>
      <!-- <dhx-grid ref="grid1" v-model="data" :config="config" /> -->
      <ag-grid-vue
        ref="gridMain"
        style="width: 100%; height: 30vh; min-height: 25px;"
        class="ag-theme-alpine"
        rowSelection="single"

        :columnDefs="columnDefs"
        :defaultColDef="defaultColDef"
        :frameworkComponents="frameworkComponents"
        :rowData="data"
        :gridOptions="gridOptions"
        :suppressRowClickSelection="false"
        :enableRangeSelection="false"
        @grid-ready="onReadyMain"
        @selection-changed="onSelectionChangedMain"
      />
    </div>
    <div class="table-b" style="margin-top: 20px">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">결재라인 상세코드</h3>
        </div>
        <div class="btn-type2 clearfix">
          <el-button type="success" icon="el-icon-user" @click="openSetApprPopup">결재자 지정</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="deleteRowd">행삭제</el-button>
          <!-- <button class="btn-size btn-w-gray" @click="openSetApprPopup">
            <span class="btn-icon">
              <i class="fas fa-user-check"></i>
            </span>결재자 지정
          </button>
          <button class="btn-size btn-w-gray" @click="deleteRowd">
            <span class="btn-icon">
              <i class="fas fa-trash-alt"></i>
            </span>행삭제
          </button> -->
        </div>
      </div>
      <!-- <dhx-grid ref="grid2" v-model="subData" :config="config_child" /> -->
      <ag-grid-vue
        ref="gridSub"
        style="width: 100%; height: 30vh; min-height: 25px;"
        class="ag-theme-alpine"
        rowSelection="single"

        :columnDefs="columnDefsSub"
        :defaultColDef="defaultColDef"
        :frameworkComponents="frameworkComponents"
        :rowData="subData"
        :gridOptions="gridOptionsSub"
        :suppressRowClickSelection="false"
        @grid-ready="onReadySub"
      />
    </div>
  </div>
</template>
<script>
import _ from "lodash";

import {AgGridVue} from 'ag-grid-vue'
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'

import ApprLineSet from '@/components/ApprLineSet.vue'
import Emp from "@/components/Emp_Ag.vue";

export default {
  name: "appLineMng",
  components: {
    AgGridVue,
    ApprLineSet,
    Emp
  },
  data() {
    return {
      title: "개인결재선관리",
      deleteid: "",
      deleteList: [],
      form: {
        mainType: '',
        useType: '',
        wrtId: '',
        wrtNm: '',
        searchTitle: '',
      },
      data: [],
      subData: [],
      gridOptions: {
        //enableColResize: true,
        //enableFilter: true,
        //animateRows: false,
        //enableSorting: true
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
        //enableColResize: true,
        //enableFilter: true,
        //animateRows: false,
        //enableSorting: true
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
      },
      gridApi: null,
      columnApi: null,
      columnDefs: [],
      gridApiSub: null,
      columnApiSub: null,
      columnDefsSub: [],
      frameworkComponents: null,
    };
  },
  methods: {
    //메인그리드 Ready
    onReadyMain(params) {
      //메인그리드 api 정의
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;

      //화면 width에 맞게 컬럼 size 조정
      //this.gridApi.sizeColumnsToFit();
    },
    //서브그리드 Ready
    onReadySub(params) {
      //서브그리드 api 정의
      this.gridApiSub = this.gridOptionsSub.api;
      this.columnApiSub = this.gridOptionsSub.columnApi;

      this.gridApiSub.sizeColumnsToFit();
    },
    //그리드컬럼 정의
    createColumnDefs() {
      const vm = this

      this.columnDefs = [
        {headerName: '결재라인번호', field:'apprSeq' , width : 120, cellStyle:{textAlign: 'center'}},
        {  headerName:'사원번호'
          , field:'userId'
          , width:110
          , cellStyle:{textAlign: 'center'}
          , editable:(params) => {
            if(this.$store.state.loginInfo.authorities[0].roleCd == 'ADMIN' && !params.data.apprSeq){
              return true
            }else{
              return false
            }
          }
        },
        {  headerName:'사원명'
          , field:'userNm'
          , width:110
          , cellStyle:{textAlign: 'center'}
          , editable: false
        },
        {headerName:'결재선제목', field:'apprLineTitle', width:260, editable:true},
        {  headerName:'메인결재선여부'
          , field:'mainApprYn'
          , width: 130
          , cellStyle : {'justify-content': 'center','align-items': 'center', 'display': 'flex','height': '100%'}
          , cellRenderer: 'checkboxRenderer'
          , cellRendererParams: {
            trueValue: "Y"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: "N"  //false값 지정 defalut-"N" ex) "N", false, "0"
          }
          , editable:true
        },
        {  headerName:'사용여부'
          , field:'useYn'
          , width: 110
          , cellStyle : {'justify-content': 'center','align-items': 'center', 'display': 'flex','height': '100%'}
          , cellRenderer: 'checkboxRenderer'
          , cellRendererParams: {
            trueValue: "Y"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: "N"  //false값 지정 defalut-"N" ex) "N", false, "0"
          }
          , editable:true
        },
        {headerName:'비고', field:'remark', width:250, editable:true},
        { headerName:'등록일자'
          , field:'regDtm'
          , width:140
          , cellStyle:{textAlign: 'center'}
          , valueFormatter: (params) => {
              return params === 'null' ? '' : this.$moment(params).format('YYYY-MM-DD')
          }
        },
        {headerName:'등록자', field:'regId', width:100, cellStyle:{textAlign: 'center'}},
        { headerName:'수정일자'
          , field:'chgDtm'
          , width:140
          , cellStyle:{textAlign: 'center'}
          , valueFormatter: (params) => {
              return params === 'null' ? '' : this.$moment(params).format('YYYY-MM-DD')
          }
        },
        {headerName:'수정자', field:'chgId', width:100, cellStyle:{textAlign: 'center'}},
        {headerName:'', field:'compCd', hide : true},
      ];

      this.columnDefsSub = [
        { headerName: '결재상세 라인번호'
          , field:'subApprSeq'
          , width : 80
          , cellStyle:{textAlign: 'center'}
        },
        {headerName:'결재자사번', field:'apprUserId', width:100 ,cellStyle:{textAlign: 'center'}},
        {headerName:'결재자이름', field:'apprUserNm', width:100 ,cellStyle:{textAlign: 'center'}},
        {headerName:'직급', field:'jobNm', width:80, cellStyle:{textAlign: 'center'}},
        {headerName:'부서', field:'deptNm', width:100 ,cellStyle:{textAlign: 'left'}},
        { headerName:'결재형식'
          , field:'apprTypeCd'
          , width:80
          , cellStyle:{textAlign: 'center'}
          , valueFormatter: (params) => {
              return (params.value === '10' ? '기안' : (params.value === '20' ? '결재' : '합의'))
          }
        },
        {headerName:'', field:'compCd', hide : true},
        {headerName:'', field:'userId', hide : true},
        {headerName:'', field:'jobCd', hide : true},
        {headerName:'', field:'deptCd', hide : true},
        {headerName:'', field:'serveCd', hide : true},
      ];
    },
    //Main 그리드 SelctionChange 발생
    onSelectionChangedMain(event) {
      const voRow = this.gridApi.getSelectedRows()

      if(voRow[0].apprSeq)
        this.refreshDetail();
      else
        this.subData = undefined;

    },
    openModal(){
      document.getElementById("open-moda").style.opacity = "1";
      document.getElementById("open-moda").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-moda").style.opacity = "0";
      document.getElementById("open-moda").style.pointerEvents = "none";
    },
    resetSearch(){
      this.form.mainType = ''
      this.form.useType = ''
      this.form.searchTitle = ''
    },
    initEmp(force) {
      if (force === true) this.form.wrtNm = '';
      if (this.form.wrtNm === '') {
        this.form.wrtId = '';
        this.form.wrtUserDut = '';
        this.form.wrtUserDept = '';
      }
    },
    popEmp(clear) {
      let vm = this
      this.$modal.open({
        component: Emp,
        props: {
          param: this.form.wrtNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.wrtId = obj.empNo;
            vm.form.wrtNm = obj.empNm;
          }
        }
      })
    },
    goOpen() {
      this.subData = undefined;

      // 결재라인 헤더 조회
      this.$store.commit('loading')
      this.$http
        .post("/api/apprLine/group", {
          compCd: this.$store.state.loginInfo.compCd,
          userId: this.form.wrtId,
          apprLineTitle: this.form.searchTitle,
          mainApprYn: this.form.mainType,
          useYn: this.form.useType
        })
        .then(response => {
          this.data = response.data;

          if(response.data.length == 0){
            this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
          }
        }).finally(() => {
        this.$store.commit('finish')
      });

      this.closeModal();
    },
    save() {
      const row = this.data
      let count = 0

      // validation check
      for(var i = 0; i < row.length; i++){
        if(!row[i].userId.trim()){
          this.$swal({ type: "error", text: (i+1) + "행의 사원번호를 입력해주세요." })
          return
        }

        if(!row[i].apprLineTitle.trim()){
          this.$swal({ type: "error", text: (i+1) + "행의 결재선 제목을 입력해주세요." })
          return
        }

        if(row[i].mainApprYn == 'Y') {
          count++;
          if(count > 1){
            this.$swal({ type: "error", text: "메인결재선은 하나만 선택 가능합니다." })
            return
          }
        }

        row[i].apprSeq = i+1;
      }

      //저장 시작
      this.$http
        .post('/api/apprLine/saveGroup', row)
        .then(response => {
          this.$swal({ type: 'success', text: '저장되었습니다.' });
          this.goOpen()
        })
        .catch(({ message }) => {
          this.$swal({ type: "error", text: "실패." });
        });
    },
    deleteRow() {

      if(this.subData && this.subData.length > 0){
        this.$swal({type: 'error' , html:'해당 그룹코드의 상세코드가 존재합니다.<br/>상세코드 삭제 후 그룹코드 삭제가 가능합니다.'})
        return
      }

      const row = this.gridApi.getSelectedNodes()

      if (row.length < 1) {
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
      } else if(row.length > 1){
        this.$swal({
          type: 'error',
          text: '하나의 행만 선택하여주세요.'
        })
      } else {
        this.$swal({
          type: 'warning',
          text: '삭제 하시겠습니까?',
          showCancelButton: true,
          confirmButtonText: '예',
          cancelButtonText: '아니요'
        }).then(response => {
          if(response.value){
            const rowIndex = row[0].rowIndex

            this.data.splice(rowIndex, 1)

            if(row[0].data.apprSeq){
              this.$http.post('/api/apprLine/delGroup', {
                compCd : row[0].data.compCd,
                userId : row[0].data.userId,
                apprSeq : row[0].data.apprSeq
              })
                .then(response => {
                  this.$swal({type: 'success', text: response.data});

                  this.goOpen();
                })
            }
          }else{
            return
          }
        })
      }

    },
    addRow() {
      this.data.push({
        apprSeq: "",
        userId: this.form.wrtId,
        apprLineTitle: "",
        mainApprYn: "N",
        useYn: "Y",
        remark: "",
        compCd: this.$store.state.loginInfo.compCd,
      });
    },
    deleteRowd() {
      try{

        const row = this.gridApiSub.getSelectedNodes()

        if(row.length < 1)
          throw '선택된 행이 없습니다.'

        this.$swal({
          type: 'warning',
          text: '삭제 하시겠습니까?',
          showCancelButton: true,
          confirmButtonText: '예',
          cancelButtonText: '아니요'
        }).then(response => {
          if(response.value){
            let index = row[0].rowIndex
            this.subData.splice(index, 1)

            this.$http
              .post('/api/apprLine/delDetail',{
                compCd : row[0].data.compCd,
                userId : row[0].data.userId,
                apprSeq : row[0].data.apprSeq,
                subApprSeq : row[0].data.subApprSeq
              })
              .then(response => {
                this.$swal({ type: 'success', text: response.data });

                this.refreshDetail();
              })
              .catch(({ message }) => {
                this.$swal({ type: "error", text: message });
              });
          }else {
            return
          }
        })

      }catch (e) {
        this.$swal({ type: 'warning', text: e})
      }
    },
    openSetApprPopup() {
      try{

        const row = this.gridApi.getSelectedNodes()

        if(row.length < 1)
          throw '그룹코드가 선택되지 않았습니다.'

        if(!row[0].data.apprSeq)
          throw '그룹코드 저장 후 가능합니다.'

        //결재자 지정
        let vm = this
        this.$modal.open({
          component: ApprLineSet,
          props: {
            lineList: this.apprLine,
            setUserId: this.form.wrtId == '' ? row[0].data.userId : this.form.wrtId,
            selectSeq: row[0].data.apprSeq
          },
          parent: this,
          events: {
            close(data) {
              vm.refreshDetail();
              // Close event handler
              //vm.apprLine = data.lineList || []
            }
          }
        })
      }catch (e) {
        this.$swal({
          type: 'warning',
          text: e
        })
      }
    },
    refreshDetail(){
      const voRow = this.gridApi.getSelectedRows()

      this.$store.commit('loading')
      this.$http
        .post("/api/apprLine/detail", {
          compCd: voRow[0].compCd,
          userId: voRow[0].userId,
          apprSeq: voRow[0].apprSeq == undefined ? -1 : voRow[0].apprSeq
        })
        .then(response => {
          this.subData = response.data
        }).finally(() => {
        this.$store.commit('finish')
      });
    },
  },
  beforeMount() {

    this.createColumnDefs(); //그리드 컬럼정의 함수 호출

    this.frameworkComponents = {//그르드에서 사용할 외부 comp 등록
      checkboxRenderer: CheckboxCellRenderer,
    };

    this.form.wrtId = this.$store.state.loginInfo.loginId;
    this.form.wrtNm = this.$store.state.loginInfo.userName;

  },
  mounted() {
    document.title = this.title + ' - IJEAS';

    //this.goOpen();
  }
};
</script>


<style scoped>
.gridbox {
  height: 200px !important;
}
.gridbox .objbox {
  height: 350px !important;
}

.desc-content:after {
  clear: both;
  content: "";
  display: block;
}
.btn-wrap {
  margin-bottom: 20px;
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
  font-family: "NotoM";
  color: #222;
  font-size: 15px;
}
.desc-content .item.desc-left .desc-item {
  padding-left: 90px;
}
.desc-content .item.desc-left .desc-item .desc:after {
  clear: both;
  content: "";
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
  content: "";
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
.search-border
.td-s-thumb.search-area
> .ip-box
.search-border
.td-s-thumb.search-area
> button {
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