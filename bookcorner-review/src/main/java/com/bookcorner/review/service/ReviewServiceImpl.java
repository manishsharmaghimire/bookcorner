package com.bookcorner.review.service;

import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.repository.UserRepository;
import com.bookcorner.books.entity.Books;
import com.bookcorner.books.exception.BookNotFoundException;
import com.bookcorner.books.repository.BookRepository;
import com.bookcorner.review.dto.ReviewRequest;
import com.bookcorner.review.dto.ReviewResponse;
import com.bookcorner.review.entity.Review;
import com.bookcorner.review.exception.ReviewNotFoundException;
import com.bookcorner.review.mapper.ReviewMapper;
import com.bookcorner.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public ReviewResponse addReview(ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Books book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found."));

        if (reviewRepository.existsByUserAndBook(user, book)) {
            throw new RuntimeException("You have already reviewed this book.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        return reviewMapper.toReviewResponse(saved);
    }

    public List<ReviewResponse> getMyReviews() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found."));

        List<Review> reviews = reviewRepository.findByUser(user);
        return reviews.stream().map(reviewMapper::toReviewResponse).toList();
    }

    public List<ReviewResponse> getBookReviews(Long bookId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found."));

        List<Review> reviews = reviewRepository.findByBook(book);
        return reviews.stream().map(reviewMapper::toReviewResponse).toList();
    }

    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found."));

        if (!review.getUser().equals(user)) {
            throw new RuntimeException("You can only update your own reviews.");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updated = reviewRepository.save(review);
        return reviewMapper.toReviewResponse(updated);
    }

    public void deleteReview(Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found."));

        if (!review.getUser().equals(user)) {
            throw new RuntimeException("You can only delete your own reviews.");
        }

        reviewRepository.delete(review);
    }
}
