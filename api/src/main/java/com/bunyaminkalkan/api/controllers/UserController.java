package com.bunyaminkalkan.api.controllers;

import com.bunyaminkalkan.api.requests.UserUpdateRequest;
import com.bunyaminkalkan.api.responses.UserResponse;
import com.bunyaminkalkan.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getOneUser(@PathVariable Long userId){
        return userService.getOneUser(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateOneUser(@RequestHeader HttpHeaders headers, @PathVariable Long userId, @Valid @ModelAttribute UserUpdateRequest userUpdateRequest){
        return userService.updateOneUser(headers, userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteOneUser(@RequestHeader HttpHeaders headers, @PathVariable Long userId){
        userService.deleteOneUser(headers, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(value = "/{userId}/profilePhoto", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Long userId){
        return userService.getProfilePhoto(userId);
    }

    @DeleteMapping("/{userId}/profilePhoto")
    public ResponseEntity<?> deleteProfilePhoto(@RequestHeader HttpHeaders headers, @PathVariable Long userId){
        userService.deleteProfilePhoto(headers, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
