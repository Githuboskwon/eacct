<template>
  <div class="vue-monthly-picker">
    <div class="month-picker-wrapper" :class="{ 'active visible':showMenu }">
      <div class="month-year-label picker" type="text" autocomplete="off" tabindex="0" @click="openMenu">
          <div class="month-year-display" :disabled="disabled" :class="[inputClass, {'placeholder': !value}]" style="float:left">
             <div class="display-text" :class="'display-text-'+ alignment" :style="[{'text-align': alignment}]">{{displayText}}</div>
             <!-- <span class="vmp-input-append" @click.stop.prevent="clearSelect" v-if="clearOption && value && !disabled">
               <i class="vmp-clear-icon"/>
             </span> -->
          </div>
      </div>
      <div class="text"></div>
      <div class="date-popover" :class="menuClass" :style="menuStyle" tabindex="-1">
        <div class="picker" style="float:none;text-align: center">
          <div class="flexbox">
            <span class="prev" @click="prevYear" :class="{deactive: !canBack}"></span>
            <div>{{year}}</div>
            <span class="next" @click="nextYear" :class="{deactive: !canNext}"></span>
          </div>
          <div class="flexbox monthItem">
            <template v-for="(month, idx) in monthLabels">
              <div class="item active"
                :class="{'selected': isCurrentSelected(-year, idx)}"
                :style="[{'background-color': getBackgroundColor(year, idx)}]"
                v-if="isActive(idx)"
                @click="selectMonth(idx)"
                :key="'on'+idx">{{month}}
              </div>
              <div v-else
                :class="{'selected': isCurrentSelected(year, idx)}"
                :key="'off'+idx"
                class="item deactive">
                {{ month }}
              </div>
            </template>
          </div>
        </div>
      </div>
   </div>
  </div>
</template>

<script>
import moment from 'moment'

export default {
  compatConfig: { MODE: 2 },
  name: 'vue-monthly-picker',
  props: {
    'value': {
      default: null
    },
    'disabled': {
      type: Boolean,
      default: false
    },
    'inputClass': {
      default: 'input'
    },
    'placeHolder': {
      type: String,
      default: ''
    },
    'alignment': {
      type: String,
      default: 'left',
      validator: function (value) {
        // The value must match one of these strings
        return ['left', 'right', 'center'].indexOf(value) !== -1
      }
    },
    'bottomY': {
      type: String,
      default: ''
    },
    'selectedBackgroundColor': {
      type: String,
      default: '#A6A6A6'
    },
    monthLabels: {
      type: Array,
      default: function () {
        return ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
      }
    },
    min: {
      default: null
    },
    max: {
      default: null
    },
    dateFormat: {
      type: String,
      default: 'YYYY-MM'
    },
    clearOption: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      showMenu: false,
      year: moment().format('YYYY'),
      month: moment().format('MM'),
      yearMonth : this.value,
    }
  },
  mounted () {
    this.init()
  },
  watch: {
    value: function (value) {      
      this.setValue(value)
    }
  },
  computed: {
    menuClass () {
      return {
        visible: this.showMenu,
        hidden: !this.showMenu
      }
    },
    menuStyle () {
      return {
        display: this.showMenu ? 'block' : 'none',
        'left': this.alignment === 'right' ? '100%' : this.alignment === 'center' ? '50%' : '',
        'transform': this.alignment === 'right' ? 'translate(-100%,0)' : this.alignment === 'center' ? 'translate(-50%,0)' : '',
        'bottom' : this.bottomY,
      }
    },
    displayText () {
      
      if (this.yearMonth) {        

        let valueMoment = null

        if (typeof this.yearMonth === 'string') {
          
          valueMoment = moment(this.yearMonth)

        } else {
          valueMoment = this.yearMonth
        }
        if (valueMoment && valueMoment.isValid()) {
          return valueMoment.format(this.dateFormat)
        }
      } else {
        return this.placeHolder
      }
    },
    canBack () {
      if (!this.min) return true
      const currentVal = this.internalMomentValue.clone().startOf('year')
      return this.min.isBefore(currentVal)
    },
    canNext () {
      if (!this.max) return true
      const currentVal = this.internalMomentValue.clone().endOf('year')
      return currentVal.isBefore(this.max)
    },
    internalMomentValue () {
      const yrMonth = this.year + this.month
      return yrMonth
    }
  },
  methods: {
    init () {
      document.addEventListener('click', (e) => {
        if (this.$el && !this.$el.contains(e.target)) {
          this.closeMenu()
        }
      }, false)
        this.setValue(this.yearMonth)
    },
    openMenu (event) {
      //그리드상에 오픈되어 있는 년월 선택 피커 close
      const popover = document.getElementsByClassName("date-popover");
      
      _.forEach(popover, (v, index) => {
        if(v.style.display == 'block'){
            v.style.display = 'none'; 
        }
      })
      
      if(this.showMenu){
        this.showMenu = false
      }else{
        this.showMenu = true
      }
      
      //화면을 벗어날 경우       
      if(event.pageY + 250 > $(window).height()){
        this.bottomY = '0'
      }
      // if (!this.disabled) {
      //   this.showMenu = true
      // }
    },
    closeMenu () {
      this.showMenu = false
    },
    prevYear () {
      if (!this.canBack) return
      let newYear = parseInt(this.year) - 1
      this.year = newYear.toString()
    },
    nextYear () {
      if (!this.canNext) return
      let newYear = parseInt(this.year) + 1
      this.year = newYear.toString()
    },
    selectMonth (idx) {      
      
      var v  = (parseInt(idx) + 1).toString()      
      if(v.length < 2){v = '0' + v}

      this.month = v 
      this.selectPicker()
      this.closeMenu()
    }, 
    selectPicker () {      
      this.$emit('input', this.internalMomentValue)
      this.$emit('selected', this.internalMomentValue)
    },
    setValue (value) {
      if (typeof value === 'string') {
        
        var v = value;

        if(v.length == 6) { value = v + '01'}
        
        this.yearMonth = value

        value = moment(value)
      }

      if (value && value.isValid()) {
        this.month = value.format('MM')
        this.year = value.format('YYYY')
      }      
    },
    isActive (idx) {
      let realMonth = idx + 1
      const yrMonth = this.year + '/' + (realMonth < 10 ? '0' + realMonth : realMonth)
      if (this.min && moment(yrMonth, 'YYYY/MM').isBefore(this.min)) {
        return false
      }
      if (this.max && moment(yrMonth, 'YYYY/MM').isAfter(this.max)) {
        return false
      }
      return true
    },
    isCurrentSelected (year, monthIdx) {
      
      if (!this.yearMonth) {
        return false
      }
      let checkValue = this.yearMonth
      if (typeof this.yearMonth === 'string') {
        checkValue = moment(this.yearMonth)
      }
      if (checkValue && checkValue.isValid()) {
        const currentMonth = checkValue.format('MM')
        const currentYear = checkValue.format('YYYY')
        return Number(currentMonth) === Number(monthIdx + 1) && Number(currentYear) === Number(year)
      }
      return false
    },
    getBackgroundColor (year, monthIdx) {
      if (this.isCurrentSelected(year, monthIdx)) {
        return this.selectedBackgroundColor
      }
    },
    clearSelect () {
      this.$emit('input', "")
      this.$emit('selected', "")
    }
  }
}
</script>

<style lang="scss">
$lightgray: #d4d4d4;

.vue-monthly-picker {
  float:none !important;
  .picker {
    .next,
    .prev {
      &:hover {
        cursor: pointer;
      }
    }

    .monthItem {
      .item {
        border-top: 1px solid $lightgray;
        &.active {
          &:hover {
            cursor: pointer;
            background-color: $lightgray;
          }
        }
      }
    }

    .flexbox {
      padding: 0px;
      display: flex;
      flex-wrap: wrap;
      min-width: 250px;
      div {
        flex-grow: 1;
        padding: 15px 0;
      }
      .item {
        flex: 1;
        flex-basis: 25%;
      }
    }
  }

  .placeholder {
    color: #8b8b8b;
  }

  .date-popover {
    overflow-x: hidden;
    overflow-y: hidden;
    outline: none;
    min-width: 250px;
    max-width: 250px;
    width: 100%;
    border-radius: 0 0 .28571429rem .28571429rem;
    box-shadow: 0 2px 3px 0 rgba(34,36,38,.15);
    background: #fff;
    transition: opacity .1s ease;
    // position: fixed;
    position: absolute;
    margin: auto;
    margin-top: 25px;
    z-index: 110;
    border: 1px solid $lightgray;
    font-size: 1rem;
    font-weight: 200;
  }

  .month-picker-wrapper {
    display: contents;
    min-width: 150px;
  }

  .month-year-label {
    outline: none;
    min-width: 135px;
    .vmp-input-append {
      display: none;
    }
    &:hover .vmp-input-append {
      display: block;
    }
  }
  .text {
    position: relative;
    z-index: 2;
  }
  .month-year-display {
    &:hover {
      cursor: pointer;
    }
  }

  .next,
  .prev {
    width: 16%;
    float: left;
    text-indent: -10000px;
    position: relative;

    &:after {
      content: "";
      position: absolute;
      left: 50%;
      top: 50%;
      -webkit-transform: translateX(-50%) translateY(-50%);
      transform: translateX(-50%) translateY(-50%);
      border: 6px solid transparent;
    }
  }
  .next {
    &:after {
      border-left: 10px solid #000;
      margin-left: 5px;
    }
    &.deactive {
      &:hover {
        cursor: default;
      }
      &:after {
        border-left: 10px solid #999999;
      }
    }
  }

  .prev {
    &:after {
      border-right: 10px solid #000;
      margin-left: -5px;
    }
    &.deactive {
      &:hover {
        cursor: default;
      }
      &:after {
        border-right: 10px solid #999999;
      }
    }
  }

  .input {
    -moz-appearance: none;
    -webkit-appearance: none;
    align-items: center;
    border: 1px solid transparent;
    border-radius: 3px;
    box-shadow: none;
    display: inline-flex;
    font-size: 14px;
    height: 25px;
    width : 100%;
    justify-content: flex-start;
    line-height: 1.5;
    padding: 2px calc(.625em - 1px);
    position: relative;
    vertical-align: top;
    background-color: #fff;
    border-color: #dbdbdb;
    color: #363636;
    box-shadow: inset 0 1px 2px hsla(0,0%,4%,.1);
    background: #fff url("../../public/img/ico_datepicker.png") no-repeat center right 10px;
  }
  .deactive {
    color: #999999;
  }

  .selected {
    color: #fff;
    text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
    font-weight: bold;
  }
  .display-text {
    width: 100%;
  }

  .display-text-right {
    margin-right: 20px;
  }

  .vmp-input-append {
    position: absolute;
    top: 0;
    right: 0;
    width: 30px;
    height: 10px;
    
  }
  // .vmp-clear-icon {
  //   display: inline-block;
  //   width: 100%;
  //   height: 100%;
  //   font-style: normal;
  //   color: #555;
  //   text-align: center;
  //   cursor: pointer;
  //   &:before {
  //     display: inline-block;
  //     content: '\2716';
  //     vertical-align: middle;
  //   }
  // }
  
  .month-year-display[disabled] {
    background-color: inherit;
    border-color: #dadada;
    color: #555;
    background: #f0f3f7;
  }
}
</style>
