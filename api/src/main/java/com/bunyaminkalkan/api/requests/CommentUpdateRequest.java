package com.bunyaminkalkan.api.requests;

import lombok.Data;

@Data
public class CommentUpdateRequest {

    String text;
    Integer likeCount;
    Integer dislikeCount;
}
