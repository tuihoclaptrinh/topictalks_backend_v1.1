package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.dtos.*;
import com.anonymity.topictalks.models.payloads.requests.ConversationRequest;
import com.anonymity.topictalks.models.payloads.requests.ParticipantRequest;
import com.anonymity.topictalks.models.payloads.responses.ConversationResponse;
import com.anonymity.topictalks.models.payloads.responses.ParticipantRandomResponse;
import com.anonymity.topictalks.models.payloads.responses.ParticipantResponse;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.ParticipantKey;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IConversationService;
import com.anonymity.topictalks.services.IMessageService;
import com.anonymity.topictalks.services.IParticipantService;
import com.anonymity.topictalks.services.IUserService;
import com.anonymity.topictalks.utils.RandomUserUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 22-09-2023 18:59:05
 * @since 1.0 - version of class
 */

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements IParticipantService {

    private final IParticipantRepository participantRepository;
    private final RandomUserUtils randomUserUtil;
    private final IConversationService conversationService;
    private final IUserRepository userRepository;
    private final IConversationRepository conversationRepository;
    private final ITopicChildrenRepository topicChildrenRepository;
    private final IMessageService messageService;
    private final IUserService userService;
    private Logger logger = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    @Override
    @Transactional
    public void createChatSingle(ParticipantRequest participantRequest) {
        Map<Long, Long> result = randomUserUtil.randomUserChatting(participantRequest);
        var conversationRequest = ConversationRequest
                .builder()
                .chatName(null)
                .topicChildrenId(participantRequest.getTopicChildId())
                .build();
        for (Map.Entry<Long, Long> entry : result.entrySet()) {
            var conversationResponse = conversationService.createConversation(conversationRequest, false);
            var key = new ParticipantKey();
            var participant = ParticipantPO.builder()
                    .id(key)
                    .userInfo(userRepository.findById(entry.getKey()).orElse(null))
                    .conversationInfo(conversationRepository.findById(conversationResponse.getConversationId()).orElse(null))
                    .build();
            participantRepository.save(participant);
            var key2 = new ParticipantKey();
            var participant2 = ParticipantPO.builder()
                    .id(key2)
                    .userInfo(userRepository.findById(entry.getValue()).orElse(null))
                    .conversationInfo(conversationRepository.findById(conversationResponse.getConversationId()).orElse(null))
                    .build();
            participantRepository.save(participant2);
        }
    }

    @Override
    public ParticipantRandomResponse createChatRandom(ChatRandomDTO chatRandomDTO) {
        var conversationRequest = ConversationRequest.builder()
                .chatName("RANDOM-CHAT-ANONYMOUS")
                .topicChildrenId(chatRandomDTO.getTpcId())
                .build();
        var conv = conversationService.createConversationRandom(conversationRequest, false);
        ParticipantRandomResponse participantRandomResponse = ParticipantRandomResponse
                .builder().conversationInfor(conv).build();
        PartnerDTO partnerDTO;
        List<PartnerDTO> lists = new ArrayList<>();
        for (String user : chatRandomDTO.getUsers()) {
            String[] params = user.split("-");
            Long uId = Long.parseLong(params[0].trim());
            UserDTO us = userService.getUserById(uId);
            partnerDTO = PartnerDTO
                    .builder()
                    .Id(us.getId())
                    .bannedAt(us.getBannedDate())
                    .image(us.getImageUrl())
                    .isBanned(us.getIsBanned())
                    .username(us.getUsername())
                    .active(us.isActive())
                    .build();
            lists.add(partnerDTO);
            var key = new ParticipantKey();
            var participant = ParticipantPO.builder()
                    .id(key)
                    .userInfo(userRepository.findById(uId).orElse(null))
                    .conversationInfo(conversationRepository.findById(conv.getId()).orElse(null))
                    .isMember(true)
                    .build();
            participantRepository.save(participant);
        }
        participantRandomResponse.setPartnerDTO(lists);
        return participantRandomResponse;
    }

    /**
     * @param conversationPO
     * @return
     */
    @Override
    public String getUserIdsByConversation(ConversationPO conversationPO) {

        List<ParticipantPO> participantS = participantRepository.findAllByConversationInfo(conversationPO);

        List<Long> userIds = participantS.stream()
                .map(ParticipantPO::getUserInfo).map(UserPO::getId)
                .toList();

        String result = userIds.stream()
                .map(Object::toString) // Convert Long to String
                .collect(Collectors.joining(",")); // Join with commas and space
        return result;
    }

    @Override
    public List<PartnerDTO> getAllUserByConversationId(long id) {
        ConversationPO conversationPO = conversationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        List<ParticipantPO> list = participantRepository.findAllByConversationInfo(conversationPO);
        List<PartnerDTO> partnerDTOList = new ArrayList<>();
        for (ParticipantPO po : list) {
            PartnerDTO partnerDTO = new PartnerDTO();
            partnerDTO.setUsername(po.getUserInfo().getUsername());
            partnerDTO.setId(po.getUserInfo().getId());
            partnerDTO.setImage(po.getUserInfo().getImageUrl());
            partnerDTO.setBanned(po.getUserInfo().getIsBanned());
            partnerDTO.setBannedAt(po.getUserInfo().getBannedDate());
            partnerDTO.setActive(po.getUserInfo().isActive());
            partnerDTO.setMember(po.getIsMember());
            partnerDTOList.add(partnerDTO);
        }
        return partnerDTOList;
    }

    @Override
    public List<ParticipantResponse> getAllParticipantByUserId(long id) {
        UserPO userPO = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist."));
        List<ParticipantResponse> responses = new ArrayList<>();
        List<ParticipantPO> list = participantRepository.findAllByUserInfo(userPO);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsMember() == true) {
                ParticipantResponse participant = new ParticipantResponse();
                participant.setConversationInfor(
                        convertToConversationDTO(list.get(i).getConversationInfo(),
                                messageService.getLastMessageByConversationId(list.get(i).getConversationInfo().getId()))
                );
                List<Long> partnerIdList = participantRepository.getPartnerIdByConversationIdAndUserId(
                        list.get(i).getConversationInfo().getId(),
                        list.get(i).getUserInfo().getId());
                List<PartnerDTO> listPartner = new ArrayList<>();
                for (int j = 0; j < partnerIdList.size(); j++) {
                    UserPO partner = userRepository.findById(partnerIdList.get(j))
                            .orElseThrow(() -> new IllegalArgumentException("This user haven't exist."));
                    PartnerDTO partnerDTO = new PartnerDTO();
                    partnerDTO.setId(partner.getId());
                    partnerDTO.setBanned(partner.getIsBanned());
                    partnerDTO.setBannedAt(partner.getIsBanned() == true ? null : partner.getBannedDate());
                    partnerDTO.setImage(partner.getImageUrl());
                    partnerDTO.setUsername(partner.getUsername());
                    partnerDTO.setActive(partner.isActive());
                    UserPO partnerInfor = userRepository.findById(partner.getId()).orElse(null);
                    ParticipantPO participantPO = participantRepository.findByConversationInfoAndAndUserInfo(list.get(i).getConversationInfo(), partnerInfor);
                    partnerDTO.setMember(participantPO.getIsMember());
                    listPartner.add(partnerDTO);

                }
                ParticipantPO participantPO = participantRepository.findByConversationInfoAndAndUserInfo(list.get(i).getConversationInfo(), userPO);
                participant.setIsMember(participantPO.getIsMember().toString());
                participant.setPartnerDTO(listPartner);
                responses.add(participant);
            }
        }
        return responses;
    }

    @Override
    public ParticipantResponse getParticipantByConversationIdAndUserId(long conversationId, long userId) {
        ConversationPO conversationInfo = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("This conversation doesn't exist."));

        UserPO userPO = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist."));

        ParticipantPO participantPO = participantRepository.findByConversationInfoAndAndUserInfo(conversationInfo, userPO);
        if (participantPO == null) {
            throw new IllegalArgumentException("Participant not found for this conversation and user.");
        }

        ParticipantResponse participantResponse = new ParticipantResponse();
        LastMessageDTO lastMessageDTO = messageService.getLastMessageByConversationId(conversationId);
        participantResponse.setConversationInfor(convertToConversationDTO(participantPO.getConversationInfo(), lastMessageDTO));
        List<Long> partnerIdList = participantRepository.getPartnerIdByConversationIdAndUserId(
                participantPO.getConversationInfo().getId(),
                participantPO.getUserInfo().getId());
        List<PartnerDTO> listPartner = new ArrayList<>();
        for (Long partnerId : partnerIdList) {
            UserPO partner = userRepository.findById(partnerId)
                    .orElseThrow(() -> new IllegalArgumentException("This user hasn't existed."));
            PartnerDTO partnerDTO = new PartnerDTO();
            partnerDTO.setId(partner.getId());
            partnerDTO.setBanned(partner.getIsBanned());
            partnerDTO.setBannedAt(partner.getIsBanned() ? null : partner.getBannedDate());
            partnerDTO.setImage(partner.getImageUrl());
            partnerDTO.setUsername(partner.getUsername());
            partnerDTO.setActive(partner.isActive());
            UserPO partnerInfo = userRepository.findById(partner.getId()).orElse(null);
            ParticipantPO partnerParticipantPO = participantRepository.findByConversationInfoAndAndUserInfo(participantPO.getConversationInfo(), partnerInfo);
            partnerDTO.setMember(partnerParticipantPO.getIsMember());
            listPartner.add(partnerDTO);
        }
        participantResponse.setIsMember(participantPO.getIsMember().toString());
        participantResponse.setPartnerDTO(listPartner);

        return participantResponse;
    }

    @Override
    public ParticipantResponse getParticipantByUserIdAndPartnerId(long userId, long partnerId, long topicChildrenId) {
        UserPO user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist."));
        UserPO partner = userRepository.findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist."));

        ParticipantResponse participant = new ParticipantResponse();
        List<PartnerDTO> listPartner = new ArrayList<>();
        List<Long> isConversationMatched = conversationRepository.checkMatchingConversations(userId, partnerId, false);
        if (!isConversationMatched.isEmpty()) {
            ConversationPO conversationPO = conversationRepository.findById(isConversationMatched.get(0))
                    .orElseThrow(() -> new IllegalArgumentException("This conversation doesn't exist"));
            List<ParticipantPO> list = participantRepository.findAllByConversationInfo(conversationPO);
            for (int i = 0; i < list.size(); i++) {
                LastMessageDTO lastMessageDTO = messageService.getLastMessageByConversationId(list.get(i).getConversationInfo().getId());
                participant.setConversationInfor(convertToConversationDTO(list.get(i).getConversationInfo(), lastMessageDTO));
                PartnerDTO partnerDTO = new PartnerDTO();
                partnerDTO.setId(partner.getId());
                partnerDTO.setBanned(partner.getIsBanned());
                partnerDTO.setBannedAt(partner.getIsBanned() == true ? null : partner.getBannedDate());
                partnerDTO.setImage(partner.getImageUrl());
                partnerDTO.setUsername(partner.getUsername());
                partnerDTO.setActive(partner.isActive());
                partnerDTO.setMember(true);
                listPartner.add(partnerDTO);

                participant.setPartnerDTO(listPartner);
                participant.setIsMember(String.valueOf(true));
                return participant;
            }
        }
        ConversationRequest request = new ConversationRequest();
        request.setAdminId(0L);
        request.setChatName(partner.getUsername());
        request.setTopicChildrenId(topicChildrenId);
        ConversationResponse conversationResponse = new ConversationResponse();
        try {
            conversationResponse = conversationService.createConversation(request, false);
        } catch (Exception e) {
            System.out.println(e);
        }
        ParticipantPO participantPO1 = new ParticipantPO();
        ConversationPO conversationPO = conversationRepository.findById(conversationResponse.getConversationId())
                .orElseThrow(() -> new IllegalArgumentException("This conversation doesn't exist"));
        participantPO1.setConversationInfo(conversationPO);
        participantPO1.setUserInfo(user);
        participantPO1.setIsMember(true);
        participantPO1.setCreatedAt(LocalDateTime.now());
        participantPO1.setCreatedAt(LocalDateTime.now());
        participantPO1.setUpdatedAt(LocalDateTime.now());
        participantRepository.save(participantPO1);

        ParticipantPO participantPO2 = new ParticipantPO();
        participantPO2.setConversationInfo(conversationPO);
        participantPO2.setUserInfo(partner);
        participantPO2.setIsMember(true);
        participantPO2.setCreatedAt(LocalDateTime.now());
        participantPO2.setCreatedAt(LocalDateTime.now());
        participantPO2.setUpdatedAt(LocalDateTime.now());
        participantRepository.save(participantPO2);

        LastMessageDTO lastMessageDTO = messageService.getLastMessageByConversationId(conversationPO.getId());
        participant.setConversationInfor(convertToConversationDTO(conversationPO, lastMessageDTO));
        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setId(partnerId);
        partnerDTO.setUsername(partner.getUsername());
        partnerDTO.setBanned(partner.getIsBanned());
        partnerDTO.setBannedAt(partner.getBannedDate());
        partnerDTO.setImage(partner.getImageUrl());
        partnerDTO.setActive(partner.isActive());
        partnerDTO.setMember(true);
        listPartner.add(partnerDTO);
        participant.setPartnerDTO(listPartner);

        return participant;
    }

    @Override
    public ParticipantResponse joinGroupChat(long userId, long conversationId) {
        UserPO userPO = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist"));
        ConversationPO conversationPO = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation doesn't exist"));
        ParticipantPO participantPO = new ParticipantPO();
        participantPO.setUserInfo(userPO);
        participantPO.setConversationInfo(conversationPO);
        participantPO.setCreatedAt(LocalDateTime.now());
        participantPO.setUpdatedAt(LocalDateTime.now());
        participantPO.setIsMember(false);
        ParticipantPO participantPO1 = participantRepository.save(participantPO);
        ParticipantResponse participantResponse = new ParticipantResponse();
        LastMessageDTO lastMessageDTO = messageService.getLastMessageByConversationId(conversationPO.getId());
        participantResponse.setConversationInfor(convertToConversationDTO(conversationPO, lastMessageDTO));
        List<ParticipantPO> list = participantRepository.findAllByConversationInfo(conversationPO);
        List<PartnerDTO> partnerList = new ArrayList<>();
        for (ParticipantPO participant : list) {
            PartnerDTO partner = new PartnerDTO();
            partner.setUsername(participant.getUserInfo().getUsername());
            partner.setId(participant.getUserInfo().getId());
            partner.setImage(participant.getUserInfo().getImageUrl());
            partner.setBanned(participant.getUserInfo().getIsBanned());
            partner.setBannedAt(participant.getUserInfo().getBannedDate());
            partner.setMember(participant.getIsMember());
            partner.setActive(participant.getUserInfo().isActive());
            partnerList.add(partner);
        }
        participantResponse.setPartnerDTO(partnerList);
        return participantResponse;
    }

    @Override
    public ParticipantResponse createGroupChat(long conversationId) {
        ConversationPO conversationPO = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        UserPO userPO = userRepository.findById(conversationPO.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        ParticipantPO participantPO = new ParticipantPO();
        participantPO.setIsMember(true);
        participantPO.setUserInfo(userPO);
        participantPO.setConversationInfo(conversationPO);
        participantPO.setCreatedAt(LocalDateTime.now());
        participantPO.setUpdatedAt(LocalDateTime.now());
        ParticipantPO po = participantRepository.save(participantPO);

        ParticipantResponse participantResponse = new ParticipantResponse();
        LastMessageDTO lastMessageDTO = messageService.getLastMessageByConversationId(conversationPO.getId());
        participantResponse.setConversationInfor(convertToConversationDTO(conversationPO, lastMessageDTO));
        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setUsername(userPO.getUsername());
        partnerDTO.setId(userPO.getId());
        partnerDTO.setMember(po.getIsMember());
        partnerDTO.setImage(userPO.getImageUrl());
        partnerDTO.setBanned(userPO.getIsBanned());
        partnerDTO.setBannedAt(userPO.getBannedDate());
        partnerDTO.setActive(userPO.isActive());
        List<PartnerDTO> list = new ArrayList<>();
        list.add(partnerDTO);
        participantResponse.setPartnerDTO(list);
        participantResponse.setIsMember(po.getIsMember().toString());
        return participantResponse;
    }

    @Override
    public ParticipantResponse approveToGroupChat(long userId, long conversationId) {
        ConversationPO conversationPO = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        UserPO userPO = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        boolean isApproved = participantRepository.existsByConversationInfoAndUserInfoAndIsMember(conversationPO, userPO, true);
        if (!isApproved) {
            ParticipantPO participantPO = participantRepository.findByConversationInfoAndAndUserInfo(conversationPO, userPO);
            participantPO.setIsMember(true);
            ParticipantPO approveParticipant = participantRepository.save(participantPO);
            ParticipantResponse response = new ParticipantResponse();
            List<PartnerDTO> list = getAllUserByConversationId(conversationId);
            response.setConversationInfor(convertToConversationDTO(conversationPO, messageService.getLastMessageByConversationId(conversationId)));
            response.setPartnerDTO(list);
            response.setIsMember(approveParticipant.getIsMember().toString());
            return response;
        }
        return null;
    }

    @Override
    public boolean checkAdminOfGroupChat(long userId, long conversationId) {
        UserPO userInSession = userRepository.findById(userId).orElse(null);
        ConversationPO conversationPO = conversationRepository.findById(conversationId).orElse(null);
        boolean isExisted = participantRepository.existsByConversationInfoAndUserInfoAndIsMember(conversationPO, userInSession, true);
        if (isExisted) {
            if (conversationPO.getAdminId() == userId) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<ParticipantResponse> getAllGroupChatByTopicChildrenId(long id) {
        TopicChildrenPO topicChildrenPO = topicChildrenRepository.findById(id);
        List<ConversationPO> list = conversationRepository.findAllByTopicChildrenAndIsGroupChat(topicChildrenPO, true);
        if (list.isEmpty()) return null;
        List<ParticipantResponse> responseList = new ArrayList<>();
        for (ConversationPO conversationPO : list) {
            ParticipantResponse response = new ParticipantResponse();
            response.setConversationInfor(convertToConversationDTO(conversationPO, messageService.getLastMessageByConversationId(conversationPO.getId())));
            List<ParticipantPO> poList = participantRepository.findAllByConversationInfo(conversationPO);
            List<PartnerDTO> partnerDtos = new ArrayList<>();
            for (ParticipantPO po : poList) {
                PartnerDTO partnerDTO = new PartnerDTO();
                partnerDTO.setUsername(po.getUserInfo().getUsername());
                partnerDTO.setImage(po.getUserInfo().getImageUrl());
                partnerDTO.setId(po.getUserInfo().getId());
                partnerDTO.setMember(po.getIsMember());
                partnerDTO.setBanned(po.getUserInfo().getIsBanned());
                partnerDTO.setActive(po.getUserInfo().isActive());
                partnerDTO.setBannedAt(po.getUserInfo().getBannedDate());
                partnerDtos.add(partnerDTO);
            }
            response.setPartnerDTO(partnerDtos);
            response.setIsMember(null);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public List<ParticipantResponse> getAllConversationByUserIdAndIsGroup(long userId, boolean isGroupChat) {
        List<ParticipantResponse> responseList = new ArrayList<>();
        List<ConversationPO> converList = conversationRepository.findAllByUserIdAndIsGroupChat(userId, isGroupChat);
        if (converList.size() > 0) {
            for (ConversationPO list : converList) {
                ParticipantResponse response = new ParticipantResponse();
                response.setConversationInfor(convertToConversationDTO(list, messageService.getLastMessageByConversationId(list.getId())));
                List<ParticipantPO> poList = participantRepository.findAllByConversationInfo(list);
                List<PartnerDTO> partnerDtos = new ArrayList<>();
                ParticipantPO myParticipant = new ParticipantPO();
                for (ParticipantPO po : poList) {
                    if (userId != po.getUserInfo().getId()) {
                        PartnerDTO partnerDTO = new PartnerDTO();
                        partnerDTO.setUsername(po.getUserInfo().getUsername());
                        partnerDTO.setImage(po.getUserInfo().getImageUrl());
                        partnerDTO.setId(po.getUserInfo().getId());
                        partnerDTO.setMember(po.getIsMember());
                        partnerDTO.setBanned(po.getUserInfo().getIsBanned());
                        partnerDTO.setActive(po.getUserInfo().isActive());
                        partnerDTO.setBannedAt(po.getUserInfo().getBannedDate());
                        partnerDtos.add(partnerDTO);
                    } else {
                        myParticipant = po;
                    }
                }
                response.setPartnerDTO(partnerDtos);
                response.setIsMember(String.valueOf(myParticipant.getIsMember()));
                responseList.add(response);
            }

            return responseList;
        }
        return null;
    }

    @Override
    public List<Long> getListUserIdByConversation(ConversationPO conversationPO) {
        List<Long> result = new ArrayList<>();
        List<ParticipantPO> list = participantRepository.findAllByConversationInfo(conversationPO);
        for (ParticipantPO par : list) {
            if (par.getIsMember() == true) {
                result.add(par.getUserInfo().getId());
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void removeToGroupChat(long userId, long conversationId) {
        UserPO user = userRepository.findById(userId).orElseThrow(() -> new GlobalException(404, "User not found"));
        ConversationPO conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new GlobalException(404, "User not found"));
        try {
            participantRepository.deleteByConversationInfoAndUserInfo(conversation, user);
        } catch (GlobalException e) {
            throw new GlobalException(404, "This participant not found");
        }
    }

//    @Override
//    public boolean checkExistConnectedOneToOneBefore(long topicId, long userId, long partnerId) {
//        List<ParticipantPO> isExisted = participantRepository.getParticipantsByTopicChildenIdAndUserIds(topicId, userId, partnerId);
//        return isExisted.size() == 2 ? true : false;
//    }

    private ConversationDTO convertToConversationDTO(ConversationPO conversation, LastMessageDTO lastMessageDTO) {
        return new ConversationDTO(conversation.getId(),
                conversation.getChatName(),
                conversation.getIsGroupChat(),
                lastMessageDTO,
                conversation.getTopicChildren(),
                conversation.getAdminId(),
                conversation.getAvatarGroupImg());
    }

}
