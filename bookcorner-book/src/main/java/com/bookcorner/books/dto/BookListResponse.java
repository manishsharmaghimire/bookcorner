package com.bookcorner.books.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BookListResponse {

    private UUID id;

    private String title;

    private String authorName;

    private String categoryName;

    private BigDecimal price;

    private String coverImageUrl;

}