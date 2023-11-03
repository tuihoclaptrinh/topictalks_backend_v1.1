package com.anonymity.topictalks.daos.message;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface IMessageRepository extends IBaseRepository<MessagePO, Long> {

    List<MessagePO> findAllByConversationId(ConversationPO conversation);

    @Query(value = "SELECT * FROM message m WHERE m.conversation_id = :id ORDER BY created_at DESC LIMIT 1;", nativeQuery = true)
    MessagePO getLastMessageByConversationId(@Param(value = "id") long conversationId);

}
