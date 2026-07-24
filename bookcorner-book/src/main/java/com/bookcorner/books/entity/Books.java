package com.bookcorner.books.entity;

import com.bookcorner.author.entity.Author;
import com.bookcorner.books.enums.BookStatus;
import com.bookcorner.publisher.entity.Publisher;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(
        name = "books",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_book_isbn",
                        columnNames = "isbn"
                )
        }
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Books extends BaseEntity {


    @Column(nullable = false, length = 250)
    private String title;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(length = 5000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 50)
    private String language;

    @Column(length = 50)
    private String edition;

    private LocalDate publicationDate;

    @Column(length = 500)
    private String coverImageUrl;

    private Integer pages;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;
}

