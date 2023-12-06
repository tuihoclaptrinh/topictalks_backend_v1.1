package com.anonymity.topictalks.models.dtos;

import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.validations.annotations.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractDTO {
    private long id;
    private String subject;
    private String content;
    private UserQaDTO senderInfor;
    private boolean isAnswered;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("answerContent")
    private String answerContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("adminReplyId")
    private Long adminReplyId;
    private LocalDateTime createAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("updateAt")
    private LocalDateTime updateAt;

    // Custom getter for adminReplyId
    public Long getAdminReplyId() {
        // Check if adminReplyId is 0, return null to exclude it from serialization
        if (adminReplyId != null && adminReplyId == 0) {
            return null;
        }
        return adminReplyId;
    }
}
