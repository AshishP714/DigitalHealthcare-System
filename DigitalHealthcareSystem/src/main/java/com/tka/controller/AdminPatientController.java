package com.tka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tka.service.PatientService;

@RestController
@RequestMapping("/admin/patients")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPatientController {

    private final PatientService patientService;

    public AdminPatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    // Get all patients
//    @GetMapping
//    public ResponseEntity<List<Patient>> getAllPatients() {
//        List<Patient> patients = patientService.getAllPatients(); // CHANGED: getAllPatient → getAllPatients
//        return ResponseEntity.ok(patients);
//    }

    // Get patient by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
//        Patient patient = patientService.findPatientById(id);
//        return ResponseEntity.ok(patient);
//    }

    // Delete patient
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id); // You need to add this method to PatientService
        return ResponseEntity.ok("Patient deleted successfully");
    }
}
