package com.anonymity.topictalks.listeners;

import com.alibaba.fastjson.JSONObject;
import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.*;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.requests.NotiRequest;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.payloads.responses.ParticipantRandomResponse;
import com.anonymity.topictalks.models.payloads.responses.ParticipantResponse;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.*;
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
    public static Map<String, SocketIOClient> clientJoinRoom = new ConcurrentHashMap<>();
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
    private final IUserService userService;

    @OnConnect
    public void eventOnConnect(SocketIOClient client) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        clientMap.put(urlParams.get("uid").get(0), client);
        String userId = urlParams.get("uid").get(0);
        userService.updateActive(true, Long.parseLong(userId));
        logger.info("link open, urlParams {}", urlParams);
        logger.info("Number of people joining: {}", clientMap.size());
    }

    @OnDisconnect
    public void eventOnDisConnect(SocketIOClient client) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        String moveUser = urlParams.get("uid").get(0);
        userService.updateActive(false, Long.parseLong(moveUser));
        clientMap.remove(moveUser);
        logger.info("Link closed, urlParams {}", urlParams);
        logger.info("Remaining number of people: {}", clientMap.size());
    }

    @OnEvent("sendMessage")
    public void onSendMessage(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO) {
        MessagePO messagePO = MessagePO.builder()
                .senderId(userRepository.findById(receiveMessageDTO.getUserId()).orElse(null))
                .conversationId(conversationRepository.findById(receiveMessageDTO.getConversationId()).orElse(null))
                .content(receiveMessageDTO.getData().get("message").toString())
                .build();
        MessagePO messageSaved = messageRepository.save(messagePO);
        if (true) {
            var conversation = conversationRepository.getOne(receiveMessageDTO.getConversationId());
//            String[] split = participantService.getUserIdsByConversation(conversation).split(",");
            List<Long> listUserIdInConversation = participantService.getListUserIdByConversation(conversation);
            Long userId = receiveMessageDTO.getUserId();
            UserPO userPO = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist"));
            receiveMessageDTO.setUsername(userPO.getUsername());
            receiveMessageDTO.setTimeAt(LocalDateTime.now().toString());
            List<Long> userOfflineList = new ArrayList<>();
//            Collection<String> keys = clientJoinRoom.keySet();
//            Stack<NotifiDTO> stack = new Stack<>();
//            for (String key : keys) {
//                String[] params = key.split("-");
//                NotifiDTO notiMessDTO = new NotifiDTO(
//                        Long.parseLong(params[0].trim()),
//                        Long.parseLong(params[1].trim())
//                );
//                stack.push(notiMessDTO);
//            }
            for (long s : listUserIdInConversation) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                Long userIdJoin = Long.valueOf(s);

                if (!userId.equals(userIdJoin)) {
                    SocketIOClient ioClient = clientMap.get(userIdJoin.toString());
                    if (ioClient != null) {
                        ioClient.sendEvent("sendMessage", receiveMessageDTO);
                    }
//                    boolean isUserOffline = true;
//                    for (NotifiDTO notiMessDTO : stack) {
//                        logger.info("notiMessDTO.getUserId() {}", notiMessDTO.getUserId());
//                        logger.info("notiMessDTO.getConversationId() {}, userIdJoin222 {},conversation.getId() {}", notiMessDTO.getConversationId(), userIdJoin,conversation.getId());
//                        if (notiMessDTO.getUserId() == userIdJoin && notiMessDTO.getConversationId() == conversation.getId()) {
//                            isUserOffline = false;
//                            break;
//                        }
//                    }


//
//                    if (isUserOffline) {
//                        userOfflineList.add(userIdJoin);
//                    }
                    if (receiveMessageDTO.getData().get("message").toString().contains("option_1410#$#")) {
                        userOfflineList.add(userIdJoin);
                    }
                }
            }
            for (Long user : userOfflineList) {
                logger.info("user {}", user);
                NotiRequest request = new NotiRequest();
                request.setConversationId(conversation.getId());
                request.setUserId(user);
                request.setMessageNoti(receiveMessageDTO.getData().get("message").toString());
                request.setPartnerId(userId);
                notificationService.saveNotification(request);
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

//    @OnEvent("onJoinRoom")
//    public void onJoinRoom(SocketIOClient client, Long conversationId) {
//        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
//        String userId = urlParams.get("uid").get(0);
//        clientJoinRoom.put(userId + "-" + conversationId.toString(), client);
//        logger.info("Link closed, urlParams {}, converId {}", urlParams, conversationId);
//        logger.info("Number of people joining to chat Room: {}", clientJoinRoom.size());
//    }

//    @OnEvent("onLeftRoom")
//    public void onLeftRoom(SocketIOClient client, Long conversationId) {
//        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
//        String userId = urlParams.get("uid").get(0);
//
//        for (Map.Entry<String, SocketIOClient> entry : clientJoinRoom.entrySet()) {
//            String key = entry.getKey();
//            String existingUserId = key.split("-")[0];
//            String exitingConverId = key.split("-")[1];
//            if (existingUserId.equals(userId) && exitingConverId.equals(conversationId.toString())) {
//                clientJoinRoom.remove(key);
//                break;
//            }
//        }
//
//        logger.info("Link closed, urlParams {}, converId {}", urlParams, conversationId);
//        logger.info("Remaining number of people in the chat Room: {}", clientJoinRoom.size());
//    }

    @OnEvent("1V1CommunicateVideo")
    public void on1V1CommunicateVideo(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO) {
        logger.info("1V1CommunicateVideo {}", receiveMessageDTO);
        if (true) { // group friend type
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if (null != socketIOClient) {
                socketIOClient.sendEvent("1V1CommunicateVideo", receiveMessageDTO);
            } else {
                logger.info("Not online user: {}, user Id {}", receiveMessageDTO.getTargetName(), receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
                socketIOClient02.sendEvent("notOnline", "The other party is not online");
            }
        }
    }

    @OnEvent("1V1CommunicatePhone")
    public void on1V1CommunicatePhone(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO) {
        logger.info("1V1CommunicatePhone {}", receiveMessageDTO);
        if (true) { // group friend type
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if (null != socketIOClient) {
                socketIOClient.sendEvent("1V1CommunicatePhone", receiveMessageDTO);
            } else {
                logger.info("Not online user: {}, user Id {}", receiveMessageDTO.getTargetName(), receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
                socketIOClient02.sendEvent("notOnline", "The other party is not online");
            }
        }
    }

    @OnEvent("ManyToManyCommunicateVideo")
    public void onManyToManyCommunicateVideo(SocketIOClient client, ReceiveMessageDTO receiveMessageDTO) {
        logger.info("ManyToManyCommunicateVideo {}", receiveMessageDTO);
        if (true) { // group friend type
            SocketIOClient socketIOClient = clientMap.get(receiveMessageDTO.getTargetId().toString());
            if (null != socketIOClient) {
                socketIOClient.sendEvent("ManyToManyCommunicateVideo", receiveMessageDTO);
            } else {
                logger.info("Not online user: {}, user ID {}", receiveMessageDTO.getTargetName(), receiveMessageDTO.getTargetId());
                SocketIOClient socketIOClient02 = clientMap.get(receiveMessageDTO.getUserId().toString());
                socketIOClient02.sendEvent("notOnline", "The other party is not online");
            }
        }
    }

    @OnEvent("onAccessChatRandom")
    public void accessChatRandom(SocketIOClient client, ReceiveMessageDTO topicChildren) {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        clientChatRandom.put(urlParams.get("uid").get(0) + "-" + topicChildren.getData().get("id").toString(), client);
        logger.info("link open, urlParams {}", urlParams);
        logger.info("Number of people joining to chat random: {}", clientChatRandom.size());

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

        client.sendEvent("userAccess", engagement);

        ParticipantRandomResponse p = onCreateChatRandom();

        if (p != null) {
            for (PartnerDTO key : p.getPartnerDTO()) {
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
        clientChatRandom.remove(moveUser + "-" + topicChildren.getData().get("id").toString(), client);
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

                for (String user : lists) {
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