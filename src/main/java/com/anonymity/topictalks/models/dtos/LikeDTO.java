package com.anonymity.topictalks.models.dtos;

import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeDTO implements Serializable {
    private long userId;
    private long postId;
    private UserDTO userInfo;
    private PostDTO postInfo;
}
