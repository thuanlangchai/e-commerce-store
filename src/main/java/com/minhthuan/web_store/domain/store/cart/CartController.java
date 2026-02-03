package com.minhthuan.web_store.domain.store.cart;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/user/cart")
    public ResponseEntity<ApiResponse<?>> getCart() {
        return ResponseEntity.ok(cartService.findCart());
    }

}
