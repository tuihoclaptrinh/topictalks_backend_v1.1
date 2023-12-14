package com.anonymity.topictalks.services.impls;

import com.anonymity.topictalks.daos.post.ICommentRepository;
import com.anonymity.topictalks.daos.post.IPostRepository;
import com.anonymity.topictalks.daos.user.IUserRepository;
import com.anonymity.topictalks.models.dtos.CommentDTO;
import com.anonymity.topictalks.models.payloads.requests.CommentRequest;
import com.anonymity.topictalks.models.payloads.requests.CommentUpdateRequest;
import com.anonymity.topictalks.models.persists.post.CommentPO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {
    private final ICommentRepository commentRepository;
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;

    @Override
    public List<CommentDTO> getAllComments() {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        List<CommentPO> list = commentRepository.findAll();
        for (CommentPO commentPO : list) {
            commentDTOList.add(convertCommentPOToCommentDTO(commentPO));
        }
        return commentDTOList;
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        PostPO postPO = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("This post doesn't exist."));
        List<CommentPO> list = commentRepository.findAllByPostId(postPO);
        for (CommentPO commentPO : list) {
            commentDTOList.add(convertCommentPOToCommentDTO(commentPO));
        }
        return commentDTOList;
    }

    @Override
    public CommentDTO getLastCommentsByPostId(long postId) {
        return convertCommentPOToCommentDTO(commentRepository.getLastCommentByPostId(postId));
    }

    @Override
    public CommentPO getCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public CommentDTO create(CommentRequest request) {
        CommentPO commentPO = new CommentPO();
        UserPO userPO = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist."));
        PostPO postPO = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("This post doesn't exist."));
        commentPO.setUserId(userPO);
        commentPO.setPostId(postPO);
        commentPO.setContent(request.getContent());
        commentPO.setCreatedAt(LocalDateTime.now());
        commentPO.setUpdatedAt(LocalDateTime.now());

        return convertCommentPOToCommentDTO(commentRepository.save(commentPO));
    }

    @Override
    public CommentDTO update(long commentId, CommentUpdateRequest request) {
        UserPO userPO = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist"));
        boolean isExisted = commentRepository.existsByIdAndUserId(commentId, userPO);
        if (!isExisted) return null;
        CommentPO commentPO = getCommentById(commentId);
        commentPO.setContent(request.getContent());
        commentPO.setUpdatedAt(LocalDateTime.now());

        return convertCommentPOToCommentDTO(commentRepository.save(commentPO));
    }

    @Override
    public boolean remove(long userId, long commentId) {
        UserPO userPO = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("This user doesn't exist"));
        boolean isExisted = commentRepository.existsByIdAndUserId(commentId, userPO);
        if (!isExisted) return false;
        else {
            commentRepository.deleteById(commentId);
            return true;
        }
    }

    @Override
    public void save(CommentPO commentPO) {

    }

    private CommentDTO convertCommentPOToCommentDTO(CommentPO commentPO) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentPO.getId());
        commentDTO.setUserId(commentPO.getUserId().getId());
        commentDTO.setUsername(commentPO.getUserId().getNickName());
        commentDTO.setUserImage(commentPO.getUserId().getImageUrl());
        commentDTO.setActive(commentPO.getUserId().isActive());
        commentDTO.setPostId(commentPO.getPostId().getId());
        commentDTO.setContent(commentPO.getContent());
        commentDTO.setCreateAt(commentPO.getCreatedAt());
        commentDTO.setUpdateAt(commentPO.getUpdatedAt());

        return commentDTO;
    }
}
