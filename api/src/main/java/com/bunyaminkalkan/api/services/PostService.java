package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.Post;
import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.repos.PostRepository;
import com.bunyaminkalkan.api.requests.PostCreateRequest;
import com.bunyaminkalkan.api.requests.PostUpdateRequest;
import com.bunyaminkalkan.api.responses.PostResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<PostResponse> getAllPost(Optional<Long> userId) {
        List<Post> list;
        if(userId.isPresent()){
            list = postRepository.findByUserId(userId.get());
        }else{
            list = postRepository.findAll();
        }
        return list.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public PostResponse createOnePost(PostCreateRequest postCreateRequest) {
        User user = userService.getOneUserById(postCreateRequest.getUserId());
        if(user == null)
            return null;
        Post toSave = new Post();
        toSave.setUser(user);
        toSave.setText(postCreateRequest.getText());
        toSave.setCreateDate(new Date());
        postRepository.save(toSave);
        return new PostResponse(toSave);
    }

    public PostResponse getOnePostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        return new PostResponse(post);
    }

    public PostResponse updateOnePost(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            return null;
        if(postUpdateRequest.getLikeCount() != null)
            post.setLikeCount(postUpdateRequest.getLikeCount());
        if(postUpdateRequest.getDislikeCount() != null)
            post.setDislikeCount(postUpdateRequest.getDislikeCount());
        if(postUpdateRequest.getText() != null)
            post.setText(postUpdateRequest.getText());
        postRepository.save(post);
        return new PostResponse(post);
    }
}
