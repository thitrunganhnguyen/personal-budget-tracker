import apiClient from './apiClient';

export const getMonthlySummary = async (year, month) => {
  const response = await apiClient.get('/transactions/summary', {
    params: {year, month}
  });
  return response.data;
}
