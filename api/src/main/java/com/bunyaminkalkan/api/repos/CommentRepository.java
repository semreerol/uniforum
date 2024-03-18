package com.bunyaminkalkan.api.repos;

import com.bunyaminkalkan.api.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
