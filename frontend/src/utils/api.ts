import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios';

const getApiBaseUrl = () => {
  // Ưu tiên 1: Nếu có VITE_API_URL, dùng nó
  const envUrl = (import.meta as any).env?.VITE_API_URL as string | undefined;
  if (envUrl) return envUrl.replace(/\/$/, '');
  
  // Ưu tiên 2: Nếu có VITE_BACKEND_URL, dùng nó
  const backendUrl = (import.meta as any).env?.VITE_BACKEND_URL as string | undefined;
  if (backendUrl) {
    return `${backendUrl.replace(/\/$/, '')}/api/auth`;
  }
  
  // Trong development mode, luôn sử dụng proxy của Vite
  // Proxy sẽ tự động forward request đến backend
  // Điều này hoạt động cả khi truy cập từ localhost và từ máy khác trong mạng
  if (typeof window !== 'undefined') {
    // Luôn sử dụng relative path để proxy hoạt động
    return `${window.location.origin.replace(/\/$/, '')}/api/auth`;
  }
  
  // Fallback
  return 'http://localhost:8080/api/auth';
};

const API_BASE_URL = getApiBaseUrl();

// Create axios instance
const api: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add token
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('accessToken');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      // Token expired, try to refresh or redirect to login
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
