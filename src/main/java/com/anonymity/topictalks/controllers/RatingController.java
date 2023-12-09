package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.models.payloads.requests.RatingRequest;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;
import com.anonymity.topictalks.models.payloads.responses.HotTopicResponse;
import com.anonymity.topictalks.models.payloads.responses.RatingResponse;
import com.anonymity.topictalks.services.IRatingService;
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
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.controllers
 * - Created At: 07-12-2023 18:26:35
 * @since 1.0 - version of class
 */

@RestController
@RequestMapping("/api/v1/ratings")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Rating", description = "")
@RequiredArgsConstructor
@Slf4j
public class RatingController {

    private final IRatingService ratingService;

    @PostMapping("/topic")
    public ResponseData ratingOnTopic(@RequestBody RatingRequest ratingRequest) {
        try {
            RatingResponse savedRating = ratingService.ratingTopic(ratingRequest);
            return ResponseData.ofSuccess("succeed", savedRating);
        } catch (Exception e) {
            return ResponseData.ofFailed(e.getMessage(), ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/all/topics")
    public ResponseData fetchAllRatings() {
        try {
            return ResponseData.ofSuccess("succeed", ratingService.getAllRatings());
        } catch (Exception e) {
            return ResponseData.ofFailed(e.getMessage(), ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @PutMapping("/update")
    public ResponseData updateRatings(@RequestBody RatingRequest ratingRequest) {
        RatingResponse updatedRating = null;
        try {
            updatedRating = ratingService.updateRating(ratingRequest);
            return ResponseData.ofSuccess("success", updatedRating);
        } catch (Exception e) {
            if(updatedRating == null && ratingRequest.getRating() == 0) {
                return ResponseData.ofSuccess("success", "removed rating!");
            }
            return ResponseData.ofFailed(e.getMessage(), ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/all/topics/hot")
    public ResponseData fetchAllHotTopics() {
        try {
            List<HotTopicResponse> hotTopics = ratingService.getAllHotTopics();
            return ResponseData.ofSuccess("success", hotTopics);
        } catch (Exception e) {
            return ResponseData.ofFailed(e.getMessage(), ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/all/tpc/{tpcId}")
    public ResponseData fetchAllRatingsByTpc(@PathVariable("tpcId") Long tpcId) {
        try {
            List<RatingResponse> ratingsByTpc = ratingService.getAllRatingsByTopicChildren(tpcId);
            return ResponseData.ofSuccess("success", ratingsByTpc);
        } catch (Exception e) {
            return ResponseData.ofFailed(e.getMessage(), ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/all/usr/{userId}")
    public ResponseData fetchAllRatingsByUser(@PathVariable("userId") Long userId) {
        try {
            List<RatingResponse> ratingsByUsr = ratingService.getAllRatingsByUser(userId);
            return ResponseData.ofSuccess("success", ratingsByUsr);
        } catch (Exception e) {
            return ResponseData.ofFailed(e.getMessage(), ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @GetMapping("/all/usr/{userId}/tpc/{tpcId}")
    public ResponseData fetchRatingByKey(@PathVariable("userId") Long userId, @PathVariable("tpcId") Long tpcId) {
        try {
            RatingResponse ratingByKey = ratingService.getRatingsByUserAndTpc(userId, tpcId);
            return ResponseData.ofSuccess("success", ratingByKey);
        } catch (Exception e) {
            return ResponseData.ofFailed(e.getMessage(), ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

}
