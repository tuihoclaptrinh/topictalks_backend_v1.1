package com.anonymity.topictalks.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartTopicInforDTO {
    private int topicId;
    private String topicName;
    private int totalGroupChat;
    private int totalPost;
}
