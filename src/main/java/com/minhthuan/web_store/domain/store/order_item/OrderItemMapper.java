package com.minhthuan.web_store.domain.store.order_item;

import com.minhthuan.web_store.domain.store.order_item.dto.OrderItemResponse;
import com.minhthuan.web_store.domain.store.order_item.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {com.minhthuan.web_store.domain.store.product_image.Product_ImgMapper.class})
public interface OrderItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "product.images", target = "productImages")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> orderItems);
}
