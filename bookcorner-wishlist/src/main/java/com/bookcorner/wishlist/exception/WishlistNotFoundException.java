package com.bookcorner.wishlist.exception;

public class WishlistNotFoundException extends RuntimeException {

    public WishlistNotFoundException(String message) {
        super(message);
    }

    public WishlistNotFoundException() {
        super("Wishlist item not found.");
    }
}
