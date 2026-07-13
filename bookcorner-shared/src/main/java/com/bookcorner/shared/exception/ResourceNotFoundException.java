package com.bookcorner.shared.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested resource is not found.
 * Used across all modules for consistent error handling.
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String resource;
    private final Object identifier;

    public ResourceNotFoundException(String resource, Object identifier) {
        super(String.format("%s with identifier '%s' not found", resource, identifier));
        this.resource = resource;
        this.identifier = identifier;
    }

    public static ResourceNotFoundException of(String resource, Object identifier) {
        return new ResourceNotFoundException(resource, identifier);
    }
}
