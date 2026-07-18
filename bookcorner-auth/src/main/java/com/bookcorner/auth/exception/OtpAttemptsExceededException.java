package com.bookcorner.auth.exception;

public class OtpAttemptsExceededException extends RuntimeException {
    public OtpAttemptsExceededException(String message) {
        super(message);
    }

    public OtpAttemptsExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
