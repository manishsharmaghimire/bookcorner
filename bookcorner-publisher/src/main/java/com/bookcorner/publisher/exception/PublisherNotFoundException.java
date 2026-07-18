package com.bookcorner.publisher.exception;

public class PublisherNotFoundException extends RuntimeException {

    public PublisherNotFoundException(String message) {
        super(message);
    }

    public PublisherNotFoundException() {
        super("Publisher not found.");
    }
}
