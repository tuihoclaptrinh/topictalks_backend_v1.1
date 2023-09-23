package com.anonymity.topictalks.listeners;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.services.IConversationService;
import com.anonymity.topictalks.services.IParticipantService;
import com.anonymity.topictalks.services.ISocketService;
import com.anonymity.topictalks.utils.RandomUserUtils;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.listeners
 * - Created At: 21-09-2023 07:58:48
 * @since 1.0 - version of class
 */

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class SocketEventListener {

    private Logger logger = LoggerFactory.getLogger(SocketEventListener.class);
    public static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;
    private final IConversationRepository conversationRepository;
    private final RandomUserUtils randomUserUtils;
    private final IParticipantService participantService;
    private final ISocketService socketService;

    @OnConnect
    public void  eventOnConnect(SocketIOClient client){
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        clientMap.put(urlParams.get("uid").get(0),client);
        logger.info("link open, urlParams {}",urlParams);
        logger.info("Number of people joining: {}",clientMap.size());
    }

    @OnDisconnect
    public void  eventOnDisConnect(SocketIOClient client){
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        String moveUser = urlParams.get("uid").get(0);
        clientMap.remove(moveUser);
        logger.info("Link closed, urlParams {}",urlParams);
        logger.info("Remaining number of people: {}",clientMap.size());
    }

    @OnEvent("sendMessage")
    public void onSendMessage(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){

        MessagePO messagePO = MessagePO.builder()
                .senderId(userRepository.findById(receiveMessageDTO.getUserId()).orElse(null))
                .conversationId(conversationRepository.findById(receiveMessageDTO.getConversationId()).orElse(null))
                .content(receiveMessageDTO.getData().get("message").toString())
                .build();
        messageRepository.save(messagePO);

        SocketIOClient socketIOClient = clientMap.get(String.valueOf(receiveMessageDTO.getTargetId()));
        if(!StringUtils.isEmpty(socketIOClient)){
            logger.info("conversation user id {} online", receiveMessageDTO.getConversationId());
            socketIOClient.sendEvent("readMessage",receiveMessageDTO);
        }else {
            logger.info("conversation user id {} is not online", receiveMessageDTO.getConversationId());
        }

//        logger.info("receiveMessageDTO {}",receiveMessageDTO);
//        socketService.saveMessage(client, receiveMessageDTO);

//        logger.info("receiveMessageDTO {}",receiveMessageDTO);
//        MessagePO messagePO = MessagePO.builder()
//                .senderId(userRepository.findById(receiveMessageDTO.getUserId()).orElse(null))
//                .conversationId(conversationRepository.findById(receiveMessageDTO.getConversationId()).orElse(null))
//                .content(receiveMessageDTO.getData().get("message").toString())
//                .build();
//        // Save message record
//        messageRepository.save(messagePO);
//
//        SocketIOClient socketIOClient = clientMap.get(String.valueOf(receiveMessageDTO.getConversationId()));
//
//        if(!StringUtils.isEmpty(socketIOClient)){
//            logger.info("conversation user id {} online", receiveMessageDTO.getConversationId());
//            socketIOClient.sendEvent("readMessage",receiveMessageDTO);
//        }else {
//            logger.info("conversation user id {} is not online", receiveMessageDTO.getConversationId());
//        }

    }

    @OnEvent("initChatSingle")
    public void onInitiateChat(SocketIOClient client, ParticipantRequest request) {


        // Create or retrieve a conversation for the 1-1 chat
        socketService.creatChatSingle(client, request);


        // Perform any additional logic as needed for each pair

    }

}
