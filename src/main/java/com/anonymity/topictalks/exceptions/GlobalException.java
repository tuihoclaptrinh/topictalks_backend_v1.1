package com.anonymity.topictalks.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.exceptions
 * - Created At: 15-09-2023 15:35:36
 * @since 1.0 - version of class
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class GlobalException extends RuntimeException {
    /**
     *
     */
    private Integer code;
    /**
     *
     */
    private String message;

}
