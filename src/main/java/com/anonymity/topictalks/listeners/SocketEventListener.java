package com.anonymity.topictalks.listeners;

import com.alibaba.fastjson.JSONObject;
import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.ChatRandomDTO;
import com.anonymity.topictalks.models.dtos.EngagementChatDTO;
import com.anonymity.topictalks.models.dtos.PartnerDTO;
import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.requests.NotiRequest;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.payloads.responses.ParticipantRandomResponse;
import com.anonymity.topictalks.models.payloads.responses.ParticipantResponse;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IConversationService;
import com.anonymity.topictalks.services.INotificationService;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public static Map<String, SocketIOClient> clientChatRandom = new ConcurrentHashMap<>();
    public static Map<Long, Long> userChatRandom = new HashMap<>();

    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;
    private final IConversationRepository conversationRepository;
    private final IConversationService conversationService;
    private final RandomUserUtils randomUserUtils;
    private final IParticipantService participantService;
    private final ISocketService socketService;
    private final IParticipantRepository participantRepository;
    private final INotificationService notificationService;

    @OnConnect
    public void eventOnConnect(SocketIOClient client) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        clientMap.put(urlParams.get("uid").get(0),client);
        logger.info("link open, urlParams {}",urlParams);
        logger.info("Number of people joining: {}",clientMap.size());
    }

    @OnDisconnect
    public void eventOnDisConnect(SocketIOClient client) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        String moveUser = urlParams.get("uid").get(0);
        clientMap.remove(moveUser);
        logger.info("Link closed, urlParams {}", urlParams);
        logger.info("Remaining number of people: {}", clientMap.size());
    }

    @OnEvent("sendMessage")
    public void onSendMessage(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO) {
        NotiRequest notiRequest;
        MessagePO messagePO = MessagePO.builder()
                .senderId(userRepository.findById(receiveMessageDTO.getUserId()).orElse(null))
                .conversationId(conversationRepository.findById(receiveMessageDTO.getConversationId()).orElse(null))
                .content(receiveMessageDTO.getData().get("message").toString())
                .build();
        MessagePO messageSaved = messageRepository.save(messagePO);
        notiRequest = NotiRequest
                .builder()
                .messageId(messageSaved.getId())
                .userId(receiveMessageDTO.getUserId())
                .build();
        notificationService.saveNotiMessage(notiRequest);

        if (true) {
            var conversation = conversationRepository.getOne(receiveMessageDTO.getConversationId());
            String[] split = participantService.getUserIdsByConversation(conversation).split(",");
            Long userId = receiveMessageDTO.getUserId();
            UserPO userPO = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist"));
            receiveMessageDTO.setUsername(userPO.getUsername());
            receiveMessageDTO.setTimeAt(LocalDateTime.now().toString());
            for (String s : split) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                Long conversationOne = Long.valueOf(s);

                if (!userId.equals(conversationOne)) {
                    SocketIOClient ioClient = clientMap.get(conversationOne.toString());
                    if (null != ioClient) {
                        ioClient.sendEvent("sendMessage", receiveMessageDTO);
                        notiRequest = NotiRequest
                                .builder()
                                .messageId(messageSaved.getId())
                                .userId(userId)
                                .build();
                        notificationService.saveNotiMessage(notiRequest);
                    }
                }
            }

        } else {
            SocketIOClient socketIOClient = clientMap.get(String.valueOf(receiveMessageDTO.getTargetId()));
            if (!StringUtils.isEmpty(socketIOClient)) {
                logger.info("conversation user id {} online", receiveMessageDTO.getConversationId());
                socketIOClient.sendEvent("sendMessage", receiveMessageDTO);
            } else {
                logger.info("conversation user id {} is not online", receiveMessageDTO.getConversationId());
            }
        }

    }

    @OnEvent("initChatSingle")
    public void onInitiateChat(SocketIOClient client, ParticipantRequest request) {

        // Create or retrieve a conversation for the 1-1 chat
        socketService.creatChatSingle(client, request);

    }

    @OnEvent("onJoinRoom")
    public void onJoinRoom(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO) {

        logger.info("onJoinRoom {}", receiveMessageDTO);
        if (true) {
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if (null != socketIOClient) {
                socketIOClient.sendEvent("onJoinRoom", receiveMessageDTO);
            } else {
                logger.info("Offline users: {} ,UserId {}", receiveMessageDTO.getTargetId(), receiveMessageDTO.getTargetId());
            }
        }

    }

    @OnEvent("onLeftRoom")
    public void onLeftRoom(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO) {
        logger.info("onLeftRoom {}", receiveMessageDTO);
        if (true) {
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if (null != socketIOClient) {
                socketIOClient.sendEvent("onLeftRoom", receiveMessageDTO);
            } else {
                logger.info("Offline users：{} ,UserId {}", receiveMessageDTO.getTargetId(), receiveMessageDTO.getTargetId());
            }
        }
    }

    @OnEvent("1V1CommunicateVideo")
    public void on1V1CommunicateVideo(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("1V1CommunicateVideo {}",receiveMessageDTO);
        if(true) { // group friend type
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if(null != socketIOClient){
                socketIOClient.sendEvent("1V1CommunicateVideo",receiveMessageDTO);
            }else {
                logger.info("Not online user: {}, user Id {}",receiveMessageDTO.getTargetName(),receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
                socketIOClient02.sendEvent("notOnline","The other party is not online");
            }
        }
    }

    @OnEvent("1V1CommunicatePhone")
    public void on1V1CommunicatePhone(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("1V1CommunicatePhone {}",receiveMessageDTO);
        if(true) { // group friend type
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if(null != socketIOClient){
                socketIOClient.sendEvent("1V1CommunicatePhone",receiveMessageDTO);
            }else {
                logger.info("Not online user: {}, user Id {}",receiveMessageDTO.getTargetName(),receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
                socketIOClient02.sendEvent("notOnline","The other party is not online");
            }
        }
    }

    @OnEvent("ManyToManyCommunicateVideo")
    public void onManyToManyCommunicateVideo(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO){
        logger.info("ManyToManyCommunicateVideo {}",receiveMessageDTO);
        if(true) { // group friend type
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if(null != socketIOClient){
                socketIOClient.sendEvent("ManyToManyCommunicateVideo",receiveMessageDTO);
            }else {
                logger.info("Not online user: {}, user ID {}",receiveMessageDTO.getTargetName(),receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
                socketIOClient02.sendEvent("notOnline","The other party is not online");
            }
        }
    }

    @OnEvent("onAccessChatRandom")
    public void accessChatRandom(SocketIOClient client, ReceiveMessageDTO topicChildren) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        clientChatRandom.put(urlParams.get("uid").get(0) + "-" + topicChildren.getData().get("id").toString(),client);
        logger.info("link open, urlParams {}",urlParams);
        logger.info("Number of people joining to chat random: {}",clientChatRandom.size());

        var engagement = EngagementChatDTO.builder()
                .timeAccess(LocalDateTime.now().toString())
                .clientChatRandom(clientChatRandom.size())
                .topicChildrenId(Long.parseLong(topicChildren.getData().get("id").toString()))
                .urlParams(urlParams)
                .timeLeaved(null)
                .build();

//        userChatRandom.put(
//                Long.parseLong(engagement.getUrlParams().get("uid").get(0)),
//                engagement.getTimeAccess());

        client.sendEvent("userAccess",engagement);

        ParticipantRandomResponse p = onCreateChatRandom();

        if(p!=null) {
            for(PartnerDTO key: p.getPartnerDTO()) {
                logger.info("Something on if {}", String.valueOf(key.getId()));
                SocketIOClient client2 = clientMap.get(String.valueOf(key.getId()));
                client2.sendEvent("partiAccess", p);
            }
        }

    }

    @OnEvent("onLeaveChatRandom")
    public void leaveChatRandom(SocketIOClient client, ReceiveMessageDTO topicChildren) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        String moveUser = urlParams.get("uid").get(0);
        clientChatRandom.remove(moveUser + "-" + topicChildren.getData().get("id").toString(),client);
        logger.info("Link closed, urlParams {}", urlParams);
        logger.info("Remaining number of people chat random: {}", clientChatRandom.size());
        client.sendEvent("userLeaved",
                EngagementChatDTO.builder()
                        .timeAccess(null)
                        .clientChatRandom(clientChatRandom.size())
                        .urlParams(urlParams)
                        .timeLeaved(LocalDateTime.now().toString())
                        .build()
        );

    }

    @OnEvent("onCreateChatRandom")
    public ParticipantRandomResponse onCreateChatRandom() {

        Collection<String> keys = clientChatRandom.keySet();
        List<String> lists = new ArrayList<>();
        ParticipantRandomResponse participantRandomResponse = null;
        String prevTpcId = null;
        for (String key : keys) {
            String[] params = key.split("-");
            Long uId = Long.parseLong(params[0].trim());
            Long tpcId = Long.parseLong(params[1].trim());
            logger.info("userId: {} - topic Children Id: {}", uId, tpcId);
            lists.add(uId + "-" + tpcId);
            if (prevTpcId != null && prevTpcId.equals(tpcId.toString())) {

                participantRandomResponse = participantService.createChatRandom(
                        ChatRandomDTO
                                .builder()
                                .users(lists)
                                .tpcId(tpcId)
                                .build());

                for (String user: lists) {
                    clientChatRandom.remove(user);
                }
                lists.clear();
                logger.info("clear data on lists");
//                SocketIOClient client = clientChatRandom.get(key);
//                client.sendEvent("partiAccess", participantRandomResponse);

//                logger.info("Inside the same value userId: {} - topic Children Id: {}", uId, tpcId);
//                lists.forEach(System.out::println);

            }
            prevTpcId = tpcId.toString();
        }

//        for (String user: lists) {
//            clientChatRandom.remove(user);
//        }
//        lists.clear();
//        logger.info("clear data on lists");

        logger.info("Client remaining: {}", clientChatRandom.size());

//        Collection<SocketIOClient> values = clientChatRandom.values();
//        Collection<String> keys = clientChatRandom.keySet();
//
//        for (SocketIOClient client : values) {
//            logger.info(client.getHandshakeData().getUrlParams().get("uid").get(0));
//        }
//
//        Map<Long, Integer> tpcIdCountMap = new HashMap<>();
//
//        for (String key : keys) {
//            String[] params = key.split("-");
//            Long uId = Long.parseLong(params[0].trim());
//            Long tpcId = Long.parseLong(params[1].trim());
//            userChatRandom.put(uId, tpcId.toString());
//            // Check if the tpcId already exists in the map
//            if (tpcIdCountMap.containsKey(tpcId)) {
//                // Increment the count for this tpcId
//                int count = tpcIdCountMap.get(tpcId);
//                // Check if the count is now >= 2
//                if (count + 1 >= 2) {
//                    tpcIdCountMap.put(tpcId, count + 1);
//                }
//            }
//            else {
//                // If tpcId is not in the map, initialize its count to 1
//                tpcIdCountMap.put(tpcId, 1);
//            }
//            for (Map.Entry<Long, Integer> entry : tpcIdCountMap.entrySet()) {
//                logger.info("tpcId: {} - Count: {}", entry.getKey(), entry.getValue());
//                userChatRandom.put(entry.getKey(), entry.getValue().toString());
//            }
//            logger.info("User id: {}", uId);
//            logger.info("Topic Children id: {}", tpcId);
//        }
//
//        logger.info("Size chat random: {}", clientChatRandom.size());
//
//        for (Map.Entry<Long, String> entry : userChatRandom.entrySet()) {
//            Long key = entry.getKey();
//            String value = entry.getValue();
//            logger.info("Key: " + key + ", Value: " + value);
//        }

        return participantRandomResponse;

    }
}
