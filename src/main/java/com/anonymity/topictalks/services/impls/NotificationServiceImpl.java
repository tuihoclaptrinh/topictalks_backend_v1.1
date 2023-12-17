package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
import com.anonymity.topictalks.daos.notification.IMessageNotificationRepository;
import com.anonymity.topictalks.daos.notification.IPostNotificationRepository;
import com.anonymity.topictalks.daos.post.IPostRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.payloads.requests.NotiRequest;
import com.anonymity.topictalks.models.payloads.responses.NotiResponse;
import com.anonymity.topictalks.models.payloads.responses.RatingResponse;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.message.QConversationPO;
import com.anonymity.topictalks.models.persists.message.QMessagePO;
import com.anonymity.topictalks.models.persists.notification.MessageNotificationPO;
import com.anonymity.topictalks.models.persists.notification.PostNotificationPO;
import com.anonymity.topictalks.models.persists.notification.QMessageNotificationPO;
import com.anonymity.topictalks.models.persists.notification.QPostNotificationPO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.rating.RatingPO;
import com.anonymity.topictalks.models.persists.user.QUserPO;
import com.anonymity.topictalks.services.INotificationService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 16-10-2023 22:18:54
 * @since 1.0 - version of class
 */
@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private IMessageNotificationRepository messageNotificationRepository;
    @Autowired
    private IPostNotificationRepository postNotificationRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IParticipantRepository participantRepository;
    @Autowired
    private IMessageRepository messageRepository;
    @Autowired
    private IConversationRepository conversationRepository;
    @Autowired
    private IPostRepository postRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * @param request
     */
    @Override
    public void saveNotification(NotiRequest request) {
        if (request.getConversationId() != null) {
            ConversationPO conversationPO = conversationRepository.findById(request.getConversationId()).orElse(null);
            if (conversationPO != null) {
                if (conversationPO.getIsGroupChat() == false) {
                    MessageNotificationPO noti = MessageNotificationPO
                            .builder()
                            .partnerId(request.getPartnerId())
                            .conversationId(conversationRepository.findById(request.getConversationId()).orElse(null))
                            .userId(userRepository.findById(request.getUserId()).orElse(null))
                            .messageNoti(request.getMessageNoti())
                            .build();
                    messageNotificationRepository.save(noti);
                } else {
                    MessageNotificationPO noti = MessageNotificationPO
                            .builder()
                            .partnerId(request.getPartnerId())
                            .conversationId(conversationRepository.findById(request.getConversationId()).orElse(null))
                            .userId(userRepository.findById(request.getUserId()).orElse(null))
                            .messageNoti(request.getMessageNoti())
                            .build();
                    messageNotificationRepository.save(noti);
                }
            }
        } else if (request.getPostId() != null) {
            PostPO postPO = postRepository.findById(request.getPostId()).orElse(null);
            PostNotificationPO post = PostNotificationPO
                    .builder()
                    .postId(postPO)
                    .userId(userRepository.findById(request.getUserId()).orElse(null))
                    .messageNoti(request.getMessageNoti())
                    .partnerId(postPO.getAuthorId().getId())
                    .build();
            postNotificationRepository.save(post);
        }
    }

    @Override
    public Integer saveMentionNotif(List<NotiRequest> requests) {
        int result = 0;
        try {
            List<PostNotificationPO> entities = requests.stream()
                    .map(this::convertToPostNotificationPO)
                    .collect(Collectors.toList());

            result = postNotificationRepository.saveAll(entities).size();

            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * @param userId
     * @return
     */
//    @Override
//    public List<NotiResponse> notiList(Long userId) {
//        QMessageNotificationPO qMessageNotificationPO = QMessageNotificationPO.messageNotificationPO;
//        QMessagePO qMessagePO = QMessagePO.messagePO;
//        QConversationPO qConversationPO = QConversationPO.conversationPO;
//        QUserPO qUserPO = QUserPO.userPO;
//        List<NotiResponse> listNotiUser = jpaQueryFactory.select(
//                Projections.bean(NotiResponse.class,
//                        qMessageNotificationPO.id.as("notiId"),
//                        qMessageNotificationPO.userId.id.as("userId"),
//                        qUserPO.username.as("username"),
//                        qConversationPO.chatName.as("chatName"),
//                        qMessageNotificationPO.messageId.id.as("messageId"),
//                        qMessagePO.conversationId.id.as("conversationId"),
//                        qConversationPO.isGroupChat.as("isGroupChat"),
//                        qMessageNotificationPO.isRead.as("isRead"))
//        ).from(qMessageNotificationPO)
//                .join(qMessagePO)
//                .on(qMessageNotificationPO.messageId.id.eq(qMessagePO.id))
//                .join(qConversationPO)
//                .on(qMessagePO.conversationId.id.eq(qConversationPO.id))
//                .join(qUserPO)
//                .on(qMessageNotificationPO.userId.id.eq(qUserPO.id))
//                .where(qMessageNotificationPO.userId.eq(userRepository.findById(userId).orElse(null)))
//                .fetch();
//        return listNotiUser;
//    }
    @Override
    public List<NotiResponse> notiList(Long userId) {
        List<PostNotificationPO> postNoti = postNotificationRepository.findAllByUserId(userId);
        List<MessageNotificationPO> messNoti = messageNotificationRepository.findAllByUserId(userId);
        List<NotiResponse> list = new ArrayList<>();

        for (PostNotificationPO post : postNoti) {
            NotiResponse response = new NotiResponse();
            response.setNotiId(post.getId());
            response.setUserId(post.getUserId().getId());
            response.setUsername(post.getUserId().getNickName());
            response.setPartnerId(post.getPartnerId());
            response.setPartnerUsername(post.getPostId().getAuthorId().getNickName());
            response.setMessage(post.getMessageNoti());
            response.setPostId(post.getPostId().getId());
            response.setPostImage(post.getPostId().getImage());
            response.setIsRead(post.getIsRead());
            response.setCreateAt(post.getCreatedAt());
            list.add(response);
        }
        for (MessageNotificationPO message : messNoti) {
            NotiResponse response = new NotiResponse();
            response.setNotiId(message.getId());
            response.setUserId(message.getUserId().getId());
            response.setUsername(message.getUserId().getNickName());
            response.setPartnerId(message.getPartnerId());
            response.setPartnerUsername(userRepository.findById(message.getPartnerId()).get().getNickName());
            response.setMessage(message.getMessageNoti());
            response.setChatName(message.getConversationId().getChatName());
            response.setConversationId(message.getConversationId().getId());
            response.setIsGroupChat(message.getConversationId().getIsGroupChat());
            response.setGroupImage(message.getConversationId().getAvatarGroupImg());
            response.setIsRead(message.getIsRead());
            response.setCreateAt(message.getCreatedAt());
            list.add(response);
        }
        Collections.sort(list, Comparator.comparing(NotiResponse::getCreateAt));
        return list;
    }

    /**
     * @param notiId
     */
    @Override
    @Transactional
    public long updateReadNoti(Long notiId) {
        QMessageNotificationPO qMessageNotificationPO = QMessageNotificationPO.messageNotificationPO;
        QPostNotificationPO qPostNotificationPO = QPostNotificationPO.postNotificationPO;

        boolean isFoundInMessageNotification = jpaQueryFactory.selectOne()
                .from(qMessageNotificationPO)
                .where(qMessageNotificationPO.id.eq(notiId))
                .fetchFirst() != null;

        if (isFoundInMessageNotification) {
            jpaQueryFactory.update(qMessageNotificationPO)
                    .where(qMessageNotificationPO.id.eq(notiId))
                    .set(qMessageNotificationPO.isRead, true)
                    .execute();
        } else {
            jpaQueryFactory.update(qPostNotificationPO)
                    .where(qPostNotificationPO.id.eq(notiId))
                    .set(qPostNotificationPO.isRead, true)
                    .execute();
        }
        return 1;
    }

    private List<PostNotificationPO> convertToPostNotificationPO(List<NotiRequest> requests){
        List<PostNotificationPO> postNotificationPOS = new ArrayList<>();

        for(NotiRequest request: requests) {
            PostNotificationPO po = convertToPostNotificationPO(request);
            postNotificationPOS.add(po);
        }

        return postNotificationPOS;
    }

    private PostNotificationPO convertToPostNotificationPO(NotiRequest request) {
        return PostNotificationPO.builder()
                .userId(userRepository.findById(request.getUserId()).orElse(null))
                .postId(postRepository.findById(request.getPostId()).orElse(null))
                .messageNoti(request.getMessageNoti())
                .partnerId(request.getPartnerId())
                .build();
    }
}
