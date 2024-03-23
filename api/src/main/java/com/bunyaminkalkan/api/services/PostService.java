package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.Post;
import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.repos.PostRepository;
import com.bunyaminkalkan.api.repos.UserRepository;
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
    private UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
        User user = userRepository.findById(postCreateRequest.getUserId()).orElse(null);
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
        PostResponse postResponse = new PostResponse(post);
        return postResponse;
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

    public void deleteOnePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
