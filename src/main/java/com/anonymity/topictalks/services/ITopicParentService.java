package com.anonymity.topictalks.services;


import com.anonymity.topictalks.models.payloads.requests.TopicParentRequest;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;

import java.util.List;

public interface ITopicParentService {
    TopicParentPO create(TopicParentRequest request);

    List<TopicParentPO> getAll();

    List<TopicParentPO> getAllByIsExpired(boolean isExpired);

    TopicParentPO updateTopicName(long id, String newName);

    TopicParentPO updateIsExpiredById(long id, boolean isExpired);

    boolean checkDuplicateTopicName(String newName);

    List<TopicParentPO> searchByTopicParentName(String topicParentName, boolean isExpired);

}

