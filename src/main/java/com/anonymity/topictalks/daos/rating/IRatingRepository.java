package com.anonymity.topictalks.daos.rating;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.payloads.responses.HotTopicResponse;
import com.anonymity.topictalks.models.persists.rating.RatingKey;
import com.anonymity.topictalks.models.persists.rating.RatingPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.models.persists.user.FriendListPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.daos.rating
 * - Created At: 27-11-2023 17:41:37
 * @since 1.0 - version of class
 */

@Repository
public interface IRatingRepository extends IBaseRepository<RatingPO, RatingKey> {

    @Query(value = "SELECT rating FROM rating WHERE user_id = :userId AND topic_children_id = :tpcId", nativeQuery = true)
    Integer getRating(int userId, int tpcId);

    @Query(value = "SELECT * FROM rating WHERE user_id = :userId AND topic_children_id = :tpcId", nativeQuery = true)
    RatingPO getRatingUpdate(int userId, int tpcId);

    @Query(value = "SELECT * FROM rating r WHERE r.user_id= :userId", nativeQuery = true)
    List<RatingPO> findAllByUserInfo(Long userId);

    List<RatingPO> findByTopicChildrenInfo(TopicChildrenPO topicChildrenPO);

    List<RatingPO> findByUserInfo(UserPO userPO);

    @Query(value = "SELECT r.topic_children_id AS topicChildrenId, " +
            "COUNT(r.topic_children_id) AS tpcCount, " +
            "MAX(r.rating) AS maxRating, " +
            "AVG(r.rating) AS avgRating, " +
            "t.created_at, t.updated_at, t.image, t.topic_children_name, t.short_descript " +
            "FROM rating r " +
            "JOIN topic_children t ON r.topic_children_id = t.topic_children_id " +
            "GROUP BY r.topic_children_id " +
            "ORDER BY tpcCount DESC, maxRating DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> findTopRatedTopicsRaw();

    @Query(value = "SELECT r.topic_children_id AS topicChildrenId, " +
            "AVG(r.rating) AS avgRating " +
            "FROM rating r " +
            "GROUP BY r.topic_children_id " +
            "HAVING r.topic_children_id= :topicChildrenId ", nativeQuery = true)
    Object[] findAvgRatings(Long topicChildrenId);

}
