package com.minhthuan.web_store.domain.store.product.dto;

import com.minhthuan.web_store.domain.store.product_image.dto.Product_ImgResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private double price;
    private int soldCount;
    private double ratingAverage;
    private List<Product_ImgResponse> images;
}
