package com.anonymity.topictalks.services;

import com.anonymity.topictalks.recommendation.DataSourceRec;

import java.util.HashMap;
import java.util.Map;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 07-12-2023 10:52:56
 * @since 1.0 - version of class
 */
public interface ISlopeOneMatrixService {

    public double getItemPairAverageDiff(Integer i, Integer j);

    /**
     * Returns the weight between items i and j
     */
    public int getWeight(Integer i, Integer j);

}
