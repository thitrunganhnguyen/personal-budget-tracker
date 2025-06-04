import apiClient from "@/services/apiClient.js";

// Create a new transaction
export const createTransaction = async (transaction) => {
  const response = await apiClient.post('/transactions', transaction);
  return response.data;
};


// Get transactions for month
export const getTransactionsForMonth = async (year, month) => {
  const response = await apiClient.get('/transactions', {
    params: { year, month }
  });
  return response.data;
};

// Get all transactions
export const getAllTransactions = async () => {
  const response = await apiClient.get('/transactions');
  return response.data;
}

// Update a transaction by ID
export const updateTransaction = async (id, transaction) => {
  const response = await apiClient.put(`/transactions/${id}`, transaction);
  return response.data;
};

// Delete a transaction by ID
export const deleteTransaction = async (id) => {
  await apiClient.delete(`/transactions/${id}`);
};

// Get summary for a specific month/year
// Juni 2025: Income: €0.00 | Expenses: €70.00
export const getMonthlyTransactionSummary = async (year, month) => {
  const response = await apiClient.get('/transactions/summary', {
    params: { year, month },
  });
  return response.data;
};
