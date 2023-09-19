package com.anonymity.topictalks.daos.user;

import com.anonymity.topictalks.models.persists.user.RefreshTokenPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos.user
 * - Created At: 15-09-2023 09:33:42
 * @since 1.0 - version of class
 */

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshTokenPO, Long> {
    Optional<RefreshTokenPO> findByToken(String token);
}
