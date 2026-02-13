package com.tka.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Appointment;
import com.tka.entity.Doctor;
import com.tka.entity.Patient;
import com.tka.entity.User;
import com.tka.entity.type.AppointmentStatus;
import com.tka.repository.AppointmentRepostory;
import com.tka.repository.DoctorRepository;
import com.tka.repository.PatientRepository;

@Service
public class AppointmentService {

    private final AppointmentRepostory appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepostory appointmentRepository,
                            PatientRepository patientRepository,
                            DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // Book appointment for patient
    public Appointment bookAppointmentForPatient(Appointment appointment, User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found. Please create your profile first.");
        }
        
        // Validate doctor is assigned
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            throw new RuntimeException("Doctor must be selected for appointment");
        }
        
        // Check for double-booking
        boolean isSlotTaken = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(
            appointment.getDoctor(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime()
        );
        
        if (isSlotTaken) {
            throw new RuntimeException(
                "This time slot is already booked. Please choose another time."
            );
        }
        
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.PENDING);
        return appointmentRepository.save(appointment);
    }

    // Get appointments for patient
    public List<Appointment> getAppointmentsForPatient(User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        return appointmentRepository.findByPatient(patient);
    }

    // Cancel appointment by patient
    public void cancelAppointmentByPatient(Long appointmentId, User user) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        Patient patient = patientRepository.findByUser(user);
        if (!appointment.getPatient().getPatientId().equals(patient.getPatientId())) {
            throw new RuntimeException("Unauthorized: You can only cancel your own appointments");
        }
        
        appointmentRepository.deleteById(appointmentId);
    }

    // Get appointments for doctor
    public List<Appointment> getAppointmentsForDoctor(User user) {
        Doctor doctor = doctorRepository.findByUser(user);
        if (doctor == null) {
            throw new RuntimeException("Doctor profile not found");
        }
        return appointmentRepository.findByDoctor(doctor);
    }

    // Update appointment status by doctor
    public Appointment updateAppointmentStatusByDoctor(Long appointmentId, String status, User user) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        Doctor doctor = doctorRepository.findByUser(user);
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("Unauthorized: You can only update your own appointments");
        }
        
        appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        return appointmentRepository.save(appointment);
    }

    // Get patient info for doctor's appointment
    public Object getPatientInfoForDoctorAppointment(Long appointmentId, User user) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        Doctor doctor = doctorRepository.findByUser(user);
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException("Unauthorized: You can only view patients for your own appointments");
        }
        
        return appointment.getPatient();
    }

    // Admin methods
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment assignDoctorToAppointment(Long appointmentId, Long doctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        appointment.setDoctor(doctor);
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new RuntimeException("Appointment not found");
        }
        appointmentRepository.deleteById(appointmentId);
    }
}