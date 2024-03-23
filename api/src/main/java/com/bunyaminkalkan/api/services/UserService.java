package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.repos.UserRepository;
import com.bunyaminkalkan.api.responses.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        List<User> list = userRepository.findAll();
        return list.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public UserResponse createOneUser(User newUser) {
        User user = userRepository.save(newUser);
        return new UserResponse(user);
    }

    public UserResponse getOneUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        return new UserResponse(user);
    }

    public UserResponse updateOneUser(Long userId, User newUser) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            User foundUser = user.get();
            if (newUser.getUserName() != null){
                foundUser.setUserName(newUser.getUserName());
            }
            if (newUser.getEmail() != null){
                foundUser.setEmail(newUser.getEmail());
            }
            if (newUser.getFirstName() != null){
                foundUser.setFirstName(newUser.getFirstName());
            }
            if (newUser.getLastName() != null){
                foundUser.setLastName(newUser.getLastName());
            }
            if (newUser.getPassword() != null){
                foundUser.setPassword(newUser.getPassword());
            }
            if (newUser.getProfilePhoto() != null){
                foundUser.setProfilePhoto(newUser.getProfilePhoto());
            }
            userRepository.save(foundUser);
            return new UserResponse(foundUser);
        }else return null;
    }

    public void deleteOneUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
