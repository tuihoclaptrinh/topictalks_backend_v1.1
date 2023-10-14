package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.AddFriendRequest;
import com.anonymity.topictalks.models.payloads.responses.AddFriendResponse;
import com.anonymity.topictalks.models.payloads.responses.FriendInforResponse;
import com.anonymity.topictalks.models.payloads.responses.FriendResponse;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 10-10-2023 10:13:42
 * @since 1.0 - version of class
 */
public interface IFriendService {

    FriendInforResponse requestAddFriend(AddFriendRequest request);

    FriendInforResponse acceptedRequestFriend(AddFriendRequest request);

    List<FriendResponse> getAllFriend(UserDTO user);

    List<FriendInforResponse> getAllFriendByUserId(long userId);

}
