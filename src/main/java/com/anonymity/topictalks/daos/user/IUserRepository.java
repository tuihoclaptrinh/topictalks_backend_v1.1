package com.anonymity.topictalks.daos.user;

import com.anonymity.topictalks.daos.IBaseRepository;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

    Optional<UserPO> findByUsername(String username);

    Optional<UserPO> findById(Long id);

    Optional<UserPO> getUserByUsernameOrEmail(String username, String mail);

}
