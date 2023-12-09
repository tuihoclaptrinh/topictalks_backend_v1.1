package com.anonymity.topictalks.services;

import java.util.HashMap;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.recommendation
 * - Created At: 07-12-2023 10:30:06
 * @since 1.0 - version of class
 */


public interface IRecDatasourceService {

    // Get the total number of users
    public int getNumUsers();

    // Get the total number of items
    public int getNumItems();

    // Get the set of items
    public int[] getItems();

    // Get the set of users
    public int[] getUsers();

    // Get the rating for item i for user u, if NaN is returned, the rating is non-existent.
    public double getRating(int u, int i);

    // Get the ratings represented in a nested HashMap
    public HashMap<Integer, HashMap<Integer, Integer>> getRatings();

}
