package com.anonymity.topictalks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class representing a token-related error.
 *
 * This exception is typically used to indicate issues related to tokens, such as token validation failures or other
 * problems related to authentication or authorization. It extends the {@link RuntimeException} class, allowing it to
 * be used without being explicitly caught or declared.
 *
 * @see java.io.Serial
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.exceptions
 * - Created At: 14-09-2023 20:33:53
 * @since 1.0 - version of class
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenException extends RuntimeException {

    /**
     * Constructs a new instance of the {@code TokenException} class with the specified token and message.
     *
     * @param token   The token associated with the exception.
     * @param message A descriptive message providing additional details about the exception.
     */
    public TokenException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }

}
