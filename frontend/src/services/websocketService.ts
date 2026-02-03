import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { MessageRequest, Message } from '@/types';

const getWsUrl = () => {
  // Ưu tiên 1: Nếu có VITE_WS_URL, dùng nó
  const envUrl = (import.meta as any).env?.VITE_WS_URL as string | undefined;
  if (envUrl) return envUrl;
  
  // Ưu tiên 2: Nếu có VITE_BACKEND_URL, dùng nó
  const backendUrl = (import.meta as any).env?.VITE_BACKEND_URL as string | undefined;
  if (backendUrl) {
    const wsUrl = backendUrl.replace(/^http/, 'ws').replace(/\/$/, '');
    return `${wsUrl}/api/auth/ws/chat`;
  }
  
  // Trong development mode, sử dụng proxy của Vite
  // Proxy sẽ tự động forward WebSocket connection đến backend
  if (typeof window !== 'undefined') {
    const origin = window.location.origin.replace(/\/$/, '');
    return origin.replace(/^http/, 'ws') + '/api/auth/ws/chat';
  }
  
  // Fallback
  return 'http://localhost:8080/api/auth/ws/chat';
};

class WebSocketService {
  private client: Client | null = null;
  private isConnected: boolean = false;
  private subscriptions: Map<string, (message: Message) => void> = new Map();

  connect(callback?: () => void) {
    if (this.isConnected) {
      callback?.();
      return;
    }

    const socket = new SockJS(getWsUrl());
    this.client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        this.isConnected = true;
        console.log('WebSocket connected');
        callback?.();
      },
      onDisconnect: () => {
        this.isConnected = false;
        console.log('WebSocket disconnected');
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
      },
    });

    this.client.activate();
  }

  disconnect() {
    if (this.client) {
      this.subscriptions.forEach((_, destination) => {
        this.unsubscribe(destination);
      });
      this.client.deactivate();
      this.client = null;
      this.isConnected = false;
    }
  }

  sendMessage(messageRequest: MessageRequest) {
    if (this.client && this.isConnected) {
      this.client.publish({
        destination: '/app/support-chat/message',
        body: JSON.stringify(messageRequest),
      });
    } else {
      console.error('WebSocket not connected');
    }
  }

  subscribeToConversation(conversationId: number, callback: (message: Message) => void) {
    const destination = `/topic/conversation.${conversationId}`;
    if (this.client && this.isConnected) {
      const subscription = this.client.subscribe(destination, (message: IMessage) => {
        const data = JSON.parse(message.body) as Message;
        callback(data);
      });
      this.subscriptions.set(destination, callback);
      return () => {
        subscription.unsubscribe();
        this.subscriptions.delete(destination);
      };
    }
    return () => {};
  }

  subscribeToSupportWaiting(callback: (message: Message) => void) {
    const destination = '/topic/support-waiting';
    if (this.client && this.isConnected) {
      const subscription = this.client.subscribe(destination, (message: IMessage) => {
        const data = JSON.parse(message.body) as Message;
        callback(data);
      });
      this.subscriptions.set(destination, callback);
      return () => {
        subscription.unsubscribe();
        this.subscriptions.delete(destination);
      };
    }
    return () => {};
  }

  unsubscribe(destination: string) {
    this.subscriptions.delete(destination);
  }

  getConnectionStatus(): boolean {
    return this.isConnected;
  }
}

export const websocketService = new WebSocketService();
