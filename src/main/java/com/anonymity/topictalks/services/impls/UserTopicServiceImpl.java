package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.topic.ITopicParentRepository;
import com.anonymity.topictalks.daos.topic.IUserTopicRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.payloads.requests.UserTopicRequest;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.models.persists.topic.UserTopicPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IUserTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserTopicServiceImpl implements IUserTopicService {
    @Autowired
    private IUserTopicRepository userTopicRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ITopicParentRepository topicParentRepository;

    @Override
    public List<UserTopicPO> createUserTopic(Long userId, UserTopicRequest request) {
        List<UserTopicPO> list = new ArrayList<>();
        for (int i = 0; i < request.getParentTopicIdList().size(); i++) {
            UserTopicPO userTopicPO = new UserTopicPO();
            userTopicPO.setUserId(userId);
            userTopicPO.setTopicParentId(request.getParentTopicIdList().get(i));
            UserPO userPO = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User doesn't exist."));
            userTopicPO.setUserInfo(userPO);
            TopicParentPO topicParentPO = topicParentRepository.findById(request.getParentTopicIdList().get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Topic parent doesn't exist."));;
            userTopicPO.setTopicParentInfo(topicParentPO);
            userTopicPO.setCreatedAt(LocalDateTime.now());
            userTopicPO.setUpdatedAt(LocalDateTime.now());
            list.add(userTopicRepository.save(userTopicPO));
        }
        return list;
    }
}
