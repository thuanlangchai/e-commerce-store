import api from '@/utils/api';
import { ApiResponse, Order, OrderRequest } from '@/types';

export const orderService = {
  createOrder: async (data: OrderRequest): Promise<ApiResponse<Order>> => {
    const response = await api.post<ApiResponse<Order>>('/user/create-orders', data);
    return response.data;
  },

  getOrders: async (): Promise<ApiResponse<Order[]>> => {
    const response = await api.get<ApiResponse<Order[]>>('/user/orders');
    return response.data;
  },
};
