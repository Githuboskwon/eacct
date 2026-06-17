<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-check" @click="sendMail">발송</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="item search_wrap">
        <div class="search_box" style="width: 35%">
          <div class="search_title">
            <span class="search_tit" style="color: #CC3D3D;">- 기준일자</span>
          </div>
          <div class="search_con search-area">
            <el-date-picker
                v-model="form.searchDt"
                unlink-panels
                type="date"
                format="yyyyMMdd"
                value-format="yyyyMMdd">
            </el-date-picker>
          </div>
        </div>
        <div class="search_box" style="width: 35%">
          <div class="search_title">
            <span class="search_tit">- 작업구분</span>
          </div>
          <div class="search_con" style="width: 30%">
            <select class="select is-fullwidth w200p" v-model="mailTypeCd">
              <option
                  v-for="item in mailTypeCombo"
                  :key="item.key"
                  :value="item.key"
                  v-text="item.value"
              />
            </select>
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
            <h3 class="ico_table_name"></h3>
          </div>
        </div>

        <div class="grid-wrap">
          <ag-grid-vue
              ref="grid"
              style="width: 100%;"
              class="ag-theme-alpine grid_search_height2"

              :columnDefs="columnDefs"
              :gridOptions="gridOptions"
              :rowData="mailSendList"
              :defaultColDef="defaultColDef"
              :frameworkComponents="frameworkComponent"
              :enableRangeSelection="true"
              :suppressRowClickSelection="true"
              rowSelection="multiple"

              @grid-ready="onReady"
              @row-selected="onRowSelected"
              @cell-clicked="onCellClicked"
              @cell-value-changed="cellValueChange"/>
        </div>
      </div>

    </div>
    <!-- //테이블 -->

  </div>
</template>

<script>
import Vue from 'vue';
import mitt from 'mitt';
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import slip from '@/mixin/slip-basic'
import _ from 'lodash'

import DhxCalendar from '@/components/DhxCalendar.vue'
import {AgGridVue} from 'ag-grid-vue';
import AgGridCheckbox from "@/components/agGrid/AgGridCheckbox.vue"
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";
import MailSendPop from "@/components/MailSendPop.vue";

const bus = mitt()
export default {
  name: 'MailSendMng',
  mixins: [mixin, mixinSlip, slip],
  components: {DhxCalendar, AgGridVue,AgGridCheckbox},
  props: {
    params: {
      type: Object,
      required: false
    },
  },
  data() {
    return {
      title: '메일발송',
      mailTypeCombo: [],
      mailTypeCd: '01',
      form: {
        searchDt: this.$moment().startOf('month').format('YYYYMMDD'),
        mailTypeCd:'01',
      },
      mailSendList: [],
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
      columnDefs:[],
      defaultColDef: {
        resizable: true,
        filter:true,
        sortable: true
      },
      chkYn: 'N',
      frameworkComponent : {
        'checkBox' : AgGridCheckbox,
        checkboxRenderer: CheckboxCellRenderer,
      },
    };
  },
  methods: {
    makeColDef(){
      this.columnDefs =[
        {
          headerName: '',
          field:'regYn',
          width : 60,
          cellStyle:{textAlign: 'center'},
          headerCheckboxSelection: true,
          checkboxSelection: true,
        },
        {
          headerName:'No.',
          field:'no',
          width:60,
          valueFormatter: function(params) {
            return params.node.rowIndex + 1;
          }
        },
        {
          headerName:'사원번호',
          field:'empNo',
          width:130,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName:'사원명',
          field:'empNm',
          width:130,
          cellStyle:{textAlign: 'center'},
        },
        {
          headerName:'부서',
          field:'deptNm',
          width:130,
        },
        {
          headerName:'직급',
          field:'jobNm',
          width:130,
        },
        {
          headerName:'미처리 건수',
          field:'count',
          width:130,
          cellStyle:{textAlign: 'right'},
        },
        {
          headerName:'발송자',
          field:'sendUserNm',
          width:130,
        },
        {
          headerName:'발송시간',
          field:'mailSendDt',
          width:180,
          valueFormatter: (params) => {
            return this.getDtmFormat(params.value);
          }
        },

      ]
    },
    onReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    onCellClicked(params) {
      var field = params.colDef.field;

    },
    getMailTypeCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "MAIL_TYPE_CD"}})
      .then(response => {
        this.mailTypeCombo = response.data;
      });
    },
    cellValueChange(params){

    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.mailSendList[rowIdx].regYn = params.node.isSelected();
    },
    goSearch() {

      this.form.mailTypeCd = this.mailTypeCd;
      this.$store.commit('loading');
      if(this.form.mailTypeCd === '01') {
        this.$http.post(`/api/mailSend/unAppr`, this.form)
        .then(response => {

          this.mailSendList = response.data;
          this.chkYn = false;

        }).finally(() => {
          this.$store.commit('finish')
        });
      } else if(this.form.mailTypeCd === '02') {
        this.$http.post(`/api/mailSend/unTr`, this.form)
        .then(response => {

          this.mailSendList = response.data;
          this.chkYn = false;

        }).finally(() => {
          this.$store.commit('finish')
        });
      }
    },
    allChk(){
      var filterList = [];

      this.gridOptions.api.forEachNodeAfterFilter((v, i) => {
        filterList.push(v.data.slipNo)
      })

      this.chkYn = this.chkYn == 'Y' ? 'N' : 'Y';

      for(var i=0; i<filterList.length; i++){
        for(var j=0; j<this.slipList.length; j++){
          if(filterList[i] === this.slipList[j].slipNo){
            this.slipList[j].useYn = this.chkYn;
            if (this.chkYn) {
              this.cellValueChange({ data: this.slipList[j] });
            }else{
              this.totalAmt = 0;
            }
          }
        }
      }

      var size = this.$refs.grid.$children.length;
      for(var k=0; k<size; k++){
        this.$refs.grid.$children[k].select = this.chkYn;
      }

    },
    sendMail() {
      let that = this;
      let sendList = this.mailSendList.filter((x) => x.regYn);
      this.$modal.open({
        component: MailSendPop,
        props: {
          mailTypeCd: this.form.mailTypeCd,
          sendList,
        },
        parent: this,
        width: 1500,
        height: 1000,
        events: {
          close(data) {
            that.goSearch();
          },
        },
      });

    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },

  },
  created() {
    this.getMailTypeCombo();
    this.makeColDef();
    this.goSearch();
  },
  mounted() {

  },
  watch: {
  }
};
</script>

<style scoped>
.desc-content:after {
  clear: both;
  content: '';
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
  float:left
}

.desc-content .item .desc-item:last-child {
  margin-bottom: 0;
  height: 25px;
  float:left
}

.desc-content .item .desc-item .tit {
  position: absolute;
  left: 0;
}

.desc-content .item .desc-item .label-tit {
  font-family: 'NotoM';
  color: #222;
  font-size: 15px;
}

.desc-content .item.desc-left .desc-item {
  padding-left: 90px;
}

.desc-content .item.desc-left .desc-item .desc:after {
  clear: both;
  content: '';
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
  content: '';
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
  width: 40%;
  padding-right: 20px;
}

.desc-content .item.desc-center {
  width: 35%;
  padding-right: 40px;
}

.desc-content .item.desc-right {
  width: 25%;
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
.search-border .td-s-thumb.search-area > .ip-box
.search-border .td-s-thumb.search-area > button {
  float: left;
}

.search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
  width: 60%;
  margin-left: 6px;
}

.search-border .td-s-thumb.search-area .ip-box {
  vertical-align: top;
}

@media (max-width: 1580px) {
  .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
    width: 50%;
  }
}
</style>
