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
import com.anonymity.topictalks.models.payloads.responses.ConversationRandomResponse;
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
    private final IMessageService messageService;

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
        List<String> userIdTopicPairs = new ArrayList<>();
        ParticipantRandomResponse participantRandomResponse = null;
        String prevTpcId = null;

        for (String key : keys) {
            String[] params = key.split("-");
            Long userId = Long.parseLong(params[0].trim());
            Long tpcId = Long.parseLong(params[1].trim());
            logger.info("userId: {} - topic Children Id: {}", userId, tpcId);
            userIdTopicPairs.add(userId + "-" + tpcId);

            if (prevTpcId != null && prevTpcId.equals(tpcId.toString())) {
                String[] userIds = keys.stream().findFirst().get().split("-");
                Long userId1 = Long.parseLong(userIds[0]);
                Long userId2 = Long.parseLong(key.split("-")[0]);
                boolean isExistedConversationBefore = conversationService.checkMatchingConversations(userId1, userId2);
                logger.info("------> isExistedConversationBefore: {}", isExistedConversationBefore);
                if (isExistedConversationBefore == true) {
                    List<Long> conversationId = conversationRepository.checkMatchingConversations(userId1, userId2, false);
                    ParticipantResponse response = participantService.getParticipantByConversationIdAndUserId(conversationId.get(0), userId1);
                    ConversationRandomResponse conversationRandomResponse = new ConversationRandomResponse();
                    TopicChildrenDTO topicChildrenDTO = new TopicChildrenDTO();
                    topicChildrenDTO.setTopicChildrenName(response.getConversationInfor().getTopicChildren().getTopicChildrenName());
                    topicChildrenDTO.setId(response.getConversationInfor().getTopicChildren().getId());
                    topicChildrenDTO.setImage(response.getConversationInfor().getTopicChildren().getImage());

                    conversationRandomResponse.setTopicChildren(topicChildrenDTO);
                    conversationRandomResponse.setChatName(response.getConversationInfor().getChatName());
                    conversationRandomResponse.setIsGroupChat(response.getConversationInfor().getIsGroupChat());
                    conversationRandomResponse.setAdminId(response.getConversationInfor().getAdminId());
                    conversationRandomResponse.setId(response.getConversationInfor().getId());

                    LastMessageDTO lastMessageDTO = messageService.getLastMessageByConversationId(conversationId.get(0));
                    conversationRandomResponse.setLastMessageDTO(lastMessageDTO);
                    List<PartnerDTO> partnerDTO = response.getPartnerDTO();

                    UserPO userPO = userRepository.findById(userId1).orElse(null);
                    ParticipantPO participantPO = participantRepository.findByConversationIdAndUserId(conversationId.get(0), userId1);
                    PartnerDTO partnerDTO1 = new PartnerDTO();
                    if (userPO != null) {
                        partnerDTO1.setUsername(userPO.getUsername());
                        partnerDTO1.setMember(participantPO.getIsMember());
                        partnerDTO1.setBanned(userPO.getIsBanned());
                        partnerDTO1.setId(userId1);
                        partnerDTO1.setImage(userPO.getImageUrl());
                        partnerDTO1.setActive(userPO.isActive());
                        partnerDTO1.setBannedAt(userPO.getBannedDate());
                    }
                    partnerDTO.add(partnerDTO1);
                    participantRandomResponse = new ParticipantRandomResponse(conversationRandomResponse, response.getPartnerDTO());
                } else {
                    participantRandomResponse = participantService.createChatRandom(
                            ChatRandomDTO.builder()
                                    .users(userIdTopicPairs)
                                    .tpcId(tpcId)
                                    .build());
                }

                for (String user : userIdTopicPairs) {
                    clientChatRandom.remove(user);
                }
                userIdTopicPairs.clear();
                logger.info("Cleared data in userIdTopicPairs");
            }
            prevTpcId = tpcId.toString();
        }

        logger.info("Client remaining: {}", clientChatRandom.size());
        return participantRandomResponse;
    }


}