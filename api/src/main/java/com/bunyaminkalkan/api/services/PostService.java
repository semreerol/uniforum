package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.Post;
import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.exceptions.BadRequestException;
import com.bunyaminkalkan.api.exceptions.ForbiddenException;
import com.bunyaminkalkan.api.exceptions.NotFoundException;
import com.bunyaminkalkan.api.repos.PostRepository;
import com.bunyaminkalkan.api.requests.PostCreateRequest;
import com.bunyaminkalkan.api.requests.PostUpdateRequest;
import com.bunyaminkalkan.api.responses.PostResponse;
import com.bunyaminkalkan.api.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final JwtService jwtService;

    public List<PostResponse> getAllPost(HttpHeaders headers, Optional<Long> userId) {
        List<Post> list;
        if (userId.isPresent()) {
            list = postRepository.findByUserId(userId.get());
        } else {
            list = postRepository.findAll();
        }

        if (headers.containsKey("authorization")) {
            User user = jwtService.getUserFromHeaders(headers);
            return list.stream().map(post -> setLikedOrDislikedState(user, post)).collect(Collectors.toList());
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

    public PostResponse getOnePostById(HttpHeaders headers, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (headers.containsKey("authorization")) {
            User user = jwtService.getUserFromHeaders(headers);
            return setLikedOrDislikedState(user, post);
        }
        return new PostResponse(post);
    }

    public PostResponse updateOnePost(HttpHeaders headers, Long postId, PostUpdateRequest postUpdateRequest) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!isUserTheAuthorOfPost(user, post)) {
            throw new ForbiddenException("You are not allowed to update this post");
        }
        if (postUpdateRequest.getText() != null)
            post.setText(postUpdateRequest.getText());
        postRepository.save(post);
        return new PostResponse(post);
    }

    public void deleteOnePost(HttpHeaders headers, Long postId) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!isUserTheAuthorOfPost(user, post)) {
            throw new ForbiddenException("You are not authorized to delete this post");
        }
        try {
            postRepository.delete(post);
        } catch (Exception e) {
            throw new BadRequestException("Post not deleted");
        }

    }

    public PostResponse likePost(HttpHeaders headers, Long postId) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!post.getLikedUsers().contains(user) && !post.getDislikedUsers().contains(user)) {
            likePostWithUser(post, user);
        } else if (post.getDislikedUsers().contains(user)) {
            unDislikePostWithUser(post, user);
            likePostWithUser(post, user);
        } else {
            throw new BadRequestException("You are already like this post");
        }
        postRepository.save(post);
        PostResponse postResponse = new PostResponse(post);
        postResponse.setLiked(true);
        return postResponse;
    }

    public PostResponse unLikePost(HttpHeaders headers, Long postId) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (post.getLikedUsers().contains(user)) {
            unLikePostWithUser(post, user);
        } else {
            throw new BadRequestException("You don't like yet this post");
        }
        postRepository.save(post);
        return new PostResponse(post);
    }

    public PostResponse dislikePost(HttpHeaders headers, Long postId) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!post.getDislikedUsers().contains(user) && !post.getLikedUsers().contains(user)) {
            dislikePostWithUser(post, user);
        } else if (post.getLikedUsers().contains(user)) {
            unLikePostWithUser(post, user);
            dislikePostWithUser(post, user);
        } else {
            throw new BadRequestException("You are already dislike this post");
        }
        postRepository.save(post);
        PostResponse postResponse = new PostResponse(post);
        postResponse.setDisliked(true);
        return postResponse;
    }

    public PostResponse unDislikePost(HttpHeaders headers, Long postId) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (post.getDislikedUsers().contains(user)) {
            unDislikePostWithUser(post, user);
        } else {
            throw new BadRequestException("You don't dislike yet this post");
        }
        postRepository.save(post);
        return new PostResponse(post);
    }

    private boolean isUserTheAuthorOfPost(User user, Post post) {
        Long userId = user.getId();
        Long postUserId = post.getUser().getId();
        return userId.equals(postUserId);
    }

    private void likePostWithUser(Post post, User user) {
        Set<User> likedUsers = post.getLikedUsers();
        likedUsers.add(user);
        post.setLikedUsers(likedUsers);
    }

    private void unLikePostWithUser(Post post, User user) {
        Set<User> likedUsers = post.getLikedUsers();
        likedUsers.remove(user);
        post.setLikedUsers(likedUsers);
    }

    private void dislikePostWithUser(Post post, User user) {
        Set<User> dislikedUsers = post.getDislikedUsers();
        dislikedUsers.add(user);
        post.setDislikedUsers(dislikedUsers);
    }

    private void unDislikePostWithUser(Post post, User user) {
        Set<User> dislikedUsers = post.getDislikedUsers();
        dislikedUsers.remove(user);
        post.setDislikedUsers(dislikedUsers);
    }

    private PostResponse setLikedOrDislikedState(User user, Post post){
        PostResponse postResponse = new PostResponse(post);
        if (post.getLikedUsers().contains(user)) {
            postResponse.setLiked(true);
        } else if (post.getDislikedUsers().contains(user)) {
            postResponse.setDisliked(true);
        }
        return postResponse;
    }
}
