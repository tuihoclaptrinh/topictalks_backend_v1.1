package com.anonymity.topictalks.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.utils
 * - Created At: 06-10-2023 14:24:16
 * @since 1.0 - version of class
 */
@Component
public class OtpUtils {

    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);

        while (output.length() < 6) {
            output = "0" + output;
        }
        return output;
    }
}
