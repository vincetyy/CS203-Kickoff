import axios from 'axios';

// Setup Axios Interceptor to conditionally attach JWT token
axios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');

        // Only attach token if it exists and is valid, skip for public endpoints
        if (token && token !== 'undefined' && token !== '') {
            config.headers['Authorization'] = `Bearer ${token}`;
        } 

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);
