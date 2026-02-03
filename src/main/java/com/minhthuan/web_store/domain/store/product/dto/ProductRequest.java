package com.minhthuan.web_store.domain.store.product.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private Long categoryId;
}
