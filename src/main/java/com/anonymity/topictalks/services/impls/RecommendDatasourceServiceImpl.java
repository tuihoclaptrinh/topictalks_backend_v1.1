package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.rating.IRatingRepository;
import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.persists.rating.RatingPO;
import com.anonymity.topictalks.services.IRecDatasourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.recommendation
 * - Created At: 07-12-2023 10:31:49
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
public class RecommendDatasourceServiceImpl implements IRecDatasourceService {

    private final IUserRepository userRepository;
    private final ITopicChildrenRepository topicChildrenRepository;
    private final IRatingRepository ratingRepository;
//    private int numItems = -1, numUsers = -1, getUserItemRating = -1;
//    private HashMap<Integer, HashMap<Integer, Integer>> ratings = null;
//    private int[] items = null, users;

    // Get the total number of users
    public int getNumUsers() {
        int numUsers = -1;
        if (numUsers == -1) {
            try {
                numUsers = userRepository.getCountUsers();
            } catch (Exception e) {
                System.out.println("[ERR] Error in getNumUsers: " + e.getMessage());
            }
        }
        return numUsers;
    }

    // Get the total number of items
    public int getNumItems() {
        int numItems = -1;
        if (numItems == -1) {
            try {
                numItems = topicChildrenRepository.getCountTopics();
            } catch (Exception e) {
                System.out.println("[ERR] Error in getNumItems: " + e.getMessage());
            }
        }
        return numItems;
    }

    // Get the set of items
    public int[] getItems() {
        int[] items = null;
        if (items == null) {
            try {
                items = new int[getNumItems()];
                List<Integer> listItems = topicChildrenRepository.getTopicChildrenIds();
                for (int i = 0; i < listItems.size(); i++) {
                    items[i] = listItems.get(i);
                }
            } catch (Exception e) {
                System.out.println("[ERR] Error in getItems: " + e.getMessage());
            }
        }
        return items;
    }

    // Get the set of users
    public int[] getUsers() {
        int[] users = null;
        if (users == null) {
            try {
                users = new int[getNumUsers()];
                List<Integer> listUsers = userRepository.getUserIds();
                for (int i = 0; i < listUsers.size(); i++) {
                    users[i] = listUsers.get(i);
                }
            } catch (Exception e) {
                System.out.println("[ERR] Error in getUsers: " + e.getMessage());
            }
        }
        return users;
    }

    // Get the rating for item i for user u, if NaN is returned, the rating is non-existent.
    public double getRating(int u, int i) {
        int getUserItemRating = -1;
        try {
            getUserItemRating = ratingRepository.getRating(u, i);
            return getUserItemRating;
        } catch (Exception e) {
            System.out.println("[ERR] Error in getRating: " + e.getMessage());
            return Double.NaN;
        }
    }

    // Get the ratings represented in a nested HashMap
    public HashMap<Integer, HashMap<Integer, Integer>> getRatings() {
        HashMap<Integer, HashMap<Integer, Integer>> ratings = null;
        if (ratings == null) {
            try {
                List<RatingPO> listRatings = ratingRepository.findAll();
                ratings = new HashMap<Integer, HashMap<Integer, Integer>>();
                HashMap<Integer ,Integer> innerHashMap = null ;

                for (RatingPO ratingPO : listRatings) {
                    int item = Integer.parseInt(ratingPO.getTopicChildrenInfo().getId().toString());
                    int user = Integer.parseInt(ratingPO.getUserInfo().getId().toString());
                    int rating = ratingPO.getRating();

                    innerHashMap = ratings.get(item);

                    if(innerHashMap == null) {
                        innerHashMap = new HashMap<Integer, Integer>();
                    }

                    innerHashMap.put(user, rating);
                    ratings.put(item, innerHashMap);

                }

            } catch (Exception e) {
                System.out.println("[ERR] Error in getRatings: " + e.getMessage());
            }
        }
        return ratings;
    }

}
