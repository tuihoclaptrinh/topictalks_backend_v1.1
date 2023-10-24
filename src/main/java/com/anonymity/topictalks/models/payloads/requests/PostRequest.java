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
public class PostRequest {
    private Long tparent_id;
    private Long author_id;
    private String title;
    private String content;
    private String image;
    @NullOrNotBlank
    private String status_id;
}
