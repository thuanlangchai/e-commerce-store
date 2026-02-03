package com.minhthuan.web_store.domain.store.cart_item;

import com.minhthuan.web_store.domain.store.cart_item.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByIdIn(List<Long> ids);
    List<CartItem> findByCartId(Long cartId);
}
