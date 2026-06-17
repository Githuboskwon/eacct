<template>
    <layout>
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
  
        <div slot="content">
            <div class="content-name">
                <!-- <div class="btn-wrap btn-type1 clearfix fl_right" >
    
                        </div> -->
                <div class="btn-wrap btn-type1 clearfix">
                    <!-- <button class="btn-size btn-blue fl_right" @click="goSearch"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span> 조회</button> -->
                    <!-- <div class="dp-ib fl_right">
                        <button class="btn-size btn-gray fl_right" style="height: 33px; margin-right: 7px;" @click="allUnChk"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span> 전체해제</button>
                    </div>
                    <div class="dp-ib fl_right">
                        <button class="btn-size btn-blue fl_right" style="height: 33px; margin-right: 7px;" @click="allChk"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span>전체선택</button>
                    </div> -->
                    <div class="dp-ib fl_right">
                        <button class="btn-size btn-orange fl_right" style="height: 33px; margin-right: 7px;" @click="setCardFgCd"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span>개인용도</button>
                    </div>
                    <div class="dp-ib fl_right">
                        <button class="btn-size btn-blue fl_right" style="height: 33px; margin-right: 7px;" @click="setDataClose"><span class="btn-icon"><i class="fas fa-pen-alt"></i></span>전표작성</button>
                    </div>
    
                </div>
            </div>
            <!-- 검색조건 영역 -->
            <el-form label-position="right" class="desc-content search-border">
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="사용일자" label-width="100px">
                            <el-date-picker
                                v-model="form.searchDtm"
                                type="daterange"
                                align="left"
                                range-separator="~"
                                unlink-panels
                                format="YYYYMMDD"
                                start-placeholder="시작일"
                                end-placeholder="종료일"
                                style="float: left; width: 100%;">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="카드번호" label-width="90px">
                            <el-select class="select is-fullwidth" v-model="form.crdNo">
                                <el-option 
                                    v-for="item in cardNos"
                                    :key="item.crdNo"
                                    :value="item.crdNo"
                                    :label="`${item.maskedCardNo} ${item.crdOln ? `(${item.crdOln})` : ''}`">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="진행상태" label-width="100px">
                            <el-select class="select is-fullwidth" v-model="form.statusCd">
                                <el-option 
                                    v-for="item in statusCds"
                                    :key="item.key"
                                    :value="item.key"
                                    :label="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item label="취소구분" label-width="100px">
                            <el-select class="select is-fullwidth" v-model="form.cancelCd">
                              <el-option :key="''" :value="''" :label="'전체'"></el-option>
                              <el-option
                                    v-for="item in cancelCds"
                                    :key="item.key"
                                    :value="item.key"
                                    :label="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="2">
                        <el-button style="float: right;" type="primary" icon="el-icon-search" @click="goSearch">
                            조회
                        </el-button>
                    </el-col>
                </el-row>
            </el-form>
            <!-- 테이블 -->
            <div class="table-area">
                <div class="table-a fixed">
                    <table class="table">
                        <tbody>
                            <tr>
                                <th width="100" style="text-align: center;">계정명</th>
                                <td width="33%">
                                    <div class="ip-box" style="width: 30%; margin-right: 5px;">
                                      <el-input class="mr3" type="text" v-model="account.acctCd"  disabled ></el-input>
                                    </div>
                                    <div class="ip-box" style="width:68%;">
                                        <el-input type="text"  v-model="account.acctNm" @keypress.native.enter="acctEnterPop(account.acctNm)">
                                            <i class="el-icon-search el-input__icon" slot="suffix" @click="acctPop"></i>
                                        </el-input>
                                    </div>
                                </td>
                                
                                <th width="100" style="text-align: center;">적요</th>
                                <td colspan="10">
                                    <el-input class="mr3" type="text" v-model="form.crdOln" clearable></el-input>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="table-b">
                    <div class="grid-wrap">
                        <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height_p1"
                            :columnDefs="columnDefs"
                            :rowData="useDetails"
                            :gridOptions="gridOptions"
                            :frameworkComponents="frameworkComponent"
                            :rowSelection="rowSelection"
                            :suppressRowClickSelection="true"
                            :defaultColDef="defaultColDef"
                            @row-selected="onRowSelected"
                            @row-double-clicked="rowDoubleClick"
                            :statusBar="statusBar"
                            @grid-ready="onReady"/>
                    </div>
                </div>
            </div>
            <!-- //테이블 -->
        </div>
    </layout>
</template>
  
<script>
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import _ from 'lodash'

import assert from '@/libs/assert'
import Layout from '@/components/ModalSlot4.vue'

import AccountModal from '@/components/accrualSlip/GridModal/AccountModal'; /** 계정과목 조회 */
import CardInfoDetailPop from '@/components/CardInfoDetailPop.vue'; /** 카드정보 */

import { AgGridVue } from 'ag-grid-vue'
  
  import {
    url as _url
  } from '@/libs/join'
  
  export default {
    name: 'SlipCrdLst',
    props: {
        params: {
            type: Object,
            required: true
        },
        copyUseDetails: {
            type: Array,
            required: false
        },
        cardType: {
            type: String,
            required: true,
            default: 'A' //항공권 외
        },
        options: {
            type: Object,
            required: false,
        }
    },
    components: {Layout, AgGridVue},
    mixins: [ mixin, mixinSlip ],
    data() {
        return {
            columnDefs : [],
            gridApi : null,
            columnApi : null,
            gridOptions : {
                enableColResize: true,
                enableFilter: true,
                animateRows: false,
                enableSorting: true,
            },
            frameworkComponent : {
                // 'checkBox' : AgGridCheckbox
            },
            defaultColDef:{
                flex: 1,
                minWidth: 100,
                resizable: true,
                headerCheckboxSelection: false,
                checkboxSelection: false,
                filter: true,
                sortable: true,
            },
            title: '법인카드사용내역',
            statusCds: [], //진행상태 코드
            cancelCds: [], //취소구분 코드
            useDetails: [],
            cardNos: [],
            account: {
                acctCd: '',
                acctNm: '',
                acctType: '',
                acctDffCnt: '',
                acctRequiredFlagCnt: '',
                drCr: '',
                attributeCategory: '',
                attributeCategoryName: '없음',
                assetsTrackingFlag: 'N'
            },
            
            form: {
                statusCd: 'R1',
                cancelCd: '',
                crdNo: '',
                crdPssrId: '',
                crdPssrNm: '',
                searchDtm: [this.$moment().add(-6, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')],
                erpSlipFlag: 'N'
            },
            rowSelection: null,
            statusBar: null,
        }
    },
    async created(){
        /**
         * 조회조건 공통코드 추가했습니다~
         *  CARD_STATUS_CD : 카드상태코드
         * CARD_CANCEL_YN : 카드취소여부
         */
        const [status, cancel] = await Promise.all([
            this.$http.get('/api/code/combo', { params: { groupCd: 'CARD_STATUS_CD' } }),
            this.$http.get('/api/code/combo', { params: { groupCd: 'CARD_CANCEL_YN' } })
        ]);
        this.statusCds = status[`data`];
        this.cancelCds = cancel[`data`];
        
        //회계년월을 받아왔다면 셋업 (메인 대시보드에서 넘어올때) 그렇지 않다면 디폴트 ? 언제부터 셋업하지..
        this.form.searchDtm = [this.options?.localMonthDate ? this.$moment(this.options?.localMonthDate).format('YYYY-MM-DD HH:mm:ss') : this.$moment().add(-3, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss') , this.options?.nextMonthDate ? this.$moment(this.options?.nextMonthDate).format('YYYY-MM-DD HH:mm:ss') : this.$moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')];
        
    },
    beforeMount(){
        const that = this;

        this.rowSelection = 'multiple';

        this.statusBar = {
            statusPanels: [
                { statusPanel: 'agTotalAndFilteredRowCountComponent', align: 'left' },
                { statusPanel: 'agTotalRowCountComponent', align: 'center' },
                { statusPanel: 'agFilteredRowCountComponent' },
                { statusPanel: 'agSelectedRowCountComponent' },
                { statusPanel: 'agAggregationComponent',
                    // statusPanelParams: { aggFuncs: ['count', 'sum'] },
                },
            ],
        }

        this.makeColDef();
        this.defaultColDef = { resizable: true, filter:true, sortable: true };

        this.gridOptions = {
            onFilterChanged: () => {
                that.allUnChk();
            },
            getRowStyle: (params) => {
                if(params.data.cStateYn){
                    return {background: 'orange'};
                }
            }
        }
    },
    mounted() {

        this.getCardNoCombo()
        .then(data => {
            if(!!data && data.length > 0) {
                this.cardNos = data.map(card => {
                    card.maskedCardNo = this.getFormattedCardNumber(card.crdNo);
                    if(this.params.empNm === card.crdOln) {
                        this.form.crdNo = card.crdNo;
                    } else {
                        this.form.crdNo = card.crdNo;
                    }
                    return card;
                });
            } else {
                const tempCrdNo = "xxxx-xxxx-xxxx-xxxx";
                this.form.crdNo = this.getFormattedCardNumber(tempCrdNo)
            }
        })
        .finally(_ => {
            this.goSearch();
        });
    },
    methods: {
        onReady(params) {
            this.gridApi = params.api;
            this.columnApi = params.columnApi;
        },
        onRowSelected(params) {
            const rowIndex = params.rowIndex;
            
        },
        rowDoubleClick(params) {
            const {apprNo, cardNo} = params.data;
            
            this.$modal.open({
                component: CardInfoDetailPop,
                parent: this,
                props: {
                    apprNo: apprNo,
                    crdNo: cardNo
                },
                width: 700
            });
            
        },
        makeColDef(){
            const that = this;
            this.columnDefs = [
                {
                    headerName: '', 
                    headerClass: 'ag-center-header',
                    headerCheckboxSelection: true, 
                    headerStyle: {'display': 'flex', 'justify-content': 'center'}, 
                    field: 'chk', 
                    width: 50, 
                    filter: false,
                    sortable: false,
                    suppressMenu: true, 
                    cellStyle:{ 'item-align': 'center', 'color' : 'transparent'},
                    checkboxSelection: (params) => {
                        return !params.data.cStateYn
                    },
                    colSpan:(params) => {
                        if(params.data.rn === '합계'){
                            return 8;
                        }else{
                            return 1;
                        }
                    },
                    cellStyle:(params) => {
                        if(params.data.rn === '합계'){
                            return {textAlign: 'center'}
                        }else{
                            return {textAlign: 'left'}
                        }
                    },
                    valueFormatter: (params) => {
                        if(params.data.rn === '합계'){
                            return `합계`;
                        }else{
                            return params.value;
                        }
                    },
                },
                {headerName: '사용일자', headerClass: 'ag-center-header', field: 'usedDt', width: 120, cellStyle:{textAlign: 'left'}},
                {headerName: '매입일자', headerClass: 'ag-center-header', field: 'buyDt', width: 120, cellStyle:{textAlign: 'left'}},
                {headerName: '진행상태', headerClass: 'ag-center-header', field: 'statusText', width: 140, cellStyle:{textAlign: 'center'}},
                {headerName: '상호', headerClass: 'ag-center-header', field: 'storeNm', width: 200, cellStyle:{textAlign: 'left'}},
                {headerName: '업종', headerClass: 'ag-center-header', field: 'mccNm', width: 140, cellStyle:{textAlign: 'left'}},
                {headerName: '가맹점주소', headerClass: 'ag-center-header', field: 'storeAddr', width: 180, cellStyle:{textAlign: 'left'}},
                {headerName: '통화', headerClass: 'ag-center-header', field: 'usedCur', width: 80, cellStyle:{textAlign: 'center'}},
                {
                    headerName: '사용금액(₩)',
                    field: 'usedAmt',
                    headerClass: 'ag-center-header', 
                    width: 140,
                    cellStyle:{textAlign: 'right'},
                    valueFormatter: function(params) {
                        return that.getNumberFormat(params.value);
                    }
                },
                {
                    headerName: '외화금액',
                    field: 'usedForAmt',
                    headerClass: 'ag-center-header', 
                    width: 140,
                    cellStyle:{textAlign: 'right'},
                    valueFormatter: function(params) {
                        return that.getNumberFormat(params.value);
                    }
                },
                {headerName: '과세유형', field: 'taxType', width: 110, cellStyle:{textAlign: 'center'}},
                {
                    headerName: '공급가액',
                    field: 'originAmt',
                    headerClass: 'ag-center-header', 
                    width: 140,
                    cellStyle:{textAlign: 'right'},
                    valueFormatter: function(params) {
                        return that.getNumberFormat(params.value);
                    }
                },
                {
                    headerName: '부가세액',
                    field: 'surtax',
                    headerClass: 'ag-center-header', 
                    width: 100,
                    cellStyle:{textAlign: 'right'},
                    valueFormatter: function(params) {
                        return that.getNumberFormat(params.value);
                    }
                },
                {
                    headerName: '봉사료/환가료',
                    field: 'serviceCharge',
                    headerClass: 'ag-center-header', 
                    width: 100,
                    cellStyle:{textAlign: 'right'},
                    valueFormatter: function(params) {
                        return that.getNumberFormat(params.value);
                    }
                },
                {headerName: '사용자', headerClass: 'ag-center-header', field: 'userNm', width: 140, cellStyle:{textAlign: 'center'}},
                {headerName: '항공권 발권자', headerClass: 'ag-center-header', field: 'userNm', width: 140, cellStyle:{textAlign: 'center'}},
                {headerName: '취소구분', headerClass: 'ag-center-header', field: 'cancelFlagText', width: 110, cellStyle:{textAlign: 'center'}},
                {headerName: '승인번호', headerClass: 'ag-center-header', field: 'apprNo', width: 140, cellStyle:{textAlign: 'center'}},
                {headerName: '카드번호', headerClass: 'ag-center-header', field: 'cardNo', width: 180, cellStyle:{textAlign: 'left'}},
                {headerName: '사업자번호', headerClass: 'ag-center-header', field: 'irsNo', width: 200, cellStyle:{textAlign: 'left'}},
                {headerName: '부서명', headerClass: 'ag-center-header', field: 'deptNm', width: 150, cellStyle:{textAlign: 'left'}},
                {headerName: '전표번호', headerClass: 'ag-center-header', field: 'slipNo', width: 150, cellStyle:{textAlign: 'left'}},
                {headerName: '리뷰아이템', headerClass: 'ag-center-header', field: 'reviewItem', width: 120, cellStyle:{textAlign: 'center'}},
                {headerName: '카드사명', headerClass: 'ag-center-header', field: 'cardComNm', width: 120, cellStyle:{textAlign: 'center'}},
                {headerName: 'TAX코드', headerClass: 'ag-center-header', field: 'taxCode', width: 120, cellStyle:{textAlign: 'center'}},
            ];
        },

        /**
         * 발생전표 작성 법인카드 팝업
         * @param cardUseListDto
         * ledgerId : ?? (필수)
         * taxEvidenceType : ?? (필수)
         * compCd : 회사구분코드(필수0
         * slipHeaderId : 전표헤더ID (필수)
         * cardNo : 카드번호(필수)
         * userId : 사용자 (필수)
         * searchDtmFr : 사용시작일자
         * searchDtmTo : 사용종료일자
         * status : 진행상태 (개인용도 : 07, 미처리 : 01)
         * cancelFlag : 취소여부 (취소 : Y, 승인 : N)
         * cardType : F(항공권), A(항공권 내역 제외 )
         * */
        goSearch() {

            this.form.erpSlipFlag = ['MCARD', 'ACARD', 'CARD'].includes(this.params.slipTypeCd) ? 'Y' : 'N';

            const param = {
                ledgerId: this.params.ledgerId,
                taxEvidenceType: this.params.taxEvidenceType,
                slipHeaderId: this.params.slipHeaderId,
                cardNo: this.cardType === 'F' ? '' : this.form.crdNo,
                userId: this.params.userId,
                searchDtmFr: this.$moment(this.form.searchDtm[0]).format('YYYY-MM-DD'),
                searchDtmTo: this.$moment(this.form.searchDtm[1]).format('YYYY-MM-DD'),
                status: this.form.statusCd,
                cancelFlag: this.form.cancelCd,
                erpSlipFlag: this.form.erpSlipFlag,
                cardType: this.cardType
            }
            const stripFields = ['searchDtmFr', 'searchDtmTo','crdNo'];
            _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));

            this.$store.commit('loading');
            this.$http.post(`/api/card/popup/useList`, param)
            .then(response => {
                if (response.data) {
                    let data = response.data
                    for(let i = 0; i < response.data.length; i++) {
                        for(let k = 0; k < this.copyUseDetails.length; k++) {
                            if(this.copyUseDetails[k].globalAttribute14 === data[i].apprNo){
                                data[i].cStateYn = true
                            }
                        }
                    }


                    for(let i=0; i<data.length; i++){
                        data[i].cardNo = data[i].cardNo;
                        // data[i].cardNo = this.getFormattedCardNumber(data[i].cardNo);
                        data[i].apprDate = this.$moment(data[i].apprDate).format('YYYY-MM-DD');
                        if(!_.isEmpty(data[i].apprTime)){
                            data[i].apprTime = this.$moment(data[i].apprTime, 'HHmmss').format('HH:mm:ss');
                        }
                        if(!_.isEmpty(data[i].erpSlipDtm)){
                            data[i].erpSlipDtm = this.$moment(data[i].erpSlipDtm).format('YYYY-MM-DD HH:mm:ss');
                        }
                    }

                    this.useDetails = data;
                }
            }).finally(()=> {
                this.$store.commit('finish');
            });
        },
        // 카드번호
        getCardNoCombo() {
            return new Promise((resolve, reject) => {
                const params = {
                    crdPssrId: this.cardType === 'F' ? null: this.params.userId,
                    crdFgCd: this.cardType,
                    crdStatCd : 'Y', // close한 카드는 보이지 않도록 변경
                    page: 0,
                    limit: 100,
                }
                this.$http.post(`/api/card/pop/list`, params)
                .then(res => res.data)
                .then(data => {
                    resolve(data);
                })
                .catch(err => reject(err));
            });
        },
        setCardFgCd(){
            //미처리 건에 대해서만 개인용도 처리 한다.
            try {
                let checkedRowIds = this.gridApi.getSelectedNodes().map(x => x.data);

                assert.apply(this, [checkedRowIds.length > 0, '전표를 선택해주세요.'])
                checkedRowIds.forEach(v => {
                    assert.apply(this, [v.status === '01', '미처리건에 대해서만 처리 가능합니다.'])
                })
                
                this.$http.post(`/api/card/useList/change`, checkedRowIds)
                .then(response => {
                    this.$swal({type: 'success',text:'개인용도로 처리되었습니다.'})
                    this.goSearch();
                });
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
            try {

                let datas = this.gridApi.getSelectedNodes().map(x => x.data).filter(f => !f.cStateYn);
                console.log(datas)
                assert.apply(this, [datas.length > 0, '전표를 선택해주세요.'])
                // assert.apply(this, [this.form.statusCd === '01' || this.form.statusCd === undefined , '사적비용처리 또는 처리완료 건에 대한 데이터는 전표작성 처리할 수 없습니다.'])

                for(let i=0; i < datas.length; i++){

                    datas[i].acctCd = this.account.acctCd; //계정코드
                    datas[i].acctNm = this.account.acctNm; //계정명
                    datas[i].acctType = this.account.acctType; //계정유형
                    datas[i].acctDffCnt = this.account.acctDffCnt;
                    datas[i].acctRequiredFlagCnt = this.account.acctRequiredFlagCnt;
                    datas[i].drCr = this.account.drCr;
                    datas[i].attributeCategory = this.account.attributeCategory;
                    datas[i].attributeCategoryName = this.account.attributeCategoryName;
                    datas[i].assetsTrackingFlag = this.account.assetsTrackingFlag;
                    datas[i].crdOln = this.form.crdOln; //적요

                    /* 메세지 분기 처리 */
                    if(datas[i].status === 'RU') {
                      assert.apply(this, [datas[i].status !== 'RU' , '미처리(반려)인 경우 반려처리된 전표내역에서 재사용하시기 바랍니다.']);
                    }else
                    if(datas[i].status === 'SV') {
                      assert.apply(this, [datas[i].status !== 'SV' , '작성중인 카드 내역은 전표작성 처리 할 수 없습니다.']);
                    }else{
                      assert.apply(this, [datas[i].status === '01' || datas[i].status === undefined , '개인용도 카드내역은 전표작성 처리 할 수 없습니다.']);
                    }

                    if((datas.length-1) === i) this.$emit('close', datas)
                }
            } catch(e){
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
        },
        /**
         * 계정과목 Enter Popup
         */
        acctEnterPop(acctNm) {
            if(!acctNm) {
                this.initialize(`initAcct`);
                return false;
            }
            const trxTypeCd = this.params.trxTypeCode; //거래유형 (필수)
            const acctModule = this.params.ttypeInterfaceModule; // 거래유형에서? 매출, 매입 유형 (필수)
            const deptType = this.params.deptType || ''; //귀속부서유형 (제조: M, 판관: S, 공통: 그 외(C?) (선택)
            const attribute3 = 'form';
            const self = this;
            const acctPop = (acctNm) => {
                if(!trxTypeCd) {
                    this.$message({ type: `warning`, message: `거래유형 미지정 상태 입니다.` });
                    return false;
                }
                this.$modal.open({
                    component: AccountModal,
                    parent: this,
                    props: {
                        trxTypeCd,
                        acctModule,
                        deptType,
                        schTxt: acctNm,
                        attribute3
                    },
                    width: 1000,
                    events: {
                        close(account) {
                            const {acctCd, acctNm, acctType, dffCnt, requiredFlagCnt, drCr, assetsTrackingFlag} = account;
                            self.account.acctCd = acctCd;
                            self.account.acctNm = acctNm;
                            self.account.acctType = acctType;
                            self.account.acctDffCnt = dffCnt || 0;
                            self.account.acctRequiredFlagCnt = requiredFlagCnt;
                            self.account.drCr = drCr;
                            self.account.attributeCategory = '';
                            if(!dffCnt) {
                                self.account.attributeCategoryName = '없음';
                            } else {
                                self.account.attributeCategoryName = '입력하세요';
                            }
                            self.account.assetsTrackingFlag = assetsTrackingFlag;
                        }
                    } 
                });
            }
            const params = {
                trxTypeCd,
                acctModule,
                deptType,
                acctNm,
                attribute3
            }
            this.$store.commit('loading');
            this.$http.post(`/api/account/slip/list`, params)
            .then(res => res.data)
            .then(data => {
                if(data.length === 1) {
                    const {acctCd, acctNm, dffCnt, requiredFlagCnt, drCr, assetsTrackingFlag} = data[0];
                    this.account.acctCd = acctCd;
                    this.account.acctNm = acctNm;
                    
                    this.account.acctDffCnt = dffCnt;
                    this.account.acctRequiredFlagCnt = requiredFlagCnt;
                    this.account.drCr = drCr;
                    this.account.attributeCategory = '';
                    if(!dffCnt) {
                        this.account.attributeCategoryName = '없음';
                    } else {
                        this.account.attributeCategoryName = '입력하세요';
                    }
                    this.account.assetsTrackingFlag = assetsTrackingFlag;
                } else {
                    acctPop(acctNm);
                }
            })
            .finally(() => {
                this.$store.commit('finish');
            });
        },
        /**
         * 계정과목 Popup
         */
        acctPop() {
            const trxTypeCd = this.params.trxTypeCode; //거래유형 (필수)
            const acctModule = this.params.ttypeInterfaceModule; // 거래유형에서? 매출, 매입 유형 (필수)
            const deptType = this.params.deptType || ''; //귀속부서유형 (제조: M, 판관: S, 공통: 그 외(C?) (선택)
            const attribute3 = 'form';

            const self = this;
            this.$modal.open({
                component: AccountModal,
                parent: this,
                props: {
                    trxTypeCd,
                    acctModule,
                    deptType,
                    attribute3
                },
                width: 1000,
                events: {
                    close(account) {
                        const {acctCd, acctNm, dffCnt, requiredFlagCnt, drCr, assetsTrackingFlag} = account;
                        self.account.acctCd = acctCd;
                        self.account.acctNm = acctNm;

                        self.account.acctDffCnt = dffCnt;
                        self.account.acctRequiredFlagCnt = requiredFlagCnt;
                        self.account.drCr = drCr;
                        self.account.attributeCategory = '';
                        if(!dffCnt) {
                            self.account.attributeCategoryName = '없음';
                        } else {
                            self.account.attributeCategoryName = '입력하세요';
                        }
                        self.account.assetsTrackingFlag = assetsTrackingFlag;
                    }
                } 
            });
        },
        /**
         * 초기화 함수
         * @param {*} target 
         */
        initialize(target) {
            const self = this;
            const func = {
                initAcct() {
                    self.account.acctCd = '';
                    self.account.acctNm = '';
                },
                all() {
                    this.initAcct();
                }
            }
            return new Promise((resolve) => {
                resolve(func[target]());
            });
        },
    },
    
    watch: {
        copyUseDetails: {
            immediate: true,
            deep: true,
            handler(value) {
                if(value !== undefined){
                    this.copyUseDetails = value
                }
            }
        },
    }
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
