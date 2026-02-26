package com.tka.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tka.dto.CreateDoctorRequestDTO;
import com.tka.entity.Doctor;
import com.tka.entity.User;
import com.tka.repository.UserRepository;
import com.tka.service.DoctorService;

@RestController
@RequestMapping("/admin/doctors")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDoctorController {

    private final DoctorService doctorService;
    private final UserRepository userRepository;

    public AdminDoctorController(DoctorService doctorService, UserRepository userRepository) {
        this.doctorService = doctorService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> addDoctor(@RequestBody CreateDoctorRequestDTO request) {
        
        User user = userRepository.findByUserEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(
                    "User not found with email: " + request.getEmail() + 
                    ". Please create a user with DOCTOR role first using /admin/users endpoint"
                ));
        
        if (!user.getRole().name().equals("DOCTOR")) {
            return ResponseEntity.badRequest()
                    .body("User with email " + request.getEmail() + " does not have DOCTOR role");
        }
        
        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setSpecilization(request.getSpecilization());
        doctor.setEmail(request.getEmail());
        doctor.setUser(user);
        
        Doctor created = doctorService.createDoctor(doctor);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        Doctor updated = doctorService.updateDoctor(id, updatedDoctor);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }
}