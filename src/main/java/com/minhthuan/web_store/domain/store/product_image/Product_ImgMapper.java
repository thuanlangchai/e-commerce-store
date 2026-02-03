package com.minhthuan.web_store.domain.store.product_image;

import com.minhthuan.web_store.domain.store.product_image.dto.Product_ImgRequest;
import com.minhthuan.web_store.domain.store.product_image.dto.Product_ImgResponse;
import com.minhthuan.web_store.domain.store.product_image.entity.Product_Img;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface Product_ImgMapper {
    Product_Img toProductImg(Product_ImgRequest productImgRequest);
    @Mapping(source = "product.id", target = "productId")
    Product_ImgResponse toProductImgResponse(Product_Img product_img);
    void updateProductImg(@MappingTarget Product_Img productImg, Product_ImgRequest productImgRequest);
    List<Product_ImgResponse> toProductImgResponseList(List<Product_Img> productImgList);
}
