package com.minhthuan.web_store.domain.store.review;

import com.minhthuan.web_store.domain.store.review.dto.ReviewRequest;
import com.minhthuan.web_store.domain.store.review.dto.ReviewResponse;
import com.minhthuan.web_store.domain.store.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(ReviewRequest reviewRequest);
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    ReviewResponse toReviewResponse(Review review);
    void updateReview(@MappingTarget Review review, ReviewRequest reviewRequest);
    List<ReviewResponse> toReviewResponseList(List<Review> reviewList);
}
