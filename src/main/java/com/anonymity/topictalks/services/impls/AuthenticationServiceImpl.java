package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.CustomAuthenticationException;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.payloads.requests.AuthenticationGoogleRequest;
import com.anonymity.topictalks.models.payloads.requests.AuthenticationRequest;
import com.anonymity.topictalks.models.payloads.requests.RegisterRequest;
import com.anonymity.topictalks.models.payloads.responses.AuthenticationResponse;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.*;
import com.anonymity.topictalks.utils.EmailUtils;
import com.anonymity.topictalks.utils.OtpUtils;
import com.anonymity.topictalks.utils.enums.ERole;
import com.anonymity.topictalks.utils.enums.ETokenType;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

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
    private final OtpUtils otpUtils;
    private final EmailUtils emailUtils;
    private final NicknameService nicknameService;
    private final IUserService userService;

    @Override
    public Object register(RegisterRequest request) {
        userRepository.deleteUnVerifyUser();
        ErrorResponse error = new ErrorResponse();
        Optional<UserPO> user = userRepository.findByEmail(request.getEmail());
        if (!user.isEmpty()) {
            if (user.get().getEmail().equalsIgnoreCase(request.getEmail().toLowerCase(Locale.ROOT))) {
                error.setMessage("This email address has been exist another account.");
            }
            return error = ErrorResponse.builder()
                    .status(HttpServletResponse.SC_FORBIDDEN)
                    .error("Register failure")
                    .timestamp(LocalDateTime.now())
                    .message(error.getMessage())
                    .build();
        }
        String otp;
        try {
            otp = otpUtils.generateOtp();
            emailUtils.sendOtpEmail(request.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
        var random = new Random();
        var nickName = nicknameService.generateUserNickname();
        var userByNickname = userRepository.findByNickName(nickName);
        var new_user = new UserPO();
        if (userByNickname!=null) {
            new_user.setNickName(nickName + random.nextInt(100 - 1 + 1) + 1);
        } else {
            new_user.setNickName(nickName);
        }
        new_user.setFullName("");
        new_user.setUsername(new_user.getNickName());
        new_user.setEmail(request.getEmail().toLowerCase(Locale.ROOT));
        new_user.setDob(null);
        new_user.setPassword(passwordEncoder.encode(request.getPassword()));
        new_user.setVerify(false);
        new_user.setOtp(otp);
        new_user.setOtpGeneratedTime(LocalDateTime.now());
        new_user.setBio("");
        new_user.setImageUrlRandom(request.getUrlAvatar());
        new_user.setImageUrl(request.getUrlAvatar());
        new_user.setGender("");
        new_user.setNumDateBan(0);
        new_user.setPhoneNumber("");
        new_user.setCountry("");
        new_user.setIsBanned(false);
        new_user.setBannedDate(null);
        new_user.setNumDateBan(0);
        new_user.setRole(ERole.USER);
        new_user.setCreatedAt(LocalDateTime.now());
        new_user.setUpdatedAt(null);

        new_user = userRepository.save(new_user);

        var jwt = jwtService.generateToken(new_user);
        var refreshToken = refreshTokenService.createRefreshToken(new_user.getId());

        var roles = new_user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .username(new_user.getNickName())
                .id(new_user.getId())
                .url_img(new_user.getImageUrl())
                .url_img_random(new_user.getImageUrlRandom())
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .tokenType(ETokenType.BEARER.name())
                .build();

    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        userService.executeUpdateIsBannProcedure(request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new CustomAuthenticationException("Invalid username or password.", ex);
        }
        var user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        String jwt = jwtService.generateToken(user);

        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        if (user.isVerify()) {
            LocalDateTime dueDate = null;
            if (user.getIsBanned() == true) {
                dueDate = user.getBannedDate().plusDays(user.getNumDateBan());
            }
            return AuthenticationResponse.builder()
                    .accessToken(jwt)
                    .roles(roles)
                    .username(user.getNickName())
                    .url_img(user.getImageUrl())
                    .id(user.getId())
                    .isBanned(user.getIsBanned())
                    .dueDateUnBan(dueDate)
                    .bannedDate(user.getBannedDate())
                    .refreshToken(refreshToken.getToken())
                    .tokenType(ETokenType.BEARER.name())
                    .build();
        } else {
            return AuthenticationResponse.builder().build();
        }

    }

    @Override
    public Object authenticateGoogle(AuthenticationGoogleRequest request) {

        var user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            Optional<UserPO> user1 = (userRepository.findByEmail(request.getEmail()));
            if (!user1.isEmpty()) {
                if (user1.get().getEmail().equalsIgnoreCase(request.getEmail().toLowerCase(Locale.ROOT))) {
                    error.setMessage("This email address has been exist another account.");
                } else {
                    error.setMessage("This username has been exist another account.");
                }
                return error = ErrorResponse.builder()
                        .status(HttpServletResponse.SC_FORBIDDEN)
                        .error("Register failure")
                        .timestamp(LocalDateTime.now())
                        .message(error.getMessage())
                        .build();
            }
            var random = new Random();
            var nickName = nicknameService.generateUserNickname();
            
            var new_user = new UserPO();
            
            var userByNickname = userRepository.findByNickName(nickName);
            if (userByNickname!=null) {
                new_user.setNickName(nickName + random.nextInt(100 - 1 + 1) + 1);
            } else {
                new_user.setNickName(nickName);
            }
        
            new_user.setFullName(request.getFullName());
            new_user.setUsername(new_user.getNickName());
            new_user.setEmail(request.getEmail());
            new_user.setDob(null);
            new_user.setPassword("");
            new_user.setBio("");
            new_user.setImageUrl(request.getUrlImage());
            new_user.setImageUrlRandom(request.getUrlImage());
            new_user.setGender("");
            new_user.setPhoneNumber("");
            new_user.setCountry("");
            new_user.setIsBanned(false);
            new_user.setBannedDate(null);
            new_user.setNumDateBan(0);
            new_user.setRole(ERole.USER);
            new_user.setCreatedAt(LocalDateTime.now());
            new_user.setUpdatedAt(null);
            new_user.setVerify(true);

            new_user = userRepository.save(new_user);

            var jwt = jwtService.generateToken(new_user);
            var refreshToken = refreshTokenService.createRefreshToken(new_user.getId());

            var roles = new_user.getRole().getAuthorities()
                    .stream()
                    .map(SimpleGrantedAuthority::getAuthority)
                    .toList();

            return AuthenticationResponse.builder()
                    .accessToken(jwt)
                    .username(new_user.getNickName())
                    .id(new_user.getId())
                    .url_img(new_user.getImageUrl())
                    .url_img_random(new_user.getImageUrlRandom())
                    .refreshToken(refreshToken.getToken())
                    .roles(roles)
                    .tokenType(ETokenType.BEARER.name())
                    .build();
        }

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        String jwt = jwtService.generateToken(user);

        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .username(user.getNickName())
                .url_img(user.getImageUrl())
                .url_img_random(user.getImageUrlRandom())
                .id(user.getId())
                .isBanned(user.getIsBanned())
                .bannedDate(user.getBannedDate())
                .refreshToken(refreshToken.getToken())
                .tokenType(ETokenType.BEARER.name())
                .build();
    }

}
