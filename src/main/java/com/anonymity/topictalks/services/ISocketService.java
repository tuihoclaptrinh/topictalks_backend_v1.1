package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.corundumstudio.socketio.SocketIOClient;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 16-09-2023 23:19:11
 * @since 1.0 - version of class
 */
public interface ISocketService {

    void sendSocketMessage(SocketIOClient senderClient, MessagePO message, Long conversation);

    void saveMessage(SocketIOClient senderClient, MessagePO message);

    void saveInfoMessage(SocketIOClient senderClient, String message, Long conversation, String username);

}
