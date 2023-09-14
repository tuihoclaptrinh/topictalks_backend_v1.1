package com.anonymity.topictalks.exceptions;

import java.io.Serial;

/**
 * Custom exception class representing an invalid old password error.
 *
 * This exception is typically used to indicate that the provided old password is incorrect when attempting to change
 * the password. It extends the {@link RuntimeException} class, allowing it to be used without being explicitly caught
 * or declared.
 *
 * @see java.io.Serial
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.exceptions
 * - Created At: 14-09-2023 20:37:00
 * @since 1.0 - version of class
 */
public final class InvalidOldPasswordException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5861310537366287163L;

    /**
     * Constructs a new instance of the {@code InvalidOldPasswordException} class with no specific error message.
     */
    public InvalidOldPasswordException() {
        super();
    }

    /**
     * Constructs a new instance of the {@code InvalidOldPasswordException} class with the specified error message and
     * a reference to the cause of the exception.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public InvalidOldPasswordException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of the {@code InvalidOldPasswordException} class with the specified error message.
     *
     * @param message The error message.
     */
    public InvalidOldPasswordException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the {@code InvalidOldPasswordException} class with a reference to the cause of the
     * exception.
     *
     * @param cause The cause of the exception.
     */
    public InvalidOldPasswordException(final Throwable cause) {
        super(cause);
    }

}
