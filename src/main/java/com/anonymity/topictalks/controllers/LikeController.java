package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.requests.LikeRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.persists.post.LikePO;
import com.anonymity.topictalks.services.ILikeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Like", description = "The Like API")
public class LikeController {

    private final ILikeService likeService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LikeRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        LikePO addLike = likeService.like(request);

        if (addLike == null) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value()); //201
            dataResponse.setSuccess(true);
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//CREATED
            dataResponse.setData("Failure to like");
            return ResponseEntity.ok(dataResponse);
        }
        dataResponse.setStatus(HttpStatus.CREATED.value()); //201
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());//CREATED
        dataResponse.setData(addLike);
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/remove/uid={userId}&&pid={postId}")
    public ResponseEntity<?> remove(@PathVariable Long userId, @PathVariable Long postId) {
        DataResponse dataResponse = new DataResponse();
        boolean isUnliked = likeService.unlike(userId, postId);
        if (!isUnliked) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value()); //201
            dataResponse.setSuccess(true);
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//CREATED
            dataResponse.setData("Failure to unlike");
            return ResponseEntity.ok(dataResponse);
        }
        dataResponse.setStatus(HttpStatus.CREATED.value()); //201
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());//CREATED
        dataResponse.setData("Un-like successfully");
        return ResponseEntity.ok(dataResponse);
    }

}
