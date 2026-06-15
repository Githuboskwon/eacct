<template>
  <layout>
    <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
    <div slot="content">
      <div class="btn-type1">
          <button class="btn-size btn-gray" @click="goSearch(true)"><span class="btn-icon"><i class="fas fa-search"></i></span> 조회</button>
      </div>

      <div class="pop-content-search">
          <div class="field has-addons ">
              <div class="mr20 ">
                  <span class="pop-c-name">비용항목명</span>
              </div>
              <div class="control is-expanded">
                  <input class="input" type="text" v-model="search" @keypress.enter="goSearch">
              </div>
          </div>
      </div>

      <div class="grid-wrap">
          <div id="erpGridbox" class="grid-tbl-box"></div>
      </div>
    </div>
  </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'
import mixin from '@/mixin';

export default {
  name: 'erpAccountPop',
  props: ['param','slipTypeCd', 'deptCd'],
  mixins: [ mixin ],
  data() {
    return {
      title: '계정과목 조회',
      search: '',
      grid: {
        head: [
          {id: 'no', type: 'cntr', align: 'center', sort: 'int', value: 'No.', width: 40},
          {id: 'expenseName', type: 'ro', align: 'left', sort: 'str', value: '비용항목', width: 180},
          {id: 'expenseAcctCode', type: 'ro', align: 'center', sort: 'str', value: '계정코드', width: 120},
          {id: 'expenseAcctName', type: 'ro', align: 'left', sort: 'str', value: '비용계정', width: 200},
          {id: 'expenseFlag', type: 'ro', align: 'left', sort: 'str', value: '비용구분', width: 80},
          {id: 'expenseId', type: 'ro', align: 'center', sort: 'str', value: '', width: 80},
        ],
        data: [],
      },
      returnObject: {}
    }
  },
  methods: {
    drawGrid() {
      // eslint-disable-next-line
      const iljinGrid = new dhtmlXGridObject('erpGridbox');
      this.$refs.grid = iljinGrid;
      iljinGrid.init();
      // iljingrid.enableAutoWidth(true);
      // iljingrid.setInitWidths("40,80,40,120,80");
      iljinGrid.setColumnHidden(5, true);
      iljinGrid.enableAlterCss('even', 'uneven');
      iljinGrid.parse(this.grid, 'js');
      this.$refs.grid.attachEvent('onRowDblClicked', (rId) => {
        this.doReturnObject(rId);
      });
      $('#erpGridbox').height('300');
      $('div.xhdr').css('padding-right', '16px');
      $('table.hdr').css('border-collapse', 'collapse');
      
      iljinGrid.setSizes()
    },
    goSearch(slow) {
      this.$store.commit('loading');
      this.grid.data = [];

      this.$http.get(_url('/api/erp_account', this.deptCd, this.search))
        .then((result) => {
          this.grid.data = result.data
          
          this.$refs.grid.parse(this.grid, 'js');
          $(this.$refs.grid.entBox).height(300);
          this.$refs.grid.setSizes();
          this.drawGrid()

          if(!slow && this.grid.data.length === 1) {
            this.doReturnObject(1);
          }
        })
        .finally(() => {
          this.$store.commit('finish');
        });
    },
    doReturnObject(rId) {
      this.returnObject.name = 'erpAccountPop';
      this.returnObject.expenseName = this.$refs.grid.cells(rId, 1).getValue();
      this.returnObject.expenseAcctCode = this.$refs.grid.cells(rId, 2).getValue();
      this.returnObject.expenseAcctName = this.$refs.grid.cells(rId, 3).getValue();
      this.returnObject.expenseId = this.$refs.grid.cells(rId, 5).getValue();
      this.$parent.$emit('receive', this.returnObject);
      this.$emit('close', this.returnObject)
      this.closeModal();
    },
    closeModal() {
      this.$parent.close();
    }
  },
  mounted() {
    if(this.param !== undefined) this.search = this.param;
    this.drawGrid();
    if(this.search) this.goSearch();
  },
  components: {
    Layout
  }
};

function _url(...args) {
  args = args.map(x => String(x || '').trim().replace(/^\/*|\/*$/g, ''))
  args = args.filter(x => x)
  return args.join('/')
}
</script>

<style>
div.objbox {
  overflow: scroll
}
</style>
<style scoped>
div#gridbox {
  width: 100%;
  height: 100%;
  min-height: 300px;
}

div.xhdr {
  padding-right: 16px;
}

</style>
