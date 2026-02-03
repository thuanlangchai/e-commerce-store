package com.minhthuan.web_store.domain.store.review.dto;

import com.minhthuan.web_store.common.excpetion.ErrorCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewRequest {
    @NotNull(message = "NOT_NULL")
    private Long productId;
    
    @NotNull(message = "NOT_NULL")
    @Min(value = 1, message = "RATING_INVALID")
    @Max(value = 5, message = "RATING_INVALID")
    private Integer rating;
    
    private String comment;
}
