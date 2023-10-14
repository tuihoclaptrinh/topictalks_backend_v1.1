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
    private long friendId;
    private String friendName;
    private boolean isPublic;
    private boolean isAccept;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
