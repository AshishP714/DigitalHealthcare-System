package com.tka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tka.dto.RegisterRequestDTO;
import com.tka.service.AuthService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    // Admin creates user with any role
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody RegisterRequestDTO request) {
        String result = authService.registerByAdmin(request);
        return ResponseEntity.ok(result);
    }
}