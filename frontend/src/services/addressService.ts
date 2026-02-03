import api from '@/utils/api';
import { ApiResponse, Address, AddressRequest } from '@/types';

export const addressService = {
  getAllAddresses: async (): Promise<ApiResponse<Address[]>> => {
    const response = await api.get<ApiResponse<Address[]>>('/user/address');
    return response.data;
  },

  getAddressById: async (id: number): Promise<ApiResponse<Address>> => {
    const response = await api.get<ApiResponse<Address>>(`/user/address/${id}`);
    return response.data;
  },

  createAddress: async (data: AddressRequest): Promise<ApiResponse<Address>> => {
    const response = await api.post<ApiResponse<Address>>('/user/create-address', data);
    return response.data;
  },

  updateAddress: async (id: number, data: AddressRequest): Promise<ApiResponse<Address>> => {
    const response = await api.put<ApiResponse<Address>>(`/user/update-address/${id}`, data);
    return response.data;
  },

  deleteAddress: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`/admin/delete-address/${id}`);
    return response.data;
  },
};
