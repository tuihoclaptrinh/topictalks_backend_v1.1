package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.services.IReasonRejectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reason-reject")
@PreAuthorize("hasAnyRole('ADMIN')")
@Tag(name = "ReasonReject", description = "The ReasonReject API contains information relate to reason reject of the post")
public class ReasonRejectController {
    private final IReasonRejectService reasonRejectService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts() {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(reasonRejectService.getAll());

        return ResponseEntity.ok(dataResponse);
    }
}
