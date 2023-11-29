package com.anonymity.topictalks.daos.post;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.post.CommentPO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.post
 * - Created At: 15-09-2023 09:36:21
 * @since 1.0 - version of class
 */

@Repository
public interface ICommentRepository extends IBaseRepository<CommentPO, Long> {
    List<CommentPO> findAllByPostId(PostPO postPO);

    boolean existsByIdAndUserId(Long id, UserPO userPO);

    @Query(value = "SELECT * FROM Comment " +
            "WHERE post_id = :postId " +
            "ORDER BY created_at DESC " +
            "LIMIT 1", nativeQuery = true)
    CommentPO getLastCommentByPostId(@Param(value = "postId") long postId);
}
