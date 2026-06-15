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
        <label class="control-label-req" id="standardMonth">기준년월</label>
        <div class="form-input">
<!--          <div class="datepicker w-type-ymd">-->
            <!-- <dhx-calendar class="input" v-model="form.standardYymm" :config="config" /> -->
<!--            <monthly-picker v-model="form.baseYm">-->
<!--            </monthly-picker>-->
<!--          </div>-->
          <el-date-picker
              v-model="form.baseYm"
              type="month"
              format="yyyy-MM"
              value-format="yyyyMM"
              style="width: 60%;">
          </el-date-picker>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label">유종구분</label>
        <div class="form-input">
          <select class="input" v-model="form.oilKindCd">
            <option value="">전체</option>
            <option
                v-for="item in combos['OIL_KIND_CD']"
                :key="item.key"
                :value="item.key"
            >{{ item.value }}</option>
          </select>
        </div>
      </div>
    </div>

    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">유류단가 내역</h3>
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
                         :suppressRowClickSelection="true"

                         rowSelection="multiple"

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
import DhxCalendar from "@/components/DhxCalendar.vue";

import mixin from "@/mixin";
import mixinSlip from "@/mixin/slip";
import MonthlyPicker from '@/components/MonthlyPicker.vue'
import SelectCellRenderer from "@/components/agGrid/select-cell-renderer";
import NumberInputCellRenderer from "@/components/agGrid/numberinput-cell-renderer";
import DatepickerCellRenderer from "@/components/agGrid/el-datepicker-cell-renderer";

const bus = new Vue();

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
          bus.$emit("OIL_KIND_CD", response.data);
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
    MonthlyPicker
  },
  data() {
    return {
      title: "유류단가 관리",
      compCds: [],
      data: [],
      rowNode: null,
      deletedata: "",
      searchFlag: false,
      columnDefs: [],
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
        'OIL_KIND_CD': [],
      },
    };
  },
  methods: {
    constructGridSuccessful(grid) {
      grid.setDateFormat("%Y-%m", "%Y%m");
    },
    onGridReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    createColumnDefs() {
      const vm = this;

      this.columnDefs = [
        {
          headerName: '기준년월',
          field: 'baseYm',
          width: 200,
          headerClass: 'require-column',
          cellRenderer: 'datePicker',
          cellRendererParams: (params) => {
            return {
              type: 'month',
              format: 'yyyy-MM',  //date format
              valueFormat: 'yyyyMM',  //받는 model 의 결과 포맷
              disable: true
            }
          },
          valueFormatter: (params) => {
            return vm.getMonthFormat(params.value)
          },
          cellStyle: () => {
            return {textAlign: 'center'}
          },
          cellClass: function (params) {
            if (params.node.data.new) {
              return 'bg-lightpink';
            } else {
              return 'bg-grey';
            }
          },
          editable: function (params) {
            if (params.node.data.new) {
              return true;
            } else {
              return false;
            }
          },
        },
        {
          headerName: '유종',
          field: 'oilKindCd',
          width: 200,
          headerClass: 'require-column',
          cellRenderer: 'select',
          cellRendererParams: {
            options: vm.combos['OIL_KIND_CD'],
            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
          },
          cellStyle: () => {
            return {textAlign: 'center'}
          },
          cellClass: function (params) {
            if (params.node.data.new) {
              return 'bg-lightpink';
            } else {
              return 'bg-grey';
            }
          },
          editable: function (params) {
            if (params.node.data.new) {
              return true;
            } else {
              return false;
            }
          },
        },
        {
          headerName: '유류단가',
          field: 'oilUpce',
          width: 200,
          editable: true,
          // cellRenderer: 'numberInput',
          cellStyle: {textAlign: 'right'}
        },
        {
          headerName: '연비(Km/L)',
          field: 'oilEff',
          width: 200,
          editable: true,
          // cellRenderer: 'numberInput',
          cellStyle: {textAlign: 'right'}
        },
        {
          headerName: '비고',
          field: 'remark',
          width: 400,
          editable: true,
          cellStyle: {textAlign: 'left'}
        },
        {
          headerName: '수정일자',
          field: 'chgDtm',
          width: 200,
          cellStyle: () => {
            return {textAlign: 'center', background: '#ededed'}
          },
          valueFormatter: (params) => {
            return vm.getDtmFormat(params.value)
          }
        },
        {
          headerName: '수정자',
          field: 'chgNm',
          width: 150,
          cellStyle: () => {
            return {textAlign: 'center', background: '#ededed'}
          }
        },
      ]
    },
    onCellClicked(params) {
      console.log(params);
      this.rowNode = params;
    },
    getCompCdCombo() {
      this.$http
          .get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})
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
      this.searchFlag = true;
      this.$store.commit("loading");
      this.$http
          .post("/api/oilPrice/list", {
            compCd: this.form.compCd,
            baseYm: this.form.baseYm,
            oilKindCd: this.form.oilKindCd
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

      let nanFlag = false;

      if(!this.searchFlag){
        this.$swal({type: "error", text: "기준년월 변경 이후에는 조회를 눌러주세요."});
        return;
      }

      //유종 중복 확인
      for (var i = 0; i < this.data.length; i++) {

        if(isNaN(this.isEmpty(this.data[i].oilUpce))){
          nanFlag = true;
        }

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

      if(nanFlag){
        this.$swal({type: "error",text: "유류단가는 숫자만 입력 가능합니다."});
        return false;
      }

      this.data.forEach(x => {
        x.oilUpce = this.subStringColumn(x.oilUpce, 10);
        x.oilEff = this.subStringColumn(x.oilEff, 5);
      })

      this.$http.post("/api/oilPrice/save", this.data).then(response => {
        this.$swal({type: "success", text: response.data});
        this.goOpen();
        return;
      });
    },
    addRow() {

      if(!this.searchFlag){
        this.$swal({type: "error", text: "기준년월 변경 이후에는 조회를 눌러주세요."});
        return;
      }

      this.data.push({
        baseYm: this.$moment(this.form.baseYm).format("YYYYMM"),
        oilKindCd: options["OIL_KIND_CD"][0].detailCd,
        oilUpce: "",
        oilEff: "",
        new: true
      });
    },
    deleteRow() {

      if(!this.searchFlag){
        this.$swal({type: "error", text: "기준년월 변경 이후에는 조회를 눌러주세요."});
        return;
      }

      if (this.rowNode == null) {
        this.$swal({
          type: 'error',
          text: '삭제할 행을 선택하여주세요.'
        })
        return;
      }

      const vm = this;

      const rowData = this.rowNode.data;

      const rowIndex = this.rowNode.rowIndex;
      const isNew = this.data[rowIndex].new;

      if (isNew) {
        this.data.splice(rowIndex, 1)
      } else {
        if (!isNew) {
          this.$swal({
            type: 'question',
            text: '선택한 항목을 삭제하시겠습니까?',
            showCancelButton: true
          }).then(r => {
            if (r.value) {
              this.$store.commit('loading')

              this.$http.post('/api/oilPrice/delete', {
                baseYm: rowData.baseYm,
                oilKindCd: rowData.oilKindCd
              }).then(response => {
                this.$swal({type: "success", text: '해당 유종을 삭제하였습니다.'});
                vm.goOpen();
              }).catch((e) => {
                this.$swal({type: 'error', text: '삭제 실패하였습니다.'})
              })
              .finally(() => {
                this.$store.commit('finish')
              });
            }
          })
        } else {
          this.data.splice(rowIndex, 1)
        }
      }
    },
    getMonthFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM')
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
    saveExcel() {
      // this.downloadExcel(this.$refs.grid);
      this.gridOptions.api.exportDataAsExcel({fileName: '유류단가'});
    },
    isEmpty(value) {
      if (value == "" || value == null || value == undefined || (value != null && typeof value == "object" && !Object.keys(value).length)) {
        return ""
      } else {
        return value
      }
    },
    subStringColumn(value, limit) {
      value = this.isEmpty(value);
      value = value.toString();
      return value.length >= limit ? value = value.toString().substring(0, limit) : value;
    },
  },
  created() {
    this.getCombos();
    queryMngItemCd.apply(this, []);
  },
  mounted() {
    this.getCompCdCombo();
    this.goOpen();
  },
  watch: {
    'form.baseYm'() {
      this.searchFlag = false;
    },
  },
};
</script>