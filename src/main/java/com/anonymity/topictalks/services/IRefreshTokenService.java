package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.payloads.requests.AuthenticationRequest;
import com.anonymity.topictalks.models.payloads.requests.RefreshTokenRequest;
import com.anonymity.topictalks.models.payloads.requests.RegisterRequest;
import com.anonymity.topictalks.models.payloads.responses.AuthenticationResponse;
import com.anonymity.topictalks.models.payloads.responses.RefreshTokenResponse;
import com.anonymity.topictalks.models.persists.user.RefreshTokenPO;

import java.util.Optional;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 15-09-2023 15:29:17
 * @since 1.0 - version of class
 */
public interface IRefreshTokenService {

    RefreshTokenPO createRefreshToken(Long userId);
    RefreshTokenPO verifyExpiration(RefreshTokenPO token);
    Optional<RefreshTokenPO> findByToken(String token);
    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);

}
