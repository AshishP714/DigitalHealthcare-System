package com.tka.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Appointment;
import com.tka.entity.Doctor;
import com.tka.entity.Patient;
import com.tka.repository.AppointmentRepostory;
import com.tka.repository.DoctorRepository;
import com.tka.repository.PatientRepository;
import com.tka.security.SecurityUtil;

@Service
public class AppointmentService {

    private final AppointmentRepostory appointmentRepostory;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepostory appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepostory = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public Appointment createAppointment(Long doctorId, Appointment appointment) {

        String email = SecurityUtil.getLoggedInUsername();
        
        Patient patient = patientRepository.findByPatientEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        return appointmentRepostory.save(appointment);
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepostory.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id " + appointmentId));
    }

    public List<Appointment> getAllAppointment() {
        return appointmentRepostory.findAll();
    }

    public void cancelAppointment(Long appointmentId) {
        if (!appointmentRepostory.existsById(appointmentId)) {
            throw new IllegalArgumentException("Appointment not found with id " + appointmentId);
        }
        appointmentRepostory.deleteById(appointmentId);
    }
}