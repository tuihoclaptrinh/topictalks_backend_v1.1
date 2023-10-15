package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.ConversationDTO;
import com.anonymity.topictalks.models.dtos.PartnerDTO;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ParticipantResponse implements Serializable {
    private ConversationDTO conversationInfor;

    private List<PartnerDTO> partnerDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String isMember;
}
