<template>
  <div  style="white-space:nowrap; overflow:auto;  ">

    <div class="table-a fixed-th">
      <div class="table-name">
        <h3 class="ico_table_name">{{title}}</h3>
      </div>
      <table id="basic" class="table">
        <colgroup>
        </colgroup>
        <tbody>
        <tr>
          <th class="tp-a ">입찰일자</th>
          <th>제품상세</th>
          <th>본품중량(KG/대)</th>
          <th>본품CBM(대)</th>
          <th>부품중량(KG/대)</th>
          <th>부품CBM(대)</th>
          <th>도착지</th>
          <th>낙찰업체</th>
          <th>선적모드</th>
          <th>입찰통화</th>
          <th>입찰환율</th>
          <th>낙찰금액</th>
        </tr>
        <tr>
          <td rowspan="4">
            <span class="search_con">
              <span class="datepicker w-type-ymd">
                <dhx-calendar class="input ddate sDate" v-model="form.evdDt" />
                <!-- @change="setPayDueDt(form.payTermCd)" :disabled="disableEvdDt"/> -->
              </span>
            </span>
          </td>
          <td rowspan="4">
            <textarea id="story" name="story" rows="7" cols="33" style="width:100%; height: 100%; border: 0;"></textarea>
          </td>
          <td rowspan="4"><input type="text" name="text" size="20" style="width:100%; height: 100%; border: 0;">
          </td>
          <td rowspan="4"></td>
          <td rowspan="4"></td>
          <td rowspan="4"></td>
          <td rowspan="4"></td>
          <td rowspan="4"></td>
          <td >국내</td>
          <td ></td>
          <td ></td>
          <td ></td>
        </tr>
        <tr>
          <td >해상</td>
          <td ></td>
          <td ></td>
          <td ></td>
        </tr>
        <tr>
          <td >항공</td>
          <td ></td>
          <td ></td>
          <td ></td>
        </tr>
        <tr>
          <td >합계</td>
          <td ></td>
          <td ></td>
          <td ></td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>

import DhxCalendar from '@/components/DhxCalendar.vue'
import {AgGridVue} from "ag-grid-vue";

export default {
  compatConfig: { MODE: 2 },
  components: {DhxCalendar, AgGridVue},
  name: 'EstimateDetailReg',
  props: [],
  mixins: [ ],
  data() {
    return {
      title: '입찰상세코드',
      search: '',
      returnObject: {},
      dataDetails:[],
      dataInfo:[],
      pageSize: 10,
      curPage :1,
      maxPage : 0,
      curRow : 0,
      subData:[],
      form: {
          evdDt: this.$moment().format('YYYY-MM-DD'),
          barcodeValue: '',
          searchVendorCode: '',
      },
    }
  },
  methods: {
  },
  updated(){
  },
  mounted() {
    //this.goSearch()
  },
};
</script>

<style scoped>
.gridbox{height: 258px!important;}
.gridbox .objbox{height:185px!important;}

</style>