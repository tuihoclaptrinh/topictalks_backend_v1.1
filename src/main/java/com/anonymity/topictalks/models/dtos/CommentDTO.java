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
public class CommentDTO {
    private long id;
    private long postId;
    private long userId;
    private String username;
    private String userImage;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
