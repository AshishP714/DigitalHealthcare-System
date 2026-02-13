package com.tka.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.Appointment;
import com.tka.entity.User;
import com.tka.service.AppointmentService;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // ============================================
    // PATIENT ENDPOINTS
    // ============================================

    // Patient: Book appointment
    @PostMapping("/patient/appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Appointment> bookAppointment(
            @RequestBody Appointment appointment, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Appointment created = appointmentService.bookAppointmentForPatient(appointment, user);
        return ResponseEntity.ok(created);
    }

    // Patient: View own appointments
    @GetMapping("/patient/appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<Appointment>> getMyAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(user);
        return ResponseEntity.ok(appointments);
    }

    // Patient: Cancel own appointment
    @DeleteMapping("/patient/appointments/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> cancelMyAppointment(
            @PathVariable Long id, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        appointmentService.cancelAppointmentByPatient(id, user);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }

    // ============================================
    // DOCTOR ENDPOINTS
    // ============================================

    // Doctor: View assigned appointments
    @GetMapping("/doctor/appointments")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(user);
        return ResponseEntity.ok(appointments);
    }

    // Doctor: Update appointment status
    @PutMapping("/doctor/appointments/{id}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Appointment updated = appointmentService.updateAppointmentStatusByDoctor(id, status, user);
        return ResponseEntity.ok(updated);
    }

    // Doctor: Get patient info for appointment
    @GetMapping("/doctor/appointments/{id}/patient")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientForAppointment(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Object patientInfo = appointmentService.getPatientInfoForDoctorAppointment(id, user);
        return ResponseEntity.ok(patientInfo);
    }

    // ============================================
    // ADMIN ENDPOINTS
    // ============================================

    // Admin: Get all appointments
    @GetMapping("/admin/appointments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // Admin: Assign doctor to appointment
    @PutMapping("/admin/appointments/{appointmentId}/doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Appointment> assignDoctor(
            @PathVariable Long appointmentId,
            @PathVariable Long doctorId) {
        Appointment updated = appointmentService.assignDoctorToAppointment(appointmentId, doctorId);
        return ResponseEntity.ok(updated);
    }

    // Admin: Delete any appointment
    @DeleteMapping("/admin/appointments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }
}