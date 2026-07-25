package com.bookcorner.books.projection;

import java.math.BigDecimal;
import java.time.LocalDate;


public interface BookDetailProjection {
    Long getId();

    String getTitle();

    String getIsbn();

    String getDescription();

    BigDecimal getPrice();

    Integer getStock();

    String getLanguage();

    String getEdition();

    LocalDate getPublicationDate();

    Integer getPages();

    String getCoverImageUrl();

    String getAuthorName();

    String getCategoryName();

    String getPublisherName();
}
