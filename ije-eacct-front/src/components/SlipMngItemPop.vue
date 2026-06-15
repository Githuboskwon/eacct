<template>
  <layout>
  <span slot="header">
    관리 항목
    <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
  </span>
    <div slot="content" class="inner-box">

      <div class="table-b">
        <div class="table-header">
          <div class="btn-wrap btn-type2 clearfix fl_right">
            <button class="btn-size btn-w-gray" @click="submit">
              <span class="btn-icon"><i class="fas fa-check"></i></span> 완료
            </button>
            <button class="btn-size btn-w-gray" @click="$parent.close()">
              <span class="btn-icon"><i class="fas fa-times"></i></span> 취소
            </button>
          </div>
        </div>

        <div class="grid-tbl-box">
          <ag-grid-vue
            ref="grid"
            style="width: 100%; height: 200px;"
            class="ag-theme-alpine"
            rowSelection="single"

            :columnDefs="columnDefs"
            :defaultColDef="defaultColDef"
            :frameworkComponents="frameworkComponents"
            :rowData="rowData"
            :gridOptions="gridOptions"
            :suppressRowClickSelection="false"
            :enableRangeSelection="false"
            @grid-ready="onReady"
            @cell-clicked="onCellClicked"
          />
        </div>
      </div>

    </div>
  </layout>
</template>

<script>
import _ from 'lodash'

import Layout from '@/components/ModalSlot.vue'

import DatepickerCellRenderer from "@/components/agGrid/datepicker-cell-renderer";
import DffDetail from "@/components/SlipMngDetailPop"


export default {
  props: {
    applicationShortCd: {
      type: String,
      required: true
    },
    acctCd: {
      type: String,
      required: true
    },
    data: {
      type: Object,
      required: false
    },
    readonly: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  components: {
    Layout
  },
  data() {
    return {
      config: undefined,
      columnDefs: [],
      defaultColDef: {resizable: true, filter:false, sortable: false},
      frameworkComponents: {
        datePicker: DatepickerCellRenderer
      },
      gridOptions: {},
      dffData: [],
      rowData: [],
      gridWidth: 0,

    }
  },
  methods: {
    queryGridConfig() {
      const that = this
      this.columnDefs = []
      this.gridWidth = 0

      this.$store.commit('loading');

      this.$http.post('/api/dff/list', {
        applicationShortCd: this.applicationShortCd,
        acctCd: this.acctCd //this.acctCd //5352410 //5352120 //9001115
      }).then(response => {
        if (response.data.length == 0) {
          this.$swal({type: "error", text: "관리항목이 없습니다."})
          this.$parent.close()
        } else {

          this.dffData = response.data

          this.columnDefs = [
            {
              headerName: '계정코드',
              field: 'acctCd',
              cellStyle: {textAlign: 'center'},
              width: 100,
              hide: true
            },
          ]

          var hiddenColumn = []

          _.forEach(response.data, col => {
            this.gridWidth += col.mngItemNm.length

            //Date 칼럼
            if (col.mngItemTypeCd == 'Date') {
              //수정 불가 상태
              if(this.readonly) {
                this.columnDefs.push({
                  headerName: col.mngItemNm,
                  field: col.mngItemCd,
                  cellStyle: {
                    textAlign : col.hAlignCd,
                    backgroundColor: col.requiredYn == 'Y' ? '#FFDCDC' : 'white'
                  },
                  // width: (col.mngItemNm.length * 20),
                  editable: false,
                })
              }
              //입력 상태
              else{
                this.columnDefs.push({
                  headerName: col.mngItemNm,
                  field: col.mngItemCd,
                  cellStyle: {
                    textAlign: col.hAlignCd,
                    backgroundColor: col.requiredYn == 'Y' ? '#FFDCDC' : 'white'
                  },
                  // width: (col.mngItemNm.length * 20),
                  editable: false,
                  cellRenderer: 'datePicker',
                  cellRendererParams: {
                    config: {
                      hideTime: true,
                      format: "YYYY-MM-DD",
                      outputFormat: "YYYYMMDD",
                      dateFormat: '%Y-%m-%d'
                    },
                  },
                })
              }

            } else {
              if (col.validationType == 'F') {
                //팝업 칼럼
                this.columnDefs.push({
                  headerName: col.mngItemNm,
                  field: col.mngItemCd,
                  cellStyle: {
                    textAlign: col.hAlignCd,
                    backgroundColor: col.requiredYn == 'Y' ? '#FFDCDC' : 'white'
                  },
                  // width: (col.mngItemNm.length * 20),
                  editable: false,
                })

                hiddenColumn.push({
                    headerName: col.mngItemNm + '_코드조합',
                    field: col.mngItemCd + 'Code',
                    editable: false,
                    hide:true
                  })

              } else {
                //Text 칼럼
                this.columnDefs.push({
                  headerName: col.mngItemNm,
                  field: col.mngItemCd,
                  cellStyle: {
                    textAlign: col.hAlignCd,
                    backgroundColor: col.requiredYn == 'Y' ? '#FFDCDC' : 'white'
                  },
                  // width: (col.mngItemNm.length * 20),
                  editable: !this.readonly,
                })
              }
            }
          })

          for(var i=0; i<hiddenColumn.length; i++)
            this.columnDefs.push(hiddenColumn[i])

          let input = {}

          input = {
            acctCd: this.acctCd,
            ATTRIBUTE1: this.data?.attribute1,
            ATTRIBUTE2: this.data?.attribute2,
            ATTRIBUTE3: this.data?.attribute3,
            ATTRIBUTE4: this.data?.attribute4,
            ATTRIBUTE5: this.data?.attribute5,
            ATTRIBUTE6: this.data?.attribute6,
            ATTRIBUTE1Code: this.data?.attribute1Code,
            ATTRIBUTE2Code: this.data?.attribute2Code,
            ATTRIBUTE3Code: this.data?.attribute3Code,
            ATTRIBUTE4Code: this.data?.attribute4Code,
            ATTRIBUTE5Code: this.data?.attribute5Code,
            ATTRIBUTE6Code: this.data?.attribute6Code
          }

          this.rowData.push(input)
        }
      })
      .finally(_ => {
        try {
          if(this.gridWidth <= 57){
            this.gridOptions.api.sizeColumnsToFit()
          }
        } catch (e) {}
        this.$store.commit('finish')
      })
    },
    onCellClicked(event){
      const field = event.colDef.field
      const that = this;

      if(this.readonly){
        return
      }else {
        for (var i = 0; i < this.dffData.length; i++) {
          var beforeColumnValue = undefined
          // 팝업 관리 항목 확인
          if (this.dffData[i].mngItemCd == field && this.dffData[i].validationType == 'F') {
            if (this.dffData[i].treeYn == 'Y' && this.dffData[i].treeSeq > 1) {
              //상위 코드 입력 확인
              var col = this.dffData[i - 1].mngItemCd.toString() + 'Code'
              beforeColumnValue = event.api.getRowNode(event.rowIndex).data[col]

              if (beforeColumnValue === undefined) {
                this.$swal({type: 'warning', text: this.dffData[i - 1].mngItemNm + ' 항목을 입력해주세요.'})
                return
              }
            }

            const trees = this.dffData[i].treeSeq

            this.$modal.open({
              component: DffDetail,
              parent: this,
              props: {
                flexValueSetId: this.dffData[i].flexValueSetId,
                beforeColumnValue: beforeColumnValue
              },
              width: 700,
              events: {
                close(obj) {
                  if (trees > 1) {
                    //상위 코드 확인
                    if (!obj.meaningName.startsWith(beforeColumnValue)) {
                      this.$swal({type: 'warning', text: '코드 조합이 올바르지 않습니다.'})
                      return
                    }
                  }

                  //상위코드 선택시 하위코드 초기화
                  for (var j = i + 1; j < that.dffData.length; j++) {
                    if (that.dffData[j].treeCd != null && that.dffData[j].treeCd == that.dffData[i].treeCd) {
                      that.rowData[0][that.dffData[j].mngItemCd.toString()] = undefined
                      that.rowData[0][that.dffData[j].mngItemCd.toString().concat('Code')] = undefined
                    }
                  }

                  event.value = obj.valueName
                  event.api.getRowNode(event.rowIndex).setDataValue(field, obj.valueName)

                  event.data[field+'Code'] = obj.meaningName
                  event.api.getRowNode(event.rowIndex).setDataValue(field+'Code', obj.meaningName)

                  event.api.refreshCells()
                }
              }
            });

            break;
          }
        }
      }
    },
    submit(){
      if(this.readonly) {
        this.$parent.close()
      }else {

        for (var i = 0; i < this.dffData.length; i++) {
          if (this.dffData[i].requiredYn == 'Y' && this.rowData[0][this.dffData[i].mngItemCd.toString()] == undefined) {
            this.$swal({type: 'warning', text: this.dffData[i].mngItemNm + '은 필수 항목입니다.'})
            return
          }
        }
        this.$emit('close', this.rowData[0])
      }
    },
    onReady(){
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
      this.autoSizeAll(this.columnApi);
    },
    /**
     * * 그리드 컬럼 width 자동 맞춤
     * @param {*} columnApi 
    */
    autoSizeAll(columnApi) {
        const allColumnIds = [];
        columnApi.columnController.gridColumns.forEach((column) => {
            if(column.getId() !== 'chk') {
                allColumnIds.push(column.getId());
            }
        });
        columnApi.columnController.autoSizeColumns(allColumnIds, false);
    },
  },
  created() {
    this.queryGridConfig()
  }
}
</script>

<style scoped>

.modal-card {
  width: 1200px;
}

</style>
