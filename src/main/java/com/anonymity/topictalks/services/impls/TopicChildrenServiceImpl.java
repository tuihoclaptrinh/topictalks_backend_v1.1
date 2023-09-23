package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.topic.ITopicParentRepository;
import com.anonymity.topictalks.models.payloads.requests.TopicChildrenRequest;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.services.ITopicChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicChildrenServiceImpl implements ITopicChildrenService {

    @Autowired
    private ITopicChildrenRepository topicChildrenRepository;
    @Autowired
    private ITopicParentRepository topicParentRepository;

    @Override
    public TopicChildrenPO create(TopicChildrenRequest request) {
        TopicChildrenPO topicChildren = new TopicChildrenPO();
        TopicParentPO topicParent = topicParentRepository.findById(request.getTopicParentId())
                .orElseThrow(() -> new IllegalArgumentException("Topic parent doesn't exist."));
       List<TopicChildrenPO> list = topicChildrenRepository.findByTopicChildrenNameAndTopicParentId(request.getTopicChildrenName(), request.getTopicParentId());
        if (!list.isEmpty()){
            return null;
        }
        topicChildren.setTopicChildrenName(request.getTopicChildrenName());
        topicChildren.setTopicParentId(topicParent);
        topicChildren.setCreatedAt(LocalDateTime.now());
        topicChildren.setUpdatedAt(LocalDateTime.now());
        return topicChildrenRepository.save(topicChildren);
    }
}
