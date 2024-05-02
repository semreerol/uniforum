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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostService postService;


    public List<CommentResponse> getAllComments(Optional<Long> postId) {
        List<Comment> list;
        if (postId.isPresent()) {
            list = commentRepository.findAllByPostId(postId.get());
        } else {
            list = commentRepository.findAll();
        }
        return list.stream().map(CommentResponse::new).collect(Collectors.toList());
    }

    public CommentResponse createOneComment(HttpHeaders headers, CommentCreateRequest commentCreateRequest) {
        User user = postService.getUserFromHeaders(headers);
        Post post = postRepository.findById(commentCreateRequest.getPostId()).orElseThrow(() -> new NotFoundException("Post not found"));
        Comment toSave = new Comment();
        toSave.setUser(user);
        toSave.setPost(post);
        toSave.setText(commentCreateRequest.getText());
        toSave.setCreateDate(new Date());
        commentRepository.save(toSave);
        return new CommentResponse(toSave);
    }

    public CommentResponse getOneComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (comment == null) return null;
        return new CommentResponse(comment);
    }

    public CommentResponse updateOneComment(HttpHeaders headers, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        User user = postService.getUserFromHeaders(headers);
        Long userId = user.getId();
        Comment updatedComment = updateCommentBasedOnUserPermissions(commentId, userId, commentUpdateRequest);
        commentRepository.save(updatedComment);
        return new CommentResponse(updatedComment);
    }

    public void deleteOneComment(HttpHeaders headers, Long commentId) {
        User user = postService.getUserFromHeaders(headers);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!isUserTheAuthorOfComment(user, comment)) {
            throw new ForbiddenException("You are not authorized to delete this comment");
        }
        try {
            commentRepository.delete(comment);
        } catch (Exception e) {
            throw new BadRequestException("Comment not deleted");
        }

        commentRepository.deleteById(commentId);
    }

    private Comment updateCommentBasedOnUserPermissions(Long commentId, Long userId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        Long commentUserId = comment.getUser().getId();
        if (commentUserId.equals(userId)) {
            if (commentUpdateRequest.getLikeCount() != null) {
                comment.setLikeCount(commentUpdateRequest.getLikeCount());
            }
            if (commentUpdateRequest.getDislikeCount() != null) {
                comment.setDislikeCount(commentUpdateRequest.getDislikeCount());
            }
            if (commentUpdateRequest.getText() != null) {
                comment.setText(commentUpdateRequest.getText());
            }
            return comment;
        } else {
            if (commentUpdateRequest.getLikeCount() != null) {
                comment.setLikeCount(commentUpdateRequest.getLikeCount());
            }
            if (commentUpdateRequest.getDislikeCount() != null) {
                comment.setDislikeCount(commentUpdateRequest.getDislikeCount());
            }
            return comment;
        }
    }

    private boolean isUserTheAuthorOfComment(User user, Comment comment) {
        Long userId = user.getId();
        Long commentUserId = comment.getUser().getId();
        return userId.equals(commentUserId);
    }

}
