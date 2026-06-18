<template>
    <layout>
        <span slot="header">
            {{title}}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button>
        </span>
        <div slot="content">
            <div class="table-area">
                <div class="table-b">
                    <div class="table-header">
                        <div class="table-name" style="width:50%;">
                            <h3 class="ico_table_name">결재자 지정</h3>
                        </div>
                        <div class="btn-wrap btn-type1 clearfix" style="width:33%; float:left; padding-bottom:5px; text-align:center;">
                            <button type="button" class="btn-size btn-blue fl_right" @click="popEmp">임직원 검색</button>
                        </div>
                        <div class="btn-wrap btn-type1 clearfix fl_right" style="padding-right: 15px">
                            <div class="dp-ib fl_left">
                                <input type="radio" id="appr_radio01" v-model="apprTypeCd" value="20"/>
                                <label for="appr_radio01" class="NotoM">결재</label>
                            </div>
                            <div class="dp-ib fl_left">
                                <input type="radio" id="appr_radio02" v-model="apprTypeCd" value="30"/>
                                <label for="appr_radio02" class="NotoM">합의</label>
                            </div>
                        </div>
                    </div>
                    <div id="treeBox" style="width:350px;height:330px;float:left; border-top:1px solid #c7c7c7; border-bottom:1px solid #c7c7c7; border-left:1px solid #c7c7c7; border-right:1px solid #c7c7c7;"></div>

                    <div class="grid-wrap" style="float:right;width: 60%;">
                        <ag-grid-vue ref="gridEmp"
                                     style="width: 100%; height: 330px;"
                                     class="ag-theme-alpine"
                                     rowSelection="single"
                                     :columnDefs="empColumnDefs"
                                     :rowData="empList"
                                     :gridOptions="empGridOptions"
                                     :defaultColDef="defaultColDef"
                                     @grid-ready="onEmpReady"
                                     @rowDoubleClicked="onEmpDblClick"/>
                    </div>
                </div>
            </div>

            <el-radio-group v-model="changeLineFlag" size="large" style="float:left; margin-bottom: 20px;">
                <el-radio-button :label="true">결재</el-radio-button>
                <el-radio-button :label="false" v-if="this.$route.name !== 'apprLineMng'">참조</el-radio-button>
            </el-radio-group>

            <div class="table-name" style="float:left; margin-left: 150px;" v-if="this.$route.name !== 'apprLineMng'">
              <h5 style="color:red;">❊ 결재선, 참조선은 저장되지 않습니다.</h5>
            </div>

            <div class="table-area">
                <div class="table-b" v-if="changeLineFlag">
                    <div class="table-header">
<!--                        <div class="table-name">
                            <h3 class="ico_table_name">결재선</h3>
                        </div>-->

                        <div class="btn-wrap btn-type2">
                            <button class="btn-size btn-w-gray" @click="moveDown"><i class="fas fa-angle-down"></i></button>
                            <button class="btn-size btn-w-gray" @click="moveUp"><i class="fas fa-angle-up"></i></button>
                            <button class="btn-size btn-w-gray" @click="save"><i class="fas fa-pen-alt"></i> 적용</button>
                            <button class="btn-size btn-w-gray" @click="addEmp"><i class="fas fa-check"></i> 추가</button>
                            <button class="btn-size btn-w-gray" @click="cancel"><i class="far fa-trash-alt"></i> 삭제</button>
                        </div>
                    </div>

                    <div class="desc-content search-border">
                      <div class="item desc-left">
                        <div class="desc-item">
                          <div class="tit">
                            <span class="label_tit">개인결재선 선택</span>
                          </div>
                        </div>
                      </div>
                      <div class="item desc-right">
                        <div class="desc-item">
                          <div class="desc">
                            <select class="select" id="apprGroupSelect" v-model="groupName" @change="selectChange()">
                              <option value="" selected="selected">== 개인결재선 선택 ==</option>
                              <option
                                v-for="item in groupList"
                                :key="item.apprLineTitle"
                                :value="item.apprSeq"
                                v-text="item.apprSeq + '. ' + item.apprLineTitle"/>
                            </select>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="grid-wrap">
                        <ag-grid-vue ref="gridLine"
                                     style="width: 100%; height: 250px;"
                                     class="ag-theme-alpine"
                                     rowSelection="single"
                                     :columnDefs="lineColumnDefs"
                                     :rowData="form.lineList"
                                     :gridOptions="lineGridOptions"
                                     :defaultColDef="defaultColDef"
                                     @grid-ready="onLineReady"
                                     @rowDoubleClicked="onLineDblClick"/>
                    </div>
                </div>

                <div class="table-b" v-else>
                  <div class="table-header">
<!--                    <div class="table-name">
                      <h3 class="ico_table_name">참조</h3>
                    </div>-->
                    <div class="btn-wrap btn-type2">
                      <button class="btn-size btn-w-gray" @click="moveDown"><i class="fas fa-angle-down"></i></button>
                      <button class="btn-size btn-w-gray" @click="moveUp"><i class="fas fa-angle-up"></i></button>
                      <button class="btn-size btn-w-gray" @click="save"><i class="fas fa-pen-alt"></i> 적용</button>
                      <button class="btn-size btn-w-gray" @click="addEmp"><i class="fas fa-check"></i> 추가</button>
                      <button class="btn-size btn-w-gray" @click="cancel"><i class="far fa-trash-alt"></i> 삭제</button>
                    </div>
                  </div>

                  <div class="grid-wrap">
                    <ag-grid-vue ref="gridRef"
                                 style="width: 100%; height: 319px;"
                                 class="ag-theme-alpine"
                                 rowSelection="single"
                                 :columnDefs="refColumnDefs"
                                 :rowData="form.refList"
                                 :gridOptions="refGridOptions"
                                 :defaultColDef="defaultColDef"
                                 @grid-ready="onRefReady"
                                 @rowDoubleClicked="onRefDblClick"/>
                  </div>
                </div>
            </div>
        </div>
    </layout>
</template>

<script>
    import Layout from '@/components/ModalSlot.vue';
    import mixin from '@/mixin';
    import mixinSlip from '@/mixin/slip';

    import { AgGridVue } from 'ag-grid-vue'
    import Emp from '@/components/Emp_Ag.vue';

    export default {
  compatConfig: { MODE: 2 },
        name: 'ApprLineSet',
        mixins: [mixin, mixinSlip],
        components: {Layout, Emp, AgGridVue},
        props: {
          ['lineList']: {
            required:false
          },
          ['refList']: {
            required:false
          },
          setUserId: {
            type:String,
            required:false
          },
          selectSeq: {
            type:Number,
            required:false
          },
          slipNo: {
            type:String,
            required:false
          },
        },
        data() {
            return {
                empColumnDefs: [
                    {headerName: 'No.', width: 50, cellStyle: {textAlign: 'center'}, valueGetter: p => p.node.rowIndex + 1},
                    {headerName: '이름', field: 'empNm', flex: 1, cellStyle: {textAlign: 'center'}},
                    {headerName: '직급', field: 'jobNm', width: 100, cellStyle: {textAlign: 'center'}},
                    {headerName: '사번', field: 'empNo', hide: true},
                    {headerName: '부서명', field: 'deptNm', hide: true},
                    {headerName: '직책명', field: 'jobDutNm', hide: true},
                ],
                lineColumnDefs: [
                    {headerName: '순서', width: 55, cellStyle: {textAlign: 'center'}, valueGetter: p => p.node.rowIndex + 1},
                    {headerName: '이름', field: 'aprverUser', flex: 1, cellStyle: {textAlign: 'center'}},
                    {headerName: '직급', field: 'jobNm', width: 80, cellStyle: {textAlign: 'center'}},
                    {headerName: '부서', field: 'deptNm', flex: 1, cellStyle: {textAlign: 'center'}},
                    {headerName: '결재형식', field: 'apprType', width: 90, cellStyle: {textAlign: 'center'}},
                    {headerName: '결재형식Cd', field: 'apprTypeCd', hide: true},
                    {headerName: '사번', field: 'aprverId', hide: true},
                    {headerName: '재직여부', field: 'serveCd', hide: true},
                ],
                refColumnDefs: [
                    {headerName: '순서', width: 55, cellStyle: {textAlign: 'center'}, valueGetter: p => p.node.rowIndex + 1},
                    {headerName: '이름', field: 'aprverUser', flex: 1, cellStyle: {textAlign: 'center'}},
                    {headerName: '직급', field: 'jobNm', width: 80, cellStyle: {textAlign: 'center'}},
                    {headerName: '부서', field: 'deptNm', flex: 1, cellStyle: {textAlign: 'center'}},
                    {headerName: '결재형식', field: 'apprType', width: 90, cellStyle: {textAlign: 'center'}},
                    {headerName: '결재형식Cd', field: 'apprTypeCd', hide: true},
                    {headerName: '사번', field: 'aprverId', hide: true},
                    {headerName: '재직여부', field: 'serveCd', hide: true},
                ],
                empGridOptions: {},
                lineGridOptions: {},
                refGridOptions: {},
                defaultColDef: {resizable: true, sortable: false, filter: false},
                empApi: null,
                lineApi: null,
                refApi: null,
                title: '결재선 지정',
                form: {
                    lineList: [],
                    refList: [],
                },
                apprTypeCd: '20',
                returnObject: {},
                empList: [],
                items: [],
                groupList: [],
                groupName: '',
                detailList: [],
                setDept: '',
                changeLineFlag: true,
                refUserId: '',
            }
        },
        methods:
            {
                onEmpReady(params) {
                    this.empApi = params.api;
                },
                onLineReady(params) {
                    this.lineApi = params.api;
                },
                onRefReady(params) {
                    this.refApi = params.api;
                },
                onEmpDblClick(params) {
                    const d = params.data;
                    let apprTypeNm;
                    if (this.apprTypeCd === '20') apprTypeNm = '결재';
                    else if (this.apprTypeCd === '30') apprTypeNm = '합의';

                    if (!this.checkDup(d.empNo)) return;

                    const item = {
                        aprverUser: d.empNm,
                        jobNm: d.jobNm,
                        aprverId: d.empNo,
                        deptNm: d.deptNm,
                        apprTypeCd: this.apprTypeCd,
                        apprType: this.changeLineFlag ? apprTypeNm : '참조',
                        serveCd: '10'
                    };
                    if (this.changeLineFlag) this.form.lineList.push(item);
                    else this.form.refList.push(item);
                },
                onLineDblClick(params) {
                    const index = params.node.rowIndex;
                    const row = this.form.lineList[index];
                    if (row && row.apprTypeCd === '10') {
                        this.$swal({type: 'warning', text: '선택하신 행은 삭제할 수 없습니다.'});
                    } else {
                        this.form.lineList.splice(index, 1);
                    }
                },
                onRefDblClick(params) {
                    const index = params.node.rowIndex;
                    const row = this.form.refList[index];
                    if (row && row.apprTypeCd === '10') {
                        this.$swal({type: 'warning', text: '선택하신 행은 삭제할 수 없습니다.'});
                    } else {
                        this.form.refList.splice(index, 1);
                    }
                },
                getGroupData(){
                  this.$http
                    .post("/api/apprLine/group", {
                      compCd: this.$store.state.loginInfo.compCd,
                      userId: this.setUserId
                    })
                    .then(response => {
                      this.groupList = response.data;

                      //for(let i=0; i<this.groupList.length; i++){
                        //if(this.groupList[i].mainApprYn == 'Y' && this.form.lineList.length == 0){
                          //this.groupName = this.groupList[i].apprSeq
                          //break;
                        //
                      //}
                    });
                },
                addEmp() {
                    try {
                        const nodes = this.empApi ? this.empApi.getSelectedNodes() : []
                        if (!nodes.length)
                            throw '선택된 행이 없습니다.'
                        let row = nodes[0].data

                        let apprTypeNm;

                        if(this.apprTypeCd==='20') apprTypeNm = '결재'
                        else if(this.apprTypeCd==='30') apprTypeNm = '합의'

                        if(!this.checkDup(row.empNo)) return

                        const item = {
                            aprverUser : row.empNm,
                            jobNm : row.jobNm,
                            aprverId : row.empNo,
                            deptNm : row.deptNm,
                            apprTypeCd : this.apprTypeCd,
                            apprType : this.changeLineFlag ? apprTypeNm : '참조',
                            serveCd : '10'
                        }
                        if(this.changeLineFlag) this.form.lineList.push(item)
                        else this.form.refList.push(item)

                    } catch (e) {
                        this.$swal({
                            type: 'warning',
                            text: e
                        })
                    }
                },
                cancel() {
                    try {
                        if(this.changeLineFlag){
                          //결재
                          const nodes = this.lineApi ? this.lineApi.getSelectedNodes() : []
                          if (!nodes.length)
                            throw '선택된 행이 없습니다.'
                          let index = nodes[0].rowIndex
                          let row = this.form.lineList[index]

                          if (row.apprTypeCd === '10') {
                            this.$swal({
                              type: 'warning',
                              text: '선택하신 행은 삭제할 수 없습니다.'
                            })
                          } else {
                            this.form.lineList.splice(index, 1)
                          }
                        }else{
                          //참조
                          const nodes = this.refApi ? this.refApi.getSelectedNodes() : []
                          if (!nodes.length)
                            throw '선택된 행이 없습니다.'
                          let index = nodes[0].rowIndex

                          this.form.refList.splice(index, 1)
                        }

                    } catch (e) {
                        this.$swal({
                            type: 'warning',
                            text: e
                        })
                    }
                },
                moveDown() {
                    const api = this.changeLineFlag ? this.lineApi : this.refApi
                    const list = this.changeLineFlag ? this.form.lineList : this.form.refList
                    const nodes = api ? api.getSelectedNodes() : []
                    if (!nodes.length) {
                        this.$swal({type: 'warning', text: '선택된 행이 없습니다.'})
                        return
                    }
                    const rowIndex = nodes[0].rowIndex
                    const alterIndex = rowIndex + 1 >= list.length ? list.length - 1 : rowIndex + 1
                    const data = list[rowIndex]
                    list.splice(rowIndex, 1)
                    list.splice(alterIndex, 0, data)

                    this.$nextTick(() => {
                        const node = api.getDisplayedRowAtIndex(alterIndex)
                        if (node) node.setSelected(true)
                    })
                },
                moveUp() {
                    const api = this.changeLineFlag ? this.lineApi : this.refApi
                    const list = this.changeLineFlag ? this.form.lineList : this.form.refList
                    const nodes = api ? api.getSelectedNodes() : []
                    if (!nodes.length) {
                        this.$swal({type: 'warning', text: '선택된 행이 없습니다.'})
                        return
                    }
                    const rowIndex = nodes[0].rowIndex
                    const alterIndex = rowIndex - 1 < 0 ? 0 : rowIndex - 1
                    const data = list[rowIndex]
                    list.splice(rowIndex, 1)
                    list.splice(alterIndex, 0, data)

                    this.$nextTick(() => {
                        const node = api.getDisplayedRowAtIndex(alterIndex)
                        if (node) node.setSelected(true)
                    })
                },
                selectChange(){
                  this.form.lineList.splice(0, this.form.lineList.length)

                  if(document.getElementById('apprGroupSelect').value){
                    this.$store.commit('loading')
                    this.$http
                      .post("/api/apprLine/detail", {
                        compCd: this.$store.state.loginInfo.compCd,
                        userId: this.setUserId,
                        apprSeq: document.getElementById('apprGroupSelect').value
                      })
                      .then(response => {
                        this.detailList = response.data

                        this.detailList.forEach(dt => {
                          this.form.lineList.push(
                            {
                              aprverUser : dt.apprUserNm,
                              jobNm : dt.jobNm,
                              aprverId : dt.apprUserId,
                              deptNm : dt.deptNm,
                              apprTypeCd : dt.apprTypeCd,
                              apprType : dt.apprTypeCd == 10 ? '기안' : (dt.apprTypeCd == 20 ? '결재' : '합의'),
                              serveCd : dt.serveCd
                            }
                          )
                        })

                      }).finally(() => {
                      this.$store.commit('finish')
                    });
                  }

                },
                save() {
/*                    if(this.form.lineList.length < 1){
                      this.$swal({
                        type: 'warning',
                        text: '결재선이 없습니다. 계속 하시겠습니까?',
                        showCancelButton: true,
                        confirmButtonText: '예',
                        cancelButtonText: '아니요'
                      }).then(response => {
                        if(response.value){
                        }else{
                        }
                      })
                    }*/

                    if(this.form.lineList.length > 0 && this.form.lineList[this.form.lineList.length-1].apprTypeCd === '30') {
                        this.$swal({
                            type: 'warning',
                            text: '마지막 결재형식은 합의가 될 수 없습니다.'
                        })
                        return
                    }

                    if(this.$route.name === 'apprLineMng' && document.getElementById('apprGroupSelect').value){
                      //개인결재선 관리
                      let lineTest = []

                      for(let i=0; i<this.form.lineList.length; i++){
                        if(this.form.lineList[i].serveCd != '10'){
                          this.$swal({
                            type: 'warning',
                            text: (i+1) + '행의 사원정보가 없습니다.'
                          })

                          return
                        }
                        lineTest.push({
                          compCd: this.$store.state.loginInfo.compCd,
                          userId: this.setUserId,
                          apprSeq: _.toNumber(document.getElementById('apprGroupSelect').value),
                          subApprSeq: (i+1),
                          apprUserId: this.form.lineList[i].aprverId,
                          apprTypeCd: this.form.lineList[i].apprTypeCd
                        })
                      }

                      if(lineTest.length > 0){
                        this.$http.post('/api/apprLine/saveLine', lineTest)
                          .then(
                            this.$swal({ type: 'success', text: '저장되었습니다.' })
                          )
                      }else{
                        //라인 없이 저장시 예외 처리
                        this.$http.post('/api/apprLine/delAllLine', {
                          compCd: this.$store.state.loginInfo.compCd,
                          userId: this.setUserId,
                          apprSeq: _.toNumber(document.getElementById('apprGroupSelect').value),
                          subApprSeq: '',
                          apprUserId: '',
                          apprTypeCd: ''
                        }).then(
                          this.$swal({ type: 'success', text: '저장되었습니다.' })
                        )
                      }

                    }else{
                      //기존 결재선 설정
                      this.returnObject.name = 'apprLine'
                      this.returnObject.lineList = this.form.lineList
                      this.returnObject.refList = this.form.refList

                      this.refUserId = '';

                      this.refUserId = this.form.refList.map(x => x.aprverId).join(' , ');

                      this.returnObject.refUserId = this.refUserId
                      //this.$parent.$emit('receive', this.returnObject);
                      //this.$emit('close', this.returnObject)
                      this.closeModal();
                    }
                },
                closeModal() {
                    this.$parent.$emit('receive', this.returnObject);
                    this.$emit('close', this.returnObject)
                    this.$parent.close();
                },
                checkDup(emp){
                    /*if(emp == this.$store.state.loginInfo.loginId){
                      this.$swal({ type: 'info', text: '본인은 추가할 수 없습니다.' });
                      return false
                    }*/

                    let outYn = 'N'
                    let grid = null

                    if(this.changeLineFlag){
                        grid = this.form.lineList
                    }else{
                        grid = this.form.refList
                    }
                    _.forEach(grid, v => {
                        if (v.aprverId === emp) {
                            outYn = 'Y'
                            return
                        }
                    })
                    if(outYn==='Y'){
                        if(this.changeLineFlag){
                            this.$swal({ type: 'info', text: '이미 결재선에 존재합니다.' });
                        }else{
                            this.$swal({ type: 'info', text: '이미 참조에 존재합니다.' });
                        }
                        return false
                    }
                    return true
                },
                goEmpList(deptCd){
                    this.$store.commit('loading')
                    this.$http.get(`/api/emp/hitDept/${deptCd}`)
                        .then(response => {
                            if (response.data) {
                                this.empList = response.data
                            }
                        }).finally(() => {
                            this.$store.commit('finish')
                        })
                },
                drawTree() {
                    let rootCd = ''

                    this.$http.get(`/api/appr/detail/approvalLine`)
                        .then((response) => {
                            this.items = response.data;

                            let arr = []
                            _.forEach(response.data, v => {
                                v.text = v.cctrNm
                                v.id = v.cctrCd

                                // switch (this.$store.state.loginInfo.compCd){
                                //   case '101' :
                                //     rootCd = 'RT00000001'
                                //     break;
                                //   default :
                                //     rootCd = '0000000000'
                                //     break;
                                //
                                // }

                                rootCd = '100045'

                                if (v.upperCd === rootCd) {
                                    arr.push(v)
                                } else {
                                    makeChildren(arr, v)
                                }
                            })

                            function makeChildren(arr, value) {
                                let result = undefined
                                if (Array.isArray(arr) && arr.length > 0) {
                                    _.forEach(arr, v => {
                                        if (v.cctrCd === value.upperCd) {
                                            v.items = v.items || []
                                            v.items.push(value)
                                            result = true
                                            return false
                                        } else if (Array.isArray(v.items) && v.items.length > 0) {
                                            let r = makeChildren(v.items, value)
                                            if (r !== undefined) {
                                                result = r
                                                return false
                                            }
                                        }
                                    })
                                }
                                return result
                            }

                            this.items = arr
                            // eslint-disable-next-line
                            const tree = new dhtmlXTreeView({
                                parent: "treeBox",
                                items: this.items,
                                iconset: "font_awesome",
                            });

                            _.forEach(response.data, v=> {
                                if(v.id) {
                                    tree.setItemIcons(v.id, {
                                        file: "fas fa-user",
                                        folder_opened: "fas fa-users",
                                        folder_closed: "fas fa-user"
                                    });
                                }
                            })

                            tree.attachEvent('onClick', (id) => {
                                this.goEmpList(tree.items[id].id)
                            });

                            let openList = []
                            openParent(response.data, this.setDept)

                            function openParent(treeItem, value) {
                                _.forEach(treeItem, v => {
                                    if (v.upperCd) {
                                        if (v.id == value) {
                                            openList.unshift({cd: v.upperCd})
                                            openParent(treeItem, v.upperCd)
                                        }
                                    }
                                })
                            }

                            _.forEach(openList, v => {
                              if(v.cd !== rootCd)
                                tree.openItem(v.cd);
                            })


                            this.goEmpList(this.setDept)
                            if(this.form.lineList.length == 0){
                              this.selectChange()
                            }
                        })
                },
                getHitDept(){
                  this.$http.get(`/api/emp/getHitDept/${this.setUserId}`)
                    .then((response) => {
                        if(response.data) {
                          this.setDept = response.data
                        }else{
                          this.setDept = this.$store.state.loginInfo.loginDeptCd
                        }
                    })
                    .catch((response) => {

                    })
                },
                popEmp(){
                  const that = this;

                  this.$modal.open({
                    component: Emp,
                    parent: this,
                    props: {
                      param: this.form.wrtNm
                    },
                    width: 700,
                    events: {
                      close(obj) {
                        that.$http.get(`/api/emp/getHitDept/${obj.empNo}`)
                          .then((response) => {
                            if(response.data) {
                              that.goEmpList(response.data)
                            }else{
                              that.$swal({ type: 'warning', text: '부서정보가 없습니다.' });
                            }
                          })
                      }
                    }
                  });
                },
            }
        ,
        created() {
            let promise = new Promise(async (resolve, reject) => {
              if(!this.setUserId){
                this.setUserId = this.$store.state.loginInfo.loginId
              }
              resolve()
            })

            promise.then(() => {
              this.getGroupData();
              this.getHitDept();
              if(this.form.lineList.length == 0 && this.selectSeq){
                this.groupName = this.selectSeq
              }
            }).catch(() => {
              console.log("error")
            })

            Object.assign(this.form.lineList, this.lineList);
            Object.assign(this.form.refList, this.refList);

        },
        mounted() {
            this.drawTree();
        },
    }
</script>

<style scoped>
    .desc-content .item.desc-left .desc-item .td-s-thumb.search-area input{
        float: left;
    }

    .desc-content .item.desc-left .desc-item .desc.select select {
        /*width: 70%;*/
    }

    .search-area input {
        position: relative;
    }

    .search-border .td-s-thumb.search-area > input,
    .search-border .td-s-thumb.search-area > .ip-box
    .search-border .td-s-thumb.search-area > button {
        float: left;
    }

    .table-area {
        /*width: 620px;*/
    }

    .btn-wrap, .modal .pop-content .btn-type1 {
        margin-bottom: 0px;
    }

    .table-b {
        margin-bottom: 20px;
    }

    .modal-card {
      width: 960px;
    }
</style>