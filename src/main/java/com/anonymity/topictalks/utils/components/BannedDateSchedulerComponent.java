package com.anonymity.topictalks.utils.components;

import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.utils.components
 * - Created At: 25-10-2023 15:58:19
 * @since 1.0 - version of class
 */

@Component
@RequiredArgsConstructor
public class BannedDateSchedulerComponent {

    private final IUserService userService;

    @Scheduled(cron = "0 0 0 * * *") // Schedule to run every midnight
    public void checkHandleBannedDates() {
        LocalDateTime currentDay = LocalDateTime.now();
        List<UserDTO> users = userService.getAllUsersBanned(currentDay);

        for (UserDTO entity : users) {
            userService.unBanUser(entity.getId()); // Call the API to update ban status
        }
    }

}
