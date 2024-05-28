package com.bunyaminkalkan.api.controllers;

import com.bunyaminkalkan.api.requests.CommentCreateRequest;
import com.bunyaminkalkan.api.requests.CommentUpdateRequest;
import com.bunyaminkalkan.api.responses.CommentResponse;
import com.bunyaminkalkan.api.responses.PostResponse;
import com.bunyaminkalkan.api.services.CommentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponse> getAllComments(@RequestHeader HttpHeaders headers, @RequestParam Optional<Long> userId){
        return commentService.getAllComments(headers, userId);
    }

    @PostMapping
    public CommentResponse createOnePost(@RequestHeader HttpHeaders headers, @RequestBody CommentCreateRequest commentCreateRequest){
        return commentService.createOneComment(headers, commentCreateRequest);
    }

    @GetMapping("/{commentId}")
    public CommentResponse getOneComment(@RequestHeader HttpHeaders headers, @PathVariable Long commentId){
        return commentService.getOneComment(headers, commentId);
    }

    @PutMapping("/{commentId}")
    public CommentResponse updateOneComment(@RequestHeader HttpHeaders headers, @PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest){
        return commentService.updateOneComment(headers, commentId, commentUpdateRequest);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteOneComment(@RequestHeader HttpHeaders headers, @PathVariable Long commentId){
        commentService.deleteOneComment(headers, commentId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/{commentId}/like")
    public CommentResponse likeComment(@RequestHeader HttpHeaders headers, @PathVariable Long commentId){
        return commentService.likeComment(headers, commentId);
    }

    @PostMapping("/{commentId}/dislike")
    public CommentResponse dislikeComment(@RequestHeader HttpHeaders headers, @PathVariable Long commentId){
        return commentService.dislikeComment(headers, commentId);
    }

}
