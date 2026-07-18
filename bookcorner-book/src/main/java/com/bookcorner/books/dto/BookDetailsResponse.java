package com.bookcorner.books.dto;


import com.bookcorner.books.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BookDetailsResponse {

    private UUID id;

    private String title;

    private String isbn;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String language;

    private String edition;

    private LocalDate publicationDate;

    private Integer pages;

    private String coverImageUrl;

    private String categoryName;

    private String authorName;

    private String publisherName;

    private BookStatus status;

}