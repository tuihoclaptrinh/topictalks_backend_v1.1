package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.payloads.requests.FriendRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;
import com.anonymity.topictalks.models.payloads.responses.FriendInforResponse;
import com.anonymity.topictalks.models.payloads.responses.FriendMentionResponse;
import com.anonymity.topictalks.services.IFriendService;
import com.anonymity.topictalks.utils.commons.ResponseData;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.controllers
 * - Created At: 10-10-2023 18:12:53
 * @since 1.0 - version of class
 */

@RestController
@RequestMapping("/api/v1/friends")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@Tag(name = "Friend Controller", description = "")
public class FriendController {

    @Autowired
    private IFriendService friendService;

    @PostMapping("/applyAddFriends")
    public ResponseEntity<?> applyAddFriends(@RequestBody FriendRequest request) {
        DataResponse dataResponse = new DataResponse();
        try {
            FriendInforResponse response = friendService.requestAddFriend(request);
            dataResponse.setStatus(HttpStatus.OK.value());
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
            dataResponse.setSuccess(true);
            dataResponse.setData(response);
            return ResponseEntity.ok(dataResponse);
        }catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

    @PostMapping("/acceptFriendsApply")
    public ResponseEntity<?> acceptFriendsApply(@RequestBody FriendRequest request) {
        DataResponse dataResponse = new DataResponse();
        try {
            FriendInforResponse response = friendService.acceptedRequestFriend(request);
            dataResponse.setStatus(HttpStatus.OK.value());
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
            dataResponse.setSuccess(true);
            dataResponse.setData(response);
            return ResponseEntity.ok(dataResponse);
        }catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

//    @GetMapping("/list")
//    public ResponseData listFriend(@RequestBody UserDTO userDTO) {
//        List<FriendResponse> friends = friendService.getAllFriend(userDTO);
//        return ResponseData.ofSuccess("success", friends);
//    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<?> listFriendByUserId(@PathVariable long userId) {
        DataResponse dataResponse = new DataResponse();

        List<FriendInforResponse> list = friendService.getAllFriendByUserId(userId);

        if (list.isEmpty()) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());
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

    @DeleteMapping("/rejectFriendsApply")
    public ResponseEntity<?> rejectFriendsApply(@RequestParam("uid") long userId, @RequestParam("fid") long friendId) {
        DataResponse dataResponse = new DataResponse();
        try {
            friendService.rejectFriendship(userId, friendId);
            dataResponse.setStatus(HttpStatus.OK.value());
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
            dataResponse.setSuccess(true);
            dataResponse.setData("Reject friend successfully");
            return ResponseEntity.ok(dataResponse);
        }catch (GlobalException e) {
            dataResponse.setStatus(e.getCode());
            dataResponse.setDesc(HttpStatus.valueOf(e.getCode()).getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(e.getMessage());
            return ResponseEntity.ok(dataResponse);
        }
    }

    @GetMapping("/mention/friends/{userId}")
    public ResponseData mentionFriends(@PathVariable("userId") Long userId) {
        try {
            List<FriendMentionResponse> lists = friendService.mentionFriends(userId);
            return ResponseData.ofSuccess("succeed", lists);
        } catch (Exception e) {
            return ResponseData.ofFailed("failed", ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

}
