package com.anonymity.topictalks.daos.message;

import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.message
 * - Created At: 15-09-2023 09:40:48
 * @since 1.0 - version of class
 */

@Repository
public interface IMessageRepository extends JpaRepository<MessagePO, Long> {

    List<MessagePO> findAllByConversationId(ConversationPO conversation);

}
