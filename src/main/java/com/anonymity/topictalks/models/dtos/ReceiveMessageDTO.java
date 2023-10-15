package com.anonymity.topictalks.models.dtos;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.dtos
 * - Created At: 21-09-2023 18:39:09
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMessageDTO implements Serializable {

    private Long userId;
    private String targetName;
    private String username;
    private String timeAt;
    private Long targetId;
    private Long conversationId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("groupChatName")
    private String groupChatName;
    private boolean isGroupChat;
    private JSONObject data;

}
