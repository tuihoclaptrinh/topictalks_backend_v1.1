package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.persists.post.PostPO;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Streamable;

import java.util.List;

public interface IPostService {
    PostPO createPost(PostRequest request);

    PostDTO updatePost(Long id, PostRequest request);

    PostDTO updateStatusPost(Long id, Long statusId);

    boolean removePostById(long id);

    Streamable<PostDTO> getAllPosts(long userId, int page, int size);

    List<PostDTO> getAllPostByAuthorIdAndRole(Long authorId);

    List<PostDTO> getAllPostByAuthorId(Long authorId);

    Page<PostDTO> getAllPostsByIsApproved(boolean isApproved, int page, int size);

    List<PostDTO> getAllPostsByParentTopicId(long id);

    List<PostDTO> getAllPostsByUserId(long userID, long userInSessionId);

    PostPO aprrovePost(Long id);

    Object getPostByPostId(Long postId);

    void save(PostPO postPO);
}
