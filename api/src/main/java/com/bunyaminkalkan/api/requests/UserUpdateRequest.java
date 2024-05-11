package com.bunyaminkalkan.api.requests;

import com.bunyaminkalkan.api.annotations.UniqueUserName;
import com.bunyaminkalkan.api.annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {

    @UniqueUserName
    private String userName;
    private String currentPassword;
    @ValidPassword
    private String newPassword;
    private String confirmNewPassword;
    @Email(message = "Please enter a valid email address.")
    private String email;
    private String firstName;
    private String lastName;
    private MultipartFile profilePhoto;
}
