package com.anonymity.topictalks.models.dtos;

import com.anonymity.topictalks.models.payloads.responses.LikeResponse;
import com.anonymity.topictalks.models.persists.post.CommentPO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String topicName;
    private long author_id;
    private Long status;
    private String statusName;
    private String username;
    private String avatar_url;
    private boolean author_active;
    private long totalComment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("lastComment")
    private CommentDTO lastComment;
    private LikeResponse like;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private boolean isApproved;
    private boolean isRejected;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("reasonRejected")
    private String reasonRejected;
}
