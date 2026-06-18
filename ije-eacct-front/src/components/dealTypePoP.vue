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
                  <span class="pop-c-name">- 거래유형</span>
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

    </div>
  </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'
import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
  compatConfig: { MODE: 2 },
  name: 'dealTypePoP',
  props: ['param','slipTypeCd'],
  mixins: [ mixin ],
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      title: '거래유형 조회',
      search: '',
      rowData:[],
      gridOptions: {},
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
          headerName:'거래 유형 코드',
          field:'trxTypeCode',
          cellStyle:{textAlign:'center'},
          width:210
        },
        {
          headerName:'거래 유형',
          field:'trxTypeName',
          cellStyle:{textAlign:'center'},
          width:210
        },
      ]
    },
    goSearch() {
      this.$store.commit('loading');
      this.$http.post(`/api/erp/submit/dealType/list/${this.search}`,
      ).then(response => {
        this.rowData = response.data;
      }).finally(() => {
        this.$store.commit('finish')
      });
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
  width: 500px;
}
</style>
