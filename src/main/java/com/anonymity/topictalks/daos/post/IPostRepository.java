package com.anonymity.topictalks.daos.post;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.post.PostPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
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
    Page<PostPO> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM post p WHERE p.author_id = :authorId", nativeQuery = true)
    List<PostPO> findByAuthorId(@Param(value = "authorId") long authorId);

    @Query(value = "SELECT * FROM post p WHERE p.author_id = :authorId AND p.is_approved = :isApproved", nativeQuery = true)
    List<PostPO> findByAuthorIdAndIsApproved(@Param(value = "authorId") long authorId, @Param(value = "isApproved") boolean isApproved);

    @Query(value = "SELECT * FROM post p WHERE p.author_id = :authorId AND p.is_approved = :isApproved AND p.status_id=1", nativeQuery = true)
    List<PostPO> findByAuthorIdAndIsApprovedAndStatusId(@Param(value = "authorId") long authorId, @Param(value = "isApproved") boolean isApproved);


    int countByIsApproved(boolean isApproved);

    Page<PostPO> findAllByIsApprovedOrderByCreatedAtDesc(boolean isApproved, Pageable pageable);

    @Query(value = "SELECT p.post_id, p.created_at, p.updated_at, p.content, p.image, p.is_approved, p.title, p.author_id, p.topic_parent_id, p.status_id " +
            "FROM post p " +
            "LEFT JOIN `like` l ON p.post_id = l.post_id " +
            "LEFT JOIN comment c ON p.post_id = c.post_id " +
            "WHERE p.is_approved= :isApproved AND p.status_id= :statusId " +
            "GROUP BY p.post_id, p.title " +
            "ORDER BY COUNT(DISTINCT l.user_id) DESC, COUNT(DISTINCT c.comment_id) DESC " +
            "LIMIT 4",nativeQuery = true)
    List<PostPO> findTop4ByIsApproved(@Param(value = "isApproved") boolean isApproved, @Param(value = "statusId") int statusId);

    @Query(value = "SELECT * FROM post p WHERE p.topic_parent_id = :topic_parent_id AND p.is_approved= :is_approved ORDER BY p.created_at DESC", nativeQuery = true)
    Page<PostPO> findByTopicParentId(@Param(value = "topic_parent_id") long topicParentId,@Param(value = "is_approved") boolean isApproved, Pageable pageable);

    @Query(value = "SELECT * FROM post p WHERE p.post_id = :post_id AND p.is_approved = :is_approved", nativeQuery = true)
    PostPO findByIdAndIsApproved(@Param(value = "post_id") long postId, @Param(value = "is_approved") boolean isApproved);

    @Query(value = "SELECT * FROM post p" +
            "JOIN status s ON p.status_id = s.id AND s.id in (1,2) AND p.author_id= :friendId AND p.is_approved=true;", nativeQuery = true)
    List<PostPO> findByFriendId(@Param(value = "friendId") long friendId);

    @Query(value = "SELECT CONCAT(tp.topic_parent_id, ':',tp.topic_parent_name,':', COUNT(*)) as topic_parent_count " +
            "FROM post p " +
            "JOIN topic_parent tp ON p.topic_parent_id = tp.topic_parent_id " +
            "GROUP BY tp.topic_parent_id, tp.topic_parent_name " +
            "ORDER BY tp.topic_parent_id ASC", nativeQuery = true)
    List<String> getListTopicAndCount();


}
