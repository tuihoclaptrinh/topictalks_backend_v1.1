package com.anonymity.topictalks.models.dtos;

import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
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
    private TopicChildrenPO topicChildren;
    private long adminId;
    private String avtGroupImg;
}
