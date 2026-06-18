<template>
  <layout>
    <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
    <div slot="content">
      <div class="btn-type1">
        <button class="btn-size btn-gray" @click="goSearch"><span class="btn-icon"><i class="fas fa-search"></i></span> 조회</button>
      </div>

      <div class="pop-content-search">
        <div class="field has-addons ">
          <div class="mr20 ">
            <span class="pop-c-name">- 코드/명</span>
          </div>
          <div class="control is-expanded">
            <input class="input" type="text" v-model="search" @keypress.enter="goSearch">
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
                @click="goPageNum(item.value)">{{item.value}}</button>

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
  name: 'MngItemDetail',
  props: {
    flexValueSetId: {
      type: Number,
      required: true
    },
    beforeColumnValue: {
      type: String,
      required: false,
      default: ''
    },
  },
  mixins: [ mixin ],
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      title: '코드 조회',
      search: '',
      gridOptions: {},
      columnDefs:[
        /*{
          headerName:'No.',
          field:'no',
          width:100,
          valueFormatter: function(params) {
            return params.node.rowIndex+1;
          }
        },*/
        {
          headerName:'코드',
          field:'valueName',
          width:200,
          //cellStyle:{textAlign:'left'}
        },
        {
          headerName:'코드조합',
          field:'meaningName',
          width:300,
          //cellStyle:{textAlign:'left'}
        },
        {
          headerName:'코드조합',
          field:'rowCnt',
          width:30,
          hide: true
        },
      ],
      defaultColDef: {
        resizable: true,
        filter:true,
        sortable: true
      },
      rowData: [],
      page:{
        page: 0,
        count: 0,
        lastPage: 0,
        pageList: []
      },
      initPage: false,
    }
  },
  methods: {
    goSearch() {
      if(this.initPage){
        this.page.page = 0
        this.initPage = false
      }
      this.$store.commit('loading');

      this.$http.post(`/api/dff/detail`, {
        flexValueSetId: this.flexValueSetId,
        beforeValue: this.beforeColumnValue,
        page: this.page.page,
        search: this.search
      })
        .then(response => {
          this.rowData = response.data;
          this.page.count = response.data[0].count

          this.page.lastPage = Math.ceil(this.page.count / 100) - 1

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
        .catch(response => {
          console.error("코드 조회 에러");
        })
        .finally(() => {
          this.$store.commit('finish')
        })
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    closeModal() {
      this.$parent.close();
    },
    onGridReady(){
      this.gridOptions.api.sizeColumnsToFit();
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
    goPageNum(param){
      this.page.page = param - 1
      this.goSearch()
    },
  },
  mounted() {
    if(this.param !== undefined) this.search = this.param;
    this.goSearch();
  },
  watch: {
    'search': {
      immediate: true,
      deep: true,
      handler(value) {
        this.initPage = true
      }
    },
  }
};
</script>

<style scoped>
div#gridbox {
  width: 100%;
  height: 100%;
  /* min-height: 300px; */
}

</style>
