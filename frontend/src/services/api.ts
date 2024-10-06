import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  auth: {
    username: 'admin',
    password: 'password'
  },
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  }
});

export default api;