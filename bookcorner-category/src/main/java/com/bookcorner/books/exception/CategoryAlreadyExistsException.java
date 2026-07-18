package com.bookcorner.books.exception;

public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }

    public CategoryAlreadyExistsException() {
        super("Category already exists.");
    }
}
