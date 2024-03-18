package com.bunyaminkalkan.api.repos;

import com.bunyaminkalkan.api.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
