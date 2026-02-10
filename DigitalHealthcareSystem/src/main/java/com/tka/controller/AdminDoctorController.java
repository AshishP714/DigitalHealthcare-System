package com.tka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tka.entity.Doctor;
import com.tka.service.DoctorService;

@RestController
@RequestMapping("/admin/doctors")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDoctorController {

    private final DoctorService doctorService;

    public AdminDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Create doctor
//    @PostMapping
//    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
//        Doctor created = doctorService.createDoctor(doctor); // CHANGED: addNewDoctor → createDoctor
//        return ResponseEntity.ok(created);
//    }

    // Get all doctors
//    @GetMapping
//    public ResponseEntity<List<Doctor>> getAllDoctors() {
//        List<Doctor> doctors = doctorService.getAllDoctors(); // CHANGED: getAllDoctor → getAllDoctors
//        return ResponseEntity.ok(doctors);
//    }

    // Get doctor by ID (you need to add this method to DoctorService)
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id); // You need to add this method
        return ResponseEntity.ok(doctor);
    }

    // Update doctor
//    @PutMapping("/{id}")
//    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
//        Doctor updated = doctorService.updateDoctor(id, updatedDoctor);
//        return ResponseEntity.ok(updated);
//    }

    // Delete doctor
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
//        doctorService.deleteDoctor(id);
//        return ResponseEntity.ok("Doctor deleted successfully");
//    }
}