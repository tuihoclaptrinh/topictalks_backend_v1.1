package com.anonymity.topictalks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class representing a bad request error.
 *
 * This exception is typically used to represent errors where the client's request is malformed or invalid, resulting
 * in a bad request response (HTTP status code 400). It extends the {@link RuntimeException} class, allowing it to be
 * used without being explicitly caught or declared.
 *
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see org.springframework.http.HttpStatus#BAD_REQUEST
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.exceptions
 * - Created At: 14-09-2023 20:36:36
 * @since 1.0 - version of class
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new instance of the {@code BadRequestException} class with the specified error message.
     *
     * @param message The error message.
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the {@code BadRequestException} class with the specified error message and a
     * reference to the cause of the exception.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
