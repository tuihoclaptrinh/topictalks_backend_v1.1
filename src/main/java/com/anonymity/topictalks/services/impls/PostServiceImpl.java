package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.post.ICommentRepository;
import com.anonymity.topictalks.daos.post.ILikeRepository;
import com.anonymity.topictalks.daos.post.IPostRepository;
import com.anonymity.topictalks.daos.post.IStatusRepository;
import com.anonymity.topictalks.daos.topic.ITopicParentRepository;
import com.anonymity.topictalks.daos.user.IFriendListRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.exceptions.GlobalException;
import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.persists.post.CommentPO;
import com.anonymity.topictalks.models.persists.post.LikePO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.post.StatusPO;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.models.persists.user.FriendListPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.ICommentService;
import com.anonymity.topictalks.services.ILikeService;
import com.anonymity.topictalks.services.IPostService;
import com.anonymity.topictalks.utils.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private IFriendListRepository friendListRepository;

    @Autowired
    private IStatusRepository statusRepository;

    private final ICommentRepository commentRepository;

    private final ILikeService likeService;

    private final ICommentService commentService;

    private final ILikeRepository likeRepository;

    @Override
    public Streamable<PostDTO> getAllPosts(long userId, int page, int size) {
        UserPO userPO = userRepository.findById(userId).orElse(null);
        String roleUser = userPO.getRole().name();

        Streamable<PostPO> postList;
        if (roleUser.equalsIgnoreCase("USER")) {
            PageRequest pageable = PageRequest.of(page, size);
            postList = postRepository.findAllByIsApprovedOrderByCreatedAtDesc(true, pageable);
        } else {
            PageRequest pageable = PageRequest.of(page, size);
            postList = postRepository.findAll(pageable);
        }
        if (postList.isEmpty()) return null;
        return postList.map(this::convertToPostDto);
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
        /**
         * If status_id =1 ---> statusName: Public
         * If status_id =2 ---> statusName: Friend
         * If status_id =3 ---> statusName: Private
         */

        if (request.getStatus_id() == null) {
            StatusPO statusPO = statusRepository.findById(Long.valueOf(3)).orElse(null);
            System.out.println("=====================> check status: " + statusPO.toString());
            post.setStatus(statusPO);
        } else {
            StatusPO statusPO = statusRepository.findById(Long.valueOf(request.getStatus_id())).orElse(null);
            post.setStatus(statusPO);
        }
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Override
    public PostDTO updatePost(Long id, PostRequest request) {
        Optional<PostPO> postExist = postRepository.findById(id);
        if (postExist.isPresent()) {
            PostPO post = postExist.get();
            TopicParentPO topicParent = topicParentRepository.findById(request.getTparent_id())
                    .orElseThrow(() -> new IllegalArgumentException("Topic parent doesn't exist."));
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            String imgUrl = post.getImage();
            post.setImage(request.getImage() != null ? request.getImage() : imgUrl);
            post.setTopicParentId(topicParent);
            post.setUpdatedAt(LocalDateTime.now());
            StatusPO statusPO = statusRepository.findById(Long.valueOf(request.getStatus_id())).orElse(null);
            post.setStatus(statusPO);

            PostDTO postDto = convertToPostDto(post);
            return postDto;
        }
        return null;
    }

    @Override
    public PostDTO updateStatusPost(Long id, Long statusId) {
        PostPO postPO = postRepository.findById(id).orElse(null);
        StatusPO statusPO = statusRepository.findById(statusId).orElse(null);
        if (postPO != null) {
            if (statusPO != null) {
                postPO.setStatus(statusPO);
            } else throw new GlobalException(403, "This status hasn't exist in system.");
        } else throw new GlobalException(403, "This post hasn't exist in system.");
        return convertToPostDto(postRepository.save(postPO));
    }

    @Override
    public boolean removePostById(long id) {
        PostPO postExisted = postRepository.findById(id).orElse(null);
        if (postExisted != null) {
            List<CommentPO> commentList = commentRepository.findAllByPostId(postExisted);
            if (!commentList.isEmpty()) {
                for (CommentPO commment : commentList) {
                    commentRepository.deleteById(commment.getId());
                }
            }
            List<LikePO> likeList = likeRepository.getAllByPostId(id);
            if (!likeList.isEmpty()) {
                likeRepository.removeByPostId(id);
            }
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<PostDTO> getAllPostByAuthorIdAndRole(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<PostPO> postList;

        if (userRepository.findById(authorId).get().getRole().toString().equals(ERole.USER.toString())) {
            postList = postRepository.findByAuthorIdAndIsApproved(authorId, true, pageable).getContent();
        } else {
            postList = postRepository.findByAuthorId(authorId, pageable).getContent();
        }

        if (postList.isEmpty()) return null;

        List<PostDTO> postDtoList = new ArrayList<>();
        for (PostPO list : postList) {
            PostDTO postDto = convertToPostDto(list);
            postDtoList.add(postDto);
        }

        return new PageImpl<>(postDtoList, pageable, postRepository.countByAuthorId(authorId));
    }

    @Override
    public Page<PostDTO> getAllPostByAuthorId(Long authorId, boolean isApproved, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostPO> postPage = postRepository.findByAuthorIdAndIsApproved(authorId, isApproved, pageable);
        if (postPage.isEmpty()) return null;
        return postPage.map(this::convertToPostDto);
    }

    @Override
    public Page<PostDTO> getAllPostsByIsApproved(boolean isApproved, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Streamable<PostPO> postStream = postRepository.findAllByIsApprovedOrderByCreatedAtDesc(isApproved, pageable);

        List<PostDTO> postList = postStream.map(this::convertToPostDto)
                .filter(postDto -> !isApproved || (postDto.getStatus() != 3 && isApproved))
                .toList();

        return PageableExecutionUtils.getPage(postList, pageable, () -> postRepository.countByIsApproved(isApproved));
    }

    @Override
    public List<PostDTO> getTop4PostsByIsApproved(boolean isApproved) {
        List<PostPO> list = postRepository.findTop4ByIsApproved(isApproved, 1);
        List<PostDTO> listDto = new ArrayList<>();
        for (PostPO po : list) {
            PostDTO postDTO = new PostDTO();
            postDTO = convertToPostDto(po);
            postDTO.setLastComment(commentService.getLastCommentsByPostId(po.getId()));
            listDto.add(postDTO);
        }
        return listDto;
    }

    @Override
    public Page<PostDTO> getAllPostsByParentTopicId(long id, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return postRepository.findByTopicParentId(id, true, pageable)
                .map(this::convertToPostDto);
    }

    @Override
    public List<PostDTO> getAllPostsByUserId(long userId, long userInSessionId) {
        FriendListPO friendListPO = friendListRepository.findByUserIdAndFriendId(userId, userInSessionId);
        if (friendListPO != null) {
            List<PostPO> list = postRepository.findByFriendId(userId);
            List<PostDTO> listDto = new ArrayList<>();
            for (PostPO po : list) {
                listDto.add(convertToPostDto(po));
            }
            return listDto;
        }
        List<PostPO> list = postRepository.findByAuthorIdAndIsApprovedAndStatusId(userId, true);
        List<PostDTO> listDto = new ArrayList<>();
        for (PostPO po : list) {
            listDto.add(convertToPostDto(po));
        }
        return listDto;
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

    public PostDTO convertToPostDto(PostPO postPO) {
        PostDTO postDto = new PostDTO(
                postPO.getId(),
                postPO.getTitle(),
                postPO.getContent(),
                postPO.getImage(),
                postPO.getTopicParentId().getId(),
                postPO.getTopicParentId().getTopicParentName(),
                postPO.getAuthorId().getId(),
                postPO.getStatus().getId(),
                postPO.getStatus().getStatusName(),
                userRepository.findById(postPO.getAuthorId().getId()).get().getUsername(),
                userRepository.findById(postPO.getAuthorId().getId()).get().getImageUrl(),
                userRepository.findById(postPO.getAuthorId().getId()).get().isActive(),
                commentService.getCommentsByPostId(postPO.getId()).size(),
                null,
                likeService.getAllUserLikeByPostId(postPO.getId()),
                postPO.getCreatedAt(),
                postPO.getUpdatedAt(),
                postPO.getIsApproved()
        );
        return postDto;
    }
}
