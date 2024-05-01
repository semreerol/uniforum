package com.bunyaminkalkan.api.requests;

import lombok.Data;

@Data
public class RegisterRequest {

    String userName;
    String email;
    String password;
}

