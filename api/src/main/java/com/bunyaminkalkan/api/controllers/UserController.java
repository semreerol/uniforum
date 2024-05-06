package com.bunyaminkalkan.api.controllers;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.requests.UserUpdateRequest;
import com.bunyaminkalkan.api.responses.UserResponse;
import com.bunyaminkalkan.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public UserResponse updateOneUser(@RequestHeader HttpHeaders headers, @PathVariable Long userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest){
        return userService.updateOneUser(headers, userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteOneUser(@RequestHeader HttpHeaders headers, @PathVariable Long userId){
        userService.deleteOneUser(headers, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
