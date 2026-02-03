import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { cartService } from '@/services/cartService';
import { useCartStore } from '@/store/cartStore';
import { useNavigate } from 'react-router-dom';
import { Trash2, Plus, Minus, ShoppingCart, ArrowRight } from 'lucide-react';
import { useEffect } from 'react';

export default function Cart() {
  const navigate = useNavigate();
  const { items, setItems, removeItem, itemCount } = useCartStore();
  const queryClient = useQueryClient();

  const { data: cartItemsResponse, isLoading } = useQuery({
    queryKey: ['cartItems'],
    queryFn: () => cartService.getCartItems(),
  });

  useEffect(() => {
    if (cartItemsResponse?.data) {
      setItems(cartItemsResponse.data);
    }
  }, [cartItemsResponse, setItems]);

  const updateMutation = useMutation({
    mutationFn: ({ id, quantity }: { id: number; quantity: number }) => {
      const item = items.find((i) => i.id === id);
      return cartService.updateCartItem(id, { productId: item?.productId || 0, quantity });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cartItems'] });
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => cartService.deleteCartItem(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cartItems'] });
    },
  });

  const handleUpdateQuantity = (id: number, currentQuantity: number, delta: number) => {
    const newQuantity = Math.max(1, currentQuantity + delta);
    updateMutation.mutate({ id, quantity: newQuantity });
  };

  const handleRemove = (id: number) => {
    if (confirm('Bạn có chắc muốn xóa sản phẩm này?')) {
      deleteMutation.mutate(id);
    }
  };

  const total = items.reduce((sum, item) => {
    return sum + (item.product?.price || 0) * item.quantity;
  }, 0);

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <p>Đang tải...</p>
      </div>
    );
  }

  if (items.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="text-center py-12">
          <ShoppingCart className="w-24 h-24 text-gray-400 mx-auto mb-4" />
          <h2 className="text-2xl font-bold mb-2">Giỏ hàng trống</h2>
          <p className="text-gray-600 mb-6">Hãy thêm sản phẩm vào giỏ hàng của bạn</p>
          <button onClick={() => navigate('/products')} className="btn btn-primary">
            Mua sắm ngay
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Giỏ hàng của tôi</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Cart Items */}
        <div className="lg:col-span-2 space-y-4">
          {items.map((item) => (
            <div key={item.id} className="card flex items-center space-x-4">
              {item.product?.images && item.product.images[0] ? (
                <img
                  src={item.product.images[0].url_img || item.product.images[0].imageUrl}
                  alt={item.product.name}
                  className="w-24 h-24 object-cover rounded-lg"
                />
              ) : (
                <div className="w-24 h-24 bg-gray-100 rounded-lg flex items-center justify-center">
                  <ShoppingCart className="w-12 h-12 text-gray-400" />
                </div>
              )}

              <div className="flex-1">
                <h3 className="font-semibold text-lg">{item.product?.name || 'Sản phẩm'}</h3>
                <p className="text-gray-600 text-sm mb-2">
                  {item.product?.price.toLocaleString('vi-VN')}₫
                </p>

                <div className="flex items-center space-x-4">
                  <div className="flex items-center space-x-2 border rounded-lg">
                    <button
                      onClick={() => handleUpdateQuantity(item.id, item.quantity, -1)}
                      className="p-1 hover:bg-gray-100"
                      disabled={updateMutation.isPending}
                    >
                      <Minus className="w-4 h-4" />
                    </button>
                    <span className="px-3 py-1 min-w-[3rem] text-center">{item.quantity}</span>
                    <button
                      onClick={() => handleUpdateQuantity(item.id, item.quantity, 1)}
                      className="p-1 hover:bg-gray-100"
                      disabled={updateMutation.isPending}
                    >
                      <Plus className="w-4 h-4" />
                    </button>
                  </div>

                  <button
                    onClick={() => handleRemove(item.id)}
                    className="text-red-600 hover:text-red-700 p-2"
                    disabled={deleteMutation.isPending}
                  >
                    <Trash2 className="w-5 h-5" />
                  </button>
                </div>
              </div>

              <div className="text-right">
                <p className="text-xl font-bold text-primary-600">
                  {((item.product?.price || 0) * item.quantity).toLocaleString('vi-VN')}₫
                </p>
              </div>
            </div>
          ))}
        </div>

        {/* Order Summary */}
        <div className="lg:col-span-1">
          <div className="card sticky top-24">
            <h2 className="text-xl font-bold mb-4">Tóm tắt đơn hàng</h2>
            <div className="space-y-3 mb-4">
              <div className="flex justify-between">
                <span>Tạm tính:</span>
                <span>{total.toLocaleString('vi-VN')}₫</span>
              </div>
              <div className="flex justify-between">
                <span>Phí vận chuyển:</span>
                <span>Miễn phí</span>
              </div>
              <div className="border-t pt-3 flex justify-between text-lg font-bold">
                <span>Tổng cộng:</span>
                <span className="text-primary-600">{total.toLocaleString('vi-VN')}₫</span>
              </div>
            </div>
            <button
              onClick={() => navigate('/checkout')}
              className="w-full btn btn-primary py-3 flex items-center justify-center"
            >
              Thanh toán
              <ArrowRight className="w-5 h-5 ml-2" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
