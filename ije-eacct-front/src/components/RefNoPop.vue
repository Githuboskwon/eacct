<template>
  <layout style="width: 700px;">
      <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
      <div slot="content">
          <div class="btn-type1">
              <el-button @click="goSearch" icon="el-icon-search">
                  조회
              </el-button>
          </div>
          <div class="pop-content-search">
              <div class="field has-addons ">
                  <div class="mr20">
                      <span class="pop-c-name">- REF NO.</span>
                  </div>
                  <div class="control is-expanded">
                      <input class="input" type="text" v-model="form.refNo" @keypress.enter="goSearch">
                  </div>
                  <div class="mr20 ml20">
                      <span class="pop-c-name">- 고객명</span>
                  </div>
                  <div class="control is-expanded">
                      <input class="input" type="text" v-model="form.customerNm" @keypress.enter="goSearch">
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
import common from '@/mixin/common';
import mixin from '@/mixin';
import {AgGridVue} from 'ag-grid-vue';

export default {
  compatConfig: { MODE: 2 },
  props: {
      param: {
          Type: String,
          required: false
      }
  },
  components: {
      Layout,
      AgGridVue
  },
  data() {
      return {
          title: 'REF NO 조회',
          rowData: [],
          gridOptions: {},
          columnDefs:[],
          defaultColDef: { 
              // resizable: true, 
              filter:true, 
              sortable: true,
              // flex: 2
          },
          gridApi : null ,
          columnApi : null ,
          form: {
            refNo : '',
            customerNm : '',
          }
      }
  },
  created() {
      this.form.refNo = this.param || '';
      if(this.form.refNo) {
          this.goSearch();
      }
      this.makeColDef();
  },
  methods: {
      goSearch() {
          const params = {
              compCd: this.$store.state.loginInfo.compCd,
              refNo : this.form.refNo,
              customerNm: this.form.customerNm,
          }
          this.$store.commit('loading');
          this.$http.post(`/api/bondMst/refNo/list`, params)
          .then(res => res.data)
          .then(data => {
              this.rowData = data;
          })
          .finally(() => {
              this.$store.commit('finish');
          });
      },
      onGridReady(params){
          this.gridApi = params.api;
          this.columnApi = params.columnApi;

          //this.gridApi.sizeColumnsToFit();
      },
      makeColDef(){
          const self = this;
          this.columnDefs = [
              {
                  headerName:'REF NO.',
                  field:'refNo',
                  cellStyle:{textAlign:'left'},
                  width: 170,
              },
              {
                  headerName:'고객명',
                  field:'customerNm',
                  cellStyle:{textAlign:'left'},
                  width: 320,
              },
              {
                  headerName:'개설일',
                  field:'openingDt',
                  width: 120,
                  valueFormatter: (params) => {
                    return this.getDateFormat(params.value)
                  },
              },
              
          ];
      },
      rowDoubleClick(params){
          this.$emit('close', params.data);
      },
      getDateFormat(val) {
            if(val){
                return this.$moment(val).format('YYYY-MM-DD')
            }
      },
  }
}
</script>