package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.LastMessageDTO;
import com.anonymity.topictalks.models.dtos.TopicChildrenDTO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Long id;
    private String chatName;
    private Boolean isGroupChat;
    private TopicChildrenDTO topicChildren;
    private Long adminId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("lastMessage")
    private LastMessageDTO lastMessageDTO;
}
