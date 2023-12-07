package com.anonymity.topictalks.services;


import com.anonymity.topictalks.models.dtos.ChartTopicInforDTO;
import com.anonymity.topictalks.models.payloads.requests.TopicRequest;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;

import java.util.List;

public interface ITopicParentService {
    TopicParentPO create(TopicRequest request);

    TopicParentPO update(Long id, TopicRequest request);

    List<TopicParentPO> getAll();

    List<TopicParentPO> getAllByIsExpired(boolean isExpired);

    TopicParentPO updateTopicName(long id, String newName);

    TopicParentPO updateIsExpiredById(long id, boolean isExpired);

    boolean checkDuplicateTopicName(String newName, long id);

    List<TopicParentPO> searchByTopicParentName(String topicParentName, boolean isExpired);

    List<ChartTopicInforDTO> retrieveDataForTopicParent();

}

