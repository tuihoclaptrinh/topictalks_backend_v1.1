package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.UserUpdateRequest;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;

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

    private UserDTO convertUserPOToUserDTO(UserPO userPO) {
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
        userDTO.setBanned(userPO.getIsBanned());
        userDTO.setBannedDate(userPO.getBannedDate());

        return userDTO;
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
                !userRepository.findByUsername(email).isEmpty()) {
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

    @Override
    public Object updateUser(long id, UserUpdateRequest request) {
        boolean isExisted = userRepository.existsById(id);

        if (isExisted) {
            UserPO userPO = userRepository.findById(id).orElse(null);
            userPO.setId(id);
            userPO.setFullName(request.getFullName());
            userPO.setEmail(request.getEmail());
            userPO.setUsername(request.getUsername());
            userPO.setDob(request.getDob());
            userPO.setCountry(request.getCountry());
            userPO.setPhoneNumber(request.getPhoneNumber());
            userPO.setBio(request.getBio());
            userPO.setImageUrl(request.getAvatar());
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
            userPO.setBannedDate(Instant.now());
            userPO.setIsBanned(true);
            userPO.setUpdatedAt(LocalDateTime.now());

            return convertUserPOToUserDTO(userRepository.save(userPO));
        }
        return null;
    }
}
