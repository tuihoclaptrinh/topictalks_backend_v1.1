package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.user.IFriendListRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.FriendRequest;
import com.anonymity.topictalks.models.payloads.responses.FriendInforResponse;
import com.anonymity.topictalks.models.payloads.responses.FriendResponse;
import com.anonymity.topictalks.models.persists.user.FriendListPO;
import com.anonymity.topictalks.models.persists.user.QFriendListPO;
import com.anonymity.topictalks.models.persists.user.QUserPO;
import com.anonymity.topictalks.services.IFriendService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class FriendServiceImpl implements IFriendService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IFriendListRepository friendListRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    /**
     * @param request
     * @return
     */
    @Override
    public FriendInforResponse requestAddFriend(FriendRequest request) {

        QFriendListPO qFriendListPO = QFriendListPO.friendListPO;

        //Check if there is already an application
        FriendListPO friendListPO = jpaQueryFactory.select(qFriendListPO)
                .from(qFriendListPO)
                .where(qFriendListPO.userId.eq(userRepository.getOne(request.getUserId()))
                        .and(qFriendListPO.friendId.eq(userRepository.getOne(request.getFriendId()))))
                .fetchOne();

        if (null != friendListPO) {
            throw new GlobalException(400, "Already applied, please do not apply again");
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
        return  convertToFriendInfor(friendListRes);

    }

    /**
     * @param request
     * @return
     */
    @Override
    @Transactional
    public FriendInforResponse acceptedRequestFriend(FriendRequest request) {
        QFriendListPO qFriendListPO = QFriendListPO.friendListPO;
        //Update own data
        long execute01 = jpaQueryFactory.update(qFriendListPO)
                .set(qFriendListPO.isAccept, true)
                // Friend's remarks ... but not implement
                .where(qFriendListPO.userId.eq(userRepository.getOne(request.getUserId()))
                        .and(qFriendListPO.friendId.eq(userRepository.getOne(request.getFriendId()))))
                .execute();
        if (execute01 != 1) {
            throw new GlobalException(400, "operation failed");
        }
        FriendListPO friend = jpaQueryFactory.select(qFriendListPO)
                .from(qFriendListPO)
                .where(qFriendListPO.userId.eq(userRepository.getOne(request.getUserId()))
                        .and(qFriendListPO.friendId.eq(userRepository.getOne(request.getFriendId()))))
                .fetchOne();
        return convertToFriendInfor(friend);
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

    @Override
    public List<FriendInforResponse> getAllFriendByUserId(long userId) {
        List<FriendInforResponse> responsesList = new ArrayList<>();
//        List<FriendListPO> list = friendListRepository.findAllByUserId(userId);
        List<FriendListPO> list = friendListRepository.findAllByUserIdAndFriendId(userId,userId);
//        if (list.size() == 0) return list;
        for (FriendListPO friendListPO : list) {
            FriendInforResponse response = convertToFriendInfor(friendListPO);
            responsesList.add(response);
        }
        return responsesList;
    }

    @Override
    @Transactional
    public void rejectFriendship(long userId, long friendId) {
        QFriendListPO qFriendListPO = QFriendListPO.friendListPO;
        long deletedRows = jpaQueryFactory
                .delete(qFriendListPO)
                .where(qFriendListPO.userId.eq(userRepository.getOne(userId))
                        .and(qFriendListPO.friendId.eq(userRepository.getOne(friendId))))
                .execute();
        if (deletedRows ==0) {
            throw new GlobalException(400, "Reject failed");
        }
    }

    private FriendInforResponse convertToFriendInfor(FriendListPO friend) {
        FriendInforResponse response = new FriendInforResponse(
                friend.getId(),
                friend.getUserId().getId(),
                friend.getUserId().getNickName(),
                friend.getUserId().getImageUrl(),
                friend.getUserId().isActive(),
                friend.getFriendId().getId(),
                friend.getFriendId().getNickName(),
                friend.getFriendId().getImageUrl(),
                friend.getFriendId().isActive(),
                friend.getIsPublic(),
                friend.getIsAccept(),
                friend.getUpdatedAt(),
                friend.getCreatedAt()
        );
        return response;
    }


}
