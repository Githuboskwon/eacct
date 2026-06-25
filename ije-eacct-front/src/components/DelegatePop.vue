<template>
  <layout>
    <span slot="header">{{ title }}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
    <div slot="content">
      <div class="btn-wrap btn-type1 clearfix">

        <button class="btn-size btn-orange fl_right" @click="deleteRow()" style="margin-left: 10px;" v-if="flag !== 'I' && delegateStatCd == '1' ">
          <span class="btn-icon">
            <i class="fas fa-pen-alt"></i>
          </span>
          해제
        </button>

        <button class="btn-size btn-gray fl_right" @click="save()">
          <span class="btn-icon">
            <i class="far fa-file"></i>
          </span>
          저장
        </button>


      </div>

      <div class="table-area">
        <div class="table-a fixed-th">

          <table id="basic" class="table" style="border: #adadad solid 1px;">
            <colgroup>
              <col width="12%"><col width="8%"><col width="8%"><col width="12%"><col width="16%"><col width="12%"><col width="22%">
            </colgroup>
            <tbody>
            <tr>
              <th class="tp-a"  colspan="2" style="text-align: center; border-left:none" >위임자</th>
              <td colspan="6" v-if=" flag === 'I' && delegateUseFlag">
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:150px;float:left" v-model="form.giveUserId" disabled>
                    <input class="input" type="text" style="width:calc(100% - 155px);float:left;margin-left:5px" v-model="form.giveUserNm" @input="initGiver" @keypress.enter="popEmp('g')">
                    <button class="icon is-right" @click="popEmp('g')"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </td>

              <td colspan="6" v-else-if=" flag === 'I' && !delegateUseFlag">
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:150px;float:left" v-model="form.giveUserId" disabled>
                    <input class="input" type="text" style="width:calc(100% - 155px);float:left;margin-left:5px" v-model="form.giveUserNm" disabled>
                  </div>
                </div>
              </td>


              <td colspan="6" v-else>
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:100%; float:left; " v-model="form.giveUserNm" disabled>
                  </div>
                </div>
              </td>

            </tr>
            <tr>
              <th class="tp-a" colspan="2" style="text-align: center">수임자</th>
              <td colspan="6" v-if=" flag === 'I'">
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:150px;float:left" v-model="form.receiveUserId" disabled>
                    <input class="input" type="text" style="width:calc(100% - 155px);float:left;margin-left:5px" v-model="form.receiveUserNm" @input="initReceiver" @keypress.enter="popEmp('r')">
                    <button class="icon is-right" @click="popEmp('r')"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </td>

              <td colspan="6" v-else>
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:100%; float:left; " v-model="form.receiveUserNm" disabled>
                  </div>
                </div>
              </td>

            </tr>

            <tr>
              <th class="tp-a" colspan="2" style="text-align: center">위임기간</th>
              <td colspan="8">
                <div style="float: left; width: 100%">
                  <div class="form-input" style="display: flex;">
                    <div class="datepicker w-type-ymd">
                      <dhx-calendar class="input" v-model="form.startDate"/>
                    </div>
                    <span> ~ </span>
                    <div class="datepicker w-type-ymd">
                      <dhx-calendar class="input" v-model="form.endDate"/>
                    </div>
                  </div>
                </div>
              </td>
            </tr>


            <tr>
              <th colspan="2" style="text-align: center">비고</th>
              <td colspan="6" style="border-bottom:none">
                <div class="td-s-thumb search-area">
                  <input class="input" type="text" style="width:200px;float:left" v-model="form.remark">
                </div>
              </td>
            </tr>
            </tbody>
          </table>

        </div>
      </div>

    </div>
  </layout>
</template>

<script>
/**
 * * components
 */
import Layout from '@/components/ModalSlot.vue';
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";
import Emp from "@/components/Emp_Ag";
import DhxCalendar from '@/components/DhxCalendar.vue'

/**
 * * Tab 구분 컴포넌트
 */

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
  mixins: [],
  props: {
    data: {
      Type: Array
      , required: false
    },
    title: {
      Type: String
      , required: false
    },
    flag: {
      Type: String
      , required: false
    },
    giveUserId: {
      Type: String
      , required: false
    },
    receiveUserId: {
      Type: String
      , required: false
    },
    seq: {
      Type: String
      , required: false
    },
    delegateStatCd: {
      Type: String
      , required: false
    }
  },
  components: {Layout,DhxCalendar},
  data() {
    return {
      rowData:[],
      gridOptions: {},
      authority: '',
      delegateUseFlag: false,
      // authDeptCds: [{'key': 'ACCOUNT', 'value' : '재무담당자'}, {'key': 'ADMIN', 'value' : '관리자'}], // 재무팀과 관리자만 위임자를 변경할 수 있도록
      defaultColDef: {
        resizable: true,
        filter:true,
        sortable: true
      },
      form: {
        compCd : "",
        giveUserId : "",
        receiveUserId : "",
        delegateSeq : "",
        delegateStatCd : "",
        startDate : this.$moment().startOf('month').format('YYYY-MM-DD'),
        endDate : this.$moment().endOf('month').format('YYYY-MM-DD'),
        remark : "",
        regId : "",
        regDtm : "",
        chgId : "",
        chgDtm : "",
        modGiveUserId : "",
        modReceiveUserId : "",
      },
    }
  },
  created() {
    this.form = this.data;

    this.frameworkComponents = {//그르드에서 사용할 외부 comp 등록
      checkboxRenderer: CheckboxCellRenderer,
    };

    //pop slots modal move
    $(".animation-content").addClass('pop_min2');
    $(".animation-content").draggable({'cancel':'.modal-card-body'});
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '4');

  },
  mounted() {

    this.authority = this.$store.state.loginInfo.authorities[0].roleCd;

    this.getDelegateUseRole();

  },
  destroyed() {
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '7');
  },
  methods: {
    save(){ // 저장 / 수정

      if(this.form.giveUserId === ""){
        this.$swal({ type: 'error', text: '위임자를 선택하여 주세요.' })
        return false;
      }else if(this.form.receiveUserId === ""){
        this.$swal({ type: 'error', text: '수임자를 선택하여 주세요.' })
        return false;
      }else if(this.form.startDate === "" || this.form.endDate === ""){
        this.$swal({ type: 'error', text: '위임기간을 선택하여 주세요.' })
        return false;
      }else if(this.form.startDate > this.form.endDate){
        this.$swal({ type: 'error', text: '위임 시작일자가 종료일자 보다 큽니다.' })
        return false;
      }

      if(this.flag === 'I'){

        this.$store.commit('loading');

        this.form.startDate = this.$moment(this.form.startDate).format('YYYYMMDD');
        this.form.endDate = this.$moment(this.form.endDate).format('YYYYMMDD');

        this.$http.post(`/api/delegate/save`,this.form)
            .then(response => {
              this.$swal({ type: 'success', text: '저장 하였습니다.' })
                  .then((result) => {
                  });
            }).catch((e) => {
          this.$swal({ type: 'error', text: e.response.data.message })
        })
            .finally(() => {
              this.$store.commit('finish');
              this.closeModal();
            });

      }else{

        this.$store.commit('loading');

        this.$http.post(`/api/delegate/update`,this.form)
            .then(response => {
              this.$swal({ type: 'success', text: '저장 하였습니다.' })
                  .then((result) => {
                    if (result.value) {
                    }
                  });
            }).catch((e) => {
          this.$swal({ type: 'error', text: e.response.data.message })
        })
            .finally(() => {
              this.$store.commit('finish');
              this.closeModal();
            });

      }
    },
    deleteRow() {
      let vm = this;

      this.$swal({
        type: 'question',
        text: '해당 위임 해제 하시겠습니까?',
        showCancelButton: true
      }).then(r => {
        if (r.value) {
          this.$store.commit('loading')
          this.$http.post(`/api/delegate/delete`, this.form)
              .then((response) => {
                this.$swal({ type: 'success', text: '위임을 해제 하였습니다.' })
                    .then((result) => {
                      if (result.value) {

                      }
                    });
              })
              .catch((e) => {
                this.$swal({ type: 'success', text: '위임 해제를 실패 하였습니다.' })
              })
              .finally(() => {
                this.$store.commit('finish')
                this.closeModal();
              });
        }
      })

    },
    closeModal() {
      this.$parent.close();
      this.$parent.$parent.goSearch();
    },
    popEmp(flag) {
      let vm = this

      let userNm = "";

      if(flag === 'g'){
        userNm = this.form.giveUserNm;
      }else{
        userNm = this.form.receiveUserNm;
      }

      this.$modal.open({
        component: Emp,
        props: {
          param: userNm
        },
        parent: this,
        events: {
          close(obj) {
            if(flag === 'g'){
              vm.receiveGiver(obj)
            }else{
              vm.receiveReceiver(obj)
            }
            vm.$forceUpdate();
          }
        }
      })
    },
    popCctr(clear) {
      let vm = this
      this.$modal.open({
        component: Cctr,
        props: {
          param: this.form.customerNm
        },
        parent: this,
        events: {
          close(obj) {
            vm.receiveCctr(obj)
          }
        }
      })
    },
    receiveGiver(obj) {
      this.form.giveUserId = obj.empNo;
      this.form.giveUserNm = obj.empNm;
    },
    receiveReceiver(obj) {
      this.form.receiveUserId = obj.empNo;
      this.form.receiveUserNm = obj.empNm;
    },
    initGiver(force) {
      if (force === true) this.form.giveUserNm = '';
      if (this.form.giveUserNm === '')this.form.giveUserId = '';
    },
    initReceiver(force) {
      if (force === true) this.form.receiveUserNm = '';
      if (this.form.receiveUserNm === '')this.form.receiveUserId = '';
    },
    onCellClicked(param){
      // this.$emit('close', param)
      // this.closeModal()
    },
    goOpen(){

    },
    getDelegateUseRole() {

      this.$http.get(`/api/code/combo`, {params: {groupCd: "DELEGATE_USE_ROLE_CD"}})
          .then(response => {
            // this.cardUseRole = response.data;
            response.data.forEach(x=>{
              if(this.authority ===  x.key){
                this.delegateUseFlag = true;
              }
            })

            if(!this.delegateUseFlag) {
              this.form.giveUserId = this.$store.state.loginInfo.loginId;
              this.form.giveUserNm = this.$store.state.loginInfo.userName;
            }

          });
    },
  }

}
</script>

<style lang="scss" scoped>
.modal-card {
  width: 800px;
}

.display-area {
  ::v-deep img {
    width: 100%;
  }

  .pop_min2 {
    width: auto !important;
    min-width: 35% !important;
    overflow: hidden;
  }

  .modal table {
    border: #adadad solid 1px !important;
    border-radius: 5px;
  }

  table tbody tr th {
    padding: 6px 23px !important;
    color: #222;
    height: 37px;
  }

}
</style>