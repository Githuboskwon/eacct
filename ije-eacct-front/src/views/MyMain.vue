<template>

  <div class="wrap type-main">
    <!-- header -->
    <div class="header">
      <div class="logo" title="전체메뉴">
        <a href="#none"></a>
        <button>메뉴</button>
      </div>
      <div class="user-info">
        <span class="user-msg">[{{ this.$store.state.loginInfo.loginDeptNm }}] <span class="point-blue">{{ this.$store.state.loginInfo.userName }}</span> <span>({{ this.$store.state.loginInfo.loginId }})</span></span>
        <a href="/" class="ico ico-home" title="HOME"></a>
        <a href="/quickSetting" class="ico ico-top02" title="환경설정"></a>
        <a href="#" class="ico ico-top03" title="메뉴얼" @click="menual()"></a>
        <a href="#" class="ico ico-logout" title="로그아웃" @click="logout"></a>
      </div>
    </div>
    <!-- //header -->
    <!-- menu -->
    <div class="menu-wrap">
      <ul class="menu">
        <li v-for="(menuhead,i) in menuheads" :key="i">
          <a>{{menuhead.menuNm}}</a>
          <ul>
            <li v-for="(mainmenu,i) in mainmenus" :key="i">
              <a v-if="menuhead.menuNo === mainmenu.upperMenuNo">
                <!-- <a :href="mainmenu.programFileNm">{{mainmenu.menuNm}}</a> -->
                <router-link :to="{path:mainmenu.programFileNm}"><a>{{mainmenu.menuNm}}</a></router-link>
              </a>
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <!-- //menu -->

    <!-- contents -->
    <div class="contents2 main_blue">
      <div class="inner">
        <div class="clearfix">
          <!-- 퀵버튼 메뉴 -->
          <div class="main_quick_top">
            <!-- 2.퀵메뉴 -->
            <!--큌메뉴 설정 6개 이하일 경우-->
            <ul v-if="myquicks === null" class="quick-menu" >
              <li v-for="(item, index) in six" :key="index"  class="">
                <a href=""></a>
              </li>
              <li class="ico06-07 card"><router-link to="/quickSetting">환경설정</router-link></li>
            </ul>
            <ul v-else-if="myquicks.length !== 6" class="quick-menu" >
              <li v-for="(quick,i) in myquicks" :key="i" :class="quickIcon[i]">
                <router-link :to="{path:quick.programFileNm}">{{quick.menuNm}}<div class="more_link">자세히보기</div></router-link>

              </li>
              <li v-for="(item, index) in six" :key="index"  class="no_choice">
                <div class="no_choice_icon"></div>
                <h4>개인별 권한</h4>
                <h5>개인별로 권한이 상이함</h5>
              </li>
              <li class="ico06-07 card"><router-link to="/quickSetting">환경설정</router-link></li>
            </ul>
            <!--큌메뉴 설정 6개일 경우-->
            <ul v-else class="quick-menu" >
              <li v-for="(quick,i) in myquicks" :key="i" :class="quickIcon[i]">
                <router-link :to="{path:quick.programFileNm}">{{quick.menuNm}}</router-link>
              </li>
            </ul>
            <!-- //2. 퀵메뉴 -->

          </div>
          <!-- //left 영역 -->

          <!-- 2단영역:법인카드, 경비처리, 사용현황 -->
          <div class="box_wrap">
            <div class="b_left">
              <!-- 3.메인법인카드내역-->
              <div class="white_box_wrap" style="height: 164px;">
                <div class="main_card_box">
                  <div class="c_left">
                    <!--카드 이미지 영역-->
                    <div class="card-box">
                      <img src="img/img_card.png">
                    </div>
                    <!--카드 이미지 영역-->

                    <div class="box_title">
                      <!--h1 class="box-title">법인카드내역_pub</h1-->
                      <!-- <router-link class="btn-more" to="/cardUseLst">더보기</router-link> -->
                      <!-- <router-link to="accrualSlip?lnk=mainCard">더보기</router-link> -->
                      <a @click="accrualSlipGo({empNo: $store.state.loginInfo.loginId, type: 'A'})">더보기</a>
                    </div>
                  </div>
                  <div class="c_right">
                    <div class="line-list">
                      <!--법인카드 미처리 리스트-->
                      <ul class="line-list using-list">
                        <li v-for="(unCard,i) in mymains.unCardList" :key="i">
                          <!-- <router-link to="accrualSlip?lnk=mainCard"> -->
                            <a @click="accrualSlipGo({empNo: $store.state.loginInfo.loginId, type: 'A'})">
                            <span class="store">{{unCard.storeNm}}</span>
                            <span class="money">{{unCard.usedAmt | amt}}<b>원</b></span>
                            <span class="date">{{unCard.usedDt | dateformat('YYYY-MM-DD')}}</span>
                            </a>
                          <!-- </router-link> -->
                        </li>
                      </ul>
                      <!--법인카드 미처리 리스트-->
                    </div>
                  </div>
                </div>


              </div>
              <!-- 3.메인법인카드내역-->

              <!-- 4.위임 법인카드 미처리 -->
              <div class="white_box_wrap mgt20" style="height: 185px;">
                <div class="box_title mgb10">
                  <h1 class="box-title dp-ib">위임 법인카드 미처리(위임자)</h1>
                  <!-- <router-link class="btn-more mgl10" to="/slipLst">더보기</router-link> -->
                </div>
                <div class="data-box" style="overflow-y: scroll; height: 82px;">
                  <ul class="line-list">
                    <li v-for="(card,i) in mymains.untrDelegateCard" :key="i">
                      <a @click="accrualSlipGo(card)" style="width: 90%;">
                        <span class="txt">{{card.deptNm}}</span>
                        <span class="txt" style="width: 15%;">{{card.jobNm}}</span>
                        <span class="txt" style="width: 30%; text-align:center;">{{card.empNm}}</span>
                        <span class="money" style="float: right; text-align: right;"><b>{{card.cnt}} 건</b></span>
                      </a>
                    </li>
                  </ul>
                </div>

              </div>
              <!-- //4.위임 법인카드 미처리 -->
            </div>
            <div class="b_right">
              <!-- 5. 사용현황 -->
              <div class="white_box_wrap status">
                <div class="box_title">
                  <h1 class="box-title dp-ib">예산 사용현황</h1>
                </div>
                <div class="data-box" style="height: 300px;">
                  <!-- 그래프 영역 -->
                  <div class="graph-box">
                    <div id="chartbox" style="height: 90%; width: 100%;padding-top:0px; position:relative; display: flex;">
                      <ul class="status-list" style="width: 100%;">
                        <li>
                          <ag-charts-vue :options="this.doughnutChart1"></ag-charts-vue>
                        </li>
                      </ul>
                      <ul class="status-list" style="width: 100%;">
                        <li>
                          <ag-charts-vue :options="this.doughnutChart2"></ag-charts-vue>
                        </li>
                      </ul>
                    </div>
                  </div>
                  <!-- //그래프 영역 -->
<!--                  <div>-->
<!--                    <ul class="status-list">-->
<!--                      <li>-->
<!--                        <ag-charts-vue :options="this.doughnutChart1"></ag-charts-vue>-->
<!--                      </li>-->
<!--                    </ul>-->
<!--                    <ul class="status-list">-->
<!--                      <li>-->
<!--                        <ag-charts-vue :options="this.doughnutChart2"></ag-charts-vue>-->
<!--                      </li>-->
<!--                    </ul>-->
<!--                  </div>-->
<!--                  <div>-->

<!--                    <ul v-if="psConds !==''" class="status-list">-->
<!--                      <li v-for="(psCond,i) in psConds" :key="i">-->
<!--                        <span class="dot"></span>{{psCond.acctName}}-->
<!--                      </li>-->
<!--                    </ul>-->

<!--                    <ul v-if = "psConds.length===0" class="status-list">-->
<!--                      <li>-->
<!--                        <span class="dot"></span>없음-->
<!--                      </li>-->
<!--                    </ul>-->
<!--                  </div>-->

                </div>
              </div>

              <!-- //5.사용현황 -->
            </div>
          </div>
          <!-- //2단영역:법인카드, 경비처리, 사용현황 -->
        </div>
        <!-- //clearfix -->

        <!-- 6.메인처리 예정건수-->
        <div class="main_ing_wrap">
          <ul>
            <li class="blue blue01">
              <span class="count">
                  <div class="c_left">
                      <!-- <router-link to="accrualSlip?lnk=main"> -->
                        <a @click="accrualSlipGo({empNo: $store.state.loginInfo.loginId, type: 'A'})">
                          <span> {{ untrCard }}</span><b>건</b>
                        </a>
                      <!-- </router-link> -->
                  </div>
                  <div class="c_right" style="display:none">0<b>건</b></div>
              </span>
              <span class="txt-kor">법인카드 미처리</span>
              <span class="txt-eng">Corporate card unprocessed</span>
            </li>
            <li class="blue blue02">
              <span class="count">
                  <div class="c_left">
                      <a @click="ebillSlipRcvLstGo">
                        <span> {{ untrEtax }}</span><b>건</b>
                      </a>
                  </div>
                  <div class="c_right" style="display:none">121<b>건</b></div>
              </span>
              <span class="txt-kor">세금계산서 미처리</span>
              <span class="txt-eng">Unprocessed tax invoice</span>
            </li>
            <li class="blue blue03">
              <span class="count">
                  <div class="c_left">
                      <!-- <router-link to="/bdgtReqLst?budStatCd=40"> -->
                    <a @click="accrualSlipGo({empNo: $store.state.loginInfo.loginId, type: 'F'})">
                      <span>{{ untrAirlineCard }}</span><b>건</b>
                    </a>
                      <!-- </router-link> -->
                  </div>
                  <div class="c_right" style="display:none">121<b>건</b></div>
              </span>
              <span class="txt-kor">법인카드 항공권</span>
              <span class="txt-eng">Airline Corporate card unprocessed</span>
            </li>
            <li class="blue blue04">
              <span class="count">
                  <div class="c_left">
                    <!-- <router-link to="/slipLst?slipStatCd=SV">
                        <span>{{ unApprSlip }}</span><b>건</b>
                    </router-link> -->
                    <a @click="slipLstGo('SV')">
                      <span>{{ unApprSlip }}</span><b>건</b>
                    </a>
                  </div>
                  <div class="c_right" style="display:none">0<b>건</b></div>
              </span>
              <span class="txt-kor">전표미상신</span>
              <span class="txt-eng">Not approved for slip</span>
            </li>
            <li class="blue blue05">
              <span class="count">
                  <div class="c_left">
                    <!-- <router-link to="/slipLst?slipStatCd=40">
                        <span>{{ rjct }}</span><b>건</b>
                    </router-link> -->
                    <a @click="slipLstGo('AR')">
                      <span>{{ rjct }}</span><b>건</b>
                    </a>
                  </div>
                  <div class="c_right" style="display:none">0<b>건</b></div>
              </span>
              <span class="txt-kor">전표반려</span>
              <span class="txt-eng">Return a document</span>
            </li>
            <li class="blue blue06">
              <span class="count">
                <div class="c_left">
                    <!-- <router-link to="/apprPendLst">
                        <span>{{ todoAppr }}</span><b>건</b>
                    </router-link> -->
                    <a @click="apprPendLstGo">
                      <span>{{ todoAppr }}</span><b>건</b>
                    </a>
                </div>
                <div class="c_right" style="display:none">0<b>건</b></div>
              </span>
              <span class="txt-kor">결재</span>
              <span class="txt-eng">Approve proposal</span>
            </li>
          </ul>

        </div>
        <!-- 6.메인처리 예정건수-->
      </div>
      <!-- //inner -->
    </div>
    <!-- //contents -->

    <!-- footer -->
    <div class="footer2">
      <span class="copyright">COPYRIGHT © ILJIN ALL RIGHTS RESERVED.</span>
    </div>
    <!-- //footer -->
  </div><!-- //wrap -->




</template>

<script>
import menual from '@/components/Menual.vue';
import { AgChartsVue } from 'ag-charts-vue';
import router from '@/router';


export default {
  name: 'MyMain',
  components: {
    menual,
    'ag-charts-vue': AgChartsVue
  },
  computed: {
    untrCard() {
      return this.mymains.counts?.untrCard?.[0]?.cnt || 0;
    },
    untrEtax() {
      return this.mymains.counts?.untrEtax?.[0]?.cnt || 0;
    },
    untrAirlineCard() {
      return this.mymains.counts?.untrAirlineCard?.[0]?.cnt || 0;
    },
    unApprSlip() {
      return this.mymains.counts?.unApprSlip?.[0]?.cnt || 0;
    },
    rjct () {
      return this.mymains.counts?.rjct?.[0]?.cnt || 0;
    },
    todoAppr() {
      return this.mymains.counts?.todoAppr?.[0]?.cnt || 0;
    }
  },
  data() {
    return {
      //메인화면 전체데이터
      mymains: {},
      //1.메인메뉴
      mainmenus:[],
      menuheads:[],
      menulink:[],
      //2.메인바로가기(퀵메뉴)
      mysets:[],
      myquicks:[],
      six:[],
      quickIcon:[],
      //3.메인법인카드내역
      unCardLists:[],
      uCard:[],
      //4.메인경비처리내역
      rcSlipLists:[],
      rcSlipData:[],
      //5. 사용현황
      psConds:[],
      piData:[],
      doughnutData1: [],
      doughnutData2: [],
      doughnutChart1: '',
      doughnutChart2: '',
      //6.메인처리예정건수
      uncounts: [],
      cardTrxType: [], //법인카드 이동 시 해당 카드의 공통코드에서 셋업되어진 거래유형 값
    }
  },
  /* 숫자에 콤마(,)넣기*/
  filters:{
    currency:function (value) {
      var num = new Number(value);
      return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g,"$1,")
    }
  },
  async mounted() {
    const [ mains, chart, cardTrxType ] = await Promise.all([this.getMyMain(), this.getMainChart(), this.$http.get('/api/code/detail', { params: { groupCd: 'CARD_SLIP_CD' } })])

    //1. 메인 대시보드 카운트
    this.mymains = mains;
    //2. 대시보드 차트 데이터 셋
    this.setChartData(chart);
    //3. 법인카드 이동 전표 유형 코드 셋
    this.cardTrxType = cardTrxType.data;

    this.getMainMenuList();
    this.getMyQuickMenus();
    this.mainMenuAction();

    this.$store.commit('setOpenDate', {localMonthDate: this.mymains.localMonthDate,  nextMonthDate: this.mymains.nextMonthDate});
    
    //this.getMySet();
    //this.getPsCond();
    //this.getRcSlipList();
    //this.getUnCardList();
  },
  methods: {
    /**
     * 법인카드 내역 팝업 호출까지의 시작...
     */
    accrualSlipGo(value) {
      
      const cardType = value.type || 'A';
      const defaultCd = this.cardTrxType.filter( f => {
        return cardType === 'A' ? f.remark1 === 'CARD' : f.remark1 === 'ACARD'
      })[0].detailCd;

      this.$router.push({name: "accrualSlip", params: {
        memberInfo: {
          name: value.empNo,
          type: cardType, //카드타입
          trxCd: defaultCd,
          options: {
            localMonthDate: this.mymains.localMonthDate, 
            nextMonthDate: this.mymains.nextMonthDate,
          }
        }
      }});
    },
    /**
     * 전자세금계산서 메인화면에서 호출
     */
    ebillSlipRcvLstGo(value) {
      this.$router.push({name: "ebillSlipRcvLst", params: {
          dateSet: {
              startDate: this.mymains.localMonthDate,
              endDate: this.mymains.nextMonthDate,
          }
        }});
    },
    /**
     * 전표 미상신, 반려 메인화면에서 호출
     */
     slipLstGo(status) {
      this.$router.push({
        path : '/slipLst', 
        name:'slipLst',
        query: {
          slipStatCd: status,
        },
        params: {
          dateSet: {
              startDate: this.mymains.localMonthDate,
              endDate: this.mymains.nextMonthDate,
          }
        }
      }).catch(()=>{
        this.$router.push({path : '/slipLst?slipStatCd=SV'})
      });
    },
    /**
     * 결재건 이동
     */
    apprPendLstGo() {
      this.$router.push({
        path : '/apprPendLst', 
        name:'apprPendLst',
        params: {
          dateSet: {
              startDate: this.mymains.localMonthDate,
              endDate: this.mymains.nextMonthDate,
          }
        }
      });
    },
    //21.03.25 메뉴얼 다운로드
    menual(){
      this.$modal.open({
        component: menual,
      });
    },
    //로그아웃
    logout() {

      this.$swal({
        type: 'info',
        html: `그룹웨어 로그아웃이 선행되야 로그아웃이 됩니다.<br>아닌경우 다시 자동 로그인 처리됩니다.<br>로그아웃을 진행하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then((result) => {
        if (result.value) {
          this.$http
              .get('/logout')
              .then(() => {
                this.$store.commit('logout');
                this.$cookie.delete('loginInfo');
                this.$router.push({path: `/`});
              })
              .catch((e) => {
                console.error(e);
              })
        }
      })

      /*
      this.$http
          .get('/logout')
          .then(() => {
            this.$store.commit('logout');
            this.$cookie.delete('loginInfo');
            this.$store.commit('setEvidFileSize', 0);
            this.$store.commit('setJiniSize', 0);
            this.$router.push({path: `/`});
          })
          .catch((e) => {
            console.error(e);
          });
       */
    },
    //상단 메인메뉴 리스트
    getMainMenuList() {
      this.$http.get(`/api/menu/list/${this.$store.state.loginInfo.loginId}`)
          .then(response => {
            //1. 메인메뉴
            this.mainmenus = response.data;
            let menuupper=[];
            for(let i=0; i<this.mainmenus.length; i++){
              if(this.mainmenus[i].upperMenuNo==0){
                menuupper.push(this.mainmenus[i]);
              }
            }

            //메뉴순서 정렬
            this.mainmenus.sort(function (a, b) {
              return a.menuOrder < b.menuOrder ? -1 : a.menuOrder > b.smenuOrdereq ? 1 : 0;
            });

            this.menuheads =menuupper;
          });
    },
    //2. 개인별 퀵 메뉴
    getMyQuickMenus() {
      this.$http.get(`/api/menu/quick-menu/${this.$store.state.loginInfo.loginId}`)
          .then(response => {
            this.myquicks = response.data;
            if(this.myquicks==null){
              this.six = 5;
            } else {
              for(let ic=0; ic<this.myquicks.length; ic++){
                switch (this.myquicks[ic].menuIconCd) {
                  case '10':
                    this.quickIcon.push( "ico01 card");
                    break;
                  case '20':
                    this.quickIcon.push( "ico02 card");
                    break;
                  case '30':
                    this.quickIcon.push( "ico03 card");
                    break;
                  case '40':
                    this.quickIcon.push( "ico04 card");
                    break;
                  case '50':
                    this.quickIcon.push( "ico05 card");
                    break;
                  case '60':
                    this.quickIcon.push( "ico06 card");
                    break;
                  case '70':
                    this.quickIcon.push( "ico06-01 card");
                    break;
                  case '80':
                    this.quickIcon.push( "ico06-02 card");
                    break;
                  case '90':
                    this.quickIcon.push( "ico06-03 card");
                    break;
                  case '100':
                    this.quickIcon.push( "ico06-04 card");
                    break;
                  case '110':
                    this.quickIcon.push( "ico06-05 card");
                    break;
                  case '120':
                    this.quickIcon.push( "ico06-06 card");
                    break;
                }
              }
              if(this.myquicks.length!=6){
                this.six = 5-this.myquicks.length;
              }
            }
          });
    },

    //메인화면 데이터 불러오기
    getMyMain() {
      return new Promise((resolve, reject) => {
        this.$http.get(`/api/settings/dashboard/${this.$store.state.loginInfo.compCd}/${this.$store.state.loginInfo.loginId}`)
          .then(response => response.data)
          .then(data => resolve(data))
          .catch(error => reject(error));
      });
    },
    /**
     * 메인 차트 데이터
     */
    getMainChart() {
      return new Promise((resolve, reject) => {
        this.$http.get(`/api/settings/dashboardChart/${this.$store.state.loginInfo.compCd}/${this.$store.state.loginInfo.loginId}`)
          .then(response => response.data)
          .then(data => resolve(data))
          .catch(error => reject(error));
      });
    },
    /**
     * 메인 차트 데이터 셋업
     */
    setChartData(chart) {
      this.doughnutData1 = chart.doughnutData1;
      if(this.doughnutData1 ===null){
          this.doughnutData1 = [];
      } else {

        this.doughnutChart1 = {
          autoSize: true,
          data: this.doughnutData1,
          title: {
            text: '복리후생비',
            fontWeight: 'bold',
          },
          series: [
            {
              type: 'pie',
              calloutLabelKey: 'acctName',
              angleKey: 'tactualAmount',
              innerRadiusRatio: 0.7,
              showInLegend: false,
              tooltip: { renderer: this.renderer },
              innerLabels: [
                {
                  text: this.doughnutData1[0].budgetAmount.toLocaleString(),
                  fontWeight: 'bold',
                },
              ],
            },
          ],
        };
      }

      this.doughnutData2 = chart.doughnutData2;
      if(this.doughnutData2 === null){
        this.doughnutData2 = [];
      } else {
        this.doughnutChart2 = {
          data: this.doughnutData2,
          title: {
            text: '접대비',
            fontWeight: 'bold',
          },
          series: [
            {
              type: 'pie',
              calloutLabelKey: 'acctName',
              angleKey: 'tactualAmount',
              innerRadiusRatio: 0.7,
              showInLegend: false,
              tooltip: { renderer: this.renderer },
              innerLabels: [
                {
                  text: this.doughnutData2[0].budgetAmount.toLocaleString(),
                  fontWeight: 'bold',
                },
              ],
            },
          ],
        };
      }
    },
    //메뉴wrap
    mainMenuAction(){
      $(".wrap.type-main .header .logo > button").click(function(){
        if($(".menu-wrap").css("display") == "none"){
          $(".menu-wrap").slideDown(300);
        }else{
          $(".menu-wrap").slideUp(300);
        }
      });
    },
    goPage(params) {
      // eslint-disable-next-line
      console.log("페이지이동", params)
      let targetName = (params.eaSlipNo === null || params.slipStatCd === '10') ? 'cardSlipReg' : 'billSlipMng';
      this.$router.push({ name: targetName, params: params });
    },
    //////////////////////////////////////////////////////////////////

    renderer( params ) {
      return {
        content: params.datum.tactualAmount.toFixed(0).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
      };
    }
  },
};

</script>


<style scoped lang="scss">
@import "public/css/main";

.wrap.lnb.minimized~.contents2 {
  left: 50px;
}

/* contents */

.contents2 {
  max-width: 100%;
  min-width: 1033px;
  position: absolute;
  right: 0;
  bottom: 0;
  z-index: 2;
  transition: .3s;
  padding: 0px 25px 33px 25px;
  /*overflow-y: auto;*/
}


.contents2>.inner-box {
  padding-bottom: 80px;
  min-height: 80vh;
}

.contents2 .content-name {
  margin-top: 33px;
}

.contents2 .content-name:after {
  clear: both;
  content: '';
  display: block;
}

.contents2 .content-name h2 {
  font-size: 24px;
  color: #061c3a;
  font-family: 'NotoM';
  float: left;
  line-height: 1;
}

.contents2 {
  /*overflow-y:auto;*/
  padding: 0;
}

.contents2>.inner-box {
  max-width: 100%;
  min-width: 1033px;
  padding: 0px 25px 33px 25px;
}
.contents2 div.gridbox_material.gridbox .xhdr {
  background: #f3f5f9;
  border-bottom: none;
  max-height: 100%;
  margin-top: -1px;
}

.contents2 .grid-tbl-box.type03 div.gridbox_material.gridbox .xhdr {
  height: 112px!important;
}
.contents2 div.gridbox_material.gridbox table.hdr td div.hdrcell {
  font-size: 14px;
  line-height: 24px;
  padding: 5.5px 0px;
  text-align: center;
  font-family: NotoM;
  color: #222;
}
.right .white-box .data-box{
  height: 239px;
}
</style>
