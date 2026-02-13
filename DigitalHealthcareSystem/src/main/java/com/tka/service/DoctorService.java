package com.tka.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Doctor;
import com.tka.entity.User;
import com.tka.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Create doctor (Admin only)
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Get doctor by ID
    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
    }

    // Get doctor by User object
    public Doctor getDoctorByUser(User user) {
        Doctor doctor = doctorRepository.findByUser(user);
        if (doctor == null) {
            throw new RuntimeException("Doctor profile not found for user: " + user.getUserEmail());
        }
        return doctor;
    }

    // Update doctor (Admin only)
    public Doctor updateDoctor(Long doctorId, Doctor doctor) {
        Doctor existing = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        existing.setName(doctor.getName());
        existing.setSpecilization(doctor.getSpecilization());
        existing.setEmail(doctor.getEmail());
        
        return doctorRepository.save(existing);
    }

    // Delete doctor (Admin only)
    public void deleteDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor not found");
        }
        doctorRepository.deleteById(doctorId);
    }

    // Update own profile (Doctor only)
    public Doctor updateOwnProfile(Doctor doctor, User user) {
        Doctor existing = doctorRepository.findByUser(user);
        if (existing == null) {
            throw new RuntimeException("Doctor profile not found");
        }
        
        existing.setName(doctor.getName());
        existing.setSpecilization(doctor.getSpecilization());
        existing.setEmail(doctor.getEmail());
        
        return doctorRepository.save(existing);
    }

    // Get doctor by user ID
    public Doctor getDoctorByUserId(Long userId) {
        return doctorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
    }

    // Get all doctors (accessible by all)
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}