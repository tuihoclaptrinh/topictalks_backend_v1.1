package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
//import com.anonymity.topictalks.daos.user.IUserRepository;
//import com.anonymity.topictalks.listeners.SocketEventListener;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.ParticipantKey;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IConversationService;
import com.anonymity.topictalks.services.IParticipantService;
import com.anonymity.topictalks.utils.RandomUserUtils;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 22-09-2023 18:59:05
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements IParticipantService {

    private final IParticipantRepository participantRepository;
    private final RandomUserUtils randomUserUtil;
    private final IConversationService conversationService;
    private final IUserRepository userRepository;
    private final IConversationRepository conversationRepository;
    private Logger logger = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    @Override
    @Transactional
    public void createChatSingle(SocketIOClient client, ParticipantRequest participantRequest) {

        Map<Long, Long> result = randomUserUtil.randomUserChatting(participantRequest);

        var conversationRequest = ConversationRequest.builder()
                .isGroupChat(false)
                .chatName(null)
                .topicChildrenId(participantRequest.getTopicChildId())
                .build();

        for(Map.Entry<Long, Long> entry: result.entrySet()) {

            var conversationResponse = conversationService.createConversation(conversationRequest);

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
            logger.info("Socket ID[{}] - conversation[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), participant.getConversationInfo().getId(), participant.getUserInfo().getUsername());
            var key2 = new ParticipantKey();
            var participant2 = ParticipantPO.builder()
                    .id(key2)
                    .userInfo(userRepository.findById(entry.getValue()).orElse(null))
                    .conversationInfo(conversationRepository.findById(conversationResponse.getConversationId()).orElse(null))
                    .build();
            participantRepository.save(participant2);

            logger.info("Socket ID[{}] - conversation[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), participant.getConversationInfo().getId(), participant2.getUserInfo().getUsername());

        }

    }

    /**
     * @param conversationPO
     * @return
     */
    @Override
    public String getUserIdsByConversation(ConversationPO conversationPO) {

        List<ParticipantPO> participantS = participantRepository.findAllByConversationInfo(conversationPO);

        List<Long> userIds = participantS.stream()
                .map(ParticipantPO::getUserInfo).map(UserPO::getId)
                .toList();

        String result = userIds.stream()
                .map(Object::toString) // Convert Long to String
                .collect(Collectors.joining(",")); // Join with commas and space

        return result;
    }

}
