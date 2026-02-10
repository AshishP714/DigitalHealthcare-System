package com.tka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
