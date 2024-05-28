package com.bunyaminkalkan.api.responses;

import com.bunyaminkalkan.api.entities.Post;
import lombok.Data;

@Data
public class PostResponse {

    Long id;
    Long userId;
    String text;
    Integer likes;
    Integer dislikes;
    Boolean liked = false;
    Boolean disliked = false;

    public PostResponse(Post entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.text = entity.getText();
        this.likes = entity.getLikedUsers().size();
        this.dislikes = entity.getDislikedUsers().size();
    }
}
