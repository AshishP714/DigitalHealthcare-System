package com.tka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Insurance;
import com.tka.entity.Patient;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

	Optional<Insurance> findByPatient(Patient patient);
	
	boolean existsByPatient(Patient patient);
}
