import api from '@/utils/api';
import { ApiResponse, ProductImage } from '@/types';

export interface ProductImageRequest {
  url_img: string;
  productId: number;
}

export const productImageService = {
  getAllProductImages: async (): Promise<ApiResponse<ProductImage[]>> => {
    const response = await api.get<ApiResponse<ProductImage[]>>('/user/product-images');
    return response.data;
  },

  createProductImage: async (data: ProductImageRequest): Promise<ApiResponse<ProductImage>> => {
    const response = await api.post<ApiResponse<ProductImage>>('/seller/create-product-image', data);
    return response.data;
  },

  updateProductImage: async (id: number, data: ProductImageRequest): Promise<ApiResponse<ProductImage>> => {
    const response = await api.put<ApiResponse<ProductImage>>(`/seller/update-product-image/${id}`, data);
    return response.data;
  },

  deleteProductImage: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`/seller/delete-product-image/${id}`);
    return response.data;
  },
};
