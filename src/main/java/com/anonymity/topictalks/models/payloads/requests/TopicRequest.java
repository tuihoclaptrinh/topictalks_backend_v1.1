package com.anonymity.topictalks.models.payloads.requests;

import com.anonymity.topictalks.validations.annotations.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicRequest {
    private String topicName;
    private String shortDescript;
    private String urlImage;
}
