package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.post.ILikeRepository;
import com.anonymity.topictalks.daos.post.IPostRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.LikeDTO;
import com.anonymity.topictalks.models.payloads.requests.LikeRequest;
import com.anonymity.topictalks.models.payloads.responses.LikeResponse;
import com.anonymity.topictalks.models.persists.post.LikePO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.ILikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements ILikeService {
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final ILikeRepository likeRepository;

    @Override
    public LikePO like(LikeRequest request) {
        UserPO userPO = userRepository.findById(request.getUserId()).orElse(null);
        PostPO postPO = postRepository.findById(request.getPostId()).orElse(null);
        boolean isLiked = likeRepository.existsByUserIdAndPostId(request.getUserId(), request.getPostId());
        if (postPO != null && userPO != null && !isLiked) {
            LikePO likePO = new LikePO();
            likePO.setUserId(request.getUserId());
            likePO.setPostId(request.getPostId());
            likePO.setUserInfo(userPO);
            likePO.setPostInfo(postPO);
            return likeRepository.save(likePO);
        }
        return null;
    }

    @Override
    public boolean unlike(long userId, long postId) {
        boolean isLiked = likeRepository.existsByUserIdAndPostId(userId, postId);
        if (isLiked) {
            likeRepository.deleteByUserIdAndPostId(userId, postId);
            return true;
        }
        return false;
    }

    @Override
    public LikeResponse getAllUserLikeByPostId(long postId) {
        boolean isPostExisted = postRepository.existsById(postId);
        if (isPostExisted) {
            LikeResponse response = new LikeResponse();
            List<LikePO> list = likeRepository.getAllByPostId(postId);
            response.setTotalLike(list.size());
            List<LikeDTO> inforLike = new ArrayList<>();
            for (LikePO infor : list) {
                LikeDTO likeDTO = new LikeDTO(infor.getUserId(),infor.getUserInfo().getUsername());
                inforLike.add(likeDTO);
            }
            response.setUserLike(inforLike);
            return response;
        }
        return null;
    }
}
