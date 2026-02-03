import { create } from 'zustand';
import { User } from '@/types';
import { getToken, setToken, removeToken, setUserId } from '@/utils/auth';
import { getUserFromToken } from '@/utils/jwt';

interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (token: string, refreshToken: string, user: User) => void;
  logout: () => void;
  setUser: (user: User) => void;
}

const loadUserFromStorage = (): User | null => {
  // First try to get user from localStorage
  const userStr = localStorage.getItem('user');
  if (userStr) {
    try {
      return JSON.parse(userStr);
    } catch {
      // If parse fails, try to decode from token
    }
  }
  
  // If no user in storage, try to decode from token
  const token = getToken();
  if (token) {
    const userInfo = getUserFromToken(token);
    if (userInfo) {
      return {
        id: userInfo.id,
        username: userInfo.username,
        email: userInfo.email,
        phone: '',
        role: userInfo.role,
      };
    }
  }
  
  return null;
};

export const useAuthStore = create<AuthState>((set) => ({
  user: loadUserFromStorage(),
  token: getToken(),
  isAuthenticated: !!getToken(),
  login: (token: string, refreshToken: string, user: User) => {
    setToken(token);
    localStorage.setItem('refreshToken', refreshToken);
    setUserId(user.id);
    localStorage.setItem('user', JSON.stringify(user));
    set({
      token,
      user,
      isAuthenticated: true,
    });
  },
  logout: () => {
    removeToken();
    localStorage.removeItem('user');
    set({
      user: null,
      token: null,
      isAuthenticated: false,
    });
  },
  setUser: (user: User) => {
    localStorage.setItem('user', JSON.stringify(user));
    set({ user });
  },
}));
