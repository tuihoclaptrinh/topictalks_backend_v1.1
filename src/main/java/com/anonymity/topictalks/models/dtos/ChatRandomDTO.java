package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.dtos
 * - Created At: 06-10-2023 11:01:52
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRandomDTO {
    private List<String> users;
    private Long tpcId;
}
