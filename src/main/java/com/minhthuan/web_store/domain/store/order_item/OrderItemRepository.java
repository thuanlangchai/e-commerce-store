package com.minhthuan.web_store.domain.store.order_item;

import com.minhthuan.web_store.domain.store.order_item.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
