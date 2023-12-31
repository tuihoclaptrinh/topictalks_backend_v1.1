package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.requests.TopicRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.payloads.responses.TopicParentResponse;
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

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TopicRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
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
        dataResponse.setStatus(HttpStatus.CREATED.value());
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());
        dataResponse.setData(newTopicParent);
        return ResponseEntity.ok(dataResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllTopicParents() {
        DataResponse dataResponse = new DataResponse();

        List<TopicParentPO> list = topicParentService.getAll();

        if (list.isEmpty()) {
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(list);

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(list);

        return ResponseEntity.ok(dataResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all-tparent")
    public ResponseEntity<?> getAllTopicParentsByIsExpired(@RequestParam(value = "isDisable") boolean isDisable) {
        DataResponse dataResponse = new DataResponse();

        List<TopicParentPO> list = topicParentService.getAllByIsExpired(isDisable);

        if (list.isEmpty()) {
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(list);

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(list);

        return ResponseEntity.ok(dataResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/rename")
    public ResponseEntity<?> updateTopicName(@RequestParam("id") long id, @RequestBody TopicRequest request) {
        DataResponse dataResponse = new DataResponse();
        if (topicParentService.checkDuplicateTopicName(request.getTopicName(),id) == true) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("This topic name has already exist.");

            return ResponseEntity.ok(dataResponse);
        }

        TopicParentPO isUpdated = topicParentService.updateTopicName(id, request.getTopicName());

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

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update-expired")
    public ResponseEntity<?> updateIsExpiredById(@RequestParam("id") long id, @RequestParam("is_expired") boolean isExpired) {
        DataResponse dataResponse = new DataResponse();
        TopicParentPO isUpdated = topicParentService.updateIsExpiredById(id, isExpired);

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

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/search")
    public List<TopicParentPO> searchByTopicParentName(@RequestParam("tp_name") String topicParentName, @RequestParam("is_expired") boolean isExpired) {
        return topicParentService.searchByTopicParentName(topicParentName,isExpired);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/retrieve")
    public List<?> retrieveDataForTopicParent() {
        return topicParentService.retrieveDataForTopicParent();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateTopicParent(@RequestParam("id") long id, @RequestBody TopicRequest request) {
        DataResponse dataResponse = new DataResponse();
        if (topicParentService.checkDuplicateTopicName(request.getTopicName(),id) == true) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("This topic name has already existed.");

            return ResponseEntity.ok(dataResponse);
        }

        TopicParentPO isUpdated = topicParentService.update(id,request);

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

    @GetMapping("/all/wishlist")
    public List<TopicParentResponse> getAllTopicParentToAddWishList() {
        return topicParentService.getAllTopicParent();
    }

}
