package com.anonymity.topictalks.models.payloads.requests;

import com.anonymity.topictalks.utils.enums.ERole;
import com.anonymity.topictalks.validations.annotations.NullOrNotBlank;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest implements Serializable {
    private String fullName;
    @NullOrNotBlank
    private String email;
    private String phoneNumber;
    private LocalDateTime dob;
    private String bio;
    private String gender;
    private String country;
}
