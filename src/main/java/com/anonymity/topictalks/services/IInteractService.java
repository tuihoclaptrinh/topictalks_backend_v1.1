package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.InteractDTO;
import com.anonymity.topictalks.models.payloads.requests.QARequest;
import com.anonymity.topictalks.models.payloads.requests.ReplyQARequest;

import java.util.List;

public interface IInteractService {
    InteractDTO createQA(QARequest request);

    InteractDTO getQAById(long id);

    List<InteractDTO> getAllQA();

    List<InteractDTO> getAllQABySenderId(long id);

    InteractDTO replyQA(ReplyQARequest request);
}
