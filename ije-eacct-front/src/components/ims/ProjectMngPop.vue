<template>
  <layout>
    <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
    <div slot="content">
      <div class="btn-type1">
          <button class="btn-size btn-gray" @click="goSearch(true)"><span class="btn-icon"><i class="fas fa-search"></i></span> 조회</button>
      </div>

      <div class="pop-content-search">
          <div class="field has-addons ">
              <div class="mr10 ">
                  <span class="pop-c-name">- PJT관리No</span>
              </div>
              <div class="control is-expanded">
                  <input class="input" type="text" v-model="searchNo" v-on:keypress.enter="goSearch">
              </div>

            <div class="mr10 ">
              <span class="pop-c-name">- PJT_TASK명</span>
            </div>
            <div class="control is-expanded">
              <input class="input" type="text" v-model="searchNm" v-on:keypress.enter="goSearch">
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
        {headerName: 'PJT관리번호', field: 'projectManageNo', width: 300, cellStyle:{textAlign: 'left'}},
        {headerName: 'PJT명_TASK명', field: 'projectManageNm', width: 300, cellStyle:{textAlign: 'left'}},
      ],
      gridOptions : {},
      defaultColDef: { 
        resizable: true, 
        filter:true, 
        sortable: true 
      },
      title: '프로젝트 조회',
      searchNo: '',
      searchNm: '',
      gridData:[]
    }
  },
  methods: {
    goSearch() {
      
      this.$store.commit('loading');

      const param = {
        projectManageNo: this.searchNo,
        projectManageNm: this.searchNm,
      }
      
      this.$http.post(`/api/project/registInfo/list`,param)
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
    }
  },
  created() {
    if(this.param !== undefined) this.searchNo = this.param;
    if(!_.isEmpty(this.header)) this.title = this.header;
    if(this.searchNo) this.goSearch();
  }
};
</script>
