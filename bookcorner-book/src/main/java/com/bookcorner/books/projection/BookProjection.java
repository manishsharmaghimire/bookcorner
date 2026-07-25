package com.bookcorner.books.projection;


import java.math.BigDecimal;


public interface BookProjection {



    Long getId();

    String getTitle();

    BigDecimal getPrice();

    String getCoverImageUrl();

    String getAuthorName();

    String getCategoryName();
}
