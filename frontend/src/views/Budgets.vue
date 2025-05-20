<template>
  <div class="container mt-4">
    <h2>Add / Update Budget</h2>

    <form @submit.prevent="handleSubmit" class="row g-3 mb-4">
      <div class="col-md-4">
        <label class="form-label">Category</label>
        <select v-model="form.categoryId" class="form-select" required>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>
      </div>
      <div class="col-md-2">
        <label class="form-label">Month</label>
        <input type="number" v-model="form.month" class="form-control" min="1" max="12" required />
      </div>
      <div class="col-md-2">
        <label class="form-label">Year</label>
        <input type="number" v-model="form.year" class="form-control" min="2000" required />
      </div>
      <div class="col-md-3">
        <label class="form-label">Initial Budget (€)</label>
        <input type="number" v-model="form.initialBudget" class="form-control" step="0.01" required />
      </div>
      <div class="col-md-1 d-flex align-items-end">
        <button type="submit" class="btn btn-primary W-100">Save</button>
      </div>
    </form>
    <h4>Budgets for {{ form.month }}/{{ form.year }}</h4>
    <table class="table table-bordered">
      <thead class="table-light">
      <tr>
        <th>Category</th>
        <th>Initial Budget</th>
        <th>Leftover from Last Month</th>
        <th>Adjusted Budget</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="budget in budgets" :key="budget.id">
        <td>{{ budget.categoryName }}</td>
        <td>€{{ budget.initialBudget.toFixed(2) }}</td>
        <td>€{{ budget.leftoverFromLastMonth.toFixed(2) }}</td>
        <td>€{{ budget.adjustedBudget.toFixed(2) }}</td>
      </tr>
      </tbody>
    </table>
    <button class="btn btn-secondary mb-3" @click="showAllBudgets = !showAllBudgets">
      {{ showAllBudgets ? 'Hide All Budgets' : 'Show All Budgets' }}
    </button>
    <div v-if="showAllBudgets">
      <h2 class="mt-5">All Budgets</h2>
      <div v-for="key in sortedMonthKeys" :key="key" class="mb-4">
        <h5 class="text-primary">{{ formatMonth(key) }}</h5>
        <table class="table table-sm table-bordered">
          <thead class="table-light">
          <tr>
            <th>Category</th>
            <th>Initial</th>
            <th>Leftover</th>
            <th>Adjusted</th>
            <th>Spent</th>
            <th>Remaining</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="b in groupedBudgets[key]" :key="b.id">
            <td>{{ b.categoryName }}</td>
            <td>{{ b.initialBudget.toFixed(2) }}</td>
            <td>{{ b.leftoverFromLastMonth.toFixed(2) }}</td>
            <td>{{ b.adjustedBudget.toFixed(2) }}</td>
            <td>{{ b.spentAmount.toFixed(2) }}</td>
            <td>{{ b.remainingBudget.toFixed(2) }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

</template>


<script setup>
import {ref, onMounted, computed} from "vue";
import { getCategories } from "@/services/categoryService.js";
import { createOrUpdateBudget, getBudgetsForMonth, getAllBudgets } from "@/services/budgetService.js";

const form = ref({
  categoryId: '',
  year: new Date().getFullYear(),
  month: new Date().getMonth() + 1,
  initialBudget: ''

})
const categories = ref([]);
const budgets = ref([]);
const allBudgets = ref([]);
const groupedBudgets = ref({});

const showAllBudgets = ref(false);

const loadCategories = async () => {
  categories.value = await getCategories();
}

const loadBudgets = async () => {
  budgets.value = await getBudgetsForMonth(form.value.year, form.value.month);
}

const loadAllBudgets = async () => {
  const result = await getAllBudgets();
  const grouped = {};

  for (const b of result) {
    const key = `${b.year}-${String(b.month).padStart(2, '0')}`;

    // If the key does not exist, it initializes it with an empty array []
    if(!grouped[key]) grouped[key] = [];
    grouped[key].push(b);
  }

  allBudgets.value = result;
  groupedBudgets.value = grouped;
}

const handleSubmit = async () => {
  await createOrUpdateBudget(form.value);
  await loadBudgets();
  await loadAllBudgets();
  form.value.initialBudget = '';
  form.value.initialBudget = '';

}

const formatMonth = (key) => {
  const[year, month] = key.split('-');
  const date = new Date(`${year}-${month}-01`);
  return date.toLocaleString("default", { month: "long", year: "numeric" });
}

const sortedMonthKeys = computed(() => {
  return Object.keys(groupedBudgets.value)
    .sort((a,b) => b.localeCompare(a)); // "2025-06" > "2025-05"
})

onMounted(async () => {
  await loadCategories();
  await loadBudgets();
  await loadAllBudgets();
})

</script>

<style scoped>

</style>
