package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IMessageDemoRepository;
import com.anonymity.topictalks.models.persists.message.MessageDemoPO;
import com.anonymity.topictalks.services.IMessageDemoService;

import java.util.List;

public class MessageDemoServiceImpl implements IMessageDemoService {
    private IMessageDemoRepository messageRepository;

    @Override
    public List<MessageDemoPO> getMessages(String room) {
        return messageRepository.findAllByRoom(room);
    }

    @Override
    public MessageDemoPO saveMessage(MessageDemoPO message) {
        return messageRepository.save(message);
    }
}
