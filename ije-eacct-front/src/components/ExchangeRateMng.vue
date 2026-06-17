<template>
    <div class="inner-box">
        <!-- 타이틀 영역 -->
        <div class="content-name">
            <h2 class="dp-ib">{{title}}</h2>
            <div class="btn-wrap btn-type1 clearfix">
                <button type="button" class="btn-size btn-gray fl_left" @click="goSearch">
                    <span class="btn-icon">
                        <i class="fas fa-search"></i>
                    </span>
                    {{this.$i18n.messages[this.$store.state.locale].search}}
                </button>
                <button type="submit" class="btn-size btn-blue fl_left"  @click="save">
                    <span class="btn-icon">
                        <i class="fas fa-save"></i>
                    </span>
                    {{this.$i18n.messages[this.$store.state.locale].save}}
                </button>
            </div>
        </div>
        <!-- 검색조건 영역 -->
        <div class="search-form">
          <div class="form-group">
            <span class="control-label-req" id="search3">환율일자</span>
            <div class="form-input">
              <div class="datepicker w-type-ymd02">
                <dhx-calendar class="input ddate sDate" lbl="search3" v-model="form.fromConvDate" />
              </div>
              <span class="wave"> ~ </span>
              <div class="datepicker w-type-ymd02">
                <dhx-calendar class="input ddate sDate" lbl="search3"  v-model="form.toConvDate" />
              </div>
            </div>
          </div>

          <div class="form-group">
            <label class="control-label">기준통화</label>
            <div class="form-input">
              <select class="select is-fullwidth" id="fromCurrency" v-model="form.fromCurrency">
                <option value=''>==전체==</option>
                <option v-for="{ key, value } in options['CUR_CD']" :key="key" :value="key" v-text="value" />
              </select>
            </div>
          </div>
        </div>
        <!-- 테이블 -->
        <div class="table-area mt20">
            <div class="table-b">
                <div class="table-header">
                <div class="table-name">
                    <h3 class="ico_table_name">환율내역</h3>
                </div>
                <div class="btn-wrap btn-type2 clearfix">
                    <button class="btn-size btn-w-gray" @click="addRow()">
                    <span class="btn-icon">
                        <i class="fas fa-plus"></i>
                    </span>{{this.$i18n.messages[this.$store.state.locale].addRow}}
                    </button>
                    <button class="btn-size btn-w-gray" @click="deleteRow">
                    <span class="btn-icon">
                        <i class="fas fa-trash-alt"></i>
                    </span>{{this.$i18n.messages[this.$store.state.locale].delRow}}
                    </button>
                </div>
                </div>                
                    <ag-grid-vue
                        ref="grid"
                        style="width: 100%; height: 65vh; min-height: 510px;"
                        class="ag-theme-alpine"
                        rowSelection="multiple"
                                                
                        :columnDefs="columnDefs"                        
                        :rowData="data"
                        :gridOptions="gridOptions"   
                        :defaultColDef="defaultColDef"
                        :frameworkComponents="frameworkComponents"    
                        @grid-ready="onReady"                                             
                        @cell-clicked="onCellClicked"
                        @cell-value-changed="onCellValueChanged" 
                    />                    
                </div>
            
        </div>
        <!-- //테이블 -->
    </div>
</template>

<script>
    import mixin from '@/mixin';
    import mixinSlip from '@/mixin/slip';
    import common from '@/mixin/common';
    import _ from 'lodash'
    import DhxCalendar from '@/components/DhxCalendar.vue'

    import DatepickerCellRenderer from '@/components/agGrid/datepicker-cell-renderer'
    import MaskEditCellRenderer from '@/components/agGrid/maskedit-cell-renderer'    
    import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
    import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'
    import NumberInputCellRender from '@/components/agGrid/numberinput-cell-renderer'
    import InputCellRender from '@/components/agGrid/input-cell-renderer'
    import ButtonCellRenderer from '@/components/agGrid/button-cell-renderer'


    /**
     * Url JOIN
     */
    function _url(...args) {
      args = args.map(x =>
          String(x || "")
              .trim()
              .replace(/^\/*|\/*$/g, "")
      );
      return args.filter(x => x).join("/");
    }

    export default {
        name: 'CardUseLst',
        mixins: [mixin, mixinSlip, common],
        components: {DhxCalendar},
        data() {
            return {
                title: '환율관리',
                options: {
                    'CUR_CD': [],
                },
                compCds: [],
                crdCompCds: [],
                useDtlsStatCds: [],
                crdFgCds: [],
                data: [],
                authority: '',
                form: {
                  fromConvDate: this.$moment().format('YYYY'),
                  toConvDate: this.$moment().format('YYYY-MM-DD'),
                  fromCurrency: '',
                  curCd : ''
                },
                gridOptions: {         
                   //enableColResize: true,
                   //enableFilter: true,
                   //animateRows: false,
                   //enableSorting: true
                },
                frameworkComponents: {
                    datePicker: DatepickerCellRenderer,
                    maskEdit: MaskEditCellRenderer,
                    select: SelectCellRenderer,
                    checkBox: CheckboxCellRenderer,
                    numberInput: NumberInputCellRender,
                    input: InputCellRender,
                    button: ButtonCellRenderer
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
            };
        },
        methods: {
            //그리드 Ready
            onReady(params) {
                //그리드용 api 정의
                this.gridApi = params.api;
                this.columnApi = params.columnApi;

                setTimeout(() => {
                  this.createColumnDefs();
                },600);
                  this.goSearch();

                //this.gridApi.sizeColumnsToFit();
            },
            addRow() {      
                const row = this.gridApi.getSelectedNodes()     
                const rowIndx = row.length < 1 ? 0 : row[0].rowIndex + 1

                this.data.splice(rowIndx, 0, {
                    new: true

                })

                setTimeout(() => {
                    this.gridApi.ensureIndexVisible(rowIndx, 'middle');
                    this.gridApi.getRowNode(rowIndx).setSelected(true, true);    
                }, 0)
            },
            deleteRow() {
                const row = this.gridApi.getSelectedNodes()
                if (row.length < 1) {
                    this.$swal({
                    type: 'error',
                    text: '삭제할 행을 선택하여주세요.'
                    })
                } else {
                  let vm = this
                  this.$swal({
                    type: 'question',
                    text: '선택된 환율정보를 삭제 하시겠습니까?',
                    showCancelButton: true
                  }).then(r => {
                    if (r.value) {
                      const rowIndex = row[0].rowIndex
                      const isNew = this.data[rowIndex].new
                      const fromConvDate = this.data[rowIndex].fromConvDate
                      const fromCurrency = this.data[rowIndex].fromCurrency
                      const toCurrency = this.data[rowIndex].toCurrency
                      this.data.splice(rowIndex, 1)
                      if (!isNew) {
                        this.$http.delete('/api/exchange/delete/' + fromConvDate + '/' + fromCurrency + '/' + toCurrency).then(response => {
                          this.$swal({ type: 'success', text: '삭제 되었습니다.' });
                          this.goSearch()
                          // Do nothing!
                        })
                      }
                    }
                  })
                }
            },
            //그리드 셀 클릭 이벤트 함수
            onCellClicked(event) {
                
                const idx = event.rowIndex
                const field = event.colDef.field
   
                if(field === "procCancel"){//클릭 컬럼 체크
                    
                    const useDtlsStatCd = event.node.data.useDtlsStatCd
                    
                    if(useDtlsStatCd === "20"){//컬럼 체크 값 확인
                        alert("Button Click")
                    }
                }else if( field === "button"){
                        alert("Button(Comp) Click")
                }
            },
            //컬럼 정의 함수
            createColumnDefs() {                
                const vm = this
                this.columnDefs = [
                    {headerName: 'No.', width : 80, valueGetter: (params) => {return params.node.rowIndex + 1 }}
                  , {headerName:'시작일자', headerClass: 'require-column', field:'fromConvDate', width: 120
                    , cellClass : function(params){
                        if(params.node.data.new){
                          return 'bg-lightpink'
                        }else{
                          return 'bg-grey'
                        }
                      }, editable: function (params) {
                        if (params.node.data.new) {
                          return true
                        } else {
                          return false
                        }
                      }, valueFormatter: function(params) {
                        return vm.getDateFormat(params.value);//숫자 포맷<common.js>
                      }
                    } //DatePicker Component
                  , {headerName:'종료일자', field:'toConvDate', width: 120
                      , cellClass : function(params){
                        if(params.node.data.new){
                          return 'bg-lightpink'
                        }else{
                          return 'bg-grey'
                        }
                      }, editable: function (params) {
                        if (params.node.data.new) {
                          return true
                        } else {
                          return false
                        }
                      }, valueFormatter: function(params) {
                        return vm.getDateFormat(params.value);//숫자 포맷<common.js>
                      }
                    }
                  , {headerName:'기준통화', headerClass: 'require-column', field:'fromCurrency', width:180, cellRenderer: 'select'
                      , cellRendererParams: {
                        options : vm.options["CUR_CD"],
                        detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                        detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
                      } //select Component
                      , cellClass : function(params){
                        if(params.node.data.new){
                          return 'bg-lightpink'
                        }else{
                          return 'bg-grey'
                        }
                      }
                      , editable: function (params) {
                        if (params.node.data.new) {
                          return true
                        } else {
                          return false
                        }
                      }
                    }
                  , {headerName:'대상통화', headerClass: 'require-column', field:'toCurrency', width:180, cellRenderer: 'select'
                      , cellRendererParams: {
                        options : vm.options["CUR_CD"],
                        detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                        detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
                      }
                    , cellClass : function(params){
                      if(params.node.data.new){
                        return 'bg-lightpink'
                      }else{
                        return 'bg-grey'
                      }
                    }
                    , editable: function (params) {
                      if (params.node.data.new) {
                        return true
                      } else {
                        return false
                      }
                    }
                    }
                  , {headerName:'환율', field:'convRate', width: 180,
                      valueFormatter: (params) => {
                        return vm.chkNumber(params, 'curRt');
                      }
                    }
                    //InputBox Component
                  , {
                    headerName: '비고', field: 'remark', width: 715
                  },
                ];
            },                       
            goSearch() {
              const param = JSON.parse(JSON.stringify(this.form));
              const stripFields = ['fromConvDate', 'toConvDate'];
              _.forEach(stripFields, (name) => param[name] = this.toPure(param[name]));
                //조회조건 필수 입력 체크 방법
                //1.필수입력 컨트롤 라벨 css(control-label-req) 정의
                //2.필수입력 컨트롤 라벨 id 정의
                //3.필수입력 컨트롤 lbl 태그에 라벨 id 정의
                //4.조회전 공통함수 호출
                if(!this.searchRequireCheck()) return;
              // code list 조회
              this.$store.commit('loading')
              this.$http
                  .post(_url("/api/exchange/rate/list"), param)
                  .then(response => {
                    this.data = response.data;

                    if(response.data.length == 0){
                      this.$swal({ type: 'warning', text: '조회내역이 존재하지 않습니다.' })
                    }
                  }).finally(() => {
                this.$store.commit('finish')
              });
            },
            save() {
              if(!this.gridRequireCheck([this.$refs.grid])) return;

              for(var i = 0;i<this.data.length;i++){
                for(var j = i+1; j<this.data.length;j++){
                  if(this.data[i].fromConvDate === this.data[j].fromConvDate
                      && this.data[i].fromCurrency === this.data[j].fromCurrency
                      && this.data[i].toCurrency === this.data[j].toCurrency){
                    this.$swal({type: 'warning', text: '이미 등록된 데이터가 존재합니다. 시작일자 : ' + this.data[i].fromConvDate + ', 기준통화 : ' + this.data[i].fromCurrency });
                    return false;
                  }
                }
              }

              //저장 시작
              this.$http
                  .put('/api/exchange/save',this.data)
                  .then(response => {
                    this.$swal({ type: 'success', text: response.data });
                    this.goSearch()
                  })
                  .catch(({ response }) => {
                    console.log(response)
                      this.$swal({ type: "error", text: response.data.message });
                  });
            },

            onCellValueChanged(event) {                
                const idx = event.rowIndex
                const field = event.colDef.field

                if(field === "useDtlsStatCd"){
                    //row refresh
                    var rows = [];
                    var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
                    
                    rows.push(rowNode)
                
                    this.gridApi.redrawRows({ rowNodes: rows });
                }
            },
            //통화 공통코드 조회
            queryCurrencyCd() {
              this.$store.commit('loading')
              this.$http.get('/api/code/combo', {
                params: {
                  groupCd: 'CUR_CD'
                }
              }).then(response => {
                this.options['CUR_CD'] = response.data
                //bus.$emit('selectBox.updated')
              }).catch(response => {
                return response
              }).finally(() => {
                this.$store.commit('finish')
              })
            },
            chkNumber(params, curRt){

              var result = '';
              var val = params.value;
              var data = this.data;

              if(!_.isEmpty(val) || _.isNumber(val)){
                val = val.toString();

                if (curRt === 'curRt'){
                  result = this.$numeral(val).format('0,0.00');
                } else {
                  result = this.$numeral(val).format('0,0');
                }

                data[params.node.id][params.column.colId] = this.$numeral(val).value();
              }

              return result
            },
        },
        mounted() {
            this.queryCurrencyCd();
        },
        created(){
        },
        watch: {
            'data' : {
                deep: true,
                handler(value){
                }
            }
        }        
    };    
</script>

<style scoped>
    .gridbox {
        height: 390px !important;
    }

    .gridbox .objbox {
        height: 350px !important;
    }

    .desc-content:after {
        clear: both;
        content: '';
        display: block;
    }

    .btn-wrap {
        margin-bottom: 20px;
    }

    .desc-content {
        border: 2px solid #9db6c9;
        background: #f9fafc;
        margin: 0 0 20px 0;
        border-radius: 4px;
        padding: 15px 2%;
        clear: both;
    }

    .desc-content .item {
        float: left;
    }

    .desc-content .item .desc-item {
        position: relative;
        padding-left: 82px;
        margin-bottom: 8px;
    }

    .desc-content .item .desc-item:last-child {
        margin-bottom: 0;
        height: 25px;
    }

    .desc-content .item .desc-item .tit {
        position: absolute;
        left: 0;
    }

    .desc-content .item .desc-item .label-tit {
        font-family: 'NotoM';
        color: #222;
        font-size: 15px;
    }

    .desc-content .item.desc-left .desc-item {
        padding-left: 90px;
    }

    .desc-content .item.desc-left .desc-item .desc:after {
        clear: both;
        content: '';
        display: block;
    }

    .desc-content .item.desc-left .desc-item .desc .datepicker {
        float: left;
    }

    .desc-content .item.desc-left .desc-item .desc span.wave {
        float: left;
        padding: 0 6px;
    }

    .desc-content .item.desc-left .desc-item .td-s-thumb.search-area:after {
        clear: both;
        content: '';
        display: block;
    }

    .desc-content .item.desc-left .desc-item .td-s-thumb.search-area input,
    .desc-content .item.desc-left .desc-item .td-s-thumb.search-area .ip-box {
        float: left;
    }

    .desc-content .item.desc-left .desc-item .desc.select select {
        width: 70%;
    }

    .desc-content .item.desc-left {
        width: 35%;
        padding-right: 40px;
    }

    .desc-content .item.desc-center {
        width: 35%;
        padding-right: 40px;
    }

    .desc-content .item.desc-right {
        width: 30%;
    }

    .search-area input {
        position: relative;
    }

    .search-area .icon {
        position: absolute;
        right: 8px;
        top: 1px;
        z-index: 100;
        cursor: pointer;
        font-size: 16px;
        color: #555;
    }

    .search-border .td-s-thumb {
        position: relative;
        display: inline-block;
    }

    .search-border .td-s-thumb.search-area > input,
    .search-border .td-s-thumb.search-area > .ip-box
    .search-border .td-s-thumb.search-area > button {
        float: left;
    }

    .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
        width: 60%;
        margin-left: 6px;
    }

    .remove_text {
        margin-left: 0;
    }

    .contents div.gridbox_material.gridbox .xhdr {
        border-bottom: 1px solid #dfdfdf;
    }

    @media (max-width: 1580px) {
        .search-border .td-s-thumb.search-area .ip-box.ip-box-w02 {
            width: 50%;
        }
    }
</style>
