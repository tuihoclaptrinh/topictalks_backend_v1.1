package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.topic.ITopicParentRepository;
import com.anonymity.topictalks.models.payloads.requests.TopicParentRequest;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.services.ITopicParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicParentServiceImpl implements ITopicParentService {

    @Autowired
    private ITopicParentRepository topicParentRepository;

    @Override
    public TopicParentPO create(TopicParentRequest request) {
        TopicParentPO topicParent = new TopicParentPO();
        List<TopicParentPO> list = topicParentRepository.findByTopicParentName(request.getTopicParentName());
        if (!list.isEmpty()) return null;
        topicParent.setTopicParentName(request.getTopicParentName());
        topicParent.setCreatedAt(LocalDateTime.now());
        topicParent.setUpdatedAt(LocalDateTime.now());
        return topicParentRepository.save(topicParent);
    }
}
