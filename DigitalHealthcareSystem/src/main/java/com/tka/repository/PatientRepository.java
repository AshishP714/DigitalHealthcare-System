package com.tka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Patient;
import com.tka.entity.User;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	Optional<Patient> findByPatientEmail(String patientEmail);

	Patient findByUser(User user);

	Optional<Patient> findByUser_UserId(Long userId);
}