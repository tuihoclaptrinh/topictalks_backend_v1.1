package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.dtos
 * - Created At: 04-10-2023 22:03:43
 * @since 1.0 - version of class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngagementChatDTO {
    private Map<String, List<String>> urlParams;
    private int clientChatRandom;
    private Long topicChildrenId;
    private String timeAccess;
    private String timeLeaved;
}
