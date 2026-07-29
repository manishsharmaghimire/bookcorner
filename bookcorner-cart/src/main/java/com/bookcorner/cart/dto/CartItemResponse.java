package com.bookcorner.cart.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CartItemResponse {

    private Long cartItemId;

    private Long bookId;

    private String title;

    private String authorName;

    private String coverImageUrl;

    private BigDecimal price;

    private Integer quantity;


    private BigDecimal subTotal;

}
