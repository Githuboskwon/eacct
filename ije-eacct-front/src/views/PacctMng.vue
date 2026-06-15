<template>
<div class="inner-box">
  
    <div class="content-name">
      <h2 class="dp-ib">개인계정과목관리</h2>
      <div class="btn-wrap btn-type1 clearfix">
        <button type="submit" class="btn-size btn-gray fl_left" @click="buttonClickEventSearch">
          <span class="btn-icon"><i class="fas fa-search"></i></span>
          조회
        </button>
        <button type="button" class="btn-size btn-blue fl_left" @click="save">
          <span class="btn-icon"><i class="fas fa-save"></i></span>
          저장
        </button>
      </div>
    </div>

    <!-- 검색영역 -->
    <div class="search-form">
      <search-comp v-model="params" :options="{ type: '03', compCd: 'compCd', authorize: true }" />
      <div class="form-group">
        <label class="control-label">사번</label>
        <div class="form-input">
          <input class="input input-bg mr5 Rt-M rs-mt2" type="text" v-model="params.empNo" style="width:60px;" disabled="disabled" />
          <div class="ip-box ip-box-w02 rs-mt2">
            <input type="text" class="input" v-model="params.empNm" @keypress.enter="empPop(params.empNm)" @input="initEmp" />
            <button class="icon is-right" @click="empPop(params.empNm)" style="position:absolute;top:0;right:0"><i class="fas fa-search"></i></button>
          </div>
        </div>
      </div>
    </div>
    <!-- //검색영역 -->
    
  

  <!-- 데이터 영역 -->
  <div class="table-area mt20">
    <div class="table-b">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">개인계정과목 내역</h3>
        </div>
      </div>
      <!-- Grid -->
      <!-- <dhx-grid ref="grid" v-model="data" :config="config" style="margin-bottom: 40px;" /> -->
      <!-- //Grid -->
      <div class="table-b">
          <div class="grid-wrap">
              <ag-grid-vue ref="grid" style="width: 100%;" class="ag-theme-alpine grid_search_height" 
                            :columnDefs="columnDefs" 
                            :rowData="data" 
                            :gridOptions="gridOptions"
                            :frameworkComponents="frameworkComponent"
                            :defaultColDef="defaultColDef"/>
          </div>
      </div>
    </div>
  </div>
  <!-- //데이터 영역 -->
</div>
</template>

<script>
// import Vue from 'vue'
// import _ from 'lodash'
// import assert from '@/libs/assert'
//import DhxGrid from '@/components/DhxGrid.vue'
// import Emp from '@/components/Emp_new.vue'
import Emp from '@/components/Emp_Ag.vue'

import { AgGridVue } from 'ag-grid-vue'
import AgGridCheckbox from "@/components/agGrid/AgGridCheckbox.vue"

export default {
  components: {
    AgGridVue
  },
  data() {
    return {
      columnDefs : [],
      gridOptions : {},
      frameworkComponent : {
          'checkBox' : AgGridCheckbox
      },
      defaultColDef: {},
      data: [],
      params: {
        compCd: undefined,
        empNo: undefined,
        empNm: undefined
      },
      options: {
        'COMP_CD': []
      }
    }
  },
  methods: {
    query() {
      this.$store.commit('loading')
      this.$http.get(`/api/account/personal/${this.params.compCd}/${this.params.empNo}`).then(response => {
        this.data = response.data
        this.data.forEach((x, i) => {
          if(x.useYn === '1') {
            x.useYn = true
          } else {
            x.useYn = false
          }
        })
        this.deleteList = []
        this.memento = []
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    empPop(searchStr) {
      let vm = this
      this.$modal.open({
          component: Emp,
          props: {
              param: searchStr
          },
          parent: this,
          events: {
              close(obj) {
                  vm.params.empNo = obj.empNo
                  vm.params.empNm = obj.empNm
              }
          }
      })
    },
    initEmp(force) {
      if (force === true) this.params.empNm = '';
      if (this.params.empNm === '') this.params.empNo = '';
    },
    queryCompCd() {
      this.$store.commit('loading')
      this.$http.get('/api/code/combo', {
        params: {
          groupCd: 'COMP_CD'
        }
      }).then(response => {
        this.options['COMP_CD'] = response.data
      }).catch(response => {
        return response
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    save() {
      try {

        let accountDtos = this.data.filter(x => x.useYn === true)
        
        this.$store.commit('loading')
        if(accountDtos.length === 0) {
          accountDtos = []
        } else {
          accountDtos.forEach((x, i) => {
            x.useYn = '1'
          })
        }
        
        let obj = {
          empNo : this.params.empNo,
          accountDtos
        }
        this.$http.post(`/api/account/personal`, obj)
          .then(response => {
          
              this.$swal({
                type: 'success',
                text: '저장되었습니다.'
              }).then(this.query)

          }).finally(() => {
            this.$store.commit('finish')
          })
        
      } catch (e) {
        this.$store.commit('finish')
        this.$swal({
          type: 'error',
          text: e
        })
      }
    },
    buttonClickEventSearch() {
      
      if (!this.params.empNo) {
          this.$swal({type: 'warning', text: '사번을 입력해 주세요.'});
          return false;
      }

      this.query()
    },
    makeColDef(){
      const that = this;
      this.columnDefs = [
          {
              headerName: 'No.', 
              width: 80,
              valueFormatter: function(params) {               
                return params.node.rowIndex+1;
              }
          },
          {
              headerName: '계정명', 
              field: 'acctCd', 
              width: 220,
              cellStyle:{textAlign: 'left'}
          },
          {
              headerName: '계정명칭', 
              field: 'acctNm', 
              width: 400,
              cellStyle:{textAlign: 'left'}
          },
          {
              headerName: '계정계정약어명칭명', 
              field: 'acctShortNm', 
              width: 400,
              cellStyle:{textAlign: 'left'}
          },
          {
              headerName: '계정그룹코드', 
              field: 'acctGrCd', 
              width: 300,
              cellStyle:{textAlign: 'left'}
          },
          {
              headerName: '사용여부', 
              field: 'useYn', 
              width: 220,
              cellStyle:{textAlign: 'center'},
              cellRenderer: 'checkBox'
          }
      ]
    },
  },
  beforeMount(){
    this.makeColDef();
    this.defaultColDef = { resizable: true, filter:true, sortable: true };
  },
  created() {
    document.title = '개인계정과목관리 - IJEAS';
    try {
      this.queryCompCd()

      let compCd = this.$store.state.loginInfo.compCd
      this.params.compCd = compCd
      this.params.empNo = this.$store.state.loginInfo.loginId
      this.params.empNm = this.$store.state.loginInfo.userName
    } catch (e) {
      console.error("PacctMng_New Error");
    } finally {
      this.query()
    }
  }
}
</script>
