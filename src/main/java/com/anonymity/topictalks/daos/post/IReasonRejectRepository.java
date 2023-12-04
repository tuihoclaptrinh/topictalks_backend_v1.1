package com.anonymity.topictalks.daos.post;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.post.ReasonRejectPO;
import org.springframework.stereotype.Repository;

@Repository
public interface IReasonRejectRepository extends IBaseRepository<ReasonRejectPO, Long> {
}
