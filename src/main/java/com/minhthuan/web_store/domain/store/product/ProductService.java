package com.minhthuan.web_store.domain.store.product;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.category.CategoryRepository;
import com.minhthuan.web_store.domain.store.category.entity.Category;
import com.minhthuan.web_store.domain.store.product.ProductMapper;
import com.minhthuan.web_store.domain.store.product.ProductRepository;
import com.minhthuan.web_store.domain.store.product.dto.ProductRequest;
import com.minhthuan.web_store.domain.store.product.dto.ProductResponse;
import com.minhthuan.web_store.domain.store.product.entity.Product;
import com.minhthuan.web_store.domain.store.user.UserRepository;
import com.minhthuan.web_store.domain.store.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final JwtUtil jwtUtil;
    private final CategoryRepository categoryRepository;

    public ApiResponse<ProductResponse> createProduct(ProductRequest productRequest) {
        if (productRequest == null)
            throw new CustomException(ErrorCode.NOT_NULL);
        if (productRepository.existsByName(productRequest.getName()))
            throw new CustomException(ErrorCode.EXISTED);

        User user = jwtUtil.getCurrentUser();

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Product product = productMapper.toProduct(productRequest);
        product.setSoldCount(0);
        product.setRatingAverage(0);
        product.setUser(user);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        
        // Return product with empty images list (can add images later)
        ProductResponse productResponse = productMapper.toProductResponse(savedProduct);
        return new ApiResponse<>(productResponse, "Create Successfully");
    }

    public  ApiResponse<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        if (id == null || productRequest == null)
            throw new CustomException(ErrorCode.NOT_NULL);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        productMapper.updateProduct(product, productRequest);
        
        // Update category if changed
        if (!product.getCategory().getId().equals(productRequest.getCategoryId())) {
            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
            product.setCategory(category);
        }
        
        productRepository.save(product);
        
        // Eagerly fetch images
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            product.getImages().forEach(img -> {
                // Access to trigger lazy loading
            });
        }
        
        ProductResponse productResponse = productMapper.toProductResponse(product);
        return new ApiResponse<>(productResponse, "Update Successfully");
    }

    public  ApiResponse<ProductResponse> deleteProduct(Long id) {
        if (id == null)
            throw new  CustomException(ErrorCode.NOT_NULL);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        productRepository.delete(product);
        ProductResponse productResponse = productMapper.toProductResponse(product);
        return new ApiResponse<>(productResponse, "Delete Successfully");
    }

    public ApiResponse<List<ProductResponse>> getAllProduct() {
        List<Product> products = productRepository.findAll();
        // Eagerly fetch images for each product
        products.forEach(product -> {
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                // Initialize lazy collection
                product.getImages().forEach(img -> {
                    // Access to trigger lazy loading
                });
            }
        });
        List<ProductResponse> productResponseList = productMapper.toProductResponseList(products);
        return new ApiResponse<>(productResponseList, "Get All Successfully");
    }


}
