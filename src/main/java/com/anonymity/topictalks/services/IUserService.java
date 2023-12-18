package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.GenderDTO;
import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.ResetPasswordRequest;
import com.anonymity.topictalks.models.payloads.requests.UserUpdateRequest;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 15-09-2023 15:27:26
 * @since 1.0 - version of class
 */
public interface IUserService {

    void executeUpdateIsBannProcedure(String username);

    String verifyAccount(String email, String otp);

    String forgotEmail(String email);

    String regenerateOtp(String email);

    String setPassword(ResetPasswordRequest request);

    boolean updateAvatar(String avatar, long id);

    boolean updateActive(boolean active, long id);

    Page<UserDTO> findAllUsers(int page, int size);

    Page<UserDTO> searchByNickName(String nickNameSearch,int page, int size);

    List<UserDTO> getAllUsersBanned(LocalDateTime bannedDate);

    UserDTO getUserById(long id);

    boolean remove(long id);

    Object updateUser(long id, UserUpdateRequest request);

    UserDTO banUser(long id, long numDateOfBan);

    UserDTO updateStatusProfile(long id, boolean isPublic);

    boolean checkDuplicateEmail(long id, String email);

    boolean checkDuplicateUsername(long id, String username);

    void unBanUser(long id);

    String verifyLinkToken(String email, String token);

    List<Integer> getAgeOfAllUsers();

    GenderDTO getAllGenderOfUser();

    UserPO getUserByEmail(String email);
}
