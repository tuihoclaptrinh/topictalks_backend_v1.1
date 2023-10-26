package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.dtos.CommentDTO;
import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.CommentRequest;
import com.anonymity.topictalks.models.payloads.requests.CommentUpdateRequest;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.persists.post.CommentPO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.services.ICommentService;
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
@RequestMapping("/api/v1/comment")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Comment", description = "The Comment API contains information relate to CRUD comment in system.")
public class CommentController {
    private final ICommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CommentRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        CommentDTO newComment = commentService.create(request);
        dataResponse.setStatus(HttpStatus.CREATED.value()); //201
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());//CREATED
        dataResponse.setData(newComment);
        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CommentUpdateRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();

        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        CommentDTO commentUpdated = commentService.update(id,request);

        if (commentUpdated == null) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(commentUpdated);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllComments() {
        DataResponse dataResponse = new DataResponse();

        List<CommentDTO> commentList = commentService.getAllComments();

        if (commentList.isEmpty()) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());//204
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(commentList);

        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getAllCommentsByPostId(@PathVariable("postId") long postId) {
        DataResponse dataResponse = new DataResponse();

        List<CommentDTO> commentList = commentService.getCommentsByPostId(postId);

        if (commentList.isEmpty()) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());//204
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(commentList);

        return ResponseEntity.ok(dataResponse);
    }

    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable("userId") long userId,@PathVariable("id") long id) {
        DataResponse dataResponse = new DataResponse();

        boolean isRemoved = commentService.remove(userId,id);

        if (isRemoved==false) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("Cann't found this comment.");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData("Removed the comment successfully.");

        return ResponseEntity.ok(dataResponse);
    }

}
