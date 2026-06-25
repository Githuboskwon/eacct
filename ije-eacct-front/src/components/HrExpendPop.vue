<template>
    <layout>
    <span slot="header">{{ title }}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
      <div slot="content">
        <div class="btn-wrap btn-type1 clearfix">
<!--          <button class="btn-size btn-gray fl_right" @click="save()">-->
<!--          <span class="btn-icon">-->
<!--            <i class="far fa-file"></i>-->
<!--          </span>-->
<!--            저장-->
<!--          </button>-->
        </div>

        <table id="basic" class="table" style="border: #adadad solid 1px;">
          <colgroup>
            <col width="12%"><col width="8%"><col width="8%"><col width="12%"><col width="16%"><col width="12%"><col width="22%">
          </colgroup>
          <tbody>

          <tr>
            <th colspan="2" style="text-align: center">전표번호</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.slipNo" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">회계일자</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.postingDt" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">경조사원</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.chgNm" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">당사자성명</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.empNo" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">경조금</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="number" style="width:200px;float:left" v-model="form.expendAmt" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">경조구분</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.expendCd" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">경조일</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.expendDt" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">화환</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.wreathYn" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">상조용품</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.mutualYn" readonly>
              </div>
            </td>
          </tr>
          <tr>
            <th colspan="2" style="text-align: center">경조금지급처</th>
            <td colspan="6" style="border-bottom:none">
              <div class="td-s-thumb search-area">
                <input class="input" type="text" style="width:200px;float:left" v-model="form.integrationVendorName" readonly>
              </div>
            </td>
          </tr>

          </tbody>
        </table>

      </div>
  </layout>
</template>

<script>
/**
 * * components
 */
import Layout from '@/components/ModalSlot.vue';
import CheckboxCellRenderer from "@/components/agGrid/checkbox-cell-renderer";
import Cctr from "@/components/Cctr_Ag.vue";
import Emp from "@/components/Emp_Ag";

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
        deptCd: {
          Type: String
          , required: false
        },
        userId: {
          Type: String
          , required: false
        },
        seq: {
          Type: String
          , required: false
        },
    },
    components: {Layout},
    data() {
        return {
            rowData:[],
            gridOptions: {},
            confirmSeqList: [{'key': '1', 'value' : '1'}, {'key': '2', 'value' : '2'}, {'key': '3', 'value' : '3'}, {'key': '3', 'value' : '3'}, {'key': '4', 'value' : '4'}, {'key': '5', 'value' : '5'}, {'key': '6', 'value' : '6'}, {'key': '7', 'value' : '7'}, {'key': '8', 'value' : '8'}, {'key': '9', 'value' : '9'}, {'key': '10', 'value' : '10'}],
            defaultColDef: {
              resizable: true,
              filter:true,
              sortable: true
            },
            form: {
              slipNo : "",
              postingDt : "",
              chgNm : "",
              empNo : "",
              expendAmt : "",
              expendCd : "",
              expendDt : "",
              wreathYn : "",
              mutualYn : "",
              integrationVendorName : ""
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
    destroyed() {
        //ModalSlot open z-index change
        $('.lnb').css('z-index', '7');
    },
    methods: {
        save(){ // 저장 / 수정

          console.log(this.flag);

          if(this.flag === 'I'){

            this.$store.commit('loading');

            this.$http.post(`/api/confirm/save`,this.form)
                .then(response => {
                  this.$swal({ type: 'success', text: '저장 하였습니다.' })
                      .then((result) => {

                      });
                }).catch((e) => {
              this.$swal({ type: 'success', text: '저장을 실패 하였습니다.' })
            })
                .finally(() => {
                  this.$store.commit('finish');
                  this.closeModal();
                });

          }else{

            this.$store.commit('loading');

            let modeFrom = {
              modDeptCd : this.form.deptCd,
              modConfirmUserId : this.form.confirmUserId,
              modConfirmSeq : this.form.confirmSeq,
              modConfirmStartAmt : this.form.confirmStartAmt,
              modConfirmEndAmt : this.form.confirmEndAmt,
              modRemark : this.form.remark,

              confirmStartAmt: this.data.confirmStartAmt,
              confirmEndAmt: this.data.confirmEndAmt,
              remark: this.data.remark,

              deptCd: this.deptCd,
              confirmUserId : this.userId,
              confirmSeq: this.seq,

            }


            this.$http.post(`/api/confirm/update`,modeFrom)
                .then(response => {
                  this.$swal({ type: 'success', text: '저장 하였습니다.' })
                      .then((result) => {

                      });
                }).catch((e) => {
                  this.$swal({ type: 'success', text: '저장을 실패 하였습니다.' })
                })
                .finally(() => {
              this.$store.commit('finish');
              this.closeModal();
            });

          }
        },
        closeModal() {
            console.log(this.$parent)
            this.$parent.close();
        },
        popEmp(clear) {
          let vm = this
          this.$modal.open({
            component: Emp,
            props: {
              param: this.form.confirmUserNm
            },
            parent: this,
            events: {
              close(obj) {
                vm.receiveEmp(obj)
              }
            }
          })
        },
        popCctr(clear) {
          let vm = this
          this.$modal.open({
            component: Cctr,
            props: {
              param: this.form.deptNm
            },
            parent: this,
            events: {
              close(obj) {
                vm.receiveCctr(obj)
              }
            }
          })
        },
        receiveCctr(obj) {
          this.form.deptCd = obj.deptCd;
          this.form.deptNm = obj.deptNm;
        },
        receiveEmp(obj) {
          this.form.confirmUserId = obj.empNo;
          this.form.confirmUserNm = obj.empNm;
        },
        initCctr(force) {
          if (force === true) this.form.deptNm = '';
          if (this.form.deptNm === '') this.form.deptCd = '';
        },
        initEmp(force) {
          if (force === true) this.form.confirmUserNm = '';
          if (this.form.confirmUserNm === '') this.form.confirmUserId = '';
        },
        onCellClicked(param){
          // this.$emit('close', param)
          // this.closeModal()
        },
        goOpen(){

        }
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