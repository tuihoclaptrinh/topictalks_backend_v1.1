package com.anonymity.topictalks.models.payloads.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 22-09-2023 19:00:37
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse implements Serializable {
    private Long conversationId;
    private String chatName;
    private Boolean isGroupChat;
    private Long topicChildrenId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avtGroupImg;
    private String topicChildrenName;
    private Long adminId;
}
