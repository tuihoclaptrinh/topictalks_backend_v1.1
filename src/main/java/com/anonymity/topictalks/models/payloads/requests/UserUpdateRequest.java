package com.anonymity.topictalks.models.payloads.requests;

import com.anonymity.topictalks.utils.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private LocalDateTime dob;
    private String bio;
    private String gender;
    private String country;
    private String avatar;
}
