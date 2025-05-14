<template>
  <div class="modal-backdrop d-flex align-items-center justify-content-center">
    <div class="modal-content p-4 bg-white rounded shadow" style="max-width: 400px; width: 100%">
      <h5>{{ title }}</h5>
      <p>{{ message }}</p>
      <div class="d-flex justify-content-end">
        <button class="btn btn-secondary me-2" @click=" $emit('cancel')">Cancel</button>
        <button class="btn btn-danger" @click=" $emit('confirm')">Delete</button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps(['title', 'message'])
defineEmits(['confirm', 'cancel'])
</script>

<style scoped>

</style>
