package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.ConversationDTO;
import com.anonymity.topictalks.models.dtos.MessageDTO;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 18-10-2023 13:23:08
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotiResponse {
    private Long notiId;
    private Long userId;
    private String username;
    private Long partnerId;
    private String partnerUsername;
    private String chatName;
    private String message;
    private Long conversationId;
    private Long postId;
    private Boolean isGroupChat;
    private Boolean isRead;
    @JsonIgnore
    private LocalDateTime createAt;
}
