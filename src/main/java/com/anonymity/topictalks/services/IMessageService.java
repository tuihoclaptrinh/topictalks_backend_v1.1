package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.persists.message.MessagePO;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 16-09-2023 23:14:20
 * @since 1.0 - version of class
 */
public interface IMessageService {

    List<ReceiveMessageDTO> getMessages(Long conversationId);

    MessagePO saveMessage(MessagePO message);

}
