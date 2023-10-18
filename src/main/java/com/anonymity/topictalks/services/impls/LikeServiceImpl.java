package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.post.ILikeRepository;
import com.anonymity.topictalks.daos.post.IPostRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.GlobalException;
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
        PostPO postOptional = postRepository.findByIdAndIsApproved(request.getPostId(), true);
//        System.out.println("=======> Post infor: "+postOptional!=null? postOptional.toString():null);
        boolean isLiked = likeRepository.existsByUserIdAndPostId(request.getUserId(), request.getPostId());
        if (postOptional != null && userPO != null && !isLiked) {
            LikePO like = new LikePO();
            like.setUserId(request.getUserId());
            like.setPostId(request.getPostId());
            like.setUserInfo(userPO);
            like.setPostInfo(postOptional);
            return likeRepository.save(like);
        }
        System.out.println("========================> null");
        return null;
    }

    @Override
    public void unlike(long userId, long postId) {
        PostPO postOptional = postRepository.findByIdAndIsApproved(postId, true);
        Optional<UserPO> userOptional = userRepository.findById(userId);
        if (postOptional != null && userOptional.isPresent()) {
            LikePO likePO = likeRepository.findByUserIdAndPostId(userId, postId);
            if (likePO != null) {
                try {
                    likeRepository.delete(likePO);
                } catch (GlobalException e) {
                    throw new GlobalException(403, "Failure to un-like");
                }
            } else throw new GlobalException(404, "Not found");
        } else throw new GlobalException(404, "Not found");
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
