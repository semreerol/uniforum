package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.Comment;
import com.bunyaminkalkan.api.entities.Post;
import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.repos.CommentRepository;
import com.bunyaminkalkan.api.repos.PostRepository;
import com.bunyaminkalkan.api.repos.UserRepository;
import com.bunyaminkalkan.api.requests.CommentCreateRequest;
import com.bunyaminkalkan.api.requests.CommentUpdateRequest;
import com.bunyaminkalkan.api.responses.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<CommentResponse> getAllComments(Optional<Long> postId) {
        List<Comment> list;
        if (postId.isPresent()){
            list = commentRepository.findAllByPostId(postId.get());
        }else {
            list = commentRepository.findAll();
        }
        return list.stream().map(CommentResponse::new).collect(Collectors.toList());
    }

    public CommentResponse createOneComment(CommentCreateRequest commentCreateRequest) {
        User user = userRepository.findById(commentCreateRequest.getUserId()).orElse(null);
        Post post = postRepository.findById(commentCreateRequest.getPostId()).orElse(null);

        if (user == null) return null;
        if (post == null) return null;

        Comment toSave = new Comment();
        toSave.setUser(user);
        toSave.setPost(post);
        toSave.setText(commentCreateRequest.getText());
        toSave.setCreateDate(new Date());
        commentRepository.save(toSave);

        return new CommentResponse(toSave);
    }

    public CommentResponse getOneComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return null;
        return new CommentResponse(comment);
    }

    public CommentResponse updateOneComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return null;
        if (commentUpdateRequest.getLikeCount() != null) {
            comment.setLikeCount(commentUpdateRequest.getLikeCount());
        }
        if (commentUpdateRequest.getDislikeCount() != null) {
            comment.setDislikeCount(commentUpdateRequest.getDislikeCount());
        }
        if (commentUpdateRequest.getText() != null) {
            comment.setText(commentUpdateRequest.getText());
        }
        commentRepository.save(comment);
        return new CommentResponse(comment);
    }

    public void deleteOneComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
