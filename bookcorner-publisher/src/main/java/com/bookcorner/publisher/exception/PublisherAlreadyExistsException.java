package com.bookcorner.publisher.exception;

public class PublisherAlreadyExistsException extends RuntimeException {

    public PublisherAlreadyExistsException(String message) {
        super(message);
    }

    public PublisherAlreadyExistsException() {
        super("Publisher already exists.");
    }
}
