package com.bunyaminkalkan.api.controllers;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.requests.UserCreateRequest;
import com.bunyaminkalkan.api.requests.UserLoginRequest;
import com.bunyaminkalkan.api.security.JwtTokenProvider;
import com.bunyaminkalkan.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);
        return "Bearer " + jwtToken;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserCreateRequest registerRequest) {
        if (userService.getOneUserByUserName(registerRequest.getUserName()) != null) {
            return new ResponseEntity<>("Username already exists.", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userService.createOneUser(user);
        return new ResponseEntity<>("User creation successfully.", HttpStatus.CREATED);
    }
}
