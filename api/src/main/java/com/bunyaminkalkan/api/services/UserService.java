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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private String uploadDirectory = "src/main/resources/static/images/profiles";

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
        updateUserWithoutPassAndPhoto(user, userUpdateRequest);
        if (checkPasswordsField(user, userUpdateRequest.getCurrentPassword(), userUpdateRequest.getNewPassword(), userUpdateRequest.getConfirmNewPassword())) {
            String newPasswordEncoded = passwordEncoder.encode(userUpdateRequest.getNewPassword());
            user.setPassword(newPasswordEncoded);
        }
        UserResponse userResponse = new UserResponse(user);
        MultipartFile profilePhoto = userUpdateRequest.getProfilePhoto();
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + profilePhoto.getOriginalFilename().replaceFirst("[.][^.]+$", ".png");
            try {
                Path uploadPath = Paths.get(uploadDirectory);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path destinationPath = uploadPath.resolve(uniqueFileName);
                Files.copy(profilePhoto.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                user.setProfilePhoto(uniqueFileName);
                userResponse.setProfilePhoto(uniqueFileName);
            } catch (IOException e) {
                throw new BadRequestException("Could not update profile photo");
            }
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new BadRequestException("Could not update user");
        }
        return userResponse;
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

    public ResponseEntity<byte[]> getProfilePhoto(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getProfilePhoto() == null) {
            throw new NotFoundException("Profile photo not found");
        }
        String profilePhotoName = user.getProfilePhoto();
        Path profilePhotoPath = Path.of(uploadDirectory, profilePhotoName);
        System.out.println(profilePhotoPath);
        if (Files.exists(profilePhotoPath)) {
            try {
                byte[] profilePhotoBytes = Files.readAllBytes(profilePhotoPath);
                return ResponseEntity.ok().body(profilePhotoBytes);
            } catch (Exception e) {
                throw new NotFoundException("Profile photo not found");
            }
        }
        return ResponseEntity.notFound().build();
    }

    public void deleteProfilePhoto(HttpHeaders headers, Long userId) {
        User user = jwtService.getUserFromHeaders(headers);
        String profilePhotoName = user.getProfilePhoto();
        Path profilePhotoPath = Path.of(uploadDirectory, profilePhotoName);
        if (user.getId().equals(userId)) {
            user.setProfilePhoto(null);
            userRepository.save(user);
        } else {
            throw new ForbiddenException("You don't have permission to delete this user's profile photo");
        }
        if (Files.exists(profilePhotoPath)) {
            try {
                Files.delete(profilePhotoPath);
            } catch (Exception e) {
                throw new BadRequestException("Something went wrong while deleting profile photo");
            }
        }
    }

    //Private

    private boolean checkPasswordsField(User user, String currentPassword, String newPassword, String confirmNewPassword) {
        if (currentPassword == null && newPassword == null && confirmNewPassword == null) {
            return false;
        } else if (currentPassword != null && newPassword != null && confirmNewPassword != null) {
            String userPass = user.getPassword();
            if (passwordEncoder.matches(currentPassword, userPass)) {
                if (currentPassword.equals(newPassword)) {
                    throw new BadRequestException("New password cannot be the same as old password.");
                }
                String newPassEncoded = passwordEncoder.encode(newPassword);
                if (passwordEncoder.matches(confirmNewPassword, newPassEncoded)) {
                    return true;
                } else {
                    throw new BadRequestException("New Password fields do not match");
                }
            } else {
                throw new BadRequestException("Passwords do not match");
            }

        } else {
            throw new BadRequestException("Fill all password fields");
        }
    }

    private void updateUserWithoutPassAndPhoto(User user, UserUpdateRequest userUpdateRequest) {

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
