package com.bookcorner.wishlist.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class WishlistResponse {
    private Long bookId;

    private String title;

    private String authorName;

    private String categoryName;

    private BigDecimal price;

    private String coverImageUrl;

}
