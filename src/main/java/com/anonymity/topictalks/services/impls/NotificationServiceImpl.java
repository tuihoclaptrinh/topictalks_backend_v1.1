package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IMessageRepository;
import com.anonymity.topictalks.daos.notification.IMessageNotificationRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.payloads.requests.NotiRequest;
import com.anonymity.topictalks.models.persists.notification.MessageNotificationPO;
import com.anonymity.topictalks.models.persists.notification.QMessageNotificationPO;
import com.anonymity.topictalks.services.INotificationService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private IUserRepository userRepository;
    @Autowired
    private IMessageRepository messageRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * @param request
     */
    @Override
    public void saveNotiMessage(NotiRequest request) {
        QMessageNotificationPO qMessageNotificationPO = QMessageNotificationPO.messageNotificationPO;
        MessageNotificationPO noti1 = jpaQueryFactory.select(qMessageNotificationPO)
                .from(qMessageNotificationPO)
                .where(qMessageNotificationPO.messageId.id.eq(request.getMessageId()))
                .fetchOne();
        if(noti1 == null) {
            MessageNotificationPO noti = MessageNotificationPO
                    .builder()
                    .id(request.getId())
                    .messageId(messageRepository.findById(request.getMessageId()).orElse(null))
                    .userId(userRepository.findById(request.getUserId()).orElse(null))
                    .build();
            messageNotificationRepository.save(noti);
        }
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<MessageNotificationPO> notiList(Long userId) {
        QMessageNotificationPO qMessageNotificationPO = QMessageNotificationPO.messageNotificationPO;
        List<MessageNotificationPO> listNotiUser = jpaQueryFactory.select(
                Projections.bean(qMessageNotificationPO)
        ).from(qMessageNotificationPO)
                .where(qMessageNotificationPO.userId.eq(userRepository.findById(userId).orElse(null)))
                .fetch();
        return listNotiUser;
    }
}
