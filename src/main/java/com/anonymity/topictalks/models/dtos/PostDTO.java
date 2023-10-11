package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private long id;
    private String title;
    private String content;
    private String img_url;
    private long tparent_id;
    private long author_id;
    private String username;
    private String avatar_url;
    private long totalComment;
    private LikeDTO like;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private boolean isApproved;
}
