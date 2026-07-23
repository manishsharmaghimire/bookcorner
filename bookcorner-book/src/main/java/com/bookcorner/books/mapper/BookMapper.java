package com.bookcorner.books.mapper;

import com.bookcorner.books.dto.BookDetailsResponse;
import com.bookcorner.books.dto.BookListResponse;
import com.bookcorner.books.dto.CategoryResponse;
import com.bookcorner.books.entity.Category;
import com.bookcorner.books.projection.BookDetailProjection;
import com.bookcorner.books.projection.BookProjection;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookListResponse toBookListResponse(
            BookProjection book
    ) {

        return new BookListResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthorName(),
                book.getCategoryName(),
                book.getPrice(),
                book.getCoverImageUrl()

        );
    }

    public BookDetailsResponse toBookDetailsResponse(
            BookDetailProjection book
    ) {

        return new BookDetailsResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getDescription(),
                book.getPrice(),
                book.getStock(),
                book.getLanguage(),
                book.getEdition(),
                book.getPublicationDate(),
                book.getPages(),
                book.getCoverImageUrl(),
                book.getCategoryName(),
                book.getAuthorName(),
                book.getPublisherName(),
                null
        );
    }
}
