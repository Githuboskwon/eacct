<template>
  <div class="modal-card pop_min2" id="draggable">
    <header class="modal-card-head pop-header">
      <button class="icon is-right" @click="move" style="position:absolute;top:18px;right:70px;z-index:30;"><i class="fas fa-window-maximize" style="color:#fff;font-size:22px;padding-top:1px"></i></button>
      <p class="modal-card-title tit">
        <slot name="header">
        </slot>
      </p>
    </header>

    <section class="modal-card-body pop-content">
      <slot name="content"></slot>
    </section>
  </div>
</template>

<script>
export default {
  name: "ModalSlot2",
  data() {
    return {
      isMxMn: true
    }
  },
  methods: {
    move() {
      if(this.isMxMn) {
        $(".modal-card").removeClass('pop_min2')
        $(".modal-card").addClass('pop_max');

        $(".animation-content").removeClass('pop_min2')
        $(".animation-content").addClass('pop_max');
        this.isMxMn = false
      } else {
        $(".modal-card").removeClass('pop_max')
        $(".modal-card").addClass('pop_min2');

        $(".animation-content").removeClass('pop_max')
        $(".animation-content").addClass('pop_min2');
        this.isMxMn = true
      }
    }
  },
  mounted() {
    //pop slots modal move
    $(".animation-content").addClass('pop_min2');
    $(".animation-content").draggable({'cancel':'.modal-card-body'});
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '4');
  },
  destroyed() {
    //ModalSlot open z-index change
    $('.lnb').css('z-index', '7');
  }
}
</script>

<style>
.modal-card-head {background: #0065b3;}
.modal-card-title {color:white; font-size:1.2em;}
.modal-close {top: 80px;}


</style>
