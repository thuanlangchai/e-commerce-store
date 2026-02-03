import { useAuthStore } from '@/store/authStore';
import { User } from 'lucide-react';

export default function Profile() {
  const { user } = useAuthStore();

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Thông tin cá nhân</h1>

      <div className="max-w-2xl">
        <div className="card">
          <div className="flex items-center space-x-6 mb-6">
            <div className="w-24 h-24 bg-primary-100 rounded-full flex items-center justify-center">
              <User className="w-12 h-12 text-primary-600" />
            </div>
            <div>
              <h2 className="text-2xl font-bold">{user?.username || 'Người dùng'}</h2>
              <p className="text-gray-600">{user?.email}</p>
            </div>
          </div>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Tên người dùng</label>
              <div className="input bg-gray-50">{user?.username || '-'}</div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <div className="input bg-gray-50">{user?.email || '-'}</div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
              <div className="input bg-gray-50">{user?.phone || '-'}</div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Vai trò</label>
              <div className="input bg-gray-50">
                {user?.role === 'ADMIN' && 'Quản trị viên'}
                {user?.role === 'SELLER' && 'Người bán'}
                {user?.role === 'USER' && 'Người dùng'}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
