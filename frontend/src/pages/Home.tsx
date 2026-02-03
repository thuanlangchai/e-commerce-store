import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { productService } from '@/services/productService';
import { categoryService } from '@/services/categoryService';
import { ShoppingBag, Star, TrendingUp } from 'lucide-react';

export default function Home() {
  const { data: productsResponse } = useQuery({
    queryKey: ['products'],
    queryFn: () => productService.getAllProducts(),
  });

  const { data: categoriesResponse } = useQuery({
    queryKey: ['categories'],
    queryFn: () => categoryService.getAllCategories(),
  });

  const products = productsResponse?.data || [];
  const categories = categoriesResponse?.data || [];
  const featuredProducts = products.slice(0, 6);
  const newProducts = products.slice(-6);

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Hero Section */}
      <section className="bg-gradient-to-r from-primary-600 to-primary-800 text-white rounded-2xl p-12 mb-12">
        <div className="max-w-3xl">
          <h1 className="text-5xl font-bold mb-4">Chào mừng đến với Web Store</h1>
          <p className="text-xl mb-8 text-primary-100">
            Khám phá hàng nghìn sản phẩm chất lượng với giá cả hợp lý và dịch vụ hỗ trợ tuyệt vời
          </p>
          <Link to="/products" className="inline-block bg-white text-primary-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors">
            Mua sắm ngay
          </Link>
        </div>
      </section>

      {/* Categories */}
      {categories.length > 0 && (
        <section className="mb-12">
          <h2 className="text-3xl font-bold mb-6">Danh mục sản phẩm</h2>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {categories.map((category) => (
              <Link
                key={category.id}
                to="/products"
                className="card hover:shadow-lg transition-shadow text-center"
              >
                <ShoppingBag className="w-12 h-12 text-primary-600 mx-auto mb-2" />
                <h3 className="font-semibold">{category.name}</h3>
              </Link>
            ))}
          </div>
        </section>
      )}

      {/* Featured Products */}
      {featuredProducts.length > 0 && (
        <section className="mb-12">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-3xl font-bold">Sản phẩm nổi bật</h2>
            <Link to="/products" className="text-primary-600 hover:text-primary-700 font-medium">
              Xem tất cả →
            </Link>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {featuredProducts.map((product) => (
              <Link
                key={product.id}
                to={`/products/${product.id}`}
                className="card hover:shadow-xl transition-shadow group"
              >
                <div className="aspect-square bg-gray-100 rounded-lg mb-4 flex items-center justify-center overflow-hidden">
                  {product.images && product.images[0] ? (
                    <img
                      src={product.images[0].url_img || product.images[0].imageUrl}
                      alt={product.name}
                      className="w-full h-full object-cover group-hover:scale-110 transition-transform"
                    />
                  ) : (
                    <ShoppingBag className="w-24 h-24 text-gray-400" />
                  )}
                </div>
                <h3 className="font-semibold text-lg mb-2 group-hover:text-primary-600 transition-colors">
                  {product.name}
                </h3>
                <p className="text-gray-600 text-sm mb-3 line-clamp-2">{product.description}</p>
                <div className="flex items-center justify-between">
                  <span className="text-2xl font-bold text-primary-600">
                    {product.price.toLocaleString('vi-VN')}₫
                  </span>
                  <div className="flex items-center space-x-1">
                    <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                    <span className="text-sm text-gray-600">{product.ratingAverage.toFixed(1)}</span>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        </section>
      )}

      {/* Stats Section */}
      <section className="bg-gray-100 rounded-2xl p-8 mb-12">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
          <div>
            <TrendingUp className="w-12 h-12 text-primary-600 mx-auto mb-4" />
            <h3 className="text-3xl font-bold mb-2">{products.length}+</h3>
            <p className="text-gray-600">Sản phẩm đa dạng</p>
          </div>
          <div>
            <ShoppingBag className="w-12 h-12 text-primary-600 mx-auto mb-4" />
            <h3 className="text-3xl font-bold mb-2">{categories.length}+</h3>
            <p className="text-gray-600">Danh mục</p>
          </div>
          <div>
            <Star className="w-12 h-12 text-primary-600 mx-auto mb-4" />
            <h3 className="text-3xl font-bold mb-2">4.8</h3>
            <p className="text-gray-600">Đánh giá trung bình</p>
          </div>
        </div>
      </section>
    </div>
  );
}
