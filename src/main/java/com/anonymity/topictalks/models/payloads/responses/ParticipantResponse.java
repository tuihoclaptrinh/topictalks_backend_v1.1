package com.anonymity.topictalks.models.payloads.responses;

import com.anonymity.topictalks.models.dtos.PartnerDTO;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse implements Serializable {
    private ConversationPO conversationInfor;
    private PartnerDTO partnerDTO;
}
