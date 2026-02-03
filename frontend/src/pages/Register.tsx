import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuthStore } from '@/store/authStore';
import { authService } from '@/services/authService';
import { UserRequest } from '@/types';
import { useMutation } from '@tanstack/react-query';
import { getUserFromToken } from '@/utils/jwt';

export default function Register() {
  const navigate = useNavigate();
  const { login } = useAuthStore();
  const [formData, setFormData] = useState<UserRequest>({
    username: '',
    email: '',
    password: '',
    phone: '',
  });
  const [error, setError] = useState('');

  const registerMutation = useMutation({
    mutationFn: (data: UserRequest) => authService.register(data),
    onSuccess: (response) => {
      if (response.code === 200) {
        // Auto login after registration
        authService
          .login({ email: formData.email, password: formData.password })
          .then((loginResponse) => {
            if (loginResponse.data && loginResponse.code === 200) {
              const { accessToken, refreshToken } = loginResponse.data;
              
              // Decode JWT token to get user info including role
              const userInfo = getUserFromToken(accessToken);
              
              if (userInfo) {
                login(accessToken, refreshToken, {
                  id: userInfo.id,
                  username: userInfo.username,
                  email: userInfo.email,
                  phone: formData.phone,
                  role: userInfo.role,
                });
                navigate('/');
              } else {
                navigate('/login');
              }
            }
          })
          .catch(() => {
            navigate('/login');
          });
      }
    },
    onError: (err: any) => {
      setError(err.response?.data?.message || 'Đăng ký thất bại');
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    
    if (formData.password.length < 6) {
      setError('Mật khẩu phải có ít nhất 6 ký tự');
      return;
    }
    
    if (formData.phone.length !== 10) {
      setError('Số điện thoại phải có 10 chữ số');
      return;
    }

    registerMutation.mutate(formData);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-50 to-primary-100 py-12 px-4">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg">
        <div>
          <h2 className="text-center text-3xl font-extrabold text-gray-900">Đăng ký</h2>
          <p className="mt-2 text-center text-sm text-gray-600">
            Đã có tài khoản?{' '}
            <Link to="/login" className="font-medium text-primary-600 hover:text-primary-500">
              Đăng nhập ngay
            </Link>
          </p>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
              {error}
            </div>
          )}
          <div className="space-y-4">
            <div>
              <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-1">
                Tên người dùng
              </label>
              <input
                id="username"
                name="username"
                type="text"
                required
                className="input"
                placeholder="Nhập tên người dùng"
                value={formData.username}
                onChange={(e) => setFormData({ ...formData, username: e.target.value })}
              />
            </div>
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                Email
              </label>
              <input
                id="email"
                name="email"
                type="email"
                required
                className="input"
                placeholder="Nhập email của bạn"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              />
            </div>
            <div>
              <label htmlFor="phone" className="block text-sm font-medium text-gray-700 mb-1">
                Số điện thoại
              </label>
              <input
                id="phone"
                name="phone"
                type="tel"
                required
                maxLength={10}
                className="input"
                placeholder="Nhập số điện thoại (10 chữ số)"
                value={formData.phone}
                onChange={(e) => setFormData({ ...formData, phone: e.target.value.replace(/\D/g, '').slice(0, 10) })}
              />
            </div>
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
                Mật khẩu
              </label>
              <input
                id="password"
                name="password"
                type="password"
                required
                className="input"
                placeholder="Nhập mật khẩu (ít nhất 6 ký tự)"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              disabled={registerMutation.isPending}
              className="w-full btn btn-primary py-3 text-lg"
            >
              {registerMutation.isPending ? 'Đang đăng ký...' : 'Đăng ký'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
