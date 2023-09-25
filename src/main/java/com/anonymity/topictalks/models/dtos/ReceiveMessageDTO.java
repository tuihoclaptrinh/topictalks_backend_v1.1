package com.anonymity.topictalks.models.dtos;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private Long targetId;
    private String targetName;
    private Long conversationId;
    private JSONObject data;

}
