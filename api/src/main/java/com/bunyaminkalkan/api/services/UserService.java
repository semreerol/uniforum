package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.exceptions.BadRequestException;
import com.bunyaminkalkan.api.exceptions.ForbiddenException;
import com.bunyaminkalkan.api.exceptions.NotFoundException;
import com.bunyaminkalkan.api.exceptions.UnauthorizedException;
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

    public UserResponse getOneUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return new UserResponse(user);
    }

    public UserResponse updateOneUser(HttpHeaders headers, Long userId, User newUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        boolean isOwn = isOwnData(headers, user);

        if (!isOwn) {
            throw new ForbiddenException("You don't have permission to update this user");
        }

        updateUser(user, newUser);

        try {
            userRepository.save(user);
            return new UserResponse(user);
        } catch (Exception e) {
            throw new BadRequestException("Could not update user");
        }
    }

    public void deleteOneUser(HttpHeaders headers, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        boolean isOwn = isOwnData(headers, user);
        if (isOwn) {
            userRepository.delete(user);
        } else {
            throw new ForbiddenException("You don't have permission to delete this user");
        }
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void updateUser(User user, User newUser) {
        if (!isValidUserData(newUser)) {
            throw new BadRequestException("Invalid user data");
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
    }

    private boolean isOwnData(HttpHeaders headers, User user) {
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.isTokenValid(token, user);
        } else {
            throw new UnauthorizedException("Unauthorized");
        }
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
