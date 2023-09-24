package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessageDemoPO;
import com.anonymity.topictalks.services.IConversationService;
import com.anonymity.topictalks.services.IMessageDemoService;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {


    private final IMessageDemoService messageService;
    private final IConversationService conversationService;


    public void sendSocketMessage(SocketIOClient senderClient, MessageDemoPO message, Long conversationId) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(conversationId.toString()).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message",
                        message);
            }
        }
    }

    public void saveMessage(SocketIOClient senderClient, MessageDemoPO message) {
        MessageDemoPO storedMessage = messageService.saveMessage(MessageDemoPO.builder()
                .content(message.getContent())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .build());
        sendSocketMessage(senderClient, storedMessage, Long.valueOf(message.getConversationId().toString()));
    }

    public void saveInfoMessage(SocketIOClient senderClient, String message, Long conversationId) {
        ConversationPO conversationPO = conversationService.getConversationById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        MessageDemoPO storedMessage = messageService.saveMessage(MessageDemoPO.builder()
                .content(message)
                .conversationId(conversationPO)
                .build());
        sendSocketMessage(senderClient, storedMessage, conversationId);
    }
}
