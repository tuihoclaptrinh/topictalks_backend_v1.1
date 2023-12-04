package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.post.IReasonRejectRepository;
import com.anonymity.topictalks.models.persists.post.ReasonRejectPO;
import com.anonymity.topictalks.services.IReasonRejectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReasonRejectServiceImpl implements IReasonRejectService {
    @Autowired
    private IReasonRejectRepository reasonRejectRepository;

    @Override
    public List<ReasonRejectPO> getAll() {
        return reasonRejectRepository.findAll();
    }
}
