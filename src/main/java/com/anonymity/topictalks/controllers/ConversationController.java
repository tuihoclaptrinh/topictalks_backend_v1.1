package com.anonymity.topictalks.controllers;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.payloads.requests.ConversationMatcherRequest;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.requests.ConversationUpdateRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.services.IConversationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversation")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Conversation", description = "The Conversation API")
public class ConversationController {

    private final IConversationService conversationService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{partnerId}")
    public ResponseEntity<Object> checkConversationMatched(@PathVariable Long partnerId, @RequestBody ConversationMatcherRequest request) {
        return ResponseEntity.ok(JSON.parseObject("{\"isMatched\":\"" + conversationService.checkMatchingConversations(request.getUserIdInSession(), partnerId) + "\"}"));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{conversationId}")
    public ResponseEntity<?> updateTopicGroupChat(@PathVariable Long conversationId, @RequestBody ConversationUpdateRequest request) {
        return ResponseEntity.ok(conversationService.updateTopicGroupChat(conversationId, request.getNewTopicId(), request.getUserIdUpdate()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/update-avt/{conversationId}")
    public ResponseEntity<?> updateAvatarGroupChat(@PathVariable Long conversationId, @RequestBody ConversationUpdateRequest request) {
        return ResponseEntity.ok(conversationService.updateAvtImgGroupChat(conversationId,request.getAvatarImg(),request.getUserIdUpdate()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/rename")
    public ResponseEntity<?> updateNameGroupChat(@RequestParam("cid") Long conversationId, @RequestBody ConversationRequest request) {
        return ResponseEntity.ok(conversationService.updateNameGroupChat(conversationId, request.getChatName(), request.getAdminId()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping
    public ResponseEntity<?> removeConversation(@RequestParam("cid") Long conversationId) {
        DataResponse dataResponse = new DataResponse();
        try {
            conversationService.deleteConversationByUser(conversationId);
            dataResponse.setStatus(HttpStatus.OK.value());
            dataResponse.setSuccess(true);
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
            dataResponse.setData("Remove conversation successfully.");
            return ResponseEntity.ok(dataResponse);
        } catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setSuccess(false);
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }
}
