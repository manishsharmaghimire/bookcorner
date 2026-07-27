package com.bookcorner.review.repository;

import com.bookcorner.auth.entity.User;
import com.bookcorner.books.entity.Books;
import com.bookcorner.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserAndBook(User user, Books book);

    boolean existsByUserAndBook(User user, Books book);

    void deleteByUserAndBook(User user, Books book);

    List<Review> findByUser(User user);

    List<Review> findByBook(Books book);
}
