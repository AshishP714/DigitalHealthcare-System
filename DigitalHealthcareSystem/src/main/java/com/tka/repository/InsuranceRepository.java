package com.tka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Insurance;
import com.tka.entity.Patient;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

	List<Insurance> findByPatient(Patient patient);
}
