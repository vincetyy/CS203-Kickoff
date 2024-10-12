import axios from 'axios';
import '../utils/axiosSetup'; 

const api = axios.create({
  baseURL: import.meta.env.VITE_DEFAULT_BASE_URL, // Default base URL (can be overridden per request)
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  }
});

api.interceptors.request.use(
  (config) => {
      // Retrieve the token (or other data) from localStorage before each request
      const token = localStorage.getItem('authToken');
      console.log(token);
      
      // If the token exists, add it to the Authorization header
      if (token && token !== 'undefined' && token !== '') {
          config.headers['Authorization'] = `Bearer ${token}`;
      }

      // Return the modified config
      return config;
  },
  (error) => {
      // Handle the error
      return Promise.reject(error);
  }
);

// Add a response interceptor (optional) for handling response/unloading
api.interceptors.response.use(
  (response) => {
      // Any response handling
      return response;
  },
  (error) => {
      // Check for 401 Unauthorized response and clear the token from localStorage
      if (error.response && error.response.status === 401) {
          localStorage.removeItem('authToken'); // Unload token from localStorage on 401
          // Use window.location.href for global navigation
          window.location.href = "/login";
      }

      // Return the error response
      return Promise.reject(error);
  }
);

export default api;