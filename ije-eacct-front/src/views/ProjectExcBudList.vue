<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <button class="btn-size btn-blue fl_left" @click="goOpen">
          <span class="btn-icon">
            <i class="fas fa-pen-alt"></i>
          </span>
          조회
        </button>
        <button class="btn-size btn-gray fl_left" @click="save">
          <span class="btn-icon">
            <i class="fas fa-check"></i>
          </span>
          저장
        </button>
      </div>
    </div>
    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
        <label class="control-label" id="standardMonth">PJT 관리번호</label>
<!--        <div class="desc">-->
<!--          <div class="td-s-thumb search-area" style="width: 300px;">-->
<!--            <input class="input input-bg" type="text" style="width:80px;" v-model="search.wrtId" disabled>-->
<!--            <div class="ip-box ip-box-w02" style="width:140px;">-->
<!--              <input class="input" type="text" v-model="search.wrtNm" @input="initEmp" @keypress.enter="">-->
<!--              <button class="icon is-right" @click=""><i class="fas fa-search"></i>-->
<!--              </button>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
        <div class="form-input">
          <div class="datepicker w-type-ymd">
            <!-- <dhx-calendar class="input" v-model="form.standardYymm" :config="config" /> -->
            <monthly-picker v-model="form.baseYm">
            </monthly-picker>
          </div>
        </div>
      </div>
    </div>

    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <!-- 상세정보 -->
          <div class="table-a fixed-th">
            <table id="basic" class="table">
              <colgroup>
                <col width="11%"><col width="17%"><col width="10%"><col width="15%"><col width="10%"><col width="15%"><col width="10%"><col width="15%">
              </colgroup>
              <tbody>
              <tr>
                <th>PJT관리번호</th>
                <td>
                  <input class="input" style="width:80px;" type="text" v-model="form.intergrationVendorNum" disabled/>
                  <input class="input" style="width:50px;" type="text" v-model="form.intergrationVendorName" disabled/>
                  <input class="input" style="width:50px;" type="text" v-model="form.intergrationVendorName" disabled/>
                </td>

                <th>시공현장명</th>
                <td>
                  <input class="input" type="text" v-model="form.registrationNum" disabled/>
                </td>

                <th>소장/담당자</th>
                <td>
                  <input class="input" style="width:60px;" type="text" v-model="form.regYn" disabled/>
                  <input class="input" style="width:100px;" type="text" v-model="form.regYn" disabled/>
                </td>

                <th>상태</th>
                <td>
                  <input class="input" type="text" v-model="form.location" disabled/>
                </td>
              </tr>
              <tr>
                <th>기간</th>
                <td>
                  <input class="input" style="width:80px;" type="text" v-model="form.intergrationVendorNum" disabled/>
                  ~
                  <input class="input" style="width:80px;" type="text" v-model="form.intergrationVendorName" disabled/>
                </td>

                <th>영업담당</th>
                <td>
                  <input class="input" type="text" style="width:60px;" v-model="form.registrationNum" disabled/>
                  <input class="input" type="text" style="width:100px;" v-model="form.registrationNum" disabled/>
                </td>

                <th>고객</th>
                <td>
                  <input class="input" type="text" v-model="form.regYn" disabled/>
                </td>

                <th>수주금액</th>
                <td>
                  <input class="input" type="text" v-model="form.location" disabled/>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="table-a fixed-th" >
            <table id="basic" class="table">
              <colgroup>
                <col width="10%">
              </colgroup>
              <tbody>
              <tr class="table-a fixed-th table th" style="min-width: 52px; !important;">
                <th>구분</th>
                <th>1월</th>
                <th>2월</th>
                <th>3월</th>
                <th>4월</th>
                <th>5월</th>
                <th>6월</th>
                <th>7월</th>
                <th>8월</th>
                <th>9월</th>
                <th>10월</th>
                <th>11월</th>
                <th>12월</th>
              </tr>
              <tr>
                <th>실적확정여부</th>
                <td>

                </td>


              </tr>
              <tr>
                <th>예산확정여부</th>
                <td>
                </td>

              </tr>
              </tbody>
            </table>
          </div>
          <div class="table-name">
            <h3 class="ico_table_name">PJT비용예산입력(3개월) 내역</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">
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
            <button class="btn-size btn-excel" @click="saveExcel">엑셀 저장</button>
          </div>
        </div>


        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height_350"
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"

                         :enableRangeSelection="true"
                         :suppressMaxRenderedRowRestriction="true"
                         :suppressColumnVirtualisation="true"
                         :suppressRowVirtualisation="true"
                         :suppress-row-click-selection="false"
                         :context="context"

                         @grid-ready="onGridReady"
                         @cell-clicked="onCellClicked"/>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import mitt from 'mitt';
import DhxCalendar from "@/components/DhxCalendar.vue";

import mixin from "@/mixin";
import mixinSlip from "@/mixin/slip";
import MonthlyPicker from '@/components/MonthlyPicker.vue'
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import NumberInputCellRenderer from "@/components/agGrid/numberinput-cell-renderer";
import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer";

const bus = mitt();

const options = {};
const lock = {};

function queryMngItemCd() {
  return new Promise((resolve, reject) => {
    if (options["OIL_KIND_CD"] !== undefined || lock["OIL_KIND_CD"]) {
      reject(false);
    } else {
      lock["OIL_KIND_CD"] = true;
      this.$store.commit("loading");
      this.$http
          .get("/api/code/detail", {
            params: {
              groupCd: "OIL_KIND_CD"
            }
          })
          .then(response => {
            options["OIL_KIND_CD"] = response.data;
            delete lock["OIL_KIND_CD"];
            bus.emit("OIL_KIND_CD", response.data);
            return resolve(response);
          })
          .catch(response => {
            return reject(response);
          })
          .finally(() => {
            this.$store.commit("finish");
          });
    }
  });
}

export default {
  name: "ProjectExcBudList",
  mixins: [mixin, mixinSlip],
  components: {
    DhxCalendar,
    MonthlyPicker
  },
  data() {
    return {
      title: "PJT비용예산입력(3개월)",
      compCds: [],
      data: [],
      rowNode: "",
      deletedata: "",

      columnDefs : [],
      gridOptions : {
        //enableColResize: true,
        //enableFilter: true,
        //animateRows: false,
        //enableSorting: true
      },
      defaultColDef: {
        resizable: true,
        filter: false,
        sortable: false,
      },
      frameworkComponents: {
        select: SelectCellRenderer,
        numberInput: NumberInputCellRenderer,
        checkboxRenderer: CheckboxCellRenderer,
        datePicker: DatepickerCellRenderer
      },
      context: {
        headerAllCheckEvent: this.allChk
      },
      gridApi: null,
      columnApi: null,

      config: {
        hideTime: false,
        format: "YYYY-MM",
        outputFormat: "YYYYMM"
      },
      form: {
        compCd: "",
        baseYm: this.$moment().format("YYYYMM"),
        oilKindCd: "",
      },
      combos: {
        'OIL_KIND_CD' : [],
      },
    };
  },
  methods: {
    constructGridSuccessful(grid) {
      grid.setDateFormat("%Y-%m", "%Y%m");
    },
    onGridReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    createColumnDefs(){
      const vm = this;

      this.columnDefs = [
        {
          headerName: '선택'
          , field:'regYn'
          , width : 80
          , cellStyle:{textAlign: 'center'}
          , editable: false
          ,headerComponent: 'checkboxRenderer',
          headerComponentParams: (params) => {
            return params;
          }
          , cellRenderer: 'checkboxRenderer'
          , cellRendererParams: {
            trueValue: "Y"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: "N"  //false값 지정 defalut-"N" ex) "N", false, "0"
          },
        },
        {
          headerName: '구분',
          field: 'oilUpce',
          width: 100,
          editable: true,
          cellRenderer: 'numberInput',
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '계정코드',
          field: 'remark',
          width: 100,
          editable: true,
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '계정과목',
          field: 'remark',
          width: 200,
          editable: true,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '수주원가',
          field: 'chgDtm',
          width: 200,
          cellRenderer: 'numberInput',
          cellStyle:{textAlign: 'right'}
        },
        {
          headerName: '실행예산합계',
          field: 'chgDtm',
          width: 200,
          cellRenderer: 'numberInput',
          cellStyle:{textAlign: 'right'}
        },
        {
          headerName: '실적합계',
          field: 'chgNm',
          width: 150,
          cellStyle:()=>{
            return {textAlign: 'center', background:'#ededed'}
          }
        },
        {
          headerName: '가용예산합계',
          field: 'chgNm',
          width: 150,
          cellStyle: () => {
            return {textAlign: 'center', background: '#ededed'}
          }
        },
        {
          headerName: '비고',
          field: 'chgNm',
          width: 150,
          cellStyle: () => {
            return {textAlign: 'center', background: '#ededed'}
          }
        },
      ]
    },
    onCellClicked(params){
      console.log(params);
      this.rowNode = params;
    },
    getCompCdCombo() {
      this.$http
          .get(`/api/code/combo`, { params: { groupCd: "COMP_CD" } })
          .then(response => {
            // eslint-disable-next-line
            this.compCds = response.data;
          });

      this.form.compCd = this.$store.state.loginInfo.compCd;
      if (this.$store.state.loginInfo.authorities[0].authority === "ADMIN") {
        //ADMIN일경우 disabled 해제
        document.getElementById("bselect").removeAttribute("disabled");
      }
    },
    async getCombos() {
      let [bizTripArea] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "OIL_KIND_CD"}})]);
      this.combos['OIL_KIND_CD'] = bizTripArea.data;
      this.createColumnDefs();
    },
    goOpen() {
      this.$http
          .post("/api/oilPrice/list", {
            compCd: this.form.compCd,
            baseYm: this.form.baseYm,
            oilKindCd : this.form.oilKindCd
          })
          .then(response => {
            this.data = response.data;
          });
    },
    save() {
      //유종 중복 확인
      for (var i = 0; i < this.data.length; i++) {
        for (var j = i + 1; j < this.data.length; j++) {
          if (this.data[i].oilKindCd === this.data[j].oilKindCd && this.data[i].baseYm === this.data[j].baseYm) {
            this.$swal({
              type: "error",
              text: "해당 유종이 이미 존재합니다."
            });
            return false;
          }
        }
      }

      this.data.forEach(x=>{
        x.oilUpce = this.subStringColumn(x.oilUpce,5);
        x.oilEff = this.subStringColumn(x.oilEff,5);
      })

      this.$http.post("/api/oilPrice/save", this.data).then(response => {
        this.$swal({ type: "success", text: response.data });
        this.goOpen();
        return;
      });
    },
    addRow() {
      this.data.push({
        baseYm: this.$moment(this.form.baseYm).format("YYYYMM"),
        oilKindCd: options["OIL_KIND_CD"][0].detailCd,
        oilUpce: "",
        oilEff: "",
        new : true
      });
    },
    deleteRow() {

      console.log(this.rowNode);
      const rowData = this.rowNode.data;

      const rowIndex = this.rowNode.rowIndex;
      const isNew = this.data[rowIndex].new

      if(this.rowNode == null){
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
      }else if(isNew){
        this.data.splice(rowIndex, 1)
      }
      else{

        this.data.splice(rowIndex, 1)

        if (!isNew) {
          this.$http.post('/api/oilPrice/delete', {
            baseYm : rowData.baseYm,
            oilKindCd : rowData.oilKindCd
          }).then(response => {
            // Do nothing!
          })
        }
      }
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
    saveExcel() {
      // this.downloadExcel(this.$refs.grid);
      this.gridOptions.api.exportDataAsExcel({fileName: 'PJT 실행예산'});
    },
    isEmpty(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return ""
      }else{
        return value
      }
    },
    subStringColumn(value,limit){
      value = this.isEmpty(value);
      value = value.toString();
      return value.length >= limit ? value = value.toString().substring(0,limit) : value;
    }
  },
  created() {
    this.getCombos();
    queryMngItemCd.apply(this, []);
  },
  mounted() {
    this.getCompCdCombo();
    this.goOpen();
  }
};
</script>