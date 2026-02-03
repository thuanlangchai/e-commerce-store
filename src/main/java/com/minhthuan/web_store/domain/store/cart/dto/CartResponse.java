package com.minhthuan.web_store.domain.store.cart.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartResponse {
    private Long id;
    private Long userId;
}
