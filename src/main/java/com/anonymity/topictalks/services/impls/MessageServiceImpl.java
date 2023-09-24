package com.anonymity.topictalks.services.impls;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.services.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

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
            receiveMessageDTO.setData(JSON.parseObject("{\"message\":\"message\"}"));
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
}
