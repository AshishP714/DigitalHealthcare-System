package com.tka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    @GetMapping("/appointments")
    public ResponseEntity<String> viewAppointments() {
        return ResponseEntity.ok("Doctor appointments");
    }

    @PutMapping("/appointment/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(@PathVariable Long id) {
        return ResponseEntity.ok("Appointment status updated");
    }
}
