package com.tka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Appointment;

public interface AppointmentRepostory extends JpaRepository<Appointment, Long> {

}