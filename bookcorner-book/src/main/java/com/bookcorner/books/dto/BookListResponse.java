package com.bookcorner.books.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {

    private String id;

    private String title;

    private String authorName;

    private String categoryName;

    private BigDecimal price;

    private String coverImageUrl;

}