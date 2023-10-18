package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.ConversationDTO;
import com.anonymity.topictalks.models.dtos.MessageDTO;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class NotiResponse {
    private Long notiId;
    private Long userId;
    private String username;
    private String chatName;
    private Long messageId;
    private Long conversationId;
    private Boolean isGroupChat;
    private Boolean isRead;
}
