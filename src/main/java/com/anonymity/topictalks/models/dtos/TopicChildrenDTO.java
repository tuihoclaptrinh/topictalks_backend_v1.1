package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.dtos
 * - Created At: 08-10-2023 18:30:47
 * @since 1.0 - version of class
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicChildrenDTO {
    private Long id;
    private String tpcName;
    private String image;
}
