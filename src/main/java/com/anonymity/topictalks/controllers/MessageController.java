package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.dtos.ReceiveMessageDTO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.services.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{room}")
    public ResponseEntity<List<ReceiveMessageDTO>> getMessages(@PathVariable Long room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }


}
