package com.tka.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.Insurance;
import com.tka.entity.User;
import com.tka.service.InsuranceService;

@RestController
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    // ============================================
    // PATIENT ENDPOINTS
    // ============================================

    // Patient: Add insurance
    @PostMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Insurance> addInsurance(
            @RequestBody Insurance insurance, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Insurance created = insuranceService.addInsuranceForPatient(insurance, user);
        return ResponseEntity.ok(created);
    }

    // Patient: View own insurance
    @GetMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<Insurance>> getMyInsurance(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Insurance> insurance = insuranceService.getInsuranceForPatient(user);
        return ResponseEntity.ok(insurance);
    }

    // Patient: Update own insurance
    @PutMapping("/patient/insurance/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Insurance> updateInsurance(
            @PathVariable Long id,
            @RequestBody Insurance insurance,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Insurance updated = insuranceService.updateInsurance(id, insurance, user);
        return ResponseEntity.ok(updated);
    }

    // Patient: Delete own insurance
    @DeleteMapping("/patient/insurance/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> deleteInsurance(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        insuranceService.deleteInsurance(id, user);
        return ResponseEntity.ok("Insurance deleted successfully");
    }

    // ============================================
    // ADMIN ENDPOINTS
    // ============================================

    // Admin: View all insurance
    @GetMapping("/admin/insurance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Insurance>> getAllInsurance() {
        List<Insurance> insurance = insuranceService.getAllInsurance();
        return ResponseEntity.ok(insurance);
    }

    // Admin: View insurance by patient ID
    @GetMapping("/admin/patients/{patientId}/insurance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Insurance>> getInsuranceByPatient(@PathVariable Long patientId) {
        List<Insurance> insurance = insuranceService.getInsuranceByPatientId(patientId);
        return ResponseEntity.ok(insurance);
    }
}