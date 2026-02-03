package com.minhthuan.web_store.domain.store.order.dto;

import com.minhthuan.web_store.domain.store.order.entity.OrderStatus;
import com.minhthuan.web_store.domain.store.order.entity.PaymentStatus;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private Long addressId;
    private double totalPrice;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDate createAt;
    private java.util.List<com.minhthuan.web_store.domain.store.order_item.dto.OrderItemResponse> items;
}
