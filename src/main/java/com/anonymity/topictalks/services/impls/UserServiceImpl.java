package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.ResetPasswordRequest;
import com.anonymity.topictalks.models.payloads.requests.UserUpdateRequest;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IUserService;
import com.anonymity.topictalks.utils.EmailUtils;
import com.anonymity.topictalks.utils.Md5Utils;
import com.anonymity.topictalks.utils.OtpUtils;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private OtpUtils otpUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void executeUpdateIsBannProcedure(String username) {
        userRepository.updateIsBannProcedure(username);
    }

    @Override
    public String verifyAccount(String email, String otp) {
        UserPO user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {

            user.setVerify(true);
            user.setOtp(null);
            user.setOtpGeneratedTime(null);
            try {
                emailUtils.sendActiveAccount(email);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to active account please try again");
            } catch (TemplateException | IOException e) {
                throw new RuntimeException(e);
            }
            userRepository.save(user);
            return "OTP verified you can login";
        } else {
            log.info("OTP: {}", otp);
            log.info("Duration: {}", Duration.between(user.getOtpGeneratedTime(),
                    LocalDateTime.now()).getSeconds()<60);
            return "Please regenerate otp and try again";
        }

    }

    /**
     * @param email
     * @return
     */
    @Override
    public String forgotEmail(String email) {
        UserPO user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String token = Md5Utils.encrypt(email);
        user.setTokenForgotPassword(token);
        user.setTokenGeneratedTime(LocalDateTime.now());
        try {
            emailUtils.sendForgotEmail(email, token);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to active account please try again");
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
        userRepository.save(user);
        return "Email sent... please access link reset account within 1 minute";
    }

    @Override
    public String regenerateOtp(String email) {
        UserPO user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if(!user.isVerify()) {
            String otp = otpUtils.generateOtp();
            try {
                emailUtils.sendOtpEmail(email, otp);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send otp please try again");
            } catch (TemplateException | IOException e) {
                throw new RuntimeException(e);
            }
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);
            return "Email sent... please verify account within 1 minute";
        } else {
            return "This account has been verified.";
        }
    }

    /**
     * @param request
     * @return
     */
    @Override
    public String setPassword(ResetPasswordRequest request) {
        UserPO user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: {}" + request.getEmail()));

        if(user.getTokenForgotPassword().equals(request.getToken())) {
            if (Duration.between(user.getTokenGeneratedTime(), LocalDateTime.now()).getSeconds() < (5 * 60)) {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                user.setTokenForgotPassword(null);
                user.setTokenGeneratedTime(null);
                userRepository.save(user);
                try {
                    emailUtils.sendSetPassword(request.getEmail());
                } catch (MessagingException e) {
                    throw new RuntimeException("Unable to send link reset please try again");
                } catch (TemplateException | IOException e) {
                    throw new RuntimeException(e);
                }
                return "New password set successfully !";
            }
            return "Link reset is expired!";
        }
        return "Wrong token!";


    }

    @Override
    public boolean updateAvatar(String avatar, long id) {
        UserPO userPO = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist."));
        userPO.setImageUrl(avatar);
        try {
            userRepository.save(userPO);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    @Override
    public boolean updateActive(boolean active, long id) {
        UserPO userPO = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist."));
        userPO.setActive(active);
        try {
            userRepository.save(userPO);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<UserPO> userPOList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserPO list : userPOList) {
            UserDTO userDTO = convertUserPOToUserDTO(list);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    /**
     * @param bannedDate
     * @return
     */
    @Override
    public List<UserDTO> getAllUsersBanned(LocalDateTime bannedDate) {
        List<UserPO> userPOList = userRepository.findAllByBannedDate(bannedDate);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserPO list : userPOList) {
            UserDTO userDTO = convertUserPOToUserDTO(list);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public UserDTO getUserById(long id) {
        UserPO userPO = userRepository.findById(id).orElse(null);
        return userPO != null ? convertUserPOToUserDTO(userPO) : null;
    }

    @Override
    public boolean remove(long id) {
        boolean exist = userRepository.existsById(id);
        if (exist) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkDuplicateEmail(long id, String email) {
        UserDTO userDTO = getUserById(id);
        if (!userDTO.getEmail().toLowerCase(Locale.ROOT).equalsIgnoreCase(email.toLowerCase(Locale.ROOT)) &&
                !userRepository.findByEmail(email).isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkDuplicateUsername(long id, String username) {
        UserDTO userDTO = getUserById(id);
        if (!userDTO.getUsername().toLowerCase(Locale.ROOT).equalsIgnoreCase(username.toLowerCase(Locale.ROOT))
                && !userRepository.findByUsername(username).isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param id
     */
    @Override
    public void unBanUser(long id) {
        boolean isExisted = userRepository.existsById(id);

        if(isExisted) {
            UserPO userPO = userRepository.findById(id).orElse(null);
            userPO.setIsBanned(false);
            userRepository.save(userPO);
        }
    }

    /**
     * @param email
     * @param token
     * @return
     */
    @Override
    public String verifyLinkToken(String email, String token) {
        UserPO userPO = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("This user doesn't exist."));
        if (userPO.getTokenForgotPassword().equals(token) && Duration.between(userPO.getTokenGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            return "Link verify OK";
        }
        return "Link verify out of time";
    }

    @Override
    public Object updateUser(long id, UserUpdateRequest request) {
        boolean isExisted = userRepository.existsById(id);

        if (isExisted) {
            UserPO userPO = userRepository.findById(id).orElse(null);
            userPO.setId(id);
            String email = userPO.getEmail();
            userPO.setFullName(request.getFullName());
            if (request.getEmail() != null) {
                userPO.setEmail(request.getEmail());
            } else {
                userPO.setEmail(email);
            }
//            String pattern = "yyyy-MM-dd";
//            String fixedTime = "00:00:00";
//            try {
//                LocalDateTime dateTime = LocalDateTime.parse(request.getDob() + "T" + fixedTime, DateTimeFormatter.ofPattern(pattern + "'T'" + "HH:mm:ss"));
//                LocalDateTime instant = dateTime.toInstant(ZoneOffset.UTC);
            userPO.setDob(request.getDob());
//            } catch (DateTimeParseException e) {
//                System.out.println("Error parsing the date string: " + e.getMessage());
//                throw new GlobalException(e.getErrorIndex(), e.getMessage());
//            }
            userPO.setCountry(request.getCountry());
            userPO.setPhoneNumber(request.getPhoneNumber());
            userPO.setBio(request.getBio());
            userPO.setGender(request.getGender());
            userPO.setUpdatedAt(LocalDateTime.now());
            return convertUserPOToUserDTO(userRepository.save(userPO));
        }
        return null;
    }

    @Override
    public UserDTO banUser(long id, long numDateOfBan) {
        boolean isExisted = userRepository.existsById(id);
        if (isExisted) {
            UserPO userPO = userRepository.findById(id).orElse(null);
            userPO.setBannedDate(LocalDateTime.now());
            userPO.setIsBanned(true);
            userPO.setNumDateBan(numDateOfBan);
            userPO.setUpdatedAt(LocalDateTime.now());

            return convertUserPOToUserDTO(userRepository.save(userPO));
        }
        return null;
    }

    public UserDTO convertUserPOToUserDTO(UserPO userPO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userPO.getId());
        userDTO.setFullName(userPO.getFullName());
        userDTO.setUsername(userPO.getUsername());
        userDTO.setEmail(userPO.getEmail());
        userDTO.setPhoneNumber(userPO.getPhoneNumber());
        userDTO.setDob(userPO.getDob());
        userDTO.setBio(userPO.getBio());
        userDTO.setCountry(userPO.getCountry());
        userDTO.setGender(userPO.getGender());
        userDTO.setImageUrl(userPO.getImageUrl());
        userDTO.setRole(userPO.getRole());
        userDTO.setActive(userPO.isActive());
        userDTO.setCreatedAt(userPO.getCreatedAt());
        userDTO.setUpdatedAt(userPO.getUpdatedAt());
        userDTO.setIsBanned(userPO.getIsBanned());
        userDTO.setBannedDate(userPO.getBannedDate());

        return userDTO;
    }
}
