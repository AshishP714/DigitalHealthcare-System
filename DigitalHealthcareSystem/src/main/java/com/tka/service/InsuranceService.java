package com.tka.service;

import org.springframework.stereotype.Service;

import com.tka.entity.Insurance;
import com.tka.entity.Patient;
import com.tka.repository.InsuranceRepository;
import com.tka.repository.PatientRepository;

@Service
public class InsuranceService {

	private final InsuranceRepository insuranceRepository;
	private final PatientRepository patientRepository;

	public InsuranceService(PatientRepository patientRepository, InsuranceRepository insuranceRepository) {
		this.insuranceRepository = insuranceRepository;
		this.patientRepository = patientRepository;
	}

	public Patient addInsuranceToPatient(Long patientId, Insurance insurance) {

		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new RuntimeException("Patient not found"));

		insurance.setPatient(patient);
		patient.setInsurance(insurance);

		insuranceRepository.save(insurance);
		return patientRepository.save(patient);
	}

	public Insurance getInsuranceById(Long insuranceId) {
		return insuranceRepository.findById(insuranceId).orElseThrow(() -> new RuntimeException("Insurance not found"));
	}

	public void deleteInsurance(Long insuranceId) {
		Insurance insurance = insuranceRepository.findById(insuranceId)
				.orElseThrow(() -> new RuntimeException("Insurance not found"));

		Patient patient = insurance.getPatient();

		if (patient != null) {
			patient.setInsurance(null);
			patientRepository.save(patient);
		}

		insuranceRepository.delete(insurance);
	}
}