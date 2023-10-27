package com.anonymity.topictalks.models.payloads.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.requests
 * - Created At: 16-10-2023 22:17:35
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các trường có giá trị null trong JSON
@JsonIgnoreProperties(ignoreUnknown = true) // Bỏ qua các trường không xác định trong JSON
public class NotiRequest {
    private Long userId;
    private Long conversationId;
    private Long postId;
    private String messageNoti;
}
