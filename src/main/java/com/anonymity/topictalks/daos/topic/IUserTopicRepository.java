package com.anonymity.topictalks.daos.topic;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.topic.UserTopicKey;
import com.anonymity.topictalks.models.persists.topic.UserTopicPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.topic
 * - Created At: 15-09-2023 09:35:29
 * @since 1.0 - version of class
 */

@Repository
public interface IUserTopicRepository extends IBaseRepository<UserTopicPO, UserTopicKey> {

}
