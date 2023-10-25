package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.services.IPostService;
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
        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        PostPO newPost = postService.createPost(request);
        dataResponse.setStatus(HttpStatus.CREATED.value()); //201
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());//CREATED
        dataResponse.setData(newPost);
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();

        if (bindingResult.hasErrors()) {//BAD REQUEST
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());//400
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());//BAD REQUEST
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        PostDTO postUpdated = postService.updatePost(id, postRequest);

        if (postUpdated == null) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
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
            dataResponse.setStatus(HttpStatus.OK.value());//200
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
            dataResponse.setSuccess(true);
            dataResponse.setData(postUpdated);
            return ResponseEntity.ok(dataResponse);
        } catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());//200
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());//OK
            dataResponse.setSuccess(false);
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("{id}/all")
    public ResponseEntity<?> getAllPosts(@PathVariable long id) {
        DataResponse dataResponse = new DataResponse();

        List<PostDTO> postList = postService.getAllPosts(id);

        if (postList == null) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());//204
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("Currently, this user haven't created any posts yet.");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(postList);

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(postService.getPostByPostId(postId));
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all-posts/aid={authorId}")
    public ResponseEntity<?> getAllPostsByAuthorId(@PathVariable Long authorId) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
//        dataResponse.setData(postService.getAllPostByAuthorIdAndRole(authorId));
        dataResponse.setData(postService.getAllPostByAuthorId(authorId));

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all-posts/tpid={parentTopicId}")
    public ResponseEntity<?> getAllPostsByParentTopicId(@PathVariable Long parentTopicId) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(postService.getAllPostsByParentTopicId(parentTopicId));

        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable("id") long id) {
        DataResponse dataResponse = new DataResponse();

        boolean isRemoved = postService.removePostById(id);

        if (isRemoved == false) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("Cann't found this post.");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
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

        if (postUpdated == null) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData("Approved successfully");
        return ResponseEntity.ok(dataResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/all-posts/is-approved={isApproved}")
    public ResponseEntity<?> getAllPostsByIsAproved(@PathVariable boolean isApproved) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(postService.getAllPostsByIsApproved(isApproved));

        return ResponseEntity.ok(dataResponse);
    }
}
