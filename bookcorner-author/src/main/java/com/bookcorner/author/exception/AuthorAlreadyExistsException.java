package com.bookcorner.author.exception;

public class AuthorAlreadyExistsException extends RuntimeException {

    public AuthorAlreadyExistsException(String message) {
        super(message);
    }

    public AuthorAlreadyExistsException() {
        super("Author already exists.");
    }
}
