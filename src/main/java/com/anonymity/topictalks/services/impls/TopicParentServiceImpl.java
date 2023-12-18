package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.post.IPostRepository;
import com.anonymity.topictalks.daos.topic.ITopicParentRepository;
import com.anonymity.topictalks.models.dtos.ChartTopicInforDTO;
import com.anonymity.topictalks.models.payloads.requests.TopicRequest;
import com.anonymity.topictalks.models.payloads.responses.TopicParentResponse;
import com.anonymity.topictalks.models.persists.topic.QTopicParentPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.services.ITopicParentService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TopicParentServiceImpl implements ITopicParentService {

    @Autowired
    private ITopicParentRepository topicParentRepository;

    @Autowired
    private IConversationRepository conversationRepository;

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public TopicParentPO create(TopicRequest request) {
        TopicParentPO topicParent = new TopicParentPO();
        List<TopicParentPO> list = topicParentRepository.findByTopicParentName(request.getTopicName());
        if (!list.isEmpty()) return null;
        topicParent.setTopicParentName(request.getTopicName());
        topicParent.setImage(request.getUrlImage());
        topicParent.setShortDescript(request.getShortDescript());
        topicParent.setExpired(false);
        topicParent.setCreatedAt(LocalDateTime.now());
        topicParent.setUpdatedAt(LocalDateTime.now());
        return topicParentRepository.save(topicParent);
    }

    @Override
    public TopicParentPO update(Long id, TopicRequest request) {
        TopicParentPO topicParentPO = topicParentRepository.findById(id).orElse(null);
        if (topicParentPO != null) {
            topicParentPO.setTopicParentName(request.getTopicName());
            topicParentPO.setImage(request.getUrlImage());
            topicParentPO.setShortDescript(request.getShortDescript());
            topicParentPO.setUpdatedAt(LocalDateTime.now());
            return topicParentRepository.save(topicParentPO);
        }
        return null;
    }

    @Override
    public List<TopicParentPO> getAll() {
        return topicParentRepository.findAll();

    }

    @Override
    public List<TopicParentPO> getAllByIsExpired(boolean isExpired) {
        return topicParentRepository.findAllByIsExpired(isExpired);
    }

    @Override
    public TopicParentPO updateTopicName(long id, String newName) {
        TopicParentPO topicParentPO = topicParentRepository.findById(id).orElse(null);
        if (topicParentPO != null) {
            topicParentPO.setId(id);
            topicParentPO.setTopicParentName(newName);
            topicParentPO.setUpdatedAt(LocalDateTime.now());
            return topicParentRepository.save(topicParentPO);
        }
        return null;
    }

    @Override
    public TopicParentPO updateIsExpiredById(long id, boolean isExpired) {
        TopicParentPO topicParentPO = topicParentRepository.findById(id).orElse(null);
        if (topicParentPO != null) {
            topicParentPO.setId(id);
            topicParentPO.setExpired(isExpired);
            topicParentPO.setUpdatedAt(LocalDateTime.now());
            return topicParentRepository.save(topicParentPO);
        }
        return null;
    }

    @Override
    public boolean checkDuplicateTopicName(String newName, long id) {
        return topicParentRepository.findByTopicParentNameAndAndId(newName,id).size() > 0 ? true : false;
    }

    @Override
    public List<TopicParentPO> searchByTopicParentName(String topicParentName, boolean isExpired) {
        return topicParentRepository.findByTopicParentNameContainingIgnoreCase(topicParentName, isExpired);
    }

    @Override
    public List<ChartTopicInforDTO> retrieveDataForTopicParent() {
        List<ChartTopicInforDTO> response = new ArrayList<>();
        List<String> groupChatList = conversationRepository.getAllTopicAndCount();
        List<String> postList = postRepository.getListTopicAndCount();
        List<String> listTopicParent = topicParentRepository.getListTopicParentId();

        for (String topicId : listTopicParent) {
            int totalPost = 0;
            int totalGroupChat = 0;
            String[] param = topicId.split(":");
            ChartTopicInforDTO dto = new ChartTopicInforDTO();
            dto.setTopicId(Integer.valueOf(param[0]));
            dto.setTopicName(param[1]);

            for (String item : groupChatList) {
                String[] param1 = item.split(":");
                if (param[0].equals(param1[0])) {
                    totalGroupChat = totalGroupChat + Integer.valueOf(param1[2]);
                }
            }
            dto.setTotalGroupChat(totalGroupChat);

            for (String item : postList) {
                String[] param2 = item.split(":");
                if (param[0].equals(param2[0])) {
                    totalPost = totalPost + Integer.valueOf(param2[2]);
                }
            }
            dto.setTotalPost(totalPost);
            response.add(dto);
        }
        return response;
    }

    /**
     * @return
     */
    @Override
    public List<TopicParentResponse> getAllTopicParent() {
        QTopicParentPO qTopicParentPO = QTopicParentPO.topicParentPO;

        List<TopicParentResponse> topicParentResponse = jpaQueryFactory.select(
                        Projections.bean(TopicParentResponse.class,
                                qTopicParentPO.id,
                                qTopicParentPO.topicParentName)
                ).where(qTopicParentPO.isExpired.eq(false))
                .from(qTopicParentPO)
                .fetch();

        return topicParentResponse;
    }
}
