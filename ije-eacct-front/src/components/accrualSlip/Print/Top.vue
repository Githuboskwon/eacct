<template>
    <div>
        <div class="table-a">
            <div class="table-name">
                <h3 class="ico_table_name">문서정보</h3>
            </div>
            <table class="table">
                <colgroup>
                    <col width="10%">
                    <col width="20%">
                    <col width="10%">
                    <col width="20%">
                    <col width="10%">
                    <col width="20%">
                </colgroup>
                <tbody>
                    <tr>
                        <th>문서유형</th>
                        <td>{{form.docType}}</td>
                        <th>문서번호</th>
                        <td>{{form.slipNo}}</td>
                        <th>문서상태</th>
                        <td>{{slipNo ? form.apprHeader[0].status : form.slipStatusName}}</td>
                    </tr>
                    <tr>
                        <th>제목</th>
                        <td colspan="5">{{ form.docTitleNm }}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="table-b">
            <div class="table-name">
                <h3 class="ico_table_name">결재선</h3>
            </div>
            <table class="table">
                <colgroup>
                    <col width="3%">
                    <col width="7%">
                    <col width="20%">
                    <col width="20%">
                    <col width="8%">
                    <col width="12%">
                    <col width="30%">
                </colgroup>
                <thead>
                <tr>
                    <th class="no-radius">NO</th>
                    <th>결재유형</th>
                    <th>결재자</th>
                    <th>실제결재자</th>
                    <th>결재상태</th>
                    <th>결재일시</th>
                    <th style="border-right: 1px solid #adadad">의견</th>
                </tr>
                </thead>
                <tbody id="tbody">
                <tr v-for="(item, index) in form.apprDetailsData" :key="index">
                    <td style="text-align: center;">{{index+1}}</td>
                    <td style="text-align: center;">{{item.apprType}}</td>
                    <td>{{item.aprverUser}}</td>
                    <td>{{item.aaprverUser}}</td>
                    <td style="text-align: center;">{{item.apprStatus}}</td>
                    <td style="text-align: center;">{{item.apprDtm}}</td>
                    <td style="border-right: 1px solid #adadad">{{item.apprDesc}}</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div v-if="refUserId && refUserId.length > 0" class="table-b mt10">
            <div class="table-name">
                <h3 class="ico_table_name">참조</h3>
            </div>
            <el-input :value="refUserId" readonly></el-input>
        </div>
    </div>
</template>

<script>
import { isThisISOWeek } from 'date-fns';


export default {
    props: {
        'value': {
            type: Object,
            required: true
        },
        'apprHeader': {
            type: Object,
            required: false,
            default() {
                return {}
            }
        },
        'apprLine': {
            type: Array,
            required: true
        },
        'refUserId': {
            type: String,
            required: false
        },
        'refList': {
            type: Array,
            required: false
        },
        docType: {
            type: String,
            required: false,
            default: 'SLIP'
        },
        slipNo: {
            type: String,
            required: false,
        }
    },
    data() {
        return {
            form: {
                status: '',//진행단계
                apprDetailsData: [],
                //문서유형
                docType: '',
                //문서번호
                apprNo: '',
                //문서상태
                docStatNm: '',
                //문서제목
                docTitleNm: '',
                //NO
                apprSeq: '',
                //결재유형
                apprType: '',
                //결재자
                aprverUser: '',
                //실제결재자
                aaprverUser: '',
                //결재상태
                apprStatus: '',
                //결재일자
                apprDtm: '',
                //의견
                apprDesc: '',
                apprSubYn: 'Y',
                slipNo: '',
            },
        }
    },
    mounted() {
        
        fetch(`/json/slip-status.json`, { method: 'GET' })
        .then(res => res.json())
        .then(res => {
            if(this.slipNo) {
                this.$http.get(`/api/appr/detail/${this.slipNo}`)
                .then(res => res.data)
                .then(data => {
                    // console.log("status1 " , this.form.status)
                    // console.log("status2 " , data.apprHeader[0].status)
                    // console.log("status3 " , this.$parent.$parent.$refs.mid.value.status)
                    Object.assign(this.form, data, this.$parent.$parent.$refs.mid.value);
                    this.form.docType = '전표';
                    this.form.slipNo = this.form.apprHeader[0].slipNo;
                    this.form.docTitleNm = this.form.apprHeader[0].docTitleNm;
                })
            } else {
                this.form.status = res.data.filter(f => f.key === this.form.slipStatus)[0]?.value;
                this.changeFormTitleEvent(Object.assign(this.form, this.apprHeader));
            }

        })
    },
    methods: {
        changeFormTitleEvent(value) {
            this.form.docType = '전표';
            this.form.docTitleNm = value.trxTypeName + ' / ' + this.$filters.number(value.totAmt || value.enteredAmount) + ' / ' + value.description
        },
        setApprHeader() {
            // console.log('Appr Header', this.apprHeader[0])
            Object.assign(this.form, this.apprHeader[0]);
        },
    },
    watch: {
        apprLine(value) {
            this.form.apprDetailsData = value.map(x => {
                x.apprDtm = x.apprDtm ? this.$moment(x.apprDtm).format('YYYY-MM-DD HH:mm:ss') : undefined
                return x
            })
        },
    }
}
</script>