package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.payloads.requests.NotiRequest;
import com.anonymity.topictalks.models.payloads.requests.RatingRequest;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.INotificationService;
import com.anonymity.topictalks.utils.commons.ResponseData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.controllers
 * - Created At: 16-10-2023 22:32:24
 * @since 1.0 - version of class
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "MessageNoti", description = "The Notification API")
public class NotificationController {

    private final INotificationService notificationService;
    private final IUserRepository userRepository;

    @PostMapping("/pushNoti")
    public ResponseData saveNotification(@RequestBody NotiRequest request) {
        notificationService.saveNotification(request);
        return ResponseData.ofSuccess("success", "save notification successfully");
    }

    @PostMapping("/pushMentionNoti")
    public ResponseData saveMentionNotif(@RequestBody List<NotiRequest> request) {
        try {
            UserPO user = userRepository.findById(request.get(0).getUserId()).orElseThrow(null);
            if (!user.isVerify()) {
                return ResponseData.ofFailed("error", ErrorResponse.builder()
                        .message("Your account is not verify!")
                        .timestamp(LocalDateTime.now())
                        .build());
            }
            Integer result = notificationService.saveMentionNotif(request);
            log.info("You have notification " + result);
            return ResponseData.ofSuccess("succeed", "added notification.");
        } catch (Exception e) {
            return ResponseData.ofFailed(e.getMessage(), ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/{userId}")
    public ResponseData getAllNotiByUserId(@PathVariable("userId") Long userId) {
        return ResponseData.ofSuccess("success", notificationService.notiList(userId));
    }

    @PutMapping("/{notiId}")
    public ResponseData readNotification(@PathVariable("notiId") Long notiId) {
        return ResponseData.ofSuccess("success", "Read {} notification!" + notificationService.updateReadNoti(notiId));
    }

}
