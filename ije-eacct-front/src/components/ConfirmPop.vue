<template>
  <layout>
    <span slot="header">{{ title }}<button class="btn-pop-close delete" aria-label="close" @click="closeModal"></button></span>
    <div slot="content">
      <div class="btn-wrap btn-type1 clearfix">
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
              <th class="tp-a" colspan="2" style="text-align: center">부서</th>
              <td colspan="6" v-if=" flag === 'I'">
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:150px;float:left" v-model="form.deptCd" disabled>
                    <input class="input" type="text" style="width:calc(100% - 155px);float:left;margin-left:5px" v-model="form.deptNm" @input="initCctr" @keypress.enter="popCctr">
                    <button class="icon is-right" @click="popCctr(true)"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </td>

              <td colspan="6" v-else>
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:100%; float:left; " v-model="form.deptNm" disabled>
                  </div>
                </div>
              </td>

            </tr>
            <tr>
              <th class="tp-a" colspan="2" style="text-align: center">검인자</th>
              <td colspan="6" v-if=" flag === 'I'">
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:150px;float:left" v-model="form.confirmUserId" disabled>
                    <!--                  <input class="input" type="text" v-show="false" v-model="form.confirmUserId">-->
                    <input class="input" type="text" style="width:calc(100% - 155px);float:left;margin-left:5px" v-model="form.confirmUserNm" @input="initEmp"  @keypress.enter="popEmp">
                    <button class="icon is-right" @click="popEmp(true)"><i class="fas fa-search"></i>
                    </button>
                  </div>
                </div>
              </td>

              <td colspan="6" v-else>
                <div class="td-s-thumb search-area">
                  <div class="ip-box ip-box-w02">
                    <input class="input" type="text" style="width:100%; float:left; " v-model="form.confirmUserNm" disabled>
                  </div>
                </div>
              </td>



            </tr>
            <tr>
              <th class="tp-a" colspan="2" style="text-align: center">검인순서</th>
              <td colspan="6">
                <div style="float: left; width: 100%">

                  <b-select v-if=" flag === 'I'" class="select" v-model="form.confirmSeq" style="width:80%;">
                    <option
                        v-for="item in confirmSeqList"
                        :key="item.key"
                        :value="item.key"
                        v-text="item.value"/>
                  </b-select>

                  <b-select v-else class="select" v-model="form.confirmSeq" style="width:80%;" disabled>
                    <option
                        v-for="item in confirmSeqList"
                        :key="item.key"
                        :value="item.key"
                        v-text="item.value"/>
                  </b-select>
                </div>
              </td>


            <tr>
              <th colspan="2" style="text-align: center">검인기준 시작금액</th>
              <td colspan="6">
                <div class="td-s-thumb search-area">
                  <input class="input" type="number" style="width:200px;float:left" v-model="form.confirmStartAmt">
                </div>
              </td>

            </tr>

            <tr>
              <th colspan="2" style="text-align: center">검인기준 종료금액</th>
              <td colspan="6">
                <div class="td-s-thumb search-area">
                  <input class="input" type="number" style="width:200px;float:left" v-model="form.confirmEndAmt">
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
import Cctr from "@/components/Cctr_Ag";
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
      confirmSeqList: [{'key': '1', 'value' : '1'}, {'key': '2', 'value' : '2'}, {'key': '3', 'value' : '3'}, {'key': '4', 'value' : '4'}, {'key': '5', 'value' : '5'}, {'key': '6', 'value' : '6'}, {'key': '7', 'value' : '7'}, {'key': '8', 'value' : '8'}, {'key': '9', 'value' : '9'}, {'key': '10', 'value' : '10'}],
      defaultColDef: {
        resizable: true,
        filter:true,
        sortable: true
      },
      form: {
        deptCd:"",
        deptNm:"",
        confirmUserId:"",
        confirmUserNm:"",
        confirmSeq:"",
        confirmStartAmt : "",
        confirmEndAmt : "",
        remark : ""
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

      if(this.form.deptCd == ''){
        this.$swal({ type: 'error', text: '부서를 선택하여 주세요.' })
        return false;
      }else if(this.form.confirmUserId == ''){
        this.$swal({ type: 'error', text: '검인자를 선택하여 주세요.' })
        return false;
      }else if(this.form.confirmSeq == ''){
        this.$swal({ type: 'error', text: '검인순서를 선택하여 주세요.' })
        return false;
      }else if(this.form.confirmStartAmt > this.form.confirmEndAmt){
        this.$swal({ type: 'error', text: '검인 기준 시작금액이 종료금액 보다 큽니다.' })
        return false;
      }

      if(this.flag === 'I'){

        this.$store.commit('loading');
        this.$http.post(`/api/confirm/save`,this.form)
            .then(response => {
              this.$swal({ type: 'success', text: '저장 하였습니다.' })
                  .then((result) => {
                    this.closeModal();
                  });
            }).catch((e) => {
          this.$swal({ type: 'error', text: e.response.data.message })
        })
            .finally(() => {
              this.$store.commit('finish');
            });

      }else{

        this.$store.commit('loading');
        this.$http.post(`/api/confirm/update`,this.form)
            .then(response => {
              this.$swal({ type: 'success', text: '저장 하였습니다.' })
                  .then((result) => {
                    this.closeModal();
                  });
            }).catch((e) => {
          this.$swal({ type: 'error', text: e.response.data.message })
        })
            .finally(() => {
              this.$store.commit('finish');
            });

      }
    },
    closeModal() {
      this.$parent.close();
      this.$parent.$parent.goSearch();
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
  :global(img) {
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