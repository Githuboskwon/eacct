<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <button type="button" class="btn-size btn-gray fl_left" @click="search()">
          <span class="btn-icon"><i class="fas fa-search"></i></span>
          조회
        </button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="search-form">
      <div class="search_box">
        <div class="search_title">
          <span class="search_tit">- 착공일 ~ 준공일</span>
        </div>
        <div class="search_con">
          <div class="datepicker w-type-ymd02">
            <dhx-calendar class="input" v-model="form.openingDtFrom" :config="config"/>
          </div>
          <span class="wave"> ~ </span>
          <div class="datepicker w-type-ymd02">
            <dhx-calendar class="input" v-model="form.openingDtTo" :config="config"/>
          </div>
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
                <span class="search_tit">- 착공일 ~ 준공일</span>
              </div>
              <div class="search_con search-area">
                <div class="form-input">
                  <div class="datepicker w-type-ymd">
                    <dhx-calendar class="input" v-model="form.openingDtFrom" :config="config"/>
                  </div>
                  <div class="datepicker w-type-ymd">
                    <dhx-calendar class="input" v-model="form.openingDtTo" :config="config"/>
                  </div>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- PJT 구분</span>
              </div>
              <div class="search_con search-area">
                <select class="select is-fullwidth" id="use_yn" v-model="form.mainType">
                  <option value="">관납</option>
                  <option value="">로컬</option>
                  <option value="">민수</option>
                  <option value="">해외</option>
                  <option value="">지사</option>
                </select>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 국가</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.SlipStatCd" disabled>
                <input class="input input-bg w30p_5r" type="text" v-show="false" v-model="form.SlipStatCd" >
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.SlipStatCd" @input="initSlipStat" @keypress.enter="popSlipStat">
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 예산부서</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.CctrAgCd" disabled>
                <input class="input input-bg w30p_5r" type="text" v-show="false" v-model="form.CctrAgCd" >
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.CctrAg" @input="initCctrAg" @keypress.enter="popCctrAg">
                  <button class="icon is-right" @click="popCctrAg('r')"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- 등록자</span>
              </div>
              <div class="search_con search-area">
                <input class="input input-bg w30p_5r" type="text" v-model="form.empNo" disabled>
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.empNm" @input="initEmp" @keypress.enter="popEmp">
                  <button class="icon is-right" @click="popEmp(true)"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- PJT NO</span>
              </div>
              <div class="search_con search-area">
                <div class="dp-ib w70p">
                  <input class="input" type="text" v-model="form.ProjectNum" @input="initProjectPop" @keypress.enter="popProjectPop">
                  <button class="icon is-right" @click="popProjectPop('r')"><i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">- PJT 명</span>
              </div>
              <div class="search_con search-area">
                <input class="input" type="text" v-model="form.PjtNm" >
              </div>
            </div>

            <div class="search_box_pop">
              <div class="search_title">
                <span class="search_tit">&nbsp;</span>
              </div>
              <span class="are">* PJT 명, 태스크 명 클릭 시 상세화면으로 이동합니다.</span>
            </div>

          </div>

          <div class="modal-window-bottom">
            <ul>
              <li>
                <button class="bt_blue_s" @click="search()">검색</button>
              </li>
              <li>
                <button @click="closeModal()" title="Close" class="bt_white_s">닫기</button>
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
            <h3 class="ico_table_name">PJT비용예산vs실적 내역</h3>
          </div>
          <div class="btn-wrap btn-type2 clearfix">
            <button class="btn-size btn-w-gray" @click="addRow()">
              <span class="btn-icon">
                <i class="fas fa-plus"></i>
              </span>
              {{this.$i18n.messages[this.$store.state.locale].addRow}}
            </button>
            <button class="btn-size btn-w-gray" @click="removeRow()">
              <span class="btn-icon">
                <i class="fas fa-trash-alt"></i>
              </span>
              {{this.$i18n.messages[this.$store.state.locale].delRow}}
            </button>
          </div>
        </div>

        <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
        <div class="table-b">
          <div class="grid-wrap">
            <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height_350"
                         rowSelection="single"
                         :columnDefs="columnDefs"
                         :rowData="data"
                         :gridOptions="gridOptions"
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef"
                         @grid-ready="onReady"
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
import AgGridSearchBtn from '@/components/agGrid/AgGridSearchBtn';
import CctrAg from '@/components/Cctr_Ag.vue';
import ProjectPop from '@/components/ProjectPop.vue';
import { AgGridVue } from 'ag-grid-vue'

export default {
  compatConfig: { MODE: 2 },
  name: 'ProjectPlanBudRepLst',
  mixins: [mixin, mixinSlip, common],
  components: {
    DhxCalendar, AgGridVue, CctrAg, ProjectPop
  },
  data() {
    return {
      title: "PJT비용예산vs실적",
      columnDefs : [],
      gridOptions : {},
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
        format: 'YYYY-MM-DD',
        outputFormat: 'YYYY-MM-DD HH:mm:ss',
        dateFormat: '%Y-%m-%d'
      },
      form: {
        compCd: '',
        openingDtFrom: this.$moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'),
        openingDtTo: this.$moment().startOf('day').format('YYYY-MM-DD HH:mm:ss'),
        CctrAgCd: '',
        CctrAg: '',
        projectNm: '',
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
    this.search();
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
          headerName: '결재상태',
          field: 'currencyCd',
          width: 100,
          cellRenderer: 'selectRenderer',
          cellRendererParams: {
            options : vm.combos['SLIP_STAT_CD'],
            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
            detailNm: 'key' //서버로 부터 받은 value defalut-"detailNm"
          },
          cellStyle: {textAlign: 'center'},
          editable: true,
        },
        {headerName: 'PJT No', field: 'projectCd', width: 150, headerClass: 'require-column',},
        {headerName: '테스크 번호', field: 'taskNm', width: 150, headerClass: 'require-column',},
        {
          headerName: 'PJT명',
          field: 'projectNm',
          width: 300,
          headerClass: 'require-column',
          // cellStyle:()=>{
          //   return {textAlign: 'center'}
          // },
        },
        {headerName: '테스크 명', field: 'taskNm', width: 150, headerClass: 'require-column',},
        {headerName: '차수', field: 'projectMngNo', width: 150},
        {headerName: 'PJT 시작일',
          field: 'startDt',
          width: 150,
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
        {
          headerName: 'PJT 종료일',
          field: 'endDt',
          width: 150,
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
          cellStyle: () => {
            return {textAlign: 'center'}
          },
        },
        {headerName: '총개월수', field: 'totalMonth', width: 150},
        {headerName: '수주금액', field: 'intergrationVendorNum', width: 150},
        {headerName: '버전', field: '', width: 150},
        {headerName: '수주원가', field: 'intergrationVendorNum', width: 150},
        {headerName: '전표종류', field: 'intergrationVendorNum', width: 150},
        {headerName: '팀ID', field: 'intergrationVendorNum', width: 150, headerClass: 'require-column',},
        {headerName: '팀명', field: 'intergrationVendorNum', width: 150, headerClass: 'require-column',},
        {
          headerName: '상신부서',
          field: 'CctrAg',
          width: 200,
          headerClass: 'require-column',
          cellRenderer: 'searchButtonRenderer',
          cellRendererParams: {
            funcNm: 'updateCctrAg',
            valFuncNm: 'updateCctrAgVal'
          },
          cellStyle:()=>{
            return {textAlign: 'center'}
          },
        },
        {headerName: '적요', field: 'intergrationVendorNum', width: 150, headerClass: 'require-column',},
        {headerName: '전표번호', field: 'intergrationVendorNum', width: 150, headerClass: 'require-column',},
        {headerName: '등록일', field: 'intergrationVendorNum', width: 150},
        {headerName: '등록자', field: 'intergrationVendorNum', width: 150},
      ]
    },
    search() {
      this.$store.commit('loading')
      this.$http.post('/api/Project/list', {
        openingDtFrom : this.form.openingDtFrom,
        openingDtTo : this.form.openingDtTo,
        customerId : this.form.customerId,
        CctrAgCd: this.form.CctrAgCd,
        refNo : this.form.refNo
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

          if (this.data[i].refNo === this.data[j].refNo) {
            this.$swal({
              type: "error",
              text: "해당 PJT가 이미 존재합니다."
            });
            return false;
          }
        }
      }
      this.$store.commit('loading');
      this.$http.post('/api/Project/save', this.data)
          .then(() => {
            this.$swal({type: 'success', text: '저장되었습니다.'})
            this.search();
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
    newPage() {
      this.$swal({
        type: "info",
        text: `변경된 데이터가 있습니다. 계속 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: "예",
        cancelButtonText: "아니오"
      }).then(result => {
        if (result.value) {
          this.goOpen();
        }
      });
    },
    reset() {
      this.goOpen();
    },
    addRow() {
      this.data.push({
        regNo:'N',
        compCd: this.$store.state.loginInfo.compCd,
        refNo: '',
        openingDt: '',
        projectId: '',
        projectNm: '',
        releaseDt:'',
        new: true,
      })
    },
    removeRow() {
      let vm = this;
      let list = this.data.filter(v => v.regYn)
      vm.result = [];
      if(list.length > 0){
        this.$swal({
          type: 'question',
          text: '선택한 항목을 삭제 하시겠습니까?',
          showCancelButton: true
        }).then(r => {
          if (r.value) {
            this.$store.commit('loading')
            vm.result = list;
            this.$http.post(`/api/Project/delete`, vm.result)
                .then((response) => {
                  this.$swal({ type: 'success', text: '삭제되었습니다.' })
                      .then((result) => {
                        if (result.value) {
                          this.search();
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
        this.$swal({ type: 'info', text: '삭제할 항목을 선택하세요.' });
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
    getDtmFormat(val) {
      if (val) {
        return val === 'null' ? '' : this.$moment(val).format('YYYY-MM-DD HH:mm:ss')
      }
    },
    popCctrAg() {
      let vm = this
      this.$modal.open({
        component: CctrAg,
        props: {
          param: this.form.CctrAg
        },
        parent: this,
        events: {
          close(obj) {
            vm.getCctrAg(obj);
          }
        }
      })
    },
    getCctrAg(obj) {
      this.form.CctrAg = obj.deptNm;
      this.form.CctrAgCd = obj.deptCd;
    },
    updateCctrAg() {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);

      this.$modal.open({
        component: CctrAg,
        parent: this,
        width: 700,
        props: {
          param: this.form.CctrAg
        },
        events: {
          close(obj) {
            vm.data[rowId].CctrAg = obj.deptNm;
            vm.data[rowId].CctrAgCd = obj.deptCd;
            rowNode.setDataValue('CctrAg', obj.deptNm);
          }
        }
      })
    },
    updateCctrAgVal(pVal) {
      const vm = this;
      let rowId = this.rowId;
      let rowNode = this.gridApi.getRowNode(this.rowId);
      if(pVal) {
        this.$http.get(`/api/Cctr_Ag/list/${pVal}`)
            .then(response => {
              if(response.data.length === 1) {
                vm.data[rowId].CctrAg = response.data[0].deptNm;
                vm.data[rowId].CctrAgCd = response.data[0].deptCd;
                rowNode.setDataValue('CctrAg', response.data[0].deptNm);
              } else {
                this.$modal.open({
                  component: Cctr_Ag,
                  parent: this,
                  width: 700,
                  props: {
                    param: pVal,
                  },
                  events: {
                    close(obj) {
                      vm.data[rowId].CctrAg = obj.deptNm;
                      vm.data[rowId].CctrAgCd = obj.deptCd;
                      rowNode.setDataValue('CctrAg', obj.deptNm);
                    }
                  }
                })
              }
            })
      } else {
        vm.data[rowId].CctrAg = '';
        vm.data[rowId].CctrAgCd = '';
        rowNode.setDataValue('CctrAg', '');
      }
    },

    initCctrAg(force) {
      if (force === true) this.form.CctrAg = '';
      if (this.form.CctrAg === '') {
        this.form.CctrAg = '';
        this.form.CctrAgCd = '';
      }
    },
    cellValueChange(params){
      const vm = this;
      const rowNode = params.api.getRowNode(params.node.rowIndex);
    },
  },
}
</script>
