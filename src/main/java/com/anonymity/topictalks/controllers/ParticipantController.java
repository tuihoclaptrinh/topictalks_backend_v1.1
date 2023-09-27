package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.ConversationMatcherRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.payloads.responses.ParticipantResponse;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.services.IParticipantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participant")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Participant", description = "")
public class ParticipantController {
    private final IParticipantService participantService;

    @GetMapping("/{id}/all")
    public ResponseEntity<?> getAllParticipantByUserId(@PathVariable("id") long id ) {
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

        ParticipantResponse participant = participantService.getParticipantByUserIdAndPartnerId(request.getUserIdInSession(),id,request.getTopicChildrenId());

        if (participant==null) {//NO CONTENT
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
