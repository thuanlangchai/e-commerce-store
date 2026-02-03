package com.minhthuan.web_store.domain.store.product_image.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product_ImgResponse {
    private Long id;
    private String url_img;
    private Long productId;
}
