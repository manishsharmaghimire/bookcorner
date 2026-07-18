package com.bookcorner.books.projection;


import java.math.BigDecimal;
import java.util.UUID;
public interface BookProjection {



    UUID getId();

    String getTitle();

    BigDecimal getPrice();

    String getCoverImageUrl();

    String getAuthorName();

    String getCategoryName();
}
