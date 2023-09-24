package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessageDemoPO;

import java.util.List;

public interface IMessageDemoService {
    List<MessageDemoPO> getMessages(Long conversationId);

    MessageDemoPO saveMessage(MessageDemoPO message);

}
