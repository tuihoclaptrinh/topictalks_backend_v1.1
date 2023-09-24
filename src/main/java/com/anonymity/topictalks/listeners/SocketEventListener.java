//package com.anonymity.topictalks.listeners;
//
//import com.anonymity.topictalks.daos.message.IConversationRepository;
//import com.anonymity.topictalks.daos.message.IMessageDemoRepository;
//import com.anonymity.topictalks.daos.message.IParticipantRepository;
//import com.anonymity.topictalks.daos.user.IUserRepository;
//import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
//import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
//import com.anonymity.topictalks.models.persists.message.MessageDemoPO;
//import com.anonymity.topictalks.services.IParticipantService;
//import com.anonymity.topictalks.services.ISocketService;
//import com.anonymity.topictalks.utils.RandomUserUtils;
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.annotation.OnConnect;
//import com.corundumstudio.socketio.annotation.OnDisconnect;
//import com.corundumstudio.socketio.annotation.OnEvent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author de140172 - author
// * @version 1.1 - version of software
// * - Package Name: com.anonymity.topictalks.listeners
// * - Created At: 21-09-2023 07:58:48
// * @since 1.0 - version of class
// */
//
//@Component
//@RequiredArgsConstructor
//@Transactional(rollbackFor = Throwable.class)
//public class SocketEventListener {
//
//    private Logger logger = LoggerFactory.getLogger(SocketEventListener.class);
//    public static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
//
//    private final IMessageDemoRepository messageRepository;
//    private final IUserRepository userRepository;
//    private final IConversationRepository conversationRepository;
//    private final RandomUserUtils randomUserUtils;
//    private final IParticipantService participantService;
//    private final ISocketService socketService;
//    private final IParticipantRepository participantRepository;
//
//    @OnConnect
//    public void  eventOnConnect(SocketIOClient client){
//        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
//        clientMap.put(urlParams.get("uid").get(0),client);
//        logger.info("link open, urlParams {}",urlParams);
//        logger.info("Number of people joining: {}",clientMap.size());
//    }
//
//    @OnDisconnect
//    public void  eventOnDisConnect(SocketIOClient client){
//        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
//        String moveUser = urlParams.get("uid").get(0);
//        clientMap.remove(moveUser);
//        logger.info("Link closed, urlParams {}",urlParams);
//        logger.info("Remaining number of people: {}",clientMap.size());
//    }
//
//    @OnEvent("sendMessage")
//    public void onSendMessage(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
//
//        MessageDemoPO messagePO = MessageDemoPO.builder()
//                .senderId(userRepository.findById(receiveMessageDTO.getUserId()).orElse(null))
//                .conversationId(conversationRepository.findById(receiveMessageDTO.getConversationId()).orElse(null))
//                .content(receiveMessageDTO.getData().get("message").toString())
//                .build();
//        messageRepository.save(messagePO);
//
//        if(true) {
//
//            var conversation = conversationRepository.getOne(receiveMessageDTO.getConversationId());
//            String[] split = participantService.getUserIdsByConversation(conversation).split(",");
//            Long userId = receiveMessageDTO.getUserId();
//            for (String s: split) {
//                if(StringUtils.isEmpty(s)){
//                    continue;
//                }
//                Long conversationOne = Long.valueOf(s);
//
//                if(!userId.equals(conversationOne)){
//                    SocketIOClient ioClient = clientMap.get(conversationOne.toString());
//                    if(null != ioClient){
//                        ioClient.sendEvent("sendMessage",receiveMessageDTO);
//                    }
//                }
//            }
//
//        } else {
//            SocketIOClient socketIOClient = clientMap.get(String.valueOf(receiveMessageDTO.getTargetId()));
//            if(!StringUtils.isEmpty(socketIOClient)){
//                logger.info("conversation user id {} online", receiveMessageDTO.getConversationId());
//                socketIOClient.sendEvent("sendMessage",receiveMessageDTO);
//            }else {
//                logger.info("conversation user id {} is not online", receiveMessageDTO.getConversationId());
//            }
//        }
//
//    }
//
//    @OnEvent("initChatSingle")
//    public void onInitiateChat(SocketIOClient client, ParticipantRequest request) {
//
//        // Create or retrieve a conversation for the 1-1 chat
//        socketService.creatChatSingle(client, request);
//
//    }
//
//    @OnEvent("onJoinRoom")
//    public void onJoinRoom(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO) {
//
//        logger.info("onJoinRoom {}",receiveMessageDTO);
//        if(true) {
//            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
//            if(null != socketIOClient){
//                socketIOClient.sendEvent("onJoinRoom",receiveMessageDTO);
//            }else {
//                logger.info("Offline users: {} ,UserId {}",receiveMessageDTO.getTargetId(),receiveMessageDTO.getTargetId());
//            }
//        }
//
//    }
//
//    @OnEvent("onLeftRoom")
//    public void onLeftRoom(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
//        logger.info("onLeftRoom {}",receiveMessageDTO);
//        if(true){
//            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
//            if(null != socketIOClient){
//                socketIOClient.sendEvent("onLeftRoom",receiveMessageDTO);
//            }else {
//                logger.info("Offline users：{} ,UserId {}",receiveMessageDTO.getTargetId(),receiveMessageDTO.getTargetId());
//            }
//        }
//    }
//
//}
