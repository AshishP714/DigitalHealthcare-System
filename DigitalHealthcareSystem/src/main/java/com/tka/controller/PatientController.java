package com.tka.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.Doctor;
import com.tka.entity.Patient;
import com.tka.entity.User;
import com.tka.service.DoctorService;
import com.tka.service.PatientService;

@RestController
@RequestMapping("/patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    public PatientController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    // Create patient profile
    @PostMapping("/profile")
    public ResponseEntity<Patient> createProfile(
            @RequestBody Patient patient, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient created = patientService.createPatientProfile(patient, user);
        return ResponseEntity.ok(created);
    }

    // Get own profile
    @GetMapping("/profile")
    public ResponseEntity<Patient> getOwnProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient patient = patientService.getPatientByUser(user);
        return ResponseEntity.ok(patient);
    }

    // Update own profile
    @PutMapping("/profile")
    public ResponseEntity<Patient> updateOwnProfile(
            @RequestBody Patient patient, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient updated = patientService.updateOwnProfile(patient, user);
        return ResponseEntity.ok(updated);
    }

    // View all doctors
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
}