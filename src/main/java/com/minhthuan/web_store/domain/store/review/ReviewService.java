package com.minhthuan.web_store.domain.store.review;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.product.ProductRepository;
import com.minhthuan.web_store.domain.store.product.entity.Product;
import com.minhthuan.web_store.domain.store.review.ReviewMapper;
import com.minhthuan.web_store.domain.store.review.ReviewRepository;
import com.minhthuan.web_store.domain.store.review.dto.ReviewRequest;
import com.minhthuan.web_store.domain.store.review.dto.ReviewResponse;
import com.minhthuan.web_store.domain.store.review.entity.Review;
import com.minhthuan.web_store.domain.store.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final JwtUtil jwtUtil;
    private final ProductRepository productRepository;

    public ApiResponse<ReviewResponse> createReview(ReviewRequest reviewRequest) {
        if (reviewRequest == null)
            throw new CustomException(ErrorCode.NOT_NULL);
        User user = jwtUtil.getCurrentUser();
        Product product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Review review = reviewMapper.toReview(reviewRequest);
        review.setUser(user);
        review.setProduct(product);
        reviewRepository.save(review);

        Double averageRating = reviewRepository.getRatingAverageByProductId(reviewRequest.getProductId());
        product.setRatingAverage(averageRating);
        productRepository.save(product);

        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);
        return new ApiResponse<>(reviewResponse, "Create Review Successfully");
    }

    public ApiResponse<ReviewResponse> updateReview(Long id, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        reviewMapper.updateReview(review, reviewRequest);
        reviewRepository.save(review);

        Double averageRating = reviewRepository.getRatingAverageByProductId(review.getProduct().getId());
        Product product = review.getProduct();
        product.setRatingAverage(averageRating);
        productRepository.save(product);

        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);
        return new ApiResponse<ReviewResponse>(reviewResponse, "Update Review Successfully");
    }

    public ApiResponse<ReviewResponse> deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        reviewRepository.delete(review);

        Double averageRating = reviewRepository.getRatingAverageByProductId(review.getProduct().getId());
        Product product = review.getProduct();
        product.setRatingAverage(averageRating);
        productRepository.save(product);

        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);
        return new ApiResponse<>(reviewResponse, "Delete Review Successfully");
    }

    public ApiResponse<List<ReviewResponse>> findAllReviews() {
        List<Review> reviewList = reviewRepository.findAll();
        List<ReviewResponse> reviewResponseList = reviewMapper.toReviewResponseList(reviewList);
        return new ApiResponse<>(reviewResponseList, "Find All Reviews Successfully");
    }
}
