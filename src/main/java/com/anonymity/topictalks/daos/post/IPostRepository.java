package com.anonymity.topictalks.daos.post;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.post.PostPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.post
 * - Created At: 15-09-2023 09:38:35
 * @since 1.0 - version of class
 */

@Repository
public interface IPostRepository extends IBaseRepository<PostPO, Long> {
    @Query(value = "SELECT * FROM post p WHERE p.author_id = :authorId", nativeQuery = true)
    List<PostPO> findByAuthorId(@Param(value = "authorId") long authorId);

    @Query(value = "SELECT * FROM post p WHERE p.author_id = :authorId AND p.is_approved = :isApproved", nativeQuery = true)
    List<PostPO> findByAuthorIdAndIsApproved(@Param(value = "authorId") long authorId, @Param(value = "isApproved") boolean isApproved);

    List<PostPO> findAllByIsApproved(boolean isApproved);

    @Query(value = "SELECT * FROM post p WHERE p.topic_parent_id = :topic_parent_id", nativeQuery = true)
    List<PostPO> findByTopicParentId(@Param(value = "topic_parent_id") long topic_parent_id);
}
