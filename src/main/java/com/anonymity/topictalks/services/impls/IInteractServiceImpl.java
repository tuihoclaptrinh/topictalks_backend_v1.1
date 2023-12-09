package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.user.IInteractRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.dtos.InteractDTO;
import com.anonymity.topictalks.models.dtos.UserQaDTO;
import com.anonymity.topictalks.models.payloads.requests.QARequest;
import com.anonymity.topictalks.models.payloads.requests.ReplyQARequest;
import com.anonymity.topictalks.models.persists.user.InteractPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IInteractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IInteractServiceImpl implements IInteractService {
    private final IUserRepository userRepository;
    private final IInteractRepository interactRepository;

    @Override
    public InteractDTO createQA(QARequest request) {
        InteractPO interactPO = new InteractPO();
        UserPO userPO = userRepository.findById(request.getSenderId()).orElse(null);
        if (userPO == null) {
            throw new GlobalException(404, "Not found this user");
        } else {
            interactPO.setSubject(request.getSubject());
            interactPO.setContent(request.getContent());
            interactPO.setCreatedAt(LocalDateTime.now());
            interactPO.setSenderId(userPO);
            try {
                return convertToInteractDTO(interactRepository.save(interactPO));
            } catch (GlobalException e) {
                throw new GlobalException(403, "Failure to create QA");
            }
        }
    }

    @Override
    public InteractDTO getQAById(long id) {
        Optional<InteractPO> interactOptional = interactRepository.findById(id);
        if (interactOptional.isPresent()) return convertToInteractDTO(interactOptional.get());
        else throw new GlobalException(404, "Not found this QA");
    }

    @Override
    public List<InteractDTO> getAllQA() {
        List<InteractPO> list = interactRepository.findAll();
        if (list.isEmpty()) {
            return null;
        } else {
            List<InteractDTO> result = new ArrayList<>();
            for (InteractPO qa : list) {
                result.add(convertToInteractDTO(qa));
            }
            return result;
        }
    }

    @Override
    public List<InteractDTO> getAllQABySenderId(long id) {
        UserPO userPO = userRepository.findById(id).orElse(null);
        if (userPO != null) {
            List<InteractPO> interactList = interactRepository.findBySenderId(userPO);
            if (!interactList.isEmpty()) {
                List<InteractDTO> interactDTOList = new ArrayList<>();
                for (InteractPO po : interactList) {
                    interactDTOList.add(convertToInteractDTO(po));
                }
                return interactDTOList;
            } else return null;
        } else throw new GlobalException(404, "Not found this user");
    }

    @Override
    public InteractDTO replyQA(ReplyQARequest request) {
        InteractPO interact = interactRepository.findById(request.getQaId()).orElse(null);
        if (interact != null) {
            interact.setAdminReplyId(request.getAdminReplyId());
            interact.setReplyContent(request.getContent());
            interact.setAnswered(true);
            interact.setUpdatedAt(LocalDateTime.now());
            return convertToInteractDTO(interactRepository.save(interact));
        }
        throw new GlobalException(403, "Not found this QA");
    }

    private InteractDTO convertToInteractDTO(InteractPO interactPO) {
        UserQaDTO userQaDTO = new UserQaDTO();
        UserPO sender = interactPO.getSenderId();
        userQaDTO.setUserId(sender.getId());
        userQaDTO.setUsername(sender.getUsername());
        userQaDTO.setAvatarUrl(sender.getImageUrl());
        userQaDTO.setEmail(sender.getEmail());
        userQaDTO.setPhoneNumber(sender.getPhoneNumber());
        return new InteractDTO(
                interactPO.getId(),
                interactPO.getSubject(),
                interactPO.getContent(),
                userQaDTO,
                interactPO.isAnswered(),
                interactPO.getReplyContent(),
                interactPO.getAdminReplyId(),
                interactPO.getCreatedAt(),
                interactPO.getUpdatedAt()
        );
    }
}
