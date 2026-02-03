package com.minhthuan.web_store.domain.store.product;

import com.minhthuan.web_store.domain.store.product.dto.ProductRequest;
import com.minhthuan.web_store.domain.store.product.dto.ProductResponse;
import com.minhthuan.web_store.domain.store.product.entity.Product;
import com.minhthuan.web_store.domain.store.product_image.Product_ImgMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {Product_ImgMapper.class})
public interface ProductMapper {
    Product toProduct(ProductRequest productRequest);
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "images", target = "images")
    ProductResponse toProductResponse(Product product);
    void updateProduct(@MappingTarget Product product, ProductRequest productRequest);
    List<ProductResponse> toProductResponseList(List<Product> productList);
}
