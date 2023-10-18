package com.anonymity.topictalks.daos.user;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.user.InteractPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInteractRepository extends IBaseRepository<InteractPO, Long> {
    InteractPO save(InteractPO interactPO);

    List<InteractPO> findBySenderId(UserPO userPO);
}
