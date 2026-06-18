<template>
  <layout>
    <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
    <div slot="content">
      <div class="btn-type1">
        <button class="btn-size btn-gray" @click="goSearch(true)"><span class="btn-icon"><i class="fas fa-search"></i></span> 조회</button>
      </div>

      <div class="pop-content-search">
        <div class="field has-addons ">
          <div class="mr20 ">
            <span class="pop-c-name">- 거래처(고객) 코드/명</span>
          </div>
          <div class="control is-expanded">
            <input class="input" v-focus type="text" v-model="search" @keypress.enter="goSearch">
          </div>
        </div>
      </div>

      <div class="grid-wrap">
        <ag-grid-vue
            style="width: 100%; height: 400px;"
            class="ag-theme-alpine"
            rowSelection="single"

            :columnDefs="columnDefs"
            :gridOptions="gridOptions"
            :rowData="rowData"
            :defaultColDef="defaultColDef"
            @rowDoubleClicked="rowDoubleClick"
            @grid-ready="onGridReady"/>
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

    </div>
  </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'
import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
  compatConfig: { MODE: 2 },
  name: 'Vendor',
  props: ['param','slipTypeCd', 'apFlag', 'arFlag', 'currencyCode'],
  mixins: [ mixin ],
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      title: '거래처(고객) 조회',
      search: '',
      rowData:[],
      gridOptions: {},
      page:{
        page: 0,
        limit: 100,
        count: 0,
        lastPage: 0,
        pageList: []
      },
      columnDefs:[],
      defaultColDef: {
        resizable: true,
        filter:true,
        sortable: true
      }
    }
  },
  methods: {
    create(){
    },
    makeColDef(){
      const that = this
      this.columnDefs = [
        {
          headerName:'No.',
          field:'no',
          width:80,
          valueFormatter: function(params) {
            return that.page.page*100 + params.node.rowIndex+1;
          }
        },
        {
          headerName:'거래처(고객)코드',
          field:'integrationVendorNum',
          cellStyle:{textAlign:'left'},
          hide:true,
        },
        {
          headerName:'거래처(고객)명',
          field:'integrationVendorName',
          cellStyle:{textAlign:'left'},
          width:250
        },
        {
          headerName:'사업자번호',
          field:'vatRegistrationNum',
          width:130,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'지급그룹',
          field:'venPayGroupLookupCd',
          width:130,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'고객 ID',
          field:'customerId',
          width:125,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'고객 SITE ID',
          field:'customerSiteId',
          width:125,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'VENDOR ID',
          field:'vendorId',
          width:150,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'VENDOR SITE ID',
          field:'vendorSiteId',
          width:150,
          cellStyle:{textAlign:'left'}
        },
      ]
    },
    getCount(){
      this.$http.post(`/api/vendor/count`, {
            compCd: this.$store.state.loginInfo.compCd,
            integrationVendorNum: this.search,
            integrationVendorName: this.search,
            apFlag: this.apFlag,
            arFlag: this.arFlag,
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
      this.getCount()
      this.$store.commit('loading');
      // this.$http.post(`/api/vendor/list/search`, {
      this.$http.post(`/api/vendor/list`, {
            compCd: this.$store.state.loginInfo.compCd,
            integrationVendorNum: this.search,
            integrationVendorName:this.search,
            currencyCode: this.currencyCode,
            apFlag: this.apFlag,
            arFlag: this.arFlag,
            page: this.page.page,
            limit: this.page.limit
          }
      ).then(response => {
        this.rowData = response.data;
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
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    closeModal() {
      this.$parent.close();
    },
    onGridReady(){
      //this.gridOptions.api.sizeColumnsToFit();
    }
  },
  mounted() {
    this.makeColDef();
    if(this.param !== undefined) this.search = this.param;
    if(this.search) this.goSearch();
  }
};
</script>

<style scoped>
.gridbox{height: 258px!important;}
.gridbox .objbox{height:185px!important;}

.modal-card {
  width: 1200px;
}
</style>
