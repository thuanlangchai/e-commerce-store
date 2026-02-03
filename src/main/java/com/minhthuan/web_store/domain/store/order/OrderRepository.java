package com.minhthuan.web_store.domain.store.order;

import com.minhthuan.web_store.domain.store.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserIdOrderByCreateAtDesc(Long userId);
}
