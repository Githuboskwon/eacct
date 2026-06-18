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
                  <span class="pop-c-name">-출금 계좌/은행/지점명</span>
              </div>
              <div class="control is-expanded">
                  <input class="input" type="text" v-model="search" v-on:keypress.enter="goSearch">
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
    </div>
  </layout>
</template>

<script>
import _ from 'lodash'
import Layout from '@/components/ModalSlot.vue'
import mixin from '@/mixin';
import { AgGridVue } from 'ag-grid-vue'

export default {
  compatConfig: { MODE: 2 },
  name: 'PayBank',
  props: ['param', 'header'],
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
        {headerName: '계좌명', field: 'bankAccountName', width: 160, cellStyle:{textAlign: 'left'}},
        {headerName: '계좌번호', field: 'bankAccountNum', width: 180, cellStyle:{textAlign: 'left'}},
        {headerName: '은행명', field: 'bankName', width: 180, cellStyle:{textAlign: 'left'}},
        {headerName: '지점명', field: 'bankBranchName', width: 288, cellStyle:{textAlign: 'left'}},
        {headerName: '통화', field: 'currencyCode', width: 288, cellStyle:{textAlign: 'left'}},
      ],
      gridOptions : {},
      defaultColDef: { 
        resizable: true, 
        filter:true, 
        sortable: true 
      },
      title: '출금계좌 조회',
      search: '',
      gridData:[]
    }
  },
  methods: {
    goSearch() {
      
      this.$store.commit('loading');

      let search = this.isEmpty(this.search);
      
      this.$http.post(`/api/payBank/list`, {
        operatingUnitId: this.$store.state.loginInfo.compCd,
        bankAccountName: this.search,
      })
        .then((result) => {
          this.gridData = result.data;

          this.$store.commit('finish');

          // if(this.gridData.length === 1) {
          //   this.$emit('close', result.data[0]);
          // }
        }).catch(response => {
          this.$store.commit('finish');
          console.error("goSearch Error" + response);
        });
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    closeModal() {
      this.$emit('dismiss');
      this.$parent.close();
    },
    isEmpty(value){
      if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
        return ""
      }else{
        return value
      }
    },
  },
  created() {
    if(this.param !== undefined) this.search = this.param;
    if(!_.isEmpty(this.header)) this.title = this.header;
    if(this.search) this.goSearch();
  }
};
</script>
<style scoped>
.modal-card {
  width: 1100px;
}
</style>
