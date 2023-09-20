package com.anonymity.topictalks.daos.notification;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.notification.MessageNotificationPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.notification
 * - Created At: 15-09-2023 09:39:24
 * @since 1.0 - version of class
 */

@Repository
public interface IMessageNotificationRepository extends IBaseRepository<MessageNotificationPO, Long> {
}
