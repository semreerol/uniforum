package com.bunyaminkalkan.api.responses;

import com.bunyaminkalkan.api.entities.Comment;
import lombok.Data;

@Data
public class CommentResponse {

    Long id;
    Long userId;
    Long postId;
    String text;
    Integer likes;
    Integer dislikes;
    Boolean liked = false;
    Boolean disliked = false;

    public CommentResponse(Comment entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.postId = entity.getPost().getId();
        this.text = entity.getText();
        this.likes = entity.getLikedUsers().size();
        this.dislikes = entity.getDislikedUsers().size();
    }
}
