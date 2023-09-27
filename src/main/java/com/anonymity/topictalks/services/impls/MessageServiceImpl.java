package com.anonymity.topictalks.services.impls;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.MessageDTO;
import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.payloads.requests.UserTopicRequest;
import com.anonymity.topictalks.models.payloads.responses.ConversationResponse;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IConversationService;
import com.anonymity.topictalks.services.IMessageService;
import com.anonymity.topictalks.services.IParticipantService;
import com.anonymity.topictalks.services.IUserService;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final IConversationService conversationService;
    private final IParticipantRepository participantRepository;
    private final IUserRepository userRepository;

    /**
     * @param conversationId
     * @return
     */
    @Override
    public List<ReceiveMessageDTO> getMessages(Long conversationId) {
        ConversationPO conversationPO = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("This conversation doesn't exist"));
        List<MessagePO> listMessage = messageRepository.findAllByConversationId(conversationPO);
        List<ReceiveMessageDTO> list = new ArrayList<>();
        for (int i = 0; i < listMessage.size(); i++) {
            ReceiveMessageDTO receiveMessageDTO = new ReceiveMessageDTO();
            receiveMessageDTO.setUserId(listMessage.get(i).getSenderId().getId());
            receiveMessageDTO.setUsername(listMessage.get(i).getSenderId().getUsername());
            receiveMessageDTO.setConversationId(listMessage.get(i).getConversationId().getId());
            String message = listMessage.get(i).getContent();
            receiveMessageDTO.setData(JSON.parseObject("{\"message\":\"" + message + "\"}"));
            receiveMessageDTO.setTimeAt(String.valueOf(listMessage.get(i).getCreatedAt()));
            list.add(receiveMessageDTO);
        }
        return list;
    }

    /**
     * @param message
     * @return
     */
    @Override
    public MessagePO saveMessage(MessagePO message) {
        return messageRepository.save(message);
    }

    @Override
    public List<ReceiveMessageDTO> getMessagesInChatOneToOne(Long userInSessionId, Long partnerId, Long topicChildrenId) {
        List<Long> isConversationMatched = conversationRepository.checkMatchingConversations(userInSessionId, partnerId);
        if (!isConversationMatched.isEmpty()) {
            System.out.println("-----------------------> Chay vao day: " + isConversationMatched.isEmpty());
            ConversationPO conversationPO = conversationRepository.findById(isConversationMatched.get(0))
                    .orElseThrow(() -> new IllegalArgumentException("This conversation doesn't exist"));
            return getMessages(conversationPO.getId());
        }
        UserPO userInSession = userRepository.findById(userInSessionId)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist"));

        System.out.println("==============> User: "+userInSession.toString());

        UserPO partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist"));

        System.out.println("==============> Partner: "+partner.toString());

        ConversationRequest request = new ConversationRequest();
        request.setChatName("You, "+partner.getUsername());
        request.setIsGroupChat(false);
        request.setTopicChildrenId(topicChildrenId);
        ConversationResponse conversationResponse = new ConversationResponse();
        try {
            conversationResponse = conversationService.createConversation(request);
            System.out.println("===================> Conversation new: "+conversationResponse.toString());
        } catch (Exception e){
            System.out.println(e);
        }
        ParticipantPO participantPO1 = new ParticipantPO();
        ConversationPO conversationPO = conversationRepository.findById(conversationResponse.getConversationId())
                .orElseThrow(() -> new IllegalArgumentException("This conversation doesn't exist"));
        participantPO1.setConversationInfo(conversationPO);
        participantPO1.setUserInfo(userInSession);
        participantPO1.setCreatedAt(LocalDateTime.now());
        participantPO1.setCreatedAt(LocalDateTime.now());
        participantPO1.setUpdatedAt(LocalDateTime.now());
        participantRepository.save(participantPO1);

        ParticipantPO participantPO2 = new ParticipantPO();
        participantPO2.setConversationInfo(conversationPO);
        participantPO2.setUserInfo(partner);
        participantPO2.setCreatedAt(LocalDateTime.now());
        participantPO2.setCreatedAt(LocalDateTime.now());
        participantPO2.setUpdatedAt(LocalDateTime.now());
        participantRepository.save(participantPO2);

        System.out.println("-------------> Conversation ID: "+conversationResponse.getConversationId());
        ReceiveMessageDTO response = new ReceiveMessageDTO();
        response.setUsername(partner.getUsername());
        response.setConversationId(conversationResponse.getConversationId());
        response.setTimeAt(null);
        response.setData(JSON.parseObject("{\"message\":\"\"}"));
        response.setUserId(partnerId);
        List<ReceiveMessageDTO> responseMessageDTOList = new ArrayList<>();
        responseMessageDTOList.add(response);
        return responseMessageDTOList;
    }

}
