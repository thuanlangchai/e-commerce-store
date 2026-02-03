package com.minhthuan.web_store.domain.store.order;

import com.minhthuan.web_store.domain.store.order.dto.OrderRequest;
import com.minhthuan.web_store.domain.store.order.dto.OrderResponse;
import com.minhthuan.web_store.domain.store.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {com.minhthuan.web_store.domain.store.order_item.OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "addressId", target = "address.id")
    Order toOrder(OrderRequest orderRequest);
    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "items")
    OrderResponse toOrderResponse(Order order);
    void updateOrder(@MappingTarget Order order, OrderRequest orderRequest);

}
