import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { addressService } from '@/services/addressService';
import { orderService } from '@/services/orderService';
import { cartService } from '@/services/cartService';
import { useCartStore } from '@/store/cartStore';
import { AddressRequest } from '@/types';
import { Plus, Check, ArrowLeft } from 'lucide-react';

export default function Checkout() {
  const navigate = useNavigate();
  const { items, clearCart } = useCartStore();
  const [selectedAddressId, setSelectedAddressId] = useState<number | null>(null);
  const [showAddressForm, setShowAddressForm] = useState(false);
  const [newAddress, setNewAddress] = useState<AddressRequest>({
    street: '',
    city: '',
    state: '',
    zipCode: '',
    country: 'Vietnam',
  });

  const queryClient = useQueryClient();

  const { data: addressesResponse } = useQuery({
    queryKey: ['addresses'],
    queryFn: () => addressService.getAllAddresses(),
  });

  const createAddressMutation = useMutation({
    mutationFn: (data: AddressRequest) => addressService.createAddress(data),
    onSuccess: (response) => {
      queryClient.invalidateQueries({ queryKey: ['addresses'] });
      if (response.data) {
        setSelectedAddressId(response.data.id);
        setShowAddressForm(false);
        setNewAddress({
          street: '',
          city: '',
          state: '',
          zipCode: '',
          country: 'Vietnam',
        });
      }
    },
  });

  const createOrderMutation = useMutation({
    mutationFn: (addressId: number) =>
      orderService.createOrder({
        addressId,
        ids: items.map((item) => item.id),
      }),
    onSuccess: () => {
      clearCart();
      queryClient.invalidateQueries({ queryKey: ['cartItems'] });
      navigate('/orders');
      alert('Đặt hàng thành công!');
    },
  });

  const addresses = addressesResponse?.data || [];
  const total = items.reduce((sum, item) => {
    return sum + (item.product?.price || 0) * item.quantity;
  }, 0);

  const handleSubmitOrder = () => {
    if (!selectedAddressId) {
      alert('Vui lòng chọn địa chỉ giao hàng');
      return;
    }
    createOrderMutation.mutate(selectedAddressId);
  };

  const handleSubmitAddress = (e: React.FormEvent) => {
    e.preventDefault();
    createAddressMutation.mutate(newAddress);
  };

  if (items.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8">
        <p>Giỏ hàng trống</p>
        <button onClick={() => navigate('/cart')} className="btn btn-primary mt-4">
          Quay lại giỏ hàng
        </button>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <button
        onClick={() => navigate('/cart')}
        className="mb-4 flex items-center text-gray-600 hover:text-gray-900 transition-colors"
      >
        <ArrowLeft className="w-5 h-5 mr-2" />
        Quay lại giỏ hàng
      </button>

      <h1 className="text-3xl font-bold mb-8">Thanh toán</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Checkout Form */}
        <div className="lg:col-span-2 space-y-6">
          {/* Address Selection */}
          <div className="card">
            <h2 className="text-xl font-bold mb-4">Địa chỉ giao hàng</h2>

            {addresses.length > 0 && (
              <div className="space-y-3 mb-4">
                {addresses.map((address) => (
                  <label
                    key={address.id}
                    className={`flex items-start p-4 border-2 rounded-lg cursor-pointer transition-colors ${
                      selectedAddressId === address.id
                        ? 'border-primary-600 bg-primary-50'
                        : 'border-gray-200 hover:border-gray-300'
                    }`}
                  >
                    <input
                      type="radio"
                      name="address"
                      value={address.id}
                      checked={selectedAddressId === address.id}
                      onChange={() => setSelectedAddressId(address.id)}
                      className="mt-1 mr-3"
                    />
                    <div className="flex-1">
                      <p className="font-semibold">{address.street}</p>
                      <p className="text-gray-600 text-sm">
                        {address.city}, {address.state} {address.zipCode}
                      </p>
                      <p className="text-gray-600 text-sm">{address.country}</p>
                    </div>
                    {selectedAddressId === address.id && (
                      <Check className="w-5 h-5 text-primary-600" />
                    )}
                  </label>
                ))}
              </div>
            )}

            {!showAddressForm ? (
              <button
                onClick={() => setShowAddressForm(true)}
                className="btn btn-secondary flex items-center"
              >
                <Plus className="w-4 h-4 mr-2" />
                Thêm địa chỉ mới
              </button>
            ) : (
              <form onSubmit={handleSubmitAddress} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Địa chỉ</label>
                  <input
                    type="text"
                    className="input"
                    value={newAddress.street}
                    onChange={(e) => setNewAddress({ ...newAddress, street: e.target.value })}
                    required
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-1">Thành phố</label>
                    <input
                      type="text"
                      className="input"
                      value={newAddress.city}
                      onChange={(e) => setNewAddress({ ...newAddress, city: e.target.value })}
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-1">Tỉnh/Thành</label>
                    <input
                      type="text"
                      className="input"
                      value={newAddress.state}
                      onChange={(e) => setNewAddress({ ...newAddress, state: e.target.value })}
                      required
                    />
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Mã bưu điện</label>
                  <input
                    type="text"
                    className="input"
                    value={newAddress.zipCode}
                    onChange={(e) => setNewAddress({ ...newAddress, zipCode: e.target.value })}
                    required
                  />
                </div>
                <div className="flex space-x-2">
                  <button type="submit" className="btn btn-primary" disabled={createAddressMutation.isPending}>
                    {createAddressMutation.isPending ? 'Đang thêm...' : 'Thêm địa chỉ'}
                  </button>
                  <button
                    type="button"
                    onClick={() => {
                      setShowAddressForm(false);
                      setNewAddress({
                        street: '',
                        city: '',
                        state: '',
                        zipCode: '',
                        country: 'Vietnam',
                      });
                    }}
                    className="btn btn-secondary"
                  >
                    Hủy
                  </button>
                </div>
              </form>
            )}
          </div>

          {/* Order Items */}
          <div className="card">
            <h2 className="text-xl font-bold mb-4">Sản phẩm</h2>
            <div className="space-y-3">
              {items.map((item) => (
                <div key={item.id} className="flex items-center justify-between">
                  <div className="flex items-center space-x-3">
                    {item.product?.images && item.product.images[0] ? (
                      <img
                        src={item.product.images[0].url_img || item.product.images[0].imageUrl}
                        alt={item.product.name}
                        className="w-16 h-16 object-cover rounded"
                      />
                    ) : null}
                    <div>
                      <p className="font-medium">{item.product?.name}</p>
                      <p className="text-sm text-gray-600">Số lượng: {item.quantity}</p>
                    </div>
                  </div>
                  <p className="font-semibold">
                    {((item.product?.price || 0) * item.quantity).toLocaleString('vi-VN')}₫
                  </p>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Order Summary */}
        <div className="lg:col-span-1">
          <div className="card sticky top-24">
            <h2 className="text-xl font-bold mb-4">Tóm tắt đơn hàng</h2>
            <div className="space-y-3 mb-6">
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
              onClick={handleSubmitOrder}
              disabled={!selectedAddressId || createOrderMutation.isPending}
              className="w-full btn btn-primary py-3"
            >
              {createOrderMutation.isPending ? 'Đang xử lý...' : 'Đặt hàng'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
