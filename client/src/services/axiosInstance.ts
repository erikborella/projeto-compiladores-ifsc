import axios, { AxiosInstance } from 'axios';

const axiosInstance: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL ?? 'http://localhost:8080',
    timeout: 15000
});

export default axiosInstance;