package com.anonymity.topictalks.models.dtos;

import com.anonymity.topictalks.utils.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
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
