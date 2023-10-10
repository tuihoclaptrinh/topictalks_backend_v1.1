package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.utils.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 10-10-2023 10:26:45
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponse {
    private long id;
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private Instant dob;
    private String bio;
    private String gender;
    private String country;
    private String imageUrl;
    private boolean isBanned;
    private Instant bannedDate;
    private ERole role;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
