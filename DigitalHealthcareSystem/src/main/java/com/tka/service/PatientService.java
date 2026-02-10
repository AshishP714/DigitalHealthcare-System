package com.tka.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Insurance;
import com.tka.entity.Patient;
import com.tka.repository.InsuranceRepository;
import com.tka.repository.PatientRepository;

@Service
public class PatientService {

	private final PatientRepository patientRepository;
	private final InsuranceRepository insuranceRepository;

	public PatientService(PatientRepository patientRepository, InsuranceRepository insuranceRepository) {
		this.patientRepository = patientRepository;
		this.insuranceRepository = insuranceRepository;
	}

	public Patient createNewPatient(Patient patient) {
		if (patient.getInsurance() != null) {
			Long insuranceId = patient.getInsurance().getId();
			Insurance insurance = insuranceRepository.findById(insuranceId)
					.orElseThrow(() -> new IllegalArgumentException("Insurance not found with id " + insuranceId));
			patient.setInsurance(insurance);
		} else {
			patient.setInsurance(null);
		}
		return patientRepository.save(patient);
	}

	public List<Patient> getAllPatient() {
		return patientRepository.findAll();
	}

	public Patient findPatientById(Long patientId) {
		return patientRepository.findById(patientId)
				.orElseThrow(() -> new IllegalArgumentException("Patient not found with id " + patientId));
	}

	//================= Update ==================
	public Patient updatePatient(Long patientId, Patient updatePatient) {
		
		Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient not found with id " + patientId));

        existingPatient.setPatientName(updatePatient.getPatientName());
        existingPatient.setPatientGender(updatePatient.getPatientGender());
        existingPatient.setPatientBirthDate(updatePatient.getPatientBirthDate());
        existingPatient.setPatientEmail(updatePatient.getPatientEmail());
        existingPatient.setBloodGroup(updatePatient.getBloodGroup());
        
        if (updatePatient.getInsurance() != null) {
            Long insuranceId = updatePatient.getInsurance().getId();
            Insurance insurance = insuranceRepository.findById(insuranceId)
                    .orElseThrow(() -> new IllegalArgumentException("Insurance not found with id " + insuranceId));
            existingPatient.setInsurance(insurance);
        } else {
            existingPatient.setInsurance(null);
        }

        return patientRepository.save(existingPatient);
	}

	//================= Delete ==================
	public void deletePatient(Long patientId) {
		if (patientRepository.existsById(patientId)) {
			patientRepository.deleteById(patientId);
		} else {
			throw new IllegalArgumentException("Patient not found on id " + patientId);
		}
	}
}