<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goOpen()">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
      </div>
    </div>
    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
        <label class="control-label-req" id="standardMonth">경조기준일</label>
        <div class="form-input">
<!--          <div class="datepicker w-type-ymd">-->
<!--             <dhx-calendar class="input" v-model="form.basicDate"/>-->
<!--          </div>-->
          <el-date-picker type="date"
                          placeholder="경조기준일"
                          v-model="form.basicDate"
                          style="width: 60%;"
                          value-format="YYYY-MM-DD">
          </el-date-picker>
        </div>
      </div>


      <div class="form-group">
        <label class="control-label" id="standardMonth">경조구분</label>
        <div class="form-input">
          <div class="td-s-thumb search-area" style="width: 300px">
            <input class="input input-bg" type="text" style="width:100px; margin-right: 10px;" v-model="form.expendCd" disabled>
<!--            <input class="input input-bg" type="text" v-show="false" v-model="form.expendCd">-->
            <div class="ip-box ip-box-w02" style="width:120px;">
              <input class="input" type="text" v-model="form.expendNm" @input="initExpend" @keypress.enter="popExpend">
              <button class="icon is-right" @click="popExpend()"><i class="fas fa-search"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">경조금 내역</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteRow()">행삭제</el-button>
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

                         :enableRangeSelection="true"
                         :suppressMaxRenderedRowRestriction="true"
                         :suppressColumnVirtualisation="true"
                         :suppressRowVirtualisation="true"
                         :suppress-row-click-selection="false"
                         :context="context"
                         :suppressRowClickSelection="true"

                         rowSelection="multiple"

                         @row-selected="onRowSelected"
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
import createBus from '@/libs/eventBus';
import DhxCalendar from "@/components/DhxCalendar.vue";

import mixin from "@/mixin";
import mixinSlip from "@/mixin/slip";
import MonthlyPicker from '@/components/MonthlyPicker.vue'
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import NumberInputCellRenderer from "@/components/agGrid/numberinput-cell-renderer";
import DatepickerCellRenderer from "@/components/agGrid/el-datepicker-cell-renderer";
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer';
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn";
import Expend from "@/components/Expend_Ag";

const bus = createBus();

const options = {};
const lock = {};

function queryMngItemCd() {
  return new Promise((resolve, reject) => {
    if (options["EXPEND_MONEY_CD"] !== undefined || lock["EXPEND_MONEY_CD"]) {
      reject(false);
    } else {
      lock["EXPEND_MONEY_CD"] = true;
      this.$store.commit("loading");
      this.$http
          .get("/api/code/detail", {
            params: {
              groupCd: "EXPEND_MONEY_CD"
            }
          })
          .then(response => {
            options["EXPEND_MONEY_CD"] = response.data;
            delete lock["EXPEND_MONEY_CD"];
            bus.emit("EXPEND_MONEY_CD", response.data);
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
  name: "oilPcePop",
  mixins: [mixin, mixinSlip],
  components: {
    DhxCalendar,
    MonthlyPicker,
    Expend
  },
  data() {
    return {
      title: "경조금 관리",
      compCds: [],
      data: [],
      rowNode: "",
      deletedata: "",

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
        filter: false,
        sortable: false,
      },
      frameworkComponents: {
        select: SelectCellRenderer,
        numberInput: NumberInputCellRenderer,
        datePicker: DatepickerCellRenderer,
        checkboxRenderer: CheckboxCellRenderer,
        schBtn : AgGridSearchBtn,
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
        basicDate: this.$moment().format('YYYY-MM-DD HH:mm:ss'),
        expendCd: "",
        expendNm: "",
      },
      combos: {
        'EXPEND_MONEY_CD' : [],
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
        {headerName: 'No.', width : 30, valueGetter: (params) => {return params.node.rowIndex + 1 } , hide:true},
        {
          headerName: ''
          , headerCheckboxSelection: true
          , checkboxSelection: true
          , field:'regYn'
          , width : 80
          , cellStyle:{textAlign: 'center', color: 'transparent'}
          , editable: false
        },
        {
          headerName: '시작일',
          field: 'startDate',
          width: 150,
          headerClass: 'require-column',
          cellRenderer: 'datePicker',
          // cellRendererParams: {
          //   config: {
          //     hideTime: true,
          //     format: "YYYY-MM-DD",
          //     outputFormat: "YYYYMMDD",
          //     dateFormat: '%Y-%m-%d'
          //   },
          // },
          cellRendererParams: (params) => {
            return {
              type: 'date',
              format: 'yyyy-MM-dd',  //date format
              valueFormat: 'yyyyMMdd',  //받는 model 의 결과 포맷
              disable: true
            }
          },
          valueFormatter: (params) => {
            return params.value;
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
          cellClass: function(params) {
            if(params.node.data.new) {
              return 'bg-lightpink';
            } else {
              return 'bg-grey';
            }
          },
          editable: function(params) {
            if(params.node.data.new) {
              return true;
            } else {
              return false;
            }
          },
        },
        {
          headerName: '종료일',
          field: 'endDate',
          width: 150,
          cellRenderer: 'datePicker',
          // cellRendererParams: {
          //   config: {
          //     hideTime: true,
          //     format: "YYYY-MM-DD",
          //     outputFormat: "YYYYMMDD",
          //     dateFormat: '%Y-%m-%d'
          //   },
          // },
          cellRendererParams: (params) => {
            return {
              type: 'date',
              format: 'yyyy-MM-dd',  //date format
              valueFormat: 'yyyyMMdd',  //받는 model 의 결과 포맷
              disable: false
            }
          },
          valueFormatter: (params) => {
            return params.value;
          }
        },
        {
          headerName: '경조구분',
          field: 'expendCd',
          width: 250,
          headerClass: 'require-column',
          cellRenderer: 'schBtn',
          cellRendererParams(params) {
            return {
              funcNm: 'updateExpendItem',
              value: vm.chkType(params.value)
            }
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
          cellClass : function(params) {
            if(params.node.data.new) {
              return 'bg-lightpink';
            } else {
              return 'bg-grey';
            }
          },
          editable: function(params) {
            if(params.node.data.new) {
              return true;
            } else {
              return false;
            }
          },
        },
        {
          headerName: '화환구분'
          , field:'wreathYn'
          , width : 150
          , editable: false
          , cellStyle:{textAlign: 'center'}
          , cellRenderer: 'checkboxRenderer'
          , cellRendererParams: {
            trueValue: "Y"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: "N"  //false값 지정 defalut-"N" ex) "N", false, "0"
          },
        },
        {
          headerName: '상조용품구분'
          , field:'mutualYn'
          , width : 150
          , editable: false
          , cellStyle:{textAlign: 'center'}
          , cellRenderer: 'checkboxRenderer'
          , cellRendererParams: {
            trueValue: "Y"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: "N"  //false값 지정 defalut-"N" ex) "N", false, "0"
          },
        },
        {
          headerName: '휴일',
          field: 'holiday',
          width: 80,
          editable: true,
          cellRenderer: 'numberInput',
          cellStyle:{textAlign: 'right'}
        },
        {
          headerName: '회사경조금(원)',
          field: 'expendAmt',
          width: 150,
          editable: true,
          cellRenderer: 'numberInput',
          cellStyle:{textAlign: 'right'}
        },
        {
          headerName: '비고',
          field: 'remark',
          width: 200,
          editable: true,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '수정일자',
          field: 'chgDtm',
          width: 150,
          cellStyle:()=>{
            return {textAlign: 'center', background:'#ededed'}
          },
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value)
          }
        },
        {
          headerName: '수정자',
          field: 'chgNm',
          width: 100,
          cellStyle:()=>{
            return {textAlign: 'center', background:'#ededed'}
          }
        },
      ]
    },
    onCellClicked(params){
      this.rowNode = params;
    },
    chkType(params) {
      this.combos['EXPEND_MONEY_CD'].forEach(x =>{
        if(params === x.key) params = x.value;
      })
      return params;
    },
    allChk(){

      const grid = this.$refs.grid
      const chkDatas = grid.rowData.filter((x,i) => {
        return x.regYn === "Y"
      })

      if(chkDatas.length === this.data.length){
        this.gridOptions.api.forEachNodeAfterFilter((v, i) => {
          v.data.regYn = "N"
          // this.subData.regYn = "0"
        })
      }else{
        this.gridOptions.api.forEachNodeAfterFilter((v, i) => {
          v.data.regYn = "Y"
          // this.subData.regYn = "1"
        })
      }


      // this.$forceUpdate()
      // this.forceRerender();
      grid.rowData.forEach((x,i)=>{
        this.mainGridRefresh(i);
      })

    },
    mainGridRefresh(idx) {
      var rows = [];
      var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
      rows.push(rowNode);
      this.gridApi.redrawRows({ rowNodes: rows });
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
      let [bizTripArea] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "EXPEND_MONEY_CD"}})]);
      this.combos['EXPEND_MONEY_CD'] = bizTripArea.data;
      this.createColumnDefs();
    },
    goOpen() {
      this.$store.commit("loading");
      this.$http
          .post("/api/expend/list", {
            compCd: this.form.compCd,
            basicDate: this.$moment(this.form.basicDate).format('YYYYMMDD'),
            expendCd : this.form.expendCd
          })
          .then(response => {
            this.data = response.data;
          }).catch((e) => {
            this.$swal({type: 'error', text: '조회 실패하였습니다.'})
          })
          .finally(() => {
            this.$store.commit('finish');
          });
    },
    save() {
      //중복 확인
      for (var i = 0; i < this.data.length; i++) {

        if(this.isEmpty(this.data[i].startDate) === ""){
          this.$swal({ type: "error", html: "시작 일자를 선택 하여 주세요."});
          return false;
        }else if(this.isEmpty(this.data[i].endDate) === ""){
          this.$swal({ type: "error", html: "종료 일자를 선택 하여 주세요."});
          return false;
        }else if(this.isEmpty(this.data[i].expendCd) === ""){
          this.$swal({ type: "error", html: "경조 구분을 선택 하여 주세요."});
          return false;
        }

        for (var j = i + 1; j < this.data.length; j++) {
          if (this.data[i].expendCd === this.data[j].expendCd && this.data[i].startDate === this.data[j].startDate) {
            this.$swal({
              type: "error",
              html: "시작일자 "+this.$moment(this.data[i].startDate).format('YYYY-MM-DD')+"일에 <br>"+this.chkType(this.data[i].expendCd)+" 경조가 이미 존재합니다."
            });
            return false;
          }
        }
      }

      this.data.forEach(x=>{
        x.holiday = this.subStringColumn(x.holiday,4);
        x.expendAmt = this.subStringColumn(x.expendAmt,8);
      })

      this.$http.post("/api/expend/save", this.data).then(response => {
        this.$swal({ type: "success", text: response.data });
        this.goOpen();
      });
    },
    addRow() {
      let index = this.data.length;
      this.data.push({
        basicDate : '',
        chgDtm : '',
        chgId : '',
        compCd : '',
        expendAmt : '',
        expendCd : '',
        holiday : '',
        mutualYn : '',
        regDtm : '',
        regId : '',
        remark : '',
        startDate: this.$moment().startOf('year').format('YYYYMMDD'),
        endDate: this.$moment().endOf('year').format('YYYYMMDD'),
        wreathYn : '',
        new : true,
        index : index
      });
    },
    deleteRow() {

      let vm = this;
      let list = [];

      this.data.forEach( (x,i) =>{
        if(x.regYn){
          list.push({
            startDate : x.startDate,
            expendCd : x.expendCd,
            new : x.new,
            index : i
          });
        }
      });

      let delList = list.filter(v => v.new !== true);
      vm.result = [];

      if(list.length == 0){
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
      }else{
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            delList.forEach(x=>{
              vm.result.push({
                compCd : this.$store.state.loginInfo.compCd,
                expendCd: x.expendCd,
                startDate: x.startDate
              })
            });

            for(var i = 0 ; i < list.length; i++ ){
              this.data.splice(list[i].index, 1)
            }

            this.$http.post('/api/expend/delete', vm.result).then((response) => {
              this.$swal({ type: 'success', text: '경조를 삭제 하였습니다.' })
                  .then((result) => {
                    this.goOpen();
                  });
              })
              .catch((e) => {
                this.$swal({ type: 'success', text: '경조 삭제를 실패 하였습니다.' })
              })
              .finally(() => {
                this.$store.commit('finish')
              });
          }
        })



      }
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
    popExpend(flag) {
      let vm = this

      this.$modal.open({
        component: Expend,
        props: {
          param: this.form.expendNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveExpend(obj)
          }
        }
      })
    },
    receiveExpend(obj) {
      this.form.expendCd = obj.detailCd;
      this.form.expendNm = obj.detailNm;
    },
    initExpend(force) {
      if (force === true) this.form.expendNm = '';
      if (this.form.expendNm === '') this.form.expendCd = '';
    },
    updateExpendItem(pVal){

      const vm = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);

      this.$modal.open({
        component: Expend,
        parent: this,
        width: 700,
        props: {
          data: {
          }
        },
        events: {
          close(object) {
            rowNode.setDataValue('expendCd', object.detailCd);
          }
        }
      });
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
      return value.length >= limit ? value.substring(0,limit): value;
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.data[rowIdx].regYn = params.node.isSelected();
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