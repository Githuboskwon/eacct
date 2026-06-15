<template>
  <div class="lnb" style="z-index:7">
      <ul class="menu-depth01">
        <li v-for="(menuhead,i) in menuheads" :key="i">
            <a href="#">
              <i v-if="menuhead.menuNo === '1000000'" class="lnb-icon fas fa-chalkboard-teacher"></i>
              <i v-else-if="menuhead.menuNo === '1100000'" class="lnb-icon far fa-folder-open"></i>
              <i v-else-if="menuhead.menuNo === '2000000'" class="lnb-icon far fa-folder-open"></i>
              <i v-else-if="menuhead.menuNo === '3000000'" class="lnb-icon far fa-check-circle"></i>
              <i v-else-if="menuhead.menuNo === '4000000'" class="lnb-icon fas fa-coins"></i>
              <i v-else-if="menuhead.menuNo === '5000000'" class="lnb-icon far fa-credit-card"></i>
              <i v-else-if="menuhead.menuNo === '6000000'" class="lnb-icon fas fa-cog"></i>
              <i v-else-if="menuhead.menuNo === '7000000'" class="lnb-icon fas fa-cog"></i>
              <i v-else class="lnb-icon far fa-folder-open"></i>
            {{menuhead.menuNm}}</a>
            <ul class="menu-depth02" >
                <li v-for="(mainmenu,i) in mainmenus" :key="i" v-show="menuhead.menuNo === mainmenu.upperMenuNo">
                        <router-link :to="{path: mainmenu.programFileNm}" class="navbar-item">
                        {{mainmenu.menuNm}}</router-link>
                </li>
            </ul>
        </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'Menu',
  data() {
    return {
        //menu
        mainmenus:[],
        menuheads:[],        
    };
  },
  methods: {
     //1. 메인메뉴
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
            this.menuheads =menuupper;
        });
    }     
  },
  created(){

  },
  mounted(){
      this.getMainMenuList();
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
