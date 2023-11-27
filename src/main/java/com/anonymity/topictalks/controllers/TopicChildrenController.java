package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.requests.TopicChildrenRequest;
import com.anonymity.topictalks.models.payloads.requests.TopicUpdateRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.services.ITopicChildrenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/topic-children")
@PreAuthorize("hasAnyRole('ADMIN')")
@Tag(name = "Topic children", description = "The Post API contains information relate to CRUD topic children in system.")
public class TopicChildrenController {
    private final ITopicChildrenService topicChildrenService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TopicChildrenRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        TopicChildrenPO newTopicChildren = topicChildrenService.create(request);
        if (newTopicChildren == null) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc("This children topic has already exist.");
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());
        dataResponse.setData(newTopicChildren);
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("")
    public ResponseEntity<?> getAllTopicChildrenByTopicParentIdAndIsExpired(@RequestParam(value = "tpid") Long id,
                                                                            @RequestParam(value = "is_expired") boolean isExpired,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(topicChildrenService.getTopicChildrenByTopicParentIdAndIsExpired(id, isExpired,page,size));

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/topic-parent={id}")
    public ResponseEntity<?> getAllTopicChildrenByTopicParentId(@PathVariable Long id) {
        DataResponse dataResponse = new DataResponse();
        List<TopicChildrenPO> list = topicChildrenService.getTopicChildrenByTopicParentId(id);
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(list);

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicChildrenById(@PathVariable Long id) {
        DataResponse dataResponse = new DataResponse();

        TopicChildrenPO topicChildrenPO = topicChildrenService.getTopicChildrenById(id);

        if (topicChildrenPO == null) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(null);

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(topicChildrenPO);

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam("pid") long topicParentId, @RequestParam("cid") long topicChildrenId, @RequestBody TopicUpdateRequest request) {
        DataResponse dataResponse = new DataResponse();
        if (topicChildrenService.checkDuplicateTopicName(request.getNewName(), topicParentId) == true) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("This topic name has already exist.");

            return ResponseEntity.ok(dataResponse);
        }
        TopicChildrenPO isUpdated = topicChildrenService.update(topicChildrenId, request.getNewName(), request.getShortDescript());

        if (isUpdated == null) {
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("Failure to update.");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(isUpdated);

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update-expired")
    public ResponseEntity<?> updateIsExpiredById(@RequestParam("id") long id, @RequestParam("is_expired") boolean isExpired) {
        DataResponse dataResponse = new DataResponse();
        TopicChildrenPO isUpdated = topicChildrenService.updateIsExpiredById(id, isExpired);

        if (isUpdated == null) {
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("Failure to update.");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(isUpdated);

        return ResponseEntity.ok(dataResponse);
    }

}
