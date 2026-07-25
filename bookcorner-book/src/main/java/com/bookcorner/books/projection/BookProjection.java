package com.bookcorner.books.projection;


import java.math.BigDecimal;
public interface BookProjection {



    String getId();

    String getTitle();

    BigDecimal getPrice();

    String getCoverImageUrl();

    String getAuthorName();

    String getCategoryName();
}
