package com.anonymity.topictalks.services;


import com.anonymity.topictalks.models.payloads.requests.TopicParentRequest;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;

public interface ITopicParentService {
    TopicParentPO create(TopicParentRequest request);

}

