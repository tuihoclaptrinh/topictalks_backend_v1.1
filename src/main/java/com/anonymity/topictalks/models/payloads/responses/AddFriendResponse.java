package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 10-10-2023 10:22:27
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddFriendResponse {

    private long friendRequestId;
    private long userId;
    private long friendId;
    private boolean isPublic;
    private boolean isAccept;

}
