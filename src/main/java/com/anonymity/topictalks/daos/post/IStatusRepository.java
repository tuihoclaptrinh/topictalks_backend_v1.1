package com.anonymity.topictalks.daos.post;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.post.StatusPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IStatusRepository extends IBaseRepository<StatusPO, Long> {
    @Query(value = "SELECT * FROM status s WHERE s.id = :statusId", nativeQuery = true)
    StatusPO findById(@Param(value = "statusId") long statusId);
}
