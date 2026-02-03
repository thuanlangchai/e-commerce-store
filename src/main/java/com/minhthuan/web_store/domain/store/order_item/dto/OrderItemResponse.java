package com.minhthuan.web_store.domain.store.order_item.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Double price;
    private String productName;
    private Double productPrice;
    private java.util.List<com.minhthuan.web_store.domain.store.product_image.dto.Product_ImgResponse> productImages;
}
