package com.minhthuan.web_store.domain.store.product_image;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.product_image.Product_ImgService;
import com.minhthuan.web_store.domain.store.product_image.dto.Product_ImgRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class Product_ImgController {
    private final Product_ImgService product_imgService;

    @GetMapping("/user/product-images")
    public ResponseEntity<ApiResponse<?>> getAllProductImages() {
        return ResponseEntity.ok(product_imgService.getAllProductImg());
    }

    @PostMapping("/seller/create-product-image")
    public ResponseEntity<ApiResponse<?>> createProductImage(@RequestBody Product_ImgRequest product_imgRequest) {
        return ResponseEntity.ok(product_imgService.createProductImg(product_imgRequest));
    }


    @PutMapping("/seller/update-product-image/{id}")
    public ResponseEntity<ApiResponse<?>> updateProductImage(@RequestBody Product_ImgRequest product_imgRequest, @PathVariable Long id) {
        return ResponseEntity.ok(product_imgService.updateProductImg(id, product_imgRequest));
    }

    @DeleteMapping("/seller/delete-product-image/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProductImage(@PathVariable Long id) {
        return ResponseEntity.ok(product_imgService.deleteProductImg(id));
    }
}
