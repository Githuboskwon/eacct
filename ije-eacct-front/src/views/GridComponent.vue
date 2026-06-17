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
                <button type="submit" class="btn-size btn-blue fl_left">
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
            <label class="control-label-req" id="search1">조회조건1</label>
            <div class="form-input">
                <input class="input Rt-M tal" type="text" id="group_cd" lbl="search1" v-model="form.search1" />
            </div>
            </div>
            <div class="form-group">
                <label class="control-label">조회조건2</label>
                <div class="form-input">
                    <select class="select is-fullwidth" id="use_yn" v-model="form.search2">
                    <option value="">-전체-</option>
                    <option value="Y">예</option>
                    <option value="N">아니오</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <span class="control-label-req" id="search3">조회조건3</span>
                <div class="form-input">
                    <div class="datepicker w-type-ymd02">
                        <dhx-calendar class="input ddate sDate" lbl="search3" v-model="form.search3" />
                    </div>
                    <span class="wave"> ~ </span>
                    <div class="datepicker w-type-ymd02">
                        <dhx-calendar class="input ddate sDate" lbl="search3"  v-model="form.search4" />
                    </div>
                </div>
            </div>    
        </div>        
        <!-- 테이블 -->
        <div class="table-area mt20">
            <div class="table-b">
                <div class="table-header">
                <div class="table-name">
                    <h3 class="ico_table_name">Component List</h3>
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

    export default {
        name: 'CardUseLst',
        mixins: [mixin, mixinSlip, common],
        components: {DhxCalendar},
        data() {
            return {
                title: 'Grid Component',
                options: {
                    'ROLE_SELECT_CD': [
                        {key:"10", value : "Select1"},
                        {key:"20", value : "Select2"},
                        {key:"30", value : "Select3"}
                    ]
                }, 
                compCds: [],
                crdCompCds: [],
                useDtlsStatCds: [],
                crdFgCds: [],
                data: [],
                authority: '',
                form: {
                    search1: '123',
                    search2: '',
                    search3: this.$moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD'),
                    search4: this.$moment().format('YYYY-MM-DD'),                    
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

                //this.gridApi.sizeColumnsToFit();
            },
            //그리드 합계 계산 함수            
            makeHeaderTotal() {
                
                const initValue = 0;
                
                let sum = [{
                    rn: '합계'
                    , purchTot: this.data.reduce((prev, next) => {return prev + next.purchTot}, initValue)
                    
                }]

                this.gridOptions.api.setPinnedTopRowData(sum)                
            },
            addRow() {      
                const row = this.gridApi.getSelectedNodes()     
                const rowIndx = row.length < 1 ? 0 : row[0].rowIndex + 1

                this.data.splice(rowIndx, 0, {
                    purchTot : 0
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
                    const rowIndex = row[0].rowIndex
                    // const isNew = this.data[rowIndex].new
                    // const groupCd = this.data[rowIndex].groupCd

                    this.data.splice(rowIndex, 1)

                    // if (!isNew) {
                    //     this.$http.delete('/api/code/delete/' + groupCd).then(response => {
                    //         this.$swal({ type: 'success', text: '삭제 되었습니다.' });
                    //         this.goOpen()
                    //         // Do nothing!
                    //     })
                    // }
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
                    {
                        headerName: 'No.', 
                        field: 'rn', 
                        width: 80,
                        colSpan:(params) => {               
                            if(params.data.rn === '합계'){
                                return 7;
                            }else{
                                return 1;
                            }
                        },
                        cellStyle:(params) => {               
                            if(params.data.rn === '합계'){
                                return {textAlign: 'center'}
                            }else{
                                return {textAlign: 'left'}
                            }
                        },
                        valueFormatter: (params) => {               
                            if(params.data.rn === '합계'){
                                return '합계'
                            }else{
                                return params.node.rowIndex+1;
                            }
                        }
                        , editable: false             
                        
                    },
                    //DatePicker Component
                    {
                        headerName:'date'
                      , field:'apprDate'                      
                      , width: 120
                      , cellRenderer: 'datePicker'
                    },
                    //MaskEdit Component
                    {
                        headerName:'maskEdit(cardNo)'
                      , field:'cardNo'
                      , width : 200
                      , cellRenderer: 'maskEdit'
                      , cellRendererParams: {
                            mask: "XXXX-XXXX-XXXX-XXXX" 
                      }                       
                    },
                    {
                        headerName:'maskEdit(time)'
                      , field:'apprTime'
                      , width: 200
                      , cellRenderer: 'maskEdit'
                      , cellRendererParams: {
                            mask: "##:##:##" 
                      }                                                                    
                    },
                    //Button Component                                       
                    {
                        headerName:'button'
                      , field:'button'
                      , width: 200                      
                      , cellRenderer: 'button'
                      , cellRendererParams: {
                            name:  '선택',
                        }
                    },
                    //CellRender Component
                    {
                        headerName:'cellRender'
                      , field:'procCancel'
                      , width: 130
                      , cellStyle : {'justify-content': 'center','align-items': 'center', 'display': 'flex','height': '150%'}
                      , cellRenderer: function(params){                          
                            if(params.node.data.useDtlsStatCd == '20'){    
                                return "<button type='button' class='icon'><i class='fas fa-ban'></i></button>"
                            }
                        }
                    },
                    //select Component
                    {
                        headerName:'Select'
                      , field:'useDtlsStatCd'
                      , width:170
                      , cellRenderer: 'select'
                      , cellRendererParams: {
                            options : vm.options["ROLE_SELECT_CD"],
                            detailCd: 'key',  //서버로 부터 받은 key defalut-"detailCd"
                            detailNm: 'value' //서버로 부터 받은 value defalut-"detailNm"
                        }
                    },
                    //NumberInputBox Component
                    {
                        headerName:'Number Input'
                      , field:'purchTot'
                      , width: 180                      
                      , cellRenderer: 'numberInput'
                      , cellStyle:(params) => {               
                            if(params.data.rn === '합계'){
                                return {textAlign: 'right', color:'red'}
                            }else{
                                return {textAlign: 'right'}
                            }
                        }
                      , editable: (params) => {               
                            if(params.data.rn === '합계'){
                                return false
                            }else{
                                return true
                            }
                        }                                               
                    },
                    // {
                    //     headerName:'Number'
                    //   , field:'apprAmt'
                    //   , width:150
                    //   , type: 'numericColumn'
                    //   , valueFormatter: function(params) {               
                    //         return vm.getNumberFormat(params.value);//숫자 포맷<slip.js>
                    //     }
                    // },

                    //InputBox Component
                    {
                        headerName:'Input'
                      , field:'merchNm'
                      , width: 220                  
                      , cellRenderer: 'input'                      
                      , colSpan:(params) => {               
                            if(params.data.rn === '합계'){
                                return 2;
                            }else{
                                return 1;
                            }
                        }
                      , editable: (params) => {               
                            if(params.data.rn === '합계'){
                                return false
                            }else{
                                return true
                            }
                        }                         
                    },
                    //CheckBox Component
                    {
                      headerName:'CheckBox'
                    , field:'useYn'
                    , width: 120
                    , cellRenderer: 'checkBox'
                    , cellRendererParams: {
                          trueValue: true    //true값  지정 defalut-"Y" ex) "Y", true, "1"
                        , falseValue: false  //false값 지정 defalut-"N" ex) "N", false, "0"
                    }  
                    , cellStyle : {textAlign: 'center'}
                    }
                ];
            },                       
            goSearch() {
                
                //조회조건 필수 입력 체크 방법
                    //1.필수입력 컨트롤 라벨 css(control-label-req) 정의
                    //2.필수입력 컨트롤 라벨 id 정의
                    //3.필수입력 컨트롤 lbl 태그에 라벨 id 정의
                    //4.조회전 공통함수 호출                                
                if(!this.searchRequireCheck()) return;

                this.data =  [{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203SMTDESC      00007510007","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204804377","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"340303","crdPssrNm":"우종수","apprDate":"20201202","apprTime":"135045","merchNm":"서울연합의원","purchTot":62700,"apprAmt":62700,"vat":0,"tips":0,"mccCode":"4204","mccName":"기타외과","etc3":"1003","etcNm":"면세사업자","apprNo":"24802303","merchAddr":"충북 음성군 대소면 오태로 175,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000167","crdUseDeptNm":"소재개발팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3039045547","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KCPDESC      00041570001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204804377","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"340303","crdPssrNm":"우종수","apprDate":"20201202","apprTime":"181900","merchNm":"대신택배광혜원영업소","purchTot":3500,"apprAmt":3182,"vat":318,"tips":0,"mccCode":"3504","mccName":"화물운송/보관","useYn":true,"etcNm":"일반과세자","apprNo":"15126838","merchAddr":"충북 음성군 대소면 대금로 143,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000167","crdUseDeptNm":"소재개발팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3031077595","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203SMTDESC      00263870002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204804377","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"340303","crdPssrNm":"우종수","apprDate":"20201202","apprTime":"203110","merchNm":"뉴카스타운","purchTot":55000,"apprAmt":50000,"vat":5000,"tips":0,"mccCode":"2505","mccName":"패밀리레스토랑","useYn":true,"etcNm":"일반과세자","apprNo":"97374376","merchAddr":"충북 음성군 대소면 오태로99번길 28-4, 2층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000167","crdUseDeptNm":"소재개발팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"2671000688","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204SMTDESC      00007180005","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204804377","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"340303","crdPssrNm":"우종수","apprDate":"20201203","apprTime":"112328","merchNm":"서울연합의원","purchTot":58100,"apprAmt":58100,"vat":0,"tips":0,"mccCode":"4204","mccName":"기타외과","etc3":"1003","etcNm":"면세사업자","apprNo":"23131316","merchAddr":"충북 음성군 대소면 오태로 175,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000167","crdUseDeptNm":"소재개발팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3039045547","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203AWPDESC      00077770001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204804575","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"282055","crdPssrNm":"권형호","apprDate":"20201202","apprTime":"114338","merchNm":"상황버섯삼계탕","purchTot":9000,"apprAmt":8182,"vat":818,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"49850529","merchAddr":"경기 안성시 죽산면 미륵당길 28,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000146","crdUseDeptNm":"생산관리팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1352345694","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204SMTDESC      00125380001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204843706","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"322374","crdPssrNm":"임지연","apprDate":"20201203","apprTime":"192953","merchNm":"한양식당","purchTot":127000,"apprAmt":115455,"vat":11545,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"44490978","merchAddr":"충북 진천군 광혜원면 화랑5길 14,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000018","crdUseDeptNm":"연구소","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3013214524","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KICDESC      00097980090","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204973701","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300517","crdPssrNm":"정우석","apprDate":"20201203","apprTime":"125437","merchNm":"태아산업(주)","purchTot":6000,"apprAmt":5455,"vat":545,"tips":0,"mccCode":"8501","mccName":"고속도로휴게실","useYn":true,"etcNm":"일반과세자","apprNo":"25775870","merchAddr":"경기 여주시 가남읍 여주남로 722,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000016","crdUseDeptNm":"직속기구","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1268514730","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KICDESC      00097980091","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204973701","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300517","crdPssrNm":"정우석","apprDate":"20201203","apprTime":"125510","merchNm":"태아산업(주)","purchTot":7000,"apprAmt":6364,"vat":636,"tips":0,"mccCode":"8501","mccName":"고속도로휴게실","useYn":true,"etcNm":"일반과세자","apprNo":"17628806","merchAddr":"경기 여주시 가남읍 여주남로 722,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000016","crdUseDeptNm":"직속기구","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1268514730","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KOCDESC      00138280008","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204973701","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300517","crdPssrNm":"정우석","apprDate":"20201203","apprTime":"202746","merchNm":"(주)이춘복참치공덕점","purchTot":177000,"apprAmt":160909,"vat":16091,"tips":0,"mccCode":"2502","mccName":"일식","useYn":true,"etcNm":"일반과세자","apprNo":"27290641","merchAddr":"서울 마포구 마포대로 92, 효성해링턴스퀘어 B동 2층 1호 (도화동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000016","crdUseDeptNm":"직속기구","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"8128101658","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204SETAXI       00003760077","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070204973701","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300517","crdPssrNm":"정우석","apprDate":"20201203","apprTime":"211027","merchNm":"(주)티머니","purchTot":11700,"apprAmt":11700,"vat":0,"tips":0,"mccCode":"5205","mccName":"택시","useYn":true,"etcNm":"일반과세자","apprNo":"47145743","merchAddr":"서울 중구 남대문로5가 서울시티타워빌딩  10층  택시사업팀","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000016","crdUseDeptNm":"직속기구","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1048183559","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204SMTDESC      00449920003","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205042688","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"893503","crdPssrNm":"류민호","apprDate":"20201203","apprTime":"113949","merchNm":"충북대학교병원","purchTot":49600,"apprAmt":49600,"vat":0,"tips":0,"mccCode":"5405","mccName":"주차서비스","useYn":true,"etcNm":"일반과세자","apprNo":"14177324","merchAddr":"충북 청주시 서원구 1순환로 776, 1층 충북대학교병원 (개신동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"008","crdUseDutNm":"연구소장","crdUseDeptCd":"O000000018","crdUseDeptNm":"연구소","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3158203375","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KISDESC      00567650004","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205042688","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"893503","crdPssrNm":"류민호","apprDate":"20201203","apprTime":"131953","merchNm":"하루의식탁","purchTot":1100000,"apprAmt":1000000,"vat":100000,"tips":0,"mccCode":"2502","mccName":"일식","useYn":true,"etcNm":"일반과세자","apprNo":"86029961","merchAddr":"충북 진천군 덕산면 예지1길 13-4","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"008","crdUseDutNm":"연구소장","crdUseDeptCd":"O000000018","crdUseDeptNm":"연구소","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5395400267","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KISDESC      00641390001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205045640","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360385","crdPssrNm":"김진용","apprDate":"20201201","apprTime":"122322","merchNm":"커피온리 마포역점","purchTot":10800,"apprAmt":9818,"vat":982,"tips":0,"mccCode":"2405","mccName":"음료","useYn":true,"etcNm":"일반과세자","apprNo":"10914360","merchAddr":"서울 마포구 삼개로 5, 1층 일부 (도화동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000041","crdUseDeptNm":"재경팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"7916300396","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202GS25EDI      00037800028","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205072719","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360414","crdPssrNm":"이재황","apprDate":"20201201","apprTime":"145840","merchNm":"지에스25안산광양점","purchTot":8100,"apprAmt":7364,"vat":736,"tips":0,"mccCode":"8303","mccName":"편의점","useYn":true,"etcNm":"일반과세자","apprNo":"00914656","merchAddr":"경기 안산시 단원구 별망로 453, 122호 (목내동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5351300165","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202CNCDESC      00065970001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205072719","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360414","crdPssrNm":"이재황","apprDate":"20201201","apprTime":"182838","merchNm":"명태촌","purchTot":39000,"apprAmt":35455,"vat":3545,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"57196530","merchAddr":"인천 남동구 담방로48번길 7-2, (만수6동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1390825080","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202NICEDI       10022630092","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205072719","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360414","crdPssrNm":"이재황","apprDate":"20201201","apprTime":"212520","merchNm":"주식회사 카카오","purchTot":19000,"apprAmt":19000,"vat":0,"tips":0,"mccCode":"AAAA","mccName":"온라인거래_일반인증외","useYn":true,"etcNm":"일반과세자","apprNo":"16831007","merchAddr":"제주 제주시 첨단로 242, 1층 (영평동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1208147521","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203NICDESC      00465510001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205072719","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360414","crdPssrNm":"이재황","apprDate":"20201202","apprTime":"125830","merchNm":"김칠순생태동태탕","purchTot":17000,"apprAmt":15455,"vat":1545,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"02897329","merchAddr":"경기 화성시 동탄면 동탄기흥로247번길 10-7, 1층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"7042600397","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KSVDESC      00175740002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205072719","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360414","crdPssrNm":"이재황","apprDate":"20201203","apprTime":"115616","merchNm":"이월원조뼈해장국","purchTot":16000,"apprAmt":14545,"vat":1455,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"40880716","merchAddr":"충북 진천군 이월면 진광로 857-1,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"8772100012","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203NICCAP       00048910001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205082759","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360328","crdPssrNm":"김명진","apprDate":"20201202","apprTime":"123149","merchNm":"마라도일식","purchTot":80000,"apprAmt":72727,"vat":7273,"tips":0,"mccCode":"2402","mccName":"주점","useYn":true,"etcNm":"일반과세자","apprNo":"97192855","merchAddr":"충북 진천군 광혜원면 구암길 30, 1층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000047","crdUseDeptNm":"구매팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3010286114","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KRPDC4       00006240004","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205082759","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360328","crdPssrNm":"김명진","apprDate":"20201202","apprTime":"123355","merchNm":"생활커피 광혜원점","purchTot":16000,"apprAmt":14545,"vat":1455,"tips":0,"mccCode":"2405","mccName":"음료","useYn":true,"etcNm":"일반과세자","apprNo":"58062741","merchAddr":"충북 진천군 광혜원면 구암길 30,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000047","crdUseDeptNm":"구매팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"8023500335","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203SMTDESC      00126280001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205373299","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"913511","crdPssrNm":"이병래","apprDate":"20201202","apprTime":"195733","merchNm":"대박","purchTot":79000,"apprAmt":71818,"vat":7182,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"81160280","merchAddr":"충북 진천군 광혜원면 장기길 98, (덕원빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"021","crdUseDutNm":"공장","crdUseDeptCd":"O000000168","crdUseDeptNm":"연삭생산팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1251221175","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204AWPDESC      00139460006","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205386457","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"272013","crdPssrNm":"박정수","apprDate":"20201203","apprTime":"155832","merchNm":"우림개발(주)","purchTot":14800,"apprAmt":13455,"vat":1345,"tips":0,"mccCode":"8501","mccName":"고속도로휴게실","useYn":true,"etcNm":"일반과세자","apprNo":"80325807","merchAddr":"경기 용인시 처인구 주북로94번길 30-1, (고림동) ,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"020","crdUseDutNm":"직장","crdUseDeptCd":"101320","crdUseDeptNm":"품질팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1358126440","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KMPDESC      00097410002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205715853","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"213501","crdPssrNm":"신광섭","apprDate":"20201201","apprTime":"203341","merchNm":"국수진","purchTot":22500,"apprAmt":20455,"vat":2045,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"42476783","merchAddr":"충북 진천군 광혜원면 화랑5길 26,102호","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"012","crdUseDutNm":"공장장","crdUseDeptCd":"O000000165","crdUseDeptNm":"생산본부","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3013172532","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KSVDESC      00134990002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205715853","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"213501","crdPssrNm":"신광섭","apprDate":"20201203","apprTime":"213720","merchNm":"정원삼겹살직영점","purchTot":289000,"apprAmt":262727,"vat":26273,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"83445850","merchAddr":"충북 진천군 광혜원면 화랑5길 25,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"012","crdUseDutNm":"공장장","crdUseDeptCd":"O000000165","crdUseDeptNm":"생산본부","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3013180371","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KISDESC      00066070006","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205715853","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"213501","crdPssrNm":"신광섭","apprDate":"20201204","apprTime":"192231","merchNm":"씨제이프레시웨이(주)","purchTot":8000,"apprAmt":7273,"vat":727,"tips":0,"mccCode":"2508","mccName":"간이음식점","useYn":true,"etcNm":"일반과세자","apprNo":"49359560","merchAddr":"서울 강서구 등촌로 11,(화곡4동, CJ빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"012","crdUseDutNm":"공장장","crdUseDeptCd":"O000000165","crdUseDeptNm":"생산본부","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"6038111270","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KGIEDI       00014800082","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981828","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243686","crdPssrNm":"박성문","apprDate":"20201201","apprTime":"135133","merchNm":"환경보전협회","purchTot":90000,"apprAmt":90000,"vat":0,"tips":0,"mccCode":"AAAA","mccName":"회비","useYn":true,"etcNm":"일반과세자","apprNo":"94223608","merchAddr":"서울 동대문구 답십리1동 497-66","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1048204056","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KOCDESC      00075480003","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981828","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243686","crdPssrNm":"박성문","apprDate":"20201202","apprTime":"131906","merchNm":"육개장대표브랜드 이화수전통육개장","purchTot":9000,"apprAmt":8182,"vat":818,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"88143163","merchAddr":"대전 유성구 도안대로 585, (봉명동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"4685000104","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204NICDESC      00347460003","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981828","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243686","crdPssrNm":"박성문","apprDate":"20201203","apprTime":"132557","merchNm":"짬뽕의진수","purchTot":6500,"apprAmt":5909,"vat":591,"tips":0,"mccCode":"2503","mccName":"중식","useYn":true,"etcNm":"일반과세자","apprNo":"64596407","merchAddr":"대전 유성구 계룡로 92, 1층 112호 (봉명동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"6575400148","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KCPEDI       00008070085","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981828","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243686","crdPssrNm":"박성문","apprDate":"20201204","apprTime":"085915","merchNm":"네이버파이낸셜 주식회사","purchTot":458000,"apprAmt":458000,"vat":0,"tips":0,"mccCode":"8401","mccName":"PG_일반인증","useYn":true,"etcNm":"일반과세자","apprNo":"49492744","merchAddr":"경기 성남시 분당구 불정로 6, (정자동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5248601528","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205DACOMEDI     00000300018","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981828","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243686","crdPssrNm":"박성문","apprDate":"20201204","apprTime":"104850","merchNm":"한국소방안전원","purchTot":30000,"apprAmt":30000,"vat":0,"tips":0,"mccCode":"AAAA","mccName":"기술&#x2F;사무계학원","useYn":true,"etcNm":"일반과세자","apprNo":"37230009","merchAddr":null,"procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1078205239","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205SMTDESC      00007720005","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981828","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243686","crdPssrNm":"박성문","apprDate":"20201204","apprTime":"105705","merchNm":"서울연합의원","purchTot":1439250,"apprAmt":1439250,"vat":0,"tips":0,"mccCode":"4204","mccName":"기타외과","etc3":"1003","etcNm":"면세사업자","apprNo":"94268404","merchAddr":"충북 음성군 대소면 오태로 175,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3039045547","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202NICDESC      00236650001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981836","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"233512","crdPssrNm":"문환균","apprDate":"20201201","apprTime":"123711","merchNm":"바다양푼이동태탕광혜원점","purchTot":37000,"apprAmt":33636,"vat":3364,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"57267187","merchAddr":"충북 진천군 광혜원면 장기길 43,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000167","crdUseDeptNm":"소재개발팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3012746527","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203SMTDESC      00274700001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981893","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"310951","crdPssrNm":"김미선","apprDate":"20201202","apprTime":"123705","merchNm":"카페203로스터스(CAFE203ROASTER","purchTot":7500,"apprAmt":6818,"vat":682,"tips":0,"mccCode":"2508","mccName":"간이음식점","useYn":true,"etcNm":"일반과세자","apprNo":"56061043","merchAddr":"충북 음성군 대소면 대금로196번길 2, 1층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000172","crdUseDeptNm":"PDC팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"7984700441","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202NICCAP       00048820002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981919","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"311400","crdPssrNm":"성열주","apprDate":"20201201","apprTime":"214855","merchNm":"마라도일식","purchTot":100000,"apprAmt":90909,"vat":9091,"tips":0,"mccCode":"2402","mccName":"주점","useYn":true,"etcNm":"일반과세자","apprNo":"52628374","merchAddr":"충북 진천군 광혜원면 구암길 30, 1층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000170","crdUseDeptNm":"공정개선팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3010286114","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203SMTDESC      00520990001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981919","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"311400","crdPssrNm":"성열주","apprDate":"20201202","apprTime":"123428","merchNm":"무봉리순대국대소점","purchTot":35000,"apprAmt":31818,"vat":3182,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"09100613","merchAddr":"충북 음성군 대소면 대금로 539,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000170","crdUseDeptNm":"공정개선팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1270723501","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202NICCAP       00048820001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981935","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243505","crdPssrNm":"김춘수","apprDate":"20201201","apprTime":"214839","merchNm":"마라도일식","purchTot":194000,"apprAmt":176364,"vat":17636,"tips":0,"mccCode":"2402","mccName":"주점","useYn":true,"etcNm":"일반과세자","apprNo":"58469781","merchAddr":"충북 진천군 광혜원면 구암길 30, 1층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000166","crdUseDeptNm":"절삭생산팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3010286114","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KCPDESC      00092470001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981935","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243505","crdPssrNm":"김춘수","apprDate":"20201201","apprTime":"223745","merchNm":"바쿠스","purchTot":57000,"apprAmt":51818,"vat":5182,"tips":0,"mccCode":"2402","mccName":"주점","useYn":true,"etcNm":"일반과세자","apprNo":"56183008","merchAddr":"충북 진천군 광혜원면 화랑5길 26,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000166","crdUseDeptNm":"절삭생산팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5610700197","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202SMTEDI       00007570004","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205981935","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243505","crdPssrNm":"김춘수","apprDate":"20201201","apprTime":"230931","merchNm":"(주)스마트로","purchTot":51900,"apprAmt":51900,"vat":0,"tips":0,"mccCode":"8404","mccName":"온라인거래일반인증외","useYn":true,"etcNm":"일반과세자","apprNo":"74592893","merchAddr":"서울 양천구 목동서로 201, 케이티정보전산센터 24층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000166","crdUseDeptNm":"절삭생산팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"2178114493","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KCPCAP       00003660002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982016","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243728","crdPssrNm":"박기표","apprDate":"20201202","apprTime":"211346","merchNm":"환장회","purchTot":78000,"apprAmt":70909,"vat":7091,"tips":0,"mccCode":"2502","mccName":"일식","useYn":true,"etcNm":"일반과세자","apprNo":"93314913","merchAddr":"충북 진천군 광혜원면 화랑3길 19, .","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"101312","crdUseDeptNm":"마케팅팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5410701343","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205NICDESC      00247290001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982016","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243728","crdPssrNm":"박기표","apprDate":"20201204","apprTime":"124122","merchNm":"바다양푼이동태탕광혜원점","purchTot":42000,"apprAmt":38182,"vat":3818,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"77180740","merchAddr":"충북 진천군 광혜원면 장기길 43,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"101312","crdUseDeptNm":"마케팅팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3012746527","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205NICDESC      00533100003","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982016","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"243728","crdPssrNm":"박기표","apprDate":"20201204","apprTime":"143308","merchNm":"아베오커피","purchTot":12000,"apprAmt":10909,"vat":1091,"tips":0,"mccCode":"2405","mccName":"음료","useYn":true,"etcNm":"일반과세자","apprNo":"36074476","merchAddr":"경기 화성시 동탄기흥로 31, (방교동) 111호","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"101312","crdUseDeptNm":"마케팅팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"2032612070","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KOCDESC      00154360001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982040","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"320547","crdPssrNm":"곽지훈","apprDate":"20201202","apprTime":"203814","merchNm":"안성깍두기 광혜원점","purchTot":77000,"apprAmt":77000,"vat":0,"tips":0,"mccCode":"2501","mccName":"한식","useYn":false,"etcNm":"간이과세자","apprNo":"47314571","merchAddr":"충북 진천군 광혜원면 화랑5길 28, 1층 101호","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000082","crdUseDeptNm":"인사팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3021501141","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KOCDESC      00062580004","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982081","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300461","crdPssrNm":"김대근","apprDate":"20201201","apprTime":"145616","merchNm":"커피하우스","purchTot":6800,"apprAmt":6182,"vat":618,"tips":0,"mccCode":"2405","mccName":"음료","useYn":true,"etcNm":"일반과세자","apprNo":"07225064","merchAddr":"인천 남동구 남동대로 322,108, (논현동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1101221339","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KICDESC      00593610003","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982081","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300461","crdPssrNm":"김대근","apprDate":"20201203","apprTime":"141625","merchNm":"삼색두보리","purchTot":10000,"apprAmt":9091,"vat":909,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"37431103","merchAddr":"경기 평택시 이화로 39, 2동 (용이동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5294900174","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204NICDESC      00548510006","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982081","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300461","crdPssrNm":"김대근","apprDate":"20201203","apprTime":"162541","merchNm":"까로치아카페","purchTot":5000,"apprAmt":4545,"vat":455,"tips":0,"mccCode":"2504","mccName":"양식","useYn":true,"etcNm":"일반과세자","apprNo":"71795589","merchAddr":"충남 아산시 둔포면 중앙공원로17번길 3-17, 1층 1호","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"4940701334","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204CNCDESC      00092220002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982081","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300461","crdPssrNm":"김대근","apprDate":"20201203","apprTime":"184020","merchNm":"모텔(썸)","purchTot":40000,"apprAmt":36364,"vat":3636,"tips":0,"mccCode":"5103","mccName":"여관","useYn":true,"etcNm":"일반과세자","apprNo":"08088641","merchAddr":"충북 진천군 광혜원면 화랑4안길 21,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5640600414","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205NICDESC      00285400072","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982081","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300461","crdPssrNm":"김대근","apprDate":"20201204","apprTime":"170722","merchNm":"네이처브리지 (주)","purchTot":7400,"apprAmt":6727,"vat":673,"tips":0,"mccCode":"1500","mccName":"스포츠의류/용품","useYn":true,"etcNm":"일반과세자","apprNo":"08430416","merchAddr":"경기 이천시 마장면 덕이로154번길 287-76,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1268680071","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KMPDESC      00098510002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982099","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"282045","crdPssrNm":"이형윤","apprDate":"20201204","apprTime":"190820","merchNm":"국수진","purchTot":36000,"apprAmt":32727,"vat":3273,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"94552964","merchAddr":"충북 진천군 광혜원면 화랑5길 26,102호","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000173","crdUseDeptNm":"신규사업 TFT","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3013172532","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204SMTDESC      00324700002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982115","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"332031","crdPssrNm":"주용석","apprDate":"20201203","apprTime":"083715","merchNm":"만랩커피 마포점","purchTot":13600,"apprAmt":12364,"vat":1236,"tips":0,"mccCode":"2405","mccName":"음료","useYn":true,"etcNm":"일반과세자","apprNo":"49641316","merchAddr":"서울 마포구 마포대로 33, (도화동,마포한화오벨리스크)1층113-1호","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"6910101267","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KSVDESC      00100900001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982115","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"332031","crdPssrNm":"주용석","apprDate":"20201203","apprTime":"123741","merchNm":"원산대반점","purchTot":27000,"apprAmt":24545,"vat":2455,"tips":0,"mccCode":"2503","mccName":"중식","useYn":true,"etcNm":"일반과세자","apprNo":"44638691","merchAddr":"충북 진천군 광혜원면 화랑길 121,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3011298754","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KICDESC      00363080042","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982115","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"332031","crdPssrNm":"주용석","apprDate":"20201204","apprTime":"120702","merchNm":"태아산업 (주)","purchTot":9500,"apprAmt":8636,"vat":864,"tips":0,"mccCode":"8501","mccName":"고속도로휴게실","useYn":true,"etcNm":"일반과세자","apprNo":"06636042","merchAddr":"충북 음성군 삼성면 중부고속도로 51,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3038120748","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KSVDESC      00519640014","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070205982115","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"332031","crdPssrNm":"주용석","apprDate":"20201204","apprTime":"151909","merchNm":"이천휴게소","purchTot":6600,"apprAmt":6000,"vat":600,"tips":0,"mccCode":"8501","mccName":"고속도로휴게실","useYn":true,"etcNm":"일반과세자","apprNo":"90173118","merchAddr":"경기 이천시 신둔면 산83-1","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1268523530","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KICDESC      00893880001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206455897","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"261784","crdPssrNm":"김지형","apprDate":"20201204","apprTime":"000014","merchNm":"알마 호텔","purchTot":60000,"apprAmt":54545,"vat":5455,"tips":0,"mccCode":"5101","mccName":"호텔","useYn":true,"etcNm":"일반과세자","apprNo":"30476476","merchAddr":"충북 진천군 광혜원면 중리1길 23,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000041","crdUseDeptNm":"재경팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"8403200618","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KFTCDESC     00055400003","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206455897","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"261784","crdPssrNm":"김지형","apprDate":"20201204","apprTime":"181757","merchNm":"(주)금호에너지대소IC주유소","purchTot":40000,"apprAmt":40000,"vat":0,"tips":0,"mccCode":"5601","mccName":"주유소","useYn":true,"etcNm":"일반과세자","apprNo":"32060763","merchAddr":"충북 음성군 대소면 대금로 77-4,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000041","crdUseDeptNm":"재경팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3038170933","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203SMTDESC      00499040002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206503621","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"320160","crdPssrNm":"김도형","apprDate":"20201202","apprTime":"112848","merchNm":"하삼동커피(안산초지점)","purchTot":8800,"apprAmt":8000,"vat":800,"tips":0,"mccCode":"2405","mccName":"음료","useYn":true,"etcNm":"일반과세자","apprNo":"97241692","merchAddr":"경기 안산시 단원구 연수원로 4, B101호 (원시동 태인드림시티오피스텔)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"2262491604","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204GS25EDI      00098840005","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206503621","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"320160","crdPssrNm":"김도형","apprDate":"20201203","apprTime":"114434","merchNm":"지에스25(GS25)음성IC점","purchTot":9500,"apprAmt":8636,"vat":864,"tips":0,"mccCode":"8303","mccName":"편의점","useYn":true,"etcNm":"일반과세자","apprNo":"11727383","merchAddr":"충북 음성군 대소면 대금로 209,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"8811900982","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KICDESC      00793420006","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206503621","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"320160","crdPssrNm":"김도형","apprDate":"20201203","apprTime":"115023","merchNm":"현대오일뱅크(주)직영 대소산단주유소","purchTot":10000,"apprAmt":10000,"vat":0,"tips":0,"mccCode":"5601","mccName":"주유소","useYn":true,"etcNm":"일반과세자","apprNo":"02626312","merchAddr":"충북 음성군 대소면 대금로 199, .","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000090","crdUseDeptNm":"전략영업팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"7378501210","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KICDESC      00014750011","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206556967","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"320572","crdPssrNm":"송호철","apprDate":"20201201","apprTime":"123702","merchNm":"주식회사 프리츠","purchTot":52950,"apprAmt":48136,"vat":4814,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"91957665","merchAddr":"서울 마포구 새창로2길 17, (도화동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"101312","crdUseDeptNm":"마케팅팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1058801127","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203SMTDESC      00371600013","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206556967","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"320572","crdPssrNm":"송호철","apprDate":"20201202","apprTime":"220317","merchNm":"지에스칼텍스 (주)해동주유소","purchTot":39150,"apprAmt":39150,"vat":0,"tips":0,"mccCode":"5601","mccName":"주유소","useYn":true,"etcNm":"일반과세자","apprNo":"24292562","merchAddr":"경기 구리시 경춘로 69, (교문동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"101312","crdUseDeptNm":"마케팅팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"6548501050","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KSVDESC      00473080001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206556967","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"320572","crdPssrNm":"송호철","apprDate":"20201203","apprTime":"191214","merchNm":"2S모텔","purchTot":100000,"apprAmt":90909,"vat":9091,"tips":0,"mccCode":"5103","mccName":"여관","useYn":true,"etcNm":"일반과세자","apprNo":"57038567","merchAddr":"충북 진천군 광혜원면 화랑3길 3,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"101312","crdUseDeptNm":"마케팅팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5193000796","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205SMTDESC      00385000015","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206556967","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"320572","crdPssrNm":"송호철","apprDate":"20201204","apprTime":"203615","merchNm":"지에스칼텍스 (주)해동주유소","purchTot":54325,"apprAmt":54325,"vat":0,"tips":0,"mccCode":"5601","mccName":"주유소","useYn":true,"etcNm":"일반과세자","apprNo":"19415272","merchAddr":"경기 구리시 경춘로 69, (교문동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"101312","crdUseDeptNm":"마케팅팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"6548501050","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205NICEDI       20001410048","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206567097","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390115","crdPssrNm":"이동훈","apprDate":"20201204","apprTime":"002225","merchNm":"(주)티머니","purchTot":8600,"apprAmt":8600,"vat":0,"tips":0,"mccCode":"5205","mccName":"택시","useYn":true,"etcNm":"일반과세자","apprNo":"64591700","merchAddr":"서울 중구 후암로 110, (서울시티타워 10층)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"13010","crdUseDeptNm":"기획팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1048183559","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205NICEDI       20001410049","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206567097","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390115","crdPssrNm":"이동훈","apprDate":"20201204","apprTime":"002226","merchNm":"(주)티머니","purchTot":1000,"apprAmt":1000,"vat":0,"tips":0,"mccCode":"5205","mccName":"택시","useYn":true,"etcNm":"일반과세자","apprNo":"59324768","merchAddr":"서울 중구 후암로 110, (서울시티타워 10층)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"13010","crdUseDeptNm":"기획팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1048183559","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KISDESC      00143510003","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206567097","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390115","crdPssrNm":"이동훈","apprDate":"20201204","apprTime":"120909","merchNm":"까페데미타스","purchTot":15100,"apprAmt":13727,"vat":1373,"tips":0,"mccCode":"2405","mccName":"음료","useYn":true,"etcNm":"일반과세자","apprNo":"19631348","merchAddr":"서울 마포구 큰우물로 75,101호 (도화동, 성지빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"13010","crdUseDeptNm":"기획팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1051626970","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203NICDESC      00152170001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206636298","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390264","crdPssrNm":"임지은","apprDate":"20201202","apprTime":"121911","merchNm":"마포김치찌개","purchTot":28000,"apprAmt":25455,"vat":2545,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"88183594","merchAddr":"서울 마포구 큰우물로 75,지하층 14호 (도화동, 성지빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000041","crdUseDeptNm":"재경팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1051843143","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204CNCDESC      00024490002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206636298","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390264","crdPssrNm":"임지은","apprDate":"20201203","apprTime":"121655","merchNm":"동경","purchTot":37500,"apprAmt":34091,"vat":3409,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"27612969","merchAddr":"서울 마포구 마포대로 49,지하1층 3호 (도화동, 성우빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000041","crdUseDeptNm":"재경팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"2030278101","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KOCDESC      00067590010","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206636298","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390264","crdPssrNm":"임지은","apprDate":"20201204","apprTime":"120659","merchNm":"신의주찹쌀순대","purchTot":41000,"apprAmt":37273,"vat":3727,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"09512741","merchAddr":"서울 마포구 큰우물로 66, 1층 (용강동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000041","crdUseDeptNm":"재경팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"7575200027","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KISDESC      00183490001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206637361","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"282007","crdPssrNm":"이승제","apprDate":"20201203","apprTime":"193317","merchNm":"힘찬한우 정왕점","purchTot":200000,"apprAmt":181818,"vat":18182,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"85023105","merchAddr":"경기 시흥시 평안상가3길 26-1,1층 1호 (정왕1동, 영우빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000168","crdUseDeptNm":"연삭생산팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1401081334","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KSVDESC      00474830002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206676179","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"340080","crdPssrNm":"정하윤","apprDate":"20201202","apprTime":"200012","merchNm":"한옥카페봄이와","purchTot":18100,"apprAmt":16455,"vat":1645,"tips":0,"mccCode":"2405","mccName":"음료","useYn":true,"etcNm":"일반과세자","apprNo":"48747143","merchAddr":"충북 진천군 광혜원면 장기길 45,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"005","crdUseDutNm":"실장","crdUseDeptCd":"O000000169","crdUseDeptNm":"운영혁신실","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"4672400849","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KOCDESC      00152420002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206676179","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"340080","crdPssrNm":"정하윤","apprDate":"20201203","apprTime":"211943","merchNm":"안성깍두기 광혜원점","purchTot":70000,"apprAmt":70000,"vat":0,"tips":0,"mccCode":"2501","mccName":"한식","useYn":false,"etcNm":"간이과세자","apprNo":"90211246","merchAddr":"충북 진천군 광혜원면 화랑5길 28, 1층 101호","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"005","crdUseDutNm":"실장","crdUseDeptCd":"O000000169","crdUseDeptNm":"운영혁신실","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3021501141","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202NICCAP       00048820003","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206676229","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"340230","crdPssrNm":"권영경","apprDate":"20201201","apprTime":"214908","merchNm":"마라도일식","purchTot":100000,"apprAmt":90909,"vat":9091,"tips":0,"mccCode":"2402","mccName":"주점","useYn":true,"etcNm":"일반과세자","apprNo":"50979817","merchAddr":"충북 진천군 광혜원면 구암길 30, 1층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000168","crdUseDeptNm":"연삭생산팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3010286114","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KMPDESC      00034220001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206679637","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"350701","crdPssrNm":"박정호","apprDate":"20201202","apprTime":"105755","merchNm":"단골사","purchTot":11000,"apprAmt":10000,"vat":1000,"tips":0,"mccCode":"3404","mccName":"종합생활용품","useYn":true,"etcNm":"일반과세자","apprNo":"70012050","merchAddr":"서울 마포구 마포대로4길 26, 1층(도화동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000041","crdUseDeptNm":"재경팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1051449907","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KGIEDI       00000080018","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206744035","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"330187","crdPssrNm":"진혜린","apprDate":"20201202","apprTime":"093650","merchNm":"서울상공회의소","purchTot":55000,"apprAmt":55000,"vat":0,"tips":0,"mccCode":"AAAA","mccName":"기타업종","useYn":true,"etcNm":"일반과세자","apprNo":"38758152","merchAddr":"서울 중구 남대문로4가 45","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"101312","crdUseDeptNm":"마케팅팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1048203606","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KCPEDI       00000380026","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206801595","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300925","crdPssrNm":"양정호","apprDate":"20201201","apprTime":"080958","merchNm":"㈜알라딘커뮤니케이션","purchTot":57600,"apprAmt":57600,"vat":0,"tips":0,"mccCode":"AAAA","mccName":"서적","useYn":true,"etcNm":"일반과세자","apprNo":"69211296","merchAddr":"서울 중구 중림동 152-7","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000016","crdUseDeptNm":"직속기구","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"2018123094","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KISEDI       00006240012","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206801595","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"300925","crdPssrNm":"양정호","apprDate":"20201202","apprTime":"204755","merchNm":"플라자 디앤씨(주)","purchTot":20000,"apprAmt":18182,"vat":1818,"tips":0,"mccCode":"5101","mccName":"호텔","useYn":true,"etcNm":"일반과세자","apprNo":"88886808","merchAddr":"경기 수원시 영통구 광교호수공원로 320, (하동) .","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000016","crdUseDeptNm":"직속기구","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"8528701357","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KISDESC      00311210002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206833366","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390690","crdPssrNm":"박선영","apprDate":"20201201","apprTime":"075546","merchNm":"(주)신세계푸드 일진빌딩","purchTot":2000,"apprAmt":1818,"vat":182,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"42693821","merchAddr":"서울 마포구 마포대로 45, (도화동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000082","crdUseDeptNm":"인사팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"4828500325","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KCPEDI       00035220067","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206833366","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390690","crdPssrNm":"박선영","apprDate":"20201202","apprTime":"114229","merchNm":"네이버파이낸셜 주식회사","purchTot":17900,"apprAmt":17900,"vat":0,"tips":0,"mccCode":"8401","mccName":"PG_일반인증","useYn":true,"etcNm":"일반과세자","apprNo":"89478542","merchAddr":"경기 성남시 분당구 불정로 6, (정자동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000082","crdUseDeptNm":"인사팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5248601528","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203LTSUPEREDI   00002110005","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206833366","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390690","crdPssrNm":"박선영","apprDate":"20201202","apprTime":"131819","merchNm":"근신산업(주)","purchTot":13500,"apprAmt":12273,"vat":1227,"tips":0,"mccCode":"8102","mccName":"마트","useYn":true,"etcNm":"일반과세자","apprNo":"38766306","merchAddr":"서울 마포구 삼개로 16,(도화동, 근신빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000082","crdUseDeptNm":"인사팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1058102748","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205AWPDESC      00022620014","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070206833366","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"390690","crdPssrNm":"박선영","apprDate":"20201204","apprTime":"160557","merchNm":"파리바게트 마포역점","purchTot":13850,"apprAmt":12591,"vat":1259,"tips":0,"mccCode":"2404","mccName":"제과점,아이스크림점","useYn":true,"etcNm":"일반과세자","apprNo":"63028645","merchAddr":"서울 마포구 마포대로 33,114호 ,  한화오벨리스크제1층(도화동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000082","crdUseDeptNm":"인사팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1051534435","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202EMARTEDI     00004490069","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070207246303","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"331981","crdPssrNm":"정용기","apprDate":"20201201","apprTime":"161651","merchNm":"(주)이마트 트레이더스 안산","purchTot":10000,"apprAmt":9091,"vat":909,"tips":0,"mccCode":"8101","mccName":"대형마트/할인점","useYn":true,"etcNm":"일반과세자","apprNo":"40275288","merchAddr":"경기 안산시 단원구 신길동 택지개발지구 C-1호","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"2068650913","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201203KICDESC      00859340001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070207576741","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"963522","crdPssrNm":"김나영","apprDate":"20201202","apprTime":"182044","merchNm":"알마 호텔","purchTot":100000,"apprAmt":90909,"vat":9091,"tips":0,"mccCode":"5101","mccName":"호텔","useYn":true,"etcNm":"일반과세자","apprNo":"63227357","merchAddr":"충북 진천군 광혜원면 중리1길 23,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000082","crdUseDeptNm":"인사팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"8403200618","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205LTSUPEREDI   00002550006","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070207626769","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"400385","crdPssrNm":"김정재","apprDate":"20201204","apprTime":"131825","merchNm":"롯데쇼핑(주)롯데슈퍼광혜원점","purchTot":21000,"apprAmt":19091,"vat":1909,"tips":0,"mccCode":"8101","mccName":"대형마트/할인점","useYn":true,"etcNm":"일반과세자","apprNo":"34129477","merchAddr":"충북 진천군 광혜원면 광혜원산단1길 115-4,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3018541139","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KRPDC8       00011490001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070207626777","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"400466","crdPssrNm":"양정화","apprDate":"20201201","apprTime":"121954","merchNm":"도원징기스칸","purchTot":94000,"apprAmt":85455,"vat":8545,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"99710472","merchAddr":"서울 마포구 삼개로 16,(도화동, 근신빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"13010","crdUseDeptNm":"기획팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1051378972","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204NICEDI       20026930005","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070207626777","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"400466","crdPssrNm":"양정화","apprDate":"20201203","apprTime":"062753","merchNm":"(주)에이텍티앤","purchTot":6600,"apprAmt":6600,"vat":0,"tips":0,"mccCode":"5206","mccName":"버스/지하철","useYn":true,"etcNm":"일반과세자","apprNo":"46760126","merchAddr":"경기 성남시 분당구 판교로 289, (삼평동, 에이텍빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"13010","crdUseDeptNm":"기획팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"6988800163","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KISEDI       00000310100","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070207626777","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"400466","crdPssrNm":"양정화","apprDate":"20201203","apprTime":"073720","merchNm":"(주)이비카드","purchTot":5220,"apprAmt":5220,"vat":0,"tips":0,"mccCode":"5205","mccName":"택시","useYn":true,"etcNm":"일반과세자","apprNo":"73849793","merchAddr":"서울 동작구 보라매로5길 51, 8층 (신대방동, 롯데타워)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"13010","crdUseDeptNm":"기획팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1298638970","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205NICEDI       30000670098","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070207626777","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"400466","crdPssrNm":"양정화","apprDate":"20201204","apprTime":"180804","merchNm":"(주)에이텍티앤","purchTot":6600,"apprAmt":6600,"vat":0,"tips":0,"mccCode":"5206","mccName":"버스/지하철","useYn":true,"etcNm":"일반과세자","apprNo":"25707901","merchAddr":"경기 성남시 분당구 판교로 289, (삼평동, 에이텍빌딩)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"13010","crdUseDeptNm":"기획팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"6988800163","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202KRPDC4       00022190001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070207876570","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"290234","crdPssrNm":"이형로","apprDate":"20201201","apprTime":"183517","merchNm":"이화수 충북대소점","purchTot":23000,"apprAmt":20909,"vat":2091,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"24783601","merchAddr":"충북 음성군 대소면 대금로 74, 1층","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"009","crdUseDutNm":"팀장","crdUseDeptCd":"O000000118","crdUseDeptNm":"생산지원팀","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5485100241","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201202NICDESC      00522710025","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"4009070208304366","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"332033","crdPssrNm":"최문혁","apprDate":"20201201","apprTime":"195642","merchNm":"SK에너지(주) 양지","purchTot":68000,"apprAmt":68000,"vat":0,"tips":0,"mccCode":"5601","mccName":"주유소","useYn":true,"etcNm":"일반과세자","apprNo":"56692704","merchAddr":"서울 마포구 마포대로 69, (도화동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"010","crdUseDutNm":"팀원","crdUseDeptCd":"O000000137","crdUseDeptNm":"중국법인","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"1018660120","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205KISDESC      00820930002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"5587490102420307","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"881509","crdPssrNm":"김희수","apprDate":"20201204","apprTime":"181219","merchNm":"동신면가","purchTot":137000,"apprAmt":124545,"vat":12455,"tips":0,"mccCode":"2501","mccName":"한식","useYn":true,"etcNm":"일반과세자","apprNo":"91848486","merchAddr":"서울 강동구 올림픽로 803, (암사2동)","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"023","crdUseDutNm":"고문","crdUseDeptCd":"O000000178","crdUseDeptNm":"고문","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"2123545882","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KCPDESC      00093480001","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"20","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"5589789900000999","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360369","crdPssrNm":"변정출","apprDate":"20201203","apprTime":"224947","merchNm":"바쿠스","purchTot":46500,"apprAmt":42273,"vat":4227,"tips":0,"mccCode":"2402","mccName":"주점","useYn":true,"etcNm":"일반과세자","apprNo":"23121496","merchAddr":"충북 진천군 광혜원면 화랑5길 26,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"002","crdUseDutNm":"대표이사","crdUseDeptCd":"101304","crdUseDeptNm":"대표이사","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5610700197","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201205DAIEDI       00011420007","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"5589789900000999","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360369","crdPssrNm":"변정출","apprDate":"20201203","apprTime":"230150","merchNm":"코리아세븐진천광혜원원룸점","purchTot":15150,"apprAmt":13773,"vat":1377,"tips":0,"mccCode":"8303","mccName":"편의점","useYn":true,"etcNm":"일반과세자","apprNo":"97189020","merchAddr":"충북 진천군 광혜원면 화랑3길 11,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"002","crdUseDutNm":"대표이사","crdUseDeptCd":"101304","crdUseDeptNm":"대표이사","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"3013048826","merchTel":null}
                                    ,{"postDt":null,"baseDeptCd":null,"baseDeptNm":null,"stnAcctCd":null,"stnAcctNm":null,"hdSgtxt":null,"useDtlsNo":"20201204KSVDESC      00473080002","compCd":"102","compNm":"일진다이아몬드(주)","useDtlsStatCd":"10","useDtlsStatNm":"미처리","slipStatCd":null,"slipStatNm":null,"cardNo":"5589789900000999","crdCompCd":"SS","crdCompNm":"삼성","crdPssrId":"360369","crdPssrNm":"변정출","apprDate":"20201203","apprTime":"230431","merchNm":"2S모텔","purchTot":50000,"apprAmt":45455,"vat":4545,"tips":0,"mccCode":"5103","mccName":"여관","useYn":true,"etcNm":"일반과세자","apprNo":"53455939","merchAddr":"충북 진천군 광혜원면 화랑3길 3,","procPeriod":null,"eaSlipNo":null,"crdPssrDeptCd":null,"crdPssrDeptNm":null,"slipProcId":null,"slipProcNm":null,"crdUseDutCd":"002","crdUseDutNm":"대표이사","crdUseDeptCd":"101304","crdUseDeptNm":"대표이사","crdFgCd":"P","crdFgNm":"개인","acqClass":"A","acqClassNm":"매입","erpSlipDtm":null,"apprDateFrom":null,"apprDateTo":null,"searchDtmFr":null,"searchDtmTo":null,"cardList":[],"unCardCnt":null,"apprDateTime":null,"apprMethod":null,"merchNo":null,"merchBizNo":"5193000796","merchTel":null}]
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
            }              
        },
        mounted() {
            this.createColumnDefs();//그리드 컬럼 정의 함수 호출            
        },
        watch: {
            'data' : {
                deep: true,
                handler(value){
                    this.makeHeaderTotal()
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

<style lang="scss" scoped>
    .slip-grid {
        :global(.xhdr table tbody tr:last-child) {
            background-color: #f9f9f3;
        }

        :global(.xhdr table tbody tr:last-child td) {
            background-color: transparent !important;
        }

        :global(.xhdr table tbody tr:last-child td div) {
            background-color: transparent !important;
            color: inherit !important;
            text-align: inherit !important;
        }
    }
</style>
