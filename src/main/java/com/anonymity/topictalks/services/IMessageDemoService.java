package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.persists.message.MessageDemoPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IMessageDemoService {
    List<MessageDemoPO> getMessages(String room);

    MessageDemoPO saveMessage(MessageDemoPO message);

}
