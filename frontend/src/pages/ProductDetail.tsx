import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { productService } from '@/services/productService';
import { reviewService } from '@/services/reviewService';
import { cartService } from '@/services/cartService';
import { useAuthStore } from '@/store/authStore';
import { ReviewRequest } from '@/types';
import { ShoppingCart, Star, Plus, Minus, ArrowLeft, ChevronLeft, ChevronRight } from 'lucide-react';
import { useState } from 'react';

export default function ProductDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const [quantity, setQuantity] = useState(1);
  const [activeImage, setActiveImage] = useState<number>(0);
  const [thumbScroll, setThumbScroll] = useState(0);
  const [reviewForm, setReviewForm] = useState<ReviewRequest>({
    productId: parseInt(id || '0'),
    rating: 5,
    comment: '',
  });

  const queryClient = useQueryClient();

  const { data: productsResponse } = useQuery({
    queryKey: ['products'],
    queryFn: () => productService.getAllProducts(),
  });

  const { data: reviewsResponse } = useQuery({
    queryKey: ['reviews'],
    queryFn: () => reviewService.getAllReviews(),
  });

  const addToCartMutation = useMutation({
    mutationFn: (qty: number) =>
      cartService.addCartItem({
        productId: parseInt(id || '0'),
        quantity: qty,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cart'] });
      alert('Đã thêm vào giỏ hàng!');
    },
  });

  const createReviewMutation = useMutation({
    mutationFn: (data: ReviewRequest) => reviewService.createReview(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reviews'] });
      setReviewForm({ productId: parseInt(id || '0'), rating: 5, comment: '' });
      alert('Đã thêm đánh giá!');
    },
    onError: (error: any) => {
      console.error('Error creating review:', error);
      const errorMessage = error.response?.data?.message || 'Không thể tạo đánh giá. Vui lòng thử lại.';
      alert(errorMessage);
    },
  });

  const product = productsResponse?.data?.find((p) => p.id === parseInt(id || '0'));
  const reviews = reviewsResponse?.data?.filter((r) => r.productId === parseInt(id || '0')) || [];
  const images = product?.images ?? [];
  const hasImages = images.length > 0;
  const maxThumbScroll = Math.max(0, (images.length - 4) * 100);

  if (!product) {
    return (
      <div className="container mx-auto px-4 py-8">
        <p>Không tìm thấy sản phẩm</p>
      </div>
    );
  }

  const handlePrevImage = () => {
    if (images.length === 0) return;
    setActiveImage((prev) => (prev - 1 + images.length) % images.length);
  };

  const handleNextImage = () => {
    if (images.length === 0) return;
    setActiveImage((prev) => (prev + 1) % images.length);
  };

  const handleAddToCart = () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    addToCartMutation.mutate(quantity);
  };

  const handleSubmitReview = (e: React.FormEvent) => {
    e.preventDefault();
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    createReviewMutation.mutate(reviewForm);
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <button
        onClick={() => navigate(-1)}
        className="mb-4 flex items-center text-gray-600 hover:text-gray-900 transition-colors"
      >
        <ArrowLeft className="w-5 h-5 mr-2" />
        Quay lại
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-12">
        {/* Product Images */}
        <div className="bg-white rounded-lg p-4 w-full max-w-[520px] mx-auto">
          {hasImages ? (
            <>
              <div className="relative aspect-square max-h-[480px] bg-gray-50 rounded-lg overflow-hidden flex items-center justify-center mb-4">
                <img
                  src={images[activeImage]?.url_img || images[activeImage]?.imageUrl}
                  alt={product.name}
                  className="w-full h-full max-h-full object-contain"
                />
                {images.length > 1 && (
                  <>
                    <button
                      type="button"
                      onClick={handlePrevImage}
                      className="absolute left-2 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white rounded-full p-2 shadow"
                    >
                      <ChevronLeft className="w-5 h-5" />
                    </button>
                    <button
                      type="button"
                      onClick={handleNextImage}
                      className="absolute right-2 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white rounded-full p-2 shadow"
                    >
                      <ChevronRight className="w-5 h-5" />
                    </button>
                  </>
                )}
              </div>
              {images.length > 1 && (
                <div className="relative">
                  <div className="grid grid-cols-4 gap-2 overflow-hidden" style={{ maxHeight: '5.5rem' }}>
                    {images.map((img, idx) => (
                      <button
                        key={img.id || idx}
                        onClick={() => setActiveImage(idx)}
                        className={`border rounded-lg overflow-hidden h-20 ${activeImage === idx ? 'border-primary-600 ring-2 ring-primary-100' : 'border-gray-200'}`}
                        style={{ transform: `translateX(-${thumbScroll}px)` }}
                      >
                        <img
                          src={img.url_img || img.imageUrl}
                          alt="thumb"
                          className="w-full h-full object-cover"
                        />
                      </button>
                    ))}
                  </div>
                  {images.length > 4 && (
                    <div className="flex justify-between items-center mt-2">
                      <button
                        type="button"
                        className="p-2 rounded border hover:bg-gray-100"
                        onClick={() => setThumbScroll((prev) => Math.max(0, prev - 100))}
                      >
                        <ChevronLeft className="w-4 h-4" />
                      </button>
                      <button
                        type="button"
                        className="p-2 rounded border hover:bg-gray-100"
                        onClick={() => setThumbScroll((prev) => Math.min(maxThumbScroll, prev + 100))}
                      >
                        <ChevronRight className="w-4 h-4" />
                      </button>
                    </div>
                  )}
                </div>
              )}
            </>
          ) : (
            <div className="aspect-square bg-gray-100 flex items-center justify-center rounded-lg">
              <ShoppingCart className="w-48 h-48 text-gray-400" />
            </div>
          )}
        </div>

        {/* Product Info */}
        <div>
          <h1 className="text-4xl font-bold mb-4">{product.name}</h1>
          <div className="flex items-center space-x-4 mb-4">
            <div className="flex items-center">
              <Star className="w-5 h-5 fill-yellow-400 text-yellow-400" />
              <span className="ml-1 text-lg font-semibold">{product.ratingAverage.toFixed(1)}</span>
            </div>
            <span className="text-gray-500">({reviews.length} đánh giá)</span>
            {product.soldCount > 0 && (
              <span className="text-gray-500">Đã bán: {product.soldCount}</span>
            )}
          </div>

          <div className="text-4xl font-bold text-primary-600 mb-6">
            {product.price.toLocaleString('vi-VN')}₫
          </div>

          <p className="text-gray-700 mb-8 leading-relaxed">{product.description}</p>

          {/* Quantity Selector */}
          <div className="mb-6">
            <label className="block text-sm font-medium mb-2">Số lượng</label>
            <div className="flex items-center space-x-4">
              <button
                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                className="p-2 border rounded-lg hover:bg-gray-100"
              >
                <Minus className="w-4 h-4" />
              </button>
              <span className="text-xl font-semibold w-12 text-center">{quantity}</span>
              <button
                onClick={() => setQuantity(quantity + 1)}
                className="p-2 border rounded-lg hover:bg-gray-100"
              >
                <Plus className="w-4 h-4" />
              </button>
            </div>
          </div>

          {/* Add to Cart Button */}
          <button
            onClick={handleAddToCart}
            disabled={addToCartMutation.isPending}
            className="w-full btn btn-primary py-4 text-lg flex items-center justify-center"
          >
            <ShoppingCart className="w-5 h-5 mr-2" />
            {addToCartMutation.isPending ? 'Đang thêm...' : 'Thêm vào giỏ hàng'}
          </button>
        </div>
      </div>

      {/* Reviews Section */}
      <div className="mt-12">
        <h2 className="text-2xl font-bold mb-6">Đánh giá sản phẩm</h2>

        {/* Review Form */}
        {isAuthenticated && (
          <div className="card mb-8">
            <h3 className="text-lg font-semibold mb-4">Viết đánh giá</h3>
            <form onSubmit={handleSubmitReview}>
              <div className="mb-4">
                <label className="block text-sm font-medium mb-2">Đánh giá</label>
                <div className="flex space-x-1">
                  {[1, 2, 3, 4, 5].map((star) => (
                    <button
                      key={star}
                      type="button"
                      onClick={() => setReviewForm({ ...reviewForm, rating: star })}
                    >
                      <Star
                        className={`w-6 h-6 ${
                          star <= reviewForm.rating
                            ? 'fill-yellow-400 text-yellow-400'
                            : 'text-gray-300'
                        }`}
                      />
                    </button>
                  ))}
                </div>
              </div>
              <div className="mb-4">
                <label className="block text-sm font-medium mb-2">Bình luận</label>
                <textarea
                  value={reviewForm.comment}
                  onChange={(e) => setReviewForm({ ...reviewForm, comment: e.target.value })}
                  className="input h-24"
                  placeholder="Chia sẻ trải nghiệm của bạn..."
                  required
                />
              </div>
              <button type="submit" className="btn btn-primary" disabled={createReviewMutation.isPending}>
                {createReviewMutation.isPending ? 'Đang gửi...' : 'Gửi đánh giá'}
              </button>
            </form>
          </div>
        )}

        {/* Reviews List */}
        <div className="space-y-4">
          {reviews.length > 0 ? (
            reviews.map((review) => (
              <div key={review.id} className="card">
                <div className="flex items-start justify-between mb-2">
                  <div>
                    <p className="font-semibold">{review.user?.username || 'Người dùng'}</p>
                    <div className="flex items-center space-x-1">
                      {[1, 2, 3, 4, 5].map((star) => (
                        <Star
                          key={star}
                          className={`w-4 h-4 ${
                            star <= review.rating ? 'fill-yellow-400 text-yellow-400' : 'text-gray-300'
                          }`}
                        />
                      ))}
                    </div>
                  </div>
                  <span className="text-sm text-gray-500">
                    {review.createdAt ? new Date(review.createdAt).toLocaleDateString('vi-VN') : ''}
                  </span>
                </div>
                <p className="text-gray-700">{review.comment}</p>
              </div>
            ))
          ) : (
            <p className="text-gray-500 text-center py-8">Chưa có đánh giá nào</p>
          )}
        </div>
      </div>
    </div>
  );
}
