package com.bunyaminkalkan.api.controllers;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.responses.UserResponse;
import com.bunyaminkalkan.api.services.UserService;
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

    @PostMapping
    public UserResponse createOneUser(@RequestBody User newUser){
        return userService.createOneUser(newUser);
    }

    @GetMapping("/{userId}")
    public UserResponse getOneUser(@PathVariable Long userId){
        return userService.getOneUserByID(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateOneUser(@RequestHeader HttpHeaders headers, @PathVariable Long userId, @RequestBody User newUser){
        return userService.updateOneUser(headers, userId, newUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteOneUser(@RequestHeader HttpHeaders headers, @PathVariable Long userId){
        userService.deleteOneUser(headers, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
