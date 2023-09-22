package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.payloads.requests.UserTopicRequest;
import com.anonymity.topictalks.models.persists.topic.UserTopicPO;

import java.util.List;

public interface IUserTopicService {
    List<UserTopicPO> createUserTopic(Long userId, UserTopicRequest request);
}
