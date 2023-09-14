package com.anonymity.topictalks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class representing application-level errors.
 *
 * This exception is typically used to represent errors that occur within the application logic and result in an
 * internal server error (HTTP status code 500). It extends the {@link RuntimeException} class, allowing it to be
 * used without being explicitly caught or declared.
 *
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see org.springframework.http.HttpStatus#INTERNAL_SERVER_ERROR
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.exceptions
 * - Created At: 14-09-2023 20:36:13
 * @since 1.0 - version of class
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {

    /**
     * Constructs a new instance of the {@code AppException} class with the specified error message.
     *
     * @param message The error message.
     */
    public AppException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the {@code AppException} class with the specified error message and a
     * reference to the cause of the exception.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
