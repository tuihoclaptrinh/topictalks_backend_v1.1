package com.anonymity.topictalks.services;

import com.anonymity.topictalks.models.dtos.PostDTO;
import com.anonymity.topictalks.models.payloads.requests.PostRequest;
import com.anonymity.topictalks.models.payloads.requests.RejectPostRequest;
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

    Page<PostDTO> getAllPostByAuthorIdAndRole(Long authorId, int page, int size);

    Page<PostDTO> getAllPostByAuthorId(Long authorId, boolean isApproved, int page, int size);

    Page<PostDTO> getAllPostsByIsApproved(boolean isApproved, int page, int size);

    List<PostDTO> getTop4PostsByIsApproved(boolean isApproved);

    Page<PostDTO> getAllPostsByParentTopicId(long id, int page, int size);

    List<PostDTO> getAllPostsByUserId(long userID, long userInSessionId);

    PostPO aprrovePost(Long id);

    PostPO rejectPost(RejectPostRequest request);

    Object getPostByPostId(Long postId);

    void save(PostPO postPO);
}
