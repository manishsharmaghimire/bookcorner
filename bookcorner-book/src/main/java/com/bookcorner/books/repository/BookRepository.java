package com.bookcorner.books.repository;

import com.bookcorner.books.dto.BookListResponse;
import com.bookcorner.books.entity.Books;
import com.bookcorner.books.enums.BookStatus;
import com.bookcorner.books.projection.BookDetailProjection;
import com.bookcorner.books.projection.BookProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Books, UUID> {


    boolean existsByIsbn(String isbn);

    Optional<Books> findByIsbn(String isbn);

    Page<Books> findByStatus(BookStatus status, Pageable pageable);


    @Query(value = """
SELECT
    b.id,
    b.title,
    b.price,
    b.cover_image_url AS coverImageUrl,
    a.author_name AS authorName,
    c.category_name AS categoryName
FROM books b
INNER JOIN authors a
    ON b.author_id = a.id
INNER JOIN categories c
    ON b.category_id = c.id
WHERE b.status = 'ACTIVE'
""", nativeQuery = true)
    Page<BookProjection> findAllActiveBooks(Pageable pageable);




@Query(value = """
SELECT
b.id,
b.title,
b.isbn,
b.description,
b.price,
b.stock,
b.language,
b.edition,
b.publication_date AS publicationDate,
b.pages,
b.cover_image_url AS coverImageUrl,
a.author_name AS authorName,
c.category_name AS categoryName,
p.publisher_name AS publisherName
FROM books b
INNER JOIN authors a
ON b.author_id = a.id
INNER JOIN categories c
ON b.category_id = c.id
INNER JOIN publishers p
ON b.publisher_id = p.id
WHERE b.id = :id
AND b.status = 'ACTIVE'
        """, nativeQuery = true)
Optional<BookDetailProjection> findActiveBookById(
        @Param("id") UUID id
);}