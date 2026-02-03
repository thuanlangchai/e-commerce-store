// API Response Types
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

// Auth Types
export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
}

export interface GoogleLoginRequest {
  token: string;
}

export interface UserRequest {
  username: string;
  email: string;
  password: string;
  phone: string;
}

export interface User {
  id: number;
  username: string;
  email: string;
  phone: string;
  role: string;
}

// Product Types
export interface Product {
  id: number;
  name: string;
  description: string;
  categoryId: number;
  price: number;
  soldCount: number;
  ratingAverage: number;
  images?: ProductImage[];
}

export interface ProductRequest {
  name: string;
  description: string;
  categoryId: number;
  price: number;
}

export interface ProductImage {
  id: number;
  url_img: string;
  productId: number;
  imageUrl?: string; // Alias for url_img
}

// Category Types
export interface Category {
  id: number;
  name: string;
  description?: string;
}

export interface CategoryRequest {
  name: string;
  description?: string;
}

// Cart Types
export interface Cart {
  id: number;
  userId: number;
  items: CartItem[];
}

export interface CartItem {
  id: number;
  productId: number;
  quantity: number;
  product?: Product;
}

export interface CartItemRequest {
  productId: number;
  quantity: number;
}

// Order Types
export interface OrderItem {
  id: number;
  productId: number;
  quantity: number;
  price: number;
  productName?: string;
  productPrice?: number;
  productImages?: ProductImage[];
  product?: Product;
}

export interface Order {
  id: number;
  userId: number;
  addressId: number;
  orderStatus?: string;
  paymentStatus?: string;
  items: OrderItem[];
  createAt?: string;
}

export interface OrderRequest {
  addressId: number;
  ids: number[];
}

// Address Types
export interface Address {
  id: number;
  userId: number;
  street: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
}

export interface AddressRequest {
  street: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
}

// Review Types
export interface Review {
  id: number;
  productId: number;
  userId: number;
  rating: number;
  comment: string;
  user?: User;
  createdAt: string;
}

export interface ReviewRequest {
  productId: number;
  rating: number;
  comment: string;
}

// Chat Types
export interface Conversation {
  id: number;
  customerId: number;
  staffId?: number;
  status: 'PENDING' | 'ACTIVE' | 'CLOSED';
}

export interface Message {
  id?: number;
  conversationId: number;
  senderId: number;
  message: string;
  type?: 'TEXT' | 'IMAGE' | 'FILE';
  createdAt?: string;
}

export interface MessageRequest {
  conversationId: number;
  senderId: number;
  message: string;
}
