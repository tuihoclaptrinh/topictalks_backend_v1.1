package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IMessageService;
import com.anonymity.topictalks.services.ISocketService;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 16-09-2023 23:19:19
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements ISocketService {

    private final IMessageService messageService;
    private final IConversationRepository conversationRepository;
    private final IUserRepository userRepository;

    /**
     * @param senderClient
     * @param message
     * @param conversation
     */
    @Override
    public void sendSocketMessage(SocketIOClient senderClient, MessagePO message, Long conversation) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(conversation.toString()).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message",
                        message);
            }
        }
    }

    /**
     * @param senderClient
     * @param message
     */
    @Override
    public void saveMessage(SocketIOClient senderClient, MessagePO message) {
        MessagePO storedMessage = messageService.saveMessage(MessagePO.builder()
                .content(message.getContent())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .build());
        sendSocketMessage(senderClient, storedMessage, message.getConversationId().getId());
    }

    /**
     * @param senderClient
     * @param message
     * @param conversation
     */
    @Override
    public void saveInfoMessage(SocketIOClient senderClient, String message, Long conversation, String username) {
        Optional<UserPO> userOptional = userRepository.findByUsername(username);
        Optional<ConversationPO> conversationOptional = conversationRepository.findById(conversation);

        if (userOptional.isPresent() && conversationOptional.isPresent()) {
            UserPO user = userOptional.get();
            ConversationPO conversationPO = conversationOptional.get();

            MessagePO storedMessage = messageService.saveMessage(MessagePO.builder()
                    .content(message)
                    .senderId(user)
                    .conversationId(conversationPO)
                    .build());

            sendSocketMessage(senderClient, storedMessage, conversationPO.getId());
        } else {
            // Handle the case when the user or conversation is not found.
            // You can log an error, return an error response, or take appropriate action.
        }
    }
}
