package com.bunyaminkalkan.api.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username cannot be blank.")
    private String userName;

    @NotBlank(message = "Password cannot be blank.")
    private String password;
}
