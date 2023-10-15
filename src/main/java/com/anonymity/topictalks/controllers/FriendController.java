package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.payloads.requests.FriendRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.payloads.responses.FriendInforResponse;
import com.anonymity.topictalks.services.IFriendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
            dataResponse.setStatus(HttpStatus.OK.value());//200
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
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
            dataResponse.setStatus(HttpStatus.OK.value());//200
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
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

        if (list == null) {//NO CONTENT
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//204
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NO CONTENT
            dataResponse.setSuccess(false);
            dataResponse.setData("Empty");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());//200
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
        dataResponse.setSuccess(true);
        dataResponse.setData(list);

        return ResponseEntity.ok(dataResponse);
    }

    @DeleteMapping("/rejectFriendsApply")
    public ResponseEntity<?> rejectFriendsApply(@RequestParam("uid") long userId, @RequestParam("fid") long friendId) {
        DataResponse dataResponse = new DataResponse();
        try {
            friendService.rejectFriendship(userId, friendId);
            dataResponse.setStatus(HttpStatus.OK.value());//200
            dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());//OK
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
}
