package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.payloads.requests.AuthenticationRequest;
import com.anonymity.topictalks.models.payloads.requests.RegisterRequest;
import com.anonymity.topictalks.models.payloads.responses.AuthenticationResponse;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IAuthenticationService;
import com.anonymity.topictalks.services.IJwtService;
import com.anonymity.topictalks.services.IRefreshTokenService;
import com.anonymity.topictalks.utils.enums.ERole;
import com.anonymity.topictalks.utils.enums.ETokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 15-09-2023 15:34:42
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, GlobalException.class})
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final IRefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        var user = UserPO.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isBanned(false)
                .phoneNumber(request.getPhoneNumber())
                .role(ERole.USER)
                .build();


        user = userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .username(user.getUsername())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .tokenType(ETokenType.BEARER.name())
                .build();

    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        String jwt = jwtService.generateToken(user);

        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .username(user.getUsername())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType(ETokenType.BEARER.name())
                .build();

    }

}
