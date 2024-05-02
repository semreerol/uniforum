package com.bunyaminkalkan.api.controllers;

import com.bunyaminkalkan.api.requests.PostCreateRequest;
import com.bunyaminkalkan.api.requests.PostUpdateRequest;
import com.bunyaminkalkan.api.responses.PostResponse;
import com.bunyaminkalkan.api.services.PostService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Transactional
    public List<PostResponse> getAllPosts(@RequestParam Optional<Long> userId){
        return postService.getAllPost(userId);
    }

    @PostMapping
    public PostResponse createOnePost(@RequestHeader HttpHeaders headers, @RequestBody PostCreateRequest postCreateRequest){
        return postService.createOnePost(headers, postCreateRequest);
    }

    @GetMapping("/{postId}")
    public PostResponse getOnePostById(@PathVariable Long postId){
        return postService.getOnePostById(postId);
    }

    @PutMapping("/{postId}")
    public PostResponse updateOnePost(@RequestHeader HttpHeaders headers, @PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest){
        return postService.updateOnePost(headers, postId, postUpdateRequest);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteOnePost(@RequestHeader HttpHeaders headers, @PathVariable Long postId){
        postService.deleteOnePost(headers, postId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
