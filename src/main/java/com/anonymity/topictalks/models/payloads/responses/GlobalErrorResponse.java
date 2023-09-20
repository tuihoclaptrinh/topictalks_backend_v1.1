package com.anonymity.topictalks.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * GlobalErrorResponse is a class representing a standardized error response format for global exceptions handling.
 * It includes an error type, error message, and a timestamp of when the error occurred.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 14-09-2023 21:16:45
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalErrorResponse {

    /**
     * The type or category of the error.
     */
    private String error;

    /**
     * A descriptive error message explaining the error in more detail.
     */
    private String message;

    /**
     * A timestamp indicating when the error occurred.
     */
    private LocalDateTime timeStamp;

}
