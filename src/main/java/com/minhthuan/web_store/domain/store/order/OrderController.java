package com.minhthuan.web_store.domain.store.order;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.order.OrderService;
import com.minhthuan.web_store.domain.store.order.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/user/create-orders")
    public ResponseEntity<ApiResponse<?>> createOrders(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @GetMapping("/user/orders")
    public ResponseEntity<ApiResponse<?>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }
}
