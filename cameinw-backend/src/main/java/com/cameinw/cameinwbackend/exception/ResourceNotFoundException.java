package com.cameinw.cameinwbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceNotFoundException is an exception class that represents a runtime exception
 * indicating that a resource was not found.
 *
 * The @ResponseStatus(HttpStatus.NOT_FOUND) annotation is used to indicate that
 * when an exception of this type is thrown within a Spring MVC controller method,
 * it should result in an HTTP response with a status code of 404 (Not Found).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    /**
     * Constructs a new ResourceNotFoundException with no specified error message.
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified error message.
     *
     * @param message The error message for this exception.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified error message and cause.
     *
     * @param message The error message for this exception.
     * @param cause   The cause of this exception.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
