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
                      <td style="border-top: 1px solid #d7d7d7"><span>{{form.docTitleNm}}</span></td>
                  </tr>
                  <tr>
                      <th class="tp-a"> 의견</th>
                      <td><textarea v-model="form.apprDesc"></textarea></td>
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
      name: 'ApprActPop',
      props: {
        apprList : {
            type: Array,
        },
        docType: {
            type: String,
            default: 'appr',
        }
      },
      mixins: [mixin],
      components: {Layout},
      data() {
          return {
              title: '일괄결재',
              form: {
                  apprStatus: '',
                  docTitleNm: '',
                  apprDesc: '결재합니다.',
              },
          }
      },
      methods: {
          save() {
              let apprList = this.apprList

              if(this.form.apprDesc === '') {
                  this.$swal({type:'error', text: '의견을 입력해주세요.'});
                  return false;
              }

              this.$store.commit('loading')

              for(let i = 0; i < apprList.length; i++) {
                apprList[i].compCd = this.$store.state.loginInfo.compCd;
                apprList[i].apprDesc = this.form.apprDesc;
              }

              if(this.docType === 'appr') {
                this.$http.post(`/api/appr/bundle/doApproval`, this.apprList)
                .then((response) => {
                  this.closeModal();
                  this.$swal({ type: 'info', text: '결재가 완료되었습니다' });
                  this.$emit('close', apprList)
                })
                .catch((e) => {
                  console.error(e);
                  this.$message.error({type: 'error', message: `${e.message}`});
                }).finally(() => {
                  this.$store.commit('finish')
                });
              } else if(this.docType === 'verify') {
                this.$http.post(`/api/appr/bundle/verify`, this.apprList)
                .then((response) => {
                  this.closeModal();
                  this.$swal({ type: 'info', text: '검인이 완료되었습니다' });
                  this.$emit('close', apprList)
                })
                .catch((e) => {
                  console.error(e);
                  this.$message.error({type: 'error', message: `${e.message}`});
                }).finally(() => {
                  this.$store.commit('finish')
                });
              }

          },
          closeModal() {
              this.$parent.close();
          }
      },
      mounted() {
          if(this.apprList.length > 1) {
            if(this.docType === 'appr') {
                this.form.docTitleNm = this.apprList[0].docTitleNm + ' 외 '+ (this.$numeral(this.apprList.length).value() - 1) + '건';
            } else {
                this.form.docTitleNm = this.apprList[0].slipNo + ' 외 '+ (this.$numeral(this.apprList.length).value() - 1) + '건';
            }
          } else {
            if(this.docType === 'appr') {
                this.form.docTitleNm = this.apprList[0].docTitleNm;
            } else {
                this.form.docTitleNm = this.apprList[0].slipNo;
            }
          }
          if(this.docType === 'appr') {
            this.title = '일괄결재';
            this.form.apprDesc = '결재합니다.';
          } else if(this.docType === 'verify') {
            this.title = '일괄검인';
            this.form.apprDesc = '검인합니다.';
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
