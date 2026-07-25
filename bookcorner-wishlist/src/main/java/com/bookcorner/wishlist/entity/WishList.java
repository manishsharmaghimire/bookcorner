package com.bookcorner.wishlist.entity;


import com.bookcorner.auth.entity.User;
import com.bookcorner.books.entity.Books;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "wishlist", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_wishlist_user_book",
                columnNames = {"user_id", "book_id"}
        )
})
public class WishList extends BaseEntity {

    @ManyToOne(
            fetch = FetchType.LAZY)

    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;


}
