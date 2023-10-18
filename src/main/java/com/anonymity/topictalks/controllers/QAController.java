package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.dtos.InteractDTO;
import com.anonymity.topictalks.models.payloads.requests.QARequest;
import com.anonymity.topictalks.models.payloads.requests.ReplyQARequest;
import com.anonymity.topictalks.models.payloads.requests.TopicChildrenRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.services.IInteractService;
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
@RequestMapping("/api/v1/qa")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Q&A interaction", description = "")
public class QAController {
    private final IInteractService interactService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody QARequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        try {
            InteractDTO data =interactService.createQA(request);
            dataResponse.setStatus(HttpStatus.CREATED.value());
            dataResponse.setSuccess(true);
            dataResponse.setDesc("Create QA successfully");
            dataResponse.setData(data);
            return ResponseEntity.ok(dataResponse);
        } catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setSuccess(false);
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/reply")
    public ResponseEntity<?> replyQA(@RequestBody ReplyQARequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        try {
            InteractDTO data = interactService.replyQA(request);
            dataResponse.setStatus(HttpStatus.CREATED.value());
            dataResponse.setSuccess(true);
            dataResponse.setDesc("Reply QA successfully");
            dataResponse.setData(data);
            return ResponseEntity.ok(dataResponse);
        } catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setSuccess(false);
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllQA() {
        DataResponse dataResponse = new DataResponse();
        try {
            List<InteractDTO> data = interactService.getAllQA();
            dataResponse.setStatus(HttpStatus.CREATED.value());
            dataResponse.setSuccess(true);
            dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());
            dataResponse.setData(data);
            return ResponseEntity.ok(dataResponse);
        } catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setSuccess(false);
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{senderId}/all")
    public ResponseEntity<?> getAllQaBySenderId(@PathVariable Long senderId) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<InteractDTO> data = interactService.getAllQABySenderId(senderId);
            dataResponse.setStatus(HttpStatus.CREATED.value());
            dataResponse.setSuccess(true);
            dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());
            dataResponse.setData(data);
            return ResponseEntity.ok(dataResponse);
        } catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setSuccess(false);
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getQaById(@PathVariable Long id) {
        DataResponse dataResponse = new DataResponse();
        try {
            InteractDTO data = interactService.getQAById(id);
            dataResponse.setStatus(HttpStatus.CREATED.value());
            dataResponse.setSuccess(true);
            dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());
            dataResponse.setData(data);
            return ResponseEntity.ok(dataResponse);
        } catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setSuccess(false);
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }
}
