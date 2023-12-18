package com.anonymity.topictalks;

import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.recommendation.DataSourceRec;
import com.anonymity.topictalks.recommendation.RMSE;
import com.anonymity.topictalks.recommendation.SlopeOneMatrix;
import com.anonymity.topictalks.recommendation.SlopeOneRecommender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TopicTalksBackEndApplicationTests {

    @Autowired
    private DataSourceRec dataSourceRec;

    @Test
    public void a() {
        SlopeOneMatrix avgDiff = new SlopeOneMatrix(dataSourceRec, true);
        SlopeOneRecommender slopeOne = new SlopeOneRecommender(dataSourceRec, true, avgDiff);
        RMSE rmse = new RMSE();

        double prediction = 0.0;
        double rating = 0.0;
        ArrayList<Double> predictions = new ArrayList<Double>();
        ArrayList<Double> ratings = new ArrayList<Double>();
        int count = 0;
        //Iterate all users
        for(int userId: dataSourceRec.getUsers()) {
            //Iterate all topics
            for(int i = 1; i <= dataSourceRec.getNumItems(); i++) {
                //Get a prediction
                prediction = slopeOne.recommendOne(userId, i);
                //Get the actual value
                rating = dataSourceRec.getRating(userId, i);
                System.out.println("1. User: "+ userId + ", Topic: " + i + ", Prediction: " + prediction);
                //Rating and Prediction is NaN if rating does not exist
                //Or if a user only has rated one movie
                if(!Double.isNaN(rating) && !Double.isNaN(prediction)) {
                    System.out.println("2. User: "+ userId + ", Topic: " + i + ", Prediction: " + prediction);
                    ratings.add(rating);
                    predictions.add(prediction);
                    count++;
                }
            }
        }
        System.out.println();
        System.out.println("Ratings: " + ratings);
        System.out.println("Predictions: " + predictions);
        System.out.println("RMSE: " + rmse.evaluate(ratings, predictions));
        System.out.println("count: " + count);

    }

}
