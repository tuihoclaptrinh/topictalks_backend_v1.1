package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.responses.ConversationRandomResponse;
import com.anonymity.topictalks.models.payloads.responses.ConversationResponse;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 22-09-2023 19:00:22
 * @since 1.0 - version of class
 */
public interface IConversationService {
    ConversationResponse createConversation(ConversationRequest conversationRequest, boolean isGroupChat);

    ConversationRandomResponse createConversationRandom(ConversationRequest conversationRequest, boolean isGroupChat);

    Boolean checkMatchingConversations(long userId1, long userId2);

    DataResponse updateTopicGroupChat(long conversationId, long newTopicId, long userIdUpdate);

    DataResponse updateNameGroupChat(long conversationId, String newName, long userIdUpdate);

    DataResponse updateAvtImgGroupChat(long conversationId, String newUrlImage, long userIdUpdate);

    void deleteByConversationId(long conversationId, long userId);

    void deleteConversationByUser(long conversationId);

    List<ConversationResponse> getAllConversationByUserIdAndIsGroup(long userId, boolean isGroupChat);



}
