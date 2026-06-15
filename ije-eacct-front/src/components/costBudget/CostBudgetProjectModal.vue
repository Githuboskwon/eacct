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
                  <span class="pop-c-name">- 프로젝트코드/명</span>
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
    </div>
  </layout>
</template>

<script>
import _ from 'lodash'
import Layout from '@/components/ModalSlot.vue'
import mixin from '@/mixin';
import { AgGridVue } from 'ag-grid-vue'

export default {
  name: 'ProjectPop',
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
        // {headerName: '고객코드', field: 'customerId', width: 160, cellStyle:{textAlign: 'left'}},
        {headerName: '프로젝트 코드', field: 'projectCd', width: 180, cellStyle:{textAlign: 'center'}},
        {headerName: '프로젝트명', field: 'projectNm', width: 180, cellStyle:{textAlign: 'left'}},
        {headerName: '프로젝트 아이디', field: 'projectId', width: 288, cellStyle:{textAlign: 'left'}},
      ],
      gridOptions : {},
      defaultColDef: { 
        resizable: true, 
        filter:true, 
        sortable: true 
      },
      title: '프로젝트 조회',
      searchWord: '',
      gridData:[]
    }
  },
  methods: {
    goSearch() {
      
      this.$store.commit('loading');

      const itemGroupCd = this.searchWord;
      const params = {
        itemGroupCd
      }
      this.$http.post(`/api/cost/budget/pop/project/list`, params)
          .then(res => res.data)
          .then(data => {
            this.gridData = data;
          })
          .finally(() => {
            this.$store.commit('finish');
          });
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    closeModal() {
      this.$emit('dismiss');
      this.$parent.close();
    }
  },
  created() {
    if(this.param !== undefined) this.searchWord = this.param;
    if(!_.isEmpty(this.header)) this.title = this.header;
    if(this.searchWord) this.goSearch();
  }
};
</script>
