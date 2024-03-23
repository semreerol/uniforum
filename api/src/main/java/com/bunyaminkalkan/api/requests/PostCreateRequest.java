package com.bunyaminkalkan.api.requests;

import lombok.Data;

@Data
public class PostCreateRequest {

    Long userId;
    String text;
}
