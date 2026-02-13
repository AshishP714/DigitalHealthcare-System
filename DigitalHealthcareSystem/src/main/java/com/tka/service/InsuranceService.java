package com.tka.service;

import java.util.List;

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

    // Patient adds insurance to their profile
    public Insurance addInsuranceForPatient(Insurance insurance, User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        
        insurance.setPatient(patient);
        return insuranceRepository.save(insurance);
    }

    // Patient views own insurance
    public List<Insurance> getInsuranceForPatient(User user) {
        Patient patient = patientRepository.findByUser(user);
        if (patient == null) {
            throw new RuntimeException("Patient profile not found");
        }
        return insuranceRepository.findByPatient(patient);
    }

    // Patient updates own insurance
    public Insurance updateInsurance(Long insuranceId, Insurance insurance, User user) {
        Insurance existing = insuranceRepository.findById(insuranceId)
                .orElseThrow(() -> new RuntimeException("Insurance not found"));
        
        Patient patient = patientRepository.findByUser(user);
        
        // Check ownership
        if (!existing.getPatient().getPatientId().equals(patient.getPatientId())) {
            throw new RuntimeException("Unauthorized: You can only update your own insurance");
        }
        
        existing.setProvider(insurance.getProvider());
        existing.setPolicyNumber(insurance.getPolicyNumber());
        existing.setValidUntil(insurance.getValidUntil());
        
        return insuranceRepository.save(existing);
    }

    // Patient deletes own insurance
    public void deleteInsurance(Long insuranceId, User user) {
        Insurance existing = insuranceRepository.findById(insuranceId)
                .orElseThrow(() -> new RuntimeException("Insurance not found"));
        
        Patient patient = patientRepository.findByUser(user);
        
        // Check ownership
        if (!existing.getPatient().getPatientId().equals(patient.getPatientId())) {
            throw new RuntimeException("Unauthorized: You can only delete your own insurance");
        }
        
        insuranceRepository.deleteById(insuranceId);
    }

    // Admin views all insurance
    public List<Insurance> getAllInsurance() {
        return insuranceRepository.findAll();
    }

    // Admin views insurance by patient ID
    public List<Insurance> getInsuranceByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return insuranceRepository.findByPatient(patient);
    }
}