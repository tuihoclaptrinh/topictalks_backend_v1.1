package com.anonymity.topictalks.models.payloads.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicChildrenRequest {
    private long topicParentId;
    private String topicChildrenName;
    private String image;
}
