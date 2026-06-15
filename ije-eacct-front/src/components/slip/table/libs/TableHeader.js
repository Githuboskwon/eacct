const fr = {
  props: {
    'value': {
      type: Object,
      required: true
    },
    'readonly': {
      type: Boolean,
      required: false,
      default: false
    }
  },
  template: `
  <tr v-if="value.slipTypeCd !== 'E1' && value.slipTypeCd !== 'E2'">
    <th style="width:60px;">신청번호</th>
    <td colspan="2" style="width:120px;">{{ value.grSlipNo }}</td>

    <th style="width:60px;">전표유형</th>
    <td style="width:120px;">{{ value.slipTypeNm }}</td>

    <th style="width:60px;">전표상태</th>
    <td colspan="3"><span class="color_table_org">{{ value.slipStatNm }}</span></td>
  </tr>
  `
}

const sr = {
  props: {
    'value': {
      type: Object,
      required: true
    },
    'readonly': {
      type: Boolean,
      required: false,
      default: false
    }
  },
  template: `
  <tr v-if="value.slipTypeCd !== 'E1' && value.slipTypeCd !== 'E2'">
    <th>작성부서</th>
    <td>{{ value.wrtDeptCd }} / {{ value.wrtDeptNm }}</td>
    <td>{{ value.bpNm }}</td>

    <th>작성자</th>
    <td>{{ value.wrtNm }}</td>

    <th>작성일자</th>
    <td colspan="3" class="Rt-M">{{ this.$moment(value.wrtDtm).format('YYYY-MM-DD') }}</td>
  </tr>
  `
}

export default {
  functional: true,
  props: {
    'value': {
      type: Object,
      required: true
    },
    'readonly': {
      type: Boolean,
      required: false,
      default: false
    }
  },
  render(createElement, context) {
    const fre = createElement(fr, { props: context.props })
    const sre = createElement(sr, { props: context.props })
    return [fre, sre]
  }
}
