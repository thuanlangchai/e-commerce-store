import api from '@/utils/api';
import { ApiResponse, Category, CategoryRequest } from '@/types';

export const categoryService = {
  getAllCategories: async (): Promise<ApiResponse<Category[]>> => {
    const response = await api.get<ApiResponse<Category[]>>('/user/categories');
    return response.data;
  },

  createCategory: async (data: CategoryRequest): Promise<ApiResponse<Category>> => {
    const response = await api.post<ApiResponse<Category>>('/seller/create-category', data);
    return response.data;
  },

  updateCategory: async (id: number, data: CategoryRequest): Promise<ApiResponse<Category>> => {
    const response = await api.put<ApiResponse<Category>>(`/seller/update-category/${id}`, data);
    return response.data;
  },

  deleteCategory: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`/seller/delete-category/${id}`);
    return response.data;
  },
};
