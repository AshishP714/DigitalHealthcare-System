package com.tka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.Doctor;
import com.tka.entity.User;
import com.tka.service.DoctorService;

@RestController
@RequestMapping("/doctor")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Get own profile
    @GetMapping("/profile")
    public ResponseEntity<Doctor> getOwnProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Doctor doctor = doctorService.getDoctorByUser(user);
        return ResponseEntity.ok(doctor);
    }

    // Update own profile
    @PutMapping("/profile")
    public ResponseEntity<Doctor> updateOwnProfile(
            @RequestBody Doctor doctor, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Doctor updated = doctorService.updateOwnProfile(doctor, user);
        return ResponseEntity.ok(updated);
    }
}