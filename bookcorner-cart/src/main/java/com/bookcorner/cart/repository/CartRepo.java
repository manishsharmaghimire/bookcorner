package com.bookcorner.cart.repository;

import com.bookcorner.auth.entity.User;
import com.bookcorner.books.entity.Books;
import com.bookcorner.cart.entity.Cart;
import com.bookcorner.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
    Optional<User>findByPhoneNumber(String phoneNumber);

    Optional<CartItem> findByCartAndBook(Cart cart, Books book);
}
