package com.tka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Doctor;
import com.tka.entity.User;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	Doctor findByUser(User user);

	Optional<Doctor> findByUser_UserId(Long userId);
}
