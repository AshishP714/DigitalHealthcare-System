package com.tka.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.Appointment;
import com.tka.entity.Doctor;
import com.tka.entity.User;
import com.tka.service.AppointmentService;
import com.tka.service.DoctorService;

@RestController
@RequestMapping("/doctor")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

	private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public DoctorController(DoctorService doctorService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    // View appointments assigned to them
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAssignedAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(user);
        return ResponseEntity.ok(appointments);
    }

    // Update appointment status
    @PutMapping("/appointments/{appointmentId}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestParam String status,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Appointment updated = appointmentService.updateAppointmentStatusByDoctor(appointmentId, status, user);
        return ResponseEntity.ok(updated);
    }

    // Update own doctor profile
    @PutMapping("/profile")
    public ResponseEntity<Doctor> updateProfile(@RequestBody Doctor doctor, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Doctor updated = doctorService.updateOwnProfile(doctor, user);
        return ResponseEntity.ok(updated);
    }

    // View own doctor profile
    @GetMapping("/profile")
    public ResponseEntity<Doctor> getOwnProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Doctor doctor = doctorService.getDoctorByUserId(user.getUserId());
        return ResponseEntity.ok(doctor);
    }

    // Access patient info for their appointments only
    @GetMapping("/appointments/{appointmentId}/patient")
    public ResponseEntity<?> getPatientInfoForAppointment(
            @PathVariable Long appointmentId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Object patientInfo = appointmentService.getPatientInfoForDoctorAppointment(appointmentId, user);
        return ResponseEntity.ok(patientInfo);
    }
}
