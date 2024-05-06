package com.bunyaminkalkan.api.requests;

import com.bunyaminkalkan.api.annotations.UniqueUserName;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {

    @UniqueUserName
    private String userName;
    private String password;
    @Email(message = "Please enter a valid email address.")
    private String email;
    private String firstName;
    private String lastName;
    private MultipartFile profilePhoto;
}
