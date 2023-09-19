package com.anonymity.topictalks.services;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 15-09-2023 15:28:01
 * @since 1.0 - version of class
 */
public interface IJwtService {

    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

}
