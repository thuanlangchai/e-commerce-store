package com.minhthuan.web_store.domain.store.product;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.product.ProductService;
import com.minhthuan.web_store.domain.store.product.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/user/products")
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping("/seller/create-product")
    public ResponseEntity<ApiResponse<?>> createProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PutMapping("/seller/update-product/{id}")
    public  ResponseEntity<ApiResponse<?>> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return  ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    @DeleteMapping("/seller/delete-product/{id}")
    public  ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id) {
        return  ResponseEntity.ok(productService.deleteProduct(id));
    }
}
