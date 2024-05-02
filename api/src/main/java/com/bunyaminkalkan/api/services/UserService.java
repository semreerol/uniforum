package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.exceptions.InvalidUserDataRequestException;
import com.bunyaminkalkan.api.exceptions.UserNotFoundRequestException;
import com.bunyaminkalkan.api.repos.UserRepository;
import com.bunyaminkalkan.api.responses.UserResponse;
import com.bunyaminkalkan.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public List<UserResponse> getAllUsers() {
        List<User> list = userRepository.findAll();
        return list.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public UserResponse createOneUser(User newUser) {
        try {
            User user = userRepository.save(newUser);
            return new UserResponse(user);
        } catch (Exception e) {
            throw new InvalidUserDataRequestException("Could not create user");
        }
    }

    public UserResponse getOneUserByID(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundRequestException("User not found"));
        return new UserResponse(user);
    }

    public UserResponse updateOneUser(HttpHeaders headers, Long userId, User newUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundRequestException("User not found"));
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (!jwtService.isTokenValid(token, user)) {
                throw new InvalidUserDataRequestException("Invalid token");
            }
        }
        if (!isValidUserData(newUser)) {
            throw new InvalidUserDataRequestException("Invalid user data");
        }
        if (newUser.getUsername() != null) {
            user.setUserName(newUser.getUsername());
        }
        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }
        if (newUser.getFirstName() != null) {
            user.setFirstName(newUser.getFirstName());
        }
        if (newUser.getLastName() != null) {
            user.setLastName(newUser.getLastName());
        }
        if (newUser.getPassword() != null) {
            user.setPassword(newUser.getPassword());
        }
        if (newUser.getProfilePhoto() != null) {
            user.setProfilePhoto(newUser.getProfilePhoto());
        }

        userRepository.save(user);
        return new UserResponse(user);

    }

    public void deleteOneUser(Long userId) {
        boolean isFound = userRepository.existsById(userId);
        if (isFound) {
            userRepository.deleteById(userId);
        }else {
            throw new UserNotFoundRequestException("User not found");
        }
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new UserNotFoundRequestException("User not found"));
    }

    private boolean isValidUserData(User user) {
        return !(user.getUsername() == null)
                || !(user.getEmail() == null)
                || !(user.getFirstName() == null)
                || !(user.getLastName() == null)
                || !(user.getPassword() == null)
                || !(user.getProfilePhoto() == null);
    }

}
