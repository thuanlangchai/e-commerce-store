package com.minhthuan.web_store.domain.store.product.entity;

import com.minhthuan.web_store.domain.store.cart_item.entity.CartItem;
import com.minhthuan.web_store.domain.store.category.entity.Category;
import com.minhthuan.web_store.domain.store.order_item.entity.OrderItem;
import com.minhthuan.web_store.domain.store.product_image.entity.Product_Img;
import com.minhthuan.web_store.domain.store.review.entity.Review;
import com.minhthuan.web_store.domain.store.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private double price;
    private int soldCount;
    private double ratingAverage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Product_Img> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
