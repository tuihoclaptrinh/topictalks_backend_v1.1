package com.anonymity.topictalks.utils;

import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.utils
 * - Created At: 22-09-2023 18:28:54
 * @since 1.0 - version of class
 */

@Component
public class RandomUserUtils {

    public Map<Long, Long> randomUserChatting(ParticipantRequest request) {

        Map<Long, LocalDateTime> instantMap = new HashMap<>();

        // ISO 8601
        for (Map.Entry<Long, String> entry : request.getUserInfoRequest().entrySet()) {
            Long key = entry.getKey();
            String dateString = entry.getValue();
            LocalDateTime instant = LocalDateTime.parse(dateString);
            instantMap.put(key, instant);
        }

        List<Map.Entry<Long, LocalDateTime>> entries = new ArrayList<>(instantMap.entrySet());

        // Sort the entries by Instant in ascending order
        entries.sort(Comparator.comparing(Map.Entry::getValue));

        List<Long> userValues = new ArrayList<>();
        int count = 0;

        for (Map.Entry<Long, LocalDateTime> entry : entries) {
            userValues.add(entry.getKey());
            count++;

            if (count >= request.getAmount()) {
                break;
            }
        }

        // If the amount is odd, remove the last element from userValues
        if (request.getAmount() % 2 != 0 && userValues.size() > 0) {
            userValues.remove(userValues.size() - 1);
        }

        // Create a map with pairs of user IDs as keys
        Map<Long, Long> result = new LinkedHashMap<>();

        for (int i = 0; i < userValues.size(); i += 2) {
            if (i + 1 < userValues.size()) {
                Long user1 = userValues.get(i);
                Long user2 = userValues.get(i + 1);
                result.put(user1, user2);
            }
        }

        return result;
    }

}
