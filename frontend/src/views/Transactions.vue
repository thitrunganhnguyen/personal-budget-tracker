<template>
  <div class="container mt-4">
    <h2>Transactions</h2>

    <!-- Summary -->
    <div v-if="summary" class="alert alert-info d-flex justify-content-between align-items-center">
      <div>
        <strong>{{ formatMonth(summary.month) }} {{ summary.year }}</strong>:
        Income: <span class="text-success">€{{ summary.totalIncome.toFixed(2) }}</span> |
        Expenses: <span class="text-danger">€{{ summary.totalExpense.toFixed(2) }}</span>
      </div>
    </div>

    <!-- Toggle for Transaction Summary -->
    <button class="btn btn-secondary mb-3" @click="showSummary = !showSummary">
      {{ showSummary ? 'Hide' : 'Show' }} Transaction Summary
    </button>

    <!-- Transaction Summary Card -->
    <div v-if="showSummary" class="card mb-4">
      <div class="card-header">
        💡 Transaction Summary for {{ formatMonth(summary.month) }} {{ summary.year }}
      </div>
      <div class="card-body p-0">
        <table class="table mb-0">
          <thead class="table-light">
          <tr>
            <th>Category</th>
            <th>Adjusted Budget (€)</th>
            <th>Spent (€)</th>
            <th>Remaining (€)</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="b in budgets" :key="b.categoryName">
            <td>{{ b.categoryName }}</td>
            <td>€{{ b.adjustedBudget.toFixed(2) }}</td>
            <td>€{{ b.spentAmount.toFixed(2) }}</td>
            <td :class="{ 'text-danger': b.remainingBudget < 0 }">
              €{{ b.remainingBudget.toFixed(2) }}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <!-- Add Transaction Card -->
    <div class="card mb-4">
      <div class="card-header">
        ➕ Add New Transaction
      </div>
      <div class="card-body">
        <form @submit.prevent="handleSubmit">
          <div class="row g-3">
            <div class="col-md-2">
              <select v-model="form.type" class="form-select" required>
                <option disabled value="">Select Type</option>
                <option value="INCOME">Income</option>
                <option value="EXPENSE">Expense</option>
              </select>
            </div>
            <div class="col-md-3">
              <select v-model="form.categoryId" class="form-select" required>
                <option disabled value="">Category</option>
                <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                  {{ cat.name }}
                </option>
              </select>
            </div>
            <div class="col-md-2">
              <input v-model="form.date" type="date" class="form-control" required />
            </div>
            <div class="col-md-2">
              <input v-model.number="form.amount" type="number" class="form-control" placeholder="Amount" required />
            </div>
            <div class="col-md-3">
              <input v-model="form.description" class="form-control" placeholder="Description" />
            </div>
          </div>
          <div class="mt-3">
            <button class="btn btn-primary me-2" type="submit">
              {{ isEditing ? 'Update' : 'Add' }}
            </button>
            <button v-if="isEditing" class="btn btn-secondary" @click="resetForm">Cancel</button>
          </div>
        </form>
      </div>
    </div>
    <!-- Toggle for Transactions Table -->
    <button class="btn btn-secondary mb-2" @click="showTransactions = !showTransactions">
      {{ showTransactions ? 'Hide' : 'Show' }} All Transactions
    </button>
    <!-- Transactions Table -->
    <table v-if="showTransactions" class="table table-striped">
      <thead>
        <tr>
          <th>Category</th>
          <th>Date</th>
          <th>Amount</th>
          <th>Description</th>
          <th>Type</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="tx in transactions" :key="tx.id">
          <td>{{ tx.categoryName }}</td>
          <td>{{ tx.date }}</td>
          <td>{{ tx.amount }}€</td>
          <td>{{ tx.description }}</td>
          <td>{{ tx.type }}</td>
          <td>
            <button class="btn btn-sm btn-outline-secondary me-1" @click="editTransaction(tx)">Edit</button>
            <button class="btn btn-sm btn-outline-danger" @click="askDelete(tx)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <ConfirmModal
      v-if="showConfirm"
      :show="true"
      title="Löschen bestätigen"
      message="Möchtest du diesen Eintrag wirklich löschen?"
      @confirm="deleteConfirm"
      @cancel="showConfirm = false"
    />

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import {
  createTransaction,
  updateTransaction,
  getTransactionsForMonth,
  deleteTransaction,
  getMonthlyTransactionSummary
} from '@/services/transactionService.js';
import { getCategories } from '@/services/categoryService.js';
import { getBudgetsForMonth } from "@/services/budgetService.js";
import { formatMonth } from '@/utils/dateFormatter.js';
import ConfirmModal from "@/components/ConfirmModal.vue";

const form = ref({
  description: '',
  amount: null,
  type: 'EXPENSE',
  categoryId: '',
  date: new Date().toISOString().slice(0, 10) // yyyy-MM-dd (new Date().toISOString() -> "2025-05-21T12:34:56.789Z")
});
const isEditing = ref(false);
const editId = ref(null);
const showSummary = ref(false);
const showTransactions = ref(false);
const showConfirm = ref(false);

const transactions = ref([]);
const budgets = ref([]);
const summary = ref(null);
const categories = ref([]);

const transactionsToDelete = ref(null);
const today = new Date();
const year = today.getFullYear();
const month = today.getMonth() + 1;

const loadBudgets = async () => {
  budgets.value = await  getBudgetsForMonth(year, month);
}

const fetchAll = async () => {
  transactions.value = await getTransactionsForMonth(year, month);
  summary.value = await getMonthlyTransactionSummary(year, month);
  categories.value = await getCategories();
};

const handleSubmit = async () => {
  console.log("Submitting form:", form.value);
  if (isEditing.value) {
    await updateTransaction(editId.value, form.value);
  } else {
    await createTransaction(form.value);
  }
  await fetchAll();
  await loadBudgets();
  resetForm();
};

const editTransaction = (tx) => {
  form.value = { ...tx, categoryId: tx.categoryId };
  isEditing.value = true;
  editId.value = tx.id;
};

const resetForm = () => {
  form.value = {
    description: '',
    amount: null,
    type: 'EXPENSE',
    categoryId: '',
    date: new Date().toISOString().slice(0, 10)
  };
  isEditing.value = false;
  editId.value = null;
};

const askDelete = (tx) => {
  transactionsToDelete.value = tx;
  showConfirm.value = true;
}

const deleteConfirm = async () => {
  await deleteTransaction(transactionsToDelete.value.id);
  await fetchAll();
  showConfirm.value = false;
}

onMounted(async () => {
  await fetchAll(); // transactions + summary
  await loadBudgets(); // brings in budget summary per category
});
</script>


<style scoped>

</style>
