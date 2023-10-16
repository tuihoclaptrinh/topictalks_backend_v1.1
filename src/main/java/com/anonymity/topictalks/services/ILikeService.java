package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.LikeDTO;
import com.anonymity.topictalks.models.payloads.requests.LikeRequest;
import com.anonymity.topictalks.models.payloads.responses.LikeResponse;
import com.anonymity.topictalks.models.persists.post.LikePO;

public interface ILikeService {
    LikePO like(LikeRequest request);

    void unlike(long userId, long postId);

    LikeResponse getAllUserLikeByPostId(long postId);
}
