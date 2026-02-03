package com.minhthuan.web_store.domain.store.review;

import com.minhthuan.web_store.domain.store.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select avg(r.rating) from Review r where r.product.id = :productId")
    Double getRatingAverageByProductId(Long productId);
}
