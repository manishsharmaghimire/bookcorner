package com.bookcorner.author.exception;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException() {
        super("Author not found.");
    }
}
