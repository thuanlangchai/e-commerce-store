package com.minhthuan.web_store.domain.store.product_image;

import com.minhthuan.web_store.domain.store.product_image.entity.Product_Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Product_ImgRepository extends JpaRepository<Product_Img, Long> {
}
