<template>
  <div class="container mt-5">
    <div class="text-center mb-4">
      <h1 class="display-4">Welcome, <span class="text-primary">{{ username }}</span> 👋</h1>
      <p class="lead">Here’s your financial overview for {{ summary.month }}/{{ summary.year }}</p>
    </div>

    <div v-if="summary" class="row justify-content-center">
      <div class="col-md-4">
        <div class="card shadow-sm mb-3">
          <div class="card-body text-center">
            <h5 class="card-title">💰 Total Income</h5>
            <p class="card-text fs-4 text-success">{{ format(summary.totalIncome) }}</p>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card shadow-sm mb-3">
          <div class="card-body text-center">
            <h5 class="card-title">💸 Total Expense</h5>
            <p class="card-text fs-4 text-danger">{{ format(summary.totalExpense) }}</p>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card shadow-sm mb-3">
          <div class="card-body text-center">
            <h5 class="card-title">📊 Balance</h5>
            <p class="card-text fs-4 fw-bold">{{ format(summary.balance) }}</p>
          </div>
        </div>
      </div>
    </div>

    <div v-else-if="error" class="alert alert-danger text-center">
      {{ error }}
    </div>

    <div v-else class="text-center text-muted">
      Loading summary...
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getMonthlySummary } from "@/services/dashboardService.js";

const username = ref('')
const summary = ref(null)
const error = ref(null)

const format = (value) => {
  return new Intl.NumberFormat('de-DE', {
    style: 'currency',
    currency: 'EUR'
  }).format(value)
}

onMounted(async () => {
  try {
    const user = JSON.parse(localStorage.getItem('user'))
    username.value = user?.username || 'User'
  } catch (e) {
    username.value = 'User'
  }

  try {
    const now = new Date()
    const year = now.getFullYear()
    const month = now.getMonth() + 1 // JavaScript months are 0-based

    summary.value = await getMonthlySummary(year, month)
  } catch (e) {
    console.error('Fehler beim Laden:', e);
    error.value = 'Failed to load summary.'
  }
})
</script>
