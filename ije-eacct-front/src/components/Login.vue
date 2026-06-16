<template>
  <section class="hero is-fullheight login">
    <div class="hero-body">
      <div class="container">
        <div class="columns is-centered">
          <article class="card is-rounded">
            <div class="card-content">
              <h1 class="title">
                <img src="img/bg_login_title_01.png" alt="">
              </h1>
              <div id="gSignInWrapper">
                <div class="customGPlusSignIn">
                  <select
                      class="select-css"
                      v-model="locale"
                      @change="onChange()"
                      style="display:none !important"
                  >
                    <option value="ko">KR</option>
                    <option value="en">English</option>

                  </select>
                </div>
              </div>
              <br>
              <p class="control has-icon">
                <input v-model="loginInfo.loginId"
                       autocomplete="name"
                       class="input" type="text" name="username"
                       placeholder="사번" autofocus="">
                <i class="fa fa-envelope"></i>
              </p>
              <p class="control has-icon">
                <input v-model="loginInfo.loginPw"
                       autocomplete="new-password"
                       class="input" type="password" name="password"
                       placeholder="PASSWORD" id="myInput" @keypress="caps_lock" @keydown.enter="login">
                <i class="fa fa-lock"></i>
              </p>

              <p id="text" class="ly_box">'<strong>Caps Lock</strong>'이 켜져 있습니다.</p>

              <p class="control">
                <button class="button is-fullwidth btn-login" @click="login">{{$t('login')}}</button>
              </p>
              <br>
              <br>

              <div class="g-signin2" data-onsuccess="onSignIn" data-width="320px" data-longtitle="true"></div>

            </div>
          </article>
        </div>
      </div>
    </div>

    <div class="field2 textR_login" style="display:block !important">
    </div>
    <!-- footer -->
    <div class="footer-login">
      <span class="copyright">COPYRIGHT © ILJIN ALL RIGHTS RESERVED.</span>
    </div>
    <!-- //footer-login -->
  </section>
</template>

<script>
export default {
  props: {
    loginId: {
      type: String,
      required: false,
    },
    token: {
      type: String,
      required: false,
    },
  },
  name: 'Login',
  data() {
    return {
      locale: "ko",
      locales: [],
      loginInfo: {
        loginId: '',
        loginPw: '',
        compCd: '',
        loginDeptNm: '',
        loginDeptCd: '',
        loginCctrNm: '',
        loginCctrCd: '',
        loginJobDutCd: '',
        loginJobDutNm: '',
        loginJobGradeCd: '',
        loginJobGradeNm: '',
        token: '',
        menu: [],
        authorities: [],
      },

      //Google
      CLIENT_ID_GOOGLE: '619347127259-nrgl4vcbqul09c2dejg82sjg5sttuthj.apps.googleusercontent.com',
      redirect_uri_google: 'http%3a%2f%2flocalhost%3a8081%2flogin%2foauth2%2fcode%2fgoogle',
      googleLoginURL: 'https://accounts.google.com/o/oauth2/auth?&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email&approval_prompt=force&access_type=offline',

      //Naver
      CLIENT_ID: 'VHjU8KV0izucgnkmB6kM',
      redirectURI: 'http%3a%2f%2flocalhost%3a8081%2flogin%2foauth2%2fcode%2fnaver',
      //  FIXME state value random string 으로 변경
      state: '2014dlfwls',
      naverLoginURL: 'https://nid.naver.com/oauth2.0/authorize?response_type=code',
    }
  },
  methods: {
    caps_lock() {
      var input = document.getElementById("myInput");
      var text = document.getElementById("text");
      input.addEventListener("keyup", function(event) {

        if (event.getModifierState("CapsLock")) {
          text.style.display = "block";
        } else {
          text.style.display = "none"
        }
      });
    },
    onChange() {
      this.$i18n.locale = this.locale;
      this.$store.commit("setLocale", this.locale);
      this.$cookie.set("xlocal", this.locale, { expires: 7 });
    },
    loginFail() {
      this.$swal({
        animation: false,
        type: 'error',
        text: `로그인 실패했습니다. ID 또는 비밀번호를 확인해 주세요.`,
      });
    },
    login() {
      this.$store.commit('loading');

      
      this.$http.post('/login', this.loginInfo)
      .then(res => res.data)
      .then(data => {
        const loginInfo = {};
        loginInfo.userName = data.userName;
        loginInfo.loginId = data.loginId;
        loginInfo.compCd = data.loginCompCd;
        loginInfo.loginDeptNm = data.loginDeptNm;
        loginInfo.loginDeptCd = data.loginDeptCd;
        loginInfo.loginCctrNm = data.loginCctrNm;
        loginInfo.loginCctrCd = data.loginCctrCd;
        loginInfo.loginJobDutCd = data.loginJobDutCd;
        loginInfo.loginJobDutNm = data.loginJobDutNm;
        loginInfo.loginJobGradeCd = data.loginJobGradeCd;
        loginInfo.loginJobGradeNm = data.loginJobGradeNm;
        loginInfo.token = data.token;
        loginInfo.menu = data.menu;
        loginInfo.authorities = data.authorities;
        loginInfo.loginPw = 'Not Use';

        if(!loginInfo.color){
          loginInfo.color = '/css/common.css'
        }

        this.$nextTick(() => {
          this.$store.commit('login', loginInfo);
          this.$cookie.set('loginInfo', JSON.stringify(loginInfo));
          this.$cookie.set('sessionAlive', true);
          this.$http.defaults.headers['x-auth-token'] = loginInfo.token;
          this.$store.commit('register', '');
          this.$store.commit("setLocale", this.locale);
          this.$router.push({path: '/'});
          //21.03.29 컬러테마
          var link = document.createElement('link');
          link.rel ='stylesheet';
          link.href = this.$store.state.loginInfo.color;
          document.head.appendChild(link);
        });

      })
      .catch(() => {{
        this.loginFail();
      }})
      .finally(() => {
        this.$store.commit('finish');
      });
    },
    signOut() {
      var auth2 = gapi.auth2.getAuthInstance();
      auth2.signOut().then(function () {
        console.log('User signed out.');
      });
    },
    async socialLogIn() {
      console.log("Now Loged in!!!!")
      try {
        this.$store.commit('loading');
        const response = await this.$http.post('/login', this.loginInfo);
        const loginInfo = {};
        loginInfo.userName = response.data.userName;
        loginInfo.loginId = response.data.loginId;
        loginInfo.compCd = response.data.loginCompCd;
        loginInfo.loginDeptNm = response.data.loginDeptNm;
        loginInfo.loginDeptCd = response.data.loginDeptCd;
        loginInfo.loginCctrNm = response.data.loginCctrNm;
        loginInfo.loginCctrCd = response.data.loginCctrCd;
        loginInfo.loginJobDutCd = response.data.loginJobDutCd;
        loginInfo.loginJobDutNm = response.data.loginJobDutNm;
        loginInfo.loginJobGradeCd = response.data.loginJobGradeCd;
        loginInfo.loginJobGradeNm = response.data.loginJobGradeNm;
        loginInfo.token = response.data.token;
        loginInfo.menu = response.data.menu;
        loginInfo.authorities = response.data.authorities;
        loginInfo.loginPw = 'Not Use';

        this.$store.commit('login', loginInfo);
        this.$cookie.set('loginInfo', JSON.stringify(loginInfo));
        this.$cookie.set('sessionAlive', true);
        this.$http.defaults.headers['x-auth-token'] = loginInfo.token;
        this.$store.commit('register', '');
        this.$router.push({path: `/`});
        this.$store.commit('finish');
      } catch(err) {
        this.$store.commit('finish');
        this.loginFail();
      }
    }
  },
  created() {

    /*if(this.$store.state.loginInfo !== null && this.$store.state.loginInfo.token !== '') {
      this.loginId = this.$store.state.loginInfo.loginId;
      this.token = this.$store.state.loginInfo.token;
  
      this.loginInfo.loginId = this.loginId;
      this.loginInfo.token = this.token;
      
    } else {
      this.loginId = this.$route.query['loginId'];
      this.token = this.$route.query['token'];
  
      this.loginInfo.loginId = this.loginId;
      this.loginInfo.token = this.token;
    }*/


    function renderButton() {
      gapi.signin2.render('my-signin2', {
        'scope': 'profile email',
        'width': 240,
        'height': 50,
        'longtitle': true,
        'theme': 'dark',
        'onsuccess': onSuccess,
        'onfailure': onFailure
      });
    }
    function onSuccess(googleUser) {
      console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
    }
    function onFailure(error) {
      console.log(error);
    }

    //모바일 iframe 메시지 수신을 통한 로그인
    window.addEventListener('message', async event => {
      // console.log(event.data.id);
      if(event.origin === process.env.VUE_APP_MO_URL) {
        // console.log(`pc:event.origin : ${event.origin}`)
        if(event.data.message === 'isPcLogin') {

          const ret = this.$store.state.loginInfo !== null && this.$store.state.token !== '';
          window.parent.postMessage({
            message: 'pcLogin',
            value: ret
          }, '*');
          console.log('pc:isPcLogin 수신 후 pcLogin' + ret + '보냄');

        } else if(event.data.message === 'login') {

          console.log('pc:login 하라는 메시지 받음');

          if(!this.$store.state.loginInfo) { //로그인 안되어있다면
            this.loginInfo.loginId = event.data.loginId;
            this.loginInfo.loginPw = event.data.loginPw;
            await this.login();
            const ret = this.$store.state.loginInfo !== null && this.$store.state.token !== '';

            window.parent.postMessage({
              message: 'pcLogin',
              value: ret
            }, '*');
            console.log('pc:현재 로그인 안되어있어서 로그인 시키고 pcLogin:' + ret + ' 메시지 보냄');

          } else {
            window.parent.postMessage({
              message: 'pcLogin',
              value: true
            }, '*');
            console.log('pc:현재 로그인 되어있어서 pcLogin true 메시지 보냄');
          }
        }
      }
    })

    this.googleLoginURL += '&client_id=' + this.CLIENT_ID_GOOGLE
    this.googleLoginURL += '&redirect_uri=' + this.redirect_uri_google

    this.naverLoginURL += '&client_id=' + this.CLIENT_ID
    this.naverLoginURL += '&redirect_uri=' + this.redirectURI
    this.naverLoginURL += '&state=' + this.state

  },
  beforeMount() {
    // 페이지 리로드를 위해 localStorage에 vuex 제거
    localStorage.removeItem('vuex');

    console.log("this.token : " + this.token);
    if(this.token) {
      if(this.token == "null") {
        this.$store.state.oauthNew = 'new';
        //this.$router.push({path:`/oauth/new`});

      } else {
        console.log("Now redirect MainPage...")
        //this.socialLogIn();
        // SSO 제거: 토큰 기반 SSO 로그인 비활성화 (id/pw 폼 로그인만 사용)
      }
    }
  },
  mounted() {
    this.$i18n.locale = this.locale;
    this.onChange();
  }
};
</script>
<style scoped>
#text {display:none;color:black}

.ly_box {
  position:relative;
  margin-bottom: 10px;
  width:230px;
  height:30px;
  background:white;
  border-radius: 10px;
  text-align: center;
}
.ly_box:after {
  border-top:0px solid transparent;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-bottom: 10px solid white;
  content:"";
  position:absolute;
  top:-10px;
  left:100px;
}
</style>
