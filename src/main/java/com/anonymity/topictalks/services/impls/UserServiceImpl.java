package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public List<UserPO> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserPO getUserById(long id) {
        return userRepository.findById(id).orElse(null);
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
    public UserPO updateUser(long id, UserPO userPO) {
        boolean isExisted = userRepository.existsById(id);
        if (isExisted) {
            userPO.setId(id);
            return userRepository.save(userPO);
        }
        return null;
    }

    @Override
    public UserPO banUser(long id) {
        boolean isExisted = userRepository.existsById(id);
        if (isExisted) {
            UserPO userPO = getUserById(id);
            userPO.setBannedDate(Instant.now());
            userPO.setIsBanned(true);
            return userRepository.save(userPO);
        }
        return null;
    }
}
