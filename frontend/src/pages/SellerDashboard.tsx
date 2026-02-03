import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { productService } from '@/services/productService';
import { categoryService } from '@/services/categoryService';
import { productImageService } from '@/services/productImageService';
import { Product, ProductRequest, Category, CategoryRequest, ProductImage } from '@/types';
import { Plus, Edit2, Trash2, Package, Tag, X, Image as ImageIcon } from 'lucide-react';

export default function SellerDashboard() {
  const [showProductForm, setShowProductForm] = useState(false);
  const [showCategoryForm, setShowCategoryForm] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [editingCategory, setEditingCategory] = useState<Category | null>(null);
  const [activeTab, setActiveTab] = useState<'products' | 'categories'>('products');

  const [productForm, setProductForm] = useState<ProductRequest>({
    name: '',
    description: '',
    categoryId: 0,
    price: 0,
  });
  const [imageUrl, setImageUrl] = useState('');
  const [selectedProductForImage, setSelectedProductForImage] = useState<number | null>(null);

  const [categoryForm, setCategoryForm] = useState<CategoryRequest>({
    name: '',
    description: '',
  });

  const queryClient = useQueryClient();

  const { data: productsResponse } = useQuery({
    queryKey: ['products'],
    queryFn: () => productService.getAllProducts(),
  });

  const { data: categoriesResponse } = useQuery({
    queryKey: ['categories'],
    queryFn: () => categoryService.getAllCategories(),
  });

  const createProductMutation = useMutation({
    mutationFn: (data: ProductRequest) => productService.createProduct(data),
    onSuccess: async (response) => {
      await queryClient.invalidateQueries({ queryKey: ['products'] });
      if (response.data) {
        // Reload products to get product with empty images list
        const updatedProductsResponse = await productService.getAllProducts();
        if (updatedProductsResponse?.data) {
          const newProduct = updatedProductsResponse.data.find((p) => p.id === response.data.id);
          if (newProduct) {
            setEditingProduct(newProduct);
            setShowProductForm(true);
          }
        }
      }
    },
  });

  const updateProductMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: ProductRequest }) =>
      productService.updateProduct(id, data),
    onSuccess: async (response) => {
      await queryClient.invalidateQueries({ queryKey: ['products'] });
      // Reload products to get updated product with images
      const updatedProductsResponse = await productService.getAllProducts();
      if (updatedProductsResponse?.data && editingProduct) {
        const updatedProduct = updatedProductsResponse.data.find((p) => p.id === editingProduct.id);
        if (updatedProduct) {
          setEditingProduct(updatedProduct);
        }
      }
    },
  });

  const deleteProductMutation = useMutation({
    mutationFn: (id: number) => productService.deleteProduct(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['products'] });
    },
  });

  const createCategoryMutation = useMutation({
    mutationFn: (data: CategoryRequest) => categoryService.createCategory(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] });
      resetCategoryForm();
    },
  });

  const updateCategoryMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: CategoryRequest }) =>
      categoryService.updateCategory(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] });
      resetCategoryForm();
    },
  });

  const deleteCategoryMutation = useMutation({
    mutationFn: (id: number) => categoryService.deleteCategory(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] });
    },
  });

  const createImageMutation = useMutation({
    mutationFn: (data: { url_img: string; productId: number }) =>
      productImageService.createProductImage(data),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['products'] });
      setImageUrl('');
      // Reload products to get updated product with new image
      const updatedProductsResponse = await productService.getAllProducts();
      if (updatedProductsResponse?.data && editingProduct) {
        const updatedProduct = updatedProductsResponse.data.find((p) => p.id === editingProduct.id);
        if (updatedProduct) {
          setEditingProduct(updatedProduct);
        }
      }
    },
  });

  const deleteImageMutation = useMutation({
    mutationFn: (id: number) => productImageService.deleteProductImage(id),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['products'] });
      // Reload products to get updated product without deleted image
      const updatedProductsResponse = await productService.getAllProducts();
      if (updatedProductsResponse?.data && editingProduct) {
        const updatedProduct = updatedProductsResponse.data.find((p) => p.id === editingProduct.id);
        if (updatedProduct) {
          setEditingProduct(updatedProduct);
        }
      }
    },
  });

  const products = productsResponse?.data || [];
  const categories = categoriesResponse?.data || [];

  const resetProductForm = () => {
    setProductForm({ name: '', description: '', categoryId: 0, price: 0 });
    setImageUrl('');
    setShowProductForm(false);
    setEditingProduct(null);
    setSelectedProductForImage(null);
  };

  const resetCategoryForm = () => {
    setCategoryForm({ name: '', description: '' });
    setShowCategoryForm(false);
    setEditingCategory(null);
  };

  const handleEditProduct = (product: Product) => {
    setEditingProduct(product);
    setProductForm({
      name: product.name,
      description: product.description,
      categoryId: product.categoryId,
      price: product.price,
    });
    setShowProductForm(true);
  };

  const handleEditCategory = (category: Category) => {
    setEditingCategory(category);
    setCategoryForm({
      name: category.name,
      description: category.description || '',
    });
    setShowCategoryForm(true);
  };

  const handleSubmitProduct = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingProduct) {
      updateProductMutation.mutate({ id: editingProduct.id, data: productForm });
    } else {
      createProductMutation.mutate(productForm);
    }
  };

  const handleSubmitCategory = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingCategory) {
      updateCategoryMutation.mutate({ id: editingCategory.id, data: categoryForm });
    } else {
      createCategoryMutation.mutate(categoryForm);
    }
  };

  const handleDeleteProduct = (id: number) => {
    if (confirm('Bạn có chắc muốn xóa sản phẩm này?')) {
      deleteProductMutation.mutate(id);
    }
  };

  const handleDeleteCategory = (id: number) => {
    if (confirm('Bạn có chắc muốn xóa danh mục này?')) {
      deleteCategoryMutation.mutate(id);
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Seller Dashboard</h1>

      {/* Tabs */}
      <div className="flex space-x-4 mb-6 border-b">
        <button
          onClick={() => setActiveTab('products')}
          className={`pb-4 px-4 font-semibold transition-colors ${
            activeTab === 'products'
              ? 'text-primary-600 border-b-2 border-primary-600'
              : 'text-gray-600 hover:text-gray-900'
          }`}
        >
          <Package className="w-5 h-5 inline mr-2" />
          Sản phẩm
        </button>
        <button
          onClick={() => setActiveTab('categories')}
          className={`pb-4 px-4 font-semibold transition-colors ${
            activeTab === 'categories'
              ? 'text-primary-600 border-b-2 border-primary-600'
              : 'text-gray-600 hover:text-gray-900'
          }`}
        >
          <Tag className="w-5 h-5 inline mr-2" />
          Danh mục
        </button>
      </div>

      {/* Products Tab */}
      {activeTab === 'products' && (
        <div>
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold">Quản lý sản phẩm</h2>
            {!showProductForm && (
              <button onClick={() => setShowProductForm(true)} className="btn btn-primary flex items-center">
                <Plus className="w-4 h-4 mr-2" />
                Thêm sản phẩm
              </button>
            )}
          </div>

          {showProductForm && (
            <div className="card mb-6">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-bold">
                  {editingProduct ? 'Sửa sản phẩm' : 'Thêm sản phẩm mới'}
                </h3>
                <button onClick={resetProductForm} className="text-gray-500 hover:text-gray-700">
                  <X className="w-5 h-5" />
                </button>
              </div>
              <form onSubmit={handleSubmitProduct} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Tên sản phẩm</label>
                  <input
                    type="text"
                    className="input"
                    value={productForm.name}
                    onChange={(e) => setProductForm({ ...productForm, name: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Mô tả</label>
                  <textarea
                    className="input h-24"
                    value={productForm.description}
                    onChange={(e) => setProductForm({ ...productForm, description: e.target.value })}
                    required
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium mb-1">Danh mục</label>
                    <select
                      className="input"
                      value={productForm.categoryId}
                      onChange={(e) => setProductForm({ ...productForm, categoryId: parseInt(e.target.value) })}
                      required
                    >
                      <option value={0}>Chọn danh mục</option>
                      {categories.map((cat) => (
                        <option key={cat.id} value={cat.id}>
                          {cat.name}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-1">Giá (₫)</label>
                    <input
                      type="number"
                      className="input"
                      value={productForm.price || ''}
                      onChange={(e) => {
                        const value = e.target.value;
                        setProductForm({
                          ...productForm,
                          price: value === '' ? 0 : parseFloat(value) || 0,
                        });
                      }}
                      min="0"
                      step="1000"
                      required
                    />
                  </div>
                </div>
                <div className="flex space-x-2">
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={createProductMutation.isPending || updateProductMutation.isPending}
                  >
                    {createProductMutation.isPending || updateProductMutation.isPending
                      ? 'Đang lưu...'
                      : editingProduct
                      ? 'Cập nhật'
                      : 'Thêm sản phẩm'}
                  </button>
                  <button type="button" onClick={resetProductForm} className="btn btn-secondary">
                    Hủy
                  </button>
                </div>
              </form>

              {/* Add Image Section */}
              {editingProduct && (
                <div className="mt-6 pt-6 border-t">
                  <h4 className="font-semibold mb-3">Quản lý hình ảnh</h4>
                  <div className="flex space-x-2 mb-4">
                    <input
                      type="text"
                      className="input flex-1"
                      placeholder="Nhập URL hình ảnh"
                      value={imageUrl}
                      onChange={(e) => setImageUrl(e.target.value)}
                    />
                    <button
                      type="button"
                      onClick={() => {
                        if (imageUrl.trim() && editingProduct) {
                          createImageMutation.mutate({
                            url_img: imageUrl.trim(),
                            productId: editingProduct.id,
                          });
                        }
                      }}
                      className="btn btn-primary"
                      disabled={createImageMutation.isPending || !imageUrl.trim()}
                    >
                      <ImageIcon className="w-4 h-4 mr-2" />
                      Thêm hình
                    </button>
                  </div>
                  <div className="grid grid-cols-3 gap-3">
                    {editingProduct.images?.map((img) => (
                      <div key={img.id} className="relative group">
                        <img
                          src={img.url_img || img.imageUrl}
                          alt="Product"
                          className="w-full h-24 object-cover rounded border"
                        />
                        <button
                          onClick={() => {
                            if (confirm('Xóa hình ảnh này?')) {
                              deleteImageMutation.mutate(img.id);
                            }
                          }}
                          className="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1 opacity-0 group-hover:opacity-100 transition-opacity"
                        >
                          <X className="w-3 h-3" />
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {products.map((product) => (
              <div key={product.id} className="card">
                <div className="flex justify-between items-start mb-3">
                  <div className="flex-1">
                    <h3 className="font-semibold text-lg mb-1">{product.name}</h3>
                    <p className="text-sm text-gray-600 line-clamp-2">{product.description}</p>
                    <p className="text-lg font-bold text-primary-600 mt-2">
                      {product.price.toLocaleString('vi-VN')}₫
                    </p>
                  </div>
                </div>
                <div className="flex space-x-2 mt-4">
                  <button
                    onClick={() => handleEditProduct(product)}
                    className="flex-1 btn btn-secondary text-sm py-2"
                  >
                    <Edit2 className="w-4 h-4 inline mr-1" />
                    Sửa
                  </button>
                  <button
                    onClick={() => handleDeleteProduct(product.id)}
                    className="flex-1 btn btn-danger text-sm py-2"
                    disabled={deleteProductMutation.isPending}
                  >
                    <Trash2 className="w-4 h-4 inline mr-1" />
                    Xóa
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Categories Tab */}
      {activeTab === 'categories' && (
        <div>
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold">Quản lý danh mục</h2>
            {!showCategoryForm && (
              <button onClick={() => setShowCategoryForm(true)} className="btn btn-primary flex items-center">
                <Plus className="w-4 h-4 mr-2" />
                Thêm danh mục
              </button>
            )}
          </div>

          {showCategoryForm && (
            <div className="card mb-6">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-bold">
                  {editingCategory ? 'Sửa danh mục' : 'Thêm danh mục mới'}
                </h3>
                <button onClick={resetCategoryForm} className="text-gray-500 hover:text-gray-700">
                  <X className="w-5 h-5" />
                </button>
              </div>
              <form onSubmit={handleSubmitCategory} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Tên danh mục</label>
                  <input
                    type="text"
                    className="input"
                    value={categoryForm.name}
                    onChange={(e) => setCategoryForm({ ...categoryForm, name: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Mô tả</label>
                  <textarea
                    className="input h-24"
                    value={categoryForm.description}
                    onChange={(e) => setCategoryForm({ ...categoryForm, description: e.target.value })}
                  />
                </div>
                <div className="flex space-x-2">
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={createCategoryMutation.isPending || updateCategoryMutation.isPending}
                  >
                    {createCategoryMutation.isPending || updateCategoryMutation.isPending
                      ? 'Đang lưu...'
                      : editingCategory
                      ? 'Cập nhật'
                      : 'Thêm danh mục'}
                  </button>
                  <button type="button" onClick={resetCategoryForm} className="btn btn-secondary">
                    Hủy
                  </button>
                </div>
              </form>
            </div>
          )}

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {categories.map((category) => (
              <div key={category.id} className="card">
                <h3 className="font-semibold text-lg mb-2">{category.name}</h3>
                <p className="text-sm text-gray-600 mb-4">{category.description || 'Không có mô tả'}</p>
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleEditCategory(category)}
                    className="flex-1 btn btn-secondary text-sm py-2"
                  >
                    <Edit2 className="w-4 h-4 inline mr-1" />
                    Sửa
                  </button>
                  <button
                    onClick={() => handleDeleteCategory(category.id)}
                    className="flex-1 btn btn-danger text-sm py-2"
                    disabled={deleteCategoryMutation.isPending}
                  >
                    <Trash2 className="w-4 h-4 inline mr-1" />
                    Xóa
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
