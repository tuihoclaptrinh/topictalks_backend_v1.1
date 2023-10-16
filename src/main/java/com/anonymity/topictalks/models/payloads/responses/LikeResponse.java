package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.LikeListDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse implements Serializable {
    private long totalLike;
    private List<LikeListDTO> userLike;
}
