package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.FriendRequest;
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

    FriendInforResponse requestAddFriend(FriendRequest request);

    FriendInforResponse acceptedRequestFriend(FriendRequest request);

    List<FriendResponse> getAllFriend(UserDTO user);

    List<FriendInforResponse> getAllFriendByUserId(long userId);

    void rejectFriendship(long userId, long friendId);
}
