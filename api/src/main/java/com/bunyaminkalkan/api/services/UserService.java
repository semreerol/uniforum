package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.exceptions.BadRequestException;
import com.bunyaminkalkan.api.exceptions.ForbiddenException;
import com.bunyaminkalkan.api.exceptions.NotFoundException;
import com.bunyaminkalkan.api.exceptions.UnauthorizedException;
import com.bunyaminkalkan.api.repos.UserRepository;
import com.bunyaminkalkan.api.requests.UserUpdateRequest;
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

    public UserResponse updateOneUser(HttpHeaders headers, Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (!isOwnData(headers, user)) {
            throw new ForbiddenException("You don't have permission to update this user");
        }

        updateUser(user, userUpdateRequest);

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

    private void updateUser(User user, UserUpdateRequest userUpdateRequest) {

        if (userUpdateRequest.getUserName() != null) {
            user.setUserName(userUpdateRequest.getUserName());
        }
        if (userUpdateRequest.getEmail() != null) {
            user.setEmail(userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getFirstName() != null) {
            user.setFirstName(userUpdateRequest.getFirstName());
        }
        if (userUpdateRequest.getLastName() != null) {
            user.setLastName(userUpdateRequest.getLastName());
        }
        if (userUpdateRequest.getPassword() != null) {
            user.setPassword(userUpdateRequest.getPassword());
        }
        if (userUpdateRequest.getProfilePhoto() != null) {
            user.setProfilePhoto(userUpdateRequest.getProfilePhoto());
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

}
