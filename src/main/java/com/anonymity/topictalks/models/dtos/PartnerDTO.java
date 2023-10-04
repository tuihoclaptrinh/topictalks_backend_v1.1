package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerDTO {
    private long Id;
    private String username;
    private boolean isBanned;
    private String image;
    private Instant bannedAt;
    private boolean isMember;
}
