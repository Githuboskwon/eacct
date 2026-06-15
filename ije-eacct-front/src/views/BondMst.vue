<template>
  <div class="inner-box">
      <div class="content-name">
        <h2 class="dp-ib">BOND 마스터</h2>
        <div class="btn-type1 clearfix">
          <el-button size="large" type="primary" icon="el-icon-search" @click="goSearch">조회</el-button>
          <el-button size="large" type="success" icon="el-icon-folder-checked" @click="save">저장</el-button>
        </div>
      </div>

      <!-- 검색조건 영역 -->
    <div class="search-form">
        <div class="form-group">
          <label class="control-label-req" id="standardMonth">개설일자</label>
          <div class="form-input">
<!--            <div class="datepicker w-type-ymd">-->
<!--               <dhx-calendar class="input" v-model="form.openingDtFrom" :config="config"/>-->
<!--            </div>-->
<!--            <span> ~ </span>-->
<!--            <div class="datepicker w-type-ymd">-->
<!--              <dhx-calendar class="input" v-model="form.openingDtTo" :config="config"/>-->
<!--            </div>-->
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
                  <span class="search_tit">개설일자</span>
                </div>
                <div class="search_con search-area">
                  <div class="form-input">
<!--                    <div class="datepicker w-type-ymd">-->
<!--                      <dhx-calendar class="input" v-model="form.openingDtFrom" :config="config"/>-->
<!--                    </div>-->
<!--                    <div class="datepicker w-type-ymd">-->
<!--                      <dhx-calendar class="input" v-model="form.openingDtTo" :config="config"/>-->
<!--                    </div>-->
                    <el-date-picker
                        v-model="searchDt"
                        type="daterange"
                        align="right"
                        unlink-panels
                        style="width: 100%;"
                        range-separator="~"
                        start-placeholder="시작일"
                        end-placeholder="종료일">
                    </el-date-picker>
                  </div>
                </div>
              </div>
              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">고객명</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg w30p_5r" type="text" v-model="form.customerId" disabled>
                  <input class="input input-bg w30p_5r" type="text" v-show="false" v-model="form.customerId" >
                  <div class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.customerNm" @input="initCustomer" @keypress.enter="popCustomer()">
                    <button class="icon is-right" @click="popCustomer()"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">수익자국가</span>
                </div>
                <div class="search_con search-area">
                  <input class="input input-bg w30p_5r" type="text" v-model="form.benCountryCd" disabled>
                  <input class="input input-bg w30p_5r" type="text" v-show="false" v-model="form.benCountryCd" >
                  <div class="dp-ib w70p">
                    <input class="input" type="text" v-model="form.benCountry" @input="initBenCountry" @keypress.enter="popBenCountry">
                    <button class="icon is-right" @click="popBenCountry('r')"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">REF NO</span>
                </div>
                <div class="search_con search-area">
                  <input class="input" type="text" v-model="form.refNo" >
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
            <h3 class="ico_table_name">BOND 마스터내역</h3>
          </div>
          <div class="btn-type2 clearfix">
            <el-button type="info" icon="el-icon-plus" @click="addRow()">{{ this.$i18n.messages[this.$store.state.locale].addRow }}</el-button>
            <el-button type="info" icon="el-icon-document-copy" @click="copyRow()">행복사</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="removeRow()">{{this.$i18n.messages[this.$store.state.locale].delRow}}</el-button>
            <!-- <button class="btn-size btn-w-gray" @click="addRow()">
              <span class="btn-icon">
                <i class="fas fa-plus"></i>
              </span>
              {{this.$i18n.messages[this.$store.state.locale].addRow}}
            </button>
            <button class="btn-size btn-w-gray" @click="copyRow()">
              <span class="btn-icon">
                <i class="fas fa-clone"></i>
              </span>
              행복사
            </button>
            <button class="btn-size btn-w-gray" @click="removeRow()">
              <span class="btn-icon">
                <i class="fas fa-trash-alt"></i>
              </span>
              {{this.$i18n.messages[this.$store.state.locale].delRow}}
            </button> -->
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
                         @grid-ready="onReady"
                         @row-selected="onRowSelected"
                         @cell-value-changed="cellValueChange"
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
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'
import DatepickerCellRenderer from '@/components/agGrid/datepicker-cell-renderer'
import CustomerPop from '@/components/CustomerPop.vue';
import AgGridSearchBtn from '@/components/agGrid/AgGridSearchBtn';
import BenCountryPop from '@/components/BenCountryPop.vue';
import ProjectPop from '@/components/ProjectPop.vue';
import LocalBankPop from '@/components/LocalBankPop.vue';
import { AgGridVue } from 'ag-grid-vue'

export default {
  name: 'bondMst',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, CustomerPop, BenCountryPop, ProjectPop
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
      searchDt: [],
      form: {
        compCd: '',
        openingDtFrom: '',
        openingDtTo: '',
        customerNm: '',
        customerNum: '',
        customerId: '',
        benCountryCd: '',
        benCountry: '',
        refNo: '',
      },
      frameworkComponents: {
        checkboxRenderer: CheckboxCellRenderer, 
        selectRenderer: SelectCellRenderer,
        numberInputRenderer: NumberInputCellRenderer,
        searchButtonRenderer: AgGridSearchBtn,
        datePicker: DatepickerCellRenderer,
      },
      combos: [],
    }
  },
  created() {
    this.getCombos();
    this.goSearch();
  },
  methods: {
    onReady() {
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
    },
    async getCombos() {
      let [curCd] = await Promise.all([this.$http.get(`/api/code/combo`, {params: {groupCd: "CUR_CD"}})]);
      this.combos['CUR_CD'] = curCd.data;
      this.createColumnDefs();
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
          , cellStyle:{textAlign: 'center',color: 'transparent'}
          , editable: false
        },
        {
          headerName: "No.",
          width: 65,
          cellStyle: {textAlign: 'center'},
          valueFormatter: (params) => {
            return params.node.rowIndex + 1;
          },
        },
        {
          headerName: 'REF NO',
          field: 'refNo',
          width: 170,
          headerClass: 'require-column',
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
          headerName: '수익자 국가',
          field: 'benCountry',
          width: 150,
          headerClass: 'require-column',
          cellRenderer: 'searchButtonRenderer',
          cellRendererParams: {
            funcNm: 'updateBenCountry',
            valFuncNm: 'updateBenCountryVal'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
        {
          headerName: '고객명',
          field: 'customerNm',
          width: 200,
          headerClass: 'require-column',
          cellRenderer: 'searchButtonRenderer',
          cellRendererParams: {
            funcNm: 'updateCustomer',
            valFuncNm: 'updateCustomerVal',
          }
        },
        {
          headerName: '프로젝트명',
          field: 'projectNm',
          headerClass: 'require-column',
          width: 250,
          cellRenderer: 'searchButtonRenderer',
          cellRendererParams(params) {
            return {
              funcNm: 'updateProject',
              valFuncNm: 'updateProjectVal'
            }
          },
          // cellStyle:()=>{
          //   return {textAlign: 'center'}
          // },
        },
        {
          headerName: '국내은행명',
          field: 'localBankNm',
          width: 200,
          headerClass: 'require-column',
          cellRenderer: 'searchButtonRenderer',
          cellRendererParams: {
            funcNm: 'updateLocalBank',
            valFuncNm: 'updateLocalBankVal',
          },
        },
        {
          headerName: '해외은행명',
          field: 'intBankNm',
          width: 250,
          headerClass: 'require-column',
          editable: true,
        },
        {
          headerName: 'BOND종류',
          field: 'bondCd',
          width: 120,
          headerClass: 'require-column',
          // cellRenderer: 'selectRenderer',
          // cellRendererParams: {
          //   options : vm.combos['BUSINESS_TRIP_AREA_CD'],
          //   detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
          //   detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
          // },
          cellStyle: {textAlign: 'center'},
          editable: true,
        },
        {
          headerName: '통화',
          field: 'currencyCd',
          width: 90,
          headerClass: 'require-column',
          cellRenderer: 'selectRenderer',
          cellRendererParams: {
            options : vm.combos['CUR_CD'],
            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'key' //서버로 부터 받은 value defalut-"detailNm"
          },
          cellStyle: {textAlign: 'center'},
          editable: true,
        },
        {
          headerName: '통화(보증금)',
          field: 'currencyAmt',
          width: 120,
          // headerClass: 'require-column',
          // selectbox
          // 숫자만
          cellStyle: {textAlign: 'right'},
          //소수 입력 수정
          //cellRenderer: 'numberInputRenderer',
          editable: true,
        },
        {
          headerName: '예산(단위:원)',
          field: 'budget',
          width: 130,
          // headerClass: 'require-column',
          cellRenderer: 'numberInputRenderer',
          cellStyle: {textAlign: 'right'},
          editable: true,
        },
        {
          headerName: '개설일',
          field: 'openingDt',
          width: 110,
          cellRenderer: 'datePicker',
          cellRendererParams: {
            config: { 
              hideTime: true,
              format: "YYYY-MM-DD",
              outputFormat: "YYYY-MM-DD HH:mm:ss",
              dateFormat: '%Y-%m-%d'
            },
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        
        },
        {
          headerName: '릴리즈일자',
          field: 'releaseDt',
          width: 110,
          cellRenderer: 'datePicker',
          cellRendererParams: {
            config: { 
              hideTime: true,
              format: "YYYY-MM-DD",
              outputFormat: "YYYY-MM-DD HH:mm:ss",
              dateFormat: '%Y-%m-%d'
            },
          },
          cellClass: () => {
            return 'bg-grey'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
      ]
    },
    goSearch() {
      this.$store.commit('loading')
      if(!this.searchDt) {
        this.searchDt = [];
      }
      this.form.openingDtFrom = this.searchDt[0] ? this.$moment(this.searchDt[0]).format("YYYY-MM-DD HH:mm:ss") : null;
      this.form.openingDtTo = this.searchDt[1] ? this.$moment(this.searchDt[1]).format("YYYY-MM-DD HH:mm:ss") : null;

      this.$http.post('/api/bondMst/list', {
        openingDtFrom : this.form.openingDtFrom,
        openingDtTo : this.form.openingDtTo,
        customerId : this.form.customerId,
        benCountryCd: this.form.benCountryCd,
        refNo : this.form.refNo
      })
      .then(response => {
        this.data = response.data;
        if(response.data.length === 0){
            this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
          }  
      })
      .finally(() => {
        this.$store.commit('finish')
      })
    },
    save() {

      const selectedRows = this.gridApi.getSelectedRows();
      if(selectedRows.length === 0) {
        this.$swal({ type: 'info', text: '저장할 항목을 선택하세요.' });
        return false;
      }

      // 기존 rows
      const comparedRows = this.data.filter(x => !x.new);
      // 선택 && 새로운 rows
      const newRows = selectedRows.filter(x => x.new);
      if(!this.gridRequireCheckRegYn([this.$refs.grid])) return;
      for (var i = 0; i < comparedRows.length; i++) {
        for (var j = 0; j < newRows.length; j++) {
          if (comparedRows[i].refNo === newRows[j].refNo) {
            this.$swal({
              type: "error",
              text: "해당 BOND가 이미 존재합니다."
            });
            return false;
          }
        }
      }

      this.$store.commit('loading');
      this.$http.post('/api/bondMst/save', selectedRows)
        .then(() => {
          this.$swal({type: 'success', text: '저장되었습니다.'})
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
    addRow() {
      this.data.push({
        compCd: this.$store.state.loginInfo.compCd,
        refNo: '',
        benCountry : '',
        benCountryCd: '',
        bondCd: '',
        budget: '',
        currencyCd: '',
        currencyAmt: '',
        customerId: '',
        customerNm: '',
        intBankNm: '',
        localBankId: '',
        localBankNm: '',
        openingDt: '',
        projectId: '',
        projectNm: '',
        releaseDt:'',
        new: true,
      })
    },
    removeRow() {
      let vm = this;
      const selectedRows = this.gridApi.getSelectedRows();
      const rowCount = selectedRows.length;
      if(rowCount === 0) {
        this.$swal({ type: 'info', text: '삭제할 항목을 선택하세요.' });
        return false;
      }

      const firstSelectedRow = this.gridApi.getSelectedNodes()[0];
      const firstSelectedRowIdx = firstSelectedRow.childIndex;
      const isNew = firstSelectedRow.data.new;

      if(!isNew) {
        const params = selectedRows.filter(x => !x.new)
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            this.$http.post(`/api/bondMst/delete`, params)
            .then((response) => {
              this.$swal({ type: 'success', text: '삭제되었습니다.' })
              .then((result) => {
                if (result.value) {
                  this.goSearch();
                }
              });
            })
            .catch((e) => {
              console.log(e)
              this.$swal({ type: 'error', text: e.message })
            })
            .finally(() => {
              this.$store.commit('finish')
            });
          }
        })
      } else {
        this.data.splice(firstSelectedRowIdx, rowCount);
      }

    },
    copyRow() {
      const selectedRows = this.gridApi.getSelectedRows();
      if(selectedRows.length > 0){

        const copyRow = _.cloneDeep(selectedRows.map(x => {
          x.id = x.id + 1;
          return x;
        }));
        copyRow.map(x => {
          x.new = true;
          this.data.push(x);
        });

      } else {
        this.$swal({ type: 'info', text: '복사할 항목을 선택하세요.' });
      }
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
    openModal() {
      document.getElementById("open-modal").style.opacity = "1";
      document.getElementById("open-modal").style.pointerEvents = "auto";
    },
    closeModal() {
      document.getElementById("open-modal").style.opacity = "0";
      document.getElementById("open-modal").style.pointerEvents = "none";
    },
    resetSearch(){
      this.searchDt = [];
      this.form.compCd = '';
      this.form.openingDtFrom = '';
      this.form.openingDtTo = '';
      this.form.customerNm = '';
      this.form.customerNum = '';
      this.form.customerId = '';
      this.form.benCountryCd = '';
      this.form.benCountry = '';
      this.form.benCountry = '';
      this.form.refNo = '';
    },
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    popCustomer() {
      let vm = this
      this.$modal.open({
        component: CustomerPop,
        props: {
          param: this.form.customerNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.getCustomer(obj);
            // vm.$forceUpdate();
          }
        }
      })
    },
    getCustomer(obj) {
      this.form.customerId = obj.customerId;
      this.form.customerNum = obj.customerNum;
      this.form.customerNm = obj.customerName;
    },
    popBenCountry() {
      let vm = this
      this.$modal.open({
        component: BenCountryPop,
        props: {
          param: this.form.benCountry
        },
        parent: this,
        events: {
          close(obj) {
            vm.getBenCountry(obj);
            // vm.$forceUpdate();
          }
        }
      })
    },
    getBenCountry(obj) {
      this.form.benCountry = obj.benCountryNm;
      this.form.benCountryCd = obj.benCountryCd;
    },
    updateBenCountry() {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);

      this.$modal.open({
        component: BenCountryPop,
        parent: this,
        width: 700,
        props: {
          param: this.form.benCountry
        },
        events: {
          close(obj) {
            vm.data[rowId].benCountry = obj.benCountryNm;
            vm.data[rowId].benCountryCd = obj.benCountryCd;
            rowNode.setDataValue('benCountry', obj.benCountryNm);
          }
        }
      })
    },
    updateBenCountryVal(pVal) {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);
      if(pVal) {
        this.$http.get(`/api/benCountry/list/${pVal}`)
        .then(response => {
          if(response.data.length === 1) {
            vm.data[rowId].benCountry = response.data[0].benCountryNm;
            vm.data[rowId].benCountryCd = response.data[0].benCountryCd;
            rowNode.setDataValue('benCountry', response.data[0].benCountryNm);
          } else {
            this.$modal.open({
              component: BenCountryPop,
              parent: this,
              width: 700,
              props: {
                param: pVal,
              },
              events: {
                close(obj) {
                  vm.data[rowId].benCountry = obj.benCountryNm;
                  vm.data[rowId].benCountryCd = obj.benCountryCd;
                  rowNode.setDataValue('benCountry', obj.benCountryNm);
                }
              }
            })
          }
        })
      } else {
        vm.data[rowId].benCountry = '';
        vm.data[rowId].benCountryCd = '';
        rowNode.setDataValue('benCountry', '');
      }
    },
    updateProject() {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);

      this.$modal.open({
        component: ProjectPop,
        parent: this,
        width: 700,
        props: {
          param: vm.data[rowId].projectNm,
        },
        events: {
          close(obj) {
            vm.data[rowId].projectId = obj.projectId;
            vm.data[rowId].projectNm = obj.projectNm;
            rowNode.setDataValue('projectNm', obj.projectNm);
          }
        }
      })
    },
    updateProjectVal(pVal) {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);
      if(pVal) {
        this.$http.get(`/api/project/list/${pVal}`)
        .then(response => {
          if(response.data.length === 1){
            vm.data[rowId].projectId = response.data[0].projectId;
            vm.data[rowId].projectNm = response.data[0].projectNm;
            rowNode.setDataValue('projectNm', response.data[0].projectNm);
          } else {
            this.$modal.open({
              component: ProjectPop,
              parent: this,
              width: 700,
              props: {
                param: pVal,
              },
              events: {
                close(obj) {
                  vm.data[rowId].projectId = obj.projectId;
                  vm.data[rowId].projectNm = obj.projectNm;
                  rowNode.setDataValue('projectNm', obj.projectNm);
                }
              }
            })
          }
        })
      } else {
        vm.data[rowId].projectId = '';
        vm.data[rowId].projectNm = '';
        rowNode.setDataValue('projectNm', '');
      }
    },
    updateCustomer() {
      const vm = this;
      let rowId = this.rowId;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      this.$modal.open({
        component: CustomerPop,
        parent: this,
        props: {
          param: vm.data[rowId].customerNm,
        },
        width: 700,
        events: {
          close(obj) {
            vm.data[rowId].customerId = obj.customerId;
            vm.data[rowId].customerNm = obj.customerName;
            rowNode.setDataValue('customerNm', obj.customerName);
          }
        }
      })
    },
    updateCustomerVal(pVal) {
      const vm = this;
      let rowId = this.rowId;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      if(pVal) {
        this.$http.get(`/api/customer/list/${pVal}`)
        .then(response => {
          if(response.data.customerList.length === 1) {
            vm.data[rowId].customerId = response.data.customerList[0].customerId;
            vm.data[rowId].customerNm = response.data.customerList[0].customerName;
            rowNode.setDataValue('customerNm', response.data.customerList[0].customerName);
          } else {
            this.$modal.open({
              component: CustomerPop,
              parent: this,
              props: {
                param: pVal,
              },
              width: 700,
              events: {
                close(obj) {
                  vm.data[rowId].customerId = obj.customerId;
                  vm.data[rowId].customerNm = obj.customerName;
                  rowNode.setDataValue('customerNm', obj.customerName);
                }
              }
            })
          }
        })
      } else {
        vm.data[rowId].customerId = '';
        vm.data[rowId].customerNm = '';
        rowNode.setDataValue('customerNm', '');
      }
    },
    updateLocalBank() {
      const vm = this;
      let rowId = this.rowId;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      this.$modal.open({
        component: LocalBankPop,
        parent: this,
        width: 700,
        events: {
          close(obj) {
            vm.data[rowId].localBankId = obj.integrationVendorNum;
            vm.data[rowId].localBankNm = obj.integrationVendorName;
            rowNode.setDataValue('localBankNm', obj.integrationVendorName);
          }
        }
      })
    },
    updateLocalBankVal(pVal) {
      const vm = this;
      let rowId = this.rowId;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      if(pVal) {
        this.$http.get(`/api/locakBank/list/${pVal}`)
        .then(response => {
          if(response.data.length === 1) {
            vm.data[rowId].localBankId = response.data[0].integrationVendorNum;
            vm.data[rowId].localBankNm = response.data[0].integrationVendorName;
            rowNode.setDataValue('localBankNm', response.data[0].integrationVendorName);
          } else {
            this.$modal.open({
              component: LocalBankPop,
              parent: this,
              width: 700,
              props: {
                param: pVal,
              },
              events: {
                close(obj) {
                  vm.data[rowId].localBankId = obj.integrationVendorNum;
                  vm.data[rowId].localBankNm = obj.integrationVendorName;
                  rowNode.setDataValue('localBankNm', obj.integrationVendorName);
                }
              }
            })
          }
        })
      } else {
        vm.data[rowId].localBankId = '';
        vm.data[rowId].localBankNm = '';
        rowNode.setDataValue('localBankNm', '');
      }
    },
    initCustomer(force) {
      if (force === true) this.form.customerNm = '';
      if (this.form.customerNm === '') {
        this.form.customerId = '';
        this.form.customerNm = '';
      }
    },
    initBenCountry(force) {
      if (force === true) this.form.benCountry = '';
      if (this.form.benCountry === '') {
        this.form.benCountry = '';
        this.form.benCountryCd = '';
      }
    },
    cellValueChange(params){
      const vm = this;
      const rowNode = params.api.getRowNode(params.node.rowIndex);
    },
    onRowSelected(params){
      const rowIdx = params.rowIndex;
      this.data[rowIdx].regYn = params.node.isSelected();
    },
    gridRequireCheckRegYn(params) {
      for(var i = 0; i < params.length; i++){

        const grid = params[i]
        let reqCol = []

        grid.columnDefs.forEach( x => {
          if(x.children !== undefined){
            x.children.forEach( child => {
              if(child.headerClass === "require-column") reqCol.push(child)
            })
          }else{
            if(x.headerClass === "require-column") reqCol.push(x)
          }
        })

        let reqColName
        let reqRowIdx

        const obj = grid.rowData.find((x,i) => {
          reqCol.forEach(c => {
            if(!x[c.field] && x.regYn){
              reqColName = c.headerName
              reqRowIdx = i
              return
            }
          })
          return reqColName !== undefined
        })

        if(obj){
          this.$swal({type: 'info', html: '<h style="color: #CC3D3D;">'+ reqColName + '</h>은(는) 필수 입력값 입니다.<br />' + (reqRowIdx + 1) + '번째 데이터 확인 바랍니다'});
          return false;
        }
      }
      return true;
    },
  },
}
</script>
