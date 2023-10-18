package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.payloads.requests.NotiRequest;
import com.anonymity.topictalks.models.payloads.responses.NotiResponse;
import com.anonymity.topictalks.models.persists.notification.MessageNotificationPO;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 16-10-2023 22:12:38
 * @since 1.0 - version of class
 */
public interface INotificationService {

    void saveNotiMessage(NotiRequest request);
    List<NotiResponse> notiList(Long userId);

    long updateReadNoti(Long notiId);

}
