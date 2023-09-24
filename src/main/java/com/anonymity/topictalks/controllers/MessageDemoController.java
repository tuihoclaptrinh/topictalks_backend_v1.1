package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessageDemoPO;
import com.anonymity.topictalks.services.IMessageDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message-demo")
@RequiredArgsConstructor
public class MessageDemoController {
    private final IMessageDemoService messageService;

    @CrossOrigin
    @GetMapping("/{room}")
    public ResponseEntity<List<MessageDemoPO>> getMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getMessages(conversationId));
    }
}
