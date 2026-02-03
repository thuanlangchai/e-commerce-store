package com.minhthuan.web_store.domain.store.address.entity;

import com.minhthuan.web_store.domain.store.order.entity.Order;
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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String streetName;
    @Column(unique = true)
    private String phone;
    private String city;
    private String district;
    private String ward;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<Order> orders;
}
