<template>
  <layout style="width:50vw;height:80vh;">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close"
                                             @click="closeModal"></button></span>
    <div slot="content">
      <div class="btn-type1">
        <button class="btn-size btn-gray" @click="save"><span class="btn-icon">
                    <i class="fas fa-check"></i></span> 발송
        </button>
      </div>

      <div class="inner-box">
        <table id="basic">
          <colgroup>
            <col style="width:10vw;">
            <col style="width:30vw;">
          </colgroup>
          <tbody>
          <tr style="">
            <th class="tp-a" style="border-top: 1px solid #d7d7d7"> 수신자</th>
            <td style="border-top: 1px solid #d7d7d7"><span>{{receiver}}</span></td>
          </tr>
          <tr style="">
            <th class="tp-a" > 제목</th>
            <td><textarea v-model="form.subject"></textarea></td>
          </tr>
          <tr>
            <th class="tp-a"> 내용</th>
            <td><textarea style="height:45vh;" v-model="form.text"></textarea></td>
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
  name: 'MailSendPop',
  props: {
    sendList:{
      type:Array,
      required:false
    },
    mailTypeCd:{
      type:String,
      required:false
    },
  },
  mixins: [mixin],
  components: {Layout},
  data() {
    return {
      title: '메일 발송',
      receiver: '',
      form: {
        subject: '',
        text:'',
      }
    }
  },
  methods: {
    save() {
      let textIncludingEnter= this.form.text.replace(/(?:\r\n|\r|\n)/g, '<br>');
      this.$store.commit('loading')
      this.$http.post('/api/mailSend/send', {
        sendList: this.sendList,
        mailTypeCd: this.mailTypeCd,
        subject: this.form.subject,
        text: textIncludingEnter,
      })
      .then(response => {
        this.closeModal();
        this.$swal({ type: 'info', text: '메일 발송이 완료되었습니다.' });
        this.$emit('close')
      })
      .catch((e) => {
        this.$swal({ type: "warning", text: e });
        this.closeModal();
        this.$emit('close');
      }).finally(() => {
        this.$store.commit('finish')
      });
    },
    closeModal() {
      this.$parent.close();
    },
  },
  created() {
    if(this.sendList.length > 1) {
      this.receiver = this.sendList[0].empNm + ' 외 '+ (this.$numeral(this.sendList.length).value() - 1) + '명';
    } else {
      this.receiver = this.sendList[0].empNm
    }

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
