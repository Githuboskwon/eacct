<template>
  <layout>
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close"
                                             @click="closeModal"></button></span>
    <div slot="content">
      <div class="btn-type1">
        <button class="btn-size btn-gray" @click="save"><span class="btn-icon">
                    <i class="fas fa-check"></i></span> 확인
        </button>
      </div>

      <div class="inner-box">
        <table id="basic">
          <colgroup>
            <col width="120px">
            <col width="480px">
          </colgroup>
          <tbody>
          <tr style="">
            <th class="tp-a" style="border-top: 1px solid #d7d7d7"> 제목</th>
            <td style="border-top: 1px solid #d7d7d7"><span>{{docTitleNm}}</span></td>
          </tr>
          <tr v-if="docType==='verifyReject'">
            <th class="tp-a">전표 재사용 가능 여부</th>
            <td>
              <div class="control">
                <div class="dp-ib fl_left">
                  <input type="radio" id="radio01" v-model="slipReusePossibleYn" value="Y"/>
                  <label for="radio01" class="NotoM">Y</label>
                </div>
                <div class="dp-ib fl_left">
                  <input type="radio" id="radio02" v-model="slipReusePossibleYn" value="N"/>
                  <label for="radio02" class="NotoM">N</label>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th class="tp-a"> 의견</th>
            <td><textarea v-model="apprDesc"></textarea></td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'
import mixin from '@/mixin';

export default {
  name: 'ApprActPopDrafter',
  props: {
    docTitleNm:{
      type:String,
      required:false
    },
    docType: {
      type: String,
      default: 'submit'
    }
  },
  mixins: [mixin],
  components: {Layout},
  data() {
    return {
      title: '기안 의견',
      apprDesc:'상신합니다.',
      slipReusePossibleYn: 'Y',
    }
  },
  methods: {
    save() {
      let params = {
        apprDesc : this.apprDesc,
        slipReusePossibleYn : this.slipReusePossibleYn,
      }
      this.$emit('close', params)
    },
    closeModal() {
      this.$parent.close();
    },
    setApprInfo() {
      if(this.docType === 'appr') {
        this.title = '결재 의견';
        this.apprDesc = '결재합니다.';
      } else if (this.docType === 'verify') {
        this.title = '검인 의견';
        this.apprDesc = '검인합니다.';
      } else if(this.docType === 'verifyReject' || this.docType === 'reject') {
        this.title = '반려 의견';
        this.apprDesc = '반려합니다.';
      }
    }
  },
  created() {
    this.setApprInfo()

  }
};
</script>

<style scoped>
div#gridbox {
  width: 100%;
  height: 100%;
  min-height: 300px;
}

</style>
