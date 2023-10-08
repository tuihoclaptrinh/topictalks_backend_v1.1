package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.TopicChildrenDTO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 08-10-2023 17:41:56
 * @since 1.0 - version of class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRandomResponse implements Serializable {
    private Long conversationId;
    private String chatName;
    private Boolean isGroupChat;
    private TopicChildrenDTO topicChildren;
    private Long adminId;
}
