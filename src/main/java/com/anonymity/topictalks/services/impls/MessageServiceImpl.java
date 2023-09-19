package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.services.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 16-09-2023 23:14:08
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final IMessageRepository messageRepository;
    private final IConversationRepository conversationRepository;

    /**
     * @param conversationId
     * @return
     */
    @Override
    public List<MessagePO> getMessages(Long conversationId) {
        return messageRepository.findAllByConversationId(conversationRepository.findById(conversationId).get());
    }

    /**
     * @param message
     * @return
     */
    @Override
    public MessagePO saveMessage(MessagePO message) {
        return messageRepository.save(message);
    }
}
