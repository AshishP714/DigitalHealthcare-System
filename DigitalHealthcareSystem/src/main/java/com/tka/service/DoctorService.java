package com.tka.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Doctor;
import com.tka.repository.DoctorRepository;

@Service
public class DoctorService {

	private final DoctorRepository doctorRepository;

	public DoctorService(DoctorRepository doctorRepository) {
		this.doctorRepository = doctorRepository;
	}

	public Doctor addNewDoctor(Doctor doctor) {
		return doctorRepository.save(doctor);
	}

	public List<Doctor> getAllDoctor() {
		return doctorRepository.findAll();
	}

	public Doctor getDoctorById(Long doctorId) {
		return doctorRepository.findById(doctorId)
				.orElseThrow(() -> new IllegalArgumentException("Doctor not found on id " + doctorId));
	}

	public Doctor updateDoctor(Long doctorId, Doctor updateDoctor) {

		Doctor existingDoctor = doctorRepository.findById(doctorId)
				.orElseThrow(() -> new IllegalArgumentException("Doctor not found oon id " + doctorId));

		existingDoctor.setName(updateDoctor.getName());
		existingDoctor.setEmail(updateDoctor.getEmail());
		existingDoctor.setSpecilization(updateDoctor.getSpecilization());

		return doctorRepository.save(existingDoctor);
	}

	public void deleteDoctor(Long doctorId) {
		if (doctorRepository.existsById(doctorId)) {
			doctorRepository.deleteById(doctorId);
		} else {
			throw new IllegalArgumentException("Doctor not found on id " + doctorId);
		}
	}
}