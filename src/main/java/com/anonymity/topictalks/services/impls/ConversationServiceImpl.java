package com.anonymity.topictalks.services.impls;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.exceptions.TopicChildrenNotFoundException;
import com.anonymity.topictalks.models.dtos.TopicChildrenDTO;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.responses.ConversationRandomResponse;
import com.anonymity.topictalks.models.payloads.responses.ConversationResponse;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.services.IConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
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
    public ConversationResponse createConversation(ConversationRequest conversationRequest, boolean isGroupChat) {

        var conversation = new ConversationPO();
        TopicChildrenPO topicChildren = topicChildrenRepository
                .findById(conversationRequest.getTopicChildrenId())
                .orElseThrow(() -> new TopicChildrenNotFoundException("Topic Children not found"));

        conversation.setChatName(conversationRequest.getChatName());
        conversation.setIsGroupChat(isGroupChat);
        conversation.setTopicChildren(topicChildren);
        conversation.setAdminId(isGroupChat == true ? conversationRequest.getAdminId() : 0);

        conversation = conversationRepository.save(conversation);

        return ConversationResponse.builder()
                .conversationId(conversation.getId())
                .chatName(conversation.getChatName())
                .isGroupChat(conversation.getIsGroupChat())
                .topicChildrenId(conversation.getTopicChildren().getId())
                .adminId(conversation.getAdminId())
                .build();
    }

    @Override
    public ConversationRandomResponse createConversationRandom(ConversationRequest conversationRequest, boolean isGroupChat) {
        var conversation = new ConversationPO();
        TopicChildrenPO topicChildren = topicChildrenRepository
                .findById(conversationRequest.getTopicChildrenId())
                .orElseThrow(() -> new TopicChildrenNotFoundException("Topic Children not found"));

        var tpc = TopicChildrenDTO
                .builder()
                .id(topicChildren.getId())
                .topicChildrenName(topicChildren.getTopicChildrenName())
                .image(topicChildren.getImage())
                .build();

        conversation.setChatName(conversationRequest.getChatName());
        conversation.setIsGroupChat(isGroupChat);
        conversation.setTopicChildren(topicChildren);
        conversation.setAdminId(isGroupChat == true ? conversationRequest.getAdminId() : 0);

        conversation = conversationRepository.save(conversation);

        return ConversationRandomResponse.builder()
                .id(conversation.getId())
                .chatName(conversation.getChatName())
                .isGroupChat(conversation.getIsGroupChat())
                .topicChildren(tpc)
                .adminId(conversation.getAdminId())
                .build();
    }

    @Override
    public Boolean checkMatchingConversations(long userId1, long userId2) {
        List<Long> isConversationMatched = conversationRepository.checkMatchingConversations(userId1, userId2, false);
        if (isConversationMatched.isEmpty()) return false;
        return true;
    }

    @Override
    public DataResponse updateTopicGroupChat(long conversationId, long newTopicId, long userIdUpdate) {
        DataResponse dataResponse = new DataResponse();
        ConversationPO conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation != null && conversation.getIsGroupChat() == true) {
            if (conversation.getAdminId() == userIdUpdate) {
                conversation.setId(conversationId);
                TopicChildrenPO newTopicChildrenPO = topicChildrenRepository.findById(newTopicId);
                conversation.setTopicChildren(newTopicChildrenPO);
                conversation.setUpdatedAt(LocalDateTime.now());
                try {
                    ConversationPO po = conversationRepository.save(conversation);
                    ConversationResponse response = convertToConversationResponse(po);
                    dataResponse.setSuccess(true);
                    dataResponse.setStatus(HttpStatus.OK.value());
                    dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
                    dataResponse.setData(response);
                } catch (Exception e) {
                    dataResponse.setSuccess(false);
                    dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                    dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
                    dataResponse.setData(JSON.parseObject("{\"message\":\"" + e.getMessage() + "\"}"));
                }
            } else {
                dataResponse.setSuccess(false);
                dataResponse.setStatus(HttpStatus.FORBIDDEN.value());
                dataResponse.setDesc(HttpStatus.FORBIDDEN.getReasonPhrase());
                dataResponse.setData(JSON.parseObject("{\"message\":\"Access Denied\"}"));
            }
        } else {
            dataResponse.setSuccess(false);
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setData(JSON.parseObject("{\"message\":\"Failure to update topic for this group chat\"}"));
        }
        return dataResponse;
    }

    @Override
    public DataResponse updateNameGroupChat(long conversationId, String newName, long userIdUpdate) {
        DataResponse dataResponse = new DataResponse();
        ConversationPO conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation != null && conversation.getIsGroupChat() == true) {
            if (conversation.getAdminId() == userIdUpdate) {
                conversation.setId(conversationId);
                conversation.setChatName(newName);
                conversation.setUpdatedAt(LocalDateTime.now());
                try {
                    ConversationPO po = conversationRepository.save(conversation);
                    ConversationResponse response = convertToConversationResponse(po);
                    dataResponse.setSuccess(true);
                    dataResponse.setStatus(HttpStatus.OK.value());
                    dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
                    dataResponse.setData(response);
                } catch (Exception e) {
                    dataResponse.setSuccess(false);
                    dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                    dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
                    dataResponse.setData(JSON.parseObject("{\"message\":\"" + e.getMessage() + "\"}"));
                }
            }else {
                dataResponse.setSuccess(false);
                dataResponse.setStatus(HttpStatus.FORBIDDEN.value());
                dataResponse.setDesc(HttpStatus.FORBIDDEN.getReasonPhrase());
                dataResponse.setData(JSON.parseObject("{\"message\":\"Access Denied\"}"));
            }
        } else {
            dataResponse.setSuccess(false);
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setData(JSON.parseObject("{\"message\":\"Failure to update name for this group chat\"}"));
        }
        return dataResponse;
    }

    private ConversationResponse convertToConversationResponse(ConversationPO conversationPO) {
        ConversationResponse response = new ConversationResponse();
        response.setConversationId(conversationPO.getId());
        response.setIsGroupChat(conversationPO.getIsGroupChat());
        response.setAdminId(conversationPO.getAdminId());
        response.setChatName(conversationPO.getChatName());
        response.setTopicChildrenId(conversationPO.getTopicChildren().getId());
        response.setTopicChildrenName(conversationPO.getTopicChildren().getTopicChildrenName());
        return response;
    }
}
