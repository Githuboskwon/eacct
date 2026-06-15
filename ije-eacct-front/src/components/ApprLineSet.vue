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
                        <dhx-grid ref="gridEmp" v-model="empList" :config="config" @constructGridSuccessful="constructGridSuccessful"/>
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
                            <b-select class="select" id="apprGroupSelect" v-model="groupName" @change.native="selectChange()">
                              <option value="" selected="selected">== 개인결재선 선택 ==</option>
                              <option
                                v-for="item in groupList"
                                :key="item.apprLineTitle"
                                :value="item.apprSeq"
                                v-text="item.apprSeq + '. ' + item.apprLineTitle"/>
                            </b-select>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="grid-wrap">
                        <dhx-grid ref="gridLine" v-model="form.lineList" :config="configLine" @constructGridSuccessful="constructGridSuccessfulLine"/>
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
                    <dhx-grid ref="gridRef" v-model="form.refList" :config="configRef" @constructGridSuccessful="constructGridSuccessfulRef"/>
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

    import DhxGrid from '@/components/DhxGrid.vue'
    import Emp from '@/components/Emp_Ag.vue';

    export default {
        name: 'ApprLineSet',
        mixins: [mixin, mixinSlip],
        components: {Layout, Emp, DhxGrid},
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
                config : {
                    columns: [
                        {id: 'rn', type: 'cntr', align: 'center', sort: 'na', value: 'No.', width: 35},
                        {id: 'empNm', type: 'ro', align: 'center', sort: 'na', value: '이름'},
                        {id: 'jobNm', type: 'ro', align: 'center', sort: 'na', value: '직급'},
                        {id: 'empNo', type: 'ro', align: 'center', sort: 'na', value: '사번'},
                        {id: 'deptNm', type: 'ro', align: 'center', sort: 'na', value: '부서명'},
                        {id: 'jobDutNm', type: 'ro', align: 'center', sort: 'na', value: '직책명'},
                    ],
                    height: 330,
                },
                configLine : {
                    columns: [
                        {id: 'rn', type: 'cntr', align: 'center', sort: 'na', value: '순서.', width: 35},
                        {id: 'aprverUser', type: 'ro', align: 'center', sort: 'na', value: '이름'},
                        {id: 'jobNm', type: 'ro', align: 'center', sort: 'na', value: '직급'},
                        {id: 'deptNm', type: 'ro', align: 'center', sort: 'na', value: '부서'},
                        {id: 'apprType', type: 'ro', align: 'center', sort: 'na', value: '결재형식'},
                        {id: 'apprTypeCd', type: 'ro', align: 'center', sort: 'na', value: '결재형식'},
                        {id: 'aprverId', type: 'ro', align: 'center', sort: 'na', value: '사번'},
                        {id: 'serveCd', value: '재직여부', hide : true}
                    ],
                    height: 250,
                },
                configRef : {
                  columns: [
                    {id: 'rn', type: 'cntr', align: 'center', sort: 'na', value: '순서.', width: 35},
                    {id: 'aprverUser', type: 'ro', align: 'center', sort: 'na', value: '이름'},
                    {id: 'jobNm', type: 'ro', align: 'center', sort: 'na', value: '직급'},
                    {id: 'deptNm', type: 'ro', align: 'center', sort: 'na', value: '부서'},
                    {id: 'apprType', type: 'ro', align: 'center', sort: 'na', value: '결재형식'},
                    {id: 'apprTypeCd', type: 'ro', align: 'center', sort: 'na', value: '결재형식'},
                    {id: 'aprverId', type: 'ro', align: 'center', sort: 'na', value: '사번'},
                    {id: 'serveCd', value: '재직여부', hide : true}
                  ],
                  height: 319,
                },
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
                constructGridSuccessful(grid) {
                    let apprTypeNm;
                    let outYn = ''
                    grid.setColumnHidden(3,true)
                    grid.setColumnHidden(4,true)
                    grid.setColumnHidden(5,true)
                    grid.attachEvent('onRowDblClicked', (rId) => {
                        outYn = 'N'
                        if(this.apprTypeCd==='20') apprTypeNm = '결재'
                        else if(this.apprTypeCd==='30') apprTypeNm = '합의'

                        if(!this.checkDup(grid.cells(rId,3).getValue())) return

                        if(this.changeLineFlag){
                            //결재
                            this.form.lineList.push(
                                {
                                  aprverUser : grid.cells(rId,1).getValue(),
                                  jobNm : grid.cells(rId,2).getValue(),
                                  aprverId : grid.cells(rId,3).getValue(),
                                  deptNm : grid.cells(rId,4).getValue(),
                                  apprTypeCd : this.apprTypeCd,
                                  apprType : apprTypeNm,
                                  serveCd : '10'
                                }
                            );
                        }else{
                            //참조
                            this.form.refList.push(
                                {
                                  aprverUser : grid.cells(rId,1).getValue(),
                                  jobNm : grid.cells(rId,2).getValue(),
                                  aprverId : grid.cells(rId,3).getValue(),
                                  deptNm : grid.cells(rId,4).getValue(),
                                  apprTypeCd : this.apprTypeCd,
                                  apprType : '참조',
                                  serveCd : '10'
                                }
                            );
                        }

                    });
                },
                constructGridSuccessfulLine(grid) {
                    grid.setColumnHidden(5,true)
                    grid.setColumnHidden(6,true)

                    grid.attachEvent('onRowDblClicked', (rId) => {
                        let index= rId-1
                        let row = this.form.lineList[index]

                        if (row.apprTypeCd === '10') {
                            this.$swal({
                                type: 'warning',
                                text: '선택하신 행은 삭제할 수 없습니다.'
                            })
                        } else {
                            this.form.lineList.splice(index, 1)
                        }
                    });
                },
                constructGridSuccessfulRef(grid) {
                    grid.setColumnHidden(5,true)
                    grid.setColumnHidden(6,true)

                    grid.attachEvent('onRowDblClicked', (rId) => {
                        let index= rId-1
                        let row = this.form.lineList[index]

                        if (row.apprTypeCd === '10') {
                            this.$swal({
                                type: 'warning',
                                text: '선택하신 행은 삭제할 수 없습니다.'
                            })
                        } else {
                           this.form.refList.splice(index, 1)
                        }
                    });
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
                        if (this.$refs.gridEmp.instance.getSelectedRowId() == null)
                            throw '선택된 행이 없습니다.'
                        let index = this.$refs.gridEmp.instance.getSelectedRowId() - 1
                        let row = this.empList[index]

                        let apprTypeNm;

                        if(this.apprTypeCd==='20') apprTypeNm = '결재'
                        else if(this.apprTypeCd==='30') apprTypeNm = '합의'

                        if(!this.checkDup(row.empNo)) return

                        if(this.changeLineFlag){
                          //결재
                          this.form.lineList.push(
                            {
                              aprverUser : row.empNm,
                              jobNm : row.jobNm,
                              aprverId : row.empNo,
                              deptNm : row.deptNm,
                              apprTypeCd : this.apprTypeCd,
                              apprType : apprTypeNm,
                              serveCd : '10'
                            }
                          );
                        }else{
                          //참조
                          this.form.refList.push(
                            {
                              aprverUser : row.empNm,
                              jobNm : row.jobNm,
                              aprverId : row.empNo,
                              deptNm : row.deptNm,
                              apprTypeCd : this.apprTypeCd,
                              apprType : '참조',
                              serveCd : '10'
                            }
                          );
                        }

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
                          if (this.$refs.gridLine.instance.getSelectedRowId() == null)
                            throw '선택된 행이 없습니다.'
                          let index = this.$refs.gridLine.instance.getSelectedRowId() - 1
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
                          if (this.$refs.gridRef.instance.getSelectedRowId() == null)
                            throw '선택된 행이 없습니다.'
                          let index = this.$refs.gridRef.instance.getSelectedRowId() - 1
                          let row = this.form.refList[index]

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
                    _process.apply(this, [0])

                    function _process(retry) {
                        retry = retry || 0

                        try {
                            let grid = null
                            if(this.changeLineFlag){
                                //결재
                                grid = this.$refs.gridLine.instance
                            }else{
                                //참조
                                grid = this.$refs.gridRef.instance
                            }

                            let rId = grid.getSelectedRowId()
                            if (rId === null) {
                                // 선택된 행이 없음
                                this.$swal({
                                    type: 'warning',
                                    text: '선택된 행이 없습니다.'
                                })
                            } else {
                                let rowIndex = grid.getRowIndex(rId)
                                /*if (rowIndex === 0) {
                                    this.$swal({
                                        type: 'warning',
                                        text: '선택된 행은 이동 불가능합니다.'
                                    })
                                    return
                                }*/

                                let alterIndex = null

                                if(this.changeLineFlag){
                                    //결재
                                    alterIndex = rowIndex + 1 >= this.form.lineList.length ? this.form.lineList.length - 1 : rowIndex + 1
                                    let data = this.form.lineList[rowIndex]

                                    this.form.lineList.splice(rowIndex, 1)
                                    this.form.lineList.splice(alterIndex, 0, data)
                                }else{
                                    //참조
                                    alterIndex = rowIndex + 1 >= this.form.refList.length ? this.form.refList.length - 1 : rowIndex + 1
                                    let data = this.form.refList[rowIndex]

                                    this.form.refList.splice(rowIndex, 1)
                                    this.form.refList.splice(alterIndex, 0, data)
                                }


                                setTimeout(() => {
                                    let alterId = grid.getRowId(alterIndex)
                                    grid.selectRowById(alterId)
                                }, 100)
                            }
                        } catch (e) {
                            if (retry < 3) {
                                _process.apply(this, [retry + 1])
                            }
                        }
                    }
                    // try {
                    //     if (this.$refs.gridLine.instance.getSelectedRowId() == null)
                    //         throw '선택된 행이 없습니다.'
                    //     let index = this.$refs.gridLine.instance.getSelectedRowId() - 1
                    //     let row = this.form.lineList[index]
                    //
                    //     let newPos = index + 1;
                    //     if (index === 0)
                    //         throw '선택하신 행은 이동 불가능합니다.'
                    //
                    //     const newContents = JSON.parse(JSON.stringify(this.form.lineList));
                    //     if (newPos >= this.form.lineList.length) newPos = this.form.lineList.length;
                    //
                    //     newContents.splice(index, 1);
                    //     newContents.splice(newPos, 0, row);
                    //     this.form.lineList = newContents;
                    //
                    //     this.$refs.gridLine.instance.selectRow(newPos)
                    // }
                    // catch (e) {
                    //     this.$swal({
                    //         type: 'warning',
                    //         text: e
                    //     })
                    // }
                },
                moveUp() {
                    _process.apply(this, [0])

                    function _process(retry) {
                        retry = retry || 0

                        try {
                            let grid = null
                            if(this.changeLineFlag){
                                //결재
                                grid = this.$refs.gridLine.instance
                            }else{
                                //참조
                                grid = this.$refs.gridRef.instance
                            }

                            let rId = grid.getSelectedRowId()
                            if (rId === null) {
                                // 선택된 행이 없음
                                this.$swal({
                                    type: 'warning',
                                    text: '선택된 행이 없습니다.'
                                })
                            } else {
                                let rowIndex = grid.getRowIndex(rId)
                                let alterIndex = rowIndex - 1 < 0 ? 0 : rowIndex - 1

                                /*if (alterIndex === 0) {
                                    this.$swal({
                                        type: 'error',
                                        text: '첫번째 행으로 이동 불가능합니다.'
                                    })
                                    return
                                }*/

                                if(this.changeLineFlag){
                                    //결재
                                    let data = this.form.lineList[rowIndex]
                                    this.form.lineList.splice(rowIndex, 1)
                                    this.form.lineList.splice(alterIndex, 0, data)
                                }else{
                                    //참조
                                    let data = this.form.refList[rowIndex]
                                    this.form.refList.splice(rowIndex, 1)
                                    this.form.refList.splice(alterIndex, 0, data)
                                }

                                setTimeout(() => {
                                    let alterId = grid.getRowId(alterIndex)
                                    grid.selectRowById(alterId)
                                }, 100)
                            }
                        } catch (e) {
                            if (retry < 3) {
                                _process.apply(this, [retry + 1])
                            }
                        }
                    }
                    // try {
                    //     if (this.$refs.gridLine.instance.getSelectedRowId() == null)
                    //         throw '선택된 행이 없습니다.'
                    //     let index = this.$refs.gridLine.instance.getSelectedRowId() - 1
                    //     let row = this.form.lineList[index]
                    //
                    //     let newPos = index - 1;
                    //     if (index === 0)
                    //         throw '선택하신 행은 이동 불가능합니다.'
                    //     if (newPos === 0)
                    //         throw '첫번쨰 행으로 이동 불가능합니다.'
                    //
                    //     const newContents = JSON.parse(JSON.stringify(this.form.lineList));
                    //     if (newPos >= this.form.lineList.length) newPos = this.form.lineList.length;
                    //
                    //     newContents.splice(index, 1);
                    //     newContents.splice(newPos, 0, row);
                    //     this.form.lineList = newContents;
                    //
                    //     this.$refs.gridLine.instance.selectRow(newPos)
                    // }
                    // catch (e) {
                    //     this.$swal({
                    //         type: 'warning',
                    //         text: e
                    //     })
                    // }
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