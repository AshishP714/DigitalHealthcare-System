package com.tka.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tka.entity.Insurance;
import com.tka.entity.Patient;
import com.tka.entity.User;
import com.tka.repository.InsuranceRepository;
import com.tka.repository.PatientRepository;

@Service
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    public InsuranceService(InsuranceRepository insuranceRepository, 
                          PatientRepository patientRepository) {
        this.insuranceRepository = insuranceRepository;
        this.patientRepository = patientRepository;
    }

    // Patient adds insurance to their profile (only if they don't have one)
    public Insurance addInsuranceForPatient(Insurance insurance, User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        
        // ✅ Check if patient already has insurance
        if (insuranceRepository.existsByPatient(patient)) {
            throw new RuntimeException("You already have insurance. Please update or delete the existing one.");
        }
        
        // Set the patient
        insurance.setPatient(patient);
        
        return insuranceRepository.save(insurance);
    }

    // Patient views own insurance (returns single insurance, not list)
    public Insurance getInsuranceForPatient(User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        
        return insuranceRepository.findByPatient(patient)
                .orElse(null);  // Returns null if no insurance found
    }

    // Patient updates own insurance
    public Insurance updateInsurance(Insurance insurance, User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        
        // Find existing insurance
        Insurance existing = insuranceRepository.findByPatient(patient)
                .orElseThrow(() -> new RuntimeException("No insurance found to update. Please add insurance first."));
        
        // Update fields
        existing.setPolicyNumber(insurance.getPolicyNumber());
        existing.setProvider(insurance.getProvider());
        existing.setValidUntil(insurance.getValidUntil());
        
        return insuranceRepository.save(existing);
    }

    // Patient deletes own insurance
    public void deleteInsurance(User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        
        Insurance existing = insuranceRepository.findByPatient(patient)
                .orElseThrow(() -> new RuntimeException("No insurance found to delete"));
        
        insuranceRepository.delete(existing);
    }

    // Admin views insurance by patient ID
    public Insurance getInsuranceByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        return insuranceRepository.findByPatient(patient)
                .orElse(null);
    }
}