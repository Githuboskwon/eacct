<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <!-- <button type="button" class="btn-size btn-gray fl_left" @click="goSearch()">
          <span class="btn-icon"><i class="fas fa-search"></i></span>
          조회
        </button>
        <button type="button" class="btn-size btn-blue fl_left" @click="pull()">
          <span class="btn-icon"><i class="fas fa-save"></i></span>
          가져오기
        </button> -->
        <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-download" @click="pull">가져오기</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="form-group">
        <label class="control-label-req" id="standardMonth">GL일자</label>
        <div class="form-input">
<!--          <div class="datepicker w-type-ymd">-->
<!--            <dhx-calendar class="input" v-model="form.searchFrom" :config="config"/>-->
<!--          </div>-->
<!--          <span> ~ </span>-->
<!--          <div class="datepicker w-type-ymd">-->
<!--            <dhx-calendar class="input" v-model="form.searchTo" :config="config"/>-->
<!--          </div>-->
          <el-date-picker
              v-model="searchDt"
              type="daterange"
              align="right"
              unlink-panels
              style="width: 260px;"
              range-separator="~"
              start-placeholder="시작일"
              end-placeholder="종료일">
          </el-date-picker>
        </div>
      </div>

      <div class="form-group">
        <button class="item-list icon is-right btn_s_w" @click="openModal()" type="button">상세검색<i class="fas fa-plus"></i></button>
      </div>


      <!-- 상세검색 내용 -->
      <div id="open-modal" class="modal-window">
        <div class="modal-window-wrap">
          <div class="modal-window-top">
            <h4>상세검색</h4>
            <button title="Close" @click="closeModal()" type="button" class="bt-modal-close mt5"><img src="../../public/img/bt_close_w.png" /></button>
          </div>

          <div class="search_box_wrap">

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">GL일자</span>
              </div>
              <div class="search_con search-area">
                <div class="form-input">
<!--                  <div class="datepicker w-type-ymd">-->
<!--                    <dhx-calendar class="input" v-model="form.searchFrom" :config="config"/>-->
<!--                  </div>-->
<!--                  <span class="wave"> ~ </span>-->
<!--                  <div class="datepicker w-type-ymd">-->
<!--                    <dhx-calendar class="input" v-model="form.searchTo" :config="config"/>-->
<!--                  </div>-->
                  <el-date-picker
                      v-model="searchDt"
                      type="daterange"
                      align="right"
                      unlink-panels
                      style="width: 100%"
                      range-separator="~"
                      start-placeholder="시작일"
                      end-placeholder="종료일">
                  </el-date-picker>
                </div>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">배치명</span>
              </div>
              <div class="search_con search-area">
                <input class="input" type="text" v-model="form.batchNm">
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">분개명</span>
              </div>
              <div class="search_con search-area">
                <input class="input" type="text" v-model="form.headerNm">
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">원천</span>
              </div>
              <div class="search_con search-area">
                  <select class="select is-fullwidth w100p" v-model="form.sourceNm">
                    <option value="">==전체==</option>
                    <option
                      v-for="item in sourceTypeCombo"
                      :key="item.value"
                      :value="item.value"
                      v-text="item.value"
                    />
                  </select>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                  <span class="search_tit">증빙첨부</span>
              </div>
              <div class="search_con search-area">
                <select class="select is-fullwidth" v-model="form.fileYn">
                  <option value="">==전체==</option>
                  <option value="Y">첨부</option>
                  <option value="N">미첨부</option>
                </select>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                  <span class="search_tit">기안서 연동</span>
              </div>
              <div class="search_con search-area">
                <select class="select is-fullwidth" v-model="form.jiniYn">
                  <option value="">==전체==</option>
                  <option value="Y">연동</option>
                  <option value="N">미연동</option>
                </select>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">전표유형</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.glSlipTypeCd" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.glSlipTypeNm" @input="initGlSlipTypeNm" @keypress.enter="popGlSlipTypeNm">
                  <button class="icon is-right" @click="popGlSlipTypeNm"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">거래유형</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.trxTypeCd" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.trxTypeNm" @input="initTrxTypeNm" @keypress.enter="popTrxTypeNm">
                  <button class="icon is-right" @click="popTrxTypeNm"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">작성자</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.transferredBy" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.transferredByNm" @input="initTransfferedBy" @keypress.enter="popTransfferedBy" >
                  <button class="icon is-right" @click="popTransfferedBy(true)"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>

          </div>

          <div class="modal-window-bottom">
            <ul>
              <li>
                <button class="bt_blue_s" @click="goSearch()">검색</button>
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
    <!-- //검색조건 영역 -->


    <div class="table-area mt20">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">전표내역</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="primary" icon="el-icon-tickets" @click="openBulkEvidencePopup()">일괄파일증빙</el-button>
            <el-button type="success" icon="el-icon-document-checked" @click="openBulkDrfPlanPopup()">기안서 일괄적용</el-button>
            <el-button type="success" icon="el-icon-folder-opened" @click="approvalBundle()">상신</el-button>
            <el-button type="danger" icon="el-icon-close" @click="removeRow()">전송취소</el-button>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
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
                         @grid-ready="onReady"
                         @row-selected="onRowSelected"
                         @cell-value-changed="cellValueChange"
                         @rowDoubleClicked="rowDoubleClick"
                         @cell-clicked="onCellClicked"
            />
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
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'
import DatepickerCellRenderer from '@/components/agGrid/datepicker-cell-renderer'
import AgGridSearchBtn from '@/components/agGrid/AgGridSearchBtn';
import VendorPop from '@/components/Vendor_Ag.vue';
import { AgGridVue } from 'ag-grid-vue';
import SlipGlDetailModal from '@/components/SlipGlDetailModal.vue';
import EmpPop from '../components/Emp_Ag.vue';
import TrxPop from "@/components/TrxSearchPop"
import EvidAtchBatchPop from '@/components/EvidAtchBatchPop';
import AgGridScanAttach from "@/components/agGrid/AgGridScanAttach.vue"
import JiniAtchBatchPop from '../components/JiniAtchBatchPop.vue';
import ApprBundleSubmTemp from "@/views/ApprBundleSubmTemp.vue";
import GlSlipTypePop from "@/components/GlSlipTypePop.vue";
import EvidAtchPopModeless from "@/components/EvidAtchPopModeless.vue";
import JiniAtchPop from "@/components/JiniAtchPop.vue";

export default {
  name: 'GLSlipList',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, VendorPop, EmpPop
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
        filter: true,
        sortable: true,
      },
      gridApi : null ,    //gridApi
      columnApi : null ,  //columApi
      data: [],
      config: {
        hideTime: true,
        format: 'YYYY-MM-DD',
        outputFormat: 'YYYY-MM-DD HH:mm:ss',
        dateFormat: '%Y-%m-%d'
      },
      title: 'GL전표',
      searchDt: [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')],
      form: {
        compCd: this.$store.state.loginInfo.compCd,
        slipType: '27',
        searchFrom: this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'),
        searchTo: this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss'),
        batchNm: '',
        headerNm: '',
        sourceNm: '',
        fileYn: '',
        jiniYn: '',
        trxTypeCd: '',
        trxTypeNm: '',
        glSlipTypeCd: '',
        glSlipTypeNm: '',
        transferredBy : this.$store.state.loginInfo.loginId,
        transferredByNm : this.$store.state.loginInfo.userName,
      },
      frameworkComponents: {
        selectRenderer: SelectCellRenderer,
        numberInputRenderer: NumberInputCellRenderer,
        searchButtonRenderer: AgGridSearchBtn,
        datePicker: DatepickerCellRenderer,
        scanAttach: AgGridScanAttach
      },
      sourceTypeCombo: [],
    }
  },
  created() {
    this.createColumnDefs();
    this.getSourceTypeCombo();
    this.goSearch();
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    createColumnDefs(){
      const vm = this;

      this.columnDefs = [
        {
          headerName: ''
          , headerCheckboxSelection: true
          , checkboxSelection: true
          , field:'regYn'
          , width : 80
          , cellStyle:{textAlign: 'center'}
          , editable: false
        },
        {
          headerName:'No.',
          field:'no',
          width:70,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          }
        },
        {
          headerName: '원천',
          field: 'sourceNm',
          width: 110,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '출처',
          field: 'categoryNm',
          width: 100,
          editable: false,
        },
        {
          headerName: '전표유형',
          field: 'glSlipType',
          width: 150,
          editable: false,
        },
        {
          headerName: '거래유형',
          field: 'trxTypeNm',
          width: 150,
          editable: false,
        },
        {
          headerName: 'GL일자',
          field: 'glDt',
          width: 110,
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value);
          },
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '분개명',
          field: 'headerNm',
          width: 170,
          editable: false,
        },
        {
          headerName: '통화코드',
          field: 'currencyCd',
          width: 100,
          cellStyle:{textAlign: 'center'},
          editable: false,
        },
        {
          headerName: '입력금액(외화)',
          field: '',
          width: 300,
          children:[
            {
              headerName: '차변',
              field: 'forTotalDr',
              width: 150,
              valueFormatter: (params) => {
                return vm.getDoubleNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '대변',
              field: 'forTotalCr',
              width: 150,
              valueFormatter: (params) => {
                return vm.getDoubleNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
          ],
          editable: false,
        },
        {
          headerName: '환산금액(원화)',
          field: '',
          width: 300,
          children:[
            {
              headerName: '차변',
              field: 'krwTotalDr',
              width: 150,
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
            {
              headerName: '대변',
              field: 'krwTotalCr',
              width: 150,
              valueFormatter: (params) => {
                return vm.getNumberFormat(params.value);
              },
              cellStyle:{textAlign: 'right'}
            },
          ],
          editable: false,
        },
        {
          headerName: '증빙첨부', 
          field: 'ufileCnt', 
          width: 100,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'scanAttach',
          // cellRendererParams:{
          //     funcNm : 'openEvidencePopup'
          // }
        },
        {
          headerName: '기안서연동',
          field: 'jiniCnt',
          width: 110,
          cellStyle:{textAlign: 'center'},
          cellRenderer: 'scanAttach',
          // cellRendererParams:{
          //     funcNm : 'openDrfPlanPopup'
          // }
        },
        {
          headerName: '배치명',
          field: 'batchNm',
          width: 160,
          editable: false,
        },
        {
          headerName: '적요',
          field: 'headerRemark',
          width: 260,
          editable: false,
        },
        {
          headerName: '환율',
          field: 'currencyRate',
          width: 90,
          valueFormatter: (params) => {
                return vm.getDoubleNumberFormat(params.value);
          },
          cellStyle:{textAlign: 'right'},
          editable: false,
        },
        {
          headerName: '환율일자',
          field: 'currencyDt',
          width: 120,
          valueFormatter: (params) => {
            return vm.getDateFormat(params.value);
          },
          editable: false,
        },
        {
          headerName: '작성자',
          field: 'regNm',
          width: 100,
          editable: false,
        },
      ]
    },
    compareDate(value){
      if(!value){
        this.$swal({ type: 'info', text: 'GL일자를 입력해주세요.' });
        return false;
      }
    },
    goSearch(){

      this.compareDate(this.searchDt);
      this.form.searchFrom = this.$moment(this.searchDt[0]).format("YYYY-MM-DD HH:mm:ss");
      this.form.searchTo = this.$moment(this.searchDt[1]).format("YYYY-MM-DD HH:mm:ss");


      this.$store.commit('loading')
      this.$http.post('/api/erp/slip/gl/list', this.form)
          .then(response => {
            this.data = response.data;
            // if(response.data.length == 0){
            //     this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
            //   }
          })
          .finally(() => {
            this.$store.commit('finish')
          })
    },
    pull() {
      this.$store.commit('loading');
      this.form.searchFrom = this.$moment(this.searchDt[0]).format("YYYY-MM-DD HH:mm:ss");
      this.form.searchTo = this.$moment(this.searchDt[1]).format("YYYY-MM-DD HH:mm:ss");
      this.$http.post('/api/erp/slip/27', this.form)
          .then((res) => {
            this.$swal({type: 'success', text: res.data})
            this.goSearch();
          })
          .catch(error => {
            this.$swal({
              type:'error', text: error.data.message,
            })
          })
          .finally(() => {
            this.$store.commit('finish');
          })
    },
    removeRow() {
      let vm = this;
      let list = this.data.filter(v => v.regYn)
      vm.result = [];
      if(list.length > 0){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;
            this.$http.post(`/api/erp/slip/delete/27`, vm.result)
                .then((response) => {
                  this.$swal({ type: 'success', text: '삭제되었습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.goSearch();
                        }
                      });
                })
                .catch((e) => {
                  this.$swal({ type: 'error', text: '삭제 실패하였습니다.' })
                })
                .finally(() => {
                  this.$store.commit('finish')
                });
          }
        })
      } else {
        this.$swal({ type: 'info', text: '전송취소할 항목을 선택하세요.' });
      }
    },
    approvalBundle() {

      const rows = this.gridApi.getSelectedRows();
      if(rows.length < 1){
        this.$swal({ type: 'warning', text: '상신할 전표를 선택해주세요.' });
        return false;
      }
      this.$modal.open({
        component: ApprBundleSubmTemp,
        props: {
          docType: 'GL전표',
          value: rows,
          config: this.config,
          apprTitle : this.title,
          slipType : this.form.slipType
        },
        parent: this,
        width: 1800,
        events: {
          close() {
            // if (vm.$route.slipNo !== undefined) {
            //   location.reload()
            // } else {
            //   vm.$router.push({
            //     name: vm.name,
            //     params: this.result
            //   })
            // }
          }
        }
      })
    },
    chkNumber(params){

      var result = '';
      var val = params.value;

      if(!_.isEmpty(val) || _.isNumber(val)){
        val = val.toString();
        result = this.$numeral(val).format('0,0');
        this.data[params.node.rowIndex][params.column.colId] = this.$numeral(val).value();
      }

      return result
    },
    getSourceTypeCombo() {
      this.$http
        .get(`/api/code/combo`, { params: { groupCd: "SOURCE_TYPE_CD" } })
        .then((response) => {
          this.sourceTypeCombo = response.data;
        });
    },
    openModal() {
      document.getElementById("open-modal").style.opacity = "1";
      document.getElementById("open-modal").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-modal").style.opacity = "0";
      document.getElementById("open-modal").style.pointerEvents = "none";
    },
    resetSearch(){
      this.searchDt = [this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss')];
      this.form.batchNm = '';
      this.form.headerNm = '';
      this.form.sourceNm = '';
      this.form.fileYn = '';
      this.form.jiniYn = '';
      this.form.trxTypeCd = '';
      this.form.trxTypeNm = '';
      this.form.transferredBy = this.$store.state.loginInfo.loginId;
      this.form.transferredByNm = this.$store.state.loginInfo.userName;
    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    getDoubleNumberFormat(val) {
      if(val) {
        return this.$numeral(val).format('0,0.00');
      }
    },
    rowDoubleClick(params) {
      const vm = this;
      this.$modal.open({
        component: SlipGlDetailModal,
        parent: this,
        props: {
          slipNo : params.data.slipNo,
          slipType: params.data.slipType,
          title : 'GL전표',
        },
        width: 1200,
        events: {
          close(obj){
            vm.goSearch();
            //console.log(obj)
            //vm.$forceUpdate();
          }
        }
      })
    },
    openBulkEvidencePopup(){
      const that = this;
      var targetList = this.data.filter(v => v.regYn).map(v => v.slipNo);
      var list = this.data.filter(v => v.regYn)
      if(targetList.length > 0) {
        this.$modal.open({
          component: EvidAtchBatchPop,
          props: {
            docMngNo: targetList,
            readonly: true,
            value: list
          },
          parent: this,
          width: 600,
          events: {
            close(value) {
                if (value.length > 0) {
                  that.goSearch();
                }
            }
          }
        })
      } else {
        this.$swal({ type: 'info', text: '파일 첨부할 전표를 선택해 주세요.' });
      }
    },
    openBulkDrfPlanPopup(){
      const that = this;
      let rowData = this.data.filter(v => v.regYn);

      if(rowData.length === 0) {
        this.$swal({ type: 'info', text: '기안서 적용할 전표를 선택해 주세요.' });
        return false;
      }

      for(let i = 0; i < rowData.length; i++) {
        if(rowData[i].jiniCnt > 0) {
          this.$swal({ type: 'info', text: '연동된 기안서가 있는 전표는 적용할 수 없습니다.' });
          return false;
        }
      }

      let slipNoList = this.data.filter(v => v.regYn).map(v => v.slipNo);

      this.$modal.open({
        component: JiniAtchBatchPop,
        props: {
          slipNo: slipNoList,
        },
        parent: this,
        width: 1200,
        events: {
          close(value) {
            if (value.length > 0) {
              that.goSearch();
            }
          }
        }
      })
    },
    popTransfferedBy() {
      let vm = this
      this.$modal.open({
        component: EmpPop,
        props: {
          param: this.form.transferredByNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.transferredBy = obj.empNo;
            vm.form.transferredByNm = obj.empNm;
          }
        }
      })
    },
    initTransfferedBy(force) {
      if (force === true) this.form.transferredByNm = '';
      if (this.form.transferredByNm === '') {
        this.form.transferredBy = '';
        this.form.transferredByNm = '';
      }
    },
    popTrxTypeNm() {
      let vm = this;
      this.$modal.open({
        component: TrxPop,
        props: {
          schTxt : this.form.trxTypeNm,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.trxTypeCd = obj.trxTypeCd;
            vm.form.trxTypeNm = obj.trxTypeNm;
          }
        }
      })
    },
    initGlSlipTypeNm(force) {
      if (force === true) this.form.glSlipTypeNm = '';
      if (this.form.glSlipTypeNm === '') {
        this.form.glSlipTypeCd = '';
        this.form.glSlipTypeNm = '';
      }
    },
    popGlSlipTypeNm() {
      let vm = this;
      this.$modal.open({
        component: GlSlipTypePop,
        props: {
          schTxt : this.form.glSlipTypeNm,
        },
        parent: this,
        events: {
          close(obj) {
            vm.form.glSlipTypeCd = obj.glSlipTypeCd;
            vm.form.glSlipTypeNm = obj.glSlipTypeNm;
          }
        }
      })
    },
    initTrxTypeNm(force) {
      if (force === true) this.form.trxTypeNm = '';
      if (this.form.trxTypeNm === '') {
        this.form.trxTypeCd = '';
        this.form.trxTypeNm = '';
      }
    },
    mainGridRefresh(idx) {
      var rows = [];
      var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
      rows.push(rowNode);
      this.gridApi.redrawRows({ rowNodes: rows });
    },
    cellValueChange(params){
      const vm = this;
      const rowNode = params.api.getRowNode(params.node.rowIndex);
    },
    onRowSelected(params) {
      const rowIdx = params.rowIndex;
      this.data[rowIdx].regYn = params.node.isSelected();
    },
    onCellClicked(event){
      const field = event.colDef.field

      if(field === "ufileCnt"){
        const vm = this;

        let row = event.data;

        let rdoCtrl = false
        let readonly = false

        const slipNo = row.slipNo;

        if(slipNo) {
          this.$modal.open({
            component: EvidAtchPopModeless,
            props: {
              slipNo,
              readonly,
              rdoCtrl
            },
            parent: this,
            width: 1200,
            events: {
              close(obj){
                vm.goSearch();
              }
            }
          });
        } else {

        }
      }
      else if(field === "jiniCnt"){
        const vm = this;

        let i = event.rowIndex;
        let row = event.data;

        let readOnly = false;

        const slipNo = row.slipNo;

        if(slipNo){
          this.$modal.open({
            component: JiniAtchPop,
            props: {
              slipNo,
              readOnly
            },
            parent: this,
            width: 1200,
            events: {
              close(value) {
                vm.goSearch();
              }
            }
          })
        } else {
        }
      }
    },
  },
}
</script>
