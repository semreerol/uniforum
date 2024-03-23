package com.bunyaminkalkan.api.requests;

import lombok.Data;

@Data
public class PostUpdateRequest {

    String text;
    Integer likeCount;
    Integer dislikeCount;
}
