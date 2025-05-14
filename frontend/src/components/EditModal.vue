<template>
<div class="modal-backdrop d-flex align-items-center justify-content-center">
  <div class="modal-content p-4 bg-white rounded shadow" style="max-width: 400px; width: 100%">
    <h5>{{ title }}</h5>
    <input
      :value="modelValue"
      @input="$emit('update:model-value', $event.target.value)"
      class="form-control mb-3"
      />
    <div class="d-flex justify-content-end">
      <button class="btn btn-secondary me-2" @click=" $emit('cancel')">Cancel</button>
      <button class="btn btn-success" @click=" $emit('save')">Save</button>
    </div>
  </div>
</div>
</template>

<script setup>
defineProps(['title', 'modelValue'])
defineEmits(['save', 'cancel', 'update:model-value'])
</script>

<style scoped>

</style>
