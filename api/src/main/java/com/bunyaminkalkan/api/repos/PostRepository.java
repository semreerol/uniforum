package com.bunyaminkalkan.api.repos;

import com.bunyaminkalkan.api.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long aLong);
}
