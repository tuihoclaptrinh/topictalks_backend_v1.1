package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.dtos
 * - Created At: 25-10-2023 16:59:25
 * @since 1.0 - version of class
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDTO {
    private String from;
    private String to;
    private String subject;
    private String content;
    private Map<String, String> model;
}
