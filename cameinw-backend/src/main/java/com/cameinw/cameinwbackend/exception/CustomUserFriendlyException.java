package com.cameinw.cameinwbackend.exception;

/**
 * CustomUserFriendlyException is an exception class that represents a runtime exception
 * with a user-friendly error message.
 */
public class CustomUserFriendlyException extends RuntimeException{
    /**
     * Constructs a new CustomUserFriendlyException with the specified error message.
     *
     * @param message The error message for this exception.
     */
    public CustomUserFriendlyException(String message) {
        super(message);
    }
}
