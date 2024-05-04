package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.Post;
import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.exceptions.BadRequestException;
import com.bunyaminkalkan.api.exceptions.ForbiddenException;
import com.bunyaminkalkan.api.exceptions.NotFoundException;
import com.bunyaminkalkan.api.exceptions.UnauthorizedException;
import com.bunyaminkalkan.api.repos.PostRepository;
import com.bunyaminkalkan.api.repos.UserRepository;
import com.bunyaminkalkan.api.requests.PostCreateRequest;
import com.bunyaminkalkan.api.requests.PostUpdateRequest;
import com.bunyaminkalkan.api.responses.PostResponse;
import com.bunyaminkalkan.api.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final JwtService jwtService;


    public List<PostResponse> getAllPost(Optional<Long> userId) {
        List<Post> list;
        if(userId.isPresent()){
            list = postRepository.findByUserId(userId.get());
        }else{
            list = postRepository.findAll();
        }
        return list.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public PostResponse createOnePost(HttpHeaders headers, PostCreateRequest postCreateRequest) {
        User user = jwtService.getUserFromHeaders(headers);
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

    public PostResponse updateOnePost(HttpHeaders headers, Long postId, PostUpdateRequest postUpdateRequest) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!isUserTheAuthorOfPost(user, post)){
            throw new ForbiddenException("You are not allowed to update this post");
        }
        if(postUpdateRequest.getLikeCount() != null)
            post.setLikeCount(postUpdateRequest.getLikeCount());
        if(postUpdateRequest.getDislikeCount() != null)
            post.setDislikeCount(postUpdateRequest.getDislikeCount());
        if(postUpdateRequest.getText() != null)
            post.setText(postUpdateRequest.getText());
        postRepository.save(post);
        return new PostResponse(post);
    }

    public void deleteOnePost(HttpHeaders headers, Long postId) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!isUserTheAuthorOfPost(user, post)){
            throw new ForbiddenException("You are not authorized to delete this post");
        }
        try {
            postRepository.delete(post);
        } catch (Exception e) {
            throw new BadRequestException("Post not deleted");
        }

    }

    private boolean isUserTheAuthorOfPost(User user, Post post) {
        Long userId = user.getId();
        Long postUserId = post.getUser().getId();
        return userId.equals(postUserId);
    }
}
