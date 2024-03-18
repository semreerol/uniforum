package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.repos.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
}
