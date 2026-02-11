package com.tka.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tka.dto.RegisterRequestDTO;
import com.tka.entity.Appointment;
import com.tka.entity.Doctor;
import com.tka.entity.Patient;
import com.tka.entity.User;
import com.tka.service.AppointmentService;
import com.tka.service.AuthService;
import com.tka.service.DoctorService;
import com.tka.service.PatientService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final AuthService authService;

    public AdminController(DoctorService doctorService, 
                          PatientService patientService,
                          AppointmentService appointmentService,
                          AuthService authService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.authService = authService;
    }

    // ===== USER MANAGEMENT =====
    
    // Create users of any role
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody RegisterRequestDTO request) {
        String result = authService.registerByAdmin(request);
        return ResponseEntity.ok(result);
    }

    // ===== DOCTOR MANAGEMENT =====
    
    // Create doctor
    @PostMapping("/doctor")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor created = doctorService.createDoctor(doctor);
        return ResponseEntity.ok(created);
    }

    // Update doctor
    @PutMapping("/doctors/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long doctorId, @RequestBody Doctor doctor) {
        Doctor updated = doctorService.updateDoctor(doctorId, doctor);
        return ResponseEntity.ok(updated);
    }

    // Delete doctor
    @DeleteMapping("/doctors/{doctorId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok("Doctor deleted successfully");
    }

    // Get all doctors
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // ===== PATIENT MANAGEMENT (Optional) =====
    
    // Get all patients
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    // Get patient by ID
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long patientId) {
        Patient patient = patientService.findPatientById(patientId);
        return ResponseEntity.ok(patient);
    }

    // ===== APPOINTMENT MANAGEMENT =====
    
    // View all appointments
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // Assign doctor to appointment
    @PutMapping("/appointments/{appointmentId}/assign-doctor/{doctorId}")
    public ResponseEntity<Appointment> assignDoctorToAppointment(
            @PathVariable Long appointmentId,
            @PathVariable Long doctorId) {
        Appointment updated = appointmentService.assignDoctorToAppointment(appointmentId, doctorId);
        return ResponseEntity.ok(updated);
    }

    // Delete any appointment
    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok("Appointment deleted successfully");
    }
}