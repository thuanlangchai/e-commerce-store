import api from '@/utils/api';
import { ApiResponse, Conversation, Message } from '@/types';

export const chatService = {
  startConversation: async (): Promise<ApiResponse<Conversation>> => {
    const response = await api.post<ApiResponse<Conversation>>('/support-chat/start');
    return response.data;
  },

  assignStaff: async (conversationId: number): Promise<ApiResponse<Conversation>> => {
    const response = await api.put<ApiResponse<Conversation>>(`/support-chat/${conversationId}/assign`);
    return response.data;
  },

  getMessages: async (conversationId: number): Promise<ApiResponse<Message[]>> => {
    const response = await api.get<ApiResponse<Message[]>>(`/support-chat/messages/conversation/${conversationId}`);
    return response.data;
  },

  getConversations: async (): Promise<ApiResponse<Conversation[]>> => {
    const response = await api.get<ApiResponse<Conversation[]>>('/support-chat/conversations');
    return response.data;
  },
};
