import api from '@/utils/api';
import { ApiResponse, Product, ProductRequest } from '@/types';

export const productService = {
  getAllProducts: async (): Promise<ApiResponse<Product[]>> => {
    const response = await api.get<ApiResponse<Product[]>>('/user/products');
    return response.data;
  },

  createProduct: async (data: ProductRequest): Promise<ApiResponse<Product>> => {
    const response = await api.post<ApiResponse<Product>>('/seller/create-product', data);
    return response.data;
  },

  updateProduct: async (id: number, data: ProductRequest): Promise<ApiResponse<Product>> => {
    const response = await api.put<ApiResponse<Product>>(`/seller/update-product/${id}`, data);
    return response.data;
  },

  deleteProduct: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`/seller/delete-product/${id}`);
    return response.data;
  },
};
