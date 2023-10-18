package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.requests.NotiRequest;
import com.anonymity.topictalks.services.INotificationService;
import com.anonymity.topictalks.utils.commons.ResponseData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.controllers
 * - Created At: 16-10-2023 22:32:24
 * @since 1.0 - version of class
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "MessageNoti", description = "The Notification API")
public class INotificationController {

    private final INotificationService notificationService;

    @PostMapping("/pushNoti")
    public ResponseData saveNotiMessage(@RequestBody NotiRequest request) {
        notificationService.saveNotiMessage(request);
        return ResponseData.ofSuccess("success", "save notification successfully");
    }

    @GetMapping("/{userId}")
    public ResponseData getAllNotiByUserId(@PathVariable("userId") Long userId) {
        return ResponseData.ofSuccess("success", notificationService.notiList(userId));
    }

    @PutMapping("/{notiId}")
    public ResponseData readNotification(@PathVariable("notiId") Long notiId) {
        return ResponseData.ofSuccess("success", "Read {} notification!"+  notificationService.updateReadNoti(notiId));
    }

}
