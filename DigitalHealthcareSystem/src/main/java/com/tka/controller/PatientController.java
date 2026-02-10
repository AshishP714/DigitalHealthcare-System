package com.tka.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.Patient;
import com.tka.service.PatientService;

@RestController
@RequestMapping("/patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.createNewPatient(patient);
    }

    @GetMapping("/me/{id}")
    public Patient getOwnPatient(@PathVariable Long id) {
        return patientService.findPatientById(id);
    }

    @PostMapping("/appointment")
    public String bookAppointment() {
        return "Appointment booked";
    }
}