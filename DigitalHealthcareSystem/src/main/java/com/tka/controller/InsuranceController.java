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

    @PostMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Insurance> addInsurance(
            @RequestBody Insurance insurance, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Insurance created = insuranceService.addInsuranceForPatient(insurance, user);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Insurance> getMyInsurance(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Insurance insurance = insuranceService.getInsuranceForPatient(user);
        
        if (insurance == null) {
            return ResponseEntity.ok(null);
        }
        
        return ResponseEntity.ok(insurance);
    }

    @PutMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Insurance> updateInsurance(
            @RequestBody Insurance insurance,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Insurance updated = insuranceService.updateInsurance(insurance, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/patient/insurance")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> deleteInsurance(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        insuranceService.deleteInsurance(user);
        return ResponseEntity.ok("Insurance deleted successfully");
    }

    @GetMapping("/admin/patients/{patientId}/insurance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Insurance> getInsuranceByPatient(@PathVariable Long patientId) {
        Insurance insurance = insuranceService.getInsuranceByPatientId(patientId);
        return ResponseEntity.ok(insurance);
    }
}