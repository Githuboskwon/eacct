<template>
  <div class="modal-card" id="draggable">
    <header class="modal-card-head pop-header">
      <button class="icon is-right" @click="move" style="position:absolute;top:18px;right:70px;z-index:30;"><i class="fas fa-window-maximize" style="color:#fff;font-size:22px;padding-top:1px"></i></button>
      <p class="modal-card-title tit">
        권한별 메뉴 내역
        <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
      </p>
    </header>
    <section class="modal-card-body pop-content">
      <div class="inner-box">

        <div class="btn-wrap btn-type1 clearfix" >
          <button class="btn-size btn-blue fl_right" @click="buttonClickEventSave()">
            <span class="btn-icon"><i class="fas fa-save"></i></span>
            저장
          </button>
        </div>

        <div class="table-area">
          <div class="table-b">
            <!-- <dhx-grid ref="grid" v-model="data" :config="config" @constructGridSuccessful="constructGridSuccessful" /> -->
            <ag-grid-vue
                ref="grid"
                style="width: 100%; height: 55vh; min-height: 300px;"
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

          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import CheckboxCellRenderer from '@/components/agGrid/ap_checkbox-cell-renderer'

export default {
  props: {
    'roleCd': {
      type: String,
      required: true
    },
    'compCd': {
      type: String,
      required: true
    }
  },
  components: {},
  name: "ModalSlot",
  data() {
    return {
      data: [],
      isMxMn: true,
      gridOptions: {
        // onColumnResized: true,
        // enableFilter: true,
        // enableSorting: true,
        // suppressRowTransform: true
      },
      defaultColDef: {
        resizable: true,
        filter: true,
        sortable: true,
        editable: false
      },
      gridApi : null ,    //gridAip
      columnApi : null ,  //columApi
      columnDefs: [] ,    //comulum Defs
      frameworkComponents: {
        checkbox: CheckboxCellRenderer
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

      setTimeout(() => {
        this.createColumnDefs();
      },300);

      $(".modal-card").addClass('pop_min');
      $(".animation-content").addClass('pop_min');
      this.isMxMn = true

      this.query();
    },
    //컬럼정의 함수
    createColumnDefs() {
      const vm = this

      this.columnDefs = [
        {headerName: 'No.'
          , width : 60
          , valueGetter: (params) => {return params.node.rowIndex + 1 }
          , filter: false
          , sortable: false
          , editable: false
        },
        {
          headerName: '권한'
          , width : 105
          , field: 'roleCk'
          , editable: true
          , cellRenderer: 'checkbox'
          , cellRendererParams: {
            trueValue: true    //true값  지정 defalut-"Y" ex) "Y", true, "1"
            , falseValue: false  //false값 지정 defalut-"N" ex) "N", false, "0"
          }
        },
        {
          headerName: '메뉴'
          , width : 350
          , field: 'menuNm'
          , cellStyle : {'justify-content': 'left', 'display': 'flex'}
          , cellRenderer : vm.menuNoDraw
        },
        {
          headerName: '메뉴설명'
          , width : 350
          , cellStyle : {'justify-content': 'left', 'display': 'flex'}
          , field: 'menuDc'
        },

      ];
    },
    //그리드 Cell 변경 이벤트
    onCellValueChanged(event) {
      const idx = event.rowIndex
      const field = event.colDef.field

      if(field == "roleCk"){

        let row = this.gridApi.getSelectedRows()[0]

        row[field] = event.newValue

        _setChildren.apply(this, [row.children, event.newValue])
        _setParents.apply(this, [row,event.newValue])

        function _setChildren(array, state) {

          if (Array.isArray(array) && array.length > 0) {
            for (var i = 0, node = array[i], size = array.length; i < size; node = array[++i]) {
              node.roleCk = state
              _setChildren.apply(this, [node.children, state])
            }
          }
        }

        function _setParents(row, state) {
          if (!state) {
            return
          } else {
            let array = this.data.filter(x => x.menuNo === row.upperMenuNo)
            if (Array.isArray(array) && array.length > 0) {
              for (var i = 0, node = array[i], size = array.length; i < size; node = array[++i]) {
                node.roleCk = state
                _setParents.apply(this, [node, state])
              }
            }
          }
        }

        this.gridApi.refreshCells();
      }
    },
    move(){
      if(this.isMxMn) {
        $(".modal-card").removeClass('pop_min')
        $(".modal-card").addClass('pop_max');

        $(".animation-content").removeClass('pop_min')
        $(".animation-content").addClass('pop_max');
        this.isMxMn = false
      } else {
        $(".modal-card").removeClass('pop_max')
        $(".modal-card").addClass('pop_min');

        $(".animation-content").removeClass('pop_max')
        $(".animation-content").addClass('pop_min');
        this.isMxMn = true
      }
      this.gridApi.sizeColumnsToFit();
    },
    constructGridSuccessful(grid) {},
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
        if(!params.node.data.children){
          return 'far fa-file'
        }else{
          if(params.node.data.children.length > 0){
            return 'far fa-folder-open'
          }else{
            return 'far fa-file'
          }
        }
      }

      function _menuNo() {
        if(params.node.data.menuNm){
          return params.node.data.menuNm
        }else{
          return ''
        }
      }

      return template
    },
    query() {
      this.$store.commit('loading')
      this.$http.get('/api/auth/menu', {
        params: {
          roleCd: this.roleCd,
          compCd: this.compCd
        }
      }).then(response => {
        let root = []
        root = response.data.map(x => {
          x.roleCk = x.roleCk === '1' ? true : false
          return x
        }).filter(x => response.data.findIndex(y => y.menuNo === x.upperMenuNo) < 0)

        _.forEach(response.data, node => {
          _constructTreeData(root, node)
        })

        this.data = response.data

        function _constructTreeData(root, node) {
          let rst = false
          for (let i = 0, r = root[i]; i < root.length; r = root[++i]) {
            if (r.menuNo === node.upperMenuNo) {
              r.children = r.children || []
              r.children.push(node)
              return true
            } else if (Array.isArray(r.children)) {
              if (_constructTreeData(r.children, node)) {
                return true
              }
            }
          }
          return rst
        }
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    buttonClickEventSave() {
      this.$store.commit('loading')
      let data = _.cloneDeep(this.data).map(x => {
        delete x.children
        x.menuNo = x.menuNo
        x.roleCk = x.roleCk ? '1' : '0'
        x.roleCd = x.roleCd == "" && x.roleCk ? this.roleCd : x.roleCd
        x.compCd = x.compCd == "" && x.roleCk ? this.compCd : x.compCd
        return x
      }).filter(x => x.roleCk === '1')
      this.$http.put(`/api/auth/menu/${this.roleCd}/${this.compCd}`, data)
          .then(response => {
            this.$emit('close');
            this.$swal({type: 'success', text: '처리되었습니다.'});
          }).catch(response => {
        console.error(response)
      }).finally(() => {
        this.$store.commit('finish')
      });
    }
  },
  created() {
    this.query()
  },
  mounted() {
    //pop slots modal move
    $(".animation-content").addClass('pop_min');
    $(".animation-content").draggable();
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '4');
  },
  destroyed() {
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '7');
  }
}
</script>
