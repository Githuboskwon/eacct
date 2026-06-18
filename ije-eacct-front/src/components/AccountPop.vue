<template>
  <layout>
    <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$dismiss"></button></span>
    <div slot="content">
      <div class="btn-type1">
        <button class="btn-size btn-gray" @click="goSearch(true)"><span class="btn-icon"><i class="fas fa-search"></i></span> 조회</button>
      </div>

      <div class="pop-content-search">
        <div class="field has-addons ">
          <div class="mr20 ">
            <span class="pop-c-name">계정과목 코드/명</span>
          </div>
          <div class="control is-expanded">
            <input class="input" type="text" v-model="search" @keypress.enter="goSearch">
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
            @rowDoubleClicked="rowDoubleClick"/>
      </div>
    </div>
  </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'
import {AgGridVue} from 'ag-grid-vue';

export default {
  compatConfig: { MODE: 2 },
  name: 'erpAccount',
  props: {
    title: {
      type: String,
      required: false,
      default: '계정과목 조회'
    },
    deptCd: {
      type: String,
      required: true,
    },
    ctrlYn: {
      type: String,
      required: false,
    },
    searchStr: {
      type: String,
      required: false
    },
    slipTypeCd: {
      type: String,
      required: false
    },
    postSearch: {
      type: Boolean,
      required: false
    },
    rcpYn: {
      type: String,
      required: false
    },
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
          headerName:'계정코드',
          field:'acctCd',
          width:150,
          cellStyle:{textAlign:'center'}
        },
        {
          headerName:'계정명',
          field:'acctNm',
          width:300,
          cellStyle:{textAlign:'left'}
        },
        {
          headerName:'구분',
          field:'assetsTrackingFlag',
          valueFormatter: function(params) {
            return (params.value == 'Y' ? '고정비' : (params.value == 'N' ? '변동비' : params.value))
          },
          width:300,
          cellStyle:{textAlign:'center'}
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
    $dismiss() {
      this.$emit('dismiss')
      this.$parent.close()
    },
    goSearch() {
      this.$store.commit('loading')
      this.data = []

      //put api 호출
      if(this.postSearch){

        this.$http.post(`/api/account/pop/list`, {
          acctCd: this.search,
          deptCd: this.deptCd,
        })
            .then(response => {
              this.rowData = response.data;
            })
            .catch(response => {
              console.error("ErpAccount_Ag Error");
            })
            .finally(() => {
              this.$store.commit('finish')
            })

        //get api 호출
      }else{
        //slipTypeCd가 없는 경우
        if(this.slipTypeCd === undefined){
          this.$http.post(`/api/account/search`,{
            deptCd: this.deptCd,
            ctrlYn: this.ctrlYn,
            searchVal: this.search
          })
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

        }else{
          this.$http.get('/api/account/'+this.deptCd+'/'+this.slipTypeCd+'/'+this.rcpYn+'/'+this.search)
              .then(response => {
                this.rowData = response.data;
              })
              .catch(response => {
                console.error("ErpAccount_Ag Error");
              })
              .finally(() => {
                this.$store.commit('finish')
              })
        }
      }
    },
    rowDoubleClick(params){
      this.$emit('close', params.data);
    }
  },
  created() {
    this.search = this.searchStr
    this.goSearch()
  }
}

</script>
