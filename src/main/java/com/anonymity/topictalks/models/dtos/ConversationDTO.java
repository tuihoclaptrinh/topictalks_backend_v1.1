package com.anonymity.topictalks.models.dtos;

import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDTO {
    private Long id;
    private String chatName;
    private Boolean isGroupChat;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("lastMessage")
    private LastMessageDTO lastMessage;
    private TopicChildrenPO topicChildren;
    private long adminId;
    private String avtGroupImg;
}
