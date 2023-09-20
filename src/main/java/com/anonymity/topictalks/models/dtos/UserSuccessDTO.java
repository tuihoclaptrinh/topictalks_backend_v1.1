package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.dtos
 * - Created At: 20-09-2023 08:03:46
 * @since 1.0 - version of class
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSuccessDTO implements Serializable {

    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String token;

}
