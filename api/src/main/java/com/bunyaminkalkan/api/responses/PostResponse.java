package com.bunyaminkalkan.api.responses;

import com.bunyaminkalkan.api.entities.Post;
import lombok.Data;

@Data
public class PostResponse {

    Long id;
    Long userId;
    String text;
    Integer likeCount;
    Integer dislikeCount;

    public PostResponse(Post entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.text = entity.getText();
        this.likeCount = entity.getLikeCount();
        this.dislikeCount = entity.getDislikeCount();
    }
}
