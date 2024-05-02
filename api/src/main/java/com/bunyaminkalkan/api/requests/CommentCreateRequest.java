package com.bunyaminkalkan.api.requests;

import lombok.Data;

@Data
public class CommentCreateRequest {

    Long postId;
    String text;
}
