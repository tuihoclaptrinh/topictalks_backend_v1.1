package com.anonymity.topictalks.daos.message;

import com.anonymity.topictalks.models.persists.message.MessageDemoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMessageDemoRepository extends JpaRepository<MessageDemoPO, Long> {
    List<MessageDemoPO> findAllByRoom(String room);
}
