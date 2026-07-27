package com.bookcorner.wishlist.service;


import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.repository.UserRepository;
import com.bookcorner.books.entity.Books;
import com.bookcorner.books.exception.BookNotFoundException;
import com.bookcorner.books.repository.BookRepository;
import com.bookcorner.wishlist.dto.WishlistResponse;
import com.bookcorner.wishlist.entity.WishList;
import com.bookcorner.wishlist.exception.WishlistNotFoundException;
import com.bookcorner.wishlist.mapper.WishlistMapper;
import com.bookcorner.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListImpl {

    private final WishListRepository wishlistRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final WishlistMapper wishlistMapper;

    public WishlistResponse addBookToWishlist(Long bookId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String phoneNumber = authentication.getName();

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new RuntimeException("User not found.")
                );

        Books book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found.")
                );


        if(wishlistRepository.existsByUserAndBook(user, book)) {
            throw new RuntimeException("Book already in wishlist.");
        }
            WishList wishList = new WishList();
            wishList.setUser(user);
            wishList.setBook(book);
            WishList save = wishlistRepository.save(wishList);

            return wishlistMapper.toWishlistResponse(save);
        }




        public List<WishlistResponse> getMyWishList(){


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String authUser = authentication.getName();

            User user = userRepository.findByPhoneNumber(authUser).orElseThrow(() ->
                    new RuntimeException("User not found.")
            );


            List<WishList> byUser = wishlistRepository.findByUser(user);
            return byUser.stream().map(wishlistMapper::toWishlistResponse).toList();


        }


        public void removeFromWishList(Long bookId){


            String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                    new RuntimeException("User not found.")
            );


            Books book = bookRepository.findById(bookId)
                    .orElseThrow(() ->
                            new BookNotFoundException("Book not found.")
                    );


          WishList wishList = wishlistRepository.findByUserAndBook(user, book) .orElseThrow(() ->
                    new WishlistNotFoundException(
                            "Book is not in wishlist."
                    )
            );
            wishlistRepository.delete(wishList);


        }


}