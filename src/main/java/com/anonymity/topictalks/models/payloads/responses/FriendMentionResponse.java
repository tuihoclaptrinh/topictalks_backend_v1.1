package com.anonymity.topictalks.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendMentionResponse {
    private Long friendId;
    private Long userId;
    private String username;
    private String fullName;
    private String imageUrl;
}
