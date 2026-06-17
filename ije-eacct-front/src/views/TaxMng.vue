<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <button class="btn-size btn-gray fl_left" @click="btnSearchClick">
          <span class="btn-icon">
            <i class="fas fa-search"></i>
          </span> 조회
        </button>
        <button class="btn-size btn-blue fl_left" @click="save">
          <span class="btn-icon">
            <i class="fas fa-save"></i>
          </span> 저장
        </button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
        <label class="control-label-req" id="compCd">회사</label>
        <div class="form-input">
          <select id="bselect" class="select is-fullwidth" v-model="form.compCd" disabled = "true" lbl="compCd">
            <option
                v-for="item in compCds"
                :key="item.key"
                :value="item.key"
                v-text="item.value"
            />
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label">세금 코드/명</label>
        <div class="form-input">
          <input class="input Rt-M tal" type="text" id="account_cd" v-model="form.codeNm" />
        </div>
      </div>
    </div>

    <!-- Grid영역 -->
    <div class="table-area mt20">
      <div class="table-b" style="margin-bottom:0 !important">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">세금코드 관리</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">
            <!-- 계정과목관리, 세금코드관리는 행추가, 행삭제 기능은 제거(주석) 2020.01.02 요청 -->
            <!--
            <button class="btn-size btn-w-gray" @click="addRow">
              <span class="btn-icon">
                <i class="fas fa-plus"></i>
              </span>행추가
            </button>
            <button class="btn-size btn-w-gray" @click="deleteRow">
              <span class="btn-icon">
                <i class="fas fa-trash-alt"></i>
              </span>행삭제
            </button>
             -->
            <button class="btn-size btn-excel" @click="saveExcel">엑셀 저장</button>
          </div>
        </div>
      </div>
    </div>
    <div class="table-b">
      <div class="grid-wrap">
        <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height"
                     :columnDefs="columnDefs"
                     :rowData="data"
                     :gridOptions="gridOptions"
                     :frameworkComponents="frameworkComponents"
                     :defaultColDef="defaultColDef"

                     @grid-ready="onReady"
        />
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";

import GridSelect from "@/components/grid/GridSelect.vue";
import mixin from "@/mixin/slip-common";
import GridNumberInput from '@/components/grid/GridNumberInput.vue'
import mixinSlip from '@/mixin/slip';
import GridCheckbox from '@/components/grid/GridCheckbox.vue'
import _ from 'lodash'

import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'

import common from '@/mixin/common';

var eventBus = new Vue();

const options = {};

var queryEvdTypeCd_run = false;

function queryTpsTypeCd() {

  if (options["evdTypeCd"] === undefined && !queryEvdTypeCd_run) {
    queryEvdTypeCd_run = true;
    this.$http
        .get("/api/code/detail", {
          params: {
            groupCd: "EVD_TYPE_CD"
          }
        })
        .then(response => {
          var datas = {};
          datas[0] = { detailCd: "", detailNm: "", detailRealNm: ""}
          for (var i = 0; i < response.data.length; i++) {
            datas[i + 1] = {
              detailCd: response.data[i].detailCd,
              originNm: response.data[i].detailNm,
              detailNm: "[" + response.data[i].detailCd + "] " + response.data[i].detailNm,
              detailRealNm: response.data[i].detailNm
            };
          }
          options["evdTypeCd"] = datas;
        })
        .catch(response => {
          options["evdTypeCd"] = undefined;
          return response;
        })
        .finally(() => {
          eventBus.$emit("evdTypeCd.updated");
          queryEvdTypeCd_run = false;
        });
  }
}

export default {
  name: "taxMng",
  mixins: [mixin, mixinSlip,common],
  data() {
    return {
      title: "세금코드 관리",
      compCds: [],
      deleteid: [],
      form: {
        compCd: "",
        codeNm: ""
      },
      gridOptions: {},
      defaultColDef: {
        resizable: true,
        filter: true,
        sortable: true,
        editable: true
      },
      gridApi : null ,    //gridAip
      columnApi : null ,  //columApi
      columnDefs: [] ,    //comulum Defs
      frameworkComponents: {
        checkbox: CheckboxCellRenderer,
        select: SelectCellRenderer,
        numberInput: NumberInputCellRenderer
      },
      selected: [],
      data: [{}]
    };
  },
  methods: {
    //그리드 Ready
    onReady(params) {

      //그리드용 api 정의
      //this.gridApi = params.api;
      //this.columnApi = params.columnApi; //params대신 gridOptions 사용가능
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;

      setTimeout(() => {
        this.createColumnDefs();
      },500);
    },
    //컬럼정의 함수
    createColumnDefs() {
      const vm = this

      this.columnDefs = [
        {
          headerName: 'No.',
          width: 80,
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          },
          editable: false
        },
        {
          headerName: '세금코드'
          , width : 105
          , field: 'taxCd'
          , editable: false
          , headerClass: 'require-column' //필수입력 컬럼 css
        },
        {
          headerName: '세금명칭'
          , width : 210
          , field: 'taxNm'
          , headerClass: 'require-column' //필수입력 컬럼 css
        },
        {
          headerName: '세율'
          , width : 80
          , field: 'taxRt'
          , cellRenderer: 'numberInput'
          , cellRendererParams: {
            maxlength: 3
          }
          , min : 0
          , max : 100
        },
        {
          headerName: '사용여부'
          , width : 105
          , field: 'useYn'
          , cellRenderer: 'checkbox'
          , cellStyle : {'justify-content': 'center','align-items': 'center', 'display': 'flex','height': '100%'}
        },
        {
          headerName: '정렬순서'
          , width : 105
          , field: 'orderSeq'
          , cellRenderer: 'numberInput'
        },
        {
          headerName: '증빙유형코드'
          , width : 245
          , field: 'evdTypeCd'
          , cellRenderer: 'select'
          , cellRendererParams: {
            options : options["evdTypeCd"],
            detailCd: 'detailCd',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'detailNm' //서버로 부터 받은 value defalut-"detailNm"
          }
        },
        {
          headerName: '비고1'
          , width : 230
          , field: 'ref1'
        },
        {
          headerName: '비고2'
          , width : 230
          , field: 'ref2'
        },
        {
          headerName: '비고3'
          , width : 230
          , field: 'ref3'
        }
      ];
    },
    save() {
      //그리드 필수입력 체크
      if(!this.gridRequireCheck([this.$refs.grid])) return;

      //그리드 유효성 체크
      if(!this.gridValidateCheck([this.$refs.grid])) return;

      for(var i = 0;i<this.data.length;i++){
        for(var j = i+1; j<this.data.length;j++){
          if(this.data[i].taxCd === this.data[j].taxCd){
            this.$swal({type: 'warning', text: '해당 세금코드가 이미 존재합니다. 중복코드 : ' + this.data[i].taxCd});
            return false;
          }
        }
      }
      this.$http.post('/api/taxes/save',  this.data)
          .then(response =>{
            this.$swal({ type: 'success', text: '저장되었습니다' });
            this.goOpen()
          })

    },
    saveExcel() {
      var params = {
        fileName : "세금코드 관리"
      };
      this.gridApi.exportDataAsCsv(params);

    },
    getCompCdCombo() {
      this.$http
          .get(`/api/code/combo`, { params: { groupCd: "COMP_CD" } })
          .then(response => {
            // eslint-disable-next-line
            this.compCds = response.data;
          });

      this.form.compCd = this.$store.state.loginInfo.compCd;
      // if (this.$store.state.loginInfo.authorities[0].authority === "ADMIN") {
      if (this.$store.state.loginInfo.loginJobDutCd === "ADMIN") { // loginInfo 에 authorities가 없어서 임시로 처리
        //ADMIN일경우 disabled 해제
        document.getElementById("bselect").removeAttribute("disabled");
      }
    },
    btnSearchClick(){
      if(!this.searchRequireCheck()) return;

      this.goOpen();
    },
    goOpen() {

      this.$http
          .post(`/api/taxes/list`,
              {
                compCd: this.compCds.key,
                taxCd: this.form.codeNm
              })
          .then(response => {
            this.data = response.data;
          });
    },

  },
  created(){
    document.title = this.title + ' - IJEAS';
    queryTpsTypeCd.apply(this, []);
  },
  mounted() {
    this.getCompCdCombo();
    this.goOpen();
  }
};
</script>

<style lang="scss" scoped>
.tax-mng-grid {
  :global(.gridbox) {
    height: 50vh !important;
  }
}

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