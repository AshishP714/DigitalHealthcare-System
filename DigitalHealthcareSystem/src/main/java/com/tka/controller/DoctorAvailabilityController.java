package com.tka.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tka.dto.TimeSlotDTO;
import com.tka.entity.DoctorAvailability;
import com.tka.service.DoctorAvailabilityService;

@RestController
@RequestMapping("/availability")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;

    public DoctorAvailabilityController(DoctorAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAvailability>> getDoctorAvailability(
            @PathVariable Long doctorId) {
        List<DoctorAvailability> availability = availabilityService.getDoctorAvailability(doctorId);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/doctor/{doctorId}/slots")
    public ResponseEntity<List<TimeSlotDTO>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TimeSlotDTO> slots = availabilityService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(slots);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorAvailability> setDoctorAvailability(
            @RequestBody DoctorAvailability availability) {
        DoctorAvailability created = availabilityService.setDoctorAvailability(availability);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.ok("Availability deleted successfully");
    }
}