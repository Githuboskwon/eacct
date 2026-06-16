<template>
<div class="inner-box">
    <div class="content-name">
      <!-- <h2 class="dp-ib">{{$t('menuMngt')}}</h2> -->
      <h2 class="dp-ib">{{this.$i18n.messages[this.$store.state.locale].menuMngt}}</h2>
      <div class=" btn-type1 clearfix">
        <el-button size="large" type="primary" icon="el-icon-search" @click="query()">조회</el-button>
        <el-button size="large" type="success" icon="el-icon-folder-checked" @click="buttonClickSave()">저장</el-button>
      </div>
    </div>
    <div class="search-form">
      <div class="form-group">
        <label class="control-label">{{this.$i18n.messages[this.$store.state.locale].menuMngt}}</label>
        <div class="form-input">
          <input type="text" class="input" v-model="params.menuNm"/>
        </div>
      </div>
    </div>
  

  <!-- 데이터 영역 -->
  <div class="table-area">
    <div class="table-b">
      <div class="table-header">
        <div class="table-name">
          <h3 class="ico_table_name">{{this.$i18n.messages[this.$store.state.locale].menuList}}</h3>
        </div>
        <div class="btn-type2 clearfix">
          <el-button type="info" icon="el-icon-plus" @click="buttonClickEventAddRow">행추가</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="buttonClickEventRemoveRow()">행삭제</el-button>
          <el-button type="success" icon="el-icon-document-checked" @click="buttonClickEventToExcel()">엑셀 저장</el-button>
        </div>
      </div>
      <!-- Grid -->
      <!-- <dhx-grid ref="grid" v-model="data" :config="config" style="margin-bottom: 40px;" @setRowDataComplete="setRowDataComplete" /> -->
      <ag-grid-vue         
        ref="grid"
        style="width: 100%; height: 70vh; min-height: 400px;"
        class="ag-theme-alpine"    
        rowSelection="multiple"                                  

        :columnDefs="columnDefs"     
        :gridOptions="gridOptions"
        :defaultColDef="defaultColDef"
        :rowData="data"
        :frameworkComponents="frameworkComponents"          

        @grid-ready="onReady"        
        @cell-value-changed="onCellValueChanged"
      />            
      <!-- //Grid -->
    </div>
  </div>
  <!-- //데이터 영역 -->
</div>
</template>

<script>
import Vue from 'vue'
import assert from '@/libs/assert'
import debug from '@/libs/debug'
import mixinSlip from '@/mixin/slip';

import common from '@/mixin/common';

import NumberInputCellRenderer from '@/components/agGrid/numberinput-cell-renderer'

const bus = new Vue()
const _debug = debug('PgmMng')

function _constructTreeData(array) {
  array = array.map(x => {
    delete x.children
    x.menuLv = 1
    return x
  })
  // Get root elements
  let idMap = array.filter(x => x.menuNo).map(x => x.menuNo)
  let root = array.filter((x, i) => {
    return idMap.indexOf(x.upperMenuNo) < 0 || !x.upperMenuNo
  })
  _constructTreeData_slave(root, array, 1)
  return _resort(root)
}

function _constructTreeData_slave(root, array, menuLv) {
  menuLv = (menuLv || 1) + 1
  if (root === undefined || !Array.isArray(root) || root.length < 1) {
    return
  }
  let arr = array.filter(x => root.indexOf(x) < 0).map(x => {
    x.menuLv = menuLv
    return x
  })
  for (var i = 0, node = root[i]; i < root.length; node = root[++i]) {
    node.children = arr.filter(x => x.upperMenuNo === node.menuNo)
    _constructTreeData_slave(node.children, arr, menuLv)
  }
  return true
}

function _resort(array, result) {
  result = result || []
  array = array.sort((a, b) => a.menuOrder - b.menuOrder)
  if (Array.isArray(array) && array.length > 0) {
    for (var i = 0, node = array[i]; i < array.length; node = array[++i]) {
      result.push(node)
      result = _resort(node.children, result)
    }
  }
  return result
}

export default {
  mixins: [mixinSlip,common],
  components: {},
  data() {
    return {
      compCd: undefined,
      memento: [],
      deleteList: [],
      queue: [],
      data: [],
      params: {
        menuNm: undefined
      },
      gridOptions: {
        statusBar: {
          statusPanels: [
            {
              statusPanel: 'agTotalAndFilteredRowCountComponent',
              align: 'left',
            },
          ]
        },
          // onColumnResized: true,
          // enableFilter: true,
          // enableSorting: true,
          // suppressRowTransform: true
      },
      defaultColDef: {         
          resizable: true,
          filter: true,
          sortable: true,
          editable: true
      },              
      gridApi : null ,    //gridAip
      columnApi : null ,  //columApi
      columnDefs: [] ,    //comulum Defs  
      frameworkComponents: {
        numberInput: NumberInputCellRenderer
      },
    }
  },
  methods: {
    //그리드 Ready
    onReady(params) {
      
      //그리드용 api 정의
      //this.gridApi = params.api;
      //this.columnApi = params.columnApi; //params대신 gridOptions 사용가능
      this.gridApi = this.gridOptions.api;
      this.columnApi = this.gridOptions.columnApi;
      
      this.createColumnDefs();

      this.query()
    },
//컬럼정의 함수
    createColumnDefs() { 
        const vm = this

        this.columnDefs = [
          {headerName: 'No.', width : 80, editable: false, valueGetter: (params) => {return params.node.rowIndex + 1 }},
          {
            headerName: '메뉴번호'
          , width : 240
          , field: 'menuNo'
          , headerClass: 'require-column' //필수입력 컬럼 css
          , cellRenderer : vm.menuNoDraw
          , cellClass : function(params){
              if(params.node.data.new){
                return 'bg-lightpink'
              }else{
                return 'bg-grey'
              }
          }
          , editable: function(params){ 
              if(params.node.data.new){
                return true
              }else{
                return false
              }
            }
          },
          {
            headerName: '상위메뉴번호'
          , width : 170
          , field: 'upperMenuNo'
          },
          {
            headerName: '메뉴명'
          , width : 210 
          , field: 'menuNm'
          , headerClass: 'require-column' //필수입력 컬럼 css
          }, 
          {
            headerName: '프로그램'
          , width : 210 
          , field: 'programFileNm'
          , headerClass: 'require-column' //필수입력 컬럼 css
          },
          {
            headerName: '순번'
          , width : 80 
          , field: 'menuOrder'
          , cellRenderer: 'numberInput'
          },
          {
            headerName: '관련이미지'
          , width : 180 
          , field: 'relateImageNm'
          },
          {
            headerName: '관련이미지경로'
          , width : 180 
          , field: 'relateImagePath'
          },
          {
            headerName: '메뉴설명'
          , width : 270 
          , field: 'menuDc'
          },                              
        ];
    },
    //그리드 Cell 변경 이벤트
    onCellValueChanged(event) {
        const idx = event.rowIndex
        const field = event.colDef.field

        if(field == "upperMenuNo" || field == "menuOrder"){
          
          if (field === 1 && String(event.newValue || '')) {
            
            if (idx >= 0) {
              let menuNumbers = this.value.filter((x, i) => i !== idx).map(x => x.menuNo)
              // Duplicate found
              if (menuNumbers.indexOf(String(event.newValue)) >= 0) {
                bus.$emit('ERROR.MENU_NO.DUPLICATE', 200)
                bus.$emit('REQUEST.RECONSTRUCT_TREE')
                return false
              }
            }
          }
          bus.$emit('REQUEST.RECONSTRUCT_TREE')
        }
        this.data[idx].chg = true
    },
    /*메뉴번호*/
    menuNoDraw(params){
      let template=`<div style="`+ _style() +`">`
      + `<i class="`+ _hasChildren() +`"></i>&nbsp`
      +  _menuNo()
      + `</div>`

      function _style() {
        return   'margin-left :' + (Number(params.node.data.menuLv) * 15) + 'px'
      }

      function _hasChildren() {          
        if(params.node.data.children.length > 0){
          return 'far fa-folder-open'
        }else{
          return 'far fa-file'  
        }
      }
      
      function _menuNo() {          
        if(params.node.data.menuNo){
          return params.node.data.menuNo
        }else{
          return ''  
        }
      }

      return template
    }, 
    query() {       
      this.$store.commit('loading')
      return this.$http.post('/api/program/list', this.params)
        .then(response => {
          if (Array.isArray(response.data)) {
            this.data = _constructTreeData(response.data)
          } else {
            this.data = []
          }
          this.memento = []
          this.deleteList = []

          if(response.data.length == 0){
            this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
          }          
        }).finally(() => {
          this.$store.commit('finish')
        })
    },                    
    buttonClickSave() {
      try {
         _debug.apply(this, ['Memento', this.memento.length])
         
         if (this.memento.length <= 1) {
           throw {
             type: 'warning',
             text: '저장할 내용이 없습니다.'
           }
         }
        
        let data = this.data.filter(x => x.chg)

        let emptyMenuNo = data.filter(x => !x.menuNo)
        assert.apply(this, [emptyMenuNo.length === 0, '메뉴번호는 빈 값일 수 없습니다.'])

        data = data.map(x => {
          delete x.children
          x.compCd = this.compCd
          x.upperMenuNo = !x.upperMenuNo ? null : x.upperMenuNo
          return x
        })

        if (this.deleteList.length > 0) {
          let work = []
          _.forEach(this.deleteList, menuNo => {
            work.push(this.$http.delete('/api/program/', {
              params: {
                menuNo
              }
            }))
          })
          Promise.all(work)
            .then(() => {
              _proceed.apply(this, [data])
            })
        } else {
          _proceed.apply(this, [data])
        }
      } catch (e) {
        if (typeof e === 'object' && e.type && e.text) {
          this.$swal(e)
        } else {
          this.$swal({
            type: 'error',
            text: e
          })
        }
      }

      function _proceed(data) {
        this.$store.commit('loading')
        this.$http.post('/api/program/save', data)
          .then(response => {
            this.$swal({
              type: 'success',
              text: '저장되었습니다.'
            }).then(this.query)
          }).finally(() => {
            this.$store.commit('finish')
          })
      }
    },
    
    buttonClickEventAddRow() {
      
      //Selected Index
      const row = this.gridApi.getSelectedNodes()     
      const rowIndx = row.length < 1 ? 0 : row[0].rowIndex + 1
      
      //Selected Row
      let rowData = this.gridApi.getSelectedRows()[0]

      let upperMenuNo = null
      if (rowData) {        
        upperMenuNo = rowData.menuNo
      }

      this.data.splice(rowIndx, 0, {
        menuNo: null,
        upperMenuNo,
        menuNm: null,
        programFileNm: null,
        menuOrder: 0,
        relateImageNm: '/',
        relateImagePath: '/',
        menuDc: undefined,
        menuLv: 1,
        children: [],
        new: true
      })
      
      bus.$emit('REQUEST.RECONSTRUCT_TREE')
    },
    buttonClickEventRemoveRow() {
      //Selected Row
      let rowData = this.gridApi.getSelectedRows()[0]

      if (!rowData) {
        this.$swal({
          type: 'error',
          text: '삭제할 메뉴를 선택해주세요.'
        })
      } else {
        
        const row = this.gridApi.getSelectedNodes()     
        const rowIndx = row[0].rowIndex
        
        if (!this.data[rowIndx].new) {
          this.deleteList.push(this.data[rowIndx].menuNo)
        }

        // Delete children
        _deleteAll(this.data[rowIndx], this.data)
        bus.$emit('REQUEST.RECONSTRUCT_TREE')
      }

      function _deleteAll(root, array) {
        if (root !== undefined && Array.isArray(root.children) && root.children.length > 0 && Array.isArray(array)) {
          for (var i = 0, node = root.children[i]; i < root.children.length; node = root.children[++i]) {
            _deleteAll(node, array)
          }
        }
        array.splice(array.indexOf(root), 1)
      }
    },
    buttonClickEventExcelExport() {
      this.$refs.grid.instance.toExcel()
    },
    buttonClickEventToExcel() {
      this.downloadExcel(this.$refs.grid);
    },
    setRowDataComplete() {
      if (this.queue.length > 0) {
        let f = this.queue.shift()
        this.$nextTick(() => {
          if (f !== undefined && typeof f === 'function') {
            f.apply(this, [])
          }
        })
      }
    },
    buttonClickEventToExcel() {
      var params = {
        fileName : "메뉴 내역"
      };
      this.gridApi.exportDataAsCsv(params);
    },    
  },
  created() {
    try {
      this.compCd = this.$store.state.loginInfo.compCd
    } catch (e) {
      // DO NOTHING...may be there is no session to get
    } finally {
      //this.query()
      //this.buttonClickSave();
    }

    bus.$on('REQUEST.RECONSTRUCT_TREE', delay => {
      delay = delay || 100
      setTimeout(() => {
        this.data = _constructTreeData(this.data)
      }, delay)
    })

    bus.$on('ERROR.MENU_NO.DUPLICATE', delay => {
      delay = delay || 200
      setTimeout(() => {
        this.$swal({
          type: 'error',
          text: '메뉴번호는 중복될 수 없습니다.'
        })
      }, delay)
    })
  },
  watch: {
    'data': {
      deep: true,
      handler(value) {
        let c = _.cloneDeep(value)
        let v = JSON.stringify(c.map(x => {
          delete x.children
          delete x.menuLv
          delete x.chg
          return x
        }))
        var exists = this.memento.indexOf(v)
        // Has similar history
        if (exists >= 0) {
          // fall back to restore point
          this.memento.splice(0, exists)
        } else {
          this.memento.unshift(v)
        }
      }
    }
  }
}
</script>
