package com.anonymity.topictalks.models.payloads.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.requests
 * - Created At: 10-10-2023 10:15:37
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    private long userId;
    private long friendId;

}
