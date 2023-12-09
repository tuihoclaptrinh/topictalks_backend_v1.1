package com.anonymity.topictalks.services;

import com.anonymity.topictalks.recommendation.DataSourceRec;
import com.anonymity.topictalks.recommendation.SlopeOneMatrix;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 07-12-2023 10:56:30
 * @since 1.0 - version of class
 */
public interface ISlopeOneRecommenderService {

    /**
     * Predicts one item i for user u using the SlopeOne Algorithms.
     */
    public double recommendOne(int u, int i);

}
