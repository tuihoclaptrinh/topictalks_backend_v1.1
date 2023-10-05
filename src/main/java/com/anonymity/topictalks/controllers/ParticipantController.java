package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.ConversationMatcherRequest;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.payloads.responses.ConversationResponse;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.payloads.responses.ParticipantResponse;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.services.IConversationService;
import com.anonymity.topictalks.services.IParticipantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participant")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Participant", description = "")
public class ParticipantController {
    private final IParticipantService participantService;
    private final IConversationService conversationService;

    @GetMapping("/{id}/all")
    public ResponseEntity<?> getAllParticipantByUserId(@PathVariable("id") long id) {
        DataResponse dataResponse = new DataResponse();

        List<ParticipantResponse> list = participantService.getAllParticipantByUserId(id);

        if (list.isEmpty()) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());//204
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(list);

        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getParticipantByPartnerId(@PathVariable("id") long id, @RequestBody ConversationMatcherRequest request) {
        DataResponse dataResponse = new DataResponse();

        ParticipantResponse participant = participantService.getParticipantByUserIdAndPartnerId(request.getUserIdInSession(), id, request.getTopicChildrenId());

        if (participant == null) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());//204
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(participant);

        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/create-group-chat")
    public ResponseEntity<?> createChatGroup(@RequestBody ConversationRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();

        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        ConversationResponse conversation = conversationService.createConversation(request, true);
        ParticipantResponse participantResponse = participantService.createGroupChat(conversation.getConversationId());

        if (participantResponse == null) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(participantResponse);
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/join-group-chat/uid={userId}&&cid={conversationId}")
    public ResponseEntity<?> joinChatGroup(@PathVariable Long userId, @PathVariable Long conversationId) {
        DataResponse dataResponse = new DataResponse();

        ParticipantResponse participantResponse = participantService.joinGroupChat(userId, conversationId);

        if (participantResponse == null) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(participantResponse);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/group-chat/{id}")
    public ResponseEntity<?> getAllGroupChatsByTopicChildrenId(@PathVariable("id") long id) {
        DataResponse dataResponse = new DataResponse();

        List<ParticipantResponse> participant = participantService.getAllGroupChatByTopicChildrenId(id);

        if (participant == null) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());//204
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(participant);

        return ResponseEntity.ok(dataResponse);
    }

}
