package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.persists.post.ReasonRejectPO;

import java.util.List;

public interface IReasonRejectService {
    List<ReasonRejectPO> getAll();
}
