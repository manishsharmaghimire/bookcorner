package com.bookcorner.cart.repository;

import com.bookcorner.books.entity.Books;
import com.bookcorner.cart.entity.Cart;
import com.bookcorner.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByCartAndBook(Cart cart, Books book);
    List<CartItem> findByCart(
            Cart cart
    );

}
