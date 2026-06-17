<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goOpen">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save">저장</el-button>
      </div>
    </div>
    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
        <label class="control-label" id="standardMonth">PJT 관리번호</label>
        <div class="form-input" style="width: 33%;">
          <input class="input" type="text" v-model="searchProjectManageNo" @keypress.enter="popProjectMng">
          <button class="icon is-right" @click="popProjectMng(true)"><i class="fas fa-search"></i>
          </button>
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
                      <input class="input" style="width:150px; background-color: #ffffff;" type="text" v-model="form.projectManageNo" readonly/>
                      <input class="input" style="width:50px; background-color: #ffffff;" type="text" v-model="form.degree" readonly/>
                      <input class="input" style="width:50px; background-color: #ffffff;" type="text" value="차수" readonly/>
                    </td>

                    <th>시공현장명</th>
                    <td>
                      <input class="input" type="text" style="background-color: #ffffff;" v-model="form.projectName" readonly/>
                    </td>

                    <th>소장/담당자</th>
                    <td>
                      <input class="input" style="width:100px; background-color: #ffffff;" type="text" v-model="form.pjtSiteManagerUserId" readonly/>
                      <input class="input" style="width:100px; background-color: #ffffff;" type="text" v-model="form.pjtSiteManagerUserName" readonly/>
                    </td>

                    <th>상태</th>
                    <td>
                      <input class="input" type="text" style="background-color: #ffffff;" v-model="form.location" readonly/>
                    </td>
                  </tr>
                  <tr>
                    <th>기간</th>
                    <td>
                      <input class="input" style="width:130px; background-color: #ffffff;" type="text" v-model="form.startDate" readonly/>
                      ~
                      <input class="input" style="width:130px; background-color: #ffffff;" type="text" v-model="form.endDate" readonly/>
                    </td>

                    <th>영업담당</th>
                    <td>
                      <input class="input" type="text" style="width:120px; background-color: #ffffff;" v-model="form.pjtBusinessUserId" readonly/>
                      <input class="input" type="text" style="width:120px; background-color: #ffffff;" v-model="form.pjtBusinessUserName" readonly/>
                    </td>

                    <th>고객</th>
                    <td>
                    </td>

                    <th>수주금액</th>
                    <td>
                      <input class="input" type="text" style="background-color: #ffffff;" v-model="form.ctAmount" readonly/>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          <div class="table-name">
            <h3 class="ico_table_name">PJT공정률계획및실적 내역</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">

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

                         @cell-value-changed="cellValueChange"
                         @grid-ready="onGridReady"
                         @cell-clicked="onCellClicked"/>
          </div>
        </div>


              <!-- 상세정보 -->
<!--              <div class="table-a fixed-th">-->
<!--                <table id="basic" class="table">-->
<!--                  <colgroup>-->
<!--                    <col width="10%"><col width="57%">-->
<!--                  </colgroup>-->
<!--                  <tbody>-->
<!--                  <tr>-->
<!--                    <th>선택년월</th>-->
<!--                    <td>-->
<!--                      <input class="input" style="width:100px;" type="text" v-model="form.endDate" />-->
<!--                      <span></span>-->
<!--                      <button class="btn-size btn-gray fl_right" @click="save">-->
<!--                        <span class="btn-icon">-->
<!--                          <i class="fas fa-check"></i>-->
<!--                        </span>-->
<!--                        비고저장-->
<!--                      </button>-->
<!--                    </td>-->
<!--                  </tr>-->

<!--                  <tr>-->
<!--                    <th>비고</th>-->
<!--                    <td>-->
<!--                      <input class="input" type="text" style="height: 200px" v-model="form.registrationNum"/>-->
<!--                    </td>-->
<!--                  </tr>-->

<!--                  </tbody>-->
<!--                </table>-->
<!--              </div>-->

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
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import NumberInputCellRenderer from "@/components/agGrid/numberinput-cell-renderer";
import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer";
import ProjectMngPop from "@/components/ims/ProjectMngPop";

const bus = createBus();

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
  name: "ProjectConsLst",
  mixins: [mixin, mixinSlip],
  components: {
    DhxCalendar
  },
  data() {
    return {
      title: "PJT공정률계획및실적",
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
      gridApi: null,
      columnApi: null,
      config: {
        hideTime: false,
        format: "YYYY-MM",
        outputFormat: "YYYYMM"
      },
      searchProjectManageNo: '',
      form: {
        compCd: "",
        projectManageNo: '',
        projectCd: '',
        projectNm: '',
        projectGubun: '',
        projectManageNm: '',
        startDate: '',
        endDate: '',
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

      this.form.monthList = this.getDateRange(this.$moment(this.form.startDate).format('YYYY-MM') , this.$moment(this.form.endDate).format('YYYY-MM'));

      this.columnDefs = [
        {
          headerName: ''
          , headerCheckboxSelection: true
          , checkboxSelection : (params) => {
            return !vm.chkStatus(params.data.ITEM_NAME)
          }
          , field:'regYn'
          , width : 80
          , cellStyle:{textAlign: 'center'}
          , editable: false
        },
        {
          headerName: '항목',
          field: 'ITEM_CODE',
          hide: true,
        },
        {
          headerName: '항목',
          field: 'ITEM_NAME',
          width: 150,
          editable: false,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '목표(%)',
          field: 'OBJECTIVE_ALL',
          width: 100,
          editable: false,
          cellStyle:{textAlign: 'right'}
        },
        {
          headerName: '공정률(%)',
          field: 'M_TOTAL',
          width: 200,
          editable: false,
          cellRenderer: 'numberInput',
          cellRendererParams: (params) => {
            return {
              disable: params.node.rowIndex === 2 ? true : false,
            }
          },
          // editable: (params) => {
          //   return params.node.rowIndex === 2 ? false : true;
          // },
          valueFormatter: (params) => {
            return vm.getDoubleNumberFormat(params.value);
          },
          // cellRenderer: 'numberInput',
          // cellRendererParams: {
          //   type:'double'
          // },
          cellStyle:{textAlign: 'right'}
        },

      ]


      if(this.data.length > 0){
        this.compareValueAddRow(this.data[0],this.data[1]);
      }

      this.form.monthList.map( (data ,idx) => {
        const column = {
          headerName: idx === 0 ? "M ["+data+"]" : "M"+idx+" ["+data+"]",
          field: idx === 0 ? "M" : "M"+idx,
          width: 100,
          cellRenderer: 'numberInput',
          cellRendererParams: (params) => {
            return {
              disable: params.node.rowIndex === 2 ? true : false,
            }
          },
          editable: (params) => {
            // return params.node.rowIndex === 2 ? false : true;
            return false;
          },
          cellStyle:{textAlign: 'right'},
          valueFormatter: (params) => {
            return vm.getDoubleNumberFormat(params.value);
          },
        };
        this.columnDefs.push(column);
      });
    },
    compareValueAddRow(row1,row2){

      const addRow = {};
      addRow['ITEM_CODE'] = 'D';
      addRow['ITEM_NAME'] = '차이';
      addRow['M_TOTAL'] = row1['M_TOTAL'] - row2['M_TOTAL'];
      addRow['OBJECTIVE_ALL'] = row1['OBJECTIVE_ALL'] - row2['OBJECTIVE_ALL'];

      this.form.monthList.map( (data ,idx) => {
        const rowdata1 = eval("row1." + (idx === 0 ? "M" : "M" + idx));
        const rowdata2 = eval("row2." + (idx === 0 ? "M" : "M" + idx));
        const propertyName = idx === 0 ? "M" : "M" + idx;
        addRow[propertyName] = rowdata1 - rowdata2;
      });

      this.data.push(addRow);
    },
    chkStatus(itemGroupCd) {
      return itemGroupCd == '차이' ? true : false;
    },
    cellValueChange(params){
      const rowNode = params.api.getRowNode(params.node.rowIndex);
      this.data.splice(2);
      this.createColumnDefs();
      rowNode.setSelected(true);
      console.log(rowNode);
      this.$forceUpdate();
    },
    onCellClicked(params){
      this.rowNode = params;
    },
    getDateRange(startDate, endDate){
      const start = new Date(startDate);
      const end = new Date(endDate);

      const result = [];

      while (start <= end) {
        result.push(this.$moment(start.toISOString().split('T')[0]).format('YYYYMM'));
        start.setMonth(start.getMonth() + 1);
      }

      return result
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

      if(this.isEmpty(this.searchProjectManageNo) === ""){
        this.$swal({type: "error",text: "PJT 관리번호를 입력해주세요."});
        return false;
      }
      this.$store.commit('loading')
      this.$http
          .post("/api/pjt/process/list", {
            projectManageNo: this.searchProjectManageNo
          })
          .then(response => {
            if(response.data.length == 0){
              this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
            }else{
              this.form = response.data.info;
              this.form.ctAmount = this.getNumberFormat(this.form.ctAmount);
              this.data = response.data.list;
              this.createColumnDefs();
            }
          })
          .finally(() => {
            this.$store.commit('finish')
          });
    },
    save() {

      let vm = this;
      const list = this.gridApi.getSelectedRows().filter(f => f.ITEM_NAME !== '차이');

      const param = [];

      if(list.length > 0) {

        this.$swal({
          type: 'question',
          html: '저장 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
            if (r.value) {
              this.$store.commit('loading');

              list.forEach((x,i)=>{

                this.form.monthList.map( (data ,idx) => {

                  let ob = {
                    monthText : (idx === 0 ? "M" : "M" + idx),
                    yyyymm : data,
                    periodYear : data.toString().substring(0,4),
                    periodMonth : data.toString().substring(4,6),
                    objectiveMon : eval("list["+i+"]." + (idx === 0 ? "M" : "M" + idx)),
                    objectiveAll : list[i].OBJECTIVE_ALL,
                    itemCode : list[i].ITEM_CODE,
                  }
                  param.push(ob);

                });

              });

              console.log(param);

              this.$http.post(`/api/pjt/process/save`, {saveList : param,
                                                                info : this.form})
                  .then(response => {
                    this.$swal({type: 'success', text: '저장 하였습니다.'})
                        .then((result) => {
                          if (result.value) {
                            this.goOpen();
                          }
                        });
                  }).catch(e => {
                this.$swal({type: 'error', text: e.response.data.message})
              }).finally(() => {
                this.$store.commit('finish');
              });


            }
          });
      }else {
        this.$swal({ type: 'info', text: '저장할 항목을 선택하세요.' });
      }

    },
    popProjectMng(){
      const vm  = this;

      this.$modal.open({
        width: 800,
        component: ProjectMngPop,
        parent: this,
        props: {
          param: this.searchProjectManageNo
        },
        events: {
          close(data) {
            vm.receiveProMng(data)
            vm.$forceUpdate();
          },
        }
      })
    },
    receiveProMng(data) {
      this.searchProjectManageNo = data.projectManageNo;
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
    getNumberFormat(val) {
      if(val){

        if(typeof val === "string"){
          val = val.replace(/[^0-9]/g, "")
        }

        return Math.floor(val).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
    },
    getDoubleNumberFormat(val) {
      if(val) {
        val = val.toString().replace(/\D/g, "");
        val = Math.floor(val).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
        val = this.$numeral(val).format('-0,0.00');
        return val;
      }else{
        return this.$numeral(this.isEmpty(val)).format('-0,0.00');
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
    // this.goOpen();
  }
};
</script>