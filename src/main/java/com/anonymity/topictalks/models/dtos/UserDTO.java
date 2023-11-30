package com.anonymity.topictalks.models.dtos;

import com.anonymity.topictalks.utils.enums.ERole;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private LocalDateTime dob;
    private String bio;
    private String gender;
    private String country;
    private String imageUrl;
    private Boolean isBanned;
    private LocalDateTime bannedDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dueDateUnBan;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long totalNumOfPosts;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long totalNumOfFriends;
    private ERole role;
    private boolean active;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
