package com.minhthuan.web_store.domain.store.review.entity;

import com.minhthuan.web_store.domain.store.product.entity.Product;
import com.minhthuan.web_store.domain.store.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    @Column(columnDefinition = "INT CHECK (rating >= 1 AND rating <=5)")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
