package com.nt.controller;

import org.springframework.web.bind.annotation.*;

import com.nt.dto.LoginRequest;
import com.nt.entity.User;
import com.nt.repository.UserRepository;
import com.nt.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil = new JwtUtil();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getId()); 
    }
}