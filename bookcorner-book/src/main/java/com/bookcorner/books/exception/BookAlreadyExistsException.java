package com.bookcorner.books.exception;

public class BookAlreadyExistsException extends RuntimeException {

    public BookAlreadyExistsException(String message) {
        super(message);
    }

    public BookAlreadyExistsException() {
        super("Book already exists.");
    }
}
