package com.anonymity.topictalks.daos.message;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.ParticipantKey;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.message
 * - Created At: 15-09-2023 09:41:17
 * @since 1.0 - version of class
 */

@Repository
public interface IParticipantRepository extends IBaseRepository<ParticipantPO, ParticipantKey> {
    List<ParticipantPO> findAllByConversationInfo(ConversationPO conversationId);

    List<ParticipantPO> findAllByUserInfo(UserPO userPO);

    @Query(value = "SELECT p.is_member, p.conversation_id, p.created_at, p.updated_at, p.user_id FROM participant p " +
            "JOIN conversation c ON p.conversation_id = c.conversation_id " +
            "WHERE c.is_group_chat= :isGroupChat", nativeQuery = true)
    List<ParticipantPO> findAllByIsGroupChat(@Param(value = "isGroupChat") boolean isGroupChat);

    boolean existsByConversationInfoAndUserInfoAndIsMember(ConversationPO conversationPO, UserPO userPO, boolean isMember);

    @Query(value = "SELECT a.user_id FROM participant a, conversation b WHERE b.conversation_id= :conversation_id AND a.conversation_id= :conversation_id AND a.user_id != :user_id", nativeQuery = true)
    List<Long> getPartnerIdByConversationIdAndUserId(@Param(value = "conversation_id") long conversation_id, @Param(value = "user_id") long user_id);

    ParticipantPO findByConversationInfoAndAndUserInfo(ConversationPO conversationPO, UserPO userPO);

    void deleteByConversationInfoAndUserInfo(ConversationPO conversationPO, UserPO userPO);
//    @Query(value = "SELECT * FROM participant p " +
//            "JOIN conversation c ON p.conversation_id = c.conversation_id " +
//            "WHERE c.topic_children_id = :tid AND c.is_group_chat = false AND p.user_id IN (:uid, :pid)", nativeQuery = true)
//    List<ParticipantPO> getParticipantsByTopicChildenIdAndUserIds(@Param(value = "tid") long topicId, @Param(value = "uid") long userId,@Param(value = "pid") long partnerId);
}
