package com.bookcorner.wishlist.repository;

import com.bookcorner.auth.entity.User;
import com.bookcorner.books.entity.Books;
import com.bookcorner.wishlist.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUserAndBook(User user, Books book);

    boolean existsByUserAndBook(User user, Books book);

    void deleteByUserAndBook(User user, Books book);

    List<WishList> findByUser(User user);
}
