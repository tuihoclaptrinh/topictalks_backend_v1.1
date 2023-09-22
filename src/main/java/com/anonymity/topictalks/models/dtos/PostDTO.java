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
    private Long id;
    private String title;
    private String content;
    private String img_url;
    private Long tparent_id;
    private Long author_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private boolean isApproved;

}
