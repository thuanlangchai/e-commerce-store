package com.minhthuan.web_store.domain.store.product_image.entity;

import com.minhthuan.web_store.domain.store.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product_Img {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url_img;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
