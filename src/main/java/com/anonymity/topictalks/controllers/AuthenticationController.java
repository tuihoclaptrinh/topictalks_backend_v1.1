package com.anonymity.topictalks.controllers;

import com.anonymity.topictalks.daos.rating.IRatingRepository;
import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.payloads.requests.*;
import com.anonymity.topictalks.models.payloads.responses.AuthenticationResponse;
import com.anonymity.topictalks.models.payloads.responses.RefreshTokenResponse;
import com.anonymity.topictalks.models.persists.rating.RatingPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.recommendation.*;
import com.anonymity.topictalks.services.IAuthenticationService;
import com.anonymity.topictalks.services.IRefreshTokenService;
import com.anonymity.topictalks.services.IUserService;
import com.anonymity.topictalks.utils.commons.ResponseData;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.controllers
 * - Created At: 15-09-2023 22:35:34
 * @since 1.0 - version of class
 */
@Tag(name = "Authentication", description = "The Authentication API. Contains operations like login, logout, refresh-token etc.")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final IAuthenticationService authenticationService;
    private final IRefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final DataSourceRec dataSourceRec;
    private final SlopeOneRecommendationService slopeOneRecommendationService;
    private final IRatingRepository ratingRepository;
    private final IUserRepository userRepository;
    private final ITopicChildrenRepository topicChildrenRepository;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/authenticate/google")
    public ResponseEntity<Object> loginGoogle(@RequestBody AuthenticationGoogleRequest request) {
        return ResponseEntity.ok(authenticationService.authenticateGoogle(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.generateNewToken(request));
    }

    @GetMapping("/info")
    public Authentication getAuthentication(@RequestBody AuthenticationRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    }

    @GetMapping("/verify-forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(userService.forgotEmail(email), HttpStatus.OK);
    }

    @GetMapping("/re-verify-link-token")
    public ResponseEntity<String> verifyLinkToken(@RequestParam String email) {
        return new ResponseEntity<>(userService.forgotEmail(email), HttpStatus.OK);
    }

    @PutMapping("/new-password")
    public ResponseEntity<String> setPassword(@RequestBody ResetPasswordRequest request) {
        return new ResponseEntity<>(userService.setPassword(request), HttpStatus.OK);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,
                                                @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @PostMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }

    @PostMapping("/user/rating")
    public ResponseEntity<RatingPO> saveRating(@RequestBody RatingPO ratingPO) {
        return new ResponseEntity<>(ratingRepository.save(ratingPO), HttpStatus.CREATED);
    }

    @GetMapping("/user/rating/all")
    public ResponseEntity<List<RatingPO>> getAllRatings() {
        return new ResponseEntity<>(ratingRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user/rating/{userId}")
    public ResponseEntity<List<RatingPO>> getAllRatingsByUser(@PathVariable("userId") int userId) {
        return new ResponseEntity<>(ratingRepository.findAllByUserInfo((long)userId), HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/topic/{topicId}/update/rating")
    public ResponseEntity<RatingPO> updateRating(@PathVariable("userId")int userId, @PathVariable("topicId") int topicId, @RequestBody RatingPO newRatingPO) {
        RatingPO ratingExist = ratingRepository.getRatingUpdate(userId, topicId);
        ratingExist.setRating(newRatingPO.getRating());
        return new ResponseEntity<>(ratingExist, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/recommend")
    public ResponseEntity<List<TopicChildrenPO>> recommendTopicChildren(@PathVariable int userId) {
        SlopeOneMatrix avgDiff = new SlopeOneMatrix(dataSourceRec, true);
        SlopeOneRecommender slopeOne = new SlopeOneRecommender(dataSourceRec, true, avgDiff);
        List<Integer> recommendedTopicChildren = new ArrayList<>();
        List<TopicChildrenPO> topicChildrenPOs = new ArrayList<>();


        int[] items = dataSourceRec.getItems();

        Map<Integer, Double> predictions = new HashMap<>();

        for (int item : items) {
            double prediction = slopeOne.recommendOne(userId, item);
            predictions.put(item, prediction);
            log.info("User: " + userId + ", Topic: " + item + ", Prediction: " + prediction);

        }
        Set<Integer> top5Keys = findTop5Entries(predictions).keySet();
        for(Integer key: top5Keys) {
            topicChildrenPOs.add(topicChildrenRepository.findById((long) key));
        }
        boolean b = recommendedTopicChildren.addAll(top5Keys);

        return new ResponseEntity<>(topicChildrenPOs, HttpStatus.OK);
    }

    @GetMapping("/evaluate/{userId}")
    public double evaluateRMSE(@PathVariable int userId) {
        SlopeOneMatrix avgDiff = new SlopeOneMatrix(dataSourceRec, true);
        SlopeOneRecommender slopeOne = new SlopeOneRecommender(dataSourceRec, true, avgDiff);
        ArrayList<Double> actualRatings = new ArrayList<>();
        ArrayList<Double> predictedRatings = new ArrayList<>();

        int[] items = dataSourceRec.getItems();

        for (int item : items) {
            double actualRating = dataSourceRec.getRating(userId, item);
            if (!Double.isNaN(actualRating)) {
                actualRatings.add(actualRating);

                double prediction = slopeOne.recommendOne(userId, item);
                predictedRatings.add(prediction);
            }
        }

        RMSE rmseCalculator = new RMSE();
        return rmseCalculator.evaluate(actualRatings, predictedRatings);
    }

    private Map<Integer, Double> findTop5Entries(Map<Integer, Double> map) {
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
        Map<Integer, Double> top5Entries = new LinkedHashMap<>();

        // Retrieve the top 5 entries and put them into the result map, excluding NaN and < 2 values
        int count = 0;
        for (Map.Entry<Double, Integer> entry : sortedMap.entrySet()) {
            Double value = entry.getKey();
            if (value != null && !value.isNaN() && value >= 2.0) {
                top5Entries.put(entry.getValue(), value);
                count++;
                if (count == 5) {
                    break; // Stop after adding the top 5 entries
                }
            }
        }

        return top5Entries;
    }

    @GetMapping("/user/{userId}/rec")
    public ResponseEntity<Map<TopicChildrenPO, Double>> getRecommendations(@PathVariable Long userId) {
        // Retrieve user preferences from the database
        List<RatingPO> userPreferences = ratingRepository.findAllByUserInfo(userId);

        // Get recommendations using the Slope One algorithm
        Map<TopicChildrenPO, Double> recommendations = slopeOneRecommendationService.predictRatings(userRepository.findById(userId).orElseThrow(), userPreferences);

        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

}
