<template>
    <div class="inner-box">
      <el-form>
        <div class="content-name">
          <h2 class="dp-ib">샘플 화면</h2>
        </div>
  
        <el-divider></el-divider>
        <el-button :size="size" type="primary">primary</el-button>
        <el-button :size="size" type="info">info</el-button>
        <el-button :size="size" type="danger">danger</el-button>
        <el-button :size="size" type="warning">warning</el-button>
        <el-button :size="size" type="success">success</el-button>
      </el-form>

      <el-divider></el-divider>
      <el-form size="mini">
            <el-form-item label="버튼 크기">
                <el-select v-model="size">
                    <el-option
                    v-for="(item, idx) in sizes" :key="idx"
                    :label="item.value"
                    :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="버튼 샘플">
                <el-button :size="size" type="primary" icon="el-icon-search">검색</el-button>
                <el-button :size="size" type="success" icon="el-icon-folder-checked">저장</el-button>
                <el-button :size="size" type="info" icon="el-icon-close">취소</el-button>
                <el-button :size="size" type="info" icon="el-icon-plus">추가</el-button>
                <el-button :size="size" type="danger" icon="el-icon-delete">삭제</el-button>
                <el-button :size="size" type="success" icon="el-icon-check">확인</el-button>
                <el-button :size="size" type="primary" icon="el-icon-plus"></el-button>
                <el-button :size="size" type="danger" icon="el-icon-minus"></el-button>
            </el-form-item>
            <el-form-item>
                <el-link href="https://element.eleme.io/#/en-US/component/icon" target="_blank" style="color:red;">아이콘 참조링크</el-link>
            </el-form-item>
      </el-form>
      <el-divider></el-divider>
   
  
      
  </div>
  </template>
  
  <script>
  import Vue from 'vue';

  
  /**
   * * mixins
   */
  import slip from '@/mixin/slip';
  import common from '@/mixin/common';
  
  
  /**
   * * grid components
   */
  import {AgGridVue} from 'ag-grid-vue';
  
  
  export default {
      name:'Sample',
      mixins: [slip, common],
      components:{ AgGridVue},
      data(){
          return {
            sizes: [{
                key: '',
                value: 'default'
            },{
                key: 'large',
                value: 'large'
            },{
                key: 'medium',
                value: 'medium'
            },{
                key: 'small',
                value: 'small'
            },{
                key: 'mini',
                value: 'mini'
            }],
            size: '',
          }
      },
      computed: {
      },
      created() {
         
      },
  
      mounted() {
          
      },
      methods: {
          
      }
  }
  
  </script>