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
                  <span class="pop-c-name">- 계정과목 코드/명</span>
              </div>
              <div class="control is-expanded">
                  <input class="input" type="text" v-model="search" @keypress.enter="goSearch">
              </div>
          </div>
      </div>

      <div class="grid-wrap">
          <ag-grid-vue 
                  style="width: 100%; height: 320px;"
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
  name: 'Account',
  props: ['param','slipTypeCd','deptCd'],
  mixins: [ mixin ],
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      title: '계정과목 조회',
      search: '',
      rcpYn: 'N',
      gridOptions: {},
      columnDefs:[
        {
          headerName:'No.', 
          field:'no', 
          width:80,
          valueFormatter: function(params) {               
            return params.node.rowIndex+1;
          }
        },
        {
          headerName:'계정코드', 
          field:'acctCd', 
          width:200,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'계정명', 
          field:'acctNm', 
          width:300,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'구분', 
          field:'acctGrNm', 
          width:150,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'통제계정',
          field:'ctrlYn',
          width:100,
          cellStyle:{textAlign:'left'},
          hide: this.slipTypeCd?false:true
        },
      ],
      defaultColDef: { 
        resizable: true, 
        filter:true, 
        sortable: true 
      },
      rowData: []
    }
  },
  methods: {
    goSearch() {
      this.$store.commit('loading');
      let slipTypeCd = this.slipTypeCd

      let deptCd = "";

      if(slipTypeCd === 'E4'|| slipTypeCd === 'E5'){
        deptCd = this.$parent.$parent.$parent.form.deptCd
      }else{
        deptCd = this.deptCd
      }

      if(this.$parent.$parent.$parent.$parent.rcpYn === 'Y'){
        this.rcpYn = this.$parent.$parent.$parent.$parent.rcpYn;
      }
      
      if(!deptCd) deptCd = this.$parent.$parent.$parent.form.cctrCd
      if(!slipTypeCd) slipTypeCd = this.$parent.$parent.$parent.$parent.slipTypeCd
      this.$http.get(`/api/account/${deptCd}/${slipTypeCd}/${this.rcpYn}/${this.search}`)
        .then((result) => {
          this.rowData = result.data

          if(result.data.length === 1) {
            this.$emit('close', result.data[0])
          }
        })
        .catch(response => {
          this.$store.commit('finish')
          console.error("Account_Ag Error");
        })
        .finally(() => {
          this.$store.commit('finish')
        });
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    onGridReady(){
      this.gridOptions.api.sizeColumnsToFit();
    },
    closeModal(){
      this.$parent.close()
    }
  },
  mounted() {
    if(this.param !== undefined) this.search = this.param;
    if(this.search) this.goSearch();    
  }
};
</script>

<style scoped>
div#gridbox {
  width: 100%;
  height: 100%;
  min-height: 300px;
}

</style>
