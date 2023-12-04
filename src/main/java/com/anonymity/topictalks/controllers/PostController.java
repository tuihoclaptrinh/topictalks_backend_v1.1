package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.payloads.requests.RejectPostRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.services.IPostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Post", description = "The Post API contains information relate to CRUD post in system.")
public class PostController {

    private final IPostService postService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PostRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        PostPO newPost = postService.createPost(request);
        dataResponse.setStatus(HttpStatus.CREATED.value());
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());
        dataResponse.setData(newPost);
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();

        if (bindingResult.hasErrors()) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        PostDTO postUpdated = postService.updatePost(id, postRequest);

        if (postUpdated == null) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(postUpdated);
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatusPost(@RequestParam("pid") long postId, @RequestParam("sid") long statusId) {
        DataResponse dataResponse = new DataResponse();
        try {
            PostDTO postUpdated = postService.updateStatusPost(postId, statusId);
            dataResponse.setStatus(HttpStatus.OK.value());
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
            dataResponse.setSuccess(true);
            dataResponse.setData(postUpdated);
            return ResponseEntity.ok(dataResponse);
        } catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAllPosts(@PathVariable("id") long id,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        DataResponse dataResponse = new DataResponse();

        Streamable<PostDTO> postList = postService.getAllPosts(id, page, size);

        if (postList == null) {
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(null);

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(postList);

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(postService.getPostByPostId(postId));
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all-posts/aid={authorId}")
    public ResponseEntity<?> getAllPostsByAuthorId(@PathVariable Long authorId,
                                                   @RequestParam(value = "isApproved") boolean isApproved,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(postService.getAllPostByAuthorId(authorId,isApproved,page,size));

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all-posts/tpid={parentTopicId}")
    public ResponseEntity<?> getAllPostsByParentTopicId(@PathVariable Long parentTopicId, @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(postService.getAllPostsByParentTopicId(parentTopicId, page, size));

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable("id") long id) {
        DataResponse dataResponse = new DataResponse();

        boolean isRemoved = postService.removePostById(id);

        if (isRemoved == false) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("Can't found this post.");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData("Removed the post successfully.");

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/approve/{pid}")
    public ResponseEntity<?> approvePost(@PathVariable("pid") long postId) {
        DataResponse dataResponse = new DataResponse();

        PostPO postUpdated = postService.aprrovePost(postId);

        if (postUpdated == null) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData("Approved successfully");
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/reject")
    public ResponseEntity<?> rejectPost(@RequestBody RejectPostRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        if (bindingResult.hasErrors()) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        PostPO postUpdated = postService.rejectPost(request);

        if (postUpdated == null) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData("Reject successfully");
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all-posts/is-approved={isApproved}")
    public ResponseEntity<?> getAllPostsByIsAproved(@PathVariable boolean isApproved, @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(postService.getAllPostsByIsApproved(isApproved, page, size));

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/popular-posts")
    public ResponseEntity<?> getTop4PostsByIsAproved(@RequestParam(value = "isApproved") boolean isApproved) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(postService.getTop4PostsByIsApproved(isApproved));

        return ResponseEntity.ok(dataResponse);
    }
}
