package com.anonymity.topictalks.services;


import com.anonymity.topictalks.models.payloads.requests.TopicChildrenRequest;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;

public interface ITopicChildrenService {
    TopicChildrenPO create(TopicChildrenRequest request);

}

