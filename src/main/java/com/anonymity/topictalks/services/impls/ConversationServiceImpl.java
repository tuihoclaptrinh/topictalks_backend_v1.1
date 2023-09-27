package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.exceptions.TopicChildrenNotFoundException;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.responses.ConversationResponse;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.services.IConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 22-09-2023 19:00:13
 * @since 1.0 - version of class
 */
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements IConversationService {

    private final IConversationRepository conversationRepository;
    private final ITopicChildrenRepository topicChildrenRepository;

    @Override
    public ConversationResponse createConversation(ConversationRequest conversationRequest) {

        var conversation = new ConversationPO();
        TopicChildrenPO topicChildren = topicChildrenRepository
                .findById(conversationRequest.getTopicChildrenId())
                .orElseThrow(() -> new TopicChildrenNotFoundException("Topic Children not found"));

        conversation.setChatName(conversationRequest.getChatName());
        conversation.setIsGroupChat(conversationRequest.getIsGroupChat());
        conversation.setTopicChildren(topicChildren);

        conversation = conversationRepository.save(conversation);

        return ConversationResponse.builder()
                .conversationId(conversation.getId())
                .chatName(conversation.getChatName())
                .isGroupChat(conversation.getIsGroupChat())
                .topicChildrenId(conversation.getTopicChildren().getId())
                .build();
    }

    @Override
    public Boolean checkMatchingConversations(long userId1, long userId2) {
        List<Long> isConversationMatched = conversationRepository.checkMatchingConversations(userId1, userId2);
        if (isConversationMatched.isEmpty()) return false;
        return true;
    }
}
