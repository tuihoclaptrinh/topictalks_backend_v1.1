package com.anonymity.topictalks.services.impls;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.LastMessageDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public Page<ReceiveMessageDTO> getMessages(Long conversationId, int page, int size) {
        ConversationPO conversationPO = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("This conversation doesn't exist"));

        List<MessagePO> sortedMessages = messageRepository.findAllByConversationIdOrderByCreatedAtDesc(conversationPO);

        int start = page * size;
        int end = Math.min(start + size, sortedMessages.size());

        List<MessagePO> pageMessages = sortedMessages.subList(start, end);

        List<ReceiveMessageDTO> messageDTOs = pageMessages.stream()
                .map(messagePO -> {
                    ReceiveMessageDTO receiveMessageDTO = new ReceiveMessageDTO();
                    receiveMessageDTO.setUserId(messagePO.getSenderId().getId());
                    receiveMessageDTO.setUsername(messagePO.getSenderId().getNickName());
                    receiveMessageDTO.setConversationId(messagePO.getConversationId().getId());
                    receiveMessageDTO.setGroupChat(messagePO.getConversationId().getIsGroupChat());
                    receiveMessageDTO.setGroupChatName(messagePO.getConversationId().getIsGroupChat() ?
                            messagePO.getConversationId().getChatName() : null);
                    String message = messagePO.getContent();
                    receiveMessageDTO.setData(JSON.parseObject("{\"message\":\"" + message + "\"}"));
                    receiveMessageDTO.setTimeAt(String.valueOf(messagePO.getCreatedAt()));
                    return receiveMessageDTO;
                })
                .sorted(Comparator.comparing(dto -> dto.getTimeAt()))
                .collect(Collectors.toList());

        return new PageImpl<>(messageDTOs, PageRequest.of(page, size), sortedMessages.size());
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
    public LastMessageDTO getLastMessageByConversationId(long converId) {
        MessagePO messagePO = messageRepository.getLastMessageByConversationId(converId);
        if (messagePO == null) {
            LastMessageDTO lastMessageDTO = new LastMessageDTO();
            ConversationPO conversationPO = conversationRepository.findById(converId).orElse(null);
            if (conversationPO != null) {
                lastMessageDTO.setTimeAt(String.valueOf(conversationPO.getCreatedAt()));
                return lastMessageDTO;
            }
        }
        return new LastMessageDTO(messagePO.getSenderId().getId(),
                messagePO.getSenderId().getNickName(),
                messagePO.getContent(),
                String.valueOf(messagePO.getCreatedAt()));
    }
}
