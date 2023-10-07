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
    public List<PostDTO> getAllPosts(long userId) {
        UserPO userPO = userRepository.findById(userId).orElse(null);
        String roleUser = userPO.getRole().name();
        List<PostDTO> dtoList = new ArrayList<>();
        List<PostPO> postList;
        if (roleUser.equalsIgnoreCase("USER")) {
            postList = postRepository.findAllByIsApproved(true);
        } else {
            postList = postRepository.findAll();
        }
        if (postList.isEmpty()) return null;
        for (PostPO list : postList) {
            PostDTO postDto = convertToPostDto(list);
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
            post.setIsApproved(postExist.get().getIsApproved());
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
            PostDTO postDto = convertToPostDto(list);
            postDtoList.add(postDto);
        }
        return postDtoList;

    }

    @Override
    public List<PostDTO> getAllPostByAuthorId(Long authorId) {
        List<PostPO> postList = postList = postRepository.findByAuthorId(authorId);
        if (postList.isEmpty()) return null;
        List<PostDTO> postDtoList = new ArrayList<>();
        for (PostPO list : postList) {
            PostDTO postDto = convertToPostDto(list);
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

    @Override
    public List<PostDTO> getAllPostsByIsApproved(boolean isApproved) {
        List<PostPO> postList = postRepository.findAllByIsApproved(isApproved);
        if (postList.isEmpty()) return null;
        List<PostDTO> postDtoList = new ArrayList<>();
        for (PostPO list : postList) {
            PostDTO postDto = convertToPostDto(list);
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

    @Override
    public PostPO aprrovePost(Long id) {
        boolean isExisted = postRepository.existsById(id);
        if (isExisted) {
            PostPO postPO = postRepository.findById(id).orElse(null);
            postPO.setId(id);
            postPO.setIsApproved(true);
            return postRepository.save(postPO);
        }
        return null;
    }

    @Override
    public Object getPostByPostId(Long postId) {
        boolean isExisted = postRepository.existsById(postId);
        if (isExisted) {
            PostPO post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("Not found"));
            PostDTO postDto = convertToPostDto(post);
            return postDto;
        }
        return null;
    }

    @Override
    public void save(PostPO postPO) {
        postRepository.save(postPO);
    }

    private PostDTO convertToPostDto(PostPO postPO) {
        PostDTO postDto = new PostDTO(
                postPO.getId(),
                postPO.getTitle(),
                postPO.getContent(),
                postPO.getImage(),
                postPO.getTopicParentId().getId(),
                postPO.getAuthorId().getId(),
                postPO.getCreatedAt(),
                postPO.getUpdatedAt(),
                postPO.getIsApproved()
        );
        return postDto;
    }

}
