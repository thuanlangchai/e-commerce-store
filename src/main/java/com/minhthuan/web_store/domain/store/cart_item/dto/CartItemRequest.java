package com.minhthuan.web_store.domain.store.cart_item.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemRequest {
    private Long productId;
    private Integer quantity;

}
