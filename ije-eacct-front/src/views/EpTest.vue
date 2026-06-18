<template>
  <div style="padding: 40px; font-size: 16px;">
    <h2>element-plus v-model 격리 테스트 (로컬 data 바인딩, prop/mixin 없음)</h2>
    <hr/>
    <p>1) el-input:
      <el-input v-model="text" style="width: 300px; display:inline-block" placeholder="여기에 타이핑" />
      → 모델값: [<b>{{ text }}</b>]
    </p>
    <p>2) native input:
      <input v-model="nativeText" style="border:1px solid #999; padding:4px;" placeholder="네이티브" />
      → 모델값: [<b>{{ nativeText }}</b>]
    </p>
    <p>3) el-date-picker:
      <el-date-picker v-model="date" type="date" value-format="YYYY-MM-DD" />
      → 모델값: [<b>{{ date }}</b>]
    </p>
    <p>4) el-select:
      <el-select v-model="sel" style="width:200px; display:inline-block">
        <el-option label="에이" value="a" />
        <el-option label="비" value="b" />
      </el-select>
      → 모델값: [<b>{{ sel }}</b>]
    </p>
  </div>
</template>
<script>
export default {
  name: 'EpTest',
  data() {
    return { text: '', nativeText: '', date: '', sel: '' };
  },
};
</script>
