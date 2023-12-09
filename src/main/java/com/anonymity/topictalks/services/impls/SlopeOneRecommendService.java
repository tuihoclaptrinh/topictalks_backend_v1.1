package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.recommendation.DataSourceRec;
import com.anonymity.topictalks.recommendation.SlopeOneMatrix;
import com.anonymity.topictalks.services.IRecDatasourceService;
import com.anonymity.topictalks.services.ISlopeOneMatrixService;
import com.anonymity.topictalks.services.ISlopeOneRecommenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 07-12-2023 10:57:16
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
public class SlopeOneRecommendService implements ISlopeOneRecommenderService {

    private final IRecDatasourceService dataSource;
    private final ISlopeOneMatrixService soMatrix;
    boolean isWeighted = true;

    /**
     * Predicts one item i for user u using the SlopeOne Algorithms.
     */
    public double recommendOne(int u, int i) {
        double difference = 0.0, userRatingSum = 0.0, prediction = 0.0;
        int weight = 0, weightSum = 0, numRatings = 0;

        //For every item j that user u has rated
        try {
            for(int j = 1; j <= dataSource.getNumItems(); j++) {
                if(dataSource.getRatings().get(j).get(u) != null && i != j) {
                    if(isWeighted) {
                        //Find the weight between j and i
                        weight = soMatrix.getWeight(i, j);
                        //Find the average rating difference between j and i
                        difference += soMatrix.getItemPairAverageDiff(j, i) * weight;
                        //Find the sum of ratings for j
                        userRatingSum += dataSource.getRatings().get(j).get(u) * weight;
                        //Calculate the weight sum
                        weightSum += weight;
                    } else {
                        difference += soMatrix.getItemPairAverageDiff(j, i);
                        userRatingSum += dataSource.getRatings().get(j).get(u);
                        //Calculate the number of ratings u has rated
                        numRatings++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ERR] ERROR in SlopeOneRecommender recommendOne: "+ e.getMessage());
        }

        //Calculate the prediction
        if(isWeighted) {
            prediction = (double) ((userRatingSum + difference) / weightSum);
        } else {
            prediction = (double) ((userRatingSum + difference) / numRatings);
        }
        return prediction;
    }

}
