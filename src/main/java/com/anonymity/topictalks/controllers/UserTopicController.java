package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.requests.UserTopicRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.services.IUserTopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-topic")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Topic parent", description = "The Post API contains information relate to CRUD user topic in system.")
public class UserTopicController {
    private final IUserTopicService userTopicService;

    @PostMapping("{id}/create")
    public ResponseEntity<?> create(@PathVariable Long id, @RequestBody UserTopicRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.CREATED.value()); //201
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());//CREATED
        dataResponse.setData(userTopicService.createUserTopic(id, request));
        return ResponseEntity.ok(dataResponse);


    }
}
