package com.minhthuan.web_store.domain.store.order.entity;

import com.minhthuan.web_store.domain.store.address.entity.Address;
import com.minhthuan.web_store.domain.store.order.entity.OrderStatus;
import com.minhthuan.web_store.domain.store.order.entity.PaymentStatus;
import com.minhthuan.web_store.domain.store.order_item.entity.OrderItem;
import com.minhthuan.web_store.domain.store.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double totalPrice;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private LocalDate createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

}
