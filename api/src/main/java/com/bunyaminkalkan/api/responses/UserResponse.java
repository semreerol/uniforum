package com.bunyaminkalkan.api.responses;

import com.bunyaminkalkan.api.entities.User;
import lombok.Data;

@Data
public class UserResponse {

    Long id;
    String userName;
    String email;
    String firstName;
    String lastName;
    String profilePhoto;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.userName = entity.getUserName();
        this.email = entity.getEmail();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.profilePhoto = entity.getProfilePhoto();
    }
}
