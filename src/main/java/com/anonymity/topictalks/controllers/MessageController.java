package com.anonymity.topictalks.controllers;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.models.dtos.MessageDTO;
import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.payloads.requests.ConversationMatcherRequest;
import com.anonymity.topictalks.services.IMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.controllers
 * - Created At: 16-09-2023 23:40:51
 * @since 1.0 - version of class
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Message", description = "The Message API")
public class MessageController {

    private final IMessageService messageService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{room}")
    public ResponseEntity<List<ReceiveMessageDTO>> getMessages(@PathVariable Long room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/content/{partnerId}")
    public ResponseEntity<List<ReceiveMessageDTO>> getMessagesInChatOneToOne(@PathVariable Long partnerId, @RequestBody ConversationMatcherRequest request) {
        return ResponseEntity.ok(messageService.getMessagesInChatOneToOne(request.getUserIdInSession(),partnerId));
    }
}
