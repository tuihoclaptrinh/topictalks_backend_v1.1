package com.anonymity.topictalks.services;


import com.anonymity.topictalks.models.dtos.PartnerDTO;
import com.anonymity.topictalks.models.dtos.ChatRandomDTO;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.payloads.requests.ProcessMemberGroupChatRequest;
import com.anonymity.topictalks.models.payloads.responses.ParticipantRandomResponse;
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
    void createChatSingle(ParticipantRequest participantRequest);

    ParticipantRandomResponse createChatRandom(ChatRandomDTO chatRandomDTO);

    String getUserIdsByConversation(ConversationPO conversationPO);

    List<PartnerDTO> getAllUserByConversationId(long id);

    List<ParticipantResponse> getAllParticipantByUserId(long id);

    ParticipantResponse getParticipantByUserIdAndPartnerId(long userId, long partnerId, long topicChildrenId);

    ParticipantResponse joinGroupChat(long userId, long conversationId);

    ParticipantResponse createGroupChat(long conversationId);

    ParticipantResponse approveToGroupChat(long userId, long conversationId);

    boolean checkAdminOfGroupChat(long userId, long conversationId);

    List<ParticipantResponse> getAllGroupChatByTopicChildrenId(long id);

    boolean removeToGroupChat(long userId, long conversationId);

}
