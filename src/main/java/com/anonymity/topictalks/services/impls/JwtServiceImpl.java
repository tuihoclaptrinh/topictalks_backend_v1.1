package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.services.IJwtService;
import com.anonymity.topictalks.utils.components.JwtUtilComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 15-09-2023 22:14:32
 * @since 1.0 - version of class
 */
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {

    private final JwtUtilComponent jwtUtil;

    @Override
    public String extractUserName(String token) {
        return jwtUtil.extractUserName(token);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return jwtUtil.generateRefreshToken(userDetails);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return jwtUtil.isTokenValid(token, userDetails);
    }

}
