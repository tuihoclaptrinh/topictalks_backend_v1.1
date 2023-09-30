package com.anonymity.topictalks.services;


import com.anonymity.topictalks.models.persists.user.UserPO;

import java.util.List;
import java.util.Optional;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 15-09-2023 15:27:26
 * @since 1.0 - version of class
 */
public interface IUserService {

    boolean updateAvatar(String avatar, long id);

    List<UserPO> findAllUsers();

    UserPO getUserById(long id);

    boolean remove(long id);

    UserPO updateUser(long id, UserPO userPO);

    UserPO banUser(long id);
}
