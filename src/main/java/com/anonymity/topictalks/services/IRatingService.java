package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.payloads.requests.RatingIdRequest;
import com.anonymity.topictalks.models.payloads.requests.RatingRequest;
import com.anonymity.topictalks.models.payloads.responses.HotTopicResponse;
import com.anonymity.topictalks.models.payloads.responses.RatingResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 05-12-2023 15:35:34
 * @since 1.0 - version of class
 */

public interface IRatingService {

    RatingResponse ratingTopic(RatingRequest ratingRequest);

    List<RatingResponse> getAllRatings();

    List<RatingResponse> getAllRatingsByTopicChildren(Long tpcId);

    List<RatingResponse> getAllRatingsByUser(Long userId);

    RatingResponse getRatingsByUserAndTpc(Long userId, Long tpcId);

    RatingResponse updateRating(RatingRequest ratingRequest);

    List<HotTopicResponse> getAllHotTopics();

    BigDecimal avgRatingById(Long tpcId);

}
