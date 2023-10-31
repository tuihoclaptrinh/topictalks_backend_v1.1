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
        if (!list.isEmpty()) {
            return null;
        }
        topicChildren.setTopicChildrenName(request.getTopicChildrenName());
        topicChildren.setTopicParentId(topicParent);
        topicChildren.setShortDescript(request.getShortDescription());
        topicChildren.setImage(request.getImage());
        topicChildren.setCreatedAt(LocalDateTime.now());
        topicChildren.setUpdatedAt(LocalDateTime.now());
        return topicChildrenRepository.save(topicChildren);
    }

    @Override
    public List<TopicChildrenPO> getTopicChildrenByTopicParentId(long parentTopicId) {
        return topicChildrenRepository.findByTopicParentId(parentTopicId);
    }

    @Override
    public TopicChildrenPO getTopicChildrenById(long TopicChildrenId) {
        return topicChildrenRepository.findById(TopicChildrenId);
    }

    @Override
    public TopicChildrenPO updateTopicName(long id, String newName) {
        TopicChildrenPO topicChildrenPO = topicChildrenRepository.findById(id);
        if (topicChildrenPO != null) {
            topicChildrenPO.setId(id);
            topicChildrenPO.setTopicChildrenName(newName);
            topicChildrenPO.setUpdatedAt(LocalDateTime.now());
            return topicChildrenRepository.save(topicChildrenPO);
        }
        return null;
    }

    @Override
    public boolean checkDuplicateTopicName(String newName, long topicParentId) {
        return topicChildrenRepository.findByTopicChildrenNameAndTopicParentId(newName, topicParentId).size() > 0 ? true : false;
    }
}
