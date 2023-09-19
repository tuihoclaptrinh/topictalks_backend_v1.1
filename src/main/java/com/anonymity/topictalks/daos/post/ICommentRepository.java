package com.anonymity.topictalks.daos.post;

import com.anonymity.topictalks.models.persists.post.CommentPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.post
 * - Created At: 15-09-2023 09:36:21
 * @since 1.0 - version of class
 */

@Repository
public interface ICommentRepository extends JpaRepository<CommentPO, Long> {
}
