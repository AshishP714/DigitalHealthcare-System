package com.tka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Insurance;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

}
