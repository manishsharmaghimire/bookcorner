package com.bookcorner.order.dto;

import lombok.*;

import java.math.BigDecimal;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {


    private Long bookId;

    private String title;

    private String authorName;

    private String coverImageUrl;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subTotal;
}
