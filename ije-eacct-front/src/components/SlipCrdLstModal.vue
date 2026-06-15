<template>
  <layout>
    <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
    
    <div slot="content">
      <div class="content-name">
          <!-- <div class="btn-wrap btn-type1 clearfix fl_right" >
                        
                    </div> -->
          <div class="btn-wrap btn-type1 clearfix">
              <!-- <button class="btn-size btn-blue fl_right" @click="goSearch"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span> 조회</button> -->
              <div class="dp-ib fl_right">
                  <button class="btn-size btn-gray fl_right" style="height: 33px; margin-right: 7px;" @click="allUnChk"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span> 전체해제</button>
              </div>
              <div class="dp-ib fl_right">
                  <button class="btn-size btn-blue fl_right" style="height: 33px; margin-right: 7px;" @click="allChk"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span>전체선택</button>
              </div>
              <div class="dp-ib fl_right">
                  <button class="btn-size btn-orange fl_right" style="height: 33px; margin-right: 7px;" @click="setCardFgCd"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span>개인용도</button>
              </div>
              <div class="dp-ib fl_right">
                  <button class="btn-size btn-blue fl_right" style="height: 33px; margin-right: 7px;" @click="setDataClose"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span>전표작성</button>
              </div>
              
          </div>
      </div>
      <!-- 검색조건 영역 -->
      <div class="desc-content search-border">
          <div class="item desc-left">
              <div class="desc-item">
                  <div class="tit">
                      <span class="label-tit">사용일자</span>
                  </div>
                  <div class="desc">
                      <div class="datepicker w-type-ymd02">
                          <dhx-calendar class="input ddate sDate" v-model="form.searchDtmFr" />
                      </div>
                      <span class="wave"> ~ </span>
                      <div class="datepicker w-type-ymd02">
                          <dhx-calendar class="input ddate sDate" v-model="form.searchDtmTo" />
                      </div>
                  </div>
              </div>
          </div>
          <div class="item desc-center" style="width: 300px; margin-right:20px;">
              <div class="desc-item" >
                <div class="tit">
                  <span class="label-tit"> 카드번호</span>
                </div>
                <div class="desc">
                    <b-select class="select is-fullwidth" v-model="form.crdNo">
                        <!-- <option value=''>==전체==</option> -->
                        <option
                                v-for="item in cardNos"
                                :key="item.crdNo"
                                :value="item.crdNo"
                                v-text="`${item.maskedCardNo} (${item.crdPssrNm})`"/>
                    </b-select>
                  <!-- <b-select class="select is-fullwidth" v-model="form.crdNo">
                      <option value=''>==선택==</option>
                      <option
                              v-for="item in cardList"
                              :key="item.crdNo"
                              :value="item.crdNo"
                              v-text="item.maskedCrdNo"/>
                  </b-select> -->
                </div>
              </div>
              <!-- <div class="desc-item" style="display:none;">
                <div class="tit">
                    <span class="label-tit"> 카드구분</span>
                </div>
                <div class="desc">
                    <b-select class="select is-fullwidth" v-model="form.crdFgCd">
                        <option value=''>==전체==</option>
                        <option
                                v-for="item in crdFgCds"
                                :key="item.key"
                                :value="item.key"
                                v-text="item.value"/>
                    </b-select>
                </div>
              </div> -->
          </div>
          <div class="item desc-right" style="width: 250px; margin-right: 10px;">
            <div class="desc-item">
                <div class="tit">
                    <span class="label-tit">처리상태</span>
                </div>
                <div class="desc">
                    <b-select class="select is-fullwidth" v-model="form.useDtlsStatCd">
                        <option
                                v-for="item in useDtlsStatCds"
                                :key="item.key"
                                :value="item.key"
                                v-text="item.value"/>
                    </b-select>
                </div>
            </div>
          </div>
          <div class="item desc-right" style="width: 100px">
              <div class="desc-item">
                  <div class="tit">
                      <div class="btn-wrap btn-type1 clearfix">
                        <button class="btn-size btn-gray fl_left" style="height: 26px;" @click="goSearch">
                            <span class="btn-icon"><i class="fas fa-search"></i></span> 조회
                        </button>
                    </div>
                  </div>
              </div>
          </div>
      </div>
      <!-- //검색조건 영역 -->

      

      <!-- 테이블 -->
      <div class="table-area">
          <div class="table-header">
            
            <div class="table-a fixed">

                <div class="content">
                    
                    <div class="btn-wrap" >
                        <table class="table">
                            <tbody>
                                <tr>
                                    <th style="width: 130px; text-align: center;">비용항목/계정명</th>
                                    <td colspan="2">
                                        <div class="td-s-thumb search-area">
                                            <input class="input input-bg mr5 Rt-M rs-mt2" type="text" v-model="account.expenseName" style="width:66px;" disabled="disabled"/>
                                            <div class="ip-box ip-box-w02 rs-mt2">
                                                <input type="text" class="input" v-model="account.expenseAcctName" @focus="popAccount(true)"/>
                                                <button class="icon is-right" @click="popAccount(true)">
                                                <i class="fas fa-search"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </td>
                                    <th style="width: 80px; text-align: center;">제품</th>
                                    <td colspan="2">
                                        <div class="td-s-thumb search-area">
                                            <div class="ip-box ip-box-w02 rs-mt2" style="width: 180px;">
                                                <input type="text" class="input" v-model="product.detailNm" @focus="popProduct(true)"/>
                                                <button class="icon is-right" @click="popProduct(true)">
                                                <i class="fas fa-search"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </td>
                                    <th style="width: 80px; text-align: center;">적요</th>
                                    <td colspan="4"><input class="input" type="text" v-model="form.crdOln"></td>
                                </tr>
                                <tr>
                                    <th style="width: 130px; text-align: center;">출발지</th>
                                    <td colspan="2">
                                        <div class="td-s-thumb search-area">
                                            <input class="input" type="text" v-model="form.stptPlcPop">
                                        </div>
                                    </td>
                                    <th style="width: 80px; text-align: center;">도착지</th>
                                    <td colspan="2">
                                        <input class="input" type="text" v-model="form.dstnPlcPop">
                                    </td>
                                    <th style="width: 80px; text-align: center;">출장목적</th>
                                    <td colspan="4"><input class="input" type="text" v-model="form.biztrpObjPop"></td>
                                </tr>
                            </tbody>
                        </table>
                        

                        <!-- <div class="dp-ib fl_left">
                            <input class="input input-bg" type="text" v-model="account.expenseName" style="width:90px;"  placeholder="항목" disabled/>
                        </div>
                        <div class="dp-ib fl_left" style="margin-right: 10px;">
                            <button class="icon is-right" @click="popAccount(true)"><i class="fas fa-search"></i></button>
                        </div>
                        
                        <div class="dp-ib fl_left">
                            <input class="input input-bg" type="text" v-model="account.expenseAcctName" style="width:150px; margin-right:10px;" placeholder="계정" @input="initAccount"/>
                        </div>
                        <div class="dp-ib fl_left">
                            <input class="input input-bg" type="text" v-model="product.detailNm" style="width:150px;" placeholder="제품" @input="initProduct"/>
                        </div>
                        <div class="dp-ib fl_left" style="margin-right: 30px;">
                            <button class="icon is-right" @click="popProduct(true)"><i class="fas fa-search"></i></button>
                        </div>
                        <div class="dp-ib fl_right" >
                            <label class="label-tit" style="margin-right:10px;"> 적요  </label>
                            <input type="text" class="input Rt-M" style="width: 600px;" v-model="form.crdOln" />
                        </div> -->


                    </div>
                </div>
            </div>
        </div>
          <div class="table-b">
              <div class="grid-wrap">
                  <dhx-grid ref="grid" v-model="useDetails" :config="config" @constructGridSuccessful="constructGridSuccessful"/>
              </div>
          </div>
      </div>
      <!-- //테이블 -->

      <!--팝업-->
      <b-modal :active.sync="showEmpModal" has-modal-card @receive="receiveEmp">
          <emp :param="form.crdPssrNm"></emp>
      </b-modal>
      <b-modal :active.sync="showAccountModal" has-modal-card @receive="receiveAccount">
        <erpAccountPop :param="account.expenseName" :slipTypeCd="slipTypeCd" :deptCd="deptCd"></erpAccountPop>
      </b-modal>
      <b-modal :active.sync="showProductModal" has-modal-card @receive="receiveProduct">
        <product :param="product.detailNm" :slipTypeCd="slipTypeCd"></product>
      </b-modal>
      <!--//팝업 -->
    </div>
  </layout>
</template>

<script>
import moment from 'moment'
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import _ from 'lodash'

import assert from '@/libs/assert'
import Layout from '@/components/ModalSlot.vue'
import DhxCalendar from '@/components/DhxCalendar.vue'
import DhxGrid from '@/components/DhxGrid.vue'
import Emp from '@/components/Emp.vue'
import ErpAccountPop from '@/components/ErpAccountPop.vue'
import Product from '@/components/Product.vue'
import GridCheckbox from "@/components/grid/GridCheckbox.vue"

import {
  url as _url
} from '@/libs/join'

//
//'./grid/GridCheckbox.vue'
//['param','slipTypeCd'],
export default {
  name: 'SlipCrdLst',
  props: {
    param: {
      type: String,
      required: false
    }, 
    deptCd: {
      type: String,
      required: false
    },
    slipTypeCd: {
      type: String,
      required: false
    },
    copyUseDetails: {
      type: Array,
      required: false
    }
  },
  components: {Emp, ErpAccountPop, Product, DhxGrid, DhxCalendar, Layout},
  mixins: [ mixin, mixinSlip ],
  data() {
    return {
      config : {
          columns: [
            {id: 'useYn', type: 'ch', value: '선택', width: 40},
            // {id: 'rn', type: 'cntr', align: 'center', sort: 'na', value: 'No.', width: 35},
            {id: 'cardNo', type: 'ro', align: 'center', sort: 'na', value: '카드번호', width: 150},
            {id: 'merchNm', type: 'ro', align: 'center', sort: 'na', value: '가맹점명', width: 150},
            {id: 'crdFgNm', type: 'ro', align: 'center', sort: 'na', value: '카드구분', width: 80},
            {id: 'apprDate', type: 'ro', align: 'center', sort: 'na', value: '승인일자', width: 120},
            {id: 'apprTime', type: 'ro', align: 'center', sort: 'na', value: '승인시각', width: 120},
            {id: 'useDtlsStatNm', type: 'ro', align: 'center', sort: 'na', value: '처리상태', width: 110},
            {id: 'purchTot', type: 'ron', align: 'right', sort: 'na', value: '매입금액', width: 80},
            {id: 'apprAmt', type: 'ron', align: 'right', sort: 'na', value: '공급가액', width: 80},
            {id: 'vat', type: 'ron', align: 'right', sort: 'na', value: '부가세', width: 80},
            {id: 'tips', type: 'ron', align: 'right', sort: 'na', value: '봉사료', width: 80},
            // {id: 'crdCompNm', type: 'ro', align: 'center', sort: 'na', value: '카드사', width: 50},
            // {id: 'apprDate', type: 'ro', align: 'center', sort: 'na', value: '승인일자', width: 120},
            // {id: 'apprTime', type: 'ro', align: 'center', sort: 'na', value: '승인시각', width: 120},
            // {id: 'purchTot', type: 'ron', align: 'right', sort: 'na', value: '사용금액', width: 150},
            // {id: 'apprAmt', type: 'ron', align: 'right', sort: 'na', value: '공급가액', width: 150},
            // {id: 'vat', type: 'ron', align: 'right', sort: 'na', value: '부가세', width: 150},
            // {id: 'tips', type: 'ron', align: 'right', sort: 'na', value: '봉사료', width: 150},
            // {id: 'merchNm', type: 'ro', align: 'center', sort: 'na', value: '가맹점명', width: 200},
            {id: 'apprNo', type: 'ro', align: 'center', sort: 'na', value: '승인번호', width: 120},
            {id: 'acqClassNm', type: 'ro', align: 'center', sort: 'na', value: '매입구분', width: 100},
            {id: 'useDtlsCancel', align: 'center', value: '처리취소', width: 60},
            {id: 'merchAddr', type: 'ro', align: 'center', sort: 'na', value: '가맹점주소', width: 300},
            {id: 'mccName', type: 'ro', align: 'center', sort: 'na', value: '업종명', width: 120},
            {id: 'etc3', type: 'ro', align: 'center', sort: 'na', value: '과세구분', width: 100},
            {id: 'eaSlipNo', type: 'ro', align: 'center', sort: 'na', value: '전표번호', width: 150},
            {id: 'slipStatNm', type: 'ro', align: 'center', sort: 'na', value: '전표상태', width: 100},
            {id: 'crdPssrNm', type: 'ro', align: 'center', sort: 'na', value: '카드사용자', width: 100},
            {id: 'crdUseDutNm', type: 'ro', align: 'center', sort: 'na', value: '직급', width: 100},
            {id: 'crdUseDeptNm', type: 'ro', align: 'center', sort: 'na', value: '부서명', width: 150},
            {id: 'procPeriod', type: 'ro', align: 'center', sort: 'na', value: '경비처리기한', width: 120},
            {id: 'slipProcNm', type: 'ro', align: 'center', sort: 'na', value: '경비처리자', width: 100},
            {id: 'erpSlipDtm', type: 'ro', align: 'center', sort: 'na', value: '전송일시', width: 150},
            // {id: 'compNm', type: 'ro', align: 'center', sort: 'na', value: '회사', width: 150},
          ],
          events: {
            // onRowDblClicked(rid) {
            //     try{
            //         var index = rid - 1
            //         let datas = this.data.filter(x => x.useYn === 'Y')

            //         assert.apply(this, [datas.length > 0, '전표를 선택해주세요.'])
            //         var self = this.$parent.$parent
            //         self.$emit('close', datas)
            //     }catch(e){
            //         this.$swal({
            //             type: 'error',
            //             text: e
            //         })
            //     }
                
            // }
          },
          height: 390,
          enablePaging: false,
        //   pagingSize: 8,
        //   queryPage: (page) => {
        //       page = page || 0
        //       return new Promise((resolve, reject) => {
        //           let data = {
        //               contents: this.useDetails,
        //               page: page,
        //               totalPages: parseInt(this.useDetails.length / this.config.pagingSize) + (parseInt(this.useDetails.length % this.config.pagingSize) > 0 ? 1 : 0),
        //               totalElements: 0
        //           }
        //           resolve({
        //               data: data
        //           })
        //       })
        //   },
        // beforeRefreshData(grid, data) {
        //     _.forEach(data, (item, index) => {
        //         var rId = index + 1
        //         grid.cells(rId, 1).setValue(this.$parent.$parent.getFormattedCardNumber(grid.cells(rId, 1).getValue()));
        //         grid.cells(rId, 4).setValue(this.$moment(grid.cells(rId, 4).getValue()).format('YYYY-MM-DD'));
        //         if (grid.cells(rId, 5).getValue()) grid.cells(rId, 5).setValue(this.$moment(grid.cells(rId, 5).getValue(), 'HHmmss').format('HH:mm:ss'));
        //         if (grid.cells(rId, 24).getValue()) grid.cells(rId, 24).setValue(this.$moment(grid.cells(rId, 24).getValue()).format('YYYY-MM-DD HH:mm:ss'));
        //     })
        // },
        afterRefreshData(grid, data) {

            this.$nextTick(() => {
                _.forEach(data, (item, index) => {
                    var rId = index + 1
                    
                    if(data[index].cStateYn !== undefined) {
                        // console.log(data[index].cStateYn)
                        if(data[index].cStateYn === true) {
                            grid.setRowColor(rId, "orange")
                        }
                    }
                    // grid.cells(rId, 1).setValue(this.$parent.$parent.getFormattedCardNumber(grid.cells(rId, 1).getValue()));
                    // grid.cells(rId, 4).setValue(this.$moment(grid.cells(rId, 4).getValue()).format('YYYY-MM-DD'));
                    // if (grid.cells(rId, 5).getValue()) grid.cells(rId, 5).setValue(this.$moment(grid.cells(rId, 5).getValue(), 'HHmmss').format('HH:mm:ss'));
                    // if (grid.cells(rId, 24).getValue()) grid.cells(rId, 24).setValue(this.$moment(grid.cells(rId, 24).getValue()).format('YYYY-MM-DD HH:mm:ss'));
                })
            })
        }
      },
      title: '법인카드전체내역',
      compCds: [],
      crdCompCds: [],
      useDtlsStatCds: [],
      crdFgCds: [],
      useDetails: [],
      showEmpModal: false,
      showAccountModal: false,
      showProductModal: false,
      authority: '',
    //   cardList: [],
      cardNos: [],
      account: {
        expenseName: '',
        expenseAcctCode: '',
        expenseAcctName: '',
        expenseId: ''
      },
      product: {
        detailCd: '',
        detailNm: '',
      },
      form: {
          useYn: 'N',
          compCd: '',
        //   crdCompCd: '',
          useDtlsStatCd: '10',
        //   crdFgCd: '',
          crdNo: '',
          crdPssrId: '',
          crdPssrNm: '',
          crdPssrTitle: '',
          crdPssrDut: '',
          crdPssrDept: '',
          searchDtmFr: this.$moment().startOf('month').format('YYYY-MM-DD'),
          searchDtmTo: this.$moment().endOf('month').format('YYYY-MM-DD'),
      },
      
    }
  },
  methods: {
    constructGridSuccessful(grid) {
        grid.attachHeader(
            []
        )
        grid.setNumberFormat('0,000', 7, '.', ',')
        grid.setNumberFormat('0,000', 8, '.', ',')
        grid.setNumberFormat('0,000', 9, '.', ',')
        grid.setNumberFormat('0,000', 10, '.', ',')
    },
    getCompCdCombo() {
        this.$http.get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})
            .then(response => {
                this.compCds = response.data;
            });
    },
    getCardCompCdCombo() {
        this.$http.get(`/api/code/combo`, {params: {groupCd: "CRD_COMP_CD"}})
            .then(response => {
                this.crdCompCds = response.data;
            });
    },
    getUseStatCdCombo() {
        this.$http.get(`/api/code/combo`, {params: {groupCd: "USE_DTLS_STAT_CD"}})
            .then(response => {
                this.useDtlsStatCds = response.data;
            });
    },
    getCardFgCdCombo() {
        this.$http.get(`/api/code/combo`, {params: {groupCd: "CRD_FG_CD"}})
            .then(response => {
                this.crdFgCds = response.data;
            });
    },
    saveExcel() {
        this.downloadExcel(this.$refs.grid);
    },
    goSearch() {
        const param = JSON.parse(JSON.stringify(this.form));
        const stripFields = ['searchDtmFr', 'searchDtmTo','crdNo'];
        _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

        this.$store.commit('loading');
        this.$http.post(`/api/card/useList`, {
                // compCd: param.compCd,
                searchDtmFr: param.searchDtmFr,
                searchDtmTo: param.searchDtmTo,
                useDtlsStatCd: param.useDtlsStatCd,
                cardNo: param.crdNo,
                // crdPssrId: param.crdPssrId,
                // crdCompCd: param.crdCompCd,
                // crdFgCd: param.crdFgCd
            }
        )
        .then(response => {
            if (response.data) {
                var data = response.data
                for(let i = 0; i < response.data.length; i++) {
                    for(let k = 0; k < this.copyUseDetails.length; k++) {
                        if(this.copyUseDetails[k].apprNo === data[i].apprNo){
                            data[i].cStateYn = true
                        }
                    }
                    data[i].useYn = false;
                }
                this.useDetails = data;
            }
        }).finally(()=> {
            this.$nextTick(() => {
                _.forEach(this.useDetails, (item, index) => {
                    this.$refs.grid.value[index].cardNo = this.getFormattedCardNumber(this.$refs.grid.value[index].cardNo)
                    this.$refs.grid.value[index].apprDate = this.$moment(this.$refs.grid.value[index].apprDate).format('YYYY-MM-DD')
                    if (this.$refs.grid.value[index].apprTime) this.$refs.grid.value[index].apprTime = this.$moment(this.$refs.grid.value[index].apprTime, 'HHmmss').format('HH:mm:ss')
                    if (this.$refs.grid.value[index].erpSlipDtm) this.$refs.grid.value[index].erpSlipDtm = this.$moment(this.$refs.grid.value[index].erpSlipDtm).format('YYYY-MM-DD HH:mm:ss')
                })
            }, 200)

            this.$store.commit('finish');
        });
    },
    popAccount(clear) {
      if (clear === true) this.account = {};
      this.showAccountModal = true;
    },
    receiveAccount(obj) {
      this.account.expenseAcctCode = obj.expenseAcctCode;
      this.account.expenseAcctName = obj.expenseAcctName;
      this.account.expenseName = obj.expenseName;
      this.account.expenseId = obj.expenseId;
    },
    initAccount() {
      if (this.account.expenseName === '') this.account = {};
    },
    popProduct(clear) {
      if (clear === true) this.product = {};
      this.showProductModal = true;
    },
    receiveProduct(obj) {
        this.product.detailCd = obj.detailCd;
        this.product.detailNm = obj.detailNm;
    },
    initProduct() {
      if (this.product.detailNm === '') this.product = {};
    },
    popEmp(clear) {
        if (clear === true) {
            this.form.crdPssrId = '';
            this.form.crdPssrNm = '';
            this.form.crdPssrDut = '';
            this.form.crdPssrDept = '';
        }
        this.showEmpModal = true;
    },
    receiveEmp(obj) {
        this.form.crdPssrId = obj.empNo;
        this.form.crdPssrNm = obj.empNm;
        this.form.crdPssrDut = obj.dutNm;
        this.form.crdPssrDept = obj.deptNm;
    },
    initEmp(force) {
        if (force === true) this.form.crdPssrNm = '';
        if (this.form.crdPssrNm === '') {
            this.form.crdPssrId = '';
            this.form.crdPssrDut = '';
            this.form.crdPssrDept = '';
        }
    },
    allChk(){
        //1.
        // let data = this.useDetails;
        // let pageSize = this.config.pagingSize;
        // let curPage = $(".page-num.active")[0].innerText;
        // let c = (curPage-1) * pageSize;

        // for(let i=0; i < data.length; i++){
        //     if(c >= pageSize){
        //         let t = i+c;
        //         if(t<data.length){
        //             this.useDetails[t].useYn = true;
        //         }
                
        //     }else{
        //         if(i < pageSize) this.useDetails[i].useYn = true;
        //     }
            
        // }
        
        // this.$store.commit('finish')
        //2.
        let data = this.useDetails;//this.$refs.grid.data;
        for(let i=0; i < data.length; i++){
            this.useDetails[i].useYn = true
        }
        this.$store.commit('finish')
    },
    allUnChk(){
        //1.
        // let data = this.useDetails;// this.$refs.grid.data;
        // let pageSize = this.config.pagingSize;
        // let curPage = $(".page-num.active")[0].innerText;
        // let c = (curPage-1) * pageSize;

        // for(let i=0; i < data.length; i++){
        //     if(c >= pageSize){
        //         let t = i+c;
        //         if(t<data.length){
        //             this.useDetails[t].useYn = false;
        //         }
                
        //     }else{
        //         if(i < pageSize) this.useDetails[i].useYn = false;
        //     }
        // }
        
        // this.$store.commit('finish')
        //2.
        let data = this.useDetails;//this.$refs.grid.data;
        for(let i=0; i < data.length; i++){
            this.useDetails[i].useYn = false
        }
        this.$store.commit('finish')
    },
    // getCardListCombo(cdlgId){
    //     this.$http.post(`/api/card/list`, {
    //         crdPssrId: cdlgId
    //     }).then(response => {
    //         if(response.data){
    //             this.cardList = response.data;
    //             _.forEach(this.cardList, (o) => {
    //                 o.maskedCrdNo = this.getFormattedPureCardNumber(o.crdNo) + ' ' + o.crdCompNm;
    //             });
    //         }
    //     })
    // },
    // 카드번호
    getCardNoCombo() {
        // this.$http.get(`/api/card/combo`)
        this.$http.get(`/api/card/combo/personal`)
            .then(response => {
                this.cardNos = response.data
                _.forEach(this.cardNos, (o) => {
                    o.maskedCardNo = this.getFormattedCardNumber(o.crdNo);
                    this.form.crdNo = o.crdNo
                })
            });
    },
    sixMonth() {
        // this.$swal('HI')
        this.form.searchDtmFr = this.$moment().add(-6, 'month').startOf('month').format('YYYY-MM-DD')
        // this.goSearch()
    },
    setCardFgCd(){
        //미처리 건에 대해서만 개인용도 처리 한다.
        try {
            let checkedRowIds = this.useDetails.filter(x => x.useYn === true)
            
            assert.apply(this, [checkedRowIds.length > 0, '전표를 선택해주세요.'])
            checkedRowIds.forEach(v => {
                assert.apply(this, [v.useDtlsStatCd === '10', '미처리건에 대해서만 처리 가능합니다.'])
            })
            let checkedUseDtlsNos = [];
            if(checkedRowIds) {
                for(let i = 0; i < checkedRowIds.length; i++){

                    checkedUseDtlsNos.push(checkedRowIds[i].useDtlsNo)
                }
                this.$http.post(`/api/card/private`, checkedUseDtlsNos)
                    .then(response => {
                        this.goSearch();
                    });
            }
        }catch(e) {
             if (e instanceof EvalError) {
                console.log(e.name + ": " + e.message);
            } else if (e instanceof RangeError) {
                console.log(e.name + ": " + e.message);
            } else if (e instanceof TypeError) {
                console.log(e.name + ": " + e.message);
            } else {
                this.$swal({
                    type: 'error',
                    text: e
                })
            }
        }
        
    },
    setDataClose() {
        try{
            
            let datas = this.useDetails.filter(x => {
                if(x.cStateYn === undefined) {
                    if(x.useYn === true) {
                        return x
                    }
                }else{
                    if(x.useYn === true && x.cStateYn === false) {
                        return x
                    }
                }
            })
            // console.log(datas)
            assert.apply(this, [datas.length > 0, '전표를 선택해주세요.'])
            assert.apply(this, [this.form.useDtlsStatCd === '10' || this.form.useDtlsStatCd === undefined
            , '사적비용처리 또는 처리완료 건에 대한 데이터는 전표작성 처리할 수 없습니다.'])
            
            for(let i=0; i < datas.length; i++){
                
                datas[i].detailCd = this.product.detailCd;
                datas[i].detailNm = this.product.detailNm;
                datas[i].expenseId = this.account.expenseId;
                datas[i].expenseName = this.account.expenseName;
                datas[i].expenseAcctCode = this.account.expenseAcctCode;
                datas[i].expenseAcctName = this.account.expenseAcctName;
                datas[i].crdOln = this.form.crdOln;
                datas[i].stptPlcPop = this.form.stptPlcPop;
                datas[i].dstnPlcPop = this.form.dstnPlcPop;
                datas[i].biztrpObjPop = this.form.biztrpObjPop;
                if((datas.length-1) === i) this.$emit('close', datas)
            }
            // assert.apply(this, [datas.length > 0, '추가할 행이 존재하지 않습니다.'])
            // setTimeout(() => {
            //     this.$emit('close', datas)
            // }, 200)
        }catch(e){
            if (e instanceof EvalError) {
                console.log(e.name + ": " + e.message);
            } else if (e instanceof RangeError) {
                console.log(e.name + ": " + e.message);
            } else if (e instanceof TypeError) {
                console.log(e.name + ": " + e.message);
            } else {
                this.$swal({
                    type: 'error',
                    text: e
                })
            }
        }
    },
    closeModal(){
        // this.$parent.close();
        this.$emit('close', undefined);
    }
    
  },
  created(){
      this.useDetails.useYn = false;
  },
  mounted() {
      this.form.compCd = this.$store.state.loginInfo.compCd;
      this.authority = this.$store.state.loginInfo.authorities[0].roleCd;
      this.getCardNoCombo();
      this.getCompCdCombo();
      this.getCardCompCdCombo();
      this.getUseStatCdCombo();
      this.getCardFgCdCombo();
      setTimeout(() => {
          this.goSearch();
        }, 1000);
  },
  watch: {
    copyUseDetails: {
      immediate: true,
      deep: true,
      handler(value) {
        if(value !== undefined){
          this.copyUseDetails = value
        //   this.$refs.grid.lockCell(0, 0)
        }
      }
    }
  }

  // created() {
  //   this.params = _.clone(defaultParams)

  //   /**
  //    * Query SLIP_TYPE_CD
  //    */
  //   this.$http.get('/api/code/detail', {
  //     params: {
  //       groupCd: 'SLIP_TYPE_CD'
  //     }
  //   }).then(response => {
  //     this.options['SLIP_TYPE_CD'] = response.data
  //     this.options['SLIP_TYPE_CD'].unshift({
  //       detailCd: undefined,
  //       detailNm: '==전체=='
  //     })
  //   })

  //   /**
  //    * Query SLIP_STAT_CD
  //    */
  //   this.$http.get('/api/code/detail', {
  //     params: {
  //       groupCd: 'SLIP_STAT_CD'
  //     }
  //   }).then(response => {
  //     this.options['SLIP_STAT_CD'] = response.data.filter(x => x.remark1 === 'Y' && x.detailCd >= '50')
  //     this.options['SLIP_STAT_CD'].unshift({
  //       detailCd: undefined,
  //       detailNm: '==전체=='
  //     })
  //   })

  //   // this.query()
  // }
};
</script>

<style scoped>
div#gridbox {
  width: 100%;
  height: 100%;
  min-height: 300px;
}
div.modal-card {
  width: 100%;
}


</style>
