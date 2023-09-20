package com.anonymity.topictalks.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * TokenErrorResponse is a class representing a standardized error response format for token-related exceptions.
 * It includes HTTP status, error type, a timestamp of when the error occurred, an error message, and the request path.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 14-09-2023 20:58:10
 * @since 1.0 - version of class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenErrorResponse {

    /**
     * The HTTP status code indicating the error.
     */
    private int status;

    /**
     * The type or category of the error.
     */
    private String error;

    /**
     * A timestamp indicating when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * A descriptive error message explaining the error in more detail.
     */
    private String message;

    /**
     * The path of the request that triggered the error.
     */
    private String path;

}
