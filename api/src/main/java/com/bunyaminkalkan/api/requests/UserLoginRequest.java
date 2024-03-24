package com.bunyaminkalkan.api.requests;

import lombok.Data;

@Data
public class UserLoginRequest {

    String userName;
    String password;
}
