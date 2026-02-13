package com.tka.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tka.dto.LoginRequestDTO;
import com.tka.dto.LoginResponseDTO;
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

    // Admin can register users with any role
    public String registerByAdmin(RegisterRequestDTO request) {
        if (userRepository.existsByUserEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUserName(request.getName());
        user.setUserEmail(request.getEmail());
        user.setUserPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole())); // Admin can set any role
        
        userRepository.save(user);
        
        return "User registered successfully with role: " + request.getRole();
    }

    // Regular registration (public endpoint)
    public String register(RegisterRequestDTO request) {
        if (userRepository.existsByUserEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUserName(request.getName());
        user.setUserEmail(request.getEmail());
//        user.setRole(request.getRole()); // FIXED: Removed valueOf(), getRole() already returns Role enum
        user.setRole(Role.valueOf(request.getRole()));
        user.setUserPassword(passwordEncoder.encode(request.getPassword()));
        
        userRepository.save(user);
        
        return "User registered successfully";
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUserEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getUserPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        // Generate token (uses email as username)
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        
        // Return response with ACTUAL NAME (not email)
        return new LoginResponseDTO(
            token,
            user.getUserId(),
            user.getUserName(),     // ✅ This gets the actual name from user_name column
            user.getUserEmail(),    // ✅ This gets the email
            user.getRole().name()
        );
    }
}