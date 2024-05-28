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

        if (headers.containsKey("Authorization")) {
            User user = jwtService.getUserFromHeaders(headers);
            return list.stream().map(post -> {
                PostResponse postResponse = new PostResponse(post);
                return setPostLikedOrDislikedState(user, post, postResponse);
            }).collect(Collectors.toList());
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

    public PostResponse getOnePost(HttpHeaders headers, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (headers.containsKey("Authorization")) {
            User user = jwtService.getUserFromHeaders(headers);
            PostResponse postResponse = new PostResponse(post);
            return setPostLikedOrDislikedState(user, post, postResponse);
        }
        return new PostResponse(post);
    }

    public PostResponse updateOnePost(HttpHeaders headers, Long postId, PostUpdateRequest postUpdateRequest) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!isUserTheAuthorOfPost(user, post)) {
            throw new ForbiddenException("You are not authorized to update this post");
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
        PostResponse postResponse = new PostResponse(post);
        postResponse.setLiked(true);
        if (!post.getLikedUsers().contains(user) && !post.getDislikedUsers().contains(user)) {
            likePostWithUser(post, user);
            postResponse.setLikes(postResponse.getLikes() + 1);
        } else if (post.getLikedUsers().contains(user)) {
            unLikePostWithUser(post, user);
            postResponse.setLikes(postResponse.getLikes() - 1);
            postResponse.setLiked(false);
        } else {
            unDislikePostWithUser(post, user);
            likePostWithUser(post, user);
            postResponse.setLikes(postResponse.getLikes() + 1);
            postResponse.setDislikes(postResponse.getDislikes() - 1);
        }
        postRepository.save(post);
        return postResponse;
    }

    public PostResponse dislikePost(HttpHeaders headers, Long postId) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        PostResponse postResponse = new PostResponse(post);
        postResponse.setDisliked(true);
        if (!post.getDislikedUsers().contains(user) && !post.getLikedUsers().contains(user)) {
            dislikePostWithUser(post, user);
            postResponse.setDislikes(postResponse.getDislikes() + 1);
        } else if (post.getDislikedUsers().contains(user)) {
            unDislikePostWithUser(post, user);
            postResponse.setDislikes(postResponse.getDislikes() - 1);
            postResponse.setDisliked(false);
        } else {
            unLikePostWithUser(post, user);
            dislikePostWithUser(post, user);
            postResponse.setDislikes(postResponse.getDislikes() + 1);
            postResponse.setLikes(postResponse.getLikes() - 1);
        }
        postRepository.save(post);
        return postResponse;
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

    private PostResponse setPostLikedOrDislikedState(User user, Post post, PostResponse postResponse){
        if (post.getLikedUsers().contains(user)) {
            postResponse.setLiked(true);
        } else if (post.getDislikedUsers().contains(user)) {
            postResponse.setDisliked(true);
        }
        return postResponse;
    }
}
