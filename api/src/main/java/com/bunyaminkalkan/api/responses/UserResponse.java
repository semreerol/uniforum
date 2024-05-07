package com.bunyaminkalkan.api.responses;

import com.bunyaminkalkan.api.entities.Department;
import com.bunyaminkalkan.api.entities.University;
import com.bunyaminkalkan.api.entities.User;
import lombok.Data;

import java.util.Optional;

@Data
public class UserResponse {

    Long id;
    String userName;
    String email;
    String firstName;
    String lastName;
    String universityName;
    String departmentName;
    String profilePhoto;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.userName = entity.getUsername();
        this.email = entity.getEmail();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.universityName = Optional.ofNullable(entity.getUniversity()).map(University::getName).orElse(null);
        this.departmentName = Optional.ofNullable(entity.getDepartment()).map(Department::getName).orElse(null);
        this.profilePhoto = entity.getProfilePhoto();
    }
}
