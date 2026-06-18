<template>
<form action="">
    <div class="modal-card" style="width: auto">
        <header class="modal-card-head">
            <p class="modal-card-title">{{title}}</p>
        </header>
        <section class="modal-card-body">
            <p>{{msg}}</p>
        </section>
    </div>
</form>
</template>

<script>
export default {
  compatConfig: { MODE: 2 },
  name: 'ModalForm',
  props: [
    'title',
    'msg',
  ],
};
</script>

<style>
</style>
