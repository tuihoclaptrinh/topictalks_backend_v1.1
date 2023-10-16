package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.post.ILikeRepository;
import com.anonymity.topictalks.daos.post.IPostRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.LikeDTO;
import com.anonymity.topictalks.models.dtos.LikeListDTO;
import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.LikeRequest;
import com.anonymity.topictalks.models.payloads.responses.LikeResponse;
import com.anonymity.topictalks.models.persists.post.LikePO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.ILikeService;
import com.anonymity.topictalks.services.IPostService;
import com.anonymity.topictalks.services.IUserService;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            LikePO like = new LikePO();
            like.setUserId(request.getUserId());
            like.setPostId(request.getPostId());
            like.setUserInfo(userPO);
            like.setPostInfo(postPO);
            return likeRepository.save(like);
        }
        return null;
    }

    @Override
    public void unlike(long userId, long postId) {
        Optional<PostPO> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            PostPO post = postOptional.get();
            List<LikePO> likes = post.getLikes();

            likes.removeIf(like -> like.getUserInfo().getId().equals(userId));

            postRepository.save(post);
        }
    }

    @Override
    public LikeResponse getAllUserLikeByPostId(long postId) {
        boolean isPostExisted = postRepository.existsById(postId);
        if (isPostExisted) {
            LikeResponse response = new LikeResponse();
            List<LikePO> list = likeRepository.getAllByPostId(postId);
            response.setTotalLike(list.size());
            List<LikeListDTO> inforLike = new ArrayList<>();
            for (LikePO infor : list) {
                LikeListDTO likeListDTO = new LikeListDTO(infor.getUserId(), infor.getUserInfo().getUsername());
                inforLike.add(likeListDTO);
            }
            response.setUserLike(inforLike);
            return response;
        }
        return null;
    }

}
