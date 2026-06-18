<template>
  <div class="inner-box">
    <div class="content-name">
      <h2 class="dp-ib">{{title}}</h2>
      <div class="btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="goFirst">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-download" @click="downloadExcel">엑셀 다운로드</el-button>
      </div>
    </div>

    <!-- 검색조건 영역 -->
    <div class="desc-content search-border">
      <div class="item desc-left">
        <div class="desc-item">
          <div class="tit">
            <span class="label-tit">- 거래처</span>
          </div>

          <div class="desc">
            <div class="td-s-thumb search-area" style="width: 300px;">
              <input class="input input-bg" type="text" style="width:80px;" v-model="search.integrationVendorNum" disabled>
              <div class="ip-box ip-box-w02" style="width:140px;">
                <input class="input" type="text" v-model="search.integrationVendorName" @input="initVendor" @keypress.enter="popVendor">
                <button class="icon is-right" @click="popVendor(true)"><i class="fas fa-search"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>


      <div class="item desc-center">
        <div class="search_box">
          <button class="item-list icon is-right btn_s_w" @click="openModal()" type="button">상세검색<i class="fas fa-plus"></i></button>
        </div>

        <!-- 상세검색 내용 -->
        <div id="open-moda" class="modal-window">
          <div class="modal-window-wrap">
            <div class="modal-window-top">
              <h4>상세검색</h4>
              <button title="Close" @click="closeModal()" type="button" class="bt-modal-close mt5"><img src="../../public/img/bt_close_w.png" /></button>
            </div>

            <div class="search_box_wrap">
              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 국가구분</span>
                </div>
                <div class="search_con search-area">
                  <input class="input dp-ib w40p_5r" type="text" v-model="search.benCountryCd" disabled>
                  <div class="dp-ib w60p">
                    <input class="input" type="text" v-model="search.benCountry" @input="initBenCountry" @keypress.enter="popBenCountry">
                    <button class="icon is-right" @click="popBenCountry(true)"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </div>


              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 사업자여부</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth" v-model="search.bizTypeName">
                    <option value="">전체</option>
                    <option value="사업자">사업자</option>
                    <option value="비사업자">비사업자</option>
                  </select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 거래처유형</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth" v-model="search.venPayGroupLookupCd">
                    <option value="">전체</option>
                    <option value="관공서">관공서</option>
                    <option value="국내업체">국내업체</option>
                    <option value="금융기관">금융기관</option>
                    <option value="동반성장업체">동반성장업체</option>
                    <option value="세관">세관</option>
                    <option value="임직원">임직원</option>
                    <option value="해외업체">해외업체</option>
                  </select>
                </div>
              </div>


              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 시도구분</span>
                </div>
                <div class="search_con search-area">
                  <select class="select is-fullwidth" v-model="search.regionName">
                    <option value="">전체</option>
                    <option value="서울">서울</option>
                    <option value="부산">부산</option>
                    <option value="대구">대구</option>
                    <option value="인천">인천</option>
                    <option value="광주">광주</option>
                    <option value="대전">대전</option>
                    <option value="울산">울산</option>
                    <option value="세종">세종</option>
                    <option value="경기">경기</option>
                    <option value="강원">강원</option>
                    <option value="충북">충북</option>
                    <option value="충남">충남</option>
                    <option value="전북">전북</option>
                    <option value="전남">전남</option>
                    <option value="경북">경북</option>
                    <option value="경남">경남</option>
                    <option value="제주">제주</option>
                    <option value="해외">해외</option>
                  </select>
                </div>
              </div>

              <div class="search_box_pop">
                <div class="search_title">
                  <span class="search_tit">- 사업자번호</span>
                </div>
                <div class="search_con search-area">
                  <div class="td-s-thumb search-area" style="width: 100%">
                    <input class="input" type="text" v-model="search.vatRegistrationNum">
                  </div>
                </div>
              </div>

            </div>

            <div class="modal-window-bottom">
              <ul>
                <li>
                  <button class="bt_blue_s" @click="goSearch">검색</button>
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
    </div>
    <!-- //검색조건 영역 -->

    <!-- 테이블 -->
    <div class="table-area">
      <div class="table-b">
        <div class="table-header">
          <div class="table-name">
            <h3 class="ico_table_name">거래처 목록</h3>
          </div>
<!--          <div class="btn-wrap btn-type1 clearfix">
            <button class="btn-size btn-excel" @click="saveExcel">엑셀 저장</button>
          </div>-->
        </div>

        <div class="grid-tbl-box">

          <ag-grid-vue
            ref="grid"
            style="width: 100%; height: 280px;"
            class="ag-theme-alpine"
            rowSelection="single"

            :columnDefs="columnDefs"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponents"
            :rowData="vendorList"
            :gridOptions="gridOptions"
            :suppressRowClickSelection="false"
            :enableRangeSelection="false"
            @grid-ready="onReady"
            @selection-changed="onSelectionChanged"
          />
        </div>

        <div class="pagingbox">
            <button class="btn-page page-first" @click="goFirst" :disabled="page.page==0 || page.count==0"></button>
            <button class="btn-page page-prev" @click="goPre" :disabled="page.page==0 || page.count==0"></button>

            <button :class="page.page==item.value-1 ? 'page-num active' : 'page-num'"
              v-for="item in page.pageList"
              :value="item.value"
              @click="goPage(item.value)">{{item.value}}</button>

            <button class="btn-page page-next" @click="goNext" :disabled="page.page==page.lastPage || page.count==0"></button>
            <button class="btn-page page-last" @click="goEnd" :disabled="page.page==page.lastPage || page.count==0"></button>
        </div>


        <!-- 상세정보 -->
        <div>
          <div class="table-a fixed-th">
            <div class="table-name">
              <h3 class="ico_table_name">기본 정보</h3>
            </div>
            <table id="basic" class="table">
              <colgroup>
                <col width="10%"><col width="15%"><col width="10%"><col width="15%"><col width="10%"><col width="15%"><col width="10%"><col width="15%">
              </colgroup>
              <tbody>
              <tr>
                <th>거래처 코드/명</th>
                <td>
                  <input class="input" style="width:75px;" type="text" v-model="form.integrationVendorNum" readonly/>&nbsp;
                  <input class="input" style="width:140px;" type="text" v-model="form.integrationVendorName" readonly/>
                </td>

                <th>고유번호</th>
                <td>
                  <input class="input" type="text" v-model="form.vatRegistrationNum" readonly/>
                </td>

                <th>사업자여부</th>
                <td>
                  <input class="input" type="text" v-model="form.bizTypeName" readonly/>
                </td>

                <th>국가구분</th>
                <td>
                  <input class="input" type="text" v-model="form.territoryCode" readonly/>
                </td>
              </tr>
              <tr>
                <th>시도구분</th>
                <td>
                  <input class="input" type="text" v-model="form.regionName" readonly/>
                </td>

                <th>우편번호</th>
                <td>
                  <input class="input" type="text" v-model="form.venZip" readonly/>
                </td>

                <th>주소</th>
                <td colspan="3">
                  <input class="input" type="text" v-model="form.address" readonly/>
                </td>
              </tr>
              <tr>
                <th>전화번호</th>
                <td>
                  <input class="input" type="text" v-model="form.venPhone" readonly/>
                </td>

                <th>이메일</th>
                <td>
                  <input class="input" type="text" v-model="form.email" readonly/>
                </td>

                <td colspan="4">

                </td>
              </tr>
              </tbody>
            </table>
          </div>


          <div class="table-a fixed-th">
            <div class="table-name">
              <h3 class="ico_table_name">고객 정보</h3>
            </div>
            <table id="basic" class="table">
              <colgroup>
                <col width="10%"><col width="15%"><col width="10%"><col width="15%"><col width="10%"><col width="15%"><col width="10%"><col width="15%">
              </colgroup>
              <tbody>
              <tr>
                <th>고객번호</th>
                <td>
                  <input class="input" type="text" v-model="form.customerNum" readonly/>
                </td>

                <th>고객구분</th>
                <td>
                  <input class="input" type="text" v-model="form.custCategoryCode" readonly/>
                </td>

                <th>사용가능</th>
                <td>
                  <input class="input" type="text" v-model="form.custEnabledFlag" readonly/>
                </td>

                <th>수금조건</th>
                <td>
                  <input class="input" type="text" v-model="form.custPaymentDescription" readonly/>
                </td>
              </tr>
              <tr>
                <th>담보설정대상</th>
                <td>
                  <input class="input" type="text" v-model="form.custDamboFlag" readonly/>
                </td>

                <td colspan="6">

                </td>
              </tr>
              </tbody>
            </table>
          </div>


          <div class="table-a fixed-th">
            <div class="table-name">
              <h3 class="ico_table_name">공급자 정보</h3>
            </div>
            <table id="basic" class="table">
              <colgroup>
                <col width="10%"><col width="15%"><col width="10%"><col width="15%"><col width="10%"><col width="15%"><col width="10%"><col width="15%">
              </colgroup>
              <tbody>
              <tr>
                <th>공급자번호</th>
                <td>
                  <input class="input" type="text" v-model="form.vendorNum" readonly/>
                </td>

                <th>지급그룹</th>
                <td>
                  <input class="input" type="text" v-model="form.venPayGroupLookupCd" readonly/>
                </td>

                <th>지급조건</th>
                <td>
                  <input class="input" type="text" v-model="form.venPaymentDescription" readonly/>
                </td>

                <th>사용가능</th>
                <td>
                  <input class="input" type="text" v-model="form.venEnabledFlag" readonly/>
                </td>
              </tr>

              <tr>
                <th>대표자명</th>
                <td>
                  <input class="input" type="text" v-model="form.venRepresontativeName" readonly/>
                </td>

                <td colspan="6">
                </td>
              </tr>
              </tbody>
            </table>
          </div>


          <div class="table-header">
            <div class="table-name">
              <h3 class="ico_table_name">지급정보</h3>
            </div>
          </div>

          <div class="grid-tbl-box">
            <ag-grid-vue
              ref="bankGrid"
              style="width: 100%; height: 200px;"
              class="ag-theme-alpine"
              rowSelection="single"

              :columnDefs="columnDefsBank"
              :defaultColDef="defaultColDefBank"
              :frameworkComponents="frameworkComponents"
              :rowData="bankList"
              :gridOptions="gridOptionsBank"
              :suppressRowClickSelection="false"
              :enableRangeSelection="false"
              @grid-ready="onReadyBank"
            />
          </div>

        </div>

        <div class="grid-tbl-box">

          <ag-grid-vue
            ref="excel"
            class="ag-theme-alpine"
            rowSelection="single"

            :columnDefs="columnDefsExcel"
            :rowData="vendorListExcel"
            :gridOptions="gridOptionsExcel"
          />
        </div>

      </div>
    </div>
    <!-- //테이블 -->

    <!--팝업-->
    <!-- <b-modal :active.sync="showEmpModal" has-modal-card @receive="receiveEmp">
        <emp :param="form.crdPssrNm"></emp>
    </b-modal> -->
    <!--//팝업 -->
  </div>
</template>

<script>
import {AgGridVue} from 'ag-grid-vue'
import mixin from '@/mixin';
import mixinSlip from '@/mixin/slip';
import _ from 'lodash'
import Vendor from "@/components/Vendor_Ag";
import BenCountryPop from '@/components/BenCountryPop.vue';

export default {
  compatConfig: { MODE: 2 },
  name: 'VendorLst',
  mixins: [mixin, mixinSlip],
  components: {
    AgGridVue,
    Vendor,
    BenCountryPop,
  },
  data() {
    return {
      title: '거래처 조회',
      compCds: [],
      vendorList: [],
      bankList: [],
      vendorListExcel: [],
      columnDefs: [],
      columnDefsBank: [],
      columnDefsExcel: [],
      defaultColDef: {},
      defaultColDefBank: {},
      frameworkComponents: {},
      gridOptions: {},
      gridOptionsBank:{},
      gridOptionsExcel:{},
      page:{
        page: 0,
        limit: 100,
        count: 0,
        lastPage: 0,
        pageList: []
      },
      authority: '',
      search: {
        integrationVendorNum: '',
        integrationVendorName: '',
        benCountry: '',
        benCountryCd: '',
        bizTypeName: '',
        venPayGroupLookupCd: '',
        regionName: '',
        vatRegistrationNum: ''
      },
      initPage: false,
      form: {
        integrationVendorNum: '',
        integrationVendorName: '',
        vatRegistrationNum: '',
        bizTypeName: '',
        territoryCode: '',
        regionName: '',
        venZip: '',
        address: '',
        venPhone: '',
        email: '',
        customerNum: '',
        custCategoryCode: '',
        custEnabledFlag: '',
        custPaymentDescription: '',
        custDamboFlagName: '',
        vendorNum: '',
        venPayGroupLookupCd: '',
        venPaymentDescription: '',
        venEnabledFlag: '',
        venRepresontativeName: '',
      },
    };
  },
  methods: {
    makeColDef(){
      const that = this
      this.columnDefs = [
        { headerName: 'No.',
          field: 'rn',
          width: 80,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return that.page.page*100 + params.node.rowIndex + 1;
          }
        },
        {headerName: '통합거래처코드', field: 'integrationVendorNum', width: 130},
        {headerName: '거래처명', field: 'integrationVendorName', width: 250},
        {headerName: '공급자번호', field: 'vendorNum', width: 120},
        {headerName: '고객번호', field: 'customerNum', width: 120},
        {headerName: '사업자번호', field: 'vatRegistrationNum', width: 140},
        {headerName: '국가', field: 'territoryCode', width: 100},
        {headerName: '시도구분', field: 'regionName', width: 100, cellStyle:{textAlign: 'center'},},
        {headerName: '거래처유형', field: 'venBusinessCondition', width: 150},
        {headerName: '우편번호', field: 'venZip', width: 150},
        {headerName: '주소', field: 'venAddress', width: 250},
        {headerName: '상세주소', field: 'venBusinessType', width: 200},
        {headerName: '전화번호', field: 'venPhone', width: 150},
        { headerName: '이메일',
          field: 'email',
          width: 170,
          valueFormatter: function(params) {
            return (params.node.data.custEmail || params.node.data.venEmail);
          }
        },
      ]

      this.columnDefsBank = [
        { headerName: 'No.',
          field: 'rn',
          width: 100,
         cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          }
        },
        { headerName: '유형',
          field: 'noteAccountFlag',
          width: 150,
          cellStyle:{textAlign: 'center'},
          valueFormatter: function(params) {
            return (params.value == 'CHECK' ? '일반' : (params.value == 'NOTE' ? '약정' : params.value))
          }
        },
        {headerName: '은행', field: 'bankName', width: 150},
        {headerName: '계좌번호', field: 'bankAccountNumber', width: 250},
        {headerName: '예금주', field: 'bankAccountName', width: 250},
        {headerName: '통화코드', field: 'currencyCode', width: 80, cellStyle:{textAlign: 'center'}},
        {headerName: '주계좌여부', field: 'primaryFlag', width: 80, cellStyle:{textAlign: 'center'}},
      ]

      this.columnDefsExcel = [
        {headerName: '통합거래처코드', field: 'integrationVendorNum', width: 150},
        {headerName: '거래처명', field: 'integrationVendorName', width: 250},
        {headerName: '공급자번호', field: 'vendorNum', width: 150},
        {headerName: '고객번호', field: 'customerNum', width: 150},
        {headerName: '사업자번호', field: 'vatRegistrationNum', width: 150},
        {headerName: '국가', field: 'territoryCode', width: 100},
        {headerName: '시도구분', field: 'regionName', width: 150},
        {headerName: '거래처유형', field: 'venBusinessCondition', width: 150},
        {headerName: '우편번호', field: 'venZip', width: 150},
        {headerName: '주소', field: 'venAddress', width: 250},
        {headerName: '상세주소', field: 'venBusinessType', width: 200},
        {headerName: '전화번호', field: 'venPhone', width: 150},
        { headerName: '이메일',
          field: 'email',
          width: 150,
          valueFormatter: function(params) {
            return (params.node.data.custEmail || params.node.data.venEmail);
          }
        },
      ]
    },
    getCompCdCombo() {
      this.$http.get(`/api/code/combo`, {params: {groupCd: "COMP_CD"}})
        .then(response => {
          this.compCds = response.data;
        });
    },
    downloadExcel(){
      this.$swal({
        type: 'warning',
        html: '엑셀 다운로드를 진행하시겠습니까?<br/>(상황에 따라 몇분정도 소요될 수 있습니다.)',
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니요'
      }).then(response => {
        if (response.value) {
          this.$store.commit('loading');
          this.$http.post(`/api/vendor/list`, {
              compCd: this.$store.state.loginInfo.compCd,
              integrationVendorNum: this.search.integrationVendorNum,
              integrationVendorName:this.search.integrationVendorName,
              territoryCode: this.search.benCountryCd,
              regionName: this.search.regionName,
              venPayGroupLookupCd: this.search.venPayGroupLookupCd,
              vatRegistrationNum: this.search.vatRegistrationNum,
              bizTypeName: this.search.bizTypeName,
              arFlag: null,
              apFlag: null,
              page: 0,
              limit: this.page.count
            }
          ).then(response => {
            this.vendorListExcel = response.data;
          }).finally(() => {
            var params = {
              fileName : "거래처목록"
            };
            this.gridOptionsExcel.api.exportDataAsCsv(params)
            this.$store.commit('finish')
          });
        }
      })

    },
    closeModal(){
      document.getElementById("open-moda").style.opacity = "0";
      document.getElementById("open-moda").style.pointerEvents = "none";
    },
    openModal(){
      document.getElementById("open-moda").style.opacity = "1";
      document.getElementById("open-moda").style.pointerEvents = "auto";
    },
    resetSearch(){
      this.initVendor(true)
      this.initBenCountry(true)
      this.search.bizTypeName = ''
      this.search.venPayGroupLookupCd = ''
      this.search.regionName = ''
      this.search.vatRegistrationNum = ''
    },
    getCount(){
      this.$http.post(`/api/vendor/count`, {
          compCd: this.$store.state.loginInfo.compCd,
          integrationVendorNum: this.search.integrationVendorNum,
          integrationVendorName:this.search.integrationVendorName,
          territoryCode: this.search.benCountryCd,
          regionName: this.search.regionName,
          venPayGroupLookupCd: this.search.venPayGroupLookupCd,
          vatRegistrationNum: this.search.vatRegistrationNum,
          bizTypeName: this.search.bizTypeName,
          arFlag: null,
          apFlag: null,
        }
      ).then(response => {
        this.page.count = response.data;
        this.page.lastPage = Math.ceil(response.data / 100) - 1

        this.page.pageList = []

        if(this.page.lastPage > -1){
          var count = 0;
          for(var i=Math.floor(this.page.page/10)*10; i<=this.page.lastPage; i++){
            count++;
            if(count > 10) break

            this.page.pageList.push({value: i+1})
          }
        }
      })

    },
    goSearch() {
      if(this.initPage){
        this.initPage = false
        this.page.page = 0
      }
      this.initDetail()
      this.closeModal()
      this.getCount()
      this.$store.commit('loading');
      this.$http.post(`/api/vendor/list`, {
          compCd: this.$store.state.loginInfo.compCd,
          integrationVendorNum: this.search.integrationVendorNum,
          integrationVendorName:this.search.integrationVendorName,
          territoryCode: this.search.benCountryCd,
          regionName: this.search.regionName,
          venPayGroupLookupCd: this.search.venPayGroupLookupCd,
          vatRegistrationNum: this.search.vatRegistrationNum,
          bizTypeName: this.search.bizTypeName,
          arFlag: null,
          apFlag: null,
          page: this.page.page,
          limit: this.page.limit
        }
      ).then(response => {
        this.vendorList = response.data;
      }).finally(() => {
        this.$store.commit('finish')
      });
    },
    goFirst(){
      this.page.page = 0
      this.goSearch()
    },
    goPre(){
      if(this.page.page > 0) {
        this.page.page = this.page.page - 1
      }
      this.goSearch()
    },
    goNext(){
      //마지막 구하기
      if(this.page.page < this.page.lastPage) {
        this.page.page = this.page.page + 1
      }
      this.goSearch()
    },
    goEnd(){
      this.page.page = this.page.lastPage
      this.goSearch()
    },
    goPage(param){
      this.page.page = param - 1
      this.goSearch()
    },
    initVendor(force) {
      if (force === true) this.search.integrationVendorName = '';
      if (this.search.integrationVendorName === '') {
        this.search.integrationVendorNum = '';
      }
    },
    initBenCountry(force) {
      if (force === true) this.search.benCountry = '';
      if (this.search.benCountry === '') {
        this.search.benCountryCd = '';
      }
    },
    popVendor(clear) {
      let vm = this
      this.$modal.open({
        component: Vendor,
        props: {
          param: this.search.integrationVendorName
        },
        parent: this,
        width: '1200px',
        events: {
          close(obj) {
            vm.search.integrationVendorNum = obj.integrationVendorNum;
            vm.search.integrationVendorName = obj.integrationVendorName;

            vm.goFirst();
          }
        }
      })
    },
    popBenCountry() {
      let vm = this
      this.$modal.open({
        component: BenCountryPop,
        props: {
          param: this.search.benCountry
        },
        parent: this,
        events: {
          close(obj) {
            vm.search.benCountry = obj.benCountryNm;
            vm.search.benCountryCd = obj.benCountryCd;
          }
        }
      })
    },
    onReady(params) {
      //메인그리드 api 정의
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;

      //화면 width에 맞게 컬럼 size 조정
      //this.gridApi.sizeColumnsToFit();
    },
    onReadyBank(params) {
      //메인그리드 api 정의
      this.gridApiBank = this.gridOptionsBank.api;
      this.columnApiBank = this.gridOptionsBank.columnApi;

      //화면 width에 맞게 컬럼 size 조정
      this.gridApiBank.sizeColumnsToFit();
    },
    initDetail(){
      this.form.integrationVendorNum = ''
      this.form.integrationVendorName = ''
      this.form.vatRegistrationNum = ''
      this.form.bizTypeName = ''
      this.form.territoryCode = ''
      this.form.regionName = ''
      this.form.venZip = ''
      this.form.address = ''
      this.form.venPhone = ''
      this.form.email = ''
      this.form.customerNum = ''
      this.form.custCategoryCode = ''
      this.form.custEnabledFlag = ''
      this.form.custPaymentDescription = ''
      this.form.custDamboFlagName = ''
      this.form.vendorNum = ''
      this.form.venPayGroupLookupCd = ''
      this.form.venPaymentDescription = ''
      this.form.venEnabledFlag = ''
      this.form.venRepresontativeName = ''
    },
    onSelectionChanged(){
      this.initDetail();
      const Row = this.gridApi.getSelectedRows();
      this.form.integrationVendorNum = Row[0].integrationVendorNum;
      this.form.integrationVendorName = Row[0].integrationVendorName;
      this.form.vatRegistrationNum = Row[0].vatRegistrationNum;
      this.form.bizTypeName = Row[0].bizTypeName;
      this.form.territoryCode = Row[0].territoryCode;
      this.form.regionName = Row[0].regionName;
      this.form.venZip = Row[0].venZip;
      this.form.address = (Row[0].custAddress || Row[0].venAddress);
      this.form.venPhone = Row[0].venPhone;
      this.form.email = (Row[0].custEmail || Row[0].venEmail)

      this.form.customerNum = Row[0].customerNum;
      this.form.custCategoryCode = Row[0].custCategoryCode;
      this.form.custEnabledFlag = (Row[0].custAccountStatus == 'A' ? 'Y' : 'N');
      this.form.custPaymentDescription = Row[0].custPaymentDescription;
      this.form.custDamboFlagName = Row[0].custDamboFlagName;

      this.form.vendorNum = Row[0].vendorNum;
      this.form.venPayGroupLookupCd = Row[0].venPayGroupLookupCd;
      this.form.venPaymentDescription = Row[0].venPaymentDescription;
      this.form.venEnabledFlag = Row[0].venEnabledFlag;
      this.form.venRepresontativeName = Row[0].venRepresontativeName

      this.$store.commit('loading');
      this.$http.post(`/api/vendor/bankList`, {
          compCd: 81,
          integrationVendorNum: Row[0].integrationVendorNum,
        }
      ).then(response => {
        this.bankList = response.data;
      }).finally(() => {
        this.$store.commit('finish')
      });
    },
  },
  watch: {
    'search': {
      immediate: false,
      deep: true,
      handler() {
        this.initPage = true
      }
    },
  },
  mounted() {
    document.title = this.title + ' - IJEAS';
    this.authority = this.$store.state.loginInfo.authorities[0].roleCd;
    //this.getCompCdCombo();
  },
  beforeMount() {
    this.makeColDef();
    this.defaultColDef = { resizable: true, filter:false, sortable: false };
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
}

.desc-content .item .desc-item:last-child {
  margin-bottom: 0;
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
  padding-left: 80px;
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
  width: 38%;
  padding-right: 20px;
}

.desc-content .item.desc-center {
  width: 35%;
  padding-right: 40px;
}

.desc-content .item.desc-right {
  width: 27%;
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

@media (max-width: 1580px) {
  .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
    width: 50%;
  }
}

input:read-only{
  background-color: white;
}
</style>

