package com.anonymity.topictalks.daos.user;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.user.UserPO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.user
 * - Created At: 15-09-2023 09:32:05
 * @since 1.0 - version of class
 */

@Repository
public interface IUserRepository extends IBaseRepository<UserPO, Long> {

    @Procedure("DELETE_UNVERIFIED_USERS_PROCEDURE")
    void deleteUnVerifyUser();
  
    @Procedure("UPDATE_IS_BAN_USER_PROCEDURE")
    void updateIsBannProcedure(String username);

    Optional<UserPO> findByUsername(String username);

    Optional<UserPO> findByEmail(String email);

    Optional<UserPO> findById(Long id);

    Optional<UserPO> getUserByUsernameOrEmail(String username, String mail);

    List<UserPO> findAllByBannedDate(LocalDateTime bannedDate);

    Optional<UserPO> findByNickname(String nickname);

}
