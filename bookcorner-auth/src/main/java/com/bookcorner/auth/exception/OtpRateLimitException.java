package com.bookcorner.auth.exception;

public class OtpRateLimitException extends RuntimeException {
    public OtpRateLimitException(String message) {
        super(message);
    }

    public OtpRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
