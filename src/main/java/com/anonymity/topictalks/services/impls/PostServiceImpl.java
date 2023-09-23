package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.post.IPostRepository;
import com.anonymity.topictalks.daos.topic.ITopicParentRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IPostService;
import com.anonymity.topictalks.utils.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {
    @Autowired
    private IPostRepository postRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ITopicParentRepository topicParentRepository;

    @Override
    // Thêm xử lí get post theo role nữa: admin include APPROVED và NOT APPROVED YET
    //                                    user just include APPROVED
    public List<PostDTO> getAllPosts() {
        List<PostDTO> dtoList = new ArrayList<>();
        List<PostPO> postList = postRepository.findAll();
        if (postList.isEmpty()) return null;
        for (PostPO list : postList) {
            PostDTO postDto = new PostDTO(
                    list.getId(),
                    list.getTitle(),
                    list.getContent(),
                    list.getImage(),
                    list.getAuthorId().getId(),
                    list.getAuthorId().getId(),
                    list.getCreatedAt(),
                    list.getUpdatedAt(),
                    list.getIsApproved()
            );
            dtoList.add(postDto);
        }
        return dtoList;
    }

    @Override
    public PostPO createPost(PostRequest request) {
        PostPO post = new PostPO();
        UserPO user = userRepository.findById(request.getAuthor_id())
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist."));
        TopicParentPO topicParent = topicParentRepository.findById(request.getTparent_id())
                .orElseThrow(() -> new IllegalArgumentException("Topic parent doesn't exist."));
        post.setAuthorId(user);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImage(request.getImage() != null ? request.getImage() : "");
        post.setTopicParentId(topicParent);
        post.setIsApproved(false);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Override
    public PostPO updatePost(Long id, PostRequest request) {
        Optional<PostPO> postExist = postRepository.findById(id);
        if (!postExist.isEmpty()) {
            PostPO post = new PostPO();
            TopicParentPO topicParent = topicParentRepository.findById(request.getTparent_id())
                    .orElseThrow(() -> new IllegalArgumentException("Topic parent doesn't exist."));
            post.setId(id);
            post.setAuthorId(postExist.get().getAuthorId());
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            post.setImage(request.getImage() != null ? request.getImage() : "");
            post.setTopicParentId(topicParent);
            post.setIsApproved(request.isApproved());
            post.setCreatedAt(postExist.get().getCreatedAt());
            post.setUpdatedAt(LocalDateTime.now());
            return postRepository.save(post);
        }
        return null;
    }

    @Override
    public boolean removePostById(long id) {
        boolean isExist = postRepository.existsById(id);
        if (isExist) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<PostDTO> getAllPostByAuthorIdAndRole(Long authorId) {
        List<PostPO> postList = new ArrayList<>();
        if (userRepository.findById(authorId).get().getRole().toString().equals(ERole.USER.toString())) {
            postList = postRepository.findByAuthorIdAndIsApproved(authorId, true);
        } else {
            postList = postRepository.findByAuthorId(authorId);
        }

        if (postList.isEmpty()) return null;
        List<PostDTO> postDtoList = new ArrayList<>();
        for (PostPO list : postList) {
            PostDTO postDto = new PostDTO(
                    list.getId(),
                    list.getTitle(),
                    list.getContent(),
                    list.getImage(),
                    list.getAuthorId().getId(),
                    list.getAuthorId().getId(),
                    list.getCreatedAt(),
                    list.getUpdatedAt(),
                    list.getIsApproved()
            );
            postDtoList.add(postDto);
        }
        return postDtoList;

    }

    @Override
    public Object getPostByPostId(Long postId) {
        Optional<PostPO> post = postRepository.findById(postId);
        PostDTO postDto = new PostDTO(
                post.get().getId(),
                post.get().getTitle(),
                post.get().getContent(),
                post.get().getImage(),
                post.get().getTopicParentId().getId(),
                post.get().getAuthorId().getId(),
                post.get().getCreatedAt(),
                post.get().getUpdatedAt(),
                post.get().getIsApproved());
        return post.isEmpty() ? null : postDto;
    }

    @Override
    public void save(PostPO postEntity) {
        postRepository.save(postEntity);
    }

}
