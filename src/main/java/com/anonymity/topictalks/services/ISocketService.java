package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.corundumstudio.socketio.SocketIOClient;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 23-09-2023 09:04:12
 * @since 1.0 - version of class
 */
public interface ISocketService {

    void sendSocketMessage(SocketIOClient senderClient, ReceiveMessageDTO receiveMessageDTO);

    void saveMessage(SocketIOClient senderClient, ReceiveMessageDTO receiveMessageDTO);

    void creatChatSingle(SocketIOClient senderClient, ParticipantRequest participantRequest);

}
