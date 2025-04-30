import apiClient from './apiClient';

export const register = async (user) => {
  const response = await apiClient.post('/users/create', user);
  return response.data;
};
