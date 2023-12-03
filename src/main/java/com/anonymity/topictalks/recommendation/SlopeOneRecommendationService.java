package com.anonymity.topictalks.recommendation;

import com.anonymity.topictalks.models.persists.rating.RatingPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.recommendation
 * - Created At: 03-12-2023 14:14:52
 * @since 1.0 - version of class
 */

@Service
public class SlopeOneRecommendationService {
    public Map<TopicChildrenPO, Double> predictRatings(UserPO user, List<RatingPO> preferences) {
        Map<TopicChildrenPO, Double> predictions = new HashMap<>();
        Map<TopicChildrenPO, Integer> frequency = new HashMap<>();
        Map<TopicChildrenPO, Double> deviationsSum = new HashMap<>();

        // Initialize data structures
        for (RatingPO preference : preferences) {
            TopicChildrenPO item = preference.getTopicChildrenInfo();
            int rating = preference.getRating();

            for (RatingPO otherPreference : preferences) {
                if (!item.equals(otherPreference.getTopicChildrenInfo())) {
                    TopicChildrenPO otherItem = otherPreference.getTopicChildrenInfo();
                    int otherRating = otherPreference.getRating();

                    double diff = rating - otherRating;

                    // Update deviationsSum and frequency maps
                    deviationsSum.merge(otherItem, diff, Double::sum);
                    frequency.merge(otherItem, 1, Integer::sum);
                }
            }
        }

        // Calculate predictions
        for (TopicChildrenPO item : deviationsSum.keySet()) {
            double deviationSum = deviationsSum.get(item);
            int freq = frequency.get(item);

            double prediction = deviationSum / freq;
            predictions.put(item, prediction);
        }

        return predictions;
    }
}
