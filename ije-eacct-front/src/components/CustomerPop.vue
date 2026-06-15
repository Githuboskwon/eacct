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
                  <span class="pop-c-name">- 고객코드/명/사업자번호</span>
              </div>
              <div class="control is-expanded">
                  <input class="input" type="text" v-model="searchWord" v-on:keypress.enter="goSearch">
              </div>
          </div>
      </div>

      <div class="grid-wrap">
          <!-- <div id="empGridbox" class="grid-tbl-box"></div> -->
          <ag-grid-vue ref="grid" style="width: 100%; height: 500px;" class="ag-theme-alpine" 
                                :columnDefs="columnDefs" 
                                :rowData="gridData" 
                                :gridOptions="gridOptions"
                                :defaultColDef="defaultColDef"
                                @rowDoubleClicked="rowDoubleClick"/>
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
import _ from 'lodash'
import Layout from '@/components/ModalSlot.vue'
import mixin from '@/mixin';
import { AgGridVue } from 'ag-grid-vue'

export default {
  name: 'CustomerPop',
  props: {
    param: {
      Type: String,
      defalut: '',
    },
  },
  mixins: [ mixin ],
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      columnDefs : [
        {
          headerName: 'No.', 
          field: 'no', 
          width: 80, 
          cellStyle:{textAlign: 'center'},
          valueFormatter: (params) => {         
            return params.node.rowIndex+1;
          }
        },
        // {headerName: '고객코드', field: 'customerId', width: 160, cellStyle:{textAlign: 'left'}},
        {headerName: '고객코드', field: 'customerNum', width: 100, cellStyle:{textAlign: 'center'}},
        {headerName: '고객명', field: 'customerName', width: 250, cellStyle:{textAlign: 'left'}},
        {headerName: '사업자등록번호', field: 'vatRegistrationNum', width: 250, cellStyle:{textAlign: 'left'}},
      ],
      gridOptions : {},
      defaultColDef: { 
        resizable: true, 
        filter:true, 
        sortable: true 
      },
      title: '고객 조회',
      searchWord: '',
      gridData:[],
      page: {
        page: 0,
        limit: 100,
        totalCount: 0,
        lastPage: 0,
        pageList: []
      }
    }
  },
  methods: {
    goSearch() {
      
      this.$store.commit('loading');
      let searchWord = this.searchWord ? this.searchWord : '';
      this.$http.get(`/api/customer/list/${searchWord}`,
      {params: {
        page: this.page.page,
        size: this.page.limit,
      }})
        .then((response) => {
          this.setPageData(response.data);
          this.gridData = response.data.customerList;

          this.$store.commit('finish');

        }).catch(response => {
          this.$store.commit('finish');
          console.error("goSearch Error" + response);
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
    setPageData(data) {
      this.page.page = data.currentPageCount;
      this.page.totalCount = data.totalPageCount;
      this.page.lastPage = data.totalPageCount - 1;
      this.page.pageList = []
      if(this.page.lastPage > -1){
        let count = 0;
        for(let i=Math.floor(this.page.page/10)*10; i<=this.page.lastPage; i++){
          count++;
          if(count > 10) break;

          this.page.pageList.push({value: i+1})
        }
      }
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    closeModal() {
      this.$emit('dismiss');
      this.$parent.close();
    },
  },
  created() {
    if(this.param) this.searchWord = this.param;
    if(!_.isEmpty(this.header)) this.title = this.header;
    if(this.searchWord) this.goSearch();
  }
};
</script>
