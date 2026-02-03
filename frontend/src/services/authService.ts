import api from '@/utils/api';
import { ApiResponse, LoginRequest, LoginResponse, UserRequest, User, GoogleLoginRequest } from '@/types';

export const authService = {
  login: async (data: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
    const response = await api.post<ApiResponse<LoginResponse>>('/login', data);
    return response.data;
  },

  loginWithGoogle: async (data: GoogleLoginRequest): Promise<ApiResponse<LoginResponse>> => {
    const response = await api.post<ApiResponse<LoginResponse>>('/login/google', data);
    return response.data;
  },

  register: async (data: UserRequest): Promise<ApiResponse<User>> => {
    const response = await api.post<ApiResponse<User>>('/register', data);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await api.post('/logout');
  },
};
