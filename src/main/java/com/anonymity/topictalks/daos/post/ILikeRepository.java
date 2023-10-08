package com.anonymity.topictalks.daos.post;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.post.LikeKey;
import com.anonymity.topictalks.models.persists.post.LikePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.post
 * - Created At: 15-09-2023 09:38:15
 * @since 1.0 - version of class
 */

@Repository
public interface ILikeRepository extends IBaseRepository<LikePO, LikeKey> {
    boolean existsByUserIdAndPostId(long userId, long postId);
    void deleteByUserIdAndPostId(long userId, long postId);
    void removeByPostId(long postId);
    List<LikePO> getAllByPostId(long postId);
}
