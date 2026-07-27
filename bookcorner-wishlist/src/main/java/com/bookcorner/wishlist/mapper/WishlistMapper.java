package com.bookcorner.wishlist.mapper;

import com.bookcorner.wishlist.dto.WishlistResponse;
import com.bookcorner.wishlist.entity.WishList;
import org.springframework.stereotype.Component;

@Component

public class WishlistMapper {




        public WishlistResponse toWishlistResponse(WishList wishlist) {

            return WishlistResponse.builder()
                    .bookId(wishlist.getBook().getId())
                    .title(wishlist.getBook().getTitle())
                    .authorName(wishlist.getBook().getAuthor().getAuthorName())
                    .categoryName(wishlist.getBook().getCategory().getCategoryName())
                    .price(wishlist.getBook().getPrice())
                    .coverImageUrl(wishlist.getBook().getCoverImageUrl())
                    .build();
        }
    }

