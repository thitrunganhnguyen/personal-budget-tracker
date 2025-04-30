<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
    <RouterView />
</template>

<style>

html, body, #app {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

#app {
  display: block !important;
  max-width: 100% !important;
}

body {
  margin: 0;
  padding: 0;
  font-family: sans-serif;
  background-color: #f8f9fa;
}

.app-wrapper {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;

  width: 100%;         /* ✅ allow it to stretch */
  box-sizing: border-box; /* ✅ make padding part of the width */
}
</style>

