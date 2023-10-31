package com.anonymity.topictalks.daos.notification;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.notification.MessageNotificationPO;
import com.anonymity.topictalks.models.persists.notification.PostNotificationPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.notification
 * - Created At: 15-09-2023 09:39:24
 * @since 1.0 - version of class
 */

@Repository
public interface IMessageNotificationRepository extends IBaseRepository<MessageNotificationPO, Long> {
    @Query(value = "SELECT * FROM message_notification m WHERE m.user_id = :userId", nativeQuery = true)
    List<MessageNotificationPO> findAllByUserId(@Param(value = "userId") long userId);
}
