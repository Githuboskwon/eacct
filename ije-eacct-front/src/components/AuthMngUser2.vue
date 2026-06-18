<template>
<div class="modal-card" id="draggable">
  <header class="modal-card-head pop-header">
    <button class="icon is-right" @click="move" style="position:absolute;top:18px;right:70px;z-index:30;"><i class="fas fa-window-maximize" style="color:#fff;font-size:22px;padding-top:1px"></i></button>
    <p class="modal-card-title tit">
      권한별 사용자 내역
      <button class="btn-pop-close delete" aria-label="close" @click="$parent.close()"></button>
    </p>
  </header>
  <section class="modal-card-body pop-content">
    <div class="inner-box">
      <div class="content-name">
        <div class="btn-wrap btn-type1 clearfix" style="float: right;">
          <button class="btn-size btn-blue fl_left" @click="query">
            <span class="btn-icon"><i class="fas fa-search"></i></span>
            조회
          </button>
          <button class="btn-size btn-blue fl_left" @click="buttonClickEventSave()">
            <span class="btn-icon"><i class="fas fa-save"></i></span>
            저장
          </button>
        </div>
      </div>
      <br>
      <br>
      
      <div class="pop-content-search">
        <div class="field has-addons ">
          <div class="control is-expanded search-area">
            <div class="ip-box rs-mt2">
            <span class="NotoM" style="margin-right: 10px;">- 성명</span>
                <input class="input" type="text" v-model="name" @keypress.enter="query" style="padding-right:20px; width: 175px;">
                <button class="icon is-right" @click="query"><i class="fas fa-search"></i></button>
            </div>
          </div>
          <div class="control is-expanded search-area">
            <span class="NotoM" style="margin-right: 10px;">- 부서</span>
            <div class="ip-box rs-mt2">
                <input class="input" type="text" v-model="dept" @keypress.enter="query" style="padding-right:20px; width: 175px;">
                <button class="icon is-right" @click="query"><i class="fas fa-search"></i></button>
            </div>
          </div>
        </div>
      </div>

      <div class="table-area">
        <div class="table-b">
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
          />
      <!-- //Grid -->
        </div>
      </div>
    </div>
  </section>
</div>
</template>

<script>

import CheckboxCellRenderer from '@/components/agGrid/ap_checkbox-cell-renderer'

export default {
  compatConfig: { MODE: 2 },
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
  data() {
    return {
      data: [],
      isMxMn: true,
      name: '',
      dept: '',
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
            headerName: '권한부여'
          , width : 105 
          , field: 'roleChk'
          , editable: true
          , cellRenderer: 'checkbox'
          , cellRendererParams: {
                  trueValue: true    //true값  지정 defalut-"Y" ex) "Y", true, "1"
                , falseValue: false  //false값 지정 defalut-"N" ex) "N", false, "0"
              }
          }, 
          {
            headerName: '현재권한'
          , width : 140 
          , field: 'crtRole'
          },
          {
            headerName: '사번'
          , width : 140 
          , field: 'empNo'
          },
          {
            headerName: '성명'
          , width : 140 
          , field: 'empNm'
          },
          {
            headerName: '직급'
          , width : 140 
          , field: 'dutNm'
          },  
          {
            headerName: '부서'
          , width : 140 
          , field: 'deptNm'
          }                 
        ];
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
    query() {
      this.$store.commit('loading')
      this.$http.get(`/api/auth/user`, {
        params: {
          roleCd: this.roleCd,
          compCd: this.compCd,
          value1: this.name,
          value2: this.dept
        }
      }).then(response => {
        this.data = response.data.map(x => {
          x.roleChk = x.roleChk === '1' ? true : false
          x.compCd = this.compCd
          return x
        })
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    buttonClickEventSave() {
      this.$store.commit('loading');
      let data = _.cloneDeep(this.data).map(x => {
        delete x.children
        x.roleChk = x.roleChk ? '1' : '0'
        x.roleCd = this.roleCd
        x.compCd = this.compCd
        return x
      })
      this.$http.put(`/api/auth/user/${this.roleCd}/${this.compCd}`, data)
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
