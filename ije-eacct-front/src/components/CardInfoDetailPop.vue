<template>
    <layout style="height: 660px">
        <span slot="header">{{title}}<button class="btn-pop-close delete" aria-label="close"
                                             @click="closeModal"></button></span>
        <div slot="content">
            <div class="inner-box">
                <div class="table-area">
                    <div class="table-a">
                        <table id="basic" class="table">
                            <colgroup>
                                <col width="20%">
                                <col width="80%">
                            </colgroup>
                            <tbody>
                            <tr>
                                <th class="tp">카드사명</th>
                                <td>
                                    {{this.form.cardComNm}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">거래일시</th>
                                <td>
                                    {{this.$moment(this.form.usedDt).format('YYYY-MM-DD HH:mm:ss')}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">카드번호</th>
                                <td>
                                    {{this.form.crdNo}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">승인번호</th>
                                <td>
                                    {{this.form.apprNo}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">소지자명</th>
                                <td>
                                    {{this.form.userNm}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">판매금액</th>
                                <td>
                                    {{$numeral(this.form.originAmt).format('0,0')}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">부가가치세</th>
                                <td>
                                    {{$numeral(this.form.surtax).format('0,0')}}
                                </td>
                            </tr>

                            <tr>
                                <th class="tp">봉사료</th>
                                <td>
                                    {{$numeral(this.form.serviceCharge).format('0,0')}}
                                </td>
                            </tr>
                            
                            <tr>
                                <th class="tp">합계</th>
                                <td>
                                    {{$numeral(this.form.usedAmt).format('0,0')}}
                                </td>
                            </tr>
                            
                            <tr>
                                <th class="tp">가맹점명</th>
                                <td>
                                    {{this.form.storeNm}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">가맹점주소</th>
                                <td>
                                    {{this.form.storeAddr}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">업종</th>
                                <td>
                                    {{this.form.storeCd}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">사업자번호</th>
                                <td>
                                    {{this.form.irsNo}}
                                </td>
                            </tr>
                            <tr>
                                <th class="tp">대표자명</th>
                                <td>
                                    {{this.form.storeOwner}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </layout>
</template>

<script>
    import Layout from '@/components/ModalSlot.vue'
    import mixin from '@/mixin'
    import mixinSlip from '@/mixin/slip'

    import DhxCalendar from '@/components/DhxCalendar.vue'
    import _ from 'lodash'

    export default {
  compatConfig: { MODE: 2 },
        name: 'CardMndPop',
        props: {
            apprNo: {
                type: String,
                required: true
            },
            crdNo: {
                type: String,
                required: true
            }
        },
        mixins: [mixin, mixinSlip],
        components: {Layout, DhxCalendar},
        data() {
            return {
                title: '법인카드 사용정보',
                form: {
                    crdNo: '',
                    cdlgId: '',
                    cdlgNm: '',
                    nomnId: '',
                    nomnNm: '',
                    cdlgSeq: '',
                    cdlgStrDt: this.$moment().format('YYYY-MM-DD'),
                    cdlgEndDt: this.$moment().format('YYYY-MM-DD')
                },
                cardList: [],
            }
        },
        methods:{
            closeModal() {
                this.$parent.close();
            },
            getcardInfo(apprNo) {
                this.$http.get(`/api/card/use/info/${apprNo}`).then(response => {
                    if(response.data) {
                        this.form.cardComNm = response.data[0].cardComNm; //카드사명
                        this.form.crdNo = response.data[0].cardNo; //카드번호
                        this.form.usedDt = response.data[0].usedDt; //거래일시
                        this.form.apprNo = response.data[0].apprNo; //승인번호
                        this.form.usedAmt = response.data[0].usedAmt // 합계
                        this.form.userNm = response.data[0].userNm // 소지자명
                        this.form.originAmt = response.data[0].originAmt //판매금액
                        this.form.surtax = response.data[0].surtax //부가가치세
                        this.form.serviceCharge = response.data[0].serviceCharge //봉사료
                        this.form.storeNm = response.data[0].storeNm //가맹점명
                        this.form.storeAddr = response.data[0].storeAddr //가맹점주소
                        this.form.storeCd = response.data[0].storeCd //업종
                        this.form.irsNo = response.data[0].irsNo //사업자번호
                        this.form.storeOwner = response.data[0].storeOwner //대표자명
                    }
                })
                .catch(({message}) => {
                    console.error(message);
                });
            }
        },
        // created(){
        //     console.log(this.crdNo)
        //     console.log(this.form.crdNo)
        // },
        mounted() {
            this.getcardInfo(this.apprNo)
        },
        watch: {

        }
    }
</script>

<style scoped>
    .modal-card {
        height: 600px;
    }
</style>
