package com.bunyaminkalkan.api.requests;

import com.bunyaminkalkan.api.annotations.UniqueUserName;
import com.bunyaminkalkan.api.annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username cannot be blank.")
    @UniqueUserName
    private String userName;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    @ValidPassword
    private String password;

    private Long universityId;
    private Long departmentId;
}

