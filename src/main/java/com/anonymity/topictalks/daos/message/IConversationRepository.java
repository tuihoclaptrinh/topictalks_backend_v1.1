package com.anonymity.topictalks.daos.message;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.message
 * - Created At: 15-09-2023 09:40:22
 * @since 1.0 - version of class
 */

@Repository
public interface IConversationRepository extends IBaseRepository<ConversationPO, Long> {
    @Query(value = "SELECT p1.conversation_id " +
            "FROM participant p1 " +
            "JOIN participant p2 ON p1.conversation_id = p2.conversation_id " +
            "JOIN conversation c ON p1.conversation_id = c.conversation_id " +
            "WHERE p1.user_id = :userId1 " +
            "AND p2.user_id = :userId2 " +
            "AND c.is_group_chat = :isGroupChat", nativeQuery = true)
    List<Long> checkMatchingConversations(@Param(value = "userId1") long userId1, @Param(value = "userId2") long userId2, @Param(value = "isGroupChat") boolean isGroupChat);

    Page<ConversationPO> findAllByTopicChildrenAndIsGroupChat(TopicChildrenPO topicChildrenPO, boolean isGroupChat, Pageable pageable);

    @Query(value = "SELECT c.conversation_id, c.created_at, c.updated_at, c.admin_id, c.chat_name, c.is_group_chat, c.topic_children_id, c.avt_group_img FROM conversation c " +
            "LEFT JOIN participant p ON c.conversation_id = p.conversation_id " +
            "where c.is_group_chat= :is_group_chat " +
            "GROUP BY c.conversation_id " +
            "ORDER BY COUNT(p.user_id) DESC", nativeQuery = true)
    Page<ConversationPO> findAllByIsGroupChat(@Param(value = "is_group_chat") boolean isGroupChat, Pageable pageable);

    @Query(value = "SELECT c.conversation_id, c.created_at, c.updated_at, c.chat_name, c.is_group_chat, c.topic_children_id, c.admin_id, c.avt_group_img " +
            "FROM conversation c " +
            "JOIN participant p ON c.conversation_id = p.conversation_id " +
            "WHERE p.user_id = :userId AND c.is_group_chat = :isGroupChat", nativeQuery = true)
    List<ConversationPO> findAllByUserIdAndIsGroupChat(@Param(value = "userId") Long userId, @Param(value = "isGroupChat") boolean isGroupChat);

    @Query(value = "SELECT CONCAT(tp.topic_parent_id, ':',tp.topic_parent_name,':', COUNT(tc.topic_children_id)) as topic_parent_count " +
            "FROM conversation c " +
            "JOIN topic_children tc ON c.topic_children_id = tc.topic_children_id " +
            "JOIN topic_parent tp ON tc.topic_parent_id = tp.topic_parent_id " +
            "WHERE c.is_group_chat = true " +
            "GROUP BY tp.topic_parent_id, tp.topic_parent_name " +
            "ORDER BY tp.topic_parent_id ASC", nativeQuery = true)
    List<String> getAllTopicAndCount();


}
