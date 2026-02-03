package com.minhthuan.web_store.domain.store.cart_item;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.cart_item.CartItemService;
import com.minhthuan.web_store.domain.store.cart_item.dto.CartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @GetMapping("/user/cart-items")
    public ResponseEntity<ApiResponse<?>> getCartItems() {
        return ResponseEntity.ok(cartItemService.findAllCartItems());
    }

    @PostMapping("/user/create-cart-item")
    public ResponseEntity<ApiResponse<?>> addCartItem(@RequestBody CartItemRequest cartItemRequest) {
        return ResponseEntity.ok(cartItemService.createCartItem(cartItemRequest));
    }

    @PutMapping("/user/update-cart-item/{id}")
    public ResponseEntity<ApiResponse<?>> updateCartItems(@PathVariable Long id, @RequestBody CartItemRequest cartItemRequest) {
        return ResponseEntity.ok(cartItemService.updateCartItem(id, cartItemRequest));
    }

    @DeleteMapping("/user/delete-cart-item/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCartItems(@PathVariable Long id) {
        return ResponseEntity.ok(cartItemService.deleteCartItem(id));
    }
}
