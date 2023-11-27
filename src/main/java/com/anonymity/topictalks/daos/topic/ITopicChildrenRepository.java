package com.anonymity.topictalks.daos.topic;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
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
 * - Package Name: com.anonymity.topictalks.daos.topic
 * - Created At: 15-09-2023 09:34:19
 * @since 1.0 - version of class
 */

@Repository
public interface ITopicChildrenRepository extends IBaseRepository<TopicChildrenPO, Long> {
    @Query(value = "SELECT * FROM topic_children t WHERE t.topic_children_name= :topicChildrenName AND t.topic_parent_id= :topicParentId", nativeQuery = true)
    List<TopicChildrenPO> findByTopicChildrenNameAndTopicParentId(@Param(value = "topicChildrenName") String topicChildrenName, @Param(value = "topicParentId") long topicParentId);

    @Query(value = "SELECT * FROM topic_children t WHERE t.topic_parent_id= :topicParentId", nativeQuery = true)
    List<TopicChildrenPO> findByTopicParentId(@Param(value = "topicParentId") long topicParentId);

    @Query(value = "SELECT * FROM topic_children t WHERE t.topic_parent_id= :topicParentId AND t.is_expired= :isExpired ORDER BY t.topic_children_id DESC", nativeQuery = true)
    Page<TopicChildrenPO> findByTopicParentIdAndIsExpired(@Param(value = "topicParentId") long topicParentId, @Param(value = "isExpired") boolean isExpired, Pageable pageable);

    TopicChildrenPO findById(long id);
}
