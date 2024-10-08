import axios from 'axios';

// Add a request interceptor
axios.interceptors.request.use(
    (config) => {
        // Retrieve the token (or other data) from localStorage before each request
        const token = localStorage.getItem('authToken');

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
axios.interceptors.response.use(
    (response) => {
        // Any response handling
        return response;
    },
    (error) => {
        // Check for 401 Unauthorized response and clear the token from localStorage
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('authToken'); // Unload token from localStorage on 401
        }

        // Return the error response
        return Promise.reject(error);
    }
);