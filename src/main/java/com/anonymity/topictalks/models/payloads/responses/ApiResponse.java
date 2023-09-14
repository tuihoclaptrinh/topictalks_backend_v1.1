package com.anonymity.topictalks.models.payloads.responses;

import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * ApiResponse is a class that represents a standardized response format for API responses.
 * It includes a message, success flag, and HTTP status.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 14-09-2023 21:11:07
 * @since 1.0 - version of class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    /**
     * The message associated with the response.
     */
    private String message;

    /**
     * A boolean flag indicating the success or failure of the operation.
     */
    private boolean success;

    /**
     * The HTTP status code associated with the response.
     */
    private HttpStatus status;
}
