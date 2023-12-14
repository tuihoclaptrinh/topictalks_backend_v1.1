package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.recommendation.DataSourceRec;
import com.anonymity.topictalks.services.IRecDatasourceService;
import com.anonymity.topictalks.services.ISlopeOneMatrixService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 07-12-2023 10:54:03
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
public class SlopeOneMatrixServiceImpl implements ISlopeOneMatrixService {

    private final IRecDatasourceService dataSource;
    private HashMap<Integer, HashMap<Integer, Double>> itemAVGDiffMatrix = null;
    private HashMap<Integer, HashMap<Integer, Integer>> itemItemWeightMatrix = null;
    private boolean isWeighted = true;

    private void calcItemPairs() {
        int weight = 0;
        HashMap<Integer, Integer> innerHashMapWeight = null;
        HashMap<Integer, Double> innerHashMapAVG = null;

        if(isWeighted) {
            itemItemWeightMatrix = new HashMap<Integer, HashMap<Integer, Integer>>();
        }

        Integer ratingI = -1, ratingJ = -1, userI = -1, userJ = -1;

        int dev = 0;
        int sum = 0;
        int countSim = 0;
        Double average = 0.0;

        System.out.println("Now running : Calculate Item−Item Average Diff");

        //For all items, i
        for(int i = 1; i <= dataSource.getNumItems(); i++) {
            //For all other items, j
            for(int j = 1; j <= i; j++) {
                //For every user u expressing preference for both i and j
                try {
                    for(Map.Entry<Integer, Integer> entry: (dataSource.getRatings()).get(j).entrySet()) {
                        userJ = entry.getKey();
                        ratingJ = entry.getValue();

                        if(dataSource.getRatings().get(i).containsKey(userJ)) {
                            if(isWeighted) {
                                weight++;
                            }
                            if(i != j) {
                                userI = userJ;
                                ratingI = dataSource.getRatings().get(i).get(userI);

                                dev = ratingJ - ratingI;
                                sum += dev;
                                countSim++;
                            }
                        }
                    }
                } catch (Exception e) {
                }

                if(i != j) {
                    //Add the difference in u s preference for i and average
                    average = ((double) sum / (double) countSim);

                    innerHashMapAVG = itemAVGDiffMatrix.get(i);

                    if (innerHashMapAVG == null) {
                        innerHashMapAVG = new HashMap<Integer, Double>();
                    }
                }

                if(isWeighted) {
                    innerHashMapWeight = itemItemWeightMatrix.get(i);
                    if(innerHashMapWeight == null) {
                        innerHashMapWeight = new HashMap<Integer, Integer>();
                        itemItemWeightMatrix.put(i, innerHashMapWeight);
                    }
                    innerHashMapWeight.put(j, weight);
                    weight = 0;
                }

                if(i != j) {
                    innerHashMapAVG.put(j, average);

                    //Put the deviation average in a matrix for the items
                    itemAVGDiffMatrix.put(i, innerHashMapAVG);
                    countSim = 0;
                    sum = 0;
                }
            }
        }
    }

    public double getItemPairAverageDiff(Integer i, Integer j) {
        calcItemPairs();
        HashMap<Integer, Double> outerHashMapI = itemAVGDiffMatrix.get(i);
        HashMap<Integer, Double> outerHashMapJ = itemAVGDiffMatrix.get(j);

        double avgDiff = 0.0;

        if(outerHashMapI != null && !outerHashMapI.isEmpty() && outerHashMapI.containsKey(j)) {
            //If itemI < itemJ return the item else return the negation
            if(i<j) {
                avgDiff = -outerHashMapI.get(j);
            } else {
                avgDiff = outerHashMapI.get(j);
            }
        } else if(outerHashMapJ != null && !outerHashMapJ.isEmpty() && outerHashMapJ.containsKey(i)) {
            if(i<j) {
                avgDiff = -outerHashMapJ.get(i);
            } else {
                avgDiff = outerHashMapJ.get(i);
            }
        }

        //If none of the class applies above, the average difference is 0
        return avgDiff;
    }

    /**
     * Returns the weight between items i and j
     */
    public int getWeight(Integer i, Integer j) {
        calcItemPairs();
        HashMap<Integer, Integer> outerHashMap = itemItemWeightMatrix.get(i);

        int weight = 0;

        if(outerHashMap != null && !outerHashMap.isEmpty() && outerHashMap.containsKey(j)) {
            weight = outerHashMap.get(j);
        } else {
            outerHashMap = itemItemWeightMatrix.get(j);
            if(outerHashMap != null && !outerHashMap.isEmpty() && outerHashMap.containsKey(i)) {
                weight = outerHashMap.get(i);
            }
        }

        return weight;
    }

}
