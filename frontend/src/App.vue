<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <div id="app">
    <!-- ✅ Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom px-4">
      <RouterLink class="navbar-brand fw-bold me-4" to="/dashboard">💸 BudgetApp</RouterLink>
      <div class="navbar-nav">
        <RouterLink class="nav-link" to="/dashboard">Dashboard</RouterLink>
        <!--
        <RouterLink class="nav-link" to="/categories">Categories</RouterLink>
        <RouterLink class="nav-link" to="/transactions">Transactions</RouterLink>
        -->
      </div>
    </nav>

    <!-- ✅ Page Content -->
    <main class="container py-4">
      <RouterView />
    </main>
  </div>
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

