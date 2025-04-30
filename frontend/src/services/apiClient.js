// services/apiClient.js
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // This is where your backend API lives
  headers: {
    'Content-Type': 'application/json',
  },
});

// Before sending a request, automatically attach the token if we have one
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); // 1. Read token from browser storage
  if (token) {
    config.headers.Authorization = `Bearer ${token}`; // 2. If token exists, add it to request headers
  }
  return config; // 3. Return the modified config, so request goes out with token
});

export default apiClient;
