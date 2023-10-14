package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.message.IConversationRepository;
import com.anonymity.topictalks.daos.message.IParticipantRepository;
import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.listeners.SocketEventListener;
import com.anonymity.topictalks.models.dtos.ChatRandomDTO;
import com.anonymity.topictalks.models.dtos.PartnerDTO;
import com.anonymity.topictalks.models.dtos.UserDTO;
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
import com.anonymity.topictalks.services.IParticipantService;
import com.anonymity.topictalks.services.IUserService;
import com.anonymity.topictalks.utils.RandomUserUtils;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
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
                    .isBanned(us.isBanned())
                    .username(us.getUsername())
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
            ParticipantResponse participant = new ParticipantResponse();
            participant.setConversationInfor(list.get(i).getConversationInfo());
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
        return responses;
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
                participant.setConversationInfor(list.get(i).getConversationInfo());
                PartnerDTO partnerDTO = new PartnerDTO();
                partnerDTO.setId(partner.getId());
                partnerDTO.setBanned(partner.getIsBanned());
                partnerDTO.setBannedAt(partner.getIsBanned() == true ? null : partner.getBannedDate());
                partnerDTO.setImage(partner.getImageUrl());
                partnerDTO.setUsername(partner.getUsername());
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

        participant.setConversationInfor(conversationPO);
        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setId(partnerId);
        partnerDTO.setUsername(partner.getUsername());
        partnerDTO.setBanned(partner.getIsBanned());
        partnerDTO.setBannedAt(partner.getBannedDate());
        partnerDTO.setImage(partner.getImageUrl());
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
        participantResponse.setConversationInfor(conversationPO);
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
        participantResponse.setConversationInfor(conversationPO);
        PartnerDTO partnerDTO = new PartnerDTO();
        partnerDTO.setUsername(userPO.getUsername());
        partnerDTO.setId(userPO.getId());
        partnerDTO.setMember(po.getIsMember());
        partnerDTO.setImage(userPO.getImageUrl());
        partnerDTO.setBanned(userPO.getIsBanned());
        partnerDTO.setBannedAt(userPO.getBannedDate());
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
            response.setConversationInfor(conversationPO);
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
            response.setConversationInfor(conversationPO);
            List<ParticipantPO> poList = participantRepository.findAllByConversationInfo(conversationPO);
            List<PartnerDTO> partnerDtos = new ArrayList<>();
            for (ParticipantPO po : poList) {
                PartnerDTO partnerDTO = new PartnerDTO();
                partnerDTO.setUsername(po.getUserInfo().getUsername());
                partnerDTO.setImage(po.getUserInfo().getImageUrl());
                partnerDTO.setId(po.getUserInfo().getId());
                partnerDTO.setMember(po.getIsMember());
                partnerDTO.setBanned(po.getUserInfo().getIsBanned());
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
    @Transactional
    public boolean removeToGroupChat(long userId, long conversationId) {
        ConversationPO conversationPO = conversationRepository.findById(conversationId)
                .orElse(null);
        UserPO userPO = userRepository.findById(userId)
                .orElse(null);
        boolean isExisted = participantRepository.existsByConversationInfoAndUserInfo(conversationPO, userPO);
        if (conversationPO != null && userPO != null && isExisted) {
            participantRepository.deleteByConversationInfoAndUserInfo(conversationPO, userPO);
            return true;
        }
        return false;
    }


}
