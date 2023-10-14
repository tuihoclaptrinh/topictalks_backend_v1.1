package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.LikeDTO;
import com.anonymity.topictalks.models.payloads.requests.LikeRequest;
import com.anonymity.topictalks.models.payloads.responses.LikeResponse;
import com.anonymity.topictalks.models.persists.post.LikePO;

import java.util.List;

public interface ILikeService {
    LikePO like(LikeRequest request);
    boolean unlike(long userId, long postId);
    LikeResponse getAllUserLikeByPostId(long postId);
}
