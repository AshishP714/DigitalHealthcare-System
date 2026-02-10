package com.tka.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tka.entity.Appointment;
import com.tka.entity.User;
import com.tka.service.AppointmentService;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // PATIENT: Book appointment
    @PostMapping("/create")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Appointment> createAppointment(
            @RequestBody Appointment appointment, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Appointment created = appointmentService.bookAppointmentForPatient(appointment, user);
        return ResponseEntity.ok(created);
    }

    // PATIENT: View own appointments
    @GetMapping("/my-appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<Appointment>> getMyAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(user);
        return ResponseEntity.ok(appointments);
    }

    // PATIENT: Cancel own appointment
    @DeleteMapping("/cancel/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> cancelMyAppointment(
            @PathVariable Long id, 
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        appointmentService.cancelAppointmentByPatient(id, user);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }

    // DOCTOR: View assigned appointments
    @GetMapping("/doctor/appointments")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(user);
        return ResponseEntity.ok(appointments);
    }

    // DOCTOR: Update appointment status
    @PutMapping("/doctor/{appointmentId}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestParam String status,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Appointment updated = appointmentService.updateAppointmentStatusByDoctor(appointmentId, status, user);
        return ResponseEntity.ok(updated);
    }

    // DOCTOR: Get patient info for appointment
    @GetMapping("/doctor/{appointmentId}/patient")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientForAppointment(
            @PathVariable Long appointmentId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Object patientInfo = appointmentService.getPatientInfoForDoctorAppointment(appointmentId, user);
        return ResponseEntity.ok(patientInfo);
    }

    // ADMIN: Get all appointments
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // ADMIN: Assign doctor to appointment
    @PutMapping("/admin/{appointmentId}/assign-doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Appointment> assignDoctor(
            @PathVariable Long appointmentId,
            @PathVariable Long doctorId) {
        Appointment updated = appointmentService.assignDoctorToAppointment(appointmentId, doctorId);
        return ResponseEntity.ok(updated);
    }

    // ADMIN: Delete any appointment
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }
}