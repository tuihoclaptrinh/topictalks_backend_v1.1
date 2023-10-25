package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.UserUpdateRequest;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IUserService;
import com.anonymity.topictalks.utils.EmailUtils;
import com.anonymity.topictalks.utils.OtpUtils;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
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
    public String verifyAccount(String email, String otp) {
        UserPO user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            userRepository.save(user);
            return "OTP verified you can login";
        }
        return "Please regenerate otp and try again";
    }

    @Override
    public String regenerateOtp(String email) {
        UserPO user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtils.generateOtp();
        try {
            emailUtils.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }

    /**
     * @param email
     * @return
     */
    @Override
    public String forgotPassword(String email) {
        UserPO user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: {}" + email));

        try {
            emailUtils.sendSetPasswordEmail(email);
        } catch (MessagingException ex) {
            throw new RuntimeException("Unable to send set password email please try again");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Please check your email to set new password to your account";
    }

    /**
     * @param email
     * @param newPassword
     * @return
     */
    @Override
    public String setPassword(String email, String newPassword) {
        UserPO user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: {}" + email));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "New password set successfully !";
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
    public UserDTO banUser(long id) {
        boolean isExisted = userRepository.existsById(id);
        if (isExisted) {
            UserPO userPO = userRepository.findById(id).orElse(null);
            userPO.setBannedDate(LocalDateTime.now());
            userPO.setIsBanned(true);
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
        userDTO.setCreatedAt(userPO.getCreatedAt());
        userDTO.setUpdatedAt(userPO.getUpdatedAt());
        userDTO.setIsBanned(userPO.getIsBanned());
        userDTO.setBannedDate(userPO.getBannedDate());

        return userDTO;
    }
}
