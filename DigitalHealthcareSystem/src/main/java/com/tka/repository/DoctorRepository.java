package com.tka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
