package com.minhthuan.web_store.domain.store.user.entity;



import com.minhthuan.web_store.domain.store.address.entity.Address;
import com.minhthuan.web_store.domain.store.cart.entity.Cart;
import com.minhthuan.web_store.domain.store.product.entity.Product;
import com.minhthuan.web_store.domain.store.review.entity.Review;
import com.minhthuan.web_store.domain.store.token.entity.Token;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(columnDefinition = "varchar(255) default 'USER'")
    @Enumerated(EnumType.STRING)
    private Role role;
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> address;

    @OneToMany(mappedBy = "user")
    private List<Product> products;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;


}
