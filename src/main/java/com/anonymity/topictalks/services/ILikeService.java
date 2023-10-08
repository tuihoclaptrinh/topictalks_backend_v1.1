package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.LikeDTO;
import com.anonymity.topictalks.models.payloads.requests.LikeRequest;
import com.anonymity.topictalks.models.persists.post.LikePO;

import java.util.List;

public interface ILikeService {
    LikePO like(LikeRequest request);
    LikePO unlike(long userId, long postId);
    LikeDTO getAllUserLikeByPostId(long postId);
}
