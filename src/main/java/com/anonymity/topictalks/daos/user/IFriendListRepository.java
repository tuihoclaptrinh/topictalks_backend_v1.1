package com.anonymity.topictalks.daos.user;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.user.FriendListPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.user
 * - Created At: 15-09-2023 09:33:01
 * @since 1.0 - version of class
 */

@Repository
public interface IFriendListRepository extends IBaseRepository<FriendListPO, Long> {
    @Query(value = "SELECT * FROM friend_list f WHERE f.user_id= :userId", nativeQuery = true)
    List<FriendListPO> findAllByUserId(@Param(value = "userId") long userId);

    @Query(value = "SELECT * FROM friend_list f WHERE f.user_id= :userId or f.friend_id = :friendId ", nativeQuery = true)
    List<FriendListPO> findAllByUserIdAndFriendId(@Param(value = "userId") long userId, @Param(value = "friendId") long friendId);

    @Query(value = "SELECT * FROM `topic-talks-app`.friend_list f WHERE (f.user_id= :userId AND f.friend_id = :friendId) OR (f.user_id= :friendId AND f.friend_id = :userId) ", nativeQuery = true)
    FriendListPO findByUserIdAndFriendId(@Param(value = "userId") long userId, @Param(value = "friendId") long friendId);


}
