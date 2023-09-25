package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.requests.AvatarRequest;
import com.anonymity.topictalks.models.payloads.requests.UserTopicRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.services.IUserService;
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
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "User", description = "")
public class UserController {
    private final IUserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> create(@RequestBody AvatarRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        boolean isSuccess = userService.updateAvatar(request.getImage(),request.getUserId());
        if (bindingResult.hasErrors() || isSuccess==false) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.FORBIDDEN.value());//400
            dataResponse.setDesc(HttpStatus.FORBIDDEN.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("Failure to upload avatar");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.CREATED.value()); //201
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());//CREATED
        dataResponse.setData("Upload avatar successfully");
        return ResponseEntity.ok(dataResponse);
    }
}
