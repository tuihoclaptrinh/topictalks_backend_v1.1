package com.anonymity.topictalks.daos.message;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.ParticipantKey;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.message
 * - Created At: 15-09-2023 09:41:17
 * @since 1.0 - version of class
 */

@Repository
public interface IParticipantRepository extends IBaseRepository<ParticipantPO, ParticipantKey> {
    List<ParticipantPO> findAllByConversationInfo(ConversationPO conversationId);
    List<ParticipantPO> findAllByUserInfo(UserPO userPO);
}
