package com.anonymity.topictalks.services;


import com.anonymity.topictalks.models.payloads.requests.TopicChildrenRequest;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;

import java.util.List;

public interface ITopicChildrenService {
    TopicChildrenPO create(TopicChildrenRequest request);

    List<TopicChildrenPO> getTopicChildrenByTopicParentId(long parentTopicId);

    List<TopicChildrenPO> getTopicChildrenByTopicParentIdAndIsExpired(long parentTopicId, boolean isExpired);

    TopicChildrenPO getTopicChildrenById(long TopicChildrenId);

    TopicChildrenPO update(long id, String newName,String newDescription);

    TopicChildrenPO updateIsExpiredById(long id, boolean isExpired);

    boolean checkDuplicateTopicName(String newName, long topicParentId) ;

}

