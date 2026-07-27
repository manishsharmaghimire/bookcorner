package com.bookcorner.review.mapper;

import com.bookcorner.review.dto.ReviewResponse;
import com.bookcorner.review.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .bookId(review.getBook().getId())
                .title(review.getBook().getTitle())
                .authorName(review.getBook().getAuthor().getAuthorName())
                .userName(review.getUser().getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
