package com.bunyaminkalkan.api.controllers;

import com.bunyaminkalkan.api.requests.CommentCreateRequest;
import com.bunyaminkalkan.api.requests.CommentUpdateRequest;
import com.bunyaminkalkan.api.responses.CommentResponse;
import com.bunyaminkalkan.api.services.CommentService;
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
    public List<CommentResponse> getAllComments(@RequestParam Optional<Long> postId){
        return commentService.getAllComments(postId);
    }

    @PostMapping
    public CommentResponse createOnePost(@RequestBody CommentCreateRequest commentCreateRequest){
        return commentService.createOneComment(commentCreateRequest);
    }

    @GetMapping("/{commentId}")
    public CommentResponse getOneComment(@PathVariable Long commentId){
        return commentService.getOneComment(commentId);
    }

    @PutMapping("/{commentId}")
    public CommentResponse updateOneComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest){
        return commentService.updateOneComment(commentId, commentUpdateRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteOneComment(@PathVariable Long commentId){
        commentService.deleteOneComment(commentId);
    }

}
