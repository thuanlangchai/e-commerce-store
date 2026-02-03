package com.minhthuan.web_store.domain.store.product_image;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.product.ProductRepository;
import com.minhthuan.web_store.domain.store.product.entity.Product;
import com.minhthuan.web_store.domain.store.product_image.Product_ImgMapper;
import com.minhthuan.web_store.domain.store.product_image.Product_ImgRepository;
import com.minhthuan.web_store.domain.store.product_image.dto.Product_ImgRequest;
import com.minhthuan.web_store.domain.store.product_image.dto.Product_ImgResponse;
import com.minhthuan.web_store.domain.store.product_image.entity.Product_Img;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Product_ImgService {
    private final Product_ImgRepository product_ImgRepository;
    private final Product_ImgMapper product_ImgMapper;
    private final ProductRepository productRepository;

    public ApiResponse<Product_ImgResponse> createProductImg(Product_ImgRequest product_ImgRequest) {
        if (product_ImgRequest == null) {
            throw new CustomException(ErrorCode.NOT_NULL);
        }
        Product product = productRepository.findById(product_ImgRequest.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Product_Img product_Img = product_ImgMapper.toProductImg(product_ImgRequest);
        product_Img.setProduct(product);
        product_ImgRepository.save(product_Img);
        Product_ImgResponse product_ImgResponse = product_ImgMapper.toProductImgResponse(product_Img);
        return new ApiResponse<>(product_ImgResponse, "Create successfully");
    }

    public ApiResponse<Product_ImgResponse> updateProductImg(Long id, Product_ImgRequest product_ImgRequest) {
        if (id == null || product_ImgRequest == null) {
            throw new CustomException(ErrorCode.NOT_NULL);
        }
        Product_Img product_Img = product_ImgRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        product_ImgMapper.updateProductImg(product_Img, product_ImgRequest);
        product_ImgRepository.save(product_Img);
        Product_ImgResponse product_ImgResponse = product_ImgMapper.toProductImgResponse(product_Img);
        return new ApiResponse<>(product_ImgResponse, "Update successfully");
    }

    public ApiResponse<Product_ImgResponse> deleteProductImg(Long id) {
        if (id == null) {
            throw new CustomException(ErrorCode.NOT_NULL);
        }
        Product_Img product_Img = product_ImgRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        product_ImgRepository.delete(product_Img);
        Product_ImgResponse product_ImgResponse = product_ImgMapper.toProductImgResponse(product_Img);
        return new ApiResponse<>(product_ImgResponse, "Delete successfully");
    }

    public ApiResponse<List<Product_ImgResponse>> getAllProductImg() {
        List<Product_Img> product_Imgs = product_ImgRepository.findAll();
        List<Product_ImgResponse> product_ImgResponses = product_ImgMapper.toProductImgResponseList(product_Imgs);
        return new ApiResponse<>(product_ImgResponses, "Get all successfully");
    }
}
