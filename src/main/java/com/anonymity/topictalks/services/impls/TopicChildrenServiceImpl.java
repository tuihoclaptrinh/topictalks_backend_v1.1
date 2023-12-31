package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.topic.ITopicParentRepository;
import com.anonymity.topictalks.models.payloads.requests.TopicChildrenRequest;
import com.anonymity.topictalks.models.payloads.requests.TopicRequest;
import com.anonymity.topictalks.models.payloads.responses.TopicChildrenResponse;
import com.anonymity.topictalks.models.payloads.responses.TopicParentResponse;
import com.anonymity.topictalks.models.persists.topic.QTopicChildrenPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.services.ITopicChildrenService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

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
        return topicChildrenRepository.findById(TopicChildrenId).orElseThrow();
    }

    @Override
    public TopicChildrenPO update(long id, TopicRequest request) {
        TopicChildrenPO topicChildrenPO = topicChildrenRepository.findById(id).orElseThrow();
        if (topicChildrenPO != null) {
            topicChildrenPO.setTopicChildrenName(request.getTopicName());
            topicChildrenPO.setImage(request.getUrlImage());
            topicChildrenPO.setShortDescript(request.getShortDescript());
            topicChildrenPO.setUpdatedAt(LocalDateTime.now());
            return topicChildrenRepository.save(topicChildrenPO);
        }
        return null;
    }

    @Override
    public TopicChildrenPO updateIsExpiredById(long id, boolean isExpired) {
        TopicChildrenPO topicChildrenPO = topicChildrenRepository.findById(id).orElseThrow();
        if (topicChildrenPO != null) {
            topicChildrenPO.setId(id);
            topicChildrenPO.setExpired(isExpired);
            topicChildrenPO.setUpdatedAt(LocalDateTime.now());
            return topicChildrenRepository.save(topicChildrenPO);
        }
        return null;
    }

    @Override
    public boolean checkDuplicateTopicName(String newName, long topicChildrenId,long topicParentId) {
        return topicChildrenRepository.findByIdAndTopicChildrenNameAndTopicParentId(newName, topicParentId,topicChildrenId).size() > 0 ? true : false;
    }

    @Override
    public Page<TopicChildrenPO> searchByTopicChildrenName(String topicChildrenName, boolean isExpired, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return topicChildrenRepository.findByTopicChildrenNameContainingIgnoreCase(topicChildrenName, isExpired, pageable);
    }

    /**
     * @param tppId
     * @return
     */
    @Override
    public List<TopicChildrenResponse> listsByParentId(Long tppId) {
        QTopicChildrenPO qTopicChildrenPO = QTopicChildrenPO.topicChildrenPO;

        List<TopicChildrenResponse> topicChildrenResponses = jpaQueryFactory.select(
                        Projections.bean(TopicChildrenResponse.class,
                                qTopicChildrenPO.id,
                                qTopicChildrenPO.topicChildrenName)
                ).where(qTopicChildrenPO.topicParentId.id.eq(tppId))
                .from(qTopicChildrenPO)
                .fetch();

        return topicChildrenResponses;
    }
}
