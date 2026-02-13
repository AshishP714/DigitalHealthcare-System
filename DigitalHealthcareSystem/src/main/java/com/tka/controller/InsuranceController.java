package com.tka.controller;

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

    // Patient: Add insurance (only if they don't have one)
    @PostMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Insurance> addInsurance(
            @RequestBody Insurance insurance, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Insurance created = insuranceService.addInsuranceForPatient(insurance, user);
        return ResponseEntity.ok(created);
    }

    // Patient: View own insurance (returns single object, not array)
    @GetMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Insurance> getMyInsurance(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Insurance insurance = insuranceService.getInsuranceForPatient(user);
        
        if (insurance == null) {
            return ResponseEntity.ok(null);  // Or return 404 if you prefer
        }
        
        return ResponseEntity.ok(insurance);
    }

    // Patient: Update own insurance (no ID needed)
    @PutMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Insurance> updateInsurance(
            @RequestBody Insurance insurance,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Insurance updated = insuranceService.updateInsurance(insurance, user);
        return ResponseEntity.ok(updated);
    }

    // Patient: Delete own insurance (no ID needed)
    @DeleteMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> deleteInsurance(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        insuranceService.deleteInsurance(user);
        return ResponseEntity.ok("Insurance deleted successfully");
    }

    // ============================================
    // ADMIN ENDPOINTS
    // ============================================

    // Admin: View insurance by patient ID
    @GetMapping("/admin/patients/{patientId}/insurance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Insurance> getInsuranceByPatient(@PathVariable Long patientId) {
        Insurance insurance = insuranceService.getInsuranceByPatientId(patientId);
        return ResponseEntity.ok(insurance);
    }
}