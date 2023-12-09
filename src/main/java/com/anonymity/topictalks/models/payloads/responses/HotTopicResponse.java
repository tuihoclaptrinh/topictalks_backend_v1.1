package com.anonymity.topictalks.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 07-12-2023 20:52:46
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotTopicResponse {

    private Long topicChildrenId;
    private Long tpcCount;
    private Integer maxRating;
    private BigDecimal avgRating;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String image;
    private String topicChildrenName;
    private String shortDescription;

}
