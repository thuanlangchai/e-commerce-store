package com.minhthuan.web_store.domain.store.order_item;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.order_item.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/user/order-items")
    public ResponseEntity<ApiResponse<?>> getOrderItems() {
        return ResponseEntity.ok(orderItemService.getAllOrderItems());
    }

}
