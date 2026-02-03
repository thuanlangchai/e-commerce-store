import { useState, useEffect, useRef } from 'react';
import { useMutation } from '@tanstack/react-query';
import { chatService } from '@/services/chatService';
import { websocketService } from '@/services/websocketService';
import { useAuthStore } from '@/store/authStore';
import { Message, MessageRequest, Conversation } from '@/types';
import { Send, MessageCircle, Loader } from 'lucide-react';

export default function Chat() {
  const { user } = useAuthStore();
  const [conversation, setConversation] = useState<Conversation | null>(null);
  const [messages, setMessages] = useState<Message[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const [isConnected, setIsConnected] = useState(false);
  const [isStarting, setIsStarting] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const startConversationMutation = useMutation({
    mutationFn: () => chatService.startConversation(),
    onSuccess: (response) => {
      if (response.data) {
        setConversation(response.data);
        connectWebSocket(response.data.id);
      }
    },
  });

  const handleStart = () => {
    setIsStarting(true);
    startConversationMutation.mutate(undefined, {
      onSettled: () => setIsStarting(false),
    });
  };

  useEffect(() => {
    return () => {
      websocketService.disconnect();
    };
  }, []);

  // Fetch existing messages when conversation is set
  useEffect(() => {
    if (conversation) {
      chatService
        .getMessages(conversation.id)
        .then((response) => {
          if (response.data) {
            setMessages(response.data);
          }
        })
        .catch((error) => {
          console.error('Error fetching messages:', error);
        });
    }
  }, [conversation?.id]);

  const connectWebSocket = (conversationId: number) => {
    websocketService.connect(() => {
      setIsConnected(true);

      // Subscribe to conversation messages
      websocketService.subscribeToConversation(conversationId, (message: Message) => {
        setMessages((prev) => [...prev, message]);
      });
    });
  };

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newMessage.trim() || !conversation || !user) return;

    const messageRequest: MessageRequest = {
      conversationId: conversation.id,
      senderId: user.id,
      message: newMessage.trim(),
    };

    websocketService.sendMessage(messageRequest);
    setNewMessage('');
  };

  if (startConversationMutation.isPending) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="card text-center">
          <Loader className="w-8 h-8 animate-spin mx-auto mb-4 text-primary-600" />
          <p>Đang kết nối với hỗ trợ...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8 flex items-center">
        <MessageCircle className="w-8 h-8 mr-3" />
        Hỗ trợ trực tuyến
      </h1>

      <div className="max-w-4xl mx-auto">
        <div className="card p-0 flex flex-col" style={{ height: '600px' }}>
          {/* Chat Header */}
          <div className="bg-primary-600 text-white p-4 rounded-t-lg flex items-center justify-between">
            <div>
              <h2 className="text-lg font-semibold">Chat hỗ trợ</h2>
              <p className="text-sm text-primary-100">
                {isConnected ? (
                  <span className="flex items-center">
                    <span className="w-2 h-2 bg-green-400 rounded-full mr-2"></span>
                    Đã kết nối
                  </span>
                ) : (
                  <span className="flex items-center">
                    <span className="w-2 h-2 bg-gray-400 rounded-full mr-2"></span>
                    Chưa kết nối
                  </span>
                )}
              </p>
            </div>
            {!conversation ? (
              <button className="btn btn-primary text-sm" onClick={handleStart} disabled={isStarting}>
                {isStarting ? 'Đang khởi tạo...' : 'Liên hệ CSKH'}
              </button>
            ) : (
              <div className="text-sm">
                {conversation.status === 'PENDING' && (
                  <span className="badge badge-warning">Chờ nhân viên</span>
                )}
                {conversation.status === 'ACTIVE' && (
                  <span className="badge badge-success">Đang chat</span>
                )}
                {conversation.status === 'CLOSED' && (
                  <span className="badge badge-danger">Đã đóng</span>
                )}
              </div>
            )}
          </div>

          {/* Messages Area */}
          <div className="flex-1 overflow-y-auto p-4 space-y-4 bg-gray-50">
              {(!conversation || (conversation.status === 'PENDING' && messages.length === 0)) && (
                <div className="text-center py-8 text-gray-500">
                  <MessageCircle className="w-12 h-12 mx-auto mb-2 text-gray-400" />
                  <p>Chào mừng bạn đến với hỗ trợ trực tuyến!</p>
                  <p className="text-sm mt-2">
                    {!conversation
                      ? 'Bấm "Liên hệ CSKH" để bắt đầu trò chuyện.'
                      : 'Bạn có thể bắt đầu trò chuyện. Nhân viên sẽ phản hồi sớm nhất có thể.'}
                  </p>
                </div>
              )}

            {messages.map((message, index) => {
              const isOwnMessage = message.senderId === user?.id;
              return (
                <div
                  key={index}
                  className={`flex ${isOwnMessage ? 'justify-end' : 'justify-start'}`}
                >
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
            })}
            <div ref={messagesEndRef} />
          </div>

          {/* Message Input */}
          {conversation && conversation.status !== 'CLOSED' && (
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
              {!isConnected && (
                <p className="text-sm text-gray-500 mt-2">Đang kết nối...</p>
              )}
            </form>
          )}

          {conversation && conversation.status === 'CLOSED' && (
            <div className="p-4 border-t bg-gray-50 text-center text-gray-500 rounded-b-lg">
              <p>Cuộc trò chuyện đã được đóng</p>
            </div>
          )}
        </div>

        {/* Info Section */}
        <div className="card mt-4">
          <h3 className="font-semibold mb-2">Thông tin hỗ trợ</h3>
          <ul className="text-sm text-gray-600 space-y-1">
            <li>• Hỗ trợ khách hàng 24/7</li>
            <li>• Phản hồi nhanh chóng</li>
            <li>• Đội ngũ hỗ trợ chuyên nghiệp</li>
          </ul>
        </div>
      </div>
    </div>
  );
}
