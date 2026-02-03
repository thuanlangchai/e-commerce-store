package com.minhthuan.web_store.domain.store.review;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.review.dto.ReviewRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/user/reviews")
    public ResponseEntity<ApiResponse<?>> getReviews() {
        return ResponseEntity.ok(reviewService.findAllReviews());
    }

    @PostMapping("/user/create-reviews")
    public ResponseEntity<ApiResponse<?>> createReview(@RequestBody @Valid ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.createReview(reviewRequest));
    }

    @PutMapping("/user/update-reviews/{id}")
    public ResponseEntity<ApiResponse<?>> updateReview(@RequestBody @Valid ReviewRequest reviewRequest, @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewRequest));
    }

    @DeleteMapping("/user/delete-reviews/{id}")
    public ResponseEntity<ApiResponse<?>> deleteReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}
