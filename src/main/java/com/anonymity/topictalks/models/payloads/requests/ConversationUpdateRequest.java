package com.anonymity.topictalks.models.payloads.requests;

import com.anonymity.topictalks.validations.annotations.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationUpdateRequest implements Serializable {
    @NullOrNotBlank
    private long newTopicId;
    @NullOrNotBlank
    private String avatarImg;
    private long userIdUpdate;
}
