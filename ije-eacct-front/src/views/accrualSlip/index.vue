<template>
  <div class="inner-box" ref="innerBox">
    <div class="content-name">
      <h2 class="dp-ib">발생전표</h2>
      <div class="btn-type1 clearfix">
        <el-button v-show="showReadApprove" type="success" size="normal" icon="el-icon-printer" @click="print($refs.hd.value)">인쇄</el-button>
        <el-button v-show="showApprove" type="primary" size="normal" icon="el-icon-folder-opened" @click="apprSubmit($refs)">결재상신</el-button>
        <el-button type="success" size="normal" icon="el-icon-document-add" @click="newPageLoad">신규</el-button>
        <el-button v-show="showDelete" type="info" size="normal" icon="el-icon-delete" @click="slipDelete($refs.hd)">삭제</el-button>
        <el-button v-show="showSave" type="primary" size="normal" icon="el-icon-folder-checked" @click="save($refs)">저장</el-button>
        <el-button v-show="showRecycle" type="success" size="normal" icon="el-icon-s-help" @click="recycle()">전표재사용</el-button>
        <el-button v-show="showReadApprove" type="primary" size="normal" icon="el-icon-folder-opened" @click="approveOpenModal($refs.hd.value)">결재보기</el-button>
        <!-- <el-button type="success" size="normal" icon="el-icon-document-add" @click="openCopCard">test</el-button> -->
      </div>
    </div>
    <!-- HEADER -->
    <accrl-hd ref="hd"></accrl-hd>

    <!-- GRID -->
    <div class="table-area"  ref="dt">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">거래상세내역</h3>
          </div>
          <div v-if="showSave" >
            <div class="btn-type2 clearfix mr3" v-for="(item, idx) in buttons" :key="idx">

              <el-button v-if="!item.hide" :type="item.type" :size="item.size" :icon="item.icon" @click="btnAction(item)">{{ item.name }}</el-button>
              <!-- <el-button type="info" icon="el-icon-search">예산조회</el-button>
                        <el-button type="danger" icon="el-icon-delete">라인부가세코드삭제</el-button>
                        <el-button type="primary" icon="el-icon-edit">선급금반제 입력</el-button> -->
            </div>
            <div class="btn-wrap btn-type1 clearfix mr5" :style="{display: ['ACARD', 'CARD', 'TRIP', 'PO', 'IM'].includes($refs.hd.value.slipTypeCd) ? 'none': ''}">
              <el-checkbox v-model="data.taxbill_amt_modify" @change="amountCalcurate">부가세액 조정</el-checkbox>
            </div>
          </div>
        </div>
        <div class="grid-tbl-box">
          <!-- :rowData="rowData.filter(f => f.status != 'D')" -->
          <ag-grid-vue
              id="agGrid"
              ref="grid"
              class="ag-theme-alpine"
              rowSelection="multiple"
              style="width: 100%; height: 280px;"
              :columnDefs="columnDefs"
              :gridOptions="gridOptions"
              :framework-components="frameworkComponent"
              :rowData="rowData"
              :defaultColDef="defaultColDef"
              :suppressMenuHide="true"
              :stopEditingWhenCellsLoseFocus="true"
              :enableRangeSelection="true"
              @bodyScroll="handleScroll($event)"
              @rowDataChanged="handleRowDataChanged($event)"
              @row-selected="onRowSelected"
              @grid-ready="onGridReady"
              @cell-key-press="onCellKeyPress"
              @cell-value-changed="onCellValueChanged"
          />
        </div>
      </div>
    </div>

    <!-- 파일 업로드 -->
    <div class="file-upload mt15" ref="file">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">파일보기</h3>
        </div>
      </div>
      <div class="file has-name" style="width: 1200px;">
        <!-- 법인카드 비통제성 예산 그룹웨어 문서 첨부 예외 -->
        <div class="file" style="margin-right: 10px;" v-show="showCardException">
          <div class="file-label" @click="openUploadWingsPopup()">
                    <span class="file-cta">
                        <span class="file-label"> 그룹웨어 문서(URL)</span>
                        <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                    </span>
            <span class="file-name" style="border-top-right-radius:0px; border-bottom-right-radius:0px;">
                        {{ this.$numeral(getJiniSize).format('00') }}<i class="far fa-file-alt"></i>
                    </span>
          </div>
          <div class="file-label" @click="openUploadGroupwarePopup()">
                    <span class="file-cta" style="border-left:0px; border-top-left-radius:0px; border-bottom-left-radius:0px;">
                        <span class="file-label"> 그룹웨어 문서(PDF)</span>
                        <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                    </span>
            <span class="file-name">{{ this.$numeral(getJiniFileSize).format('00') }}<i class="far fa-file-alt"></i></span>
          </div>
        </div>
        <!-- 일반 그룹웨어 문서 첨부 -->
        <div class="file" style="margin-right: 10px;" v-show="!showCardException">
          <div class="file-label" @click="openUploadWingsPopup()">
                    <span class="file-cta">
                        <span class="file-label"> 그룹웨어 문서</span>
                        <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                    </span>
            <span class="file-name">
                        {{ this.$numeral(getJiniSize).format('00') }}<i class="far fa-file-alt"></i>
                    </span>
          </div>
        </div>

        <div class="file">
          <div class="file-label" @click="openUploadEvidencePopup()">
                    <span class="file-cta">
                        <span class="file-label"> 증빙첨부</span>
                        <span class="icon-bar"><img src="/img/bar.png" alt="" /></span>
                    </span>
            <span class="file-name">{{ this.$numeral(getEvidFileSize).format('00') }}<i class="far fa-file-alt"></i></span>
          </div>
        </div>
      </div>
      <div class="table-area mt20">
        <div class="table-b">
          <div class="table-header">
            <div class="table-name">
              <h3 class="ico_table_name">비고</h3>
            </div>
          </div>
          <div class="grid-tbl-box">
            <el-input
                type="textarea"
                class="mb40"
                :rows="5"
                placeholder="내용을 입력하세요."
                maxlength="4000"
                show-word-limit
                v-model="data.remark"
                :readonly="!showSave"
                style="font-size: 0.9em;">
            </el-input>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>

import _ from 'lodash';
import assert from '@/libs/assert';
// import slip from '@/mixin/accrual-slip';
/**
 * * Components
 */
import accrlHd from '@/components/accrualSlip/HD'; /** 발생전표 헤더 */
/**
 * * Modals
 */
import WingsAtchPop from '@/components/JiniAtchPop'; /** Wings 문서 팝업 */
import EvidAtchPopGroupware from '@/components/EvidAtchPopGroupware'; /** 그룹웨어 증빙첨부 파일 */
import EvidAtchPopModeless from '@/components/EvidAtchPopModeless'; /** 증빙첨부 파일 */
import BudgtAmountModal from '@/components/accrualSlip/Modals/BudgtAmountModal.vue'; /** 예산조회 팝업 */
import CorpCardHstModal from '@/components/accrualSlip/Modals/CorpCardHstModal.vue'; /** 법인카드사용내역 */
// import ApprSubm from '@/views/ApprSubm.vue' /** 결재상신 */
import ApprovalModal from '@/components/accrualSlip/Approval/Main.vue'; /** 결재상신(대체) */
import PrintModal from '@/components/accrualSlip/Print/Main.vue'; /** 인쇄 팝업 */
/**
 * * grid 관련 모달
 */
import Cctr2Modal from '@/components/accrualSlip/GridModal/Cctr2Modal'; /** 예산부서 조회 */
import CctrModal from '@/components/accrualSlip/GridModal/CctrModal'; /** 귀속부서 조회 */
import PjtModal from '@/components/accrualSlip/GridModal/PjtModal'; /** 프로젝트 조회 */
import TaskModal from '@/components/accrualSlip/GridModal/TaskModal'; /** Task 조회 */
import ProductModal from '@/components/accrualSlip/GridModal/ProductModal'; /** 제품군 조회 */
import AccountModal from '@/components/accrualSlip/GridModal/AccountModal'; /** 계정과목 조회 */
import SlipMngItemPop from '@/components/SlipMngItemPop'; /** 관리항목 팝업 */
import VatModal from '@/components/accrualSlip/GridModal/VatModal'; /** 부가세 코드 조회 */
import SubTrxTypeModal from '@/components/accrualSlip/GridModal/SubTrxTypeModal'; /** 보조 거래유형 조회 */

/**
 * * grid 관련 컴포넌트
 */
import AgGridSearchBtn from "@/components/agGrid/AgGridSearchBtn.vue"; /** 그리드 검색 버튼 컴포넌트 */
import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'; /** 그리드 select 컴포넌트 */
import DatepickerCellRenderer from "@/components/agGrid/el-datepicker-cell-renderer"; /** 그리드 date 컴포넌트 */

/**
 * * (임시) 자리수 표기
 * @param {*} curCd
 * @param {*} decimal
 */
const numFormats = (curCd, decimal) => {
  let format = `0,000`;
  if(decimal > 0 && curCd != 'KRW') {
    format = `${format}.` + '#'.repeat(decimal);
  }
  return format;
}

export default {
  name: 'accrualSlip',
  components: {
    accrlHd,
  },
  mixins: [],
  data() {
    return {
      buttons: [],
      rowData: [],
      rowCount: 0,
      gridOptions: {
        suppressScrollOnNewData: true,
        // suppressCellFocus: true,
      },
      columnDefs:[],
      defaultColDef: {
        resizable: true,
        // flex: 1
      },
      frameworkComponent: {
        schBtn: AgGridSearchBtn,
        select: SelectCellRenderer,
        date: DatepickerCellRenderer,
      },
      gridApi : null,
      columnApi : null,
      stayScrolledToEnd: true,
      data: {
        taxbill_amt_modify: false, //부가세액조정
        remark: ''
      },
      options: {  //공통코드 리스트
        'TAX_FLAG': [], //공제여부 유형
        'DISTANCE_GB': [], //시내/시외
        'AREA_LIST': [], //출장거리
        'OIL_KIND': [], //유종
        'TPS_TYPE': [], //교통수단
        'TRAFFIC_TYPE': [], //기타교통비 1,2,3
        'TRIP_AMT_TYPE': [], //국내/해외 출장 용도
        'WITHHOLDING_TAX_CODE': [], //원천세코드(잡급비)
      },
      // 파일관련 변수
      showWingsAtchPop: true,
      evidFileSize: 0,
      wingsFileSize: 0,
      showSave: false,
      showDelete: false,
      showApprove: false,
      showRecycle: false,
      showReadApprove: false,
      showCardException: false,
      // 통제 계정
      controlledAccount: []
    }
  },
  computed: {
    getEvidFileSize() {
      return this.$store.state.evidFileSize;
    },
    getJiniSize() {
      return this.$store.state.jiniSize;
    },
    getJiniFileSize() {
      return this.$store.state.jiniFileSize;
    },
  },
  async created() {
    const { slipHeaderId } = this.$route.params;

    /**
     * * 버튼 활성화 이벤트 버스
     */
    this.$bus.on('setShowingButton', (val) => {
      this.showSave = val.save;
      this.showDelete = val.delete;
      this.showApprove = val.approve;
      this.showRecycle = val.recycle;
      this.showReadApprove = val.readApprove;
      this.showCardException = val.cardException;
    });

    /**
     * * 신규 그리드 셋팅 (헤더 버스 on 이벤트)
     */
    this.$bus.on('setGridColDef', (val) => {
      this.rowData = [];
      this.createColumnDef(val, true);
    });
    /**
     * 세금계산서 세업 이후 세금 관련 정보 세팅
     * * 초기화 후 행 추가 (헤더 버스 on 이벤트)
     */
    this.$bus.on('setDefaultTaxLine', (data = {}) => {
      this.rowData = [];
      this.addRow().then(() => {
        const rowNode = this.gridApi.getRowNode(0);
        if(data.chargetotal) {
          console.log("data.chargetotal " , data.chargetotal)
          rowNode.setDataValue('supplyAmount', data.chargetotal);
          this.setGridDataValue(0, 'supplyAmount', data.chargetotal);
        }
        if(data.taxtotal) {
          console.log("data.vatAmount " , data.taxtotal)
          rowNode.setDataValue('vatAmount', data.taxtotal);
          this.setGridDataValue(0, 'vatAmount', data.taxtotal);
        }
        if(data.dtiType) {
          console.log("data.dtiType " , data.dtiType)
        }
      })
          .finally(() => {
            //부가세 금액이 들어올 때의 라인 세금코드 세팅
            this.$nextTick(() => {
              const taxRateId = this.$refs.hd.value.taxRateId || '';
              const taxRateCode = this.$refs.hd.value.taxRateCode || '';
              this.rowData.forEach((item, idx) => {
                const rowNode = this.gridApi.getRowNode(idx);
                this.setGridDataValue(idx, 'taxId', taxRateId);
                this.setGridDataValue(idx, 'taxCd', taxRateCode);
                this.setGridDataValue(idx, 'taxNm', taxRateCode);
                rowNode.setDataValue('taxId', taxRateId);
                rowNode.setDataValue('taxCd', taxRateCode);
                rowNode.setDataValue('taxNm', taxRateCode);

                if(taxRateCode == '매입불공제') {
                  rowNode.setDataValue('taxFlag', 'N');
                  rowNode.setDataValue('lineAttribute1', 'Y');
                } else if(taxRateCode != '' && taxRateCode != '매입불공제') {
                  rowNode.setDataValue('taxFlag', 'Y');
                  rowNode.setDataValue('lineAttribute1', '');
                } else {
                  rowNode.setDataValue('lineAttribute1', '');
                }
              });
            })
          });
    });
    /**
     * 세금계산서 세업 이후 세금 관련 정보 세팅
     * * 초기화 후 행 추가 (헤더 버스 on 이벤트)
     */
    this.$bus.on('addRow', (data = {}) => {
      this.rowData = [];
      this.addRow();
    });
    /**
     * * 그리드 활성 여부 (헤더 버스 on 이벤트)
     */
    this.$bus.on('setGridEditingStatus', (values) => {
      try {
        if(!!this.columnDefs && this.columnDefs.length > 0) {
          this.columnDefs = [];
          this.createColumnDef(values.slipTypeCd, values.editable);
        } else {
          // this.$message.error({ type: '알림', message: '거래유형을 선택해주세요.' });
          // $refs.hd가 아직 마운트되지 않은 타이밍 가드 (미준비 시 무시)
          if (this.$refs.hd && this.$refs.hd.value) this.$refs.hd.value.prepaymentApplyFlag = 'N';
        }
      } catch (e) {
        if (this.$refs.hd && this.$refs.hd.value) this.$refs.hd.value.prepaymentApplyFlag = 'N';
      }
    });
    /**
     * * 공급가액 0 으로 초기화
     */
    this.$bus.on('supplyAmountInit', () => {
      this.rowData = this.rowData.map(data => {
        data.supplyAmount = 0;
        return data;
      });
      this.$refs.hd.initialize(`initAmounts`);
    });

    const [taxes, distance ,area, oil, tps, traffic, trip, withholding] = await Promise.all([
      this.$http.get('/api/code/combo', { params: { groupCd: 'TAX_FLAG' } }),
      this.$http.get('/api/code/combo', { params: { groupCd: 'DISTANCE_GB_CD' } }),
      this.$http.get('/api/code/combo', { params: { groupCd: 'BUSINESS_TRIP_AREA_CD' } }),
      this.$http.get('/api/code/combo', { params: { groupCd: 'OIL_KIND_CD' } }),
      this.$http.get('/api/code/combo', { params: { groupCd: 'TPS_TYPE_CD' } }),
      this.$http.get('/api/code/combo', { params: { groupCd: 'TRAFFIC_TYPE_CD' } }),
      this.$http.get('/api/code/combo', { params: { groupCd: 'TRIP_AMT_TYPE_CD' } }),
      this.$http.get('/api/code/combo', { params: { groupCd: 'WITHHOLDING_TAX_CODE' } }),
    ]);
    //공제여부
    this.options['TAX_FLAG'] = taxes.data;
    this.options['TAX_FLAG'].unshift({key: '', value: '선택안함'});
    //출장거리 리스트
    this.options['AREA_LIST'] = area.data;
    this.options['AREA_LIST'].unshift({key: '00', value: '직접입력'});
    //시내/시외
    this.options['DISTANCE_GB'] = distance.data;
    this.options['DISTANCE_GB'].unshift({key: '', value: '선택'});
    //유종
    this.options['OIL_KIND'] = oil.data;
    this.options['OIL_KIND'].unshift({key: '', value: '선택'});
    //교통수단
    this.options['TPS_TYPE'] = tps.data;
    this.options['TPS_TYPE'].unshift({key: '', value: '선택'});
    //기타교통비1,2,3
    this.options['TRAFFIC_TYPE'] = traffic.data;
    this.options['TRAFFIC_TYPE'].unshift({key: '', value: '선택'});
    //국내/해외 출장 용도
    this.options['TRIP_AMT_TYPE'] = trip.data;
    //원천세코드
    this.options['WITHHOLDING_TAX_CODE'] = withholding.data;
    this.options['WITHHOLDING_TAX_CODE'].unshift({key: '', value: '선택'});
  },
  mounted() {
  },
  destroyed() {
    console.log("전표발생 나감.");
    //증빙파일 첨부 / Winds 문서의 스토어 Clear
    this.$store.commit('setEvidFileSize', 0);
    this.$store.commit('setJiniSize', 0);
    this.$store.commit('setJiniFileSize', 0);
  },
  methods: {
    reloadData(slipHeaderId) {
      const {slipTypeCd, trxTypeCode} = this.$refs.hd.value;
      let methodPath = `/api/slip/line`;
      if(slipTypeCd === 'TRAFFIC') {
        methodPath = `/api/slip/trafficLine`;
      }
      return new Promise((resolve, reject) => {
        this.createColumnDef(slipTypeCd || 'COMMON', true)
            .then(() => {
              this.$http.get(`${methodPath}/${slipHeaderId}`)
                  .then(res => {

                    this.rowData = res.data.map((x, i) => {
                      //디테일 저장시 키값 재정의
                      x.id = this.getNewRowId();
                      x.slipTypeCd = x.slipTypeCode;
                      x.cctrCd = x.budgetDeptCode;
                      x.cctrNm = x.budgetDeptName;
                      x.deptCd = x.actualDeptCode;
                      x.deptNm = x.actualDeptName;
                      x.deptType = x.actualDeptType;
                      x.acctCd = x.acctCode;
                      x.acctNm = x.acctName;
                      x.acctDffCnt = x.dffCnt;
                      x.acctRequiredFlagCnt = x.requiredFlagCnt;
                      x.projectId = x.projectId;
                      x.pjtCd = x.projectCode;
                      x.pjtNm = x.projectName;
                      x.taskNo = x.taskCode;
                      x.taskNm = x.taskName;
                      x.trAcctCd = x.trAcctCode; //금융계좌 코드
                      x.productCd = x.itemGroupCode;
                      x.productNm = x.itemGroupName;
                      x.taxAcctCode = x.taxAcctCode;
                      x.taxId = x.taxId;
                      x.taxCd = x.taxCode;
                      x.taxNm = x.taxCode;
                      x.segment9Cd = x.segment9Code;
                      x.segment10Cd = x.segment10Code;
                      if(['MCARD', 'ACARD', 'CARD'].includes(slipTypeCd)) {
                        x.c_type = x.attribute6;
                      }
                      if(slipTypeCd === 'TRIP') {
                        // 전기(해외출장)
                        if(trxTypeCode === 'SPAP167') {
                          x.amtType = x.oamtType;
                        }
                        // 전기(국내출장)
                        if(trxTypeCode === 'SPAP170') {
                          x.amtType = x.iamtType;
                        }
                      }
                      if(['AWT', 'ETCAWT'].includes(slipTypeCd)) {
                        if(x.withholdingTaxCode === 'null') {
                          x.withholdingTaxCode = '';
                        }
                      }
                      return x;
                    });

                    if(trxTypeCode === 'SPAP004'){
                      this.$http.get('/api/slip/getControlledAccount')
                          .then(res => {
                            this.controlledAccount = res.data
                          })
                    }

                    resolve(this.rowData);
                  })
                  .catch(err => reject(err))
                  .finally(() => {
                    this.$refs.hd.gridUpdate()
                    this.amountCalcurate();
                  })
            }).finally(() => {
        }); //end of createColmnDef

      }); //end of promise

    },
    onGridReady(params) {
      this.gridApi = params.api;
      this.columnApi = params.columnApi;
    },
    /**
     * * 그리드 Cell Enter Key 전용으로 사용 .
     * @param {*} e
     */
    onCellKeyPress(e) {
      if (!e.event) {
        return;
      }
      const keyboardEvent = e.event;
      const key = keyboardEvent.key;
      const field = e.colDef.field;

      if (key.length) {
        if (key === 'Enter') {
          // let rowNode = e.node;
          // const newSelection = !rowNode.isSelected();
          // rowNode.setSelected(newSelection);

          if(field === 'cctrNm') { //예산부서
            this.cctr2EnterPop(e.rowIndex ,e.value)
          }
          if(field === 'deptNm') { //귀속부서
            this.cctrEnterPop(e.rowIndex ,e.value)
          }
          if(field === 'acctNm') { //계정과목
            this.acctEnterPop(e.rowIndex ,e.value);
          }
          if(field === 'pjtNm') {  //프로젝트
            this.projectEnterPop(e.rowIndex ,e.value);
          }
          if(field === 'taskNm') {  //Task
            this.taskEnterPop(e.rowIndex ,e.value);
          }
          if(field === 'productNm') {  //제품군
            this.productEnterPop(e.rowIndex ,e.value);
          }
          if(field === 'taxNm') {  //세금코드
            this.vatEnterPop(e.rowIndex ,e.value);
          }
          if(field === 'subTrxTypeName') {  //유형코드
            this.subTrxTypeEnterPop(e.rowIndex ,e.value);
          }
        }
      }
    },
    /**
     * * 그리드 변수 변경 이벤트 시
     * @param {*} params
     */
    onCellValueChanged(params) {

      const field = params.colDef.field;
      const newValue = params.newValue;
      const oldValue = params.oldValue;
      const rowIndex = params.node.id;
      const newData = this.rowData[rowIndex];
      const rowNode = this.gridApi.getRowNode(rowIndex);

      const { curCd, lineAttribute1, percentageRate, excRt, slipTypeCd, orgId, integrationVendorNum, trxTypeCode, taxAcctCode, taxAcctName } = this.$refs.hd.value;
      const { options, value, isTaxReadOnly } = this.$refs.hd;

      //통화 자리수 확인
      const AMT_LENGTH = options['FRGN_CUR_CD'].filter(f => f.currencyCode === curCd )[0]?.precision || 0;
      /**
       * * 소수점 합계 금액
       * @param {*} a
       * @param {*} b
       * @return
       */
      const sumAmt = (a = 0, b = 0) => {
        const result = (parseFloat(parseFloat(a).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(b).toFixed(AMT_LENGTH)));
        return this.$numeral(result).format(numFormats(curCd,AMT_LENGTH));
      };
      if(field === 'withholdingTaxCode') { /** 원천세 (잡급비) */
      rowNode.setDataValue(field, newData.withholdingTaxCode);
        this.amountCalcurate();
      }
      else if(field === 'supplyAmount') { /** 공급가액 */
      const supplyAmount = Math.round(curCd === 'KRW' ? this.$numeral(newData.supplyAmount).value() * 1 : newData.supplyAmount);

        if(['AWT', 'ETCAWT'].includes(slipTypeCd)) {
          this.setGridDataValue(rowIndex, 'supplyAmount', supplyAmount);
          // rowNode.setDataValue(field, supplyAmount);
          this.amountCalcurate();
        } else {

          if(curCd === 'KRW') {
            this.setGridDataValue(rowIndex, 'supplyAmount', supplyAmount);
            const vatAmount = this.$numeral(newData.taxId ? Math.round(supplyAmount * (this.$numeral(percentageRate).value() / 100) ) : 0).value();
            this.setGridDataValue(rowIndex, 'vatAmount', vatAmount);
            this.setGridDataValue(rowIndex, 'usedAmt', this.$numeral(sumAmt(supplyAmount, vatAmount)).value());
            this.setGridDataValue(rowIndex, 'accountedVatAmount', vatAmount);
            this.setGridDataValue(rowIndex, 'accountedSupplyAmount', supplyAmount);
          } else {
            const tax_amount = 0;
            // this.setGridDataValue(rowIndex, 'vatAmount', this.$numeral(newData.supplyAmount * (percentageRate / 100)).value());
            this.setGridDataValue(rowIndex, 'usedAmt', this.$numeral(sumAmt(newData.supplyAmount, tax_amount || 0)).value());
            this.setGridDataValue(rowIndex, 'accountedVatAmount', tax_amount);
            // this.setGridDataValue(rowIndex, 'accountedSupplyAmount', this.$numeral(newData.supplyAmount).value() * 1 * this.$numeral(excRt).value());
            this.setGridDataValue(rowIndex, 'accountedSupplyAmount', supplyAmount);
          }

          this.amountCalcurate();
        }
      }
      else if(field === 'afterTaxAmount') { // 실지급액 (원천세 / 잡급비)
        if(['AWT', 'ETCAWT'].includes(slipTypeCd)) {

          const afterTaxAmount = Math.round(this.$numeral(newData.afterTaxAmount).value());

          rowNode.setDataValue(`supplyAmount`, afterTaxAmount);
          this.setGridDataValue(rowIndex, 'supplyAmount', afterTaxAmount);
          this.setGridDataValue(rowIndex, 'accountedSupplyAmount', afterTaxAmount);
        }
      }
      else if(field === 'vatAmount') { /** 세액 */
      const supplyAmount = Math.round(this.$numeral(newData.supplyAmount  * 1).value());

        const vatAmount = this.$numeral(newData.taxId ? newData.vatAmount : 0).value();

        this.setGridDataValue(rowIndex, 'vatAmount', vatAmount);

        if(this.data.taxbill_amt_modify || lineAttribute1 === 'V') {
          this.setGridDataValue(rowIndex, 'usedAmt', this.$numeral(sumAmt(supplyAmount, vatAmount)).value());
          if(curCd === 'KRW') {
            // const tax_amount = newData.taxId ? Math.round((supplyAmount * 1) * (percentageRate * 1) / 100) : 0;
            this.setGridDataValue(rowIndex, 'accountedVatAmount', vatAmount);
          } else {
            this.setGridDataValue(rowIndex, 'accountedVatAmount', 0);
          }
        } else {//부가세 세금계산서가 아니라면..
          // this.setGridDataValue(rowIndex, 'vatAmount', 0);
          this.setGridDataValue(rowIndex, 'accountedVatAmount', 0);
        }
        this.amountCalcurate();
      }
      else if(field == 'taxFlag') {
        if(['MCARD', 'ACARD', 'CARD', 'TRIP'].includes(slipTypeCd)) {

          /**
           * TODO: 법인카드의 경우에는 공제여부가 변경 될 때마다 세금코드가 변경되어야 하는 로직이 필요함.
           * * 법인카드 로직 -> 불공제 : 매입 불공제, 공제 : 매입신용카드, 면세 : 부가세액이 없는 경우
           * * 계정과목 접대비를 사용했을 경우에는 -> 불공제로 간다 (공제로 선택 후 저장시 불공제 계정이 존재합니다 라는 메세지가 나온다 전표 시스템에 로직이 있음)
           */
          if(newData.taxFlag == '' || newData.taxFlag === 'X') {
            const vatAmount = ['MCARD', 'ACARD', 'CARD', 'TRIP'].includes(slipTypeCd) ? this.$numeral(newData.taxId ? newData.vatAmount : 0).value() : this.$numeral(newData.vatAmount || 0).value();

            if(vatAmount === 0) {
              this.setGridDataValue(rowIndex, 'taxId', '');
              this.setGridDataValue(rowIndex, 'taxCd', '');
              this.setGridDataValue(rowIndex, 'taxNm', '');
              this.setGridDataValue(rowIndex, 'percentageRate', 0);
              this.setGridDataValue(rowIndex, 'taxAcctCode', '');
              this.setGridDataValue(rowIndex, 'taxAcctName', '');
              this.setGridDataValue(rowIndex, 'lineAttribute1', '');
            } else {
              this.$message.error({ type: `warning`, message: `부가세액이 존재 합니다. 공제 혹은 불공제 선택하세요.` });
              if(newData.taxCd === '매입불공제') {
                this.setGridDataValue(rowIndex, 'taxFlag', 'N');
              } else {
                this.setGridDataValue(rowIndex, 'taxFlag', 'Y');
              }
              // return false;
            }
          } else {

          }
          if(newData.taxCd) {
            if(newData.taxFlag === 'Y') { /** 공제 */
              if(newData.originalTaxFlag === 'N') {

                this.$message.error({ type: `warning`, message: `불공제 건은 변경 할 수 없습니다.` });
                this.setGridDataValue(rowIndex, 'taxFlag', 'N');

                return false;
              } else {
                if(orgId == "81") {
                  this.setGridDataValue(rowIndex, 'taxId', '10088');
                }
                if(orgId == "101") { //홀딩스
                  this.setGridDataValue(rowIndex, 'taxId', '10111');
                }
                if(orgId == "102") { //디앤코
                  this.setGridDataValue(rowIndex, 'taxId', '10139');
                }
                if(orgId == "10125") { //파트너스
                  this.setGridDataValue(rowIndex, 'taxId', '10139');
                }
                this.setGridDataValue(rowIndex, 'taxCd', '매입신용카드');
                this.setGridDataValue(rowIndex, 'taxNm', '매입신용카드');
                this.setGridDataValue(rowIndex, 'percentageRate', 10);
                this.setGridDataValue(rowIndex, 'lineAttribute1', '');

                //접대비가 아닌 경우 헤더의 부가세계정코드가 와야한다. (공제 예외)
                if(['5503010', '5503020', '5503030', '5352410', '5352420', '5352430',
                  '5352130', '5355410', '5501230', '5355430', '5355440', '5502730',
                  '5506010', '5506020', '5506030', '5506040', '5501210', '5501220',
                  '5501225'].includes(newData.acctCd)) {
                  this.setGridDataValue(rowIndex, 'taxAcctCode', newData.taxAcctCode || '');
                  this.setGridDataValue(rowIndex, 'taxAcctName', newData.taxAcctCode || '');
                } else {
                  //헤더의 부가세 계정코드
                  this.setGridDataValue(rowIndex, 'taxAcctCode', taxAcctCode);
                  this.setGridDataValue(rowIndex, 'taxAcctName', taxAcctName);
                }
              }
            } else {
              if(orgId == "81") {
                this.setGridDataValue(rowIndex, 'taxId', '10085');
              }
              if(orgId == "101") { //홀딩스
                this.setGridDataValue(rowIndex, 'taxId', '10108');
              }
              if(orgId == "102") { //디앤코
                this.setGridDataValue(rowIndex, 'taxId', '10136');
              }
              if(orgId == "10125") { //파트너스
                this.setGridDataValue(rowIndex, 'taxId', '10122');
              }

              this.setGridDataValue(rowIndex, 'taxCd', '매입불공제');
              this.setGridDataValue(rowIndex, 'taxNm', '매입불공제');
              this.setGridDataValue(rowIndex, 'percentageRate', 10);
              this.setGridDataValue(rowIndex, 'lineAttribute1', 'Y');
            }
          } else {
            if(integrationVendorNum === '1002318' || integrationVendorNum === '1004386') {
            } else {
              if(newData.taxFlag != 'X') {
                this.setGridDataValue(rowIndex, 'taxFlag', '');
              } else {
                this.setGridDataValue(rowIndex, 'taxFlag', 'X');
              }
            }
            // return false;
          }
          // const supplyAmount = Math.round(this.$numeral(newData.supplyAmount * 1).value());

          // if(curCd === 'KRW') {
          //     const tax_amount = Math.round((supplyAmount * 1) * (newData.percentageRate * 1) / 100) ;
          //     rowNode.setDataValue(`vatAmount`, this.$numeral(tax_amount).value());
          //     this.setGridDataValue(rowIndex, 'vatAmount', this.$numeral(tax_amount).value());
          // } else {
          //     const tax_amount = Math.round((supplyAmount * 1 * newData.percentageRate) * (newData.percentageRate * 1) / 100) ;
          //     rowNode.setDataValue(`vatAmount`, this.$numeral(tax_amount).value());
          //     this.setGridDataValue(rowIndex, 'vatAmount', this.$numeral(tax_amount).value());
          // }
          this.amountCalcurate();
        } //card if
        else {
          if(this.data.taxbill_amt_modify || lineAttribute1 === 'V') {
            const supplyAmount = Math.round(this.$numeral(newData.supplyAmount * 1).value());
            if(curCd === 'KRW') {
              const tax_amount = Math.round((supplyAmount * 1) * (newData.percentageRate * 1) / 100) ;
              this.setGridDataValue(rowIndex, 'vatAmount', this.$numeral(tax_amount).value());
            } else {
              const tax_amount = Math.round((supplyAmount * 1 * newData.percentageRate) * (newData.percentageRate * 1) / 100) ;
              this.setGridDataValue(rowIndex, 'vatAmount', this.$numeral(tax_amount).value());
            }
          } else {//부가세 세금계산서가 아니라면..
            this.setGridDataValue(rowIndex, 'vatAmount', 0);
          }
        } //card if else

      }  else if(field === 'usedDt') {
        if(slipTypeCd === 'TRAFFIC') { //여비교통비 일 때
          if(newData.usedDt && newData.oilBungae) {
            this.oliPriceCalcuration(newData.usedDt, newData.oilBungae)
                .then(data => {
                  const {oilKindCd, oilKindNm, oilUpce, oilEff, remark} = data;
                  this.setGridDataValue(rowIndex, 'oilPrice', oilUpce); //유류대
                  this.setGridDataValue(rowIndex, 'mileage', oilEff); //연비
                  const distance = this.$numeral(newData.distance || 0).value();
                  const oilPrice = this.$numeral(oilUpce || 0).value();
                  const fuelEff = this.$numeral(oilEff || 0).value();
                  this.setGridDataValue(rowIndex, 'oilAmt', Math.round((distance / fuelEff) * oilPrice)); // 계산 = (거래 / 연비) * 유류대


                  this.amountCalcurate();
                })
                .catch(err => {
                  this.setGridDataValue(rowIndex, 'oilBungae', '');
                  return false;
                });
          }
        } //traffic if end
      } else if(field === 'trafficType') {
        if(slipTypeCd === 'TRAFFIC') { //여비교통비 일 때
          if(newData.trafficType === 'B') {
            this.$message.error({ type: `warning`, message: `대중교통은 유종을 선택 할 수 없습니다.` });
            this.setGridDataValue(rowIndex, 'oilBungae', '');
            this.setGridDataValue(rowIndex, 'oilPrice', 0);
            this.setGridDataValue(rowIndex, 'mileage', 0);
            this.setGridDataValue(rowIndex, 'oilAmt', 0);

            return false;
          }
          this.setGridDataValue(rowIndex, field, newData[field]);
          if(newData.usedDt && newData.oilBungae) {
            this.oliPriceCalcuration(newData.usedDt, newData.oilBungae)
                .then(data => {
                  const {oilKindCd, oilKindNm, oilUpce, oilEff, remark} = data;
                  this.setGridDataValue(rowIndex, 'oilPrice', oilUpce); //유류대
                  this.setGridDataValue(rowIndex, 'mileage', oilEff); //연비
                  const distance = this.$numeral(newData.distance || 0).value();
                  const oilPrice = this.$numeral(oilUpce || 0).value();
                  const fuelEff = this.$numeral(oilEff || 0).value();
                  this.setGridDataValue(rowIndex, 'oilAmt', Math.round((distance / fuelEff) * oilPrice)); // 계산 = (거래 / 연비) * 유류대

                  this.amountCalcurate();
                })
                .catch(err => {
                  this.setGridDataValue(rowIndex, 'oilBungae', '');
                  return false;
                });
          }
        }
      } else if(field === 'distance') {
        if(slipTypeCd === 'TRAFFIC') { //여비교통비 일 때
          this.setGridDataValue(rowIndex, field, this.$numeral(newData[field]).value());
          if(newData.usedDt && newData.oilBungae) {
            this.oliPriceCalcuration(newData.usedDt, newData.oilBungae)
                .then(data => {
                  const {oilKindCd, oilKindNm, oilUpce, oilEff, remark} = data;
                  this.setGridDataValue(rowIndex, 'oilPrice', oilUpce); //유류대
                  this.setGridDataValue(rowIndex, 'mileage', oilEff); //연비
                  const distance = this.$numeral(newData.distance || 0).value();
                  const oilPrice = this.$numeral(oilUpce || 0).value();
                  const fuelEff = this.$numeral(oilEff || 0).value();
                  this.setGridDataValue(rowIndex, 'oilAmt', Math.round((distance / fuelEff) * oilPrice)); // 계산 = (거래 / 연비) * 유류대

                  this.amountCalcurate();
                })
                .catch(err => {
                  this.setGridDataValue(rowIndex, 'oilBungae', '');
                  return false;
                });
          }
        } //traffic if end

      } else if(field === 'etcAmt1' || field === 'etcAmt2' || field === 'etcAmt3') {
        if(slipTypeCd === 'TRAFFIC') { //여비교통비 일 때 : 기타금액 1,2,3
          this.setGridDataValue(rowIndex, field, this.$numeral(newData[field]).value());
          if(field === 'etcAmt1') {
            const etcChitGubun1 = newData.etcChitGubun1;
            if(etcChitGubun1 === '01') {
              this.$message.error({ type: `warning`, message: `카드는 변경할 수 없습니다.` });
              return false;
            }
          }
          if(field === 'etcAmt2') {
            const etcChitGubun2 = newData.etcChitGubun2;
            if(etcChitGubun2 === '01') {
              this.$message.error({ type: `warning`, message: `카드는 변경할 수 없습니다.` });
              return false;
            }
          }
          if(field === 'etcAmt3') {
            const etcChitGubun3 = newData.etcChitGubun3;
            if(etcChitGubun3 === '01') {
              this.$message.error({ type: `warning`, message: `카드는 변경할 수 없습니다.` });
              return false;
            }
          }

          this.amountCalcurate();
        }
      } else if(field === 'distanceGubun') {
        if(slipTypeCd === 'TRAFFIC') {  //여비교통비 일 때
          const { temp2 } = this.$refs.hd.value.traffic;
          if(!newData.usedDt) {
            this.rowData[rowIndex][field] = '';
            this.gridOptions.api.refreshCells();
            this.$message.error({ type: `warning`, message: `사용일자를 먼저 선택해주세요.` });
            return ;
          }

          if(newData[field] && !temp2) {
            this.setGridDataValue(rowIndex, field, '');
            this.$message.error({ type: `warning`, message: `자가운전 보조금 대상자 해당여부를 체크하여 주시기 바랍니다.` });
            return ;
          }

          if(newData[field] === '1' && temp2 === 'Y') {
            this.setGridDataValue(rowIndex, field, '');
            this.$message.error({ type: `warning`, message: `자가운전 보조금 대상자는 시내교통비를 선택할 수 없습니다.` });
            return ;
          }
        } //traffic if end
      } else if(field === 'fromArea') {
        if(slipTypeCd === 'TRAFFIC') {  //여비교통비 일 때

          if(!newData.usedDt) {
            this.setGridDataValue(rowIndex, field, '');
            this.$message.error({ type: `warning`, message: `사용일자를 먼저 선택해주세요.` });
            return ;
          }
          const area = this.options['AREA_LIST'].filter(f => f.key === newData.fromArea)[0];
          if(area.key === '00') { //직접입력 일 경우 (수동 입력가능하도록)
            this.columnDefs = this.columnDefs.map(x => {
              if(x.field === 'fromAreaText' || x.field === 'distance') {
                x.editable = true;
              }
              return x;
            });
          } else {
            this.setGridDataValue(rowIndex, 'fromAreaText', area.value);
            this.columnDefs = this.columnDefs.map(x => {
              if(x.field === 'fromAreaText' || x.field === 'distance') {
                x.editable = false;
              }
              return x;
            });

            this.$http.post('/api/businessTripDis/slip', {standardYymm: newData.usedDt, search: area.value}).then(res => res.data)
                .then(data => {
                  if(!!data && data.length === 1) {
                    const { distance } = data[0] ;
                    this.setGridDataValue(rowIndex, 'distance', distance);
                  }
                }).finally(_ => {

              if(newData.usedDt && newData.oilBungae) {
                this.oliPriceCalcuration(newData.usedDt, newData.oilBungae)
                    .then(data => {
                      const {oilKindCd, oilKindNm, oilUpce, oilEff, remark} = data;
                      this.setGridDataValue(rowIndex, 'oilPrice', oilUpce); //유류대
                      this.setGridDataValue(rowIndex, 'mileage', oilEff); //연비
                      const distance = this.$numeral(newData.distance || 0).value();
                      const oilPrice = this.$numeral(oilUpce || 0).value();
                      const fuelEff = this.$numeral(oilEff || 0).value();
                      this.setGridDataValue(rowIndex, 'oilAmt', Math.round((distance / fuelEff) * oilPrice)); // 계산 = (거래 / 연비) * 유류대

                      this.amountCalcurate();
                    })
                    .catch(err => {
                      this.setGridDataValue(rowIndex, 'oilBungae', '');
                      return false;
                    });
              }
            });
          }
        } //traffic if end

      } else if(field === 'oilBungae') {
        if(slipTypeCd === 'TRAFFIC') {  //여비교통비 일 때
          if(!newData.usedDt) {
            this.$message.error({ type: `warning`, message: `사용일자를 먼저 선택해주세요.` });
            return false;
          }
          if(!newData.distance && !this.$numeral(newData.distance || 0).value()) {
            this.$message.error({ type: `warning`, message: `거리가 없습니다.` });
            return false;
          }

          this.oliPriceCalcuration(newData.usedDt, newData.oilBungae)
              .then(data => {
                const {oilKindCd, oilKindNm, oilUpce, oilEff, remark} = data;
                this.setGridDataValue(rowIndex, 'oilPrice', oilUpce); //유류대
                this.setGridDataValue(rowIndex, 'mileage', oilEff); //연비
                const distance = this.$numeral(newData.distance || 0).value();
                const oilPrice = this.$numeral(oilUpce || 0).value();
                const fuelEff = this.$numeral(oilEff || 0).value();
                this.setGridDataValue(rowIndex, 'oilAmt', Math.round((distance / fuelEff) * oilPrice)); // 계산 = (거래 / 연비) * 유류대

                this.amountCalcurate();
              })
              .catch(err => {
                this.setGridDataValue(rowIndex, field, '');//field : oilBungae
                return false;
              });
        } //traffic if end

      } //parent if end
      else if(field === 'acctCd') {
        if(['MCARD', 'ACARD', 'CARD'].includes(slipTypeCd)) {  //카드
          /**
           * 접대비 계정 컨트롤
           */
          if(['5503010', '5503020', '5503030', '5352410', '5352420', '5352430',
            '5352130', '5355410', '5501230', '5355430', '5355440', '5502730',
            '5506010', '5506020', '5506030', '5506040', '5501210', '5501220',
            '5501225'].includes(newData[field])) {

            if(Math.abs(newData.vatAmount * 1) > 0 ) {
              if(newData.taxCd != "매입불공제") {
                this.$alert(`불공제 계정이 존재합니다. 해당 계정을 확인하시고, 공제여부를 불공제로 변경하시기 바랍니다.\n\n※ 세액이 0원이면, 공제여부를 면세_영세_간이과세로 선택 하시고 진행하시기 바랍니다.`, '확인', {
                  dangerouslyUseHTMLString: true,
                  confirmButtonText: '확인',
                  type: 'error',
                  center: true
                });

                return false;
              }
            } else {
              if(integrationVendorNum === '1002318' || integrationVendorNum === '1004386') {
              } else {
                if(newData.taxCd && !(newData.taxCd === '매입영세' || newData.taxCd === '매입면세')) {
                  this.$alert(`불공제 계정이 존재합니다. 해당 계정을 확인하시고, 공제여부를 불공제로 변경하시기 바랍니다.\n\n※ 세액이 0원이면, 공제여부를 면세_영세_간이과세로 선택 하시고 진행하시기 바랍니다.`, '확인', {
                    dangerouslyUseHTMLString: true,
                    confirmButtonText: '확인',
                    type: 'error',
                    center: true
                  });
                  return false;
                }
              }
            }
          }
        } else {
          //법인카드 이외일경우
          if(['5503010', '5503020', '5503030', '5352410', '5352420', '5352430',
            '5352130', '5355410', '5501230', '5355430', '5355440', '5502730',
            '5506010', '5506020', '5506030', '5506040', '5501210', '5501220',
            '5501225'].includes(newData[field])) {

            if(Math.abs(newData.vatAmount * 1) > 0 ) {
              if(newData.taxCd != "매입불공제") {
                this.$alert(`불공제 계정이 존재합니다. 해당 계정을 확인하시고, 공제여부를 불공제로 변경하시기 바랍니다.\n\n※ 세액이 0원이면, 공제여부를 면세_영세_간이과세로 선택 하시고 진행하시기 바랍니다.`, '확인', {
                  dangerouslyUseHTMLString: true,
                  confirmButtonText: '확인',
                  type: 'error',
                  center: true
                });
                return false;
              }
            } else {
              if(newData.taxCd && !(newData.taxCd === '매입영세' || newData.taxCd === '매입면세')) {
                this.$alert(`불공제 계정이 존재합니다. 해당 계정을 확인하시고, 공제여부를 불공제로 변경하시기 바랍니다.\n\n※ 세액이 0원이면, 공제여부를 면세_영세_간이과세로 선택 하시고 진행하시기 바랍니다.`, '확인', {
                  dangerouslyUseHTMLString: true,
                  confirmButtonText: '확인',
                  type: 'error',
                  center: true
                });
                return false;
              }
            }
          }
        }
      } else if(field === 'etcType1') {
        if(!newData.trafficType) {
          this.$message.error({ type: `warning`, message: `교통수단을 선택해 주세요.` });
          return ;
        }
        if(newData.etcType1) {
          this.setGridDataValue(rowIndex, 'etcAmt1', 0);
          this.setGridDataValue(rowIndex, 'etcBungae1', 0);
          this.setGridDataValue(rowIndex, 'etcChitGubun1', '02');
          this.setGridDataValue(rowIndex, 'etcChitGubunName1', '현금');
          this.setGridDataValue(rowIndex, 'etcUsedNo1', '');
        } else {
          this.setGridDataValue(rowIndex, 'etcAmt1', 0);
          this.setGridDataValue(rowIndex, 'etcBungae1', 0);
          this.setGridDataValue(rowIndex, 'etcChitGubun1', '');
          this.setGridDataValue(rowIndex, 'etcChitGubunName1', '');
          this.setGridDataValue(rowIndex, 'etcUsedNo1', '');
        }
      } else if(field === 'etcType2') {
        if(!newData.trafficType) {
          this.$message.error({ type: `warning`, message: `교통수단을 선택해 주세요.` });
          return ;
        }
        if(newData.etcType2) {
          this.setGridDataValue(rowIndex, 'etcAmt2', 0);
          this.setGridDataValue(rowIndex, 'etcBungae2', 0);
          this.setGridDataValue(rowIndex, 'etcChitGubun2', '02');
          this.setGridDataValue(rowIndex, 'etcChitGubunName2', '현금');
          this.setGridDataValue(rowIndex, 'etcUsedNo2', '');
        } else {
          this.setGridDataValue(rowIndex, 'etcAmt2', 0);
          this.setGridDataValue(rowIndex, 'etcBungae2', 0);
          this.setGridDataValue(rowIndex, 'etcChitGubun2', '');
          this.setGridDataValue(rowIndex, 'etcChitGubunName2', '');
          this.setGridDataValue(rowIndex, 'etcUsedNo2', '');
        }
      } else if(field === 'etcType3') {
        if(!newData.trafficType) {
          this.$message.error({ type: `warning`, message: `교통수단을 선택해 주세요.` });
          return ;
        }
        if(newData.etcType3) {
          this.setGridDataValue(rowIndex, 'etcAmt3', 0);
          this.setGridDataValue(rowIndex, 'etcBungae3', 0);
          this.setGridDataValue(rowIndex, 'etcChitGubun3', '02');
          this.setGridDataValue(rowIndex, 'etcChitGubunName3', '현금');
          this.setGridDataValue(rowIndex, 'etcUsedNo3', '');
        } else {
          this.setGridDataValue(rowIndex, 'etcAmt3', 0);
          this.setGridDataValue(rowIndex, 'etcBungae3', 0);
          this.setGridDataValue(rowIndex, 'etcChitGubun3', '');
          this.setGridDataValue(rowIndex, 'etcChitGubunName3', '');
          this.setGridDataValue(rowIndex, 'etcUsedNo3', '');
        }
      } else if(field == 'globalAttribute13') { /** 하나카드(회장님 예외부분) 가맹점 사업자 번호 숫자만 입력 가능*/
      const reg = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z]/;
        let data = newData.globalAttribute13;
        if(reg.exec(newData.globalAttribute13)!==null) {
          data = newData.globalAttribute13.replace(/[^0-9]/g, '');
        }
        if(isNaN(parseFloat(data))) {
          data = '';
        }
        this.setGridDataValue(rowIndex, 'globalAttribute13', data);
      }

    },
    /**
     * * grid row 데이터 변화
     * @param {*} event
     */
    handleRowDataChanged(event) {
      const index = this.rowData.length - 1;
      if (this.stayScrolledToEnd && index >= 0) {
        this.gridOptions.api.ensureIndexVisible(index, 'bottom');
      }
    },
    /**
     * * 스크롤 감지하여 최 하단으로 이동 시킴.
     * @param {*} event
     */
    handleScroll(event) {
      const grid = document.getElementById('agGrid');
      if (grid) {
        const gridBody = grid.querySelector('.ag-body-viewport');
        const scrollPos = gridBody.offsetHeight + event.top;
        const scrollDiff = gridBody.scrollHeight - scrollPos;

        this.stayScrolledToEnd = (scrollDiff <= 3);
      }
    },
    /**
     * * 거래 유형별 (부모 코드로 사용) 컬럼 정의
     * @param {*} slipTypeCd
     */
    createColumnDef(slipTypeCd, editable) {

      const self = this;

      /**
       * * json 샘플데이터 호출 ...
       */
      const colDef = new Promise((resolve) => {
        fetch(`/json/accrual-slip.json`, { method: 'GET' })
            .then(res => res.json())
            .then(res => { resolve(res.data); })
            .catch(_ => { resolve([]); })
      });

      if(!this.$refs.hd || !this.$refs.hd.value) {
        return false;
      }

      const curCd  = this.$refs.hd.value.curCd || 'KRW';
      const options = this.$refs.hd.options;
      const editabledState = this.$refs.hd.editabledState;

      /**
       * * 그리드 컬럼 확장 하기.
       * @param {*} cols
       */
      const extensionColDef = (cols) => {


        const AMT_LENGTH = options['FRGN_CUR_CD']?.filter(f => f.currencyCode === curCd )[0]?.precision || 0;

        // if(!this.showSave) {
        //     cols.suppressMenu = false;
        // } else {
        //     cols.suppressMenu = true;
        // }

        if(cols.layout === 'checkbox') {    /** checkbox 컬럼 */
        cols.headerClass = 'ag-center-header';
          cols.headerStyle = {'display': 'flex', 'justify-content': 'center'};
          cols.headerCheckboxSelection = editabledState ? false : editable;
          cols.checkboxSelection = editabledState ? false : editable;
          cols.showDisabledCheckboxes = editabledState ? false : editable;
          cols.hide = editabledState;
          cols.sortable = false;
          cols.width = 50;
          cols.cellStyle = (params) => {
            return {'item-align': 'center', 'color' : 'transparent'};
          };
        } else if(cols.layout === 'popup') { /** modal 컬럼 */
        cols.width = 160;
          //전표 상태에 따라 팝업 설정
          cols.cellRenderer = editabledState ? '' : 'schBtn';
          if(cols.field === 'cctrNm') { //예산부서
            if(editable) {
              cols.cellRendererParams = {
                funcNm : `cctr2Pop`
              };
            }
          } else if(cols.field === 'deptNm') { //귀속부서
            if(editable) {
              cols.cellRendererParams = {
                funcNm : `cctrPop`
              };
            }
          } else if(cols.field === 'acctNm') { //계정과목
            if(editable) {
              cols.cellRendererParams = {
                funcNm : `acctPop`
              };
            }
          } else if (cols.field === 'pjtNm') { //프로젝트
            if(editable) {
              cols.cellRendererParams = {
                funcNm : `projectPop`
              };
            }
          } else if (cols.field === 'taskNm') { //타스크
            if(editable) {
              cols.cellRendererParams = {
                funcNm : `taskPop`
              };
            }
          } else if (cols.field === 'productNm') { //제품군
            if(editable) {
              cols.cellRendererParams = {
                funcNm : `productPop`
              };
            }
          } else if(editable && cols.field === 'attributeCategoryName') { //관리항목
            cols.cellRenderer = 'schBtn';
            cols.cellRendererParams = {
              funcNm : `attributeCategoryPop`
            };
            cols.valueFormatter = (params) => {
              if(!params.data.acctDffCnt && !params.value) {
                return `없음`;
              } else if(params.value) {
                return params.value;
              } else {
                return `입력하세요`;
              }
            };
          } else if (cols.field === 'taxNm') { //세금코드
            if(editable) {
              if(['MCARD', 'ACARD', 'CARD'].includes(slipTypeCd)) {
                cols.cellRendererParams = {
                  funcNm : `vatPop`,
                  disable: true
                };
              }
            }
          } else if (cols.field === 'subTrxTypeName') { //보조거래유형
            if(editable) {
              cols.cellRendererParams = {
                funcNm : `subTrxTypePop`,
                disable: false
              };
            }
          }
        } else if(cols.layout === 'select') { /** comboBox 컬럼 */
        cols.width = 140;
          cols.cellRenderer = 'select';
          if(cols.field === 'taxFlag') { //공제여부
            cols.cellRendererParams = {
              options : self.options["TAX_FLAG"],
              detailCd: 'key',
              detailNm: 'value',
              disable: true
            }
          }
          if(cols.field === 'fromArea') { //장소코드 (여비교통비)
            cols.cellRendererParams = {
              options : self.options["AREA_LIST"],
              detailCd: 'key',
              detailNm: 'value',
              disable: true
            }
          }
          if(cols.field === 'oilBungae') { //유종 (여비교통비)
            cols.cellRendererParams = {
              options : self.options["OIL_KIND"],
              detailCd: 'key',
              detailNm: 'value',
              disable: true
            }
          }
          if(cols.field === 'trafficType') { //교통수단 (여비교통비)
            cols.cellRendererParams = {
              options : self.options["TPS_TYPE"],
              detailCd: 'key',
              detailNm: 'value',
              disable: true
            }
          }
          if(cols.field === 'etcType1' || cols.field === 'etcType2' || cols.field === 'etcType3') { //기타교통비 (여비교통비)
            cols.cellRendererParams = {
              options : self.options["TRAFFIC_TYPE"],
              detailCd: 'key',
              detailNm: 'value',
              disable: true
            }
          }
          if(cols.field === 'distanceGubun') { //시내/시외 (여비교통비)
            cols.cellRendererParams = {
              options : self.options["DISTANCE_GB"],
              detailCd: 'key',
              detailNm: 'value',
              disable: true
            }
          }
          if(cols.field === 'amtType') { //용도 (국내/해외 출장)
            cols.cellRendererParams = {
              options : self.options["TRIP_AMT_TYPE"],
              detailCd: 'key',
              detailNm: 'value',
              disable: false
            }
          }

          if(cols.field === 'withholdingTaxCode') { //원천세코드(잡급비)
            cols.cellRendererParams = {
              options : self.options["WITHHOLDING_TAX_CODE"],
              detailCd: 'key',
              detailNm: 'value',
              disable: false
            }
          }


        } else if(cols.layout === 'date') {
          cols.cellRenderer = 'date';

          cols.cellRendererParams = (params) => {
            return {
              type: 'date',
              format: 'yyyyMMdd',  //date format
              valueFormat: 'yyyyMMdd',  //받는 model 의 결과 포맷
              disable: !cols.editable
            }
          };
        } else if(cols.layout === 'number') {
          if(slipTypeCd === 'MCARD' && cols.field === 'globalAttribute13') {
            cols.valueFormatter = (params) => {
              return self.$numeral(params.value).format('0');
            };
          } else {
            cols.valueFormatter = (params) => {
              return self.$numeral(params.value).format('0,0');
            };
          }
        }


        /** 금액 관련 콤마 숫자 포맷 형변환 */
        if(['supplyAmount', 'vatAmount', 'usedAmt', 'afterTaxAmount', 'oilPrice'].includes(cols.field)) {
          cols.valueFormatter = (params) => {
            if(cols.field == 'supplyAmount' || cols.field == 'usedAmt') {
              return self.$numeral(params.value).format(numFormats(curCd, AMT_LENGTH));
            } else if(cols.field == 'oilPrice') {
              return self.$numeral(params.value).format('0,0.0');
            } else {
              return self.$numeral(params.value).format('0,0');
            }
          }
          if(['ACARD', 'CARD', 'TRIP'].includes(slipTypeCd)) {
            if(cols.field === 'supplyAmount') {
              if('TRIP' === slipTypeCd) {
                cols.editable = (params) => {
                  if(params.data.c_type === '현금') {
                    return true;
                  } else {
                    return false;
                  }
                }
              } else {
                cols.editable = false;
              }
            }
          } else {
            if(cols.field === 'supplyAmount') {
              cols.editable = editable;
            }
          }
        }
        // else {
        //     cols.editable = editable;
        // }
        if(['cardUsedNo', 'globalAttribute8', 'globalAttribute12', 'globalAttribute13',
          'globalAttribute14', 'globalAttribute15', 'globalAttribute16', 'globalAttribute17',
          'globalAttribute18', 'globalAttribute19', 'globalAttribute20'].includes(cols.field)) {
          if(slipTypeCd === 'MCARD') {
          } else {
            cols.editable = false;
          }
        }
        if(['taxFlag'].includes(cols.field)) {
          if(['MCARD', 'ACARD', 'CARD', 'TRIP'].includes(slipTypeCd)) {
            cols.editable = editable;
          } else {
            cols.editable = false;
          }
        }

        // if(editable) {
        //     cols.cellStyle = () => {
        //         if(['MCARD', 'ACARD', 'CARD'].includes(slipTypeCd) && ['globalAttribute13', 'globalAttribute15', 'globalAttribute16', 'globalAttribute18', 'globalAttribute20'].includes(cols.field)) {
        //             return { 'background': 'transparent', 'textAlign': 'center'};
        //         }
        //         return { 'background' : 'transparent'};
        //     }
        // } else {
        //     cols.cellStyle = () => {
        //         if(['MCARD', 'ACARD', 'CARD'].includes(slipTypeCd) && ['globalAttribute13', 'globalAttribute15', 'globalAttribute16', 'globalAttribute18', 'globalAttribute20'].includes(cols.field)) {
        //             return { 'background': 'grey', 'textAlign': 'center'};
        //         }
        //         return { 'background' : 'grey'};
        //     }
        // }
        //타이틀 센터 정렬
        cols.headerClass = 'ag-center-header';

        //전표 상태에 따라 팝업 설정
        if(editabledState) {
          cols.editable = false;
        }
        return cols;
      };

      return new Promise((resolve, reject) => {
        /**
         * * 그리드 랜더링 시작 ... 그리고 헤더 버튼 셋업
         */
        colDef.then((data) => {
          if(slipTypeCd && data.length > 0) {
            const { columns, buttons, gridOptions, defaultColDef } = data.filter(f => f.type == slipTypeCd)[0];

            //1. 버튼 활성화 결정 ( 참고사항 : prepaymentInput 반제입력인데, 없앨 예정)
            this.buttons = buttons.map(button => {

              if(['rowAdd', 'rowDel', 'rowCopy', 'prepaymentInput'].includes(button.action) && !editable) {
                button.hide = true;
              } else {
                button.hide = false;
              }
              return button;
            }).sort((a, b) => b.sort - a.sort);
            //2. 그리드 옵션과 기본 컬럼 정의 값 병합
            Object.assign(this.gridOptions, gridOptions);
            Object.assign(this.defaultColDef, defaultColDef);
            // if(this.showSave) {
            //     this.defaultColDef.sortable = false;
            // }
            //3. 컬럼에 대한 옵션과 이벤트 등의 확장 재정의
            this.columnDefs = columns.map(x => {
              return extensionColDef(x);
            });
          } else {
            this.buttons = [];
            this.columnDefs = [];
          }
          resolve();
        })
            .catch(err => {
              reject(err);
            })
            .finally(() => {
              if(this.rowData.length > 0) {
                this.gridApi.redrawRows({ rowNodes: this.rowData });
                // this.gridApi.refreshCells();
              }
            });
      })


    },
    /**
     * * 예산부서 엔터 팝업
     * @param {*} idx
     * @param {*} val
     */
    cctr2EnterPop(idx, cctrNm) {
      if(!cctrNm) {
        this.initialize(`initCctr2`, idx)
        return false;
      }
      const cctr2Pop = () => {
        const self = this;
        const rowNode = this.gridApi.getRowNode(idx);

        const { personId, postingDt } = this.$refs.hd.value;

        if(!personId) {
          this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
          return false;
        }
        if(!postingDt) {
          this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
          return false;
        }

        this.$modal.open({
          component: Cctr2Modal,
          parent: this,
          props: {
            personId : personId,
            postingDate: postingDt,
            schTxt: rowNode.data.cctrNm
          },
          width: 700,
          events: {
            close(object) {
              self.setGridDataValue(idx, 'cctrCd', object.deptCd);
              self.setGridDataValue(idx, 'cctrNm', object.deptNm);
            }
          },
        });
      };

      const {personId, postingDt } = this.$refs.hd.value;
      const params = {
        personId,
        postingDate: postingDt,
        deptNm: cctrNm
      }
      this.$store.commit('loading');
      this.$http.post(`/api/slip/cctr2`, params)
          .then(res => res.data)
          .then(data => {
            console.log("data : ", data)
            if(data && data.length === 1) {
              const { deptCd , deptNm } = data[0];
              this.setGridDataValue(idx, 'cctrCd', deptCd);
              this.setGridDataValue(idx, 'cctrNm', deptNm);
            } else {
              cctr2Pop();
            }
          })
          .finally(() => {
            this.$store.commit('finish');
          });

    },
    /**
     * * 예산부서 팝업
     */
    cctr2Pop() {
      const self = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);

      const { personId, postingDt } = this.$refs.hd.value;

      if(!personId) {
        this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
        return false;
      }
      if(!postingDt) {
        this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
        return false;
      }

      this.$modal.open({
        component: Cctr2Modal,
        parent: this,
        props: {
          personId : personId,
          postingDate: postingDt,
          schTxt: rowNode.data.cctrNm
        },
        width: 700,
        events: {
          close(object) {
            self.setGridDataValue(self.rowId, 'cctrCd', object.deptCd);
            self.setGridDataValue(self.rowId, 'cctrNm', object.deptNm);
          }
        },
      });
    },
    /**
     * * 귀속부서 엔터 팝업
     * @param {*} idx
     * @param {*} deptNm
     */
    cctrEnterPop(idx, deptNm) {
      if(!deptNm) {
        this.initialize(`initCctr`, idx)
        return false;
      }
      const cctrPop = () => {
        const self = this;
        const rowNode = this.gridApi.getRowNode(idx);

        const {personId, postingDt } = self.$refs.hd.value;

        if(!personId) {
          this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
          return false;
        }
        if(!postingDt) {
          this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
          return false;
        }

        this.$modal.open({
          component: CctrModal,
          parent: self,
          props: {
            personId : personId,
            postingDate: postingDt,
            schTxt: rowNode.data.deptNm
          },
          width: 700,
          events: {
            close(object) {
              self.setGridDataValue(idx, 'deptCd', object.deptCd);
              self.setGridDataValue(idx, 'deptNm', object.deptNm);
              self.setGridDataValue(idx, 'deptType', object.attribute2);
            }
          },
        });
      };

      const {personId, postingDt } = this.$refs.hd.value;

      const params = {
        personId,
        postingDate: postingDt,
        deptNm
      }
      this.$store.commit('loading');
      this.$http.post(`/api/slip/cctr`, params)
          .then(res => res.data)
          .then(data => {
            if(data.length === 1) {
              const { deptCd, deptNm, attribute2 } = data[0];
              this.setGridDataValue(idx, 'deptCd', deptCd);
              this.setGridDataValue(idx, 'deptNm', deptNm);
              this.setGridDataValue(idx, 'deptType', attribute2);
            } else {
              cctrPop();
            }
          })
          .finally(() => {
            this.$store.commit('finish');
          });
    },
    /**
     * * 귀속부서 팝업
     * */
    cctrPop() {
      const self = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);

      const {personId, postingDt } = this.$refs.hd.value;

      if(!personId) {
        this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
        return false;
      }
      if(!postingDt) {
        this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
        return false;
      }

      this.$modal.open({
        component: CctrModal,
        parent: this,
        props: {
          personId : personId,
          postingDate: postingDt,
          schTxt: rowNode.data.deptNm
        },
        width: 700,
        events: {
          close(object) {
            self.setGridDataValue(self.rowId, 'deptCd', object.deptCd);
            self.setGridDataValue(self.rowId, 'deptNm', object.deptNm);
            self.setGridDataValue(self.rowId, 'deptType', object.attribute2);
          }
        },
      });
    },
    /**
     * * 계정과목 엔터 팝업
     * @param {*} idx
     * @param {*} acctNm
     */
    acctEnterPop(idx, acctNm) {
      const slipTypeCd = this.$refs.hd.value.slipTypeCd;
      const rowNode = this.gridApi.getRowNode(idx);

      if(!acctNm) {
        this.initialize(`initAcct`, idx);
        return false;
      }

      const trxTypeCd = slipTypeCd === 'TRIP' ? rowNode?.data?.subTrxType : this.$refs.hd.value.trxTypeCode; //거래유형 (필수)
      const acctModule = this.$refs.hd.value.ttypeInterfaceModule; // 거래유형에서? 매출, 매입 유형 (필수)
      const deptType = rowNode?.data?.deptType || ''; //귀속부서유형 (제조: M, 판관: S, 공통: 그 외(C?) (선택)
      const attribute1 = rowNode?.data?.c_type || '';
      const attribute3 = 'grid';

      const acctPop = (idx, acctNm) => {
        const self = this;

        if(slipTypeCd === 'TRIP') {
          if(!rowNode?.data?.subTrxType) {
            this.initialize(`initAcct`, idx);
            this.$message({ type: `warning`, message: `상세내역 ${idx + 1} 번째 유형을 선택하여 주세요.` });
            return false;
          }
        } else {
          if(!trxTypeCd) {
            this.$message({ type: `warning`, message: `거래유형을 선택하여 주세요.` });
            return false;
          }
        }


        this.$modal.open({
          component: AccountModal,
          parent: this,
          props: {
            trxTypeCd,
            acctModule,
            deptType,
            schTxt: rowNode?.data?.acctNm || acctNm,
            attribute1,
            attribute3
          },
          width: 1000,
          events: {
            close(object) {
              const rowNode = self.gridApi.getRowNode(idx);
              const rowData = rowNode.data;
              rowNode.setDataValue(`acctCd`, object.acctCd);
              self.setGridDataValue(idx, 'acctCd', object.acctCd);
              self.setGridDataValue(idx, 'acctNm', object.acctNm);
              self.setGridDataValue(idx, 'acctDffCnt', object.dffCnt);
              self.setGridDataValue(idx, 'acctRequiredFlagCnt', object.requiredFlagCnt);
              self.setGridDataValue(idx, 'drCr', object.drCr);
              self.setGridDataValue(idx, 'attributeCategory', ''); //초기값세팅
              if(!object.dffCnt) {
                self.setGridDataValue(idx, 'attributeCategoryName', '없음');
              } else {
                self.setGridDataValue(idx, 'attributeCategoryName', '입력하세요');
              }

              self.setGridDataValue(idx, 'assetsTrackingFlag', object.assetsTrackingFlag);

              // 계정과목 변경시, 관리항목 초기화 로직 추가
              rowData.attribute1 = '';
              rowData.attribute2 = '';
              rowData.attribute3 = '';
              rowData.attribute4 = '';
              rowData.attribute5 = '';
              rowData.attribute6 = '';
              rowData.attribute1Code = '';
              rowData.attribute2Code = '';
              rowData.attribute3Code = '';
              rowData.attribute4Code = '';
              rowData.attribute5Code = '';
              rowData.attribute6Code = '';

            }
          },
        });
      };

      const params = { trxTypeCd, acctModule, deptType, acctCd: '', acctNm, attribute1, attribute3 };
      this.$store.commit('loading');
      this.$http.post(`/api/account/slip/list`, params)
          .then(res => res.data)
          .then(data => {
            if(data.length === 1) {
              const { acctCd, acctNm, dffCnt, requiredFlagCnt, drCr, assetsTrackingFlag } = data[0];
              const rowNode = this.gridApi.getRowNode(idx);
              rowNode.setDataValue(`acctCd`, acctCd);
              this.setGridDataValue(idx, 'acctCd', acctCd);
              this.setGridDataValue(idx, 'acctNm', acctNm);
              this.setGridDataValue(idx, 'acctDffCnt', dffCnt);
              this.setGridDataValue(idx, 'acctRequiredFlagCnt', requiredFlagCnt);
              this.setGridDataValue(idx, 'drCr', drCr);
              this.setGridDataValue(idx, 'attributeCategory', ''); //초기값세팅
              if(!dffCnt) {
                this.setGridDataValue(idx, 'attributeCategoryName', '없음');
              } else {
                this.setGridDataValue(idx, 'attributeCategoryName', '입력하세요');
              }
              this.setGridDataValue(idx, 'assetsTrackingFlag', assetsTrackingFlag);
            } else {
              acctPop(idx, acctNm)
            }
          })
          .finally(() => {
            this.$store.commit('finish');
          });
    },
    /**
     * * 계정과목 팝업
     */
    acctPop() {
      const self = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);
      const slipTypeCd = this.$refs.hd.value.slipTypeCd;

      const trxTypeCd = slipTypeCd === 'TRIP' ? rowNode?.data?.subTrxType : this.$refs.hd.value.trxTypeCode; //거래유형 (필수)
      const acctModule = this.$refs.hd.value.ttypeInterfaceModule; // 거래유형에서? 매출, 매입 유형 (필수)
      const deptType = rowNode?.data?.deptType || ''; //귀속부서유형 (제조: M, 판관: S, 공통: 그 외(C?) (선택)
      const attribute1 = rowNode?.data?.c_type || '';
      const attribute3 = 'grid';

      if(slipTypeCd === 'TRIP') {
        if(!rowNode?.data?.subTrxType) {
          this.initialize(`initAcct`, this.rowId);
          this.$message({ type: `warning`, message: `상세내역 ${this.rowId + 1} 번째 유형을 선택하여 주세요.` });
          return false;
        }
      } else {
        if(!trxTypeCd) {
          this.$message({ type: `warning`, message: `거래유형을 선택하여 주세요.` });
          return false;
        }
      }

      this.$modal.open({
        component: AccountModal,
        parent: this,
        props: {
          trxTypeCd,
          acctModule,
          deptType,
          attribute1,
          attribute3,
          schTxt: rowNode?.data?.acctNm
        },
        width: 1000,
        events: {
          close(object) {
            const {acctCd, acctNm, dffCnt, requiredFlagCnt, drCr, assetsTrackingFlag } = object;
            const rowNode = self.gridApi.getRowNode(self.rowId);
            const rowData = rowNode.data;
            rowNode.setDataValue(`acctCd`, acctCd);
            self.setGridDataValue(self.rowId, 'acctCd', acctCd);
            self.setGridDataValue(self.rowId, 'acctNm', acctNm);
            self.setGridDataValue(self.rowId, 'acctDffCnt', dffCnt);
            self.setGridDataValue(self.rowId, 'acctRequiredFlagCnt', requiredFlagCnt);
            self.setGridDataValue(self.rowId, 'drCr', drCr);
            self.setGridDataValue(self.rowId, 'attributeCategory', ''); //초기값세팅
            if(!dffCnt) {
              self.setGridDataValue(self.rowId, 'attributeCategoryName', '없음');
            } else {
              self.setGridDataValue(self.rowId, 'attributeCategoryName', '입력하세요');
            }
            self.setGridDataValue(self.rowId, 'assetsTrackingFlag', assetsTrackingFlag);

            // 계정과목 변경시, 관리항목 초기화 로직 추가
            rowData.attribute1 = '';
            rowData.attribute2 = '';
            rowData.attribute3 = '';
            rowData.attribute4 = '';
            rowData.attribute5 = '';
            rowData.attribute6 = '';
            rowData.attribute1Code = '';
            rowData.attribute2Code = '';
            rowData.attribute3Code = '';
            rowData.attribute4Code = '';
            rowData.attribute5Code = '';
            rowData.attribute6Code = '';

          }
        },
      });

    },
    /**
     * * 프로젝트 엔터 팝업
     * @param {*} idx
     * @param {*} projectNm
     */
    projectEnterPop(idx, projectNm) {
      if(!projectNm) {
        this.initialize(`initProject`, idx)
        this.initialize(`initTask`, idx);
        this.initialize(`initProduct`, idx).then(_ => {
          this.$message({ type: '초기화', message: '초기화 되었습니다.' });
        });
        this.gridOptions.api.refreshCells();

        return false;
      }
      const self = this;
      const projectPop = (idx, projectNm) => {
        const rowNode = this.gridApi.getRowNode(idx);

        const { personId, postingDt } = this.$refs.hd.value;

        if(!personId) {
          this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
          return false;
        }
        if(!postingDt) {
          this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
          return false;
        }

        this.$modal.open({
          component: PjtModal,
          parent: this,
          props: {
            personId : personId,
            postingDate: postingDt,
            schTxt: rowNode?.data?.pjtNm || projectNm
          },
          width: 800,
          events: {
            close(object) {
              const { projectCd, projectNm, projectId } = object;
              self.setGridDataValue(idx, 'pjtCd', projectCd);
              self.setGridDataValue(idx, 'pjtNm', projectNm);
              self.setGridDataValue(idx, 'projectId', projectId);
            }
          },
        });
      };
      const { personId, postingDt } = this.$refs.hd.value;
      if(!personId) {
        this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
        return false;
      }
      if(!postingDt) {
        this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
        return false;
      }
      const params = {
        personId,
        postingDate: postingDt,
        projectNm
      }
      this.$store.commit('loading');
      this.$http.post(`/api/project/slip/list`, params)
          .then(res => res.data)
          .then(data => {
            if(data.length === 1) {
              const { projectCd, projectNm, projectId } = data[0];
              this.setGridDataValue(idx, 'pjtCd', projectCd);
              this.setGridDataValue(idx, 'pjtNm', projectNm);
              this.setGridDataValue(idx, 'projectId', projectId);
            } else {
              projectPop(idx, projectNm)
            }
          })
          .finally(() => {
            this.initialize(`initTask`, idx);
            this.initialize(`initProduct`, idx)

            const rowNode = this.gridApi.getRowNode(idx);

            const self = this;

            this.$modal.open({
              component: TaskModal,
              parent: this,
              props: {
                projectId: rowNode?.data?.projectId
              },
              width: 800,
              events: {
                close(object) {
                  const { taskNo, taskNm, taskId, taskItemGroup } = object;
                  const reloader = () => {
                    return new Promise((resolve, reject) =>{
                      self.setGridDataValue(idx, 'taskNo', taskNo);
                      self.setGridDataValue(idx, 'taskNm', taskNm);
                      self.setGridDataValue(idx, 'taskId', taskId);
                      self.setGridDataValue(idx, 'taskItemGroup', taskItemGroup);
                      resolve();
                    });
                  };
                  reloader().then(() => {
                    const rowNode = self.gridApi.getRowNode(idx);
                    const { taskItemGroup } = rowNode?.data;
                    const { personId, postingDt } = self.$refs.hd.value;

                    if(!personId) {
                      self.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
                      return false;
                    }
                    if(!postingDt) {
                      self.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
                      return false;
                    }

                    self.$modal.open({
                      component: ProductModal,
                      parent: self,
                      props: {
                        personId : personId,
                        postingDate: postingDt,
                        taskItemGroup
                      },
                      width: 800,
                      events: {
                        close(object) {
                          const { itemGroupCd, itemGroupNm } = object;

                          self.setGridDataValue(idx, 'productCd', itemGroupCd);
                          self.setGridDataValue(idx, 'productNm', itemGroupNm);
                        }
                      },
                    });
                  });
                }
              },
            });
            this.$store.commit('finish');
          });
    },
    /**
     * * 프로젝트 팝업
     */
    projectPop() {
      const self = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);

      const { personId, postingDt } = this.$refs.hd.value;

      if(!personId) {
        this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
        return false;
      }
      if(!postingDt) {
        this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
        return false;
      }

      this.$modal.open({
        component: PjtModal,
        parent: this,
        props: {
          personId : personId,
          postingDate: postingDt,
          schTxt: rowNode?.data?.pjtNm
        },
        width: 800,
        events: {
          close(object) {
            const { projectCd, projectNm, projectId } = object;
            const reloader = () => {
              return new Promise((resolve, reject) =>{
                self.setGridDataValue(self.rowId, 'pjtCd', projectCd);
                self.setGridDataValue(self.rowId, 'pjtNm', projectNm);
                self.setGridDataValue(self.rowId, 'projectId', projectId);
                resolve();
              });
            };
            reloader().then(() => {
              self.taskPop();
            });


          }
        },
      });
    },
    /**
     * * 타스크 엔터키 팝업
     * @param {*} idx
     * @param {*} trAcctNm
     */
    taskEnterPop(idx, taskNm) {
      if(!taskNm) {
        this.initialize(`initTask`, idx);
        this.initialize(`initProduct`, idx).then(_ => {
          this.$message({ type: '초기화', message: '초기화 되었습니다.' });
        });
        this.gridOptions.api.refreshCells();
        return false;
      }
      const rowNode = this.gridApi.getRowNode(idx);

      const taskPop = (idx, taskNm) => {

        const self = this;

        this.$modal.open({
          component: TaskModal,
          parent: this,
          props: {
            projectId: rowNode?.data?.projectId,
            schTxt: rowNode?.data?.taskNm || taskNm
          },
          width: 800,
          events: {
            close(object) {
              const { taskNo, taskNm, taskId, taskItemGroup } = object;

              self.setGridDataValue(idx, 'taskNo', taskNo);
              self.setGridDataValue(idx, 'taskNm', taskNm);
              self.setGridDataValue(idx, 'taskId', taskId);
              self.setGridDataValue(idx, 'taskItemGroup', taskItemGroup);

            }
          },
        });
      };
      this.$store.commit('loading');
      const params = {
        projectId: rowNode?.data?.projectId,
        taskNm: rowNode?.data?.taskNm,
      };
      this.$http.post(`/api/task/slip/list`, params)
          .then(res => res.data)
          .then(data => {
            if(data.length === 1) {
              const {taskNo, taskNm, taskId, taskItemGroup} = data[0];
              this.setGridDataValue(idx, 'taskNo', taskNo);
              this.setGridDataValue(idx, 'taskNm', taskNm);
              this.setGridDataValue(idx, 'taskId', taskId);
              this.setGridDataValue(idx, 'taskItemGroup', taskItemGroup);
            } else {
              taskPop(idx, taskNm)
            }
          })
          .finally(() => {
            this.$store.commit('finish');
            this.productPop()
          });

    },
    /**
     * * 타스크 팝업
     */
    taskPop() {
      const rowNode = this.gridApi.getRowNode(this.rowId);

      const self = this;

      this.$modal.open({
        component: TaskModal,
        parent: this,
        props: {
          projectId: rowNode?.data?.projectId
          // schTxt: rowNode?.data?.trAcctNm
        },
        width: 800,
        events: {
          close(object) {
            const { taskNo, taskNm, taskId, taskItemGroup } = object;
            const reloader = () => {
              return new Promise((resolve, reject) =>{
                self.setGridDataValue(self.rowId, 'taskNo', taskNo);
                self.setGridDataValue(self.rowId, 'taskNm', taskNm);
                self.setGridDataValue(self.rowId, 'taskId', taskId);
                self.setGridDataValue(self.rowId, 'taskItemGroup', taskItemGroup);
                resolve();
              });
            };
            reloader().then(() => {
              self.productPop();
            });
          }
        },
      });
    },
    /**
     * * 제품군 엔터키 팝업
     * @param {*} idx
     * @param {*} itemGroupNm
     */
    productEnterPop(idx, itemGroupNm) {
      if(!itemGroupNm) {
        this.initialize(`initProduct`, idx);
        return false;
      }
      const productPop = (idx, itemGroupNm) => {
        const rowNode = this.gridApi.getRowNode(idx);

        const { personId, postingDt } = this.$refs.hd.value;

        if(!personId) {
          this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
          return false;
        }
        if(!postingDt) {
          this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
          return false;
        }

        const self = this;
        const taskItemGroup = rowNode?.data?.taskItemGroup; //태스크 제품군
        this.$modal.open({
          component: ProductModal,
          parent: this,
          props: {
            personId : personId,
            postingDate: postingDt,
            taskItemGroup,
            schTxt: rowNode?.data?.productNm || itemGroupNm
          },
          width: 800,
          events: {
            close(object) {
              const { itemGroupCd, itemGroupNm } = object;

              self.setGridDataValue(idx, 'productCd', itemGroupCd);
              self.setGridDataValue(idx, 'productNm', itemGroupNm);
            }
          },
        });
      };
      const { personId, postingDt } = this.$refs.hd.value;
      const pRowNode = this.gridApi.getRowNode(idx);
      const taskItemGroup  = pRowNode?.data?.taskItemGroup;
      if(!personId) {
        this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
        return false;
      }
      if(!postingDt) {
        this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
        return false;
      }

      const params = {
        personId,
        postingDate: postingDt,
        taskItemGroup,
        itemGroupNm
      }
      this.$store.commit('loading');
      this.$http.post(`/api/item/slip/list`, params)
          .then(res => res.data)
          .then(data => {
            if(data.length == 1) {
              const { itemGroupCd, itemGroupNm } = data[0];

              this.setGridDataValue(idx, 'productCd', itemGroupCd);
              this.setGridDataValue(idx, 'productNm', itemGroupNm);
            } else {
              productPop(idx, itemGroupNm)
            }
          })
          .finally(() => {
            this.$store.commit('finish');
          });
    },
    /**
     * * 제품군 팝업
     */
    productPop() {
      const rowNode = this.gridApi.getRowNode(this.rowId);
      const taskItemGroup = rowNode?.data?.taskItemGroup;
      const { personId, postingDt } = this.$refs.hd.value;

      if(!personId) {
        this.$message({ type: `warning`, message: `직원을 선택하여 주세요.` });
        return false;
      }
      if(!postingDt) {
        this.$message({ type: `warning`, message: `회계일자를 선택하여 주세요.` });
        return false;
      }

      const self = this;

      this.$modal.open({
        component: ProductModal,
        parent: this,
        props: {
          personId : personId,
          postingDate: postingDt,
          taskItemGroup
          // schTxt: rowNode?.data?.productNm
        },
        width: 800,
        events: {
          close(object) {
            const { itemGroupCd, itemGroupNm } = object;

            self.setGridDataValue(self.rowId, 'productCd', itemGroupCd);
            self.setGridDataValue(self.rowId, 'productNm', itemGroupNm);
          }
        },
      });
    },
    /**
     * * 관리항목 팝업
     */
    attributeCategoryPop() {
      const self = this;
      const rowNode = this.gridApi.getRowNode(this.rowId);

      const {editabledState} = this.$refs.hd;

      if(!rowNode.data.acctCd) {
        this.$message({
          type: `warning`,
          message: `계정과목을 선택하여 주세요.`
        });
        return false;
      }

      this.$modal.open({
        component: SlipMngItemPop,
        parent: this,
        props: {
          applicationShortCd : this.$refs.hd.value.ttypeInterfaceModule,
          acctCd: rowNode.data.acctCd,
          data: {
            attribute1: rowNode.data?.attribute1,
            attribute2: rowNode.data?.attribute2,
            attribute3: rowNode.data?.attribute3,
            attribute4: rowNode.data?.attribute4,
            attribute5: rowNode.data?.attribute5,
            attribute6: rowNode.data?.attribute6,
            attribute1Code: rowNode.data?.attribute1Code,
            attribute2Code: rowNode.data?.attribute2Code,
            attribute3Code: rowNode.data?.attribute3Code,
            attribute4Code: rowNode.data?.attribute4Code,
            attribute5Code: rowNode.data?.attribute5Code,
            attribute6Code: rowNode.data?.attribute6Code,
          },
          //TODO: 상태 결과가 나오면 아래 readonly 적용
          readonly: editabledState
        },
        width: 1200,
        events: {
          close(object) {
            /**
             * * 팝업에서 클로징되어진 오브젝트에서의 입력된 값의 카운트
             * @param {*} obj
             */
            const returnValueCount = (obj) => {
              let count = 0;
              const keys = Object.keys(obj);
              for (const key of keys) {
                if(obj[key]) {
                  count++;
                }
              }
              return count;
            }
            const closeObjCnt = returnValueCount(object);
            const acctRequiredFlagCnt = parseInt(rowNode.data.acctRequiredFlagCnt);
            if(!!object && closeObjCnt >= acctRequiredFlagCnt) {
              //pass
              const keys = Object.keys(object);
              for (const key of keys) {
                if(key === 'acctCd' || key === 'acctNm') {
                  self.setGridDataValue(self.rowId, 'attributeCategory', rowNode.data.acctCd);
                  self.setGridDataValue(self.rowId, 'attributeCategoryName', rowNode.data.acctNm);
                } else {
                  if(key.indexOf('Code') > -1) {
                    self.setGridDataValue(self.rowId, String(key).toLowerCase().replace('c', 'C'), object[key]);
                  } else {
                    self.setGridDataValue(self.rowId, String(key).toLowerCase(), object[key]);
                  }
                }
              }
              self.$message({ type: `success`, message: `입력되었습니다.` });
            } else {
              self.setGridDataValue(self.rowId, 'attributeCategory', '');
              self.setGridDataValue(self.rowId, 'attributeCategoryName', '없음');
              self.$message({ type: `warning`, message: `관리항목 필수 입력 갯수와 일치하지 않습니다.` });
            }
          }
        },
      });
    },
    /**
     * * 세금코드 엔터 팝업
     */
    vatEnterPop(idx, val) {
      if(!val) {

        this.initialize(`initVat`, idx);
        return false;
      }
      const vatPop = (idx, taxCd) => {
        const rowNode = this.gridApi.getRowNode(idx);

        const self = this;
        const {taxEvidenceType, slipTypeCd} = this.$refs.hd.value;

        this.$modal.open({
          component: VatModal,
          parent: this,
          props: {
            taxEvidenceType,
            returnType: 'grid',
            taxRateCd: taxCd
          },
          width: 800,
          events: {
            close(object) {
              const { taxStatusCode, percentageRate, taxRateId, taxRateCode, taxAcctCode } = object;

              self.setGridDataValue(idx, 'taxId', taxRateId);
              self.setGridDataValue(idx, 'taxCd', taxRateCode);
              self.setGridDataValue(idx, 'taxNm', taxRateCode);
              self.setGridDataValue(idx, 'taxAcctCode', taxAcctCode);
              self.setGridDataValue(idx, 'taxAcctName', taxRateCode);
              self.setGridDataValue(idx, 'percentageRate', percentageRate);
              if(taxRateCode == '매입불공제') {
                self.setGridDataValue(idx, 'taxFlag', 'N');
                self.setGridDataValue(idx, 'lineAttribute1', 'Y');
              } else if(taxRateCode != '' && taxRateCode != '매입불공제') {
                self.setGridDataValue(idx, 'taxFlag', 'Y');
                self.setGridDataValue(idx, 'lineAttribute1', '');
              } else {
                self.setGridDataValue(idx, 'lineAttribute1', '');
              }
            }
          },
        });
      };


      const {taxEvidenceType, slipTypeCd} = this.$refs.hd.value;

      const params = {
        searchCd: val,
        searchNm: '',
        taxRateCd: ''
      }
      this.$http.post(`/api/surTaxCode/list/${taxEvidenceType}/grid`, params)
          .then(res => res.data)
          .then(data => {
            if(!!data && data.length === 1) {
              const { taxStatusCode, percentageRate, taxRateId, taxRateCode, taxAcctCode } = data[0];

              this.setGridDataValue(idx, 'taxId', taxRateId);
              this.setGridDataValue(idx, 'taxCd', taxRateCode);
              this.setGridDataValue(idx, 'taxNm', taxRateCode);
              this.setGridDataValue(idx, 'taxAcctCode', taxAcctCode);
              this.setGridDataValue(idx, 'taxAcctName', taxRateCode);
              this.setGridDataValue(idx, 'percentageRate', percentageRate);
              if(taxRateCode == '매입불공제') {
                this.setGridDataValue(idx, 'taxFlag', 'N');
                this.setGridDataValue(idx, 'lineAttribute1', 'Y');
              } else if(taxRateCode != '' && taxRateCode != '매입불공제') {
                this.setGridDataValue(idx, 'taxFlag', 'Y');
                this.setGridDataValue(idx, 'lineAttribute1', '');
              } else {
                this.setGridDataValue(idx, 'lineAttribute1', '');
              }
            } else {
              vatPop(idx, val)
            }
          })
          .finally(() => {
          });

    },
    /**
     * * 세금코드 팝업
     */
    vatPop() {
      const rowNode = this.gridApi.getRowNode(this.rowId);
      const { taxCd } = rowNode?.data;

      const self = this;
      const {taxEvidenceType, slipTypeCd} = this.$refs.hd.value;

      this.$modal.open({
        component: VatModal,
        parent: this,
        props: {
          taxEvidenceType,
          returnType: 'grid',
          taxRateCd: taxCd
        },
        width: 800,
        events: {
          close(object) {
            const { taxStatusCode, percentageRate, taxRateId, taxRateCode, taxAcctCode } = object;

            self.setGridDataValue(self.rowId, 'taxId', taxRateId);
            self.setGridDataValue(self.rowId, 'taxCd', taxRateCode);
            self.setGridDataValue(self.rowId, 'taxNm', taxRateCode);
            self.setGridDataValue(self.rowId, 'taxAcctCode', taxAcctCode);
            self.setGridDataValue(self.rowId, 'taxAcctName', taxRateCode);
            self.setGridDataValue(self.rowId, 'percentageRate', percentageRate);
            if(taxRateCode == '매입불공제') {
              self.setGridDataValue(self.rowId, 'taxFlag', 'N');
              self.setGridDataValue(self.rowId, 'lineAttribute1', 'Y');
            } else if(taxRateCode != '' && taxRateCode != '매입불공제') {
              self.setGridDataValue(self.rowId, 'taxFlag', 'Y');
              self.setGridDataValue(self.rowId, 'lineAttribute1', '');
            } else {
              self.setGridDataValue(self.rowId, 'lineAttribute1', '');
            }
          }
        },
      });
    },
    /**
     * * 유형코드 엔터 팝업
     */
    subTrxTypeEnterPop(idx, val) {
      if(!val) {
        this.initialize(`initSubTrxType`, idx);
        return false;
      }
      const {trxTypeCode} = this.$refs.hd.value;
      const rowNode = this.gridApi.getRowNode(idx);
      const { c_type } = rowNode?.data;

      const subTrxTypePop = (idx) => {
        const self = this;

        this.$modal.open({
          component: SubTrxTypeModal,
          parent: this,
          props: {
            trxTypeCd: trxTypeCode,
            attribute1: c_type
          },
          width: 800,
          events: {
            close(object) {
              const { childTrxTypeCd, childTrxTypeNm } = object;
              self.setGridDataValue(idx, 'subTrxType', childTrxTypeCd);
              self.setGridDataValue(idx, 'subTrxTypeName', childTrxTypeNm);

            }
          },
        });
      };


      const params = {
        trxTypeCd: trxTypeCode,  //거래유형
        attribute1: c_type //법인카드 or 현금
      };
      this.$http.post(`/api/account/slip/subList`, params)
          .then(res => res.data)
          .then(data => {
            if(!!data && data.length === 1) {
              const { childTrxTypeCd, childTrxTypeNm } = data[0];

              this.setGridDataValue(idx, 'subTrxType', childTrxTypeCd);
              this.setGridDataValue(idx, 'subTrxTypeName', childTrxTypeNm);
            } else {
              subTrxTypePop(idx, val)
            }
          })
          .finally(() => {
          });
    },
    /**
     * * 유형코드 팝업
     */
    subTrxTypePop() {
      const rowNode = this.gridApi.getRowNode(this.rowId);
      const {trxTypeCode} = this.$refs.hd.value;
      const { c_type } = rowNode?.data;
      const self = this;
      this.$modal.open({
        component: SubTrxTypeModal,
        parent: this,
        props: {
          trxTypeCd: trxTypeCode,
          attribute1: c_type
        },
        width: 800,
        events: {
          close(object) {
            const { childTrxTypeCd, childTrxTypeNm } = object;

            self.setGridDataValue(self.rowId, 'subTrxType', childTrxTypeCd);
            self.setGridDataValue(self.rowId, 'subTrxTypeName', childTrxTypeNm);
          }
        },
      });
    },
    /**
     * * 그리드 셀 데이터 변경
     * @param {*} i (index)
     * @param {*} k (key)
     * @param {*} v (value)
     */
    setGridDataValue(i, k, v) {
      const rowNode = this.gridApi.getRowNode(i);
      // rowNode.setDataValue(`${k}`, v);
      this.rowData[i][k] = v;
      this.gridOptions.api.refreshCells();
    },
    /**
     * * 그리드 합계 금액 계산
     */
    amountCalcurate() {
      this.columnDefs = this.columnDefs.map(x => {
        if(x.field === 'vatAmount') {
          x.editable = this.data.taxbill_amt_modify;
        }
        return x;
      })
      const { curCd, excRt, amountToApply, lineAttribute1, slipTypeCd } = this.$refs.hd.value;
      const { options } = this.$refs.hd;
      //통화 자리수 확인
      const AMT_LENGTH = options['FRGN_CUR_CD'].filter(f => f.currencyCode === curCd )[0]?.precision || 0;

      if(['AWT', 'ETCAWT'].includes(slipTypeCd)) {
        const awtData = this.$refs.hd.value[String(slipTypeCd).toLowerCase()];
        if(slipTypeCd === 'AWT') {
          //원천세 헤더 세액 계산
          awtData.forEach((x, i) => {
            const subRowNode = this.$refs.hd.$refs.subHD.gridApi.getRowNode(i);
            subRowNode.setDataValue(`preTaxAmt`, this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
          });
          this.$refs.hd.$refs.subHD.gridOptions.api.refreshCells();
          //상세내역 그리드 세액 및 합계 세팅
          const tax_amount = 0;

          const rate_total = awtData.map(x => x.taxRate).reduce((prev, next) => {return prev + next}, 0); //총 세율
          // const minus_rate_total = awtData.map(x => x.taxRate).reduce((prev, next) => {return prev + next}, 0);

          this.rowData.forEach((x, idx) => {
            // const afterTotTaxAmt = Math.round(((x.supplyAmount * (rate_total / 100)) / 10) * 10);
            // const minus_afterTotTaxAmt = Math.round(((x.supplyAmount * (minus_rate_total / 100)) / 10) * 10);

            // this.setGridDataValue(idx, 'afterTaxAmount', afterTotTaxAmt < 1000 ? (x.supplyAmount - minus_afterTotTaxAmt) : (x.supplyAmount - afterTotTaxAmt));
            const afterTotTaxAmt = Math.floor((x.supplyAmount * (rate_total / 100)) / 10) * 10;
            this.setGridDataValue(idx, 'afterTaxAmount', x.supplyAmount - afterTotTaxAmt);
            this.setGridDataValue(idx, 'usedAmt', this.$numeral(x.supplyAmount).value());
            this.setGridDataValue(idx, 'vatAmount', Math.round(this.$numeral(tax_amount).value()));
            this.setGridDataValue(idx, 'accountedVatAmount', Math.round(this.$numeral(tax_amount).value()));
            this.setGridDataValue(idx, 'accountedSupplyAmount', Math.round(this.$numeral(x.supplyAmount * 1 * excRt).value()));
          });
        }
        if(slipTypeCd === 'ETCAWT') {
          //잡급비 헤더 세액 계산
          let afterTotTaxAmt = 0;
          awtData.forEach((x, i) => {
            const subRowNode = this.$refs.hd.$refs.subHD.gridApi.getRowNode(i);

            if(subRowNode.data.awtAccountName.indexOf('고용보험료') > -1) {
              subRowNode.setDataValue(`preTaxAmt`, this.rowData.filter(f => f.withholdingTaxCode && f.withholdingTaxCode === '2').map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
              // subRowNode.setDataValue(`preTaxAmt`, this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
              afterTotTaxAmt += parseInt(parseInt(this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0) * (subRowNode.data.taxRate / 100)) / 10) * 10;
            }
            else if(subRowNode.data.awtAccountName.indexOf('소득세') > -1) {
              subRowNode.setDataValue(`preTaxAmt`, this.rowData.filter(f => f.withholdingTaxCode && f.withholdingTaxCode && !['2', '3', '4'].includes(f.withholdingTaxCode)).map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
              afterTotTaxAmt += parseInt(parseInt(this.rowData.filter(f => f.withholdingTaxCode && f.withholdingTaxCode && !['2', '3', '4'].includes(f.withholdingTaxCode)).map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0) * (subRowNode.data.taxRate / 100)) / 10) * 10;
            }
            else if(subRowNode.data.awtAccountName.indexOf('주민세') > -1) {
              subRowNode.setDataValue(`preTaxAmt`, this.rowData.filter(f => f.withholdingTaxCode && f.withholdingTaxCode && !['2', '3', '4'].includes(f.withholdingTaxCode)).map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
              afterTotTaxAmt += parseInt(parseInt(this.rowData.filter(f => f.withholdingTaxCode && f.withholdingTaxCode && !['2', '3', '4'].includes(f.withholdingTaxCode)).map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0) * (subRowNode.data.taxRate / 100)) / 10) * 10;
            }
            else if(subRowNode.data.awtAccountName.indexOf('국민연금') > -1) {
              subRowNode.setDataValue(`preTaxAmt`, this.rowData.filter(f => f.withholdingTaxCode && f.withholdingTaxCode === '3').map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
              // subRowNode.setDataValue(`preTaxAmt`, this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
              afterTotTaxAmt += parseInt(parseInt(this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0) * (subRowNode.data.taxRate / 100)) / 10) * 10;
            }
            else if(subRowNode.data.awtAccountName.indexOf('의료보험료') > -1) {
              subRowNode.setDataValue(`preTaxAmt`, this.rowData.filter(f => f.withholdingTaxCode && f.withholdingTaxCode === '4').map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
              // subRowNode.setDataValue(`preTaxAmt`, this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0));
              afterTotTaxAmt += parseInt(parseInt(this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {return prev + next}, 0) * (subRowNode.data.taxRate / 100)) / 10) * 10;
            }
          });
          //상세내역 그리드 세액 및 합계 세팅
          const tax_amount = 0;

          this.rowData.forEach((x, idx) => {
            const {getRowData} = this.$refs.hd.$refs.subHD.$refs[`${String(slipTypeCd).toLocaleLowerCase()}-grid`];

            let afterTaxAmount = '';
            if(x.withholdingTaxCode === '2'){
              afterTaxAmount = getRowData().filter(f => f.awtAccountName.indexOf('고용보험료') > -1).map(x => this.$numeral(x.preTaxAmt).value()).reduce((prev, next) => {return prev + next}, 0) - afterTotTaxAmt
            }else if(x.withholdingTaxCode === '3'){
              afterTaxAmount = getRowData().filter(f => f.awtAccountName.indexOf('국민연금') > -1).map(x => this.$numeral(x.preTaxAmt).value()).reduce((prev, next) => {return prev + next}, 0) - afterTotTaxAmt
            }else if(x.withholdingTaxCode === '4'){
              afterTaxAmount = getRowData().filter(f => f.awtAccountName.indexOf('의료보험료') > -1).map(x => this.$numeral(x.preTaxAmt).value()).reduce((prev, next) => {return prev + next}, 0) - afterTotTaxAmt
            }else{
              afterTaxAmount = x.supplyAmount
            }

            // this.setGridDataValue(idx, 'afterTaxAmount', x.withholdingTaxCode === '2' ? getRowData().filter(f => f.awtAccountName.indexOf('고용보험료') > -1).map(x => this.$numeral(x.preTaxAmt).value()).reduce((prev, next) => {return prev + next}, 0) - afterTotTaxAmt : x.supplyAmount);
            // this.setGridDataValue(idx, 'afterTaxAmount', ['2', '3', '4'].includes(x.withholdingTaxCode) ? getRowData().filter(f => ['고용보험료', '의료보험료', '국민연금'].some(term => f.awtAccountName.includes(term))).map(x => this.$numeral(x.preTaxAmt).value()).reduce((prev, next) => {return prev + next}, 0) - afterTotTaxAmt : x.supplyAmount);
            this.setGridDataValue(idx, 'afterTaxAmount', afterTaxAmount);
            this.setGridDataValue(idx, 'usedAmt', this.$numeral(x.supplyAmount).value());
            this.setGridDataValue(idx, 'vatAmount', Math.round(this.$numeral(tax_amount).value()));
            this.setGridDataValue(idx, 'accountedVatAmount', Math.round(this.$numeral(tax_amount).value()));
            this.setGridDataValue(idx, 'accountedSupplyAmount', Math.round(this.$numeral(x.supplyAmount * 1 * excRt).value()));
          });
        }

      }
      if(slipTypeCd === 'TRAFFIC') {

        this.rowData.forEach((x, idx) => {

          const oilAmt = this.$numeral(x.oilAmt || 0).value();
          const etcAmt1 = this.$numeral(x.etcAmt1 || 0).value();
          const etcAmt2 = this.$numeral(x.etcAmt2 || 0).value();
          const etcAmt3 = this.$numeral(x.etcAmt3 || 0).value();
          const lineAmt = Math.round(oilAmt + etcAmt1 + etcAmt2 + etcAmt3);

          this.setGridDataValue(idx, 'sumAmt', lineAmt);
          this.setGridDataValue(idx, 'supplyAmount', Math.round(this.$numeral(lineAmt || 0).value()));
          this.setGridDataValue(idx, 'vatAmount', 0);
          this.setGridDataValue(idx, 'accountedSupplyAmount', Math.round(this.$numeral(lineAmt || 0).value()));
          this.setGridDataValue(idx, 'accountedVatAmount', 0);
          this.setGridDataValue(idx, 'usedAmt', lineAmt);
        });
        let sumAmt = this.rowData.map(x => x.sumAmt).reduce((prev, next) => {
          return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
        }, 0) ;

        // 한화만 사용하는지....?
        // if(curCd !== 'KRW') sumAmt = this.$numeral(sumAmt.toFixed(AMT_LENGTH)).value();

        this.$refs.hd.value.totAmt = this.$numeral(sumAmt).format(`0,0`);
        this.$refs.hd.value.totAmtKrw = this.$numeral(sumAmt).format(`0,0`);
        this.$refs.hd.value.supplyAmount = this.$numeral(sumAmt).format(`0,0`);
        this.$refs.hd.value.taxAmount = 0;
        this.$refs.hd.value.totalAmount = this.$numeral(sumAmt).format(`0,0`);
        this.$refs.hd.value.realAmt = this.$numeral(sumAmt - this.$numeral(amountToApply || 0).value()).format(`0,0`);

      } else {

        let totSupAmt = this.rowData.reduce((prev, next) => {
          return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next?.supplyAmount || 0).toFixed(AMT_LENGTH)) ) ;
        }, 0) ;
        const totVatAmt = this.rowData.reduce((prev, next) => {
          return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next?.vatAmount || 0).toFixed(AMT_LENGTH)) ) ;
        }, 0) ;


        // 통화가 한화가 아닌경우 통화에 맞게 소수점 반올림
        if(curCd !== 'KRW') totSupAmt = this.$numeral(totSupAmt.toFixed(AMT_LENGTH)).value();

        if(this.data.taxbill_amt_modify || lineAttribute1 === 'V') { //부가세 조정일 때 또는 부가세라면
          // 전표금액(KRW) = 전표금액((공금가액 + 부가세액 : 부가새액 조정 일때 ) * 환율)
          // this.$refs.hd.value.totAmtKrw = this.$numeral(Math.round(this.$numeral((totSupAmt * (excRt * 1000)) / 1000 + totVatAmt).value())).format(`0,0`);
          // 전표금액(KRW) = 전표금액( (공금가액 * 환율) + (부가세액 * 환율) : 부가새액 조정 일때 )
          this.$refs.hd.value.totAmtKrw = this.$numeral(Math.round(this.$numeral((totSupAmt * (excRt * 1000)) / 1000).value()) + Math.round(this.$numeral((totVatAmt * (excRt * 1000)) / 1000).value())).format(`0,0`);
          // 전표금액 = 공급가액 + 부가세액
          this.$refs.hd.value.totAmt = this.$numeral(totSupAmt + totVatAmt).format(numFormats(curCd,AMT_LENGTH));
          // this.$refs.hd.value.totAmtKrw = Math.floor((this.$refs.hd.value.totAmt * this.$refs.hd.value.excRt));
          //실지급금액 = 전표금액((공금가액 + 부가세액 : 부가새액 조정 일때 ) * 환율) - 선급금정산금액
          this.$refs.hd.value.realAmt = this.$numeral(totSupAmt + totVatAmt - this.$numeral(amountToApply || 0).value()).format(numFormats(curCd,AMT_LENGTH));
        } else { // 부가세 조정이 아닐 때
          // 전표금액(KRW) = 전표금액(공금가액 * 환율)
          this.$refs.hd.value.totAmtKrw = this.$numeral(Math.round(this.$numeral((totSupAmt * (excRt * 1000)) / 1000).value())).format(`0,0`);
          // this.$numeral(totSupAmt * excRt);
          // 전표금액 = 공급가액
          this.$refs.hd.value.totAmt = this.$numeral(totSupAmt).format(numFormats(curCd,AMT_LENGTH));
          //실지급금액 = 전표금액((공금가액) * 환율) - 선급금정산금액
          this.$refs.hd.value.realAmt = this.$numeral(totSupAmt - this.$numeral(amountToApply || 0).value()).format(numFormats(curCd,AMT_LENGTH));
        }

        if(lineAttribute1 === 'V') {
          let lineSupAmt = 0;
          let lineVatAmt = 0;
          let lineViewSupAmt = 0;
          let lineViewVatAmt = 0;
          if(slipTypeCd === 'ACARD') { //세금코드가 없어도 공급가액을 넣어야 하는 전표가 있으면 여기 조건에 추가.
            lineSupAmt = this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {
              return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
            }, 0) ;
            lineVatAmt = this.rowData.map(x => this.$numeral(x.vatAmount).value()).reduce((prev, next) => {
              return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
            }, 0) ;
            lineViewSupAmt = lineSupAmt;
            lineViewVatAmt = lineVatAmt;
          } else {
            //세금코드가 있는 것에 대한 부분만 합계를 구한다.
            lineSupAmt = this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {
              return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
            }, 0) ;
            lineVatAmt = this.rowData.map(x => this.$numeral(x.vatAmount).value()).reduce((prev, next) => {
              return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
            }, 0) ;
            lineViewSupAmt = this.rowData.filter(f => f.taxId).map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {
              return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
            }, 0) ;
            lineViewVatAmt = this.rowData.filter(f => f.taxId).map(x => this.$numeral(x.vatAmount).value()).reduce((prev, next) => {
              return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
            }, 0) ;
          }

          this.$refs.hd.value.supplyAmount = this.$numeral(lineSupAmt || 0).value();
          this.$refs.hd.value.taxAmount = Math.round(this.$numeral(lineVatAmt || 0).value()); //실 세금
          this.$refs.hd.value.totalAmount = this.$numeral(this.$refs.hd.value.supplyAmount).value() + this.$numeral(this.$refs.hd.value.taxAmount).value(); // 실 금액 합계

          //this.$refs.hd.value.vSupplyAmount = this.$numeral(lineViewSupAmt || 0).value(); //(View 용)
          //this.$refs.hd.value.vTaxAmount = Math.round(this.$numeral(lineViewVatAmt || 0).value()); //(View 용) 실 세금
          //this.$refs.hd.value.vTotalAmount = this.$numeral(this.$refs.hd.value.vSupplyAmount).value() + this.$numeral(this.$refs.hd.value.vTaxAmount).value(); // //(View 용) 실 금액 합계

          this.$refs.hd.value.vSupplyAmount = Math.round(this.$numeral((lineSupAmt * (excRt * 1000)) / 1000).value());
          this.$refs.hd.value.vTaxAmount = Math.round(this.$numeral((lineViewVatAmt * (excRt * 1000)) / 1000).value());
          this.$refs.hd.value.vTotalAmount = this.$numeral(this.$refs.hd.value.vSupplyAmount).value() + this.$numeral(this.$refs.hd.value.vTaxAmount).value();
        } else {
          const lineSupAmt = this.rowData.map(x => this.$numeral(x.supplyAmount).value()).reduce((prev, next) => {
            return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
          }, 0) ;
          const lineVatAmt = this.rowData.map(x => this.$numeral(x.vatAmount).value()).reduce((prev, next) => {
            return ( parseFloat(parseFloat(prev).toFixed(AMT_LENGTH)) + parseFloat(parseFloat(next || 0).toFixed(AMT_LENGTH)) ) ;
          }, 0) ;

          this.$refs.hd.value.supplyAmount = this.$numeral(lineSupAmt || 0).value();
          this.$refs.hd.value.taxAmount = Math.round(this.$numeral(lineVatAmt || 0).value()); //실 세금
          this.$refs.hd.value.totalAmount = this.$numeral(this.$refs.hd.value.supplyAmount).value() + this.$numeral(this.$refs.hd.value.taxAmount).value(); // 실 금액 합계

          this.$refs.hd.value.vSupplyAmount = this.$refs.hd.value.supplyAmount;
          this.$refs.hd.value.vTaxAmount = this.$refs.hd.value.taxAmount;
          this.$refs.hd.value.vTotalAmount = this.$refs.hd.value.totalAmount;

        }

      }


    },
    /**
     * * 예산조회 모달
     */
    budgtOpenModal() {
      const { postingDt, deptCd, deptNm, personId } = this.$refs.hd.value;

      if(!postingDt) {
        this.$message({ type: 'danger', message: '회계기간을 선택해주세요.' });
        return false;
      }
      if(!deptCd) {
        // 부서코드가 없다는 것은 직원 선택을 안하였거나 부서코드가 없는 비정상 사용자
        this.$message({ type: 'danger', message: '부서코드가 없습니다. 직원을 선택해주세요.' });
        return false;
      }

      this.$modal.open({
        component: BudgtAmountModal,
        props: {
          dept: {
            postingDt,
            deptCd,
            deptNm,
            personId
          }
        },
        parent: this,
        width: 1500,
        events: {
          close(value) {

          }
        }
      })
    },
    /**
     * 법인카드 사용 내역 팝업
     */
    openCopCard(cardType, options = {}) {
      const self = this;
      const {empNo, empNm, deptType, ledgerId, taxEvidenceType, slipHeaderId, trxTypeCode, ttypeInterfaceModule, slipTypeCd} = this.$refs.hd.value;
      if(!empNo) {
        this.$swal({ type: 'warning', text: "직원을 선택하여주세요." })
        return false;
      }

      const params = {
        userId: empNo,
        userNm: empNm,
        taxEvidenceType,
        ledgerId,
        slipHeaderId: slipHeaderId || '',
        trxTypeCode,
        ttypeInterfaceModule,
        slipTypeCd,
        deptType
      }
      this.$modal.open({
        component: CorpCardHstModal,
        props: {
          params,
          copyUseDetails: self.rowData,
          cardType,
          options
        },
        parent: this,
        width: 1200,
        events: {
          close(cards) {
            if(!!cards && cards.length > 0) {
              cards.map(card => {
                const {empNo, percentageRate, taxRateId, taxRateCode, taxAcctCode} = self.$refs.hd.value;
                self.$http.post(`/api/emp/accrualSlip/search/${empNo}`)
                    .then(res => res.data)
                    .then(data => {
                      data.map((x, i) => {
                        x.vatAmount = self.$numeral(x.supplyAmount * (percentageRate / 100)).value()
                        x.projectId = x.pjtId;

                        if(!x.acctDffCnt) {
                          x.attributeCategoryName = '없음';
                        } else {
                          x.attributeCategoryName = '입력하세요';
                        }
                        x.attributeCategory = '';
                        x.accountedVatAmount = 0;
                        x.accountedSupplyAmount = 0;
                        x.deptType = x.attribute2; //
                        x.attribute2 = '';

                        //법인카드 내역 세팅
                        x.taxId = card.taxId || '';
                        x.taxCd = card.taxCode;
                        x.taxNm = card.taxCode;

                        x.orgTaxFlag = card.taxFlag; //법인카드내역에서 찍어진 공제여부(헷갈리지말것.)

                        if(card.taxCode == '매입불공제') {
                          x.taxFlag = 'N';
                          x.lineAttribute1 = 'Y';
                          x.originalTaxFlag = 'N';
                        } else if(card.taxCode && card.taxCode != '매입불공제') {
                          x.taxFlag = 'Y';
                          x.originalTaxFlag = 'Y';
                          x.lineAttribute1 = '';
                        } else {
                          x.taxFlag = '';
                          x.lineAttribute1 = '';
                        }

                        if(x.taxFlag === 'Y') {
                          x.taxAcctCode = taxAcctCode;
                          x.taxAcctName = taxAcctCode;
                        } else {
                          x.taxAcctCode = card.taxAcctCode || '';
                          x.taxAcctName = card.taxAcctCode || '';
                        }

                        x.cardUsedNo = card.usedNo; //카드 key 값
                        x.globalAttribute8 = card.cardNo; //법인카드번호
                        x.globalAttribute12 = card.storeAddr; //가맹점주소
                        x.globalAttribute13 = card.irsNo; //사업자번호
                        x.globalAttribute14 = card.apprNo; //승인번호
                        x.globalAttribute15 = card.storeNm; //상호
                        x.globalAttribute16 = card.mccNm; //업종
                        x.globalAttribute17 = card.taxType; //과세유형
                        x.globalAttribute18 = card.usedDt; //사용일자
                        x.globalAttribute19 = card.buyDt; //매입일자
                        x.globalAttribute20 = card.cardComNm; //카드명


                        x.supplyAmount = self.$numeral(card.originAmt).value() + self.$numeral(card.serviceCharge || 0).value(); //공급가액 + 봉사료
                        x.vatAmount = self.$numeral(card.surtax).value(); //공급가액
                        x.usedAmt = self.$numeral(card.usedAmt).value(); //사용금액(총액)
                        x.accountedSupplyAmount = x.supplyAmount;
                        x.accountedVatAmount = x.vatAmount;

                        x.acctCd = card.acctCd; //계정코드
                        x.acctNm = card.acctNm; //계정명
                        x.acctType = card.acctType; //계정유형
                        x.acctDffCnt = card.acctDffCnt;
                        x.acctRequiredFlagCnt = card.acctRequiredFlagCnt;
                        x.drCr = card.drCr || 'Dr';
                        x.attributeCategory = card.attributeCategory;
                        x.attributeCategoryName = card.attributeCategoryName;
                        x.assetsTrackingFlag = card.assetsTrackingFlag || card.acctCd ? 'Y' : 'N';
                        x.description = card.crdOln; //적요
                        x.c_type = '법인카드';
                        self.rowData.push(x);

                        return x;
                      });
                    })
                    .finally(_ => {
                      self.rowData = self.rowData.sort((a, b) => b.globalAttribute18 - a.globalAttribute18)
                      self.amountCalcurate();
                    });
              });
            }

          }
        }
      });
    },
    /**
     *
     */
    lineVatInit() {

      const data = this.gridApi.getSelectedNodes().map(x => x.data);
      if(!!data && data.length > 0) {
        this.gridApi.forEachNode((node, index) => {
          if(node.selected) {
            this.setGridDataValue(index, 'taxId', '');
            this.setGridDataValue(index, 'taxCd', '');
            this.setGridDataValue(index, 'taxNm', '');
            this.setGridDataValue(index, 'taxAcctCode', '');
            this.setGridDataValue(index, 'taxAcctName', '');
            this.setGridDataValue(index, 'percentageRate', 0);
            this.setGridDataValue(index, 'taxFlag', '');
            this.setGridDataValue(index, 'lineAttribute1', '');
            this.setGridDataValue(index, 'vatAmount', 0);
          }
        });
        this.gridOptions.api.refreshCells();
        this.$message({ type: `success`, message: `초기화 하였습니다.` });
      } else {
        this.$message({ type: `warning`, message: `삭제 할 행을 선택해주세요.` });
      }
    },
    /**
     * * 그리드 행 추가
     */
    addRow() {
      return new Promise((resolve, reject) => {
        // const {empNo, percentageRate, taxRateId, taxRateCode, taxAcctCode, slipTypeCd} = this.$refs.hd.value;
        const empNo = this.$refs.hd?.value?.empNo;
        const percentageRate = this.$refs.hd?.value?.percentageRate;
        const taxRateId = this.$refs.hd?.value?.taxRateId;
        const taxRateCode = this.$refs.hd?.value?.taxRateCode;
        const taxAcctCode = this.$refs.hd?.value?.taxAcctCode;
        const slipTypeCd = this.$refs.hd?.value?.slipTypeCd;

        this.$store.commit('loading');
        this.$http.post(`/api/emp/accrualSlip/search/${empNo}`)
            .then(res => res.data)
            .then(data => {
              data.map((x, i) => {
                x.id = this.getNewRowId();
                // 상태값으로 관리 하는 부분 우선 주석처리.
                //x.status = 'I';
                x.percentageRate = percentageRate;
                x.vatAmount = this.$numeral(x.supplyAmount * (percentageRate / 100)).value()
                x.projectId = x.pjtId;
                x.drCr = 'Cr'; //대변
                if(taxRateId && taxRateCode && taxAcctCode) {
                  x.taxId = taxRateId;
                  x.taxCd = taxRateCode;
                  x.taxNm = taxRateCode;
                  x.taxAcctCode = taxAcctCode;
                  x.taxAcctName = taxAcctCode;
                  if(taxRateCode == '매입불공제') {
                    x.taxFlag = 'N';
                    x.lineAttribute1 = 'Y';
                  } else if(taxRateCode != '' && taxRateCode != '매입불공제') {
                    x.taxFlag = 'Y';
                    x.lineAttribute1 = '';
                  } else {
                    x.lineAttribute1 = '';
                  }
                }
                if(!x.acctDffCnt) {
                  x.attributeCategoryName = '없음';
                } else {
                  x.attributeCategoryName = '입력하세요';
                }
                x.attributeCategory = '';
                x.accountedVatAmount = 0;
                x.accountedSupplyAmount = 0;
                x.deptType = x.attribute2; //
                x.attribute2 = '';
                if(slipTypeCd === 'TRIP') {
                  x.c_type = '현금';
                  x.lineAttribute1 = '';
                  x.taxId = '';
                  x.taxCd = '';
                  x.taxNm = '';
                  x.taxAcctCode = '';
                  x.taxAcctName = '';
                  x.taxFlag = '';
                  x.percentageRate = 0;
                }
                if(slipTypeCd === 'BOND') {
                  /**
                   * TODO: 1. 계정과목 세팅 ?
                   * TODO: 1. 관리항목 세팅 ?  (x.attribute1, x.attribute2, x.attribute3)
                   */
                }
                // return x;
                this.rowData.push(x);
              });
              resolve(this.rowData);
            })
            .catch(error => {
              reject(error)
            })
            .finally(_ => {
              this.$store.commit('finish');
            });
      });
    },
    /**
     * * 그리드 행 삭제
     */
    delRow() {
      const length = this.gridApi.getSelectedNodes().map(x => x.data).length || 0;

      if(length > 0) {
        const data = this.rowData.filter(f => !f.chk);
        this.rowData = data;

        this.gridOptions.api.refreshCells();
        this.amountCalcurate();
      } else {
        this.$message({ type: `warning`, message: `삭제 할 행을 선택해주세요.` });
      }
    },
    /**
     * * 그리드 행 복사
     */
    copyRow() {
      const selectedRows = this.gridApi.getSelectedRows();
      if(selectedRows.length > 0){

        const copyRow = _.cloneDeep(selectedRows.map(x => {
          x.id = x.id + 1;
          x.chk = false;
          return x;
        }));
        copyRow.map(x => {
          // 상태값으로 관리 하는 부분 우선 주석처리.
          // x.status = 'I';
          this.rowData.push(x);
        });

      } else {
        this.$message({ type: `warning`, message: `복사 할 행을 선택해주세요.` });
      }
    },
    /**
     * 그리드 엑셀 다운로드
     * @param {*} title
     */
    excelDown(title) {
      this.$confirm(`${title}을(를) 다운로드를 하시겠습니까?`, `${title}`, {
        confirmButtonText: '예',
        cancelButtonText: '아니오',
        type: 'success'
      }).then(() => {
        const params = { fileName : title };
        this.gridApi.exportDataAsCsv(params)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '취소하였습니다.'
        });
      });
    },
    /**
     * * 그리드 체크 선택 이벤트
     * @param {*} params
     */
    onRowSelected(params) {
      const rowIndex = params.rowIndex;

      this.rowData[rowIndex].chk = params.node.isSelected();
    },
    /**
     * * 그리드 임시 순번 ID 카운트
     */
    getNewRowId() {
      return ++this.rowCount;
    },
    /**
     * * 그리드 상단 버튼 이벤트 리스트
     * @param {*} param0
     */
    btnAction({action, act_type, options}) {
      const { slipTypeCd } = this.$refs.hd.value;
      const self = this;
      const func = {
        popup(action) {
          if(action === 'budgtSelect') { /** 예산조회 */
          self.budgtOpenModal();
          } else if(action === 'cardUsedHst') { /** 법인카드 사용내역 */
          self.openCopCard('A', options);
          }  else if(action === 'acardUsedHst') { /** 항공권카드내역 사용내역 */
          self.openCopCard('F', options);
          } else if(action === 'lineVatCodeDel') { /** 라인부가세코드 삭제 */
            /**
             * 세금 변수 초기화
             * TAX_FLAG, TAX_ID, TAX_CODE, TAX_ACCT_CODE, ORIGINAL_TAX_ACCT_CODE, TAX_PERCENTAGE_RATE, TAX_ACCT_CODE, TAX_CODE_COMBINATION_ID
             * taxId, taxCd, taxNm, taxAcctCode, taxAcctName, taxFlag = 'N ? Y', attribute1
             * ? TODO : 금액 재계산
             * ? doCalcTotalAmt 부가세코드 영세로 재계산 ?
             */
            self.lineVatInit();
          }
        },
        event(action) {
          if(action === 'rowAdd') { /** 행 추가 */
          self.addRow()
          } else if(action === 'rowDel') { /** 행 삭제 */
          self.delRow();
          } else if(action === 'rowCopy') { /** 행 복사 */
          self.copyRow();
          } else if(action === 'excelDwn') { /** 엑셀 다운로드 */
          self.excelDown('발생전표 상세내역');
          } else if(action === 'prepaymentInput') { /** 선급금반제 입력 */
          }
        }
      }

      return func[act_type](action);
    },

    /**
     * * 유류비 계산 호출
     */
    oliPriceCalcuration(baseYm, oilKindCd) {
      return new Promise((resolve, reject) => {
        this.$store.commit('loading');
        this.$http.post('/api/oilPrice/slip', {baseYm: this.$moment(baseYm).format('YYYYMM'), oilKindCd}).then(res => res.data)
            .then(data => resolve(data))
            .catch(err => reject(err))
            .finally(_ => {
              this.$store.commit('finish');
            });
      })
    },

    /**
     * * Wings 문서 팝업
     */
    openUploadWingsPopup(){

      const self = this;
      const readOnly = !this.showSave;
      const slipNo = this.$refs.hd.value.slipNo;

      if(slipNo){
        this.$modal.open({
          component: WingsAtchPop,
          props: {
            slipNo,
            readOnly
          },
          parent: this,
          width: 1200,
          events: {
            close(value) {

              const jiniId = value.map(x => x.jiniId).join('|||');
              const jiniNm = value.map(x => x.jiniNm).join(',');
              self.$refs.hd.value.docUrl = jiniId;
              self.$refs.hd.value.docTitle = jiniNm;
            }
          }
        })
      } else {
        this.$alert(`발생전표를 우선 저장해야 증빙첨부가 가능합니다.`, '확인', {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '확인',
          type: 'error',
          center: true,
          callback: () => {}
        });
      }
    },
    /**
     * 그룹웨어 증빙파일 첨부 팝업
     */
    openUploadGroupwarePopup() {

      const disabled = !this.showSave; //파일삭제 readOnloy flag
      const rdoCtrl = !this.showSave; //파일업로드 readOnloy flag

      const slipNo = this.$refs.hd.value.slipNo;

      if(slipNo) {

        this.$modal.open({
          component: EvidAtchPopGroupware,
          props: {
            slipNo,
            disabled,
            rdoCtrl
          },
          parent: this,
          width: 1200
        });

      } else {
        this.$alert(`발생전표를 우선 저장해야 그룹웨어 문서 첨부가 가능합니다.`, '확인', {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '확인',
          type: 'error',
          center: true,
          callback: () => {}
        });
      }

    },
    /**
     * 증빙파일 첨부 팝업
     */
    openUploadEvidencePopup() {

      const disabled = !this.showSave; //파일삭제 readOnloy flag
      const rdoCtrl = !this.showSave; //파일업로드 readOnloy flag


      console.log(this.$refs.hd.value);

      const slipNo = this.$refs.hd.value.slipNo;

      if(slipNo) {

        this.$modal.open({
          component: EvidAtchPopModeless,
          props: {
            slipNo,
            disabled,
            rdoCtrl
          },
          parent: this,
          width: 1200
        });

      } else {
        this.$alert(`발생전표를 우선 저장해야 증빙첨부가 가능합니다.`, '확인', {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '확인',
          type: 'error',
          center: true,
          callback: () => {}
        });
      }

    },
    /**
     * * 그리드 행 데이터 초기화 함수
     * @param {*} target
     * @param {*} idx
     */
    initialize(target, idx) {
      const self = this;

      const func = {
        initCctr() {
          self.rowData[idx]['deptCd'] = '';
          self.rowData[idx]['deptNm'] = '';
          self.rowData[idx]['deptType'] = '';
        },
        initCctr2() {
          self.rowData[idx]['cctrCd'] = '';
          self.rowData[idx]['cctrNm'] = '';
        },
        initAcct() {
          self.rowData[idx]['acctCd'] = '';
          self.rowData[idx]['acctNm'] = '';
          self.rowData[idx]['acctDffCnt'] = 0;
          self.rowData[idx]['acctRequiredFlagCnt'] = 0;
        },
        initProject() {
          self.rowData[idx]['pjtCd'] = '';
          self.rowData[idx]['pjtNm'] = '';
          self.rowData[idx]['projectId'] = '';
        },
        initTask() {
          self.rowData[idx]['taskNo'] = '';
          self.rowData[idx]['taskNm'] = '';
          self.rowData[idx]['taskId'] = '';
          self.rowData[idx]['taskItemGroup'] = '';
        },
        initProduct() {
          self.rowData[idx]['productCd'] = '';
          self.rowData[idx]['productNm'] = '';
        },
        initVat() {
          self.rowData[idx]['taxId'] = '';
          self.rowData[idx]['taxCd'] = '';
          self.rowData[idx]['taxNm'] = '';
          self.rowData[idx]['taxAcctCode'] = '';
          self.rowData[idx]['taxAcctName'] = '';
          self.rowData[idx]['taxFlag'] = '';
          self.rowData[idx]['lineAttribute1'] = '';
          self.rowData[idx]['percentageRate'] = 0;
        },
        initSubTrxType() {
          self.rowData[idx]['subTrxType'] = '';
          self.rowData[idx]['subTrxTypeName'] = '';
        },
        all() {
          this.initCctr();
          this.initCctr2();
          this.initAcct();
          this.initProject();
          this.initTask();
          this.initProduct();
          this.initVat();
          this.initSubTrxType();
        }
      };

      return new Promise((resolve) => {
        resolve(func[target]());
      });
    },
    /**
     * * 저장 태우기
     * @param {*} data
     */
    save(data) {
      const params = this.redefinition(data.hd.value);

      /** header variables validation */
      data.hd.validate(params)
          .then(async _ => {
            this.validate()
                .then(() => {
                  this.$store.commit('loading');
                  this.$http.post(`/api/slip/save`, params)
                      .then(res => res.data)
                      .then(result => {
                        if(result) {
                          let msg = !params.slipStatus ? '저장 되었습니다.' : '수정 되었습니다.';
                          this.$alert(`<div style="overflow: auto;">${result}의 ${msg}</div>`, '확인', {
                            dangerouslyUseHTMLString: true,
                            confirmButtonText: 'OK',
                            callback: action => {
                              //전표 조회 (header, detail)
                              //this.reloadData(result);
                              this.$router.push({path: `/accrualSlip/${result}`})
                                  .catch(()=>{
                                    window.location.reload();
                                  });
                            }
                          });
                        }
                      })
                      .catch(e => {
                        let errMsg = e.response.status === 400 ? e.response.data.message : '저장 실패하였습니다.';
                        this.$swal({type: 'error' , html: errMsg});
                      })
                      .finally(_ => {
                        this.$store.commit('finish');
                      });
                });
          });
    },
    /**
     * * 전표 삭제
     */
    slipDelete(header) {

      const {slipGroupYn, approvalGroupId, slipHeaderId, trxTypeCode, ledgerId} = header.value;

      if(approvalGroupId && slipHeaderId && trxTypeCode && ledgerId) {
        this.$confirm('해당 전표를 삭제하시겠습니까?', 'Warning',
            {
              distinguishCancelAndClose: true,
              confirmButtonText: '예',
              cancelButtonText: '아니오',
              type: 'warning'
            })
            .then((val) => {
              if(val) {
                const params = {
                  slipGroupYn: slipGroupYn ? slipGroupYn : '',
                  approvalGroupId,
                  slipHeaderId,
                  slipType: trxTypeCode,
                  ledgerId
                }
                this.$store.commit('loading');
                this.$http.post(`/api/slip/delete`, [params]) //객체를 Array 로 담아야 한다고함.. with. WCK
                    .then(res => {
                      this.$swal({
                        type: 'success',
                        text: `${res.data}`
                      }).then(() => {
                        this.$router.push({path: `/accrualSlip`});
                      });
                    })
                    .catch((e) => {
                      this.$message({ type: 'warning', message: `${e.message}` });
                    })
                    .finally(_ => {
                      this.$store.commit('finish');
                    });
              }
            })
            .catch(_ => {
              this.$message({ type: '취소', message: '취소되었습니다.' });
            });
      } else {
        this.$message.error({ type: '알림', message: '삭제할 수 없는 전표 입니다.' });
      }

    },
    /**
     * 저장 시점 요청 데이터 재가공.
     * @param {*} values
     */
    redefinition(values) {
      const keys = Object.keys(values);
      let params = {};
      for (const key of keys) {
        if(typeof values[key] == 'object') {
          if(String(values.slipTypeCd).toLowerCase() == key) {
            if(values.slipTypeCd === 'EXPEND') {
              params['slipExpendDto'] = values[key];
            }
            if(values.slipTypeCd === 'TRAFFIC') {
              params['slipTrafficHdDto'] = values[key];
            }
            if(values.slipTypeCd === 'TRIP') {
              params['slipBusinessTripDtoList'] = values[key];
            }
            if(values.slipTypeCd === 'BOND') {
              params['bondHeaderDto'] = values[key];
            }
          }
        } else {
          params[key] = values[key];

        }
      }

      //slipHeader Parameters 재정의
      params.actualDeptCode = params.deptCd; //귀속부서
      params.globalAttribute9 = params.deptCd; //상신에 필요한 값 위임자 귀속부서
      params.globalAttribute10 = params.empNo; //상신에 필요한 값 위임자 userId
      params.slipCurrencyCode = params.curCd; //통화코드
      if(params.curCd == 'KRW') {
        params.exchangeRateType = null;
        params.exchangeDate = null;
        params.exchangeRate = null;
      } else {
        params.exchangeDate = this.$moment(params.exchangeDate).format("YYYY-MM-DD HH:mm:ss") //환율일자
        params.exchangeRate = params.excRt; //환율
      }
      // params.realAmt = this.$numeral(params.realAmt).value(); //실지급금액
      params.supplyAmount = this.$numeral(params.supplyAmount || 0).value(); //공급가액
      params.totAmt = this.$numeral(params.totAmt || 0).value(); //총금액
      params.totAmtKrw = this.$numeral(params.totAmtKrw || 0).value(); //총 원화 금액
      params.totAmt = this.$numeral(params.totAmt).value(); //전표금액
      params.enteredAmount = this.$numeral(params.totAmt).value(); //전표금액
      params.accountedAmount = Math.round(this.$numeral(params.totAmtKrw).value()); //원화
      params.termDueDate = this.$moment(params.termDueDate).format("YYYY-MM-DD HH:mm:ss") //결제예정일
      if(params.noteFlag == 'Y') {
        if(params.maturityDate) {
          params.maturityDate = this.$moment(params.maturityDate).format("YYYY-MM-DD HH:mm:ss") //어음만기예정일
        } else {
          params.maturityDate = '';
        }
      } else {
        params.maturityDate = '';
      }
      params.postingDate = this.$moment(params.postingDt).format("YYYY-MM-DD HH:mm:ss") //회계년월일

      if(['MCARD', 'ACARD', 'CARD', 'TRIP'].includes(params.slipTypeCd)) {
        params.evidenceDate = this.$moment(params.postingDate).format("YYYY-MM-DD HH:mm:ss")
      } else {
        if(params.lineAttribute1 === 'V') { //부가세인 경우
          if(params.taxSmartbillNo) {
            params.globalAttribute14 = params.taxSmartbillNo;
          }
          if(params.evidenceDate) {
            params.evidenceDate = this.$moment(params.evidenceDate).format("YYYY-MM-DD HH:mm:ss") //부가세 작성년월일
          } else {
            params.evidenceDate = '';
          }
        } else {
          params.evidenceDate = '';
          params.taxIssueTypeCode = '';
          params.taxIssueTypeName = '';
          params.evidenceVendorNum = '';
          params.evidenceVendorVat = '';
          params.evidenceVendorName = '';
          params.evidenceVendorCustSiteId = ''; // 거래처 ID
        }
      }

      //재조회 이후 파라미터
      if(params.glDate) {
        params.glDate = this.$moment(params.glDate).format("YYYY-MM-DD HH:mm:ss")
      }
      if(params.slipDate) {
        params.slipDate = this.$moment(params.slipDate).format("YYYY-MM-DD HH:mm:ss")
      }
      if(params.creationDate) {
        params.creationDate = this.$moment(params.creationDate).format("YYYY-MM-DD HH:mm:ss")
      }
      if(params.lastUpdateDate) {
        params.lastUpdateDate = this.$moment(params.lastUpdateDate).format("YYYY-MM-DD HH:mm:ss")
      }
      if(params.repaymentDueDate) {
        params.repaymentDueDate = this.$moment(params.repaymentDueDate).format("YYYYMMDD")
      }

      if(this.$numeral(params.amountToApply).value() > 0) {
        params.prepaymentApplyFlag = 'K';
      } else {
        params.prepaymentApplyFlag = 'N';
      }

      if(params.ttypePrepaymentFlag === 'Y') {
        params.invoiceTypeLookupCode = 'PREPAYMENT';
      } else {
        if(params.enteredAmount < 0) {
          params.invoiceTypeLookupCode = 'CREDIT';
        } else {
          params.invoiceTypeLookupCode = 'STANDARD';
        }
      }

      if(params.slipTypeCd === 'BOND') {
        params['bondHeaderDto'].openingDt = params['bondHeaderDto'].openingDt ? this.$moment(params['bondHeaderDto'].openingDt).format("YYYY-MM-DD HH:mm:ss") : '';//개설일
        params['bondHeaderDto'].maturityDt = params['bondHeaderDto'].maturityDt ? this.$moment(params['bondHeaderDto'].maturityDt).format("YYYY-MM-DD HH:mm:ss") : '';//만기일
      } else if(params.slipTypeCd === 'AWT') {
        params.awt = params.awt || [];
      } else if(params.slipTypeCd === 'ETCAWT') {
        params.etcawt = params.etcawt || [];
      }


      //slipDetail Parameters 재정의
      params.slipDetailDtoList = this.rowData.map(x => {
        //디테일 저장시 키값 재정의
        x.budgetDeptCode = x.cctrCd;
        x.actualDeptCode = x.deptCd ;
        x.actualDeptType = x.deptType;
        x.acctCode = x.acctCd;
        x.dffCnt = x.acctDffCnt;
        x.requiredFlagCnt = x.acctRequiredFlagCnt;
        x.projectCode = x.pjtCd;
        x.taskCode = x.taskNo;
        x.itemGroupCode = x.productCd;
        x.taxCode = x.taxCd;
        // x.projectId = x.pjtId;
        x.projectCd = x.pjtCd;
        //프론트 정의 안되어있지만 호출 시 셋업되는 변수 재정의(행 추가 또는 조회 시 보이는 컬럼.)
        x.slipTypeCode = x.slipTypeCd;
        x.trAcctCode = x.trAcctCd; //금융계좌 코드
        x.segment9Code = x.segment9Cd;
        x.segment10Code = x.segment10Cd;
        x.taxRateId = x.taxRateId || '';
        x.taxAcctCode = x.taxAcctCode || '';
        x.supplyAmount = this.$numeral(x.supplyAmount || 0).value();
        x.vatAmount = this.$numeral(x.vatAmount || 0).value();
        x.usedAmt = this.$numeral(x.usedAmt || 0).value();
        if(x.taxId) {
          x.accountedVatAmount = this.$numeral(x.accountedVatAmount || 0).value();
        } else {
          x.accountedVatAmount = 0;
        }
        x.accountedSupplyAmount = this.$numeral(x.accountedSupplyAmount || 0).value();

        if(['MCARD', 'ACARD', 'CARD', 'TRIP'].includes(params.slipTypeCd)) {
          x.cardUsedNo = x.cardUsedNo || '';
          if(params.slipTypeCd === 'TRIP') {
            // 전기(해외출장)
            if(params.trxTypeCode === 'SPAP167') {
              x.oamtType = x.amtType;
            }
            // 전기(국내출장)
            if(params.trxTypeCode === 'SPAP170') {
              x.iamtType = x.amtType;
            }
          }
        } else {
          if(params.slipTypeCd === 'ETCAWT') { //잡급비 인 경우
            if(x.withholdingTaxCode === '1') { //소득세 인 경우
              //헤더 거래유형에 의한 awtGroupId 세팅
              x.withholdingTaxCode = params.awtGroupId;
            }
            if(x.withholdingTaxCode === '2') { //고용보험 인 경우
              const awtData = this.$refs.hd.value[String(params.slipTypeCd).toLowerCase()];
              const groupId = awtData.filter(f => f.awtAccountName.indexOf('고용보험료') > -1).map( data => data.groupId)[0];
              x.withholdingTaxCode = groupId;
            }
            if(x.withholdingTaxCode === '3') { //국민연금 인 경우
              const awtData = this.$refs.hd.value[String(params.slipTypeCd).toLowerCase()];
              const groupId = awtData.filter(f => f.awtAccountName.indexOf('국민연금') > -1).map( data => data.groupId)[0];
              x.withholdingTaxCode = groupId;
            }
            if(x.withholdingTaxCode === '4') { //의료보험료 인 경우
              const awtData = this.$refs.hd.value[String(params.slipTypeCd).toLowerCase()];
              const groupId = awtData.filter(f => f.awtAccountName.indexOf('의료보험료') > -1).map( data => data.groupId)[0];
              x.withholdingTaxCode = groupId;
            }
          }

        }
        x.lineTypeLookupCode = 'ITEM';
        x.assetsTrackingFlag = x.assetsTrackingFlag || 'N'; //ASSETS_TRACKING_FLAG : 자산 트래킹 여부
        if(!x.attributeCategoryName || x.attributeCategoryName == '없음') {
          x.attribute2 = '';
        } else {
          x.attribute2 = x.attribute2 || '';
        }
        //라인의 공제여부가 불공제 일 경우 taxAcctCode(부가세계정코드)를 acctCd(계정코드)로 재설정 해준다.
        if(x.taxFlag === 'N') { //불공제
          x.taxAcctCode = x.acctCd;
          x.taxAcctName = x.acctNm;
        }

        return x;
      }); //상세내역
      return params;
    },
    /**
     * 결재 상신 버튼 클릭
     * *
     */
    apprSubmit(data) {
      const self = this;
      // header variables validation
      const params = this.redefinition(data.hd.value);
      const slipTypeCd =  data.hd.value.slipTypeCd;

      data.hd.validate(params)
          .then(async _ => {
            //그리드 벨리데이션
            this.uncontrolledValidate()
                .then(() => {
                  this.validate()
                      .then(() => {
                        this.$store.commit('loading');
                        this.$http.post(`/api/slip/save`, params)
                            .then(res => res.data)
                            .then(result => {
                              this.reloadData(result)
                                  .then((data) => {

                                    if(data && data.length > 0) {
                                      const values = Object.assign({}, params, {rowData: data}, {fileCnt: this.getEvidFileSize, jiniCnt: this.getJiniSize, jinFileiCnt: this.getJiniFileSize});
                                      this.$modal.open({
                                        component: ApprovalModal,
                                        props: {
                                          values
                                        },
                                        parent: this,
                                        width: 1200,
                                        events: {
                                          close(slipNo) {
                                            // ESC 로 넘어올 경우가 있기 때문에 0.1초 간의 시간차를 둬야함..
                                            setTimeout(() =>{
                                              self.$nextTick(() => {
                                                self.$router.push({path: `/accrualSlip/${slipNo}`}).catch(_ => window.location.reload())
                                              });
                                            }, 100)
                                          }
                                        }
                                      });
                                    }
                                  });
                            })
                            .catch((e) => {
                              this.$message({ type: 'warning', message: `${e.message}` });
                            })
                            .finally(_ => {
                              this.$store.commit('finish');
                            });
                      });
                })

          });
    },
    /**
     * * 결재보기 팝업
     */
    approveOpenModal(data) {
      const params = this.redefinition(data);
      const values = Object.assign({}, params, {rowData: this.rowData}, {fileCnt: this.getEvidFileSize, jiniCnt: this.getJiniSize, jiniFileCnt: this.getJiniFileSize});
      this.$modal.open({
        component: ApprovalModal,
        props: {
          values
        },
        parent: this,
        width: 1200,
      });
    },
    /**
     * * 라인 벨리데이션
     */
    validate() {
      return new Promise(async (resolve, reject) => {
        try {
          const { slipTypeCd, totAmt, taxAmount, lineAttribute1, taxbillSupplyAmt, taxbillTaxAmt, taxbillTotalAmt, postingDt, trxTypeCode, integrationVendorNum , taxEvidenceType , taxSmartbillNo } = this.$refs.hd.value;
          const { isTaxReadOnly } = this.$refs.hd;

          if(['110','120'].includes(taxEvidenceType)){
            assert.apply(this, [taxSmartbillNo, `세금계산서를 선택해 주세요.`]);
          }

          if(['110','120','210','220'].includes(taxEvidenceType)){ // 부가세가 존재 하는 경우
            if(taxSmartbillNo) assert.apply(this, [taxbillTaxAmt === taxAmount, `세금계산서 세액과 세액이 일치하지 않습니다.`]);
          }

          assert.apply(this, [this.rowData[0], `상세내역 데이터가 없습니다.`]);

          this.rowData.forEach((v, index) => {
            assert.apply(this, [v.cctrCd, `상세내역 No.${index + 1}의 예산부서를 선택해 주세요.`]);
            assert.apply(this, [v.deptCd, `상세내역 No.${index + 1}의 귀속부서를 선택해 주세요.`]);
            assert.apply(this, [v.acctCd, `상세내역 No.${index + 1}의 계정과목을 선택해 주세요.`]);
            assert.apply(this, [v.projectId, `상세내역 No.${index + 1}의 프로젝트를 선택해 주세요.`]);
            assert.apply(this, [v.taskNm, `상세내역 No.${index + 1}의 태스크를 선택해 주세요.`]);
            assert.apply(this, [v.productCd, `상세내역 No.${index + 1}의 제품군을 선택해 주세요.`]);

            if(v.acctRequiredFlagCnt > 0) {
              assert.apply(this, [v.attributeCategory, `상세내역 No.${index + 1}의 관리항목을 입력해 주세요.`]);
            }
            //0원짜리 전표 작성 가능하도록...
            // if(slipTypeCd == 'COMMON') { //공통
            //     assert.apply(this, [this.$numeral(v.supplyAmount).value(), `상세내역 No.${index + 1}의 공급가액을 입력해 주세요.`]);
            // }
            if(['MCARD', 'ACARD', 'CARD', 'TRIP'].includes(slipTypeCd)) {  //카드

              if(['5503010', '5503020', '5503030', '5352410', '5352420', '5352430',
                '5352130', '5355410', '5501230', '5355430', '5355440', '5502730',
                '5506010', '5506020', '5506030', '5506040', '5501210', '5501220',
                '5501225'].includes(v.acctCd)) {

                if(Math.abs(v.vatAmount * 1) > 0 ) {
                  if(v.taxCd != "매입불공제") {
                    assert.apply(this, [(false), `불공제 계정이 존재합니다. 해당 계정을 확인하시고, 공제여부를 불공제로 변경하시기 바랍니다.\n\n※ 세액이 0원이면, 공제여부를 면세_영세_간이과세로 선택 하시고 진행하시기 바랍니다.`]);
                  }
                } else {
                  if(integrationVendorNum === '1002318' || integrationVendorNum === '1004386') {
                  } else {
                    if(v.taxCd && !(v.taxCd === '매입영세' || v.taxCd === '매입면세')) {
                      assert.apply(this, [(false), `불공제 계정이 존재합니다. 해당 계정을 확인하시고, 공제여부를 불공제로 변경하시기 바랍니다.\n\n※ 세액이 0원이면, 공제여부를 면세_영세_간이과세로 선택 하시고 진행하시기 바랍니다.`]);
                    }
                  }
                }
              }
            } else {
              //법인카드 이외일경우
              if(['5503010', '5503020', '5503030', '5352410', '5352420', '5352430',
                '5352130', '5355410', '5501230', '5355430', '5355440', '5502730',
                '5506010', '5506020', '5506030', '5506040', '5501210', '5501220',
                '5501225'].includes(v.acctCd)) {
                console.log("v.vatAmount " , v.vatAmount)
                if(Math.abs(v.vatAmount * 1) > 0 ) {
                  if(v.taxCd != "매입불공제") {
                    assert.apply(this, [(false), `불공제 계정이 존재합니다. 해당 계정을 확인하시고, 헤더부가세코드를 불공제로 변경하시기 바랍니다.`]);
                  }
                } else {
                  if(v.taxCd && !(v.taxCd === '매입영세' || v.taxCd === '매입면세')) {
                    assert.apply(this, [(false), `불공제 계정이 존재합니다. 해당 계정을 확인하시고, 공제여부를 불공제로 변경하시기 바랍니다.\n\n※ 세액이 0원이면, 공제여부를 면세_영세_간이과세로 선택 하시고 진행하시기 바랍니다.`]);
                  }
                }
              }
            }
            if(slipTypeCd == 'EXPEND') { //경조금
            } else if(slipTypeCd == 'MCARD') { //직속기구
              //법인카드번호
              assert.apply(this, [v.globalAttribute8, `상세내역 No.${index + 1}의 법인카드번호를 입력해 주세요.`]);
              //가맹점 사업자번호
              assert.apply(this, [v.globalAttribute13, `상세내역 No.${index + 1}의 가맹점 사업자번호를 입력해 주세요.`]);
              //가맹점 사업자번호
              assert.apply(this, [v.globalAttribute13.replace(/,/gi,'').length == 10, `상세내역 No.${index + 1}의 가맹점 사업자번호가 형식에 맞지 않습니다. -없이 총 10자리의 숫자를 입력하세요.`]);
              //사용일자
              assert.apply(this, [v.globalAttribute18, `상세내역 No.${index + 1}의 사용일자를 입력해 주세요.`]);
              //가맹점상호
              assert.apply(this, [v.globalAttribute15, `상세내역 No.${index + 1}의 가맹점상호를 입력해 주세요.`]);
              //가맹점업종
              assert.apply(this, [v.globalAttribute16, `상세내역 No.${index + 1}의 가맹점업종을 입력해 주세요.`]);
              //과세유형
              // assert.apply(this, [v.globalAttribute16, `상세내역 No.${index + 1}의 과세유형을 입력해 주세요.`]);
              //공제여부
              assert.apply(this, [v.taxFlag, `상세내역 No.${index + 1}의 공제여부를 선택해 주세요.`]);
            }
            if(slipTypeCd == 'TRAFFIC') { //교통비
              assert.apply(this, [(v.usedDt), `상세내역 No.${index + 1}의 사용일자가 없습니다.`]);
              assert.apply(this, [(v.fromArea), `상세내역 No.${index + 1}의 장소코드가 없습니다.`]);
              assert.apply(this, [(v.fromAreaText), `상세내역 No.${index + 1}의 출발-도착지가 없습니다.`]);
              assert.apply(this, [(v.trafficType), `상세내역 No.${index + 1}의 교통수단이 없습니다.`]);
              assert.apply(this, [(v.sumAmt), `상세내역 No.${index + 1}의 교통비 사용금액이 없습니다.`]);
              assert.apply(this, [(v.usedAmt), `상세내역 No.${index + 1}의 사용금액이 없습니다`]);

              const postDt = this.$moment(postingDt).format('YYYYMMDD'); //회계일자
              const usedDt = this.$moment(v.usedDt).format('YYYYMMDD'); //사용일자
              assert.apply(this, [(postDt >= usedDt), `상세내역 No.${index + 1}의 회계일자보다 사용일자가 클 수 없습니다.`]);
              const etcAmt1 = this.$numeral(v.etcAmt1 || 0).value();
              const etcAmt2 = this.$numeral(v.etcAmt2 || 0).value();
              const etcAmt3 = this.$numeral(v.etcAmt3 || 0).value();
              if(etcAmt1) {
                assert.apply(this, [(v.etcType1), `상세내역 No.${index + 1}의 기타교통비1 을 선택하세요`]);
              }
              if(v.etcType1) {
                assert.apply(this, [(v.etcAmt1), `상세내역 No.${index + 1}의 기타금액1 금액이 없습니다.`]);
              }
              if(etcAmt2) {
                assert.apply(this, [(v.etcType2), `상세내역 No.${index + 1}의 기타교통비2 을 선택하세요`]);
              }
              if(v.etcType2) {
                assert.apply(this, [(v.etcAmt2), `상세내역 No.${index + 1}의 기타금액2 금액이 없습니다.`]);
              }
              if(etcAmt3) {
                assert.apply(this, [(v.etcType3), `상세내역 No.${index + 1}의 기타교통비3 을 선택하세요`]);
              }
              if(v.etcType3) {
                assert.apply(this, [(v.etcAmt3), `상세내역 No.${index + 1}의 기타금액3 금액이 없습니다.`]);
              }

            } else {

              assert.apply(this, [v.description, `상세내역 No.${index + 1}의 적요를 입력해 주세요.`]);

              if(['PO', 'IM'].includes(slipTypeCd)){
                assert.apply(this, [v.description.length < 220, `상세내역 No.${index + 1}의 자재품명 길이가 220자를 초과 하였습니다.`]);
              }else{
                assert.apply(this, [v.description.length < 70, `상세내역 No.${index + 1}의 적요 길이가 70자를 초과 하였습니다.`]);
              }

            }
          });

          if(lineAttribute1 === 'V' && !['MCARD', 'ACARD', 'CARD', 'TRIP', 'PO', 'IM'].includes(slipTypeCd)) {
            //  ===
            if(!isTaxReadOnly) {
              const supplyAmount = this.$numeral(this.rowData.filter(f => f.taxId).map(x => x.supplyAmount).reduce((prev, next) => {
                return parseInt(next) + parseInt(prev);
              }, 0)).value() ;
              const vatAmount = this.$numeral(this.rowData.filter(f => f.taxId).map(x => x.vatAmount).reduce((prev, next) => {
                return parseInt(next) + parseInt(prev);
              }, 0)).value() ;
              const usedAmt = this.$numeral(this.rowData.filter(f => f.taxId).map(x => x.usedAmt).reduce((prev, next) => {
                return parseInt(next) + parseInt(prev);
              }, 0)).value() ;

              const taxSupAmt = this.$numeral(taxbillSupplyAmt).value();
              const taxVatAmt = this.$numeral(taxbillTaxAmt).value();
              const taxtotal = this.$numeral(taxbillTotalAmt).value();
              assert.apply(this, [supplyAmount === taxSupAmt, `세금계산서 공급가액과 라인 공급가액의 합이 일치하지 않습니다.`]);
              assert.apply(this, [vatAmount === taxVatAmt, `세금계산서 세액과 라인 세액의 합이 일치하지 않습니다.`]);
              assert.apply(this, [usedAmt === taxtotal, `세금계산서 합계와 라인 합계가 일치하지 않습니다.`]);
            }
          } else {
            const usedAmt = this.$numeral(this.rowData.map(x => x.usedAmt).reduce((prev, next) => {
              return parseInt(next) + parseInt(prev);
            }, 0)).value();

            const hdTotAmt = this.$numeral(totAmt).value();

            if(!isTaxReadOnly) {
              assert.apply(this, [usedAmt === hdTotAmt, `합계금액과 전표금액이 일치하지 않습니다.`]);
            }
          }
          resolve();
        } catch (e) {
          if (e instanceof EvalError) {
            console.error(e.name + ": " + e.message);
          } else if (e instanceof RangeError) {
            console.error(e.name + ": " + e.message);
          } else if (e instanceof TypeError) {
            console.error(e.name + ": " + e.message);
            this.$swal({
              type: 'warning',
              text: "저장할 데이터가 존재하지 않습니다."
            }).then(reject)
          } else {
            this.$swal({
              type: 'error',
              text: e
            }).then(reject)
          }
        }
      })
    },
    /**
     * * 법인카드 비통제성 계정 벨리데이션
     */
    uncontrolledValidate() {
      return new Promise(async (resolve, reject) => {
        try {
          const { trxTypeCode } = this.$refs.hd.value;

          this.rowData.forEach((v, index) => {
            assert.apply(this, [v.acctCd, `상세내역 No.${index + 1}의 계정과목을 선택해 주세요.`]);

            if(['SPAP004'].includes(trxTypeCode)) {  //카드
              if(this.controlledAccount.filter(x => x.flexValue === v.acctCd).length == 0) {
                assert.apply(this, [this.getJiniSize + this.getJiniFileSize > 0, `비통제계정의 그룹웨어 문서 첨부는 필수입니다.`]);
              }
            }
          });

          resolve();
        } catch (e) {
          if (e instanceof EvalError) {
            console.error(e.name + ": " + e.message);
          } else if (e instanceof RangeError) {
            console.error(e.name + ": " + e.message);
          } else if (e instanceof TypeError) {
            console.error(e.name + ": " + e.message);
            this.$swal({
              type: 'warning',
              text: "저장할 데이터가 존재하지 않습니다."
            }).then(reject)
          } else {
            this.$swal({
              type: 'error',
              text: e
            }).then(reject)
          }
        }
      })
    },
    recycle() {
      this.$confirm(`전표를 재사용하시겠습니까?`, `전표 재사용`, {
        confirmButtonText: '예',
        cancelButtonText: '아니오',
        type: 'warning',
      }).then((val) => {
        if(val) {
          const params = {
            slipNo: this.$refs.hd.value.slipNo,
          }
          this.$store.commit('loading');
          this.$http.post(`/api/slip/reuse`, params)
              .then(res => {
                this.$swal({
                  type: 'success',
                  text: `전표 재사용이 완료되었습니다.`
                }).then(() => {
                  this.$router.push({path: `/accrualSlip/${res.data}`});
                }).catch((e) => {
                  this.$message({ type: 'warning', message: `${e.message}` });
                })
              })
              .catch(e => {
                let errMsg = e.response.status === 400 ? e.response.data.message : '재사용 실패하였습니다.';
                this.$message.error({type: 'error', message: errMsg});
              })
              .finally(_ => {
                this.$store.commit('finish');
              });
        }
      })
          .catch(_ => {
            this.$message({ type: '취소', message: '취소되었습니다.' });
          });
    },
    /**
     * 페이지 새로고침 (신규 로드)
     */
    newPageLoad() {
      this.$confirm(`신규버튼 클릭 시 모든 데이터가 초기화 되어집니다. 초기화 하시겠습니까?`, `초기화`, {
        confirmButtonText: '예',
        cancelButtonText: '아니오',
        type: 'warning',
      }).then(() => {
        // 이미 /accrualSlip(파라미터 없음)이면 경로가 동일해 재마운트되지 않으므로 강제 새로고침으로 초기화.
        // (router.js 전역 패치가 중복 네비게이션 reject를 삼켜 .catch reload가 동작하지 않음)
        if (this.$route.path === '/accrualSlip') {
          window.location.reload()
        } else {
          // 기존 전표(/accrualSlip/:id)를 보던 중이면 라우팅만으로 :key(fullPath) 변경 → 재마운트 → 초기화
          this.$router.push({path: `/accrualSlip`})
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '취소하였습니다.'
        });
      });
    },
    print(data) {
      const params = this.redefinition(data);
      const values = Object.assign({}, params, {rowData: this.rowData}, {fileCnt: this.getEvidFileSize, jiniCnt: this.getJiniFileSize});
      this.$modal.open({
        component: PrintModal,
        props: {
          values
        },
        parent: this,
        width: 1400,
      });
    },
  },
  //method end.
  watch: {
    'data.remark'(val) {
      this.$refs.hd.value.remark = val;
    }
  }
}
</script>