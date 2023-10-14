package com.anonymity.topictalks.services;


import com.anonymity.topictalks.models.payloads.requests.TopicParentRequest;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;

import java.util.List;

public interface ITopicParentService {
    TopicParentPO create(TopicParentRequest request);

    List<TopicParentPO> getAll();

    TopicParentPO updateTopicName(long id, String newName);

    boolean checkDuplicateTopicName(String newName);

}

