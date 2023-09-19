package com.anonymity.topictalks.utils.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.utils.commons
 * - Created At: 18-09-2023 16:29:00
 * @since 1.0 - version of class
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseData<T> implements Serializable {

    private String requestId;
    private Integer code;
    private String message;
    private String path;
    private T data;

    public static ResponseData ofSuccess(String message,Object data){
        return ResponseData.builder().code(200).message(message).data(data).build();
    }
    public static ResponseData ofFailed(String message,Object data){
        return ResponseData.builder().code(400).message(message).data(data).build();
    }

}
