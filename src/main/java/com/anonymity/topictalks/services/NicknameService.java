package com.anonymity.topictalks.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.services
 * - Created At: 26-10-2023 14:50:44
 * @since 1.0 - version of class
 */

@Service
public class NicknameService {

    private List<String> nicknames = new ArrayList<>();

    public NicknameService() {
        loadNicknames();
    }

    private void loadNicknames() {
        try {
            ClassPathResource resource = new ClassPathResource("nicknames.properties");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2 && parts[0].equals("nicknames")) {
                    String[] values = parts[1].split(",");
                    nicknames = List.of(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateUserNickname() {
        Random random = new Random();
        String nickname = nicknames.get(random.nextInt(nicknames.size()));

        return nickname;
    }

}
