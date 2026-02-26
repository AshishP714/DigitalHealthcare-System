package com.tka.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Insurance;
import com.tka.entity.Patient;
import com.tka.entity.User;
import com.tka.repository.InsuranceRepository;
import com.tka.repository.PatientRepository;

@Service
public class PatientService {

	private final PatientRepository patientRepository;

	public PatientService(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	public Patient createPatientProfile(Patient patient, User user) {
		Patient existing = patientRepository.findByUser(user);
		if (existing != null) {
			throw new RuntimeException("Patient profile already exists for this user");
		}

		patient.setUser(user);
		patient.setPatientEmail(user.getUserEmail());
		return patientRepository.save(patient);
	}

	public Patient updateOwnProfile(Patient patient, User user) {
		Patient existing = patientRepository.findByUser(user);
		if (existing == null) {
			throw new RuntimeException("Patient profile not found");
		}

		existing.setPatientName(patient.getPatientName());
		existing.setPatientBirthDate(patient.getPatientBirthDate());
		existing.setPatientGender(patient.getPatientGender());
		existing.setBloodGroup(patient.getBloodGroup());

		return patientRepository.save(existing);
	}

	public Patient getPatientByUserId(Long userId) {
		return patientRepository.findByUser_UserId(userId)
				.orElseThrow(() -> new RuntimeException("Patient profile not found"));
	}

	public Patient createNewPatient(Patient patient) {
		return patientRepository.save(patient);
	}

	public Patient findPatientById(Long id) {
		return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
	}

	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	public void deletePatient(Long patientId) {
		if (!patientRepository.existsById(patientId)) {
			throw new RuntimeException("Patient not found");
		}
		patientRepository.deleteById(patientId);
	}

	public Patient getPatientByUser(User user) {
		Patient patient = patientRepository.findByUser(user);
		if (patient == null) {
			throw new RuntimeException("Patient profile not found for user: " + user.getUserEmail());
		}
		return patient;
	}
}