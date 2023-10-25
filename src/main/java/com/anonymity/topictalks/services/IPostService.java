package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.persists.post.PostPO;

import java.util.List;

public interface IPostService {
    PostPO createPost(PostRequest request);

    PostDTO updatePost(Long id, PostRequest request);

    PostDTO updateStatusPost(Long id, Long statusId);

    boolean removePostById(long id);

    List<PostDTO> getAllPosts(long userId);

    List<PostDTO> getAllPostByAuthorIdAndRole(Long authorId);

    List<PostDTO> getAllPostByAuthorId(Long authorId);

    List<PostDTO> getAllPostsByIsApproved(boolean isApproved);

    List<PostDTO> getAllPostsByParentTopicId(long id);

    List<PostDTO> getAllPostsByUserId(long userID, long userInSessionId);

    PostPO aprrovePost(Long id);

    Object getPostByPostId(Long postId);

    void save(PostPO postPO);
}
