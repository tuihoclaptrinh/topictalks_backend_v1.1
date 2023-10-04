package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.CommentDTO;
import com.anonymity.topictalks.models.payloads.requests.CommentRequest;
import com.anonymity.topictalks.models.payloads.requests.CommentUpdateRequest;
import com.anonymity.topictalks.models.persists.post.CommentPO;

import java.util.List;

public interface ICommentService {
    List<CommentDTO> getAllComments();

    List<CommentDTO> getCommentsByPostId(long postId);

    CommentPO getCommentById(long id);

    CommentDTO create(CommentRequest request);

    CommentDTO update(long commentId, CommentUpdateRequest request);

    boolean remove(long userId, long commentId);
    void save(CommentPO commentPO);
}
