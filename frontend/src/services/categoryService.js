import apiClient from "@/services/apiClient.js";

// Get all categories for the current user
export const getCategories = async () => {
  const response = await apiClient.get('/categories')
  return response.data
}

// Create a new category
export const createCategory = async (category) => {
  const response = await apiClient.post('/categories', category)
  return response.data
}

// Delete a category by ID
export const deleteCategoryById = async (id) => {
  const response = await apiClient.delete(`/categories/${id}`)
  return response.data
}

// Update a category by ID
export const updateCategory = async (id, updatedData) => {
  const response = await apiClient.put(`/categories/${id}`, updatedData)
  return response.data
}
