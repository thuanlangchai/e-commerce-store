import api from '@/utils/api';
import { ApiResponse, Cart, CartItem, CartItemRequest } from '@/types';

export const cartService = {
  getCart: async (): Promise<ApiResponse<Cart>> => {
    const response = await api.get<ApiResponse<Cart>>('/user/cart');
    return response.data;
  },

  getCartItems: async (): Promise<ApiResponse<CartItem[]>> => {
    const response = await api.get<ApiResponse<CartItem[]>>('/user/cart-items');
    return response.data;
  },

  addCartItem: async (data: CartItemRequest): Promise<ApiResponse<CartItem>> => {
    const response = await api.post<ApiResponse<CartItem>>('/user/create-cart-item', data);
    return response.data;
  },

  updateCartItem: async (id: number, data: CartItemRequest): Promise<ApiResponse<CartItem>> => {
    const response = await api.put<ApiResponse<CartItem>>(`/user/update-cart-item/${id}`, data);
    return response.data;
  },

  deleteCartItem: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`/user/delete-cart-item/${id}`);
    return response.data;
  },
};
