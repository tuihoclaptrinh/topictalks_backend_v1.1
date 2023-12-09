package com.anonymity.topictalks.models.payloads.requests;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.requests
 * - Created At: 07-12-2023 20:30:27
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingIdRequest implements Serializable {
    private Long userId;
    private Long tpcId;
}
