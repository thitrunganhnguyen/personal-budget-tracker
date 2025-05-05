import apiClient from './apiClient';

export const register = async (user) => {
  const response = await apiClient.post('/users/create', user);
  return response.data;
};

export const login = async (username, password) => {
  const payload = { username, password };
  const response = await apiClient.post('/auth/login', payload);
  return response.data; // contains: token + user
};
