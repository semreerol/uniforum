package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.User;
import com.bunyaminkalkan.api.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOneUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
