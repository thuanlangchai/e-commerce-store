package com.minhthuan.web_store.domain.store.cart_item;

import com.minhthuan.web_store.domain.store.cart_item.dto.CartItemRequest;
import com.minhthuan.web_store.domain.store.cart_item.dto.CartItemResponse;
import com.minhthuan.web_store.domain.store.cart_item.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {com.minhthuan.web_store.domain.store.product.ProductMapper.class})
public interface CartItemMapper {
    CartItem toCartItem(CartItemRequest cartItemRequest);
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product", target = "product")
    CartItemResponse toCartItemResponse(CartItem cartItem);
    void updateCartItem(@MappingTarget CartItem cartItem, CartItemRequest cartItemRequest);
    List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItemList);
}
