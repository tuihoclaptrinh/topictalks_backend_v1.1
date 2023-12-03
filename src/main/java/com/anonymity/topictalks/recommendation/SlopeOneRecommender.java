package com.anonymity.topictalks.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A SlopeOne Recommender System. Takes input in constructor: DataSource, boolean to specify if
 * weighted version is used and SlopeOneMatrix to get the matrices that SlopeOne uses in the algorithms.
 */

public class SlopeOneRecommender {

    private DataSourceRec dataSource;
    boolean isWeighted;
    SlopeOneMatrix soMatrix;

    public SlopeOneRecommender(DataSourceRec dataSource, boolean isWeighted, SlopeOneMatrix soMatrix) {
        this.dataSource = dataSource;
        this.isWeighted = isWeighted;
        this.soMatrix = soMatrix;
    }

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
