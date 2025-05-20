import apiClient from "@/services/apiClient.js";

// Create or update a budget
export const createOrUpdateBudget = async (budget) => {
  const response = await apiClient.post('/budgets', budget);
  return response.data;
};

// Get budgets for a given month/year
export const getBudgetsForMonth = async (year, month) => {
  const response = await apiClient.get('/budgets', {
    params: {year, month},
  })
  return response.data;
}

// Get all budgets set for the user
export const getAllBudgets = async () => {
  const response = await apiClient.get("/budgets/all");
  return response.data;
}

