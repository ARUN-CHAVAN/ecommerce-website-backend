package com.nt.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nt.entity.User;
import com.nt.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}