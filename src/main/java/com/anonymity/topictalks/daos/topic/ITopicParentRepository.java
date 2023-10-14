package com.anonymity.topictalks.daos.topic;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.topic
 * - Created At: 15-09-2023 09:34:47
 * @since 1.0 - version of class
 */

@Repository
public interface ITopicParentRepository extends IBaseRepository<TopicParentPO, Long> {

    Optional<TopicParentPO> findById(Long id);

    @Query(value = "SELECT * FROM topic_parent t WHERE t.topic_parent_name= :topicParentName", nativeQuery = true)
    List<TopicParentPO> findByTopicParentName(@Param(value = "topicParentName") String topicParentName);




}
