package com.tka.service;

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

    public Insurance addInsuranceForPatient(Insurance insurance, User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        
        if (insuranceRepository.existsByPatient(patient)) {
            throw new RuntimeException("You already have insurance. Please update or delete the existing one.");
        }
        
        insurance.setPatient(patient);
        
        return insuranceRepository.save(insurance);
    }

    public Insurance getInsuranceForPatient(User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        
        return insuranceRepository.findByPatient(patient)
                .orElse(null);  // Returns null if no insurance found
    }

    public Insurance updateInsurance(Insurance insurance, User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
      
        Insurance existing = insuranceRepository.findByPatient(patient)
                .orElseThrow(() -> new RuntimeException("No insurance found to update. Please add insurance first."));
      
        existing.setPolicyNumber(insurance.getPolicyNumber());
        existing.setProvider(insurance.getProvider());
        existing.setValidUntil(insurance.getValidUntil());
        
        return insuranceRepository.save(existing);
    }

    public void deleteInsurance(User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        
        Insurance existing = insuranceRepository.findByPatient(patient)
                .orElseThrow(() -> new RuntimeException("No insurance found to delete"));
        
        insuranceRepository.delete(existing);
    }

    public Insurance getInsuranceByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        return insuranceRepository.findByPatient(patient)
                .orElse(null);
    }
}