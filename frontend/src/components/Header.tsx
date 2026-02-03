import { Link, useNavigate } from 'react-router-dom';
import { ShoppingCart, User, LogOut, MessageCircle, Home } from 'lucide-react';
import { useAuthStore } from '@/store/authStore';
import { useCartStore } from '@/store/cartStore';
import { authService } from '@/services/authService';
import { useEffect } from 'react';

export default function Header() {
  const { isAuthenticated, user, logout } = useAuthStore();
  const { itemCount } = useCartStore();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await authService.logout();
      logout();
      navigate('/');
    } catch (error) {
      console.error('Logout error:', error);
      logout();
      navigate('/');
    }
  };

  return (
    <header className="bg-white shadow-md sticky top-0 z-50">
      <div className="container mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link to="/" className="flex items-center space-x-2">
            <Home className="w-8 h-8 text-primary-600" />
            <span className="text-2xl font-bold text-gray-900">Web Store</span>
          </Link>

          <nav className="hidden md:flex items-center space-x-6">
            <Link to="/" className="text-gray-700 hover:text-primary-600 transition-colors">
              Trang chủ
            </Link>
            <Link to="/products" className="text-gray-700 hover:text-primary-600 transition-colors">
              Sản phẩm
            </Link>
            {isAuthenticated && user?.role === 'USER' && (
              <>
                <Link to="/chat" className="text-gray-700 hover:text-primary-600 transition-colors">
                  Hỗ trợ
                </Link>
                <Link to="/orders" className="text-gray-700 hover:text-primary-600 transition-colors">
                  Đơn hàng
                </Link>
              </>
            )}
            {isAuthenticated && user?.role === 'SELLER' && (
              <Link to="/seller" className="text-gray-700 hover:text-primary-600 transition-colors">
                Seller Dashboard
              </Link>
            )}
            {isAuthenticated && (user?.role === 'ADMIN' || user?.role === 'SELLER') && (
              <Link to="/support" className="text-gray-700 hover:text-primary-600 transition-colors">
                Support Dashboard
              </Link>
            )}
          </nav>

          <div className="flex items-center space-x-4">
            {isAuthenticated ? (
              <>
                {user?.role === 'USER' && (
                  <Link
                    to="/cart"
                    className="relative p-2 text-gray-700 hover:text-primary-600 transition-colors"
                  >
                    <ShoppingCart className="w-6 h-6" />
                    {itemCount > 0 && (
                      <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                        {itemCount}
                      </span>
                    )}
                  </Link>
                )}
                <Link
                  to="/profile"
                  className="p-2 text-gray-700 hover:text-primary-600 transition-colors"
                >
                  <User className="w-6 h-6" />
                </Link>
                <button
                  onClick={handleLogout}
                  className="p-2 text-gray-700 hover:text-red-600 transition-colors"
                  title="Đăng xuất"
                >
                  <LogOut className="w-6 h-6" />
                </button>
                <span className="hidden md:block text-sm text-gray-600">{user?.username}</span>
              </>
            ) : (
              <>
                <Link to="/login" className="btn btn-secondary">
                  Đăng nhập
                </Link>
                <Link to="/register" className="btn btn-primary">
                  Đăng ký
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </header>
  );
}
