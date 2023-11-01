package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.PartnerDTO;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.models.payloads.responses
 * - Created At: 07-10-2023 22:17:43
 * @since 1.0 - version of class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRandomResponse implements Serializable {
    @JsonProperty("conversationRandomResponse")
    private ConversationRandomResponse conversationRandomResponse;
    @JsonProperty("partnerDTO")
    private List<PartnerDTO> partnerDTO;
}
