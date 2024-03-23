package com.bunyaminkalkan.api.responses;

import com.bunyaminkalkan.api.entities.Comment;
import lombok.Data;

@Data
public class CommentResponse {

    Long id;
    Long userId;
    Long postId;
    String text;
    Integer likeCount;
    Integer dislikeCount;

    public CommentResponse(Comment entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.postId = entity.getPost().getId();
        this.text = entity.getText();
        this.likeCount = entity.getLikeCount();
        this.dislikeCount = entity.getDislikeCount();
    }
}
