package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQaDTO implements Serializable {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
}
