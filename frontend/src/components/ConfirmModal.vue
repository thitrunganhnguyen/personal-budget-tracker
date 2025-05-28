<template>
  <div
    v-if="show"
    class="modal fade show d-block"
    tabindex="-1"
    style="background-color: rgba(0,0,0,0.5);"
    >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ title }}</h5>
            <button type="button" class="btn-close" @click="cancel"></button>
          </div>
        <div class="modal-body">
          <p>{{ message }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="cancel">Abbrechen</button>
          <button type="button" class="btn btn-danger" @click="confirm">Löschen</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  show: Boolean,
  title: String,
  message: String,
});

const emit = defineEmits(['confirm', 'cancel']);

const confirm = () => emit('confirm');
const cancel = () => emit('cancel');

</script>

<style scoped>

</style>
