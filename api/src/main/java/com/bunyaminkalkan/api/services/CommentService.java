package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.Comment;
import com.bunyaminkalkan.api.entities.Post;
import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.exceptions.BadRequestException;
import com.bunyaminkalkan.api.exceptions.ForbiddenException;
import com.bunyaminkalkan.api.exceptions.NotFoundException;
import com.bunyaminkalkan.api.repos.CommentRepository;
import com.bunyaminkalkan.api.repos.PostRepository;
import com.bunyaminkalkan.api.requests.CommentCreateRequest;
import com.bunyaminkalkan.api.requests.CommentUpdateRequest;
import com.bunyaminkalkan.api.responses.CommentResponse;
import com.bunyaminkalkan.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtService jwtService;


    public List<CommentResponse> getAllComments(HttpHeaders headers, Optional<Long> userId) {
        List<Comment> list;
        if (userId.isPresent()) {
            list = commentRepository.findAllByPostId(userId.get());
        } else {
            list = commentRepository.findAll();
        }

        if (headers.containsKey("Authorization")) {
            User user = jwtService.getUserFromHeaders(headers);
            return list.stream().map(comment -> {
                CommentResponse commentResponse = new CommentResponse(comment);
                return setCommentLikedOrDislikedState(user, comment, commentResponse);
            }).collect(Collectors.toList());
        }
        return list.stream().map(CommentResponse::new).collect(Collectors.toList());
    }

    public CommentResponse createOneComment(HttpHeaders headers, CommentCreateRequest commentCreateRequest) {
        User user = jwtService.getUserFromHeaders(headers);
        Post post = postRepository.findById(commentCreateRequest.getPostId()).orElseThrow(() -> new NotFoundException("Post not found"));
        Comment toSave = new Comment();
        toSave.setUser(user);
        toSave.setPost(post);
        toSave.setText(commentCreateRequest.getText());
        toSave.setCreateDate(new Date());
        commentRepository.save(toSave);
        return new CommentResponse(toSave);
    }

    public CommentResponse getOneComment(HttpHeaders headers, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        CommentResponse commentResponse = new CommentResponse(comment);
        if (headers.containsKey("Authorization")) {
            User user = jwtService.getUserFromHeaders(headers);
            return setCommentLikedOrDislikedState(user, comment, commentResponse);
        }
        return commentResponse;
    }

    public CommentResponse updateOneComment(HttpHeaders headers, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        User user = jwtService.getUserFromHeaders(headers);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (isUserTheAuthorOfComment(user, comment)) {
            throw new ForbiddenException("You are not authorized to update this comment");
        }
        if (commentUpdateRequest.getText() != null) {
            comment.setText(commentUpdateRequest.getText());
        }
        commentRepository.save(comment);
        return new CommentResponse(comment);

    }

    public void deleteOneComment(HttpHeaders headers, Long commentId) {
        User user = jwtService.getUserFromHeaders(headers);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (isUserTheAuthorOfComment(user, comment)) {
            throw new ForbiddenException("You are not authorized to delete this comment");
        }
        try {
            commentRepository.delete(comment);
        } catch (Exception e) {
            throw new BadRequestException("Comment not deleted");
        }
        commentRepository.deleteById(commentId);
    }

    public CommentResponse likeComment(HttpHeaders headers, Long commentId) {
        User user = jwtService.getUserFromHeaders(headers);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        CommentResponse commentResponse = new CommentResponse(comment);
        commentResponse.setLiked(true);
        if (!comment.getLikedUsers().contains(user) && !comment.getDislikedUsers().contains(user)) {
            likeCommentWithUser(comment, user);
            commentResponse.setLikes(commentResponse.getLikes() + 1);
        } else if (comment.getLikedUsers().contains(user)) {
            unLikeCommentWithUser(comment, user);
            commentResponse.setLikes(commentResponse.getLikes() - 1);
            commentResponse.setLiked(false);
        } else {
            unDislikeCommentWithUser(comment, user);
            likeCommentWithUser(comment, user);
            commentResponse.setLikes(commentResponse.getLikes() + 1);
            commentResponse.setDislikes(commentResponse.getDislikes() - 1);
        }
        commentRepository.save(comment);
        return commentResponse;
    }

    public CommentResponse dislikeComment(HttpHeaders headers, Long commentId) {
        User user = jwtService.getUserFromHeaders(headers);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        CommentResponse commentResponse = new CommentResponse(comment);
        commentResponse.setDisliked(true);
        if (!comment.getLikedUsers().contains(user) && !comment.getDislikedUsers().contains(user)) {
            dislikeCommentWithUser(comment, user);
            commentResponse.setDislikes(commentResponse.getDislikes() + 1);
        } else if (comment.getDislikedUsers().contains(user)) {
            unDislikeCommentWithUser(comment, user);
            commentResponse.setDislikes(commentResponse.getDislikes() - 1);
            commentResponse.setDisliked(false);
        } else {
            unLikeCommentWithUser(comment, user);
            dislikeCommentWithUser(comment, user);
            commentResponse.setDislikes(commentResponse.getDislikes() + 1);
            commentResponse.setLikes(commentResponse.getLikes() - 1);
        }
        commentRepository.save(comment);
        return commentResponse;
    }

    private boolean isUserTheAuthorOfComment(User user, Comment comment) {
        Long userId = user.getId();
        Long commentUserId = comment.getUser().getId();
        return !userId.equals(commentUserId);
    }

    private void likeCommentWithUser(Comment comment, User user) {
        Set<User> likedUsers = comment.getLikedUsers();
        likedUsers.add(user);
        comment.setLikedUsers(likedUsers);
    }

    private void unLikeCommentWithUser(Comment comment, User user) {
        Set<User> likedUsers = comment.getLikedUsers();
        likedUsers.remove(user);
        comment.setLikedUsers(likedUsers);
    }

    private void dislikeCommentWithUser(Comment comment, User user) {
        Set<User> dislikedUsers = comment.getDislikedUsers();
        dislikedUsers.add(user);
        comment.setDislikedUsers(dislikedUsers);
    }

    private void unDislikeCommentWithUser(Comment comment, User user) {
        Set<User> dislikedUsers = comment.getDislikedUsers();
        dislikedUsers.remove(user);
        comment.setDislikedUsers(dislikedUsers);
    }

    private CommentResponse setCommentLikedOrDislikedState(User user, Comment comment, CommentResponse commentResponse){
        if (comment.getLikedUsers().contains(user)) {
            commentResponse.setLiked(true);
        } else if (comment.getDislikedUsers().contains(user)) {
            commentResponse.setDisliked(true);
        }
        return commentResponse;
    }
}
