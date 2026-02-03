package com.minhthuan.web_store.domain.store.order_item;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.order_item.OrderItemMapper;
import com.minhthuan.web_store.domain.store.order_item.OrderItemRepository;
import com.minhthuan.web_store.domain.store.order_item.dto.OrderItemResponse;
import com.minhthuan.web_store.domain.store.order_item.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public ApiResponse<List<OrderItemResponse>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        List<OrderItemResponse> orderItemResponses = orderItemMapper.toOrderItemResponses(orderItems);
        return new ApiResponse<>(orderItemResponses, "Get all order items successfully");
    }
}
