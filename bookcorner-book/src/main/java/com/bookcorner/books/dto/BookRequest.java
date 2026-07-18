package com.bookcorner.books.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BookRequest {

    @NotBlank(message = "Title is required.")
    @Size(max = 250)
    private String title;

    @NotBlank(message = "ISBN is required.")
    @Size(max = 20)
    private String isbn;

    @Size(max = 5000)
    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull(message = "Stock is required.")
    @Min(0)
    private Integer stock;

    @Size(max = 50)
    private String language;

    @Size(max = 50)
    private String edition;

    private LocalDate publicationDate;

    @Size(max = 500)
    private String coverImageUrl;

    @Min(1)
    private Integer pages;

    @NotNull(message = "Category is required.")
    private UUID categoryId;

    @NotNull(message = "Author is required.")
    private UUID authorId;

    @NotNull(message = "Publisher is required.")
    private UUID publisherId;

}