package com.bookcorner.review.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ReviewResponse {
    private Long id;
    private Long bookId;
    private String title;
    private String authorName;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
