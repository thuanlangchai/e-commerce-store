package com.minhthuan.web_store.domain.store.cart_item.dto;

import com.minhthuan.web_store.domain.store.product.dto.ProductResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemResponse {
    private Long id;
    private Long productId;
    private Long cartId;
    private Integer quantity;
    private ProductResponse product;
}
