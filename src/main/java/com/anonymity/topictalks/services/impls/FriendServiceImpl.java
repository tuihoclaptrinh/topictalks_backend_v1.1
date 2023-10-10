package com.anonymity.topictalks.services.impls;

import com.alibaba.fastjson.JSONObject;
import com.anonymity.topictalks.daos.user.IFriendListRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.listeners.SocketEventListener;
import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.AddFriendRequest;
import com.anonymity.topictalks.models.payloads.responses.AddFriendResponse;
import com.anonymity.topictalks.models.payloads.responses.FriendResponse;
import com.anonymity.topictalks.models.persists.user.FriendListPO;
import com.anonymity.topictalks.models.persists.user.QFriendListPO;
import com.anonymity.topictalks.models.persists.user.QUserPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IFriendService;
import com.corundumstudio.socketio.SocketIOClient;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 10-10-2023 10:28:41
 * @since 1.0 - version of class
 */

@Service
@Slf4j
public record FriendServiceImpl(
        IUserRepository userRepository,
        IFriendListRepository friendListRepository,
        JPAQueryFactory jpaQueryFactory
)
        implements IFriendService {


    /**
     * @param request
     * @return
     */
    @Override
    public void requestAddFriend(AddFriendRequest request) {

        QFriendListPO qFriendListPO = QFriendListPO.friendListPO;

        //Check if there is already an application
        FriendListPO friendListPO = jpaQueryFactory.select(qFriendListPO)
                .from(qFriendListPO)
                .where(qFriendListPO.userId.eq(userRepository.getOne(request.getUserId()))
                        .and(qFriendListPO.friendId.eq(userRepository.getOne(request.getFriendId()))))
                .fetchOne();

        if(null != friendListPO){
            throw new GlobalException(400,"Already applied, please do not apply again");
        }

        // Applicant's friend information
        FriendListPO userfriendListPO = FriendListPO.builder()
                .userId(userRepository.getOne(request.getUserId()))
                .friendId(userRepository.getOne(request.getFriendId()))
                .isPublic(false)
                .isAccept(false)
                .build();

//        SocketIOClient socketIOClient = SocketEventListener.clientMap.get(request.getUserId());
//        if(socketIOClient != null){
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("message","You have a new friend request");
//            log.info("Client notified");
//            socketIOClient.sendEvent("newFriendsNotify",jsonObject);
//        }
        var friendListRes = friendListRepository.save(userfriendListPO);

    }

    /**
     * @param request
     * @return
     */
    @Override
    public void acceptedRequestFriend(AddFriendRequest request) {
        QFriendListPO qFriendListPO = QFriendListPO.friendListPO;
        //Update own data
        long execute01 = jpaQueryFactory.update(qFriendListPO)
                .set(qFriendListPO.isAccept, true)
                // Friend's remarks ... but not implement
                .where(qFriendListPO.userId.eq(userRepository.getOne(request.getUserId()))
                        .and(qFriendListPO.friendId.eq(userRepository.getOne(request.getFriendId()))))
                .execute();
        if(execute01 != 1) {
            throw new GlobalException(400,"operation failed");
        }
    }

    /**
     * @param user
     * @return
     */
    @Override
    public List<FriendResponse> getAllFriend(UserDTO user) {

        QFriendListPO qFriendListPO = QFriendListPO.friendListPO;
        QUserPO qUserPO = QUserPO.userPO;

        List<FriendResponse> lists = jpaQueryFactory.select(
                Projections.bean(FriendResponse.class,
                        qFriendListPO.friendId.as("friendId"),
                        qUserPO.fullName,
                        qUserPO.username,
                        qUserPO.email,
                        qUserPO.phoneNumber,
                        qUserPO.dob,
                        qUserPO.bio,
                        qUserPO.gender,
                        qUserPO.country,
                        qUserPO.imageUrl,
                        qUserPO.isBanned,
                        qUserPO.bannedDate,
                        qUserPO.role,
                        qUserPO.createdAt,
                        qUserPO.updatedAt
                        )
        )
                .from(qFriendListPO)
                .leftJoin(qUserPO)
                .on(qFriendListPO.userId.id.eq(qUserPO.id))
                .where(qFriendListPO.userId.id.eq(user.getId()))
                .fetch();

        return lists;
    }
}
