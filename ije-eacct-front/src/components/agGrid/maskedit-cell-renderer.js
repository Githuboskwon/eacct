import Vue from 'vue';

// '#': {pattern: /\d/}, 숫자
// 'X': {pattern: /[0-9a-zA-Z]/}, 숫자 + 문자
// 'S': {pattern: /[a-zA-Z]/}, 문자
// 'A': {pattern: /[a-zA-Z]/, transform: v => v.toLocaleUpperCase()}, 문자[대문자]
// 'a': {pattern: /[a-zA-Z]/, transform: v => v.toLocaleLowerCase()}, 문자[소문자]
// '!': {escape: true}

export default Vue.extend({
    template: `
    <div>
     <the-mask ref="box" :mask="mask" class="maskedit" v-model="string" type="text" :masked="true" @keydown.native="keyDown" @keyup.enter.native="apply" @blur.native="apply"/>
    </div>                              
`,
  data() {
    return {
      string: '',
      disable: false,
      showIf: true,
      mask: ''
    }
  },  
  created() {
    
    if(this.params.colDef.cellRendererParams !== undefined){
      
      const param = this.params.colDef.cellRendererParams
      
      if(param.disable !== undefined) this.disable = param.disable

      if(param.mask !== undefined) this.mask = param.mask 
    }
    
    //그리드 셀 focusing 방지
    this.params.colDef.editable = false;
  },
  methods: {
    apply(e) {
      
      var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;

      this.params.node.setDataValue(this.params.column.colId, this.string.replace(regExp, ""));      
    },
    keyDown(e) {
      //좌우측 방향키 눌림시 다음 컬럼 넘어감 방지
      if(e.key === "ArrowLeft" || e.key === "ArrowRight" || e.key === "Home" || e.key === "End"){
        e.stopPropagation()
      }
    }     
       
  }, watch: {
    'value': {
      immediate: true,
      deep: true,
      handler() {
        this.string = this.params.value
      }
    }
  }
});