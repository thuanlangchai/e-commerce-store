package com.minhthuan.web_store.domain.store.product;

import com.minhthuan.web_store.domain.store.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    List<Product> findByIdIn(List<Long> ids);
}
