package com.bookcorner.review.entity;


import com.bookcorner.auth.entity.User;
import com.bookcorner.books.entity.Books;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_review_user_book",
                columnNames = {"user_id", "book_id"}
        )
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
ev
public class Review extends BaseEntity {

    @ManyToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

}
