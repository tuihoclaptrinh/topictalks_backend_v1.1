package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.requests.TopicParentRequest;
import com.anonymity.topictalks.models.payloads.requests.TopicUpdateRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.services.ITopicParentService;
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
@RequestMapping("/api/v1/topic-parent")
@PreAuthorize("hasAnyRole('ADMIN')")
@Tag(name = "Topic parent", description = "The Post API contains information relate to CRUD topic parent in system.")
public class TopicParentController {
    private final ITopicParentService topicParentService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TopicParentRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        TopicParentPO newTopicParent = topicParentService.create(request);
        if (newTopicParent == null) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc("This parent topic has already exist");
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        dataResponse.setStatus(HttpStatus.CREATED.value()); //201
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());//CREATED
        dataResponse.setData(newTopicParent);
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllTopicParents() {
        DataResponse dataResponse = new DataResponse();

        List<TopicParentPO> list = topicParentService.getAll();

        if (list.isEmpty()) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());//204
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData(null);

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(list);

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/rename")
    public ResponseEntity<?> updateTopicName(@RequestParam("id") long id, @RequestBody TopicUpdateRequest request) {
        DataResponse dataResponse = new DataResponse();
        if (topicParentService.checkDuplicateTopicName(request.getNewName()) == true) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//204
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("This topic name has already exist.");

            return ResponseEntity.ok(dataResponse);
        }

        TopicParentPO isUpdated = topicParentService.updateTopicName(id, request.getNewName());

        if (isUpdated == null) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());//204
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("Failure to update.");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(isUpdated);

        return ResponseEntity.ok(dataResponse);
    }
}
