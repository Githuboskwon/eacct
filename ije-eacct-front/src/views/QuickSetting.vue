<template>
    <div class="inner-box">
        <div class="content-name">
            <h2 class="dp-ib">{{this.$i18n.messages[this.$store.state.locale].confSetting}}</h2>
            <div class="btn-type1 clearfix">
              <el-button size="large" type="success" icon="el-icon-folder-checked" @click="submit">저장</el-button>
            </div>
            <div class="btn-wrap btn-type1 clearfix" style="margin-top: 7px;">
              <input type="radio" id="Basic" value="/css/common.css" v-model="color" @click="showAlarm()">
              <label for="Basic">Basic</label>
              <input type="radio" id="Mint" value="/css/common_mt.css" v-model="color" @click="showAlarm()">
              <label for="Mint">Mint</label>
              <input type="radio" id="Orange" value="/css/common_or.css" v-model="color" @click="showAlarm()">
              <label for="Orange">Orange</label>
              <input type="radio" id="Violet" value="/css/common_pp.css" v-model="color" @click="showAlarm()">
              <label for="Violet" style="padding-right:30px;">Violet</label>
            </div>
        </div>

        <!-- 테이블 -->
       <ag-grid-vue         
        ref="grid"
        style="width: 100%; height: 420vh; min-height: 300px;"
        class="ag-theme-alpine"    
        rowSelection="multiple"                                  

        :columnDefs="columnDefs"
        :defaultColDef="defaultColDef"
        :gridOptions="gridOptions"       
        :rowData="mysets"
        :frameworkComponents="frameworkComponents"          

        @grid-ready="onReady"
        @cell-value-changed="onCellValueChanged"        
      />       
    </div>
</template>

<script>
    import Vue from 'vue'
    import mixin from '@/mixin';
    import mixinSlip from '@/mixin/slip';
    import SelectCellRenderer from '@/components/agGrid/select-cell-renderer'
    import CheckboxCellRenderer from '@/components/agGrid/checkbox-cell-renderer'

    const bus = new Vue()
    
    export default {
        name: 'QuickSetting',
        mixins: [mixin, mixinSlip],
        data() {
            return {
                title: '환경설정',
                mysets:[],
                options:
                [{'key': 'null', 'value' : ''},
                  {'key': '1', 'value' : '1'},
                  {'key': '2', 'value' : '2'},
                  {'key': '3', 'value' : '3'},
                  {'key': '4', 'value' : '4'},
                  {'key': '5', 'value' : '5'},
                  {'key': '6', 'value' : '6'},
                ],
                menuIconCds:[],
                menuNo:[],
                authority: '',
                compCd:'',
                loginId:'',
                checkId:[],
                checkList:[],
                chkSaveList:[],
                IconCdv:[],
                Icon:[],
                saveCheck:[],
                saveCheckID:[],
                resultId:[],
                resultCd:[],
                color:'',
                id:'chk',
                defaultColDef: {
                    resizable: true,
                    filter: true,
                    sortable: true,
                    editable: false,
                    width: 400 , 
                    cellStyle : {'justify-content': 'center','align-items': 'center', 'display': 'flex','height': '100%'}
                }, 
                gridOptions: {          
                    rowHeight: 100
                },                             
                gridApi : null ,    //gridAip
                columnApi : null ,  //columApi
                columnDefs: [] ,    //comulum Defs
                frameworkComponents: {
                    selectCellRenderer: SelectCellRenderer,
                    checkboxRenderer: CheckboxCellRenderer
                },
            };
        },

        methods: {
            //그리드 Ready
            onReady(params) {            
                //그리드용 api 정의
                //this.gridApi = params.api;
                //this.columnApi = params.columnApi; //params대신 gridOptions 사용가능
                this.gridApi = params.api;
                this.columnApi = params.columnApi;

                this.createColumnDefs();

                this.getMySet();
            },
            //컬럼정의 함수
            createColumnDefs() { 
                const vm = this

                this.columnDefs = [
                    {  
                        headerName: this.$i18n.messages[this.$store.state.locale].select
                      , field:'regYn'
                      , width : 150
                      , editable: true
                      , cellRenderer: 'checkboxRenderer'
                      , cellRendererParams: {
                           trueValue: "1"    //true값  지정 defalut-"Y" ex) "Y", true, "1"
                         , falseValue: "0"  //false값 지정 defalut-"N" ex) "N", false, "0"
                      }                      
                    },
                    {
                        headerName: this.$i18n.messages[this.$store.state.locale].main
                      , width : 150
                      , cellRenderer: function(params){                          
                            if(params.node.data.regYn === "1"){
                                return "등록"
                            }                        
                        }
                    },
                    {
                      headerName: "메뉴순서"
                      , field:'menuOrdr'
                      , width: 100
                      , editable: true
                      , cellRenderer: 'selectCellRenderer'
                      , cellRendererParams: {
                        options : vm.options,
                        detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                        detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
                      }
                    },
                    {
                        headerName: this.$i18n.messages[this.$store.state.locale].menu
                      , field:'menuName'                 
                      , width : 520
                    },                    
                    {  
                        headerName: this.$i18n.messages[this.$store.state.locale].icon
                      , field:'menuIconCd'
                      , editable: true
                      , cellRenderer: 'selectCellRenderer'
                      , cellRendererParams: {
                            options : vm.menuIconCds,
                            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                            detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
                        }
                    },
                    {  
                        headerName: this.$i18n.messages[this.$store.state.locale].iconImg                 
                      , cellRenderer : vm.handleChange
                    }
                ];
            },            
            /*설정화면 전체 데이터 가져오기*/
            getMySet() {
                this.$http.get(`/api/settings/${this.$store.state.loginInfo.loginId}`)
                    .then(response => {
                        this.mysets = response.data
                    });
            },
            /*아이콘 셀렉트박스 코드값 가져오기*/
            getMenuIconCdCombo() {
                this.$http.get(`/api/code/combo`, {params: {groupCd: "MENU_ICON_CD"}})
                    .then(response => {
                        this.menuIconCds = response.data;
                        bus.$emit('selectBox.updated')
                    });
            },
            /*아이콘 셀렉트박스 이벤트*/
            handleChange(params){
                
                let imgtag='';
                
                switch (params.node.data.menuIconCd) {
                    case '10':
                      imgtag = '<img src="img/icon_maintop_01_b.png"/>';
                      break;
                    case '20':
                      imgtag = '<img src="img/icon_maintop_02_b.png"/>';
                      break;
                    case '30':
                      imgtag = '<img src="img/icon_maintop_03_b.png"/>';
                      break;
                    case '40':
                      imgtag = '<img src="img/icon_maintop_04_b.png"/>';
                      break;
                    case '50':
                      imgtag = '<img src="img/icon_maintop_05_b.png"/>';
                      break;
                    case '60':
                      imgtag = '<img src="img/icon_maintop_06_b.png"/>';
                      break;
                    case '70':
                      imgtag = '<img src="img/icon_maintop_06_01_b.png"/>';
                      break;
                    case '80':
                      imgtag = '<img src="img/icon_maintop_06_02_b.png"/>';
                      break;
                    case '90':
                      imgtag = '<img src="img/icon_maintop_06_03_b.png"/>';
                      break;
                    case '100':
                      imgtag = '<img src="img/icon_maintop_06_04_b.png"/>';
                      break;
                    case '110':
                      imgtag = '<img src="img/icon_maintop_06_05_b.png"/>';
                      break;
                    case '120':
                      imgtag = '<img src="img/icon_maintop_06_06_b.png"/>';
                      break;
                }
                return imgtag
            },
            checkDuplicateMenuOrdr(dataList) {
              let seenMenuOrdr = new Set();
              let duplicateMenuOrdr = [];

              for (let i = 0; i < dataList.length; i++) {
                let item = dataList[i];
                let menuOrdr = item.menuOrdr;

                if (seenMenuOrdr.has(menuOrdr)) {
                  duplicateMenuOrdr.push(menuOrdr);
                } else {
                  seenMenuOrdr.add(menuOrdr);
                }
              }

              return duplicateMenuOrdr;
            },
            submit() {
                let saveList = []
                
                /* 체크된 데이터 가져오기*/
                const grid = this.$refs.grid

                const chkDatas = grid.rowData.filter((x,i) => {
                    return x.regYn === "1"
                })

                if(chkDatas.length > 6) {
                    this.$swal({ type: 'warning', text: '메인아이콘은 6개를 초과할 수 없습니다.' })
                    return;
                }

                const duplicateMenuOrdrValues = this.checkDuplicateMenuOrdr(chkDatas);

                if(duplicateMenuOrdrValues.length > 0){
                  this.$swal({ type: 'warning', text: duplicateMenuOrdrValues+' 의 메뉴 순서가 중복 되었습니다.' })
                  return;
                }

                const uncheckIcon = chkDatas.find( x => {
                    let saveQuick = {
                        "compCd": this.compCd,
                        "loginId": this.loginId,
                        "menuNo": x.menuNo,
                        "menuIconCd": x.menuIconCd,
                        "menuOrdr": x.menuOrdr
                    };
                    
                    saveList.push(saveQuick);

                    return !x.menuIconCd
                })
                // 색상 테마 설정
                // 로컬 스토리지에서 컬러 값을 넣어준다.
                // key = color_loginId , value = css address
                if(this.color != ''){
                  localStorage.setItem('color_'+this.loginId , this.color);
                }

                if(uncheckIcon){
                    this.$swal({type: 'warning', text: '아이콘을 선택해 주세요.'})
                }else{
                    this.$http.put(`/api/settings/`, saveList)
                     .then(response => {
                         this.$swal({ type: 'success', text: '저장되었습니다' }).then(function (){
                           window.location.reload(true);
                         });
                         this.getMySet()
                     }).catch(response => {
                        console.error(response)
                     })
                }
            },
            onCellValueChanged(event) {                
                const idx = event.rowIndex
                const field = event.colDef.field
                
                if(field === "regYn" || field === "menuIconCd"){
                    //row refresh
                    var rows = [];
                    var rowNode = this.gridApi.getDisplayedRowAtIndex(idx);
                    
                    rows.push(rowNode)
                
                    this.gridApi.redrawRows({ rowNodes: rows });
                }
            },
            showAlarm(){
              this.$swal({type:'info',text: '저장하시면 테마가 적용됩니다.'})
            }
        },
        created() {
            bus.$on('selectBox.updated', () => {
                 //셀렉트 박스 안에 맵핑될 데이터가 그리드에 반영되기 위해 비동기 처리
                 this.createColumnDefs();
            })
        },
        mounted() {
            this.authority = this.$store.state.loginInfo.authorities[0].roleCd;
            this.loginId = this.$store.state.loginInfo.loginId;
            this.compCd = this.$store.state.loginInfo.compCd;
            this.getMenuIconCdCombo();
        }       
    };
</script>
