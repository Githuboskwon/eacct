<template>
    <layout style="width: 700px;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button></span>
        <div slot="content">
            <div class="pop-content-search" style="height: 60px">
                <div class="field has-addons ">
                    <div class="mr20 ">
                        <span class="pop-c-name">- 거래유형 명</span>
                    </div>
                    <div class="control is-expanded">
                        <input class="input" type="text" v-model="search" @keypress.enter="goSearch">
                    </div>
                    <div class="btn-type1" style="padding-left: 10px">
                      <el-button @click="goSearch" icon="el-icon-search">
                        조회
                      </el-button>
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
                    @cell-clicked="onCellClicked"
                    @grid-ready="onGridReady"/>
            </div>
            <br/>
            <div class="grid-wrap" style="padding-bottom: 10px">
              <ag-grid-vue
                style="width: 100%; height: 200px;"
                class="ag-theme-alpine"
                rowSelection="single"
                :columnDefs="subColumnDefs"
                :gridOptions="subGridOptions"
                :rowData="subRowData"
                :defaultColDef="subDefaultColDef"
                @grid-ready="onSubGridReady"/>
            </div>
          <div class="btn-type1">
            <button class="btn-size btn-blue" @click="writeSlip">전표 작성</button>
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
    props: {
        schTxt: {
            Type: String,
            required: false
        },
        personId: {
            Type: String,
            required: false
        },
        etaxInfo: {
          Type: Object,
          required: false
        }
    },
    components: {
        Layout,
        AgGridVue
    },
    data() {
        return {
            title: '거래유형 조회',
            rowData: [],
            gridOptions: {},
            columnDefs:[],
            defaultColDef: { 
                resizable: true,
                filter:true, 
                sortable: true,
                // flex: 2
            },
            subRowData: [],
            subGridOptions: {},
            subColumnDefs:[],
            subDefaultColDef: {
              resizable: true,
              filter:true,
              sortable: true,
              // flex: 2
            },
            gridApi : null ,
            columnApi : null ,
            subGridApi : null ,
            subColumnApi : null ,
            search: '',
        }
    },
    created() {
        this.search = this.schTxt || '';
        this.goSearch();

        this.makeColDef();
        this.makeSubColDef();
    },
    methods: {
        goSearch() {
            const params = {
                compCd: this.$store.state.loginInfo.compCd,
                trxTypeNm: this.search,
                trxSpTypeCd: '10',
                personId: this.personId
            }
            this.$store.commit('loading');
            this.$http.post(`/api/trx/slip/list`, params)
            .then(res => res.data)
            .then(data => {
                this.rowData = data.filter(x => (x.interfaceModule == 'AP' && x.slipTypeCd != 'IM' && x.slipTypeCd != 'PO' ))
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
                    headerName:'거래유형명',
                    field:'trxTypeNm',
                    cellStyle:{textAlign:'left'},
                    width: 375,
                },
                {
                    headerName:'거래유형코드',
                    field:'trxTypeCd',
                    cellStyle:{textAlign:'left'},
                    width: 250,
                },
                {
                    headerName:'전표양식코드',
                    field:'slipTypeCd',
                    hide: true
                },
            ];
        },
        onCellClicked(params){
          this.$store.commit('loading');
          this.$http.post(`/api/tax/evidence/code/${params.data.trxTypeCd}`, {compCd: 81})
            .then(res => res.data)
            .then(data => {
              this.subRowData = data.filter(x => (x.lineAttribute1 == 'V' && x.lineAttribute3 == 'S' ));

              if(this.etaxInfo.dtiType != '01' && this.etaxInfo.dtiType != 'B01'){
                this.subRowData = this.subRowData.filter(x => (x.evidenceCode != '110' ))
              }
            })
            .finally(() => {
              this.$store.commit('finish');
            });

        },
        rowDoubleClick(params){
            this.$emit('close', params.data);
        },
        onSubGridReady(params){
          this.subGridApi = params.api;
          this.subColumnApi = params.columnApi;

          //this.subGridApi.sizeColumnsToFit();
        },
        makeSubColDef(){
          const self = this;
          this.subColumnDefs = [
            {
              headerName:'세무증빙유형',
              field:'evidenceName',
              cellStyle:{textAlign:'left'},
              width: 250,
            },
            {
              headerName:'부가세여부',
              field:'lineAttribute1Name',
              cellStyle:{textAlign:'left'},
              width: 185,
            },
            {
              headerName:'세무증빙유형 코드',
              field:'evidenceCode',
              width: 190,
            },
          ];
        },
        writeSlip(){
          const Rows = this.gridApi.getSelectedRows();
          const subRows = this.subGridApi.getSelectedRows();

          if(Rows.length < 1){
            this.$swal({ type: 'warning', text: '거래유형을 선택해 주세요.' });
            return
          }

          if(subRows.length < 1){
            this.$swal({ type: 'warning', text: '세무증빙유형을 선택해 주세요.' });
            return
          }

          let params = {
              taxInfo: {
                empNo: this.$store.state.loginInfo.loginId,
                trxTypeCd: Rows[0].trxTypeCd,
                evidenceCode: subRows[0].evidenceCode,
                evidenceName: subRows[0].evidenceName,
                issueId: this.etaxInfo.issueId,
                suId: this.etaxInfo.supComRegno,
                chargetotal: this.etaxInfo.supAmount,
                taxtotal: this.etaxInfo.taxAmount,
                grandtotal: this.etaxInfo.totalAmount,
                issueDate: this.etaxInfo.dtiWdate,
                dtiType: this.etaxInfo.dtiType
              }
          }

          this.$router.push({
            name: 'accrualSlip',
            params
          })

          this.$emit('close')
        }
    }
}
</script>