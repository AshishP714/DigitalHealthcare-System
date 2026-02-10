package com.tka.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tka.dto.LoginRequestDTO;
import com.tka.dto.RegisterRequestDTO;
import com.tka.entity.User;
import com.tka.entity.type.Role;
import com.tka.repository.UserRepository;
import com.tka.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequestDTO request) {

        User user = new User();
        user.setUserName(request.getName());
        user.setUserEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole()));
        user.setUserPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        
        return "User registered successfully";
    }

    public String login(LoginRequestDTO request) {

        User user = userRepository.findByUserEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getUserPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name()); 
    }
}