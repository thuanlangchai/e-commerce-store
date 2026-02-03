import { useQuery } from '@tanstack/react-query';
import { orderService } from '@/services/orderService';
import { Package, Clock, CheckCircle, XCircle, Loader } from 'lucide-react';
import { Order } from '@/types';

export default function Orders() {
  const { data, isLoading } = useQuery({
    queryKey: ['orders'],
    queryFn: () => orderService.getOrders(),
  });

  const orders: Order[] = data?.data || [];

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'PENDING':
        return (
          <span className="badge badge-warning flex items-center">
            <Clock className="w-3 h-3 mr-1" />
            Chờ xử lý
          </span>
        );
      case 'PROCESSING':
        return (
          <span className="badge badge-primary flex items-center">
            <Package className="w-3 h-3 mr-1" />
            Đang xử lý
          </span>
        );
      case 'COMPLETED':
        return (
          <span className="badge badge-success flex items-center">
            <CheckCircle className="w-3 h-3 mr-1" />
            Hoàn thành
          </span>
        );
      case 'CANCELLED':
        return (
          <span className="badge badge-danger flex items-center">
            <XCircle className="w-3 h-3 mr-1" />
            Đã hủy
          </span>
        );
      default:
        return <span className="badge">{status}</span>;
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Đơn hàng của tôi</h1>

      {isLoading ? (
        <div className="text-center py-12">
          <Loader className="w-8 h-8 animate-spin mx-auto mb-4 text-primary-600" />
          <p>Đang tải đơn hàng...</p>
        </div>
      ) : orders.length === 0 ? (
        <div className="text-center py-12">
          <Package className="w-24 h-24 text-gray-400 mx-auto mb-4" />
          <h2 className="text-2xl font-bold mb-2">Chưa có đơn hàng nào</h2>
          <p className="text-gray-600">Bạn chưa có đơn hàng nào. Hãy bắt đầu mua sắm!</p>
        </div>
      ) : (
        <div className="space-y-4">
          {orders.map((order) => (
            <div key={order.id} className="card">
              <div className="flex items-center justify-between mb-4">
                <div>
                  <h3 className="text-lg font-semibold">Đơn hàng #{order.id}</h3>
                  <p className="text-sm text-gray-600">
                    Ngày đặt: {order.createAt ? new Date(order.createAt).toLocaleDateString('vi-VN') : '-'}
                  </p>
                </div>
                {getStatusBadge(order.orderStatus || '')}
              </div>

              <div className="space-y-2 mb-4">
                {order.items?.map((item) => (
                  <div key={item.id} className="flex items-center justify-between py-2 border-b">
                    <div className="flex items-center space-x-3">
                      {item.productImages && item.productImages[0] && (
                        <img
                          src={item.productImages[0].url_img || item.productImages[0].imageUrl}
                          alt={item.productName}
                          className="w-16 h-16 object-cover rounded"
                        />
                      )}
                      <div>
                        <p className="font-medium">{item.productName}</p>
                        <p className="text-sm text-gray-600">Số lượng: {item.quantity}</p>
                      </div>
                    </div>
                    <p className="font-semibold">
                      {(item.price || 0).toLocaleString('vi-VN')}₫
                    </p>
                  </div>
                ))}
              </div>

              <div className="flex justify-between items-center pt-4 border-t">
                <span className="text-gray-600">Tổng cộng:</span>
                <span className="text-xl font-bold text-primary-600">
                  {order.totalPrice?.toLocaleString('vi-VN') || '0'}₫
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
