package com.bunyaminkalkan.api.requests;

import lombok.Data;

@Data
public class UserCreateRequest {

    String userName;
    String email;
    String password;
}
