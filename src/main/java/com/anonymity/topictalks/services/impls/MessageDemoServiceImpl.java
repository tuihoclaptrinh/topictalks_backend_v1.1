package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageDemoRepository;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessageDemoPO;
import com.anonymity.topictalks.services.IMessageDemoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageDemoServiceImpl implements IMessageDemoService {
    private IMessageDemoRepository messageRepository;
    private IConversationRepository conversationRepository;


    @Override
    public List<MessageDemoPO> getMessages(Long conversationId) {
        ConversationPO conversationPO = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        return messageRepository.findAllByConversationId(conversationPO);
    }

    @Override
    public MessageDemoPO saveMessage(MessageDemoPO message) {
        return messageRepository.save(message);
    }
}
