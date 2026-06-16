<template>
<div class="modal-card">
  <header class="modal-card-head pop-header">
    <p class="modal-card-title tit">
      권한별 메뉴 내역
      <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
    </p>
  </header>
  <section class="modal-card-body pop-content">
    <div class="inner-box">
      <div class="content-name">
        <div class="btn-wrap btn-type1 clearfix" style="float: right; margin-top: -25px;">
          <button class="btn-size btn-blue fl_left" @click="buttonClickEventSave()">
            <span class="btn-icon"><i class="fas fa-save"></i></span>
            저장
          </button>
        </div>
      </div>

      <div class="table-area">
        <div class="table-b">
          <ag-grid-vue ref="grid"
                       style="width: 100%; height: 450px;"
                       class="ag-theme-alpine"
                       :columnDefs="columnDefs"
                       :rowData="data"
                       :gridOptions="gridOptions"
                       :defaultColDef="defaultColDef"
                       :frameworkComponents="frameworkComponents"
                       @grid-ready="onGridReady"
                       @cell-value-changed="onCellValueChanged" />
        </div>
      </div>
    </div>
  </section>
</div>
</template>

<script>
import { AgGridVue } from 'ag-grid-vue'
import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'

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
  components: {
    AgGridVue
  },
  data() {
    return {
      data: [],
      columnDefs: [
        {headerName: 'No.', width: 60, cellStyle: {textAlign: 'center'}, valueGetter: params => params.node.rowIndex + 1},
        {
          headerName: '권한', field: 'roleCk', width: 70, cellStyle: {textAlign: 'center'},
          cellRenderer: 'checkboxRenderer',
          cellRendererParams: {trueValue: true, falseValue: false}
        },
        {
          headerName: '메뉴', field: 'menuNm', width: 260, cellStyle: {textAlign: 'left'},
          cellRenderer: (params) => {
            const d = params.data || {};
            const hasChildren = Array.isArray(d.children) && d.children.length > 0;
            const ml = (d.menuLv || 0) * 15;
            const icon = hasChildren ? 'far fa-folder-open' : 'far fa-file';
            const text = params.value == null ? '' : params.value;
            return `<div style="margin-left:${ml}px"><i class="${icon}"></i> ${text}</div>`;
          }
        },
        {headerName: '메뉴설명', field: 'menuDc', width: 260, cellStyle: {textAlign: 'left'}},
      ],
      gridOptions: {},
      defaultColDef: {resizable: true, sortable: false, filter: false},
      frameworkComponents: {checkboxRenderer: CheckboxCellRenderer},
      gridApi: null,
    }
  },
  methods: {
    onGridReady() {
      this.gridApi = this.gridOptions.api;
    },
    // 체크박스 변경 시 상/하위 메뉴 권한 동기화 (DHTMLX onCheck 대체)
    onCellValueChanged(params) {
      if (params.colDef.field !== 'roleCk') return;
      const state = params.value;
      const row = params.data;
      this.setChildren(row.children, state);
      this.setParents(row, state);
      if (this.gridApi) this.gridApi.refreshCells({force: true});
    },
    setChildren(array, state) {
      if (Array.isArray(array) && array.length > 0) {
        array.forEach(node => {
          node.roleCk = state;
          this.setChildren(node.children, state);
        });
      }
    },
    setParents(row, state) {
      if (!state) return;
      const array = this.data.filter(x => x.menuNo === row.upperMenuNo);
      array.forEach(node => {
        node.roleCk = state;
        this.setParents(node, state);
      });
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
      
      let data = _.cloneDeep(this.data).map(x => {
        delete x.children 
        x.roleCk = x.roleCk ? '1' : '0'
        x.roleCd = this.roleCd
        x.compCd = this.compCd
        return x
      }).filter(x => x.roleCk === '1')

      this.$http.put(`/api/auth/menu/${this.roleCd}/${this.compCd}`, data)
        .then(response => {
          this.$swal({ type: 'success', text: '저장되었습니다' });
          this.$emit('close')
        }).catch(response => {
          console.error(response)
        })

    }
  },
  created() {
    this.query()
  }
}
</script>
