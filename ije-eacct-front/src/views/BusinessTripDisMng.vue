<template>
  <div class="inner-box">
      <div class="content-name">
        <h2 class="dp-ib">출장거리 관리</h2>
        <div class="btn-type1 clearfix">
          <el-button size="large" type="primary" icon="el-icon-search" @click="search()">조회</el-button>
          <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save()">저장</el-button>
        </div>
      </div>

      <!-- 검색영역 -->
      <div class="search-form">
        <div class="form-group">
          <label class="control-label-req" id="standardMonth">기준년월</label>
          <div class="form-input">
            <el-date-picker
                v-model="form.standardYymm"
                type="month"
                format="yyyy-MM"
                value-format="yyyyMM"
            >
            </el-date-picker>
<!--            <div class="datepicker w-type-ymd">-->
              <!-- <dhx-calendar class="input" v-model="form.standardYymm" :config="config" /> -->
<!--              <monthly-picker v-model="form.standardYymm">-->
<!--              </monthly-picker>-->
<!--            </div>-->
          </div>
        </div>
        <div class="form-group">
          <label class="control-label">출발-도착지</label>
            <div class="form-input">
              <select class="input" v-model="form.departureArrivalArea">
                <option value="">==선택==</option>
                <option
                  v-for="item in combos['BUSINESS_TRIP_AREA_CD']"
                  :key="item.key"
                  :value="item.key"
                >{{ item.value }}</option>
              </select>
            </div>
          </div>
      </div>

      <!-- //검색영역 -->

    <div class="table-area mt20">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">출장거리 내역</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="info" icon="el-icon-plus" @click="addRow">행추가</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="removeRow()">행삭제</el-button>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height"
                         rowSelection="multiple" 
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"
                         :suppressRowClickSelection="true"
                         @grid-ready="onReady"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import _ from 'lodash';
import mixin from '@/mixin'
import mixinSlip from '@/mixin/slip'
import common from '@/mixin/common';
import DhxCalendar from "@/components/DhxCalendar.vue";
import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'
import DatepickerCellRenderer from "@/components/agGrid/el-datepicker-cell-renderer";
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
import MonthlyPicker from '@/components/MonthlyPicker.vue'
import MonthlyPickerCellRenderer from '@/components/grid/GridMonthlyPicker.vue'

import { AgGridVue } from 'ag-grid-vue'

export default {
  name: 'businessTripDisMng',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, MonthlyPicker
  },
  data() {
    return {
      columnDefs : [],
      gridOptions : {
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
      gridApi : null ,    //gridApi
      columnApi : null ,  //columApi
      data: [],
      config: {
        hideTime: true,
        format: "YYYY-MM",
        outputFormat: "YYYYMM",
        dateFormat: '%Y-%m'
      },
      form: {
        compCd: '',
        standardYymm: this.$moment().format("YYYYMM"), // 기준년월
        departureArrivalArea: '', // 출발도착지
        distance: '', // 거리
        remark: '', // 비고
        useYn: 'Y',  //사용여부
      },
      combos: {
        'BUSINESS_TRIP_AREA_CD' : [],
      },
      frameworkComponents: {
        select: SelectCellRenderer,
        numberInput: NumberInputCellRenderer,
        datePicker: DatepickerCellRenderer,
        monthlyPicker: MonthlyPickerCellRenderer,
      },
    }
  },
  created() {
    this.getCombos();
    this.search();
  },
  methods: {
    onReady() {
      //그리드용 api 정의
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    async getCombos() {
      let [bizTripArea] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "BUSINESS_TRIP_AREA_CD"}})]);
      this.combos['BUSINESS_TRIP_AREA_CD'] = bizTripArea.data;
      this.createColumnDefs();
    },
    createColumnDefs(){
      const vm = this;
      this.columnDefs = [
        {
          headerName: 'No.',
          width: 100,
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          },
          editable:false
        },
        {
          headerName: '기준년월',
          field: 'standardYymm',
          width: 200,
          headerClass: 'require-column',
          cellRenderer: 'datePicker',
          // cellRendererParams: {
          //     config: {
          //       hideTime: true,
          //       format: "YYYY-MM",
          //       outputFormat: "YYYYMM",
          //       dateFormat: '%Y-%m'
          //     },
          // },
          cellRendererParams: (params) => {
            return {
              type: 'month',
              format: 'yyyy-MM',  //date format
              valueFormat: 'yyyyMM',  //받는 model 의 결과 포맷
              disable: params.node.data.new ? false : true
            }
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
          headerName: '출발-도착지',
          field: 'departureArrivalArea',
          width: 300,
          headerClass: 'require-column',
          cellRenderer: 'select',
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
          cellRendererParams: {
            options : vm.combos['BUSINESS_TRIP_AREA_CD'],
            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
          },
        },
        {
          headerName: '거리 (km)',
          field: 'distance',
          width: 200,
          editable: true,
          cellRenderer: 'numberInput',
          cellStyle:{textAlign: 'right'}
        },
        {
          headerName: '비고',
          field: 'remark',
          width: 500,
          editable: true,
          cellStyle:{textAlign: 'left'}
        },
        {
          headerName: '수정일자',
          field: 'chgDtm',
          width: 150,
          cellClass: function(){
            return 'bg-grey'
          },
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value)
          },
          cellStyle:{textAlign: 'center'}
        },
        {
          headerName: '수정자',
          field: 'chgNm',
          width: 150,
          cellClass: function(){
            return 'bg-grey'
          },
        },
      ]
    },
    search() {
      this.$store.commit('loading')
      this.$http.post('/api/businessTripDis/list', {
        standardYymm : this.form.standardYymm,
        departureArrivalArea : this.form.departureArrivalArea
      })
      .then(response => {
        this.data = response.data;
        if(response.data.length == 0){
            this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
          }  
      })
      .finally(() => {
        this.$store.commit('finish')
      })
    },
    save() {
      if(!this.gridRequireCheck([this.$refs.grid])) return;
      for (var i = 0; i < this.data.length; i++) {
        for (var j = i + 1; j < this.data.length; j++) {
          
          if (this.data[i].departureArrivalArea === this.data[j].departureArrivalArea && this.data[i].standardYymm === this.data[j].standardYymm) {
            this.$swal({
              type: "error",
              text: "해당 출장거리가 이미 존재합니다."
            });
            return false;
          }
        }
      }
      this.$http.post('/api/businessTripDis/save', this.data)
        .then(() => {
          this.$swal({type: 'success', text: '저장되었습니다.'});
          this.search();
        })
        .catch(error => {
          this.$swal({
            type:'error', text: error.data.message,
          })
        })
    },
    addRow() {
      this.data.push({
        standardYymm: this.$moment().format("YYYYMM"),
        departureArrivalArea: this.combos['BUSINESS_TRIP_AREA_CD'][0].key,
        distance: '',
        remark: '',
        useYn: 'Y',
        compCd: this.$store.state.loginInfo.compCd,
        new: true,
      })
    },
    removeRow() {
      let row = this.gridApi.getSelectedRows()[0];
      if(!row) {
        this.$swal({type:'error', text: '삭제할 행을 선택해주세요.'})
        return false;
      }
      const rowIndex = this.gridApi.getSelectedNodes()[0].childIndex;
      const isNew = row.new;
      const standardYymm = row.standardYymm;
      const departureArrivalArea = row.departureArrivalArea;

      if(!isNew){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then((r) => {
          if(r.value) {
            this.$http.post('/api/businessTripDis/delete', {standardYymm, departureArrivalArea})
            .then(() => {
              this.data.splice(rowIndex, 1);
              this.$swal({type: 'info', text: '삭제되었습니다.'})
            })
            .catch((err) => {
              console.error(err);
            })
          }
        })
      } else {
        this.data.splice(rowIndex, 1);
      }
    },
  },
}
</script>
