package com.bunyaminkalkan.api.repos;

import com.bunyaminkalkan.api.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long aLong);
}
