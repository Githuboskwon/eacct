<template>
<layout>
  <span slot="header">
    메뉴얼 다운로드
    <button class="btn-pop-close delete" aria-label="close" @click="$dismiss"></button>
  </span>
  <div slot="content">
    <ul>
      <li style="padding-bottom:10px; padding-left:85px; text-align:left;"><i class="far fa-file-powerpoint"></i>&nbsp;&nbsp;<a href="./documents/전자전표_사용자 매뉴얼_v1_20240126.pptx">일진전기 전자전표 사용자 매뉴얼(종합)</a></li>
      <!-- <li style="padding-bottom:10px; padding-left:85px; text-align:left;"><i class="far fa-file-powerpoint"></i>&nbsp;&nbsp;<a href="./documents/일진전기 전자전표_모바일_사용자 매뉴얼_V0_20211110.ppt">일진전기 전자전표 모바일 사용자 매뉴얼</a></li> -->
      <!-- <li class="ap"><a data-ajax="false" href="itms-services://?action=download-manifest&url=https://ebid.iljin.co.kr/apple/ijdia_eas.plist">iPhone</a></li> -->
      <!-- <li style="text-align:center;">&nbsp;&nbsp;<a onclick="window.open('http://197.200.59.35:30000/dti/uploadTaxInvoiceXml','세금계산서 XML 원본 업로드','width=850,height=500,location=no,status=no,scrollbars=yes');">세금계산서 XML 원본 업로드</a></li> -->
    </ul>
  </div>
</layout>
</template>

<script>
import Layout from '@/components/ModalSlot3.vue'

export default {
  compatConfig: { MODE: 2 },
  components: {
    Layout
  },
  data() {
    return {
    }
  },
  methods: {
    $dismiss() {
      this.$emit('dismiss')
      this.$parent.close()
    },
    
  },
  created() {
    
  }
}

function _url(...args) {
  args = args.map(x => String(x || '').trim().replace(/^\/*|\/*$/g, ''))
  args = args.filter(x => x)
  return args.join('/')
}
</script>

<style lang="scss" scoped>

</style>
