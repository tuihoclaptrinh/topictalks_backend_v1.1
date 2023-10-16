package com.anonymity.topictalks.models.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendInforResponse {
    private long friendListId;
    private long userid;
    private String userName;
    private String userUrl;
    private long friendId;
    private String friendName;
    private String friendUrl;
    private boolean isPublic;
    private boolean isAccept;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
