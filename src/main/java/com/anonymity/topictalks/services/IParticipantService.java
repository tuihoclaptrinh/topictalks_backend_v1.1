package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.payloads.responses.ParticipantResponse;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.corundumstudio.socketio.SocketIOClient;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 22-09-2023 18:58:35
 * @since 1.0 - version of class
 */
public interface IParticipantService {
    void createChatSingle(SocketIOClient client, ParticipantRequest participantRequest);

    String getUserIdsByConversation(ConversationPO conversationPO);

    List<ParticipantResponse> getAllParticipantByUserId(long id);
}
