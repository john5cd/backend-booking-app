package com.cameinw.cameinwbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceAlreadyExistException is an exception class that represents a runtime exception
 * indicating that a resource already exists.
 *
 * The @ResponseStatus(HttpStatus.NOT_FOUND) annotation is used to indicate that
 * when an exception of this type is thrown within a Spring MVC controller method,
 * it should result in an HTTP response with a status code of 404 (Not Found).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistException extends RuntimeException{
    /**
     * Constructs a new ResourceAlreadyExistException with no specified error message.
     */
    public ResourceAlreadyExistException() {
        super();
    }

    /**
     * Constructs a new ResourceAlreadyExistException with the specified error message.
     *
     * @param message The error message for this exception.
     */
    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceAlreadyExistException with the specified error message and cause.
     *
     * @param message The error message for this exception.
     * @param cause   The cause of this exception.
     */
    public ResourceAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
