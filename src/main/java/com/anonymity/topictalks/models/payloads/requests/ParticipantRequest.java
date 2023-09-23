package com.anonymity.topictalks.models.payloads.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.requests
 * - Created At: 22-09-2023 18:30:28
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRequest implements Serializable {
    Map<Long, String> userInfoRequest;
    int amount;
    long topicChildId;
}
