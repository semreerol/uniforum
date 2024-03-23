package com.bunyaminkalkan.api.requests;

import lombok.Data;

@Data
public class CommentCreateRequest {

    Long userId;
    Long postId;
    String text;
}
