package com.tka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Appointment;
import com.tka.entity.Doctor;
import com.tka.entity.Patient;

public interface AppointmentRepostory extends JpaRepository<Appointment, Long> {

	List<Appointment> findByPatient(Patient patient);

	List<Appointment> findByDoctor(Doctor doctor);
}