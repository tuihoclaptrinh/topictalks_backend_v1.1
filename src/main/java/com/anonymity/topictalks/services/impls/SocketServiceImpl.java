package com.anonymity.topictalks.services.impls;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.listeners.SocketEventListener;
import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.models.persists.message.ParticipantKey;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.services.IConversationService;
import com.anonymity.topictalks.services.IMessageService;
import com.anonymity.topictalks.services.ISocketService;
import com.anonymity.topictalks.utils.RandomUserUtils;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 23-09-2023 09:05:40
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements ISocketService {

    private final IMessageService messageService;
    private final IConversationRepository conversationRepository;
    private final IUserRepository userRepository;
    private final RandomUserUtils randomUserUtil;
    private final IConversationService conversationService;
    private final IParticipantRepository participantRepository;
    private Logger logger = LoggerFactory.getLogger(SocketServiceImpl.class);

    /**
     * @param senderClient
     * @param receiveMessageDTO
     */
    @Override
    public void sendSocketMessage(SocketIOClient senderClient, ReceiveMessageDTO receiveMessageDTO) {
        for ( // getRoomOperations(receiveMessageDTO.getConversationId().toString()).getClients()
                SocketIOClient client : senderClient.getNamespace().getAllClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                logger.info("something on : {}", !client.getSessionId().equals(senderClient.getSessionId()));
                client.sendEvent("readMessage",
                        receiveMessageDTO);
            }
        }
    }

    /**
     * @param senderClient
     * @param receiveMessageDTO
     */
    @Override
    public void saveMessage(SocketIOClient senderClient, ReceiveMessageDTO receiveMessageDTO) {
        MessagePO messagePO = MessagePO.builder()
                .senderId(userRepository.findById(receiveMessageDTO.getUserId()).orElse(null))
                .conversationId(conversationRepository.findById(receiveMessageDTO.getConversationId()).orElse(null))
                .content(receiveMessageDTO.getData().get("message").toString())
                .build();
        messageService.saveMessage(messagePO);
        sendSocketMessage(senderClient, receiveMessageDTO);
    }

    /**
     * @param client
     * @param participantRequest
     */
    @Override
    public void creatChatSingle(SocketIOClient client, ParticipantRequest participantRequest) {

        Map<Long, Long> result = randomUserUtil.randomUserChatting(participantRequest);

        var conversationRequest = ConversationRequest.builder()
                .chatName(null)
                .topicChildrenId(participantRequest.getTopicChildId())
                .build();

        for(Map.Entry<Long, Long> entry: result.entrySet()) {

            var conversationResponse = conversationService.createConversation(conversationRequest,false);

            var key = new ParticipantKey();
            var participant = ParticipantPO.builder()
                    .id(key)
                    .userInfo(userRepository.findById(entry.getKey()).orElse(null))
                    .conversationInfo(conversationRepository.findById(conversationResponse.getConversationId()).orElse(null))
                    .build();
            participantRepository.save(participant);

//            client.joinRoom(participant.getConversationInfo().getId().toString());
            logger.info("Receive: {}", participant.getConversationInfo().getId().toString());
            client.joinRoom(participant.getConversationInfo().getId().toString());
            logger.info("Socket ID[{}] - conversation[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), participant.getConversationInfo().getId(), participant.getUserInfo().getNickName());
            var key2 = new ParticipantKey();
            var participant2 = ParticipantPO.builder()
                    .id(key2)
                    .userInfo(userRepository.findById(entry.getValue()).orElse(null))
                    .conversationInfo(conversationRepository.findById(conversationResponse.getConversationId()).orElse(null))
                    .build();
            participantRepository.save(participant2);

            logger.info("Socket ID[{}] - conversation[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), participant.getConversationInfo().getId(), participant2.getUserInfo().getNickName());

            sendSocketMessage(client,
                    ReceiveMessageDTO.builder()
                            .conversationId(conversationResponse.getConversationId())
                            .build());

        }

    }
}
