package com.bookcorner.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;

    private String title;

    private String isbn;

    private String authorName;

    private String categoryName;

    private String publisherName;

    private String message;

}