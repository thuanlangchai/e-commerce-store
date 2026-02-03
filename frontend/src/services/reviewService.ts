import api from '@/utils/api';
import { ApiResponse, Review, ReviewRequest } from '@/types';

export const reviewService = {
  getAllReviews: async (): Promise<ApiResponse<Review[]>> => {
    const response = await api.get<ApiResponse<Review[]>>('/user/reviews');
    return response.data;
  },

  createReview: async (data: ReviewRequest): Promise<ApiResponse<Review>> => {
    const response = await api.post<ApiResponse<Review>>('/user/create-reviews', data);
    return response.data;
  },

  updateReview: async (id: number, data: ReviewRequest): Promise<ApiResponse<Review>> => {
    const response = await api.put<ApiResponse<Review>>(`/user/update-reviews/${id}`, data);
    return response.data;
  },

  deleteReview: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`/user/delete-reviews/${id}`);
    return response.data;
  },
};
