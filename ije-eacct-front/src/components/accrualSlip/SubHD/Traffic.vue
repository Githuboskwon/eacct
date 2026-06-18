<template>
    <div>
        <div class="table-name">
            <h3 class="ico_table_name">자가운전 보조금 대상자</h3>
        </div>
        <table class="table">
            <tbody>
                <tr>
                    <th width="10%">해당여부</th>
                    <td>
                        <el-radio-group v-model="value.traffic.temp2" :disabled="readOnly" @change="setGridDistanceGubun(value.traffic.temp2)">
                            <el-radio label="Y">유</el-radio>
                            <el-radio label="N">무</el-radio>
                        </el-radio-group>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="table-name">
            <h3 class="ico_table_name">교통비 내역</h3>
        </div>
        <table class="table">
            <tbody>
                <tr>
                    <th width="10%">유형</th>
                    <td>
                        <el-radio-group v-model="value.traffic.tripCd" :disabled="readOnly">
                            <el-radio label="C">교통비</el-radio>
                        </el-radio-group>
                    </td>
                </tr>
                <tr>
                    <th width="10%">내용</th>
                    <td>
                        <el-input
                            type="textarea"
                            :rows="10"
                            placeholder="내용을 입력하세요."
                            maxlength="500"
                            show-word-limit
                            v-model="value.traffic.tripObj"
                            :readonly="readOnly">
                        </el-input>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>


export default {
  compatConfig: { MODE: 2 },
    props: ['value', 'readOnly'],
    data() {
        return {
        }
    },
    methods: {
        setGridDistanceGubun(value) {
            
            if(value === 'Y') {
                this.$parent.$parent.rowData.forEach((item, idx) => {

                    const rowNode = this.$parent.$parent.gridApi.getRowNode(idx);
                    
                    if(rowNode.data.distanceGubun === '1') {
                        this.$confirm(`상세내역 ${idx + 1}번째 시내교통비 내역이 존재합니다. 시외로 변경하시겠습니까?`, 'Warning',
                        {
                            distinguishCancelAndClose: true,
                            confirmButtonText: '예',
                            cancelButtonText: '아니오',
                            type: 'warning'
                        })
                        .then((val) => {
                            if(val) {
                                this.$parent.$parent.setGridDataValue(idx, 'distanceGubun', '2');
                                rowNode.setDataValue('distanceGubun', '2');
                                this.$message({ type: 'info', message: '시외로 변경되었습니다.' });
                            }
                        })
                        .catch(() => {
                            this.$message({ type: 'info', message: '취소하였습니다.' });
                        });
                    }
                    
                });
            }
        }
    }
}
</script>