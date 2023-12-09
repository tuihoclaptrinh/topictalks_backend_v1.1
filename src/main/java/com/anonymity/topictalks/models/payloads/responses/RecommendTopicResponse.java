package com.anonymity.topictalks.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 09-12-2023 09:28:02
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendTopicResponse {
    private Long topicChildrenId;
    private String topicChildrenName;
    private String image;
    private BigDecimal avgRating;
    private String shortDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
