package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.exceptions.InvalidUserDataRequestException;
import com.bunyaminkalkan.api.exceptions.UserNotFoundException;
import com.bunyaminkalkan.api.repos.UserRepository;
import com.bunyaminkalkan.api.responses.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        List<User> list = userRepository.findAll();
        return list.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public UserResponse createOneUser(User newUser) {
        try {
            User user = userRepository.save(newUser);
            return new UserResponse(user);
        }catch (Exception e) {
            throw new InvalidUserDataRequestException("Could not create user");
        }
    }

    public UserResponse getOneUserByID(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new UserResponse(user);
    }

    public UserResponse updateOneUser(Long userId, User newUser) {
        try {
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            if (!isValidUserData(newUser)) {
                throw new InvalidUserDataRequestException("Invalid user data");
            }

            if (newUser.getUserName() != null) {
                user.setUserName(newUser.getUserName());
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

        } catch (InvalidUserDataRequestException e) {
            throw new InvalidUserDataRequestException("Invalid user data");
        }
    }

    public void deleteOneUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    private boolean isValidUserData(User user) {
        return !(user.getUserName() == null)
                || !(user.getEmail() == null)
                || !(user.getFirstName() == null)
                || !(user.getLastName() == null)
                || !(user.getPassword() == null)
                || !(user.getProfilePhoto() == null);
    }

}
