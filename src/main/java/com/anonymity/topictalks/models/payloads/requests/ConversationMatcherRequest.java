package com.anonymity.topictalks.models.payloads.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMatcherRequest implements Serializable {
    private long userIdInSession;
}
