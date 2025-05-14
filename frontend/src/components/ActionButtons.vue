<template>
  <div class="d-flex gap-2">
    <button class="btn btn-sm btn-outline-primary" @click="$emit('edit')">✏️ Edit</button>
    <button class="btn btn-sm btn-outline-danger" @click="$emit('delete')">🗑️ Delete</button>
  </div>
</template>
<script setup lang="ts">
defineEmits(['edit', 'delete'])
</script>
