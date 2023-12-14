package com.anonymity.topictalks.models.payloads.requests;

import com.anonymity.topictalks.validations.annotations.NullOrNotBlank;
import com.anonymity.topictalks.validations.annotations.StrongPassword;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.requests
 * - Created At: 15-09-2023 15:30:52
 * @since 1.0 - version of class
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NullOrNotBlank(message = "Username cannot be null")
    private String username;

    @Email
    @NullOrNotBlank(message = "User email cannot be null")
    private String email;

    @NullOrNotBlank(message = "Password cannot be null")
    @StrongPassword
    private String password;

}
