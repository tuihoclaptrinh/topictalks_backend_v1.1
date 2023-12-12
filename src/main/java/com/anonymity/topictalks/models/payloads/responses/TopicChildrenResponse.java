package com.anonymity.topictalks.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 11-12-2023 15:42:32
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicChildrenResponse {

    private Long tpcId;
    private String tpcName;

}
