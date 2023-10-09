package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.payloads.requests.AuthenticationGoogleRequest;
import com.anonymity.topictalks.models.payloads.requests.AuthenticationRequest;
import com.anonymity.topictalks.models.payloads.requests.RegisterRequest;
import com.anonymity.topictalks.models.payloads.responses.AuthenticationResponse;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 15-09-2023 15:29:00
 * @since 1.0 - version of class
 */
public interface IAuthenticationService {

    Object register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    Object authenticateGoogle(AuthenticationGoogleRequest request);

}
