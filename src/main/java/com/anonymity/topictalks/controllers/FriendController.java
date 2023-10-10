package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.AddFriendRequest;
import com.anonymity.topictalks.models.payloads.responses.FriendResponse;
import com.anonymity.topictalks.services.IFriendService;
import com.anonymity.topictalks.utils.commons.ResponseData;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
@PreAuthorize("hasAnyRole('USER')")
@Tag(name = "Friend Controller", description = "")
public class FriendController {

    @Autowired
    private IFriendService friendService;
    @PostMapping("/applyAddFriends")
    public ResponseData applyAddFriends(@RequestBody AddFriendRequest request){
        friendService.requestAddFriend(request);
        return ResponseData.ofSuccess("Apply successful",null);
    }

    @PostMapping("/acceptFriendsApply")
    public ResponseData acceptFriendsApply(@RequestBody AddFriendRequest request){
        friendService.acceptedRequestFriend(request);
        return ResponseData.ofSuccess("success",null);
    }

    @GetMapping("/list")
    public ResponseData listFriend(@RequestBody UserDTO userDTO) {
        List<FriendResponse> friends = friendService.getAllFriend(userDTO);
        return ResponseData.ofSuccess("success", friends);
    }

}
