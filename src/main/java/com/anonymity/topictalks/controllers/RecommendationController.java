package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.daos.rating.IRatingRepository;
import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;
import com.anonymity.topictalks.models.payloads.responses.RecommendTopicResponse;
import com.anonymity.topictalks.models.persists.rating.RatingPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.recommendation.DataSourceRec;
import com.anonymity.topictalks.recommendation.SlopeOneMatrix;
import com.anonymity.topictalks.recommendation.SlopeOneRecommendationService;
import com.anonymity.topictalks.recommendation.SlopeOneRecommender;
import com.anonymity.topictalks.services.*;
import com.anonymity.topictalks.utils.commons.ResponseData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.controllers
 * - Created At: 07-12-2023 18:23:47
 * @since 1.0 - version of class
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommends")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Recommend", description = "")
@Slf4j
public class RecommendationController {

    private final DataSourceRec dataSourceRec;
    private final ITopicChildrenService topicChildrenService;
    private final IRatingService ratingService;

    @GetMapping("/user/{userId}")
    public ResponseData recommendTopicChildren(@PathVariable int userId) {
        SlopeOneMatrix avgDiff = new SlopeOneMatrix(dataSourceRec, true);
        SlopeOneRecommender slopeOne = new SlopeOneRecommender(dataSourceRec, true, avgDiff);
        try {
            List<Integer> recommendedTopicChildren = new ArrayList<>();
            List<RecommendTopicResponse> responses = new ArrayList<>();

            int[] items = dataSourceRec.getItems();

            Map<Integer, Double> predictions = new HashMap<>();

            for (int item : items) {
                double prediction = slopeOne.recommendOne(userId, item);
                predictions.put(item, prediction);
            }

            Set<Integer> top7Keys = findTop7Entries(predictions).keySet();
            for(Integer key: top7Keys) {
                TopicChildrenPO data = topicChildrenService.getTopicChildrenById((long) key);
                RecommendTopicResponse recommendTopicResponse = RecommendTopicResponse.builder()
                        .topicChildrenId(data.getId())
                        .topicChildrenName(data.getTopicChildrenName())
                        .image(data.getImage())
                        .shortDescription(data.getShortDescript())
                        .avgRating(ratingService.avgRatingById(data.getId()))
                        .createdAt(data.getCreatedAt())
                        .updatedAt(data.getUpdatedAt())
                        .build();
                responses.add(recommendTopicResponse);
            }
            boolean b = recommendedTopicChildren.addAll(top7Keys);

            return ResponseData.ofSuccess("succeed", responses);
        } catch (Exception e) {
            return ResponseData.ofFailed("failed", ErrorResponse.builder()
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<TopicChildrenPO>> recommendTopicChildren(@PathVariable int userId) {
//        List<Integer> recommendedTopicChildren = new ArrayList<>();
//        List<TopicChildrenPO> topicChildrenPOs = new ArrayList<>();
//
//        int[] items = datasourceService.getItems();
//
//        Map<Integer, Double> predictions = new HashMap<>();
//
//        for (int item : items) {
//            double prediction = slopeOneRecommenderService.recommendOne(userId, item);
//            predictions.put(item, prediction);
//            log.info("User: " + userId + ", Topic: " + item + ", Prediction: " + prediction);
//
//        }
//        Set<Integer> top7Keys = findTop7Entries(predictions).keySet();
//        for(Integer key: top7Keys) {
//            topicChildrenPOs.add(topicChildrenRepository.findById((long) key));
//        }
//        boolean b = recommendedTopicChildren.addAll(top7Keys);
//
//        return new ResponseEntity<>(topicChildrenPOs, HttpStatus.OK);
//    }

//    @GetMapping("/evaluate/{userId}")
//    public double evaluateRMSE(@PathVariable int userId) {
//        ArrayList<Double> actualRatings = new ArrayList<>();
//        ArrayList<Double> predictedRatings = new ArrayList<>();
//
//        int[] items = dataSourceRec.getItems();
//
//        for (int item : items) {
//            double actualRating = dataSourceRec.getRating(userId, item);
//            if (!Double.isNaN(actualRating)) {
//                actualRatings.add(actualRating);
//
//                double prediction = slopeOne.recommendOne(userId, item);
//                predictedRatings.add(prediction);
//            }
//        }
//
//        RMSE rmseCalculator = new RMSE();
//        return rmseCalculator.evaluate(actualRatings, predictedRatings);
//    }

    private Map<Integer, Double> findTop7Entries(Map<Integer, Double> map) {
        // Create a TreeMap to automatically sort the entries based on values in descending order
        TreeMap<Double, Integer> sortedMap = new TreeMap<>(Collections.reverseOrder());

        // Populate the TreeMap with the entries from the original map, excluding NaN values
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            Double value = entry.getValue();
            if (value != null && !value.isNaN() && value >= 2.0) {
                sortedMap.put(value, entry.getKey());
            }
        }

        // Create a LinkedHashMap to maintain the order of insertion
        Map<Integer, Double> top7Entries = new LinkedHashMap<>();

        // Retrieve the top 7 entries and put them into the result map, excluding NaN and < 2 values
        int count = 0;
        for (Map.Entry<Double, Integer> entry : sortedMap.entrySet()) {
            Double value = entry.getKey();
            if (value != null && !value.isNaN() && value >= 2.0) {
                top7Entries.put(entry.getValue(), value);
                count++;
                if (count == 7) {
                    break; // Stop after adding the top 7 entries
                }
            }
        }

        return top7Entries;
    }
//
//    @GetMapping("/user/{userId}/rec")
//    public ResponseEntity<Map<TopicChildrenPO, Double>> getRecommendations(@PathVariable Long userId) {
//        // Retrieve user preferences from the database
//        List<RatingPO> userPreferences = ratingRepository.findAllByUserInfo(userId);
//
//        // Get recommendations using the Slope One algorithm
//        Map<TopicChildrenPO, Double> recommendations = slopeOneRecommendationService.predictRatings(userRepository.findById(userId).orElseThrow(), userPreferences);
//
//        return new ResponseEntity<>(recommendations, HttpStatus.OK);
//    }

}
