package com.anonymity.topictalks.controllers;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.models.payloads.requests.ConversationMatcherRequest;
import com.anonymity.topictalks.services.IConversationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversation/")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Conversation", description = "The Conversation API")
public class ConversationController {

    private final IConversationService conversationService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{partnerId}")
    public ResponseEntity<Object> checkConversationMatched(@PathVariable Long partnerId, @RequestBody ConversationMatcherRequest request) {
        return ResponseEntity.ok(JSON.parseObject("{\"isMatched\":\""+conversationService.checkMatchingConversations(request.getUserIdInSession(),partnerId)+"\"}"));
    }
}
