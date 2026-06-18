<template>
<layout>
  <span slot="header">
    {{ title }} 조회
    <button class="btn-pop-close delete" aria-label="close" @click="$dismiss"></button>
  </span>
  <div slot="content">
    <div class="btn-type1">
      <button class="btn-size btn-gray" @click="goSearch(true)">
        <span class="btn-icon">
          <i class="fas fa-search"></i>
        </span>
        조회
      </button>
    </div>

    <div class="pop-content-search">
      <div class="field has-addons ">
        <div class="mr20 ">
          <span class="pop-c-name">- {{ title }} 코드/명</span>
        </div>
        <div class="control is-expanded">
          <input class="input" type="text" v-model="search" @keypress.enter="goSearch" />
        </div>
      </div>
    </div>
    <div class="grid-tbl-box">
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
import {AgGridVue} from 'ag-grid-vue';

export default {
  compatConfig: { MODE: 2 },
  props: {
    title: {
      type: String,
      required: false,
      default: '비용부서'
    },
    searchStr: {
      type: String,
      required: false
    }
  },
  components: {
    Layout,
    AgGridVue
  },
  data() {
    return {
      search: undefined,
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
          headerName:'비용부서코드', 
          field:'deptCd', 
          width:250, 
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'비용부서명', 
          field:'deptNm', 
          width:370, 
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'프로젝트구분', 
          field:'expenseFlag', 
          width:200, 
          cellStyle:{textAlign:'left'}
        }
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
    $dismiss() {
      this.$emit('dismiss')
      this.$parent.close()
    },
    goSearch() {
      this.$store.commit('loading')
      this.data = []
      // console.log("erp cctr value ", this)
      this.$http
        .get(_url('/api/cctr/erp/', this.search))
        .then(response => {
          this.rowData = response.data;
        })
        .catch(response => {
          console.error("ErpCctr_Ag Error");
        })
        .finally(() => {
          this.$store.commit('finish')
        })
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    },
    onGridReady(){
      this.gridOptions.api.sizeColumnsToFit();
    }
  },
  created() {
    this.search = this.searchStr
    if (this.search) {
      this.goSearch()
    }
  }
}

function _url(...args) {
  args = args.map(x => String(x || '').trim().replace(/^\/*|\/*$/g, ''))
  args = args.filter(x => x)
  return args.join('/')
}
</script>
