package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.rating.IRatingRepository;
import com.anonymity.topictalks.daos.topic.ITopicChildrenRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.payloads.requests.RatingIdRequest;
import com.anonymity.topictalks.models.payloads.requests.RatingRequest;
import com.anonymity.topictalks.models.payloads.responses.HotTopicResponse;
import com.anonymity.topictalks.models.payloads.responses.RatingResponse;
import com.anonymity.topictalks.models.persists.rating.QRatingPO;
import com.anonymity.topictalks.models.persists.rating.RatingKey;
import com.anonymity.topictalks.models.persists.rating.RatingPO;
import com.anonymity.topictalks.services.IRatingService;
import com.anonymity.topictalks.services.IUserService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.services.impls
 * - Created At: 07-12-2023 21:02:06
 * @since 1.0 - version of class
 */


@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService implements IRatingService {

    private final IRatingRepository ratingRepository;
    private final IUserRepository userRepository;
    private final ITopicChildrenRepository topicChildrenRepository;
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * @param ratingRequest
     * @return
     */
    @Override
    public RatingResponse ratingTopic(RatingRequest ratingRequest) {
        RatingPO savedRating = ratingRepository.save(RatingPO.builder()
                        .id(new RatingKey(ratingRequest.getUserId(), ratingRequest.getTpcId()))
                        .userInfo(userRepository.findById(ratingRequest.getUserId()).orElseThrow())
                        .topicChildrenInfo(topicChildrenRepository.findById(ratingRequest.getTpcId()).orElseThrow())
                        .rating(ratingRequest.getRating())
                .build());
        return RatingResponse.builder()
                .userId(savedRating.getUserInfo().getId())
                .tpcId(savedRating.getTopicChildrenInfo().getId())
                .rating(savedRating.getRating())
                .build();
    }

    /**
     * @param ratingManyRequest
     * @return
     */
    @Override
    public Integer ratingManyTopics(List<RatingRequest> ratingManyRequest) {
        int result = 0;
        try {
            List<RatingPO> entities = ratingManyRequest.stream()
                    .map(this::convertToRatingPO)
                    .collect(Collectors.toList());

            result = ratingRepository.saveAll(entities).size();

            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * @return
     */
    @Override
    public List<RatingResponse> getAllRatings() {
        return convertToRatingsPayload(ratingRepository.findAll());
    }

    /**
     * @param tpcId
     * @return
     */
    @Override
    public List<RatingResponse> getAllRatingsByTopicChildren(Long tpcId) {
        return convertToRatingsPayload(ratingRepository.findByTopicChildrenInfo(topicChildrenRepository.findById(tpcId).orElseThrow()));
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<RatingResponse> getAllRatingsByUser(Long userId) {
        return convertToRatingsPayload(ratingRepository.findByUserInfo(userRepository.findById(userId).orElseThrow()));
    }

    /**
     * @param userId
     * @param tpcId
     * @return
     */
    @Override
    public RatingResponse getRatingsByUserAndTpc(Long userId, Long tpcId) {
        return convertToRatingPayload(ratingRepository.getRatingUpdate(Integer.parseInt(userId.toString()), Integer.parseInt(tpcId.toString())));
    }

    /**
     * @param ratingRequest
     * @return
     */
    @Override
    public RatingResponse updateRating(RatingRequest ratingRequest) {
        RatingPO newRating = ratingRepository.findById(new RatingKey(ratingRequest.getUserId(), ratingRequest.getTpcId())).orElseThrow();
        newRating.setRating(ratingRequest.getRating());
        if(ratingRequest.getRating() == 0) {
            ratingRepository.delete(newRating);
            return convertToRatingPayload(RatingPO.builder().build());
        }
        return convertToRatingPayload(ratingRepository.save(newRating));
    }

    /**
     * @return
     */
    @Override
    public List<HotTopicResponse> getAllHotTopics() {
        List<Object[]> rawResults = ratingRepository.findTopRatedTopicsRaw();
        List<HotTopicResponse> hotTopicResponses = new ArrayList<>();

        for (Object[] result : rawResults) {
            Long topicChildrenId = (Long) result[0];
            Long tpcCount = (Long) result[1];
            Integer maxRating = (Integer) result[2];
            BigDecimal avgRating = (BigDecimal) result[3];
            Timestamp createdAt = (Timestamp) result[4];
            Timestamp updatedAt = (Timestamp) result[5];
            String image = (String) result[6];
            String topicChildrenName = (String) result[7];
            String shortDescription = (String) result[8];

            HotTopicResponse hotTopicResponse = HotTopicResponse.builder()
                    .topicChildrenId(topicChildrenId)
                    .tpcCount(tpcCount)
                    .maxRating(maxRating)
                    .avgRating(avgRating)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .image(image)
                    .topicChildrenName(topicChildrenName)
                    .shortDescription(shortDescription)
                    .build();
            hotTopicResponses.add(hotTopicResponse);
        }

        return hotTopicResponses;
    }

    /**
     * @return
     */
    @Override
    public BigDecimal avgRatingById(Long tpcId) {
        Object[] result = ratingRepository.findAvgRatings(tpcId);

        if (result != null && result.length > 1 && result[1] instanceof BigDecimal) {
            return (BigDecimal) result[1];
        } else {
            return BigDecimal.ZERO;
        }
    }

    private List<RatingResponse> convertToRatingsPayload(List<RatingPO> ratings) {
        List<RatingResponse> ratingsResponse = new ArrayList<>();

        for(RatingPO rating: ratings) {
            RatingResponse ratingResponse = convertToRatingPayload(rating);
            ratingsResponse.add(ratingResponse);
        }

        return ratingsResponse;
    }

    private RatingResponse convertToRatingPayload(RatingPO ratingPO) {
        return RatingResponse.builder()
                .userId(ratingPO.getUserInfo().getId())
                .tpcId(ratingPO.getTopicChildrenInfo().getId())
                .rating(ratingPO.getRating())
                .build();
    }

    private RatingPO convertToRatingPO(RatingRequest request) {
        return RatingPO.builder()
                .id(new RatingKey(request.getUserId(), request.getTpcId()))
                .userInfo(userRepository.findById(request.getUserId()).orElseThrow(null))
                .topicChildrenInfo(topicChildrenRepository.findById(request.getTpcId()).orElseThrow(null))
                .rating(request.getRating())
                .build();
    }

}
