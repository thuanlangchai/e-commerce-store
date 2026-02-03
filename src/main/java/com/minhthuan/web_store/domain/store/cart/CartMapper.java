package com.minhthuan.web_store.domain.store.cart;

import com.minhthuan.web_store.domain.store.cart.dto.CartRequest;
import com.minhthuan.web_store.domain.store.cart.dto.CartResponse;
import com.minhthuan.web_store.domain.store.cart.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toCart(CartRequest cartRequest);
    @Mapping(source = "user.id", target = "userId")
    CartResponse toCartResponse(Cart cart);
    void updateCart(@MappingTarget Cart cart, CartRequest cartRequest);
}
