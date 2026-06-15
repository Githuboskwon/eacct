<template>
    <div>
        
    </div>
</template>

<script>

export default {
    props: ['value'],
    components: {
        
    },
    data() {
        return {
            
        }
    },
    mounted() {
        // this.setHeaderVat();
    },
    methods: {
        /**
         * * 부가세 헤더 세팅
         */ 
        setHeaderVat() {
            let success = false;
            const {taxEvidenceType} = this.value;
            const params = {
                searchCd: '매입불공제'
            }
            this.$http.post(`/api/surTaxCode/list/${taxEvidenceType}/form`, params)
            .then(res => res.data)
            .then(data => {
                if(!!data && data.length === 1) {
                    const { 
                        taxStatusCode,  //부가세명
                        percentageRate, //세율
                        taxRateId,      //부가세계정ID (이게 key 인듯)
                        taxRateCode,    //부가세계정
                        taxAcctCode     //부가세계정코드
                    } = data[0];
                    this.value.taxStatusCode = taxStatusCode;
                    this.value.percentageRate = percentageRate;
                    this.value.taxRateId = taxRateId;
                    this.value.taxRateCode = taxRateCode;
                    this.value.taxAcctCode = taxAcctCode;
                    this.value.taxAcctName = taxRateCode; //저장 시 필요한 듯
                    success = true;
                }
            })
            .finally(() => {
                console.log(`CARD 부가세 세팅 ${success ? '성공' : '실패'}`)
            });
        }

    }
}
</script>