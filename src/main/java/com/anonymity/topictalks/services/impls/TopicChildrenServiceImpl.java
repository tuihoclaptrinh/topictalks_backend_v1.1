package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.topic.ITopicParentRepository;
import com.anonymity.topictalks.models.payloads.requests.TopicChildrenRequest;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.services.ITopicChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<TopicChildrenPO> getTopicChildrenByTopicParentIdAndIsExpired(long parentTopicId, boolean isExpired, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return topicChildrenRepository.findByTopicParentIdAndIsExpired(parentTopicId, isExpired, pageable);
    }

    @Override
    public TopicChildrenPO getTopicChildrenById(long TopicChildrenId) {
        return topicChildrenRepository.findById(TopicChildrenId);
    }

    @Override
    public TopicChildrenPO update(long id, String newName, String newDescription) {
        TopicChildrenPO topicChildrenPO = topicChildrenRepository.findById(id);
        if (topicChildrenPO != null) {
            topicChildrenPO.setId(id);
            if (newName!=null){
                topicChildrenPO.setTopicChildrenName(newName);
            }
            if (newDescription!=null){
                topicChildrenPO.setShortDescript(newDescription);
            }
            topicChildrenPO.setUpdatedAt(LocalDateTime.now());
            return topicChildrenRepository.save(topicChildrenPO);
        }
        return null;
    }

    @Override
    public TopicChildrenPO updateIsExpiredById(long id, boolean isExpired) {
        TopicChildrenPO topicChildrenPO = topicChildrenRepository.findById(id);
        if (topicChildrenPO != null) {
            topicChildrenPO.setId(id);
            topicChildrenPO.setExpired(isExpired);
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
