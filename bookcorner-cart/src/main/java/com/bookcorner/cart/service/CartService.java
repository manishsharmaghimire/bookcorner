package com.bookcorner.cart.service;

import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.exception.UserNotFoundException;
import com.bookcorner.auth.security.AuthenticationService;
import com.bookcorner.books.entity.Books;
import com.bookcorner.books.exception.BookNotFoundException;
import com.bookcorner.books.repository.BookRepository;
import com.bookcorner.cart.dto.AddQuantityRequest;
import com.bookcorner.cart.dto.CartItemResponse;
import com.bookcorner.cart.dto.CartResponse;
import com.bookcorner.cart.dto.UpdateCartItemRequest;
import com.bookcorner.cart.entity.Cart;
import com.bookcorner.cart.entity.CartItem;
import com.bookcorner.cart.exception.CartItemNotFoundException;
import com.bookcorner.cart.exception.CartNotFoundException;
import com.bookcorner.cart.mapper.CartMapper;
import com.bookcorner.cart.repository.CartItemRepo;
import com.bookcorner.cart.repository.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CartService {

private final CartRepo cartRepo;
private final AuthenticationService authenticationService;
private final BookRepository bookRepository;
private final CartItemRepo cartItemRepo;
private final CartMapper cartMapper;

    public CartResponse addToCart(Long bookId, AddQuantityRequest request) {


        User authenticatedUser = authenticationService.getAuthenticatedUser();

        Cart cart = cartRepo.findByUser(authenticatedUser)
                .orElseGet(() -> cartRepo.save(
                        Cart.builder()
                                .user(authenticatedUser)
                                .build()
                ));

        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found."));

        CartItem cartItem = cartItemRepo.findByCartAndBook(cart, book)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .book(book)
                    .quantity(request.getQuantity())
                    .build();
        }

        cartItemRepo.save(cartItem);

        cart = cartRepo.findByUser(authenticatedUser)
                .orElseThrow();

        return cartMapper.toCartResponse(cart);
    }



    public CartResponse getCart(){


        User authenticatedUser = authenticationService.getAuthenticatedUser();

        return cartRepo.findByUser(authenticatedUser)
                .map(cartMapper::toCartResponse)
                .orElse(CartResponse.builder()
                        .cartId(null).items(Collections.emptyList()).grandTotal(BigDecimal.ZERO).build());

    }


    public CartResponse updateCart(Long cartItemId, UpdateCartItemRequest request) {

        CartItem cartItem = getAndValidateCartItem(cartItemId);
        cartItem.setQuantity(request.getQuantity());
        cartItemRepo.save(cartItem);

        return cartMapper.toCartResponse(cartItem.getCart());
    }



    public CartResponse renoveItem(Long cartItemId){

        CartItem cartItem = getAndValidateCartItem(cartItemId);
        cartItemRepo.delete(cartItem);
        return cartMapper.toCartResponse(cartItem.getCart());
    }



    public void clearCart(){
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        cartRepo.findByUser(authenticatedUser)
                .ifPresent(cart -> cartItemRepo.deleteAll(cart.getCartItems()));
    }



    private CartItem getAndValidateCartItem(Long cartItemId) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        Cart cart = cartRepo.findByUser(authenticatedUser)
                .orElseThrow(() -> new CartNotFoundException("Cart not found."));
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found."));
        
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Invalid cart item.");
        }
        
        return cartItem;
    }
}
