import { useState, useEffect } from 'react';
import { useMutation, useQuery } from '@tanstack/react-query';
import { chatService } from '@/services/chatService';
import { websocketService } from '@/services/websocketService';
import { useAuthStore } from '@/store/authStore';
import { Message, MessageRequest, Conversation } from '@/types';
import { Send, MessageCircle, User, CheckCircle, Clock } from 'lucide-react';

export default function SupportDashboard() {
  const { user } = useAuthStore();
  const [conversations, setConversations] = useState<Map<number, Conversation>>(new Map());
  const [activeConversation, setActiveConversation] = useState<Conversation | null>(null);
  const [messages, setMessages] = useState<Map<number, Message[]>>(new Map());
  const [newMessage, setNewMessage] = useState('');
  const [isConnected, setIsConnected] = useState(false);
  const { data: conversationData, refetch } = useQuery({
    queryKey: ['support-conversations'],
    queryFn: () => chatService.getConversations(),
  });

  const assignMutation = useMutation({
    mutationFn: (conversationId: number) => chatService.assignStaff(conversationId),
    onSuccess: (response, conversationId) => {
      if (response.data) {
        setConversations((prev) => {
          const newMap = new Map(prev);
          newMap.set(conversationId, response.data);
          return newMap;
        });
        if (activeConversation?.id === conversationId) {
          setActiveConversation(response.data);
        }
      }
    },
  });

  // Load conversations when data changes
  useEffect(() => {
    if (conversationData?.data) {
      const map = new Map<number, Conversation>();
      conversationData.data.forEach((c) => map.set(c.id, c));
      setConversations(map);
    }
  }, [conversationData]);

  useEffect(() => {
    // Connect to WebSocket
    websocketService.connect(() => {
      setIsConnected(true);

      // Subscribe to support-waiting topic to receive new conversation notifications
      websocketService.subscribeToSupportWaiting(() => {
        refetch();
      });
    });

    return () => {
      websocketService.disconnect();
    };
  }, [refetch]);

  useEffect(() => {
    if (activeConversation) {
      // Fetch existing messages when selecting conversation
      chatService.getMessages(activeConversation.id).then((response) => {
        if (response.data) {
          setMessages((prev) => {
            const newMap = new Map(prev);
            newMap.set(activeConversation.id, response.data);
            return newMap;
          });
        }
      });

      // Subscribe to conversation messages
      const unsubscribe = websocketService.subscribeToConversation(activeConversation.id, (message: Message) => {
        setMessages((prev) => {
          const newMap = new Map(prev);
          const currentMessages = newMap.get(activeConversation.id) || [];
          newMap.set(activeConversation.id, [...currentMessages, message]);
          return newMap;
        });
      });

      return () => {
        unsubscribe();
      };
    }
  }, [activeConversation]);

  const handleAssignConversation = (conversationId: number) => {
    assignMutation.mutate(conversationId);
  };

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newMessage.trim() || !activeConversation || !user) return;

    const messageRequest: MessageRequest = {
      conversationId: activeConversation.id,
      senderId: user.id,
      message: newMessage.trim(),
    };

    websocketService.sendMessage(messageRequest);
    setNewMessage('');
  };

  // Mock data - In real app, you'd fetch this from an API
  // You might need to add a GET endpoint to fetch all conversations
  const pendingConversations: Conversation[] = Array.from(conversations.values()).filter(
    (c) => c.status === 'PENDING'
  );
  const activeConversations: Conversation[] = Array.from(conversations.values()).filter(
    (c) => c.status === 'ACTIVE'
  );

  const activeMessages = activeConversation ? messages.get(activeConversation.id) || [] : [];

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-8">
        <h1 className="text-3xl font-bold flex items-center">
          <MessageCircle className="w-8 h-8 mr-3" />
          Hỗ trợ khách hàng
        </h1>
        <div className="flex items-center space-x-2">
          <div
            className={`w-3 h-3 rounded-full ${isConnected ? 'bg-green-500' : 'bg-gray-400'}`}
          ></div>
          <span className="text-sm text-gray-600">
            {isConnected ? 'Đã kết nối' : 'Đang kết nối...'}
          </span>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6" style={{ height: 'calc(100vh - 200px)' }}>
        {/* Conversations List */}
        <div className="lg:col-span-1 flex flex-col">
          <div className="card flex-1 flex flex-col overflow-hidden">
            <h2 className="text-xl font-bold mb-4">Cuộc trò chuyện</h2>

            {/* Pending Conversations */}
            {pendingConversations.length > 0 && (
              <div className="mb-4">
                <h3 className="text-sm font-semibold text-gray-600 mb-2 flex items-center">
                  <Clock className="w-4 h-4 mr-1" />
                  Chờ xử lý ({pendingConversations.length})
                </h3>
                <div className="space-y-2">
                  {pendingConversations.map((conversation) => (
                    <div
                      key={conversation.id}
                      className="p-3 border rounded-lg cursor-pointer hover:bg-gray-50 transition-colors"
                      onClick={() => {
                        setActiveConversation(conversation);
                        handleAssignConversation(conversation.id);
                      }}
                    >
                      <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-2">
                          <User className="w-5 h-5 text-gray-400" />
                          <div>
                            <p className="font-medium">Khách hàng #{conversation.customerId}</p>
                            <p className="text-xs text-gray-500">Chờ xử lý</p>
                          </div>
                        </div>
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            handleAssignConversation(conversation.id);
                          }}
                          className="btn btn-primary text-xs py-1 px-2"
                          disabled={assignMutation.isPending}
                        >
                          Nhận
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Active Conversations */}
            {activeConversations.length > 0 && (
              <div>
                <h3 className="text-sm font-semibold text-gray-600 mb-2 flex items-center">
                  <CheckCircle className="w-4 h-4 mr-1" />
                  Đang xử lý ({activeConversations.length})
                </h3>
                <div className="space-y-2 overflow-y-auto flex-1">
                  {activeConversations.map((conversation) => (
                    <div
                      key={conversation.id}
                      className={`p-3 border rounded-lg cursor-pointer transition-colors ${
                        activeConversation?.id === conversation.id
                          ? 'bg-primary-50 border-primary-600'
                          : 'hover:bg-gray-50'
                      }`}
                      onClick={() => setActiveConversation(conversation)}
                    >
                      <div className="flex items-center space-x-2">
                        <User className="w-5 h-5 text-gray-400" />
                        <div className="flex-1">
                          <p className="font-medium">Khách hàng #{conversation.customerId}</p>
                          <p className="text-xs text-gray-500">Đang chat</p>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {pendingConversations.length === 0 && activeConversations.length === 0 && (
              <div className="text-center py-8 text-gray-500">
                <MessageCircle className="w-12 h-12 mx-auto mb-2 text-gray-400" />
                <p>Chưa có cuộc trò chuyện nào</p>
              </div>
            )}
          </div>
        </div>

        {/* Chat Area */}
        <div className="lg:col-span-2 flex flex-col">
          {activeConversation ? (
            <div className="card flex-1 flex flex-col p-0">
              {/* Chat Header */}
              <div className="bg-primary-600 text-white p-4 rounded-t-lg">
                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="text-lg font-semibold">Khách hàng #{activeConversation.customerId}</h3>
                    <p className="text-sm text-primary-100">
                      {activeConversation.status === 'PENDING' && 'Đang chờ...'}
                      {activeConversation.status === 'ACTIVE' && 'Đang trò chuyện'}
                      {activeConversation.status === 'CLOSED' && 'Đã đóng'}
                    </p>
                  </div>
                  {activeConversation.status === 'PENDING' && (
                    <button
                      onClick={() => handleAssignConversation(activeConversation.id)}
                      className="btn btn-secondary text-sm"
                      disabled={assignMutation.isPending}
                    >
                      {assignMutation.isPending ? 'Đang xử lý...' : 'Nhận cuộc trò chuyện'}
                    </button>
                  )}
                </div>
              </div>

              {/* Messages */}
              <div className="flex-1 overflow-y-auto p-4 bg-gray-50 space-y-4">
                {activeMessages.length === 0 ? (
                  <div className="text-center py-8 text-gray-500">
                    <MessageCircle className="w-12 h-12 mx-auto mb-2 text-gray-400" />
                    <p>Chưa có tin nhắn nào</p>
                    <p className="text-sm mt-2">Bắt đầu trò chuyện với khách hàng</p>
                  </div>
                ) : (
                  activeMessages.map((message, index) => {
                    const isOwnMessage = message.senderId === user?.id;
                    return (
                      <div key={index} className={`flex ${isOwnMessage ? 'justify-end' : 'justify-start'}`}>
                        <div
                          className={`max-w-xs lg:max-w-md px-4 py-2 rounded-lg ${
                            isOwnMessage
                              ? 'bg-primary-600 text-white'
                              : 'bg-white text-gray-900 border'
                          }`}
                        >
                          <p className="text-sm">{message.message}</p>
                          {message.createdAt && (
                            <p
                              className={`text-xs mt-1 ${
                                isOwnMessage ? 'text-primary-100' : 'text-gray-500'
                              }`}
                            >
                              {new Date(message.createdAt).toLocaleTimeString('vi-VN', {
                                hour: '2-digit',
                                minute: '2-digit',
                              })}
                            </p>
                          )}
                        </div>
                      </div>
                    );
                  })
                )}
              </div>

              {/* Message Input */}
              {activeConversation.status === 'ACTIVE' && (
                <form onSubmit={handleSendMessage} className="p-4 border-t bg-white rounded-b-lg">
                  <div className="flex space-x-2">
                    <input
                      type="text"
                      value={newMessage}
                      onChange={(e) => setNewMessage(e.target.value)}
                      placeholder="Nhập tin nhắn..."
                      className="flex-1 input"
                      disabled={!isConnected}
                    />
                    <button
                      type="submit"
                      disabled={!newMessage.trim() || !isConnected}
                      className="btn btn-primary flex items-center px-6"
                    >
                      <Send className="w-5 h-5" />
                    </button>
                  </div>
                </form>
              )}

              {activeConversation.status === 'CLOSED' && (
                <div className="p-4 border-t bg-gray-50 text-center text-gray-500 rounded-b-lg">
                  <p>Cuộc trò chuyện đã được đóng</p>
                </div>
              )}
            </div>
          ) : (
            <div className="card flex-1 flex items-center justify-center">
              <div className="text-center text-gray-500">
                <MessageCircle className="w-16 h-16 mx-auto mb-4 text-gray-400" />
                <p className="text-lg">Chọn một cuộc trò chuyện để bắt đầu</p>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Info Card */}
      <div className="card mt-6">
        <h3 className="font-semibold mb-2">Hướng dẫn</h3>
        <ul className="text-sm text-gray-600 space-y-1">
          <li>• Nhấn "Nhận" để nhận các cuộc trò chuyện đang chờ</li>
          <li>• Chọn một cuộc trò chuyện từ danh sách để bắt đầu chat</li>
          <li>• Tin nhắn được gửi và nhận theo thời gian thực</li>
        </ul>
      </div>
    </div>
  );
}
