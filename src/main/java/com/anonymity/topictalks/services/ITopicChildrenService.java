package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.payloads.requests.TopicChildrenRequest;
import com.anonymity.topictalks.models.payloads.requests.TopicRequest;
import com.anonymity.topictalks.models.payloads.responses.TopicChildrenResponse;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITopicChildrenService {
    TopicChildrenPO create(TopicChildrenRequest request);

    List<TopicChildrenPO> getTopicChildrenByTopicParentId(long parentTopicId);

    Page<TopicChildrenPO> getTopicChildrenByTopicParentIdAndIsExpired(long parentTopicId, boolean isExpired, int page, int size);

    TopicChildrenPO getTopicChildrenById(long TopicChildrenId);

    TopicChildrenPO update(long id, TopicRequest request);

    TopicChildrenPO updateIsExpiredById(long id, boolean isExpired);

    boolean checkDuplicateTopicName(String newName, long topicChildrenId,long topicParentId);

    Page<TopicChildrenPO> searchByTopicChildrenName(String topicChildrenName, boolean isExpired, int page, int size);

    List<TopicChildrenResponse> listsByParentId(Long tppId);

}

