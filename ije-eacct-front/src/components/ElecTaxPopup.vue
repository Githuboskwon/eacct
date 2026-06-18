<template>
  <layout>
    <span slot="header">{{title}}
      <button class="btn-pop-close delete" aria-label="close" @click="closePop"></button>
      <i class="fas fa-print" style="font-size: 22px !important; float: right; margin-right: 70px; margin-top: 2px; cursor: pointer;" @click="goPrint"></i>
    </span>
    <div slot="content">
      <!-- <div class="btn-type1">
          <button class="btn-size btn-gray fl_right" @click="goPrint">
            <span class="btn-icon"><i class="fas fa-print"></i></span> 출력
          </button>
      </div> -->
      <!-- <div>
        <iframe id="printf" name="printf" :src="url" style="width:100%; height:500px; border:none;"/>
      </div> -->
      <div id="printf">
        <!-- <component :is="etaxScreen" :etaxData="etaxData" :etaxItems="etaxItems"/> -->
        <etaxScreen :etaxData="etaxData" :etaxItems="etaxItems" :etaxScreen="etaxScreen" :broker="broker"/>
        <div style="padding-top:10px;text-align:left; font-size:13px;">주의 : 본 세금계산서는 국세청고시 기준에 따라 발행된 전자세금계산서 입니다.</div>
      </div>
    </div>
  </layout>
</template>

<script>
import Layout from '@/components/ModalSlot.vue'
import EtaxScreen from '@/components/EtaxScreen/EtaxScreen.vue'

export default {
  compatConfig: { MODE: 2 },
  name: 'erpAccount',
  props: {
    eTaxNo: {
      type: String,
      required: true,
    }
  },
  components: {Layout, EtaxScreen},
  data() {
    return {
      title: '전자세금계산서',
      url: '',
      etaxScreen:'',
      etaxData:{},
      etaxItems:[],
      broker:false,
    }
  },
  methods: {
    getEtaxInfo(){
      this.$http.post('/api/ebill/etaxInfo', {issueId:this.eTaxNo})
      .then((response) => {
        this.etaxData = response.data;
        this.etaxScreen = this.etaxData.taxScreen;
        if(this.etaxData.invoicerId){
          var str1 = this.etaxData.invoicerId;
          this.etaxData.invoicerId = str1.substr(0,3)+'-'+str1.substr(3,2)+'-'+str1.substr(5); 
        }
        if(this.etaxData.invoiceeId){
          var str2 = this.etaxData.invoiceeId;
          this.etaxData.invoiceeId = str2.substr(0,3)+'-'+str2.substr(3,2)+'-'+str2.substr(5); 
        }
        if(this.etaxData.brokerId){
          var str3 = this.etaxData.brokerId;
          this.etaxData.brokerId = str3.substr(0,3)+'-'+str3.substr(3,2)+'-'+str3.substr(5); 
        }
        var brokerChk = this.etaxData.taxInvDocTypeCode;
        if(brokerChk.includes('03',2) || brokerChk.includes('05',2)){
          this.broker = true;
        }

      })
    },
    getEtaxItem(){
      this.$http.post('/api/ebill/etaxItem', {issueId:this.eTaxNo})
      .then((response) => {
        this.etaxItems = response.data;

      })
    },
    closePop() {
      this.$emit('dismiss')
      this.$parent.close()
    },
  //   goPrint(){
  //     var printDiv = document.getElementById("printf");
      
  //     var pop = window.open(this.url, '_blank'); 
  //     pop.document.write(printDiv.outerHTML); 
      
  //     pop.print(); 
  //     pop.close();
  //   }
  // },
   goPrint(){

    var printDiv = document.getElementById("printf");
    var pop = window.open(this.url, '_blank'); 

    pop.document.write('<html><head><title></title><link rel="stylesheet" type="text/css" href="/css/common.css"></head><body>'); 
    pop.document.write(printDiv.innerHTML); 
    pop.document.write('</body></html>'); 
    
    pop.document.close();
    //pop.focus();
    //The Timeout is ONLY to make Safari work, but it still works with FF, IE & Chrome.
    
    setTimeout(function() {
        pop.print();
        pop.close();
    }, 100);

    // pop.print(); 
    // pop.close();
   }
      
  },
  created() {
    this.getEtaxInfo();
    this.getEtaxItem();
  }
}
</script>