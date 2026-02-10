package com.tka.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.Appointment;
import com.tka.entity.Doctor;
import com.tka.entity.Patient;
import com.tka.entity.User;
import com.tka.service.AppointmentService;
import com.tka.service.DoctorService;
import com.tka.service.PatientService;


@RestController
@RequestMapping("/patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public PatientController(PatientService patientService, 
                           AppointmentService appointmentService,
                           DoctorService doctorService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @PostMapping("/profile")
    public ResponseEntity<Patient> createProfile(@RequestBody Patient patient, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient created = patientService.createPatientProfile(patient, user);
        return ResponseEntity.ok(created);
    }

    // Update own patient profile
    @PutMapping("/profile")
    public ResponseEntity<Patient> updateProfile(@RequestBody Patient patient, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient updated = patientService.updateOwnProfile(patient, user);
        return ResponseEntity.ok(updated);
    }

    // View own profile
    @GetMapping("/profile")
    public ResponseEntity<Patient> getOwnProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Patient patient = patientService.getPatientByUserId(user.getUserId());
        return ResponseEntity.ok(patient);
    }

    // Book appointment
    @PostMapping("/appointments")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Appointment booked = appointmentService.bookAppointmentForPatient(appointment, user);
        return ResponseEntity.ok(booked);
    }

    // View own appointments
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getOwnAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(user);
        return ResponseEntity.ok(appointments);
    }

    // Cancel own appointment
    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        appointmentService.cancelAppointmentByPatient(appointmentId, user);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }

    // Access doctor list
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getDoctorList() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
}