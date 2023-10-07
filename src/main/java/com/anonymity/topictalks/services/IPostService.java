package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.persists.post.PostPO;

import java.util.List;

public interface IPostService {
    PostPO createPost(PostRequest request);

    PostPO updatePost(Long id, PostRequest request);

    boolean removePostById(long id);

    List<PostDTO> getAllPosts(long userId);

    List<PostDTO> getAllPostByAuthorIdAndRole(Long authorId);

    List<PostDTO> getAllPostsByIsApproved(boolean isApproved);

    PostPO aprrovePost(Long id);

    Object getPostByPostId(Long postId);

    void save(PostPO postPO);
}
